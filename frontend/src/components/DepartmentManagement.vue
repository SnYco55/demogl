<script setup lang="ts">
import { onMounted, ref } from 'vue'
import ConfirmDeleteModal from '@/components/ConfirmDeleteModal.vue'
import type {
  Department,
  DepartmentCreateRequest,
  DepartmentPatchRequest,
} from '@/types/type'

const departments = ref<Department[]>([])

const showForm = ref(false)
const editingDepartment = ref<Department | null>(null)
const departmentToDelete = ref<Department | null>(null)

const departmentId = ref('')
const facultyId = ref('')

const loading = ref(false)
const saving = ref(false)
const deleting = ref(false)
const error = ref<string | null>(null)

async function loadDepartments() {
  loading.value = true
  error.value = null

  try {
    const response = await fetch('http://localhost:8080/departments')

    if (!response.ok) {
      throw new Error('Impossible de récupérer les départements')
    }

    departments.value = await response.json()
  } catch (err) {
    error.value =
        err instanceof Error
            ? err.message
            : 'Une erreur est survenue'
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingDepartment.value = null
  departmentId.value = ''
  facultyId.value = ''
  error.value = null
  showForm.value = true
}

function openEditForm(department: Department) {
  editingDepartment.value = department
  departmentId.value = department.id
  facultyId.value = department.facultyId
  error.value = null
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  editingDepartment.value = null
  departmentId.value = ''
  facultyId.value = ''
}

async function saveDepartment() {
  const id = departmentId.value.trim()
  const faculty = facultyId.value.trim()

  if (!editingDepartment.value && !id) {
    error.value = 'L’identifiant du département est requis'
    return
  }

  if (!faculty) {
    error.value = 'L’identifiant de la faculté est requis'
    return
  }

  saving.value = true
  error.value = null

  try {
    let response: Response

    if (editingDepartment.value) {
      const body: DepartmentPatchRequest = {
        facultyId: faculty,
      }

      response = await fetch(
          `http://localhost:8080/departments/${editingDepartment.value.id}`,
          {
            method: 'PATCH',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
          },
      )
    } else {
      const body: DepartmentCreateRequest = {
        id,
        facultyId: faculty,
      }

      response = await fetch(
          'http://localhost:8080/departments',
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
          },
      )
    }

    if (!response.ok) {
      let message = 'Une erreur est survenue'

      try {
        const data = await response.json()

        if (data.message) {
          message = data.message
        }
      } catch {
      }

      throw new Error(message)
    }

    await loadDepartments()

    closeForm()
  } catch (err) {
    error.value =
        err instanceof Error
            ? err.message
            : 'Une erreur est survenue'
  } finally {
    saving.value = false
  }
}

function askDeleteDepartment(department: Department) {
  departmentToDelete.value = department
}

async function confirmDeleteDepartment() {
  if (!departmentToDelete.value) {
    return
  }

  deleting.value = true
  error.value = null

  try {
    const response = await fetch(
        `http://localhost:8080/departments/${departmentToDelete.value.id}`,
        {
          method: 'DELETE',
        },
    )

    if (!response.ok) {
      let message = 'Impossible de supprimer le département'

      try {
        const data = await response.json()

        if (data.message) {
          message = data.message
        }
      } catch {
      }

      throw new Error(message)
    }

    departmentToDelete.value = null

    await loadDepartments()
  } catch (err) {
    error.value =
        err instanceof Error
            ? err.message
            : 'Une erreur est survenue'
  } finally {
    deleting.value = false
  }
}

onMounted(() => {
  loadDepartments()
})
</script>

<template>
  <section class="space-y-6">

    <!-- Header -->
    <header class="flex items-center justify-between">
      <div>
        <h2 class="text-2xl font-bold text-gray-900">
          Départements
        </h2>

        <p class="mt-1 text-sm text-gray-500">
          Gérez les départements de l'université.
        </p>
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
    <section
        v-if="showForm"
        class="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm"
    >
      <div class="mb-5">
        <h3 class="text-lg font-semibold text-gray-900">
          {{
            editingDepartment
                ? 'Modifier le département'
                : 'Ajouter un département'
          }}
        </h3>

        <p class="mt-1 text-sm text-gray-500">
          {{
            editingDepartment
                ? 'Modifiez les informations du département.'
                : 'Entrez les informations du nouveau département.'
          }}
        </p>
      </div>

      <div class="space-y-4">

        <!-- ID -->
        <div>
          <label
              for="department-id"
              class="mb-2 block text-sm font-medium text-gray-700"
          >
            ID
          </label>

          <input
              id="department-id"
              v-model="departmentId"
              type="text"
              :disabled="!!editingDepartment || saving"
              placeholder="Ex. med"
              class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
          />
        </div>

        <!-- Faculty ID -->
        <div>
          <label
              for="faculty-id"
              class="mb-2 block text-sm font-medium text-gray-700"
          >
            ID de la faculté
          </label>

          <input
              id="faculty-id"
              v-model="facultyId"
              type="text"
              :disabled="saving"
              placeholder="Ex. fmpb"
              class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
              @keyup.enter="saveDepartment"
          />
        </div>

        <!-- Buttons -->
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
              @click="saveDepartment"
              :disabled="saving"
              class="rounded-xl bg-black px-4 py-2.5 text-sm font-medium text-white transition hover:bg-gray-800 disabled:cursor-not-allowed disabled:opacity-50"
          >
            {{
              saving
                  ? 'Enregistrement...'
                  : editingDepartment
                      ? 'Enregistrer'
                      : 'Ajouter'
            }}
          </button>
        </div>
      </div>
    </section>

    <!-- Loading -->
    <section
        v-if="loading"
        class="rounded-2xl border border-gray-200 bg-white p-8 text-center text-sm text-gray-500 shadow-sm"
    >
      Chargement des départements...
    </section>

    <!-- List -->
    <section
        v-else
        class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm"
    >
      <div
          v-if="departments.length === 0"
          class="p-8 text-center text-sm text-gray-500"
      >
        Aucun département.
      </div>

      <div
          v-else
          class="divide-y divide-gray-100"
      >
        <div
            v-for="department in departments"
            :key="department.id"
            class="flex items-center justify-between gap-6 px-6 py-5 transition hover:bg-gray-50"
        >
          <div class="min-w-0">
            <h3 class="font-semibold text-gray-900">
              {{ department.id }}
            </h3>

            <p class="mt-1 text-sm text-gray-400">
              Faculté : {{ department.facultyId }}
            </p>
          </div>

          <div class="flex shrink-0 items-center gap-2">
            <button
                type="button"
                @click="openEditForm(department)"
                class="rounded-xl border border-gray-200 px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-100"
            >
              Modifier
            </button>

            <button
                type="button"
                @click="askDeleteDepartment(department)"
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
        :visible="departmentToDelete !== null"
        :message="`Êtes-vous sûr de vouloir supprimer le département « ${departmentToDelete?.id ?? ''} » ?`"
        @cancel="departmentToDelete = null"
        @confirm="confirmDeleteDepartment"
    />

  </section>
</template>