<script setup lang="ts">
import { onMounted, ref } from 'vue'
import ConfirmDeleteModal from '@/components/ConfirmDeleteModal.vue'
import MemberForm from '@/components/MemberForm.vue'
import type { Member, MemberDetails, Role, ServiceListItem } from '@/types/type'
import { API_URL } from '@/config/api.ts'

const members = ref<Member[]>([])
const services = ref<ServiceListItem[]>([])
const roles = ref<Role[]>([])

const showForm = ref(false)
const editingMember = ref<Member | null>(null)

const initialServiceIds = ref<string[]>([])
const initialRoleIds = ref<number[]>([])

const showDeleteModal = ref(false)
const memberToDelete = ref<Member | null>(null)

const loading = ref(false)
const loadingForm = ref(false)
const saving = ref(false)
const deleting = ref(false)
const error = ref<string | null>(null)

async function loadMembers() {
  loading.value = true
  error.value = null

  try {
    const response = await fetch(`${API_URL}/members`)

    if (!response.ok) {
      throw new Error('Impossible de récupérer les membres')
    }

    members.value = await response.json()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    loading.value = false
  }
}

async function loadServices() {
  try {
    const response = await fetch(`${API_URL}/services`)

    if (!response.ok) {
      throw new Error('Impossible de récupérer les services')
    }

    services.value = await response.json()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  }
}

async function loadRoles() {
  try {
    const response = await fetch(`${API_URL}/roles`)

    if (!response.ok) {
      throw new Error('Impossible de récupérer les rôles')
    }

    roles.value = await response.json()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  }
}

function openCreateForm() {
  editingMember.value = null
  initialServiceIds.value = []
  initialRoleIds.value = []
  error.value = null
  showForm.value = true
}

async function openEditForm(member: Member) {
  editingMember.value = member
  initialServiceIds.value = []
  initialRoleIds.value = []
  error.value = null
  showForm.value = true

  loadingForm.value = true

  try {
    const response = await fetch(`${API_URL}/members/${member.id}`)

    if (!response.ok) {
      throw new Error('Impossible de récupérer le détail du membre')
    }

    const details: MemberDetails = await response.json()

    initialServiceIds.value = details.services.map((service) => service.id)
    initialRoleIds.value = details.roles.map((role) => role.id)
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    loadingForm.value = false
  }
}

function closeForm() {
  showForm.value = false
  editingMember.value = null
  initialServiceIds.value = []
  initialRoleIds.value = []
}

