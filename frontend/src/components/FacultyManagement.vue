<script setup lang="ts">
import { onMounted, ref } from 'vue'
import ConfirmDeleteModal from '@/components/ConfirmDeleteModal.vue'
import type { Faculty, FacultyCreateRequest, FacultyPatchRequest } from '@/types/type.ts'
import { API_URL } from "@/config/api.ts";

const faculties = ref<Faculty[]>([])

const showForm = ref(false)
const editingFaculty = ref<Faculty | null>(null)
const facultyToDelete = ref<Faculty | null>(null)

const facultyId = ref('')
const facultyName = ref('')

const loading = ref(false)
const saving = ref(false)
const deleting = ref(false)
const error = ref<string | null>(null)

async function loadFaculties() {
  loading.value = true
  error.value = null

  try {
    const response = await fetch(`${API_URL}/faculties`)

    if (!response.ok) {
      throw new Error('Impossible de récupérer les facultés')
    }

    faculties.value = await response.json()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingFaculty.value = null
  facultyId.value = ''
  facultyName.value = ''
  error.value = null
  showForm.value = true
}

function openEditForm(faculty: Faculty) {
  editingFaculty.value = faculty
  facultyId.value = faculty.id
  facultyName.value = faculty.name
  error.value = null
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  editingFaculty.value = null
  facultyId.value = ''
  facultyName.value = ''
}

async function saveFaculty() {
  const id = facultyId.value.trim()
  const name = facultyName.value.trim()

  if (!editingFaculty.value && !id) {
    error.value = 'L’identifiant de la faculté est requis'
    return
  }

  if (!name) {
    error.value = 'Le nom de la faculté est requis'
    return
  }

  saving.value = true
  error.value = null

  try {
    let response: Response

    if (editingFaculty.value) {
      const body: FacultyPatchRequest = {
        name,
      }

      response = await fetch(`${API_URL}/faculties/${editingFaculty.value.id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
      })
    } else {
      const body: FacultyCreateRequest = {
        id,
        name,
      }

      response = await fetch(`${API_URL}/faculties`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
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

    await loadFaculties()

    closeForm()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    saving.value = false
  }
}

function askDeleteFaculty(faculty: Faculty) {
  facultyToDelete.value = faculty
}

async function confirmDeleteFaculty() {
  if (!facultyToDelete.value) {
    return
  }

  deleting.value = true
  error.value = null

  try {
    const response = await fetch(`${API_URL}/faculties/${facultyToDelete.value.id}`, {
      method: 'DELETE',
    })

    if (!response.ok) {
      let message = 'Impossible de supprimer la faculté'

      try {
        const data = await response.json()

        if (data.message) {
          message = data.message
        }
      } catch {}

      throw new Error(message)
    }

    facultyToDelete.value = null

    await loadFaculties()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    deleting.value = false
  }
}

onMounted(() => {
  loadFaculties()
})
</script>

<template>
  <section class="space-y-6">
    <!-- Header -->
    <header class="flex items-center justify-between">
      <div>
        <h2 class="text-2xl font-bold text-gray-900">Facultés</h2>

        <p class="mt-1 text-sm text-gray-500">Gérez les facultés de l'université.</p>
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

    <!-- Error -->
    <div
      v-if="error"
      class="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
    >
      {{ error }}
    </div>

    <!-- Form -->
    <section v-if="showForm" class="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm">
      <div class="mb-5">
        <h3 class="text-lg font-semibold text-gray-900">
          {{ editingFaculty ? 'Modifier la faculté' : 'Ajouter une faculté' }}
        </h3>

        <p class="mt-1 text-sm text-gray-500">
          {{
            editingFaculty
              ? 'Modifiez les informations de la faculté.'
              : 'Entrez les informations de la nouvelle faculté.'
          }}
        </p>
      </div>

      <div class="space-y-4">
        <!-- ID -->
        <div>
          <label for="faculty-id" class="mb-2 block text-sm font-medium text-gray-700"> ID </label>

          <input
            id="faculty-id"
            v-model="facultyId"
            type="text"
            :disabled="!!editingFaculty || saving"
            placeholder="Ex. fmpb"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
          />
        </div>

        <!-- Name -->
        <div>
          <label for="faculty-name" class="mb-2 block text-sm font-medium text-gray-700">
            Nom
          </label>

          <input
            id="faculty-name"
            v-model="facultyName"
            type="text"
            placeholder="Ex. Faculté de Médecine"
            :disabled="saving"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100"
            @keyup.enter="saveFaculty"
          />
        </div>

        <div class="flex justify-end gap-3">
          <button
            type="button"
            @click="closeForm"
            :disabled="saving"
            class="rounded-xl border border-gray-200 px-4 py-2.5 text-sm font-medium text-gray-700 transition hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-50"
          >
            Annuler
          </button>

          <button
            type="button"
            @click="saveFaculty"
            :disabled="saving"
            class="rounded-xl bg-black px-4 py-2.5 text-sm font-medium text-white transition hover:bg-gray-800 disabled:cursor-not-allowed disabled:opacity-50"
          >
            {{ saving ? 'Enregistrement...' : editingFaculty ? 'Enregistrer' : 'Ajouter' }}
          </button>
        </div>
      </div>
    </section>

    <!-- Loading -->
    <section
      v-if="loading"
      class="rounded-2xl border border-gray-200 bg-white p-8 text-center text-sm text-gray-500 shadow-sm"
    >
      Chargement des facultés...
    </section>

    <!-- List -->
    <section v-else class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
      <div v-if="faculties.length === 0" class="p-8 text-center text-sm text-gray-500">
        Aucune faculté.
      </div>

      <div v-else class="divide-y divide-gray-100">
        <div
          v-for="faculty in faculties"
          :key="faculty.id"
          class="flex items-center justify-between gap-6 px-6 py-5 transition hover:bg-gray-50"
        >
          <div class="min-w-0">
            <h3 class="font-semibold text-gray-900">
              {{ faculty.name }}
            </h3>

            <p class="mt-1 text-sm text-gray-400">ID : {{ faculty.id }}</p>
          </div>

          <div class="flex shrink-0 items-center gap-2">
            <button
              type="button"
              @click="openEditForm(faculty)"
              class="rounded-xl border border-gray-200 px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-100"
            >
              Modifier
            </button>

            <button
              type="button"
              @click="askDeleteFaculty(faculty)"
              :disabled="deleting"
              class="rounded-xl border border-red-200 px-4 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50 disabled:cursor-not-allowed disabled:opacity-50"
            >
              Supprimer
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Delete confirmation -->
    <ConfirmDeleteModal
      :visible="facultyToDelete !== null"
      :message="`Êtes-vous sûr de vouloir supprimer « ${facultyToDelete?.name ?? ''} » ?`"
      @cancel="facultyToDelete = null"
      @confirm="confirmDeleteFaculty"
    />
  </section>
</template>
