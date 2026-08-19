<script setup lang="ts">
import { ref } from 'vue'
import ConfirmDeleteModal from '@/components/ConfirmDeleteModal.vue'
import type { Type, Role, Service } from '@/types/type.ts'

const members = ref<Type[]>([
  {
    id: 1,
    firstname: 'Jean',
    lastname: 'Dupont',
    start: '2026-01-01T00:00:00',
    end: null,
    createdAt: '2026-01-01T00:00:00',
  },
  {
    id: 2,
    firstname: 'Marie',
    lastname: 'Martin',
    start: '2026-02-01T00:00:00',
    end: null,
    createdAt: '2026-02-01T00:00:00',
  },
])

const services = ref<Service[]>([])
const roles = ref<Role[]>([])

const showForm = ref(false)
const editingMember = ref<Type | null>(null)

const showDeleteModal = ref(false)
const memberToDelete = ref<Type | null>(null)

const firstname = ref('')
const lastname = ref('')
const start = ref('')
const end = ref('')

const selectedServices = ref<string[]>([])
const selectedRoles = ref<number[]>([])

function openCreateForm() {
  editingMember.value = null

  firstname.value = ''
  lastname.value = ''
  start.value = ''
  end.value = ''

  selectedServices.value = []
  selectedRoles.value = []

  showForm.value = true
}

function openEditForm(member: Type) {
  editingMember.value = member

  firstname.value = member.firstname
  lastname.value = member.lastname
  start.value = member.start
  end.value = member.end ?? ''

  selectedServices.value = []
  selectedRoles.value = []

  showForm.value = true
}

function closeForm() {
  showForm.value = false
  editingMember.value = null

  firstname.value = ''
  lastname.value = ''
  start.value = ''
  end.value = ''

  selectedServices.value = []
  selectedRoles.value = []
}

function saveMember() {
  if (!firstname.value.trim() || !lastname.value.trim() || !start.value) {
    return
  }

  if (editingMember.value) {
    editingMember.value.firstname = firstname.value.trim()
    editingMember.value.lastname = lastname.value.trim()
    editingMember.value.start = start.value
    editingMember.value.end = end.value || null
  } else {
    members.value.push({
      id: Date.now(),
      firstname: firstname.value.trim(),
      lastname: lastname.value.trim(),
      start: start.value,
      end: end.value || null,
      createdAt: new Date().toISOString(),
    })
  }

  closeForm()
}

function openDeleteModal(member: Type) {
  memberToDelete.value = member
  showDeleteModal.value = true
}

function closeDeleteModal() {
  showDeleteModal.value = false
  memberToDelete.value = null
}

function deleteMember() {
  if (!memberToDelete.value) {
    return
  }

  members.value = members.value.filter((member) => member.id !== memberToDelete.value!.id)

  closeDeleteModal()
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

      <div class="space-y-4">
        <!-- Firstname / Lastname -->
        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <div>
            <label for="member-firstname" class="mb-2 block text-sm font-medium text-gray-700">
              Prénom
            </label>

            <input
              id="member-firstname"
              v-model="firstname"
              type="text"
              placeholder="Prénom"
              class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100"
            />
          </div>

          <div>
            <label for="member-lastname" class="mb-2 block text-sm font-medium text-gray-700">
              Nom
            </label>

            <input
              id="member-lastname"
              v-model="lastname"
              type="text"
              placeholder="Nom"
              class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100"
            />
          </div>
        </div>

        <!-- Dates -->
        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <div>
            <label for="member-start" class="mb-2 block text-sm font-medium text-gray-700">
              Début
            </label>

            <input
              id="member-start"
              v-model="start"
              type="datetime-local"
              class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100"
            />
          </div>

          <div>
            <label for="member-end" class="mb-2 block text-sm font-medium text-gray-700">
              Fin
            </label>

            <input
              id="member-end"
              v-model="end"
              type="datetime-local"
              class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100"
            />
          </div>
        </div>

        <!-- Services -->
        <div>
          <label for="member-services" class="mb-2 block text-sm font-medium text-gray-700">
            Services
          </label>

          <select
            id="member-services"
            v-model="selectedServices"
            multiple
            class="min-h-32 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100"
          >
            <option v-for="service in services" :key="service.id" :value="service.id">
              {{ service.name }}
            </option>
          </select>

          <p class="mt-1 text-xs text-gray-400">
            Maintenez Ctrl (ou Cmd) pour sélectionner plusieurs services.
          </p>
        </div>

        <!-- Roles -->
        <div>
          <label for="member-roles" class="mb-2 block text-sm font-medium text-gray-700">
            Rôles
          </label>

          <select
            id="member-roles"
            v-model="selectedRoles"
            multiple
            class="min-h-32 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100"
          >
            <option v-for="role in roles" :key="role.id" :value="role.id">
              {{ role.name }}
            </option>
          </select>

          <p class="mt-1 text-xs text-gray-400">
            Maintenez Ctrl (ou Cmd) pour sélectionner plusieurs rôles.
          </p>
        </div>

        <!-- Form actions -->
        <div class="flex justify-end gap-3 pt-2">
          <button
            type="button"
            @click="closeForm"
            class="rounded-xl border border-gray-200 px-4 py-2.5 text-sm font-medium text-gray-700 transition hover:bg-gray-50"
          >
            Annuler
          </button>

          <button
            type="button"
            @click="saveMember"
            class="rounded-xl bg-black px-4 py-2.5 text-sm font-medium text-white transition hover:bg-gray-800"
          >
            {{ editingMember ? 'Enregistrer' : 'Ajouter' }}
          </button>
        </div>
      </div>
    </section>

    <!-- Type list -->
    <section class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
      <div v-if="members.length === 0" class="p-8 text-center text-sm text-gray-500">
        Aucun membre.
      </div>

      <div v-else class="divide-y divide-gray-100">
        <div
          v-for="member in members"
          :key="member.id"
          class="flex items-center justify-between gap-6 px-6 py-5 transition hover:bg-gray-50"
        >
          <!-- Type information -->
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
              class="rounded-lg border border-red-200 px-3 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50"
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