async function saveMember(payload: {
  firstname: string
  lastname: string
  start: string
  end: string | null
  serviceIds: string[]
  roleIds: number[]
}) {
  saving.value = true
  error.value = null

  try {
    let response: Response

    if (editingMember.value) {
      response = await fetch(`${API_URL}/members/${editingMember.value.id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      })
    } else {
      response = await fetch(`${API_URL}/members`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      })
    }

    if (!response.ok) {
      let message = 'Une erreur est survenue'

      try {
        const data = await response.json()

        if (data.message) {
          message = data.message
        }
      } catch {}

      throw new Error(message)
    }

    await loadMembers()

    closeForm()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    saving.value = false
  }
}

function openDeleteModal(member: Member) {
  memberToDelete.value = member
  showDeleteModal.value = true
}

function closeDeleteModal() {
  showDeleteModal.value = false
  memberToDelete.value = null
}

async function deleteMember() {
  if (!memberToDelete.value) {
    return
  }

  deleting.value = true
  error.value = null

  try {
    const response = await fetch(`${API_URL}/members/${memberToDelete.value.id}`, {
      method: 'DELETE',
    })

    if (!response.ok) {
      let message = 'Impossible de supprimer le membre'

      try {
        const data = await response.json()

        if (data.message) {
          message = data.message
        }
      } catch {}

      throw new Error(message)
    }

    closeDeleteModal()

    await loadMembers()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    deleting.value = false
  }
}

function formatDate(date: string | null) {
  if (!date) {
    return '—'
  }

  return new Intl.DateTimeFormat('fr-BE', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(new Date(date))
}

onMounted(() => {
  loadMembers()
  loadServices()
  loadRoles()
})
</script>

<template>
  <section class="space-y-6">
    <!-- Header -->
    <header class="flex items-center justify-between">
      <div>
        <h2 class="text-2xl font-bold text-gray-900">Membres</h2>

        <p class="mt-1 text-sm text-gray-500">Gérez les membres de l'université.</p>
      </div>

      <button
        type="button"
        @click="openCreateForm"
        class="flex items-center gap-2 rounded-xl bg-black px-4 py-3 text-sm font-medium text-white shadow-sm transition hover:bg-gray-800"
      >
        <span class="text-lg leading-none">+</span>
        Ajouter
      </button>
    </header>

    <div
      v-if="error"
      class="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
    >
      {{ error }}
    </div>

    <!-- Add / Edit form -->
    <section v-if="showForm" class="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm">
      <div class="mb-5">
        <h3 class="text-lg font-semibold text-gray-900">
          {{ editingMember ? 'Modifier le membre' : 'Ajouter un membre' }}
        </h3>

        <p class="mt-1 text-sm text-gray-500">
          {{
            editingMember
              ? 'Modifiez les informations du membre.'
              : 'Entrez les informations du nouveau membre.'
          }}
        </p>
      </div>

      <div v-if="loadingForm" class="py-6 text-center text-sm text-gray-500">
        Chargement des informations du membre...
      </div>

      <MemberForm
        v-else
        :key="editingMember?.id ?? 'new'"
        :editing-member="editingMember"
        :services="services"
        :roles="roles"
        :initial-service-ids="initialServiceIds"
        :initial-role-ids="initialRoleIds"
        :saving="saving"
        @save="saveMember"
        @cancel="closeForm"
      />
    </section>

    <!-- Loading -->
    <section
      v-if="loading"
      class="rounded-2xl border border-gray-200 bg-white p-8 text-center text-sm text-gray-500 shadow-sm"
    >
      Chargement des membres...
    </section>

    <!-- Member list -->
    <section v-else class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
      <div v-if="members.length === 0" class="p-8 text-center text-sm text-gray-500">
        Aucun membre.
      </div>

      <div v-else class="divide-y divide-gray-100">
        <div
          v-for="member in members"
          :key="member.id"
          class="flex items-center justify-between gap-6 px-6 py-5 transition hover:bg-gray-50"
        >
          <!-- Member information -->
          <div class="min-w-0">
            <h3 class="font-semibold text-gray-900">
              {{ member.firstname }} {{ member.lastname }}
            </h3>

            <div class="mt-1 flex flex-wrap gap-x-4 gap-y-1 text-sm text-gray-400">
              <span> #{{ member.id }} </span>

              <span> Début : {{ formatDate(member.start) }} </span>

              <span> Fin : {{ formatDate(member.end) }} </span>
            </div>
          </div>

          <!-- Actions -->
          <div class="flex shrink-0 items-center gap-2">
            <button
              type="button"
              @click="openEditForm(member)"
              class="rounded-lg border border-gray-200 px-3 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-100"
            >
              Modifier
            </button>

            <button
              type="button"
              @click="openDeleteModal(member)"
              :disabled="deleting"
              class="rounded-lg border border-red-200 px-3 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50 disabled:cursor-not-allowed disabled:opacity-50"
            >
              Supprimer
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Delete confirmation -->
    <ConfirmDeleteModal
      :visible="showDeleteModal"
      title="Supprimer le membre"
      :message="
        memberToDelete
          ? `Êtes-vous sûr de vouloir supprimer ${memberToDelete.firstname} ${memberToDelete.lastname} ?`
          : ''
      "
      @cancel="closeDeleteModal"
      @confirm="deleteMember"
    />
  </section>
</template>
