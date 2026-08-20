<script setup lang="ts">
import { onMounted, ref } from 'vue'
import ConfirmDeleteModal from '@/components/ConfirmDeleteModal.vue'
import type { ServiceListItem, ServiceCreateRequest, ServicePatchRequest } from '@/types/type'
import { API_URL } from '@/config/api.ts'

const services = ref<ServiceListItem[]>([])

const showForm = ref(false)
const editingService = ref<ServiceListItem | null>(null)
const serviceToDelete = ref<ServiceListItem | null>(null)

const serviceId = ref('')
const serviceName = ref('')
const departmentId = ref('')
const directorId = ref('')

const loading = ref(false)
const saving = ref(false)
const deleting = ref(false)
const error = ref<string | null>(null)

async function loadServices() {
  loading.value = true
  error.value = null

  try {
    const response = await fetch(`${API_URL}/services`)

    if (!response.ok) {
      throw new Error('Impossible de récupérer les services')
    }

    services.value = await response.json()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingService.value = null
  serviceId.value = ''
  serviceName.value = ''
  departmentId.value = ''
  directorId.value = ''
  error.value = null
  showForm.value = true
}

function openEditForm(service: ServiceListItem) {
  editingService.value = service
  serviceId.value = service.id
  serviceName.value = service.name
  departmentId.value = service.departmentId
  directorId.value = String(service.directorId)
  error.value = null
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  editingService.value = null
  serviceId.value = ''
  serviceName.value = ''
  departmentId.value = ''
  directorId.value = ''
}

async function saveService() {
  const id = serviceId.value.trim()
  const name = serviceName.value.trim()
  const department = departmentId.value.trim()
  const director = String(directorId.value).trim()

  if (!editingService.value && !id) {
    error.value = 'L’identifiant du service est requis'
    return
  }

  if (!name) {
    error.value = 'Le nom du service est requis'
    return
  }

  if (!department) {
    error.value = 'L’identifiant du département est requis'
    return
  }

  if (!director || Number.isNaN(Number(director))) {
    error.value = 'L’identifiant du directeur est requis'
    return
  }

  saving.value = true
  error.value = null

  try {
    let response: Response

    if (editingService.value) {
      const body: ServicePatchRequest = {
        name,
        departmentId: department,
        directorId: Number(director),
      }

      response = await fetch(`${API_URL}/services/${editingService.value.id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
      })
    } else {
      const body: ServiceCreateRequest = {
        id,
        name,
        departmentId: department,
        directorId: Number(director),
      }

      response = await fetch(`${API_URL}/services`, {
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

    await loadServices()

    closeForm()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    saving.value = false
  }
}

function askDeleteService(service: ServiceListItem) {
  serviceToDelete.value = service
}

async function confirmDeleteService() {
  if (!serviceToDelete.value) {
    return
  }

  deleting.value = true
  error.value = null

  try {
    const response = await fetch(`${API_URL}/services/${serviceToDelete.value.id}`, {
      method: 'DELETE',
    })

    if (!response.ok) {
      let message = 'Impossible de supprimer le service'

      try {
        const data = await response.json()

        if (data.message) {
          message = data.message
        }
      } catch {}

      throw new Error(message)
    }

    serviceToDelete.value = null

    await loadServices()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Une erreur est survenue'
  } finally {
    deleting.value = false
  }
}

onMounted(() => {
  loadServices()
})
</script>

<template>
  <section class="space-y-6">
    <!-- Header -->
    <header class="flex items-center justify-between">
      <div>
        <h2 class="text-2xl font-bold text-gray-900">Services</h2>

        <p class="mt-1 text-sm text-gray-500">Gérez les services de l'université.</p>
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
          {{ editingService ? 'Modifier le service' : 'Ajouter un service' }}
        </h3>

        <p class="mt-1 text-sm text-gray-500">
          {{
            editingService
              ? 'Modifiez les informations du service.'
              : 'Entrez les informations du nouveau service.'
          }}
        </p>
      </div>

      <div class="space-y-4">
        <!-- ID -->
        <div>
          <label for="service-id" class="mb-2 block text-sm font-medium text-gray-700"> ID </label>

          <input
            id="service-id"
            v-model="serviceId"
            type="text"
            :disabled="!!editingService || saving"
            placeholder="Ex. S852"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
          />
        </div>

        <!-- Name -->
        <div>
          <label for="service-name" class="mb-2 block text-sm font-medium text-gray-700">
            Nom
          </label>

          <input
            id="service-name"
            v-model="serviceName"
            type="text"
            :disabled="saving"
            placeholder="Ex. Service de Génie Logiciel"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
          />
        </div>

        <!-- Department ID -->
        <div>
          <label for="department-id" class="mb-2 block text-sm font-medium text-gray-700">
            Nom du département
          </label>

          <input
            id="department-id"
            v-model="departmentId"
            type="text"
            :disabled="saving"
            placeholder="Ex. informatique"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
          />
        </div>

        <!-- Director ID -->
        <div>
          <label for="director-id" class="mb-2 block text-sm font-medium text-gray-700">
            ID du directeur
          </label>

          <input
            id="director-id"
            v-model="directorId"
            type="number"
            :disabled="saving"
            placeholder="Ex. 1"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
            @keyup.enter="saveService"
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
            @click="saveService"
            :disabled="saving"
            class="rounded-xl bg-black px-4 py-2.5 text-sm font-medium text-white transition hover:bg-gray-800 disabled:cursor-not-allowed disabled:opacity-50"
          >
            {{ saving ? 'Enregistrement...' : editingService ? 'Enregistrer' : 'Ajouter' }}
          </button>
        </div>
      </div>
    </section>

    <!-- Loading -->
    <section
      v-if="loading"
      class="rounded-2xl border border-gray-200 bg-white p-8 text-center text-sm text-gray-500 shadow-sm"
    >
      Chargement des services...
    </section>

    <!-- List -->
    <section v-else class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
      <div v-if="services.length === 0" class="p-8 text-center text-sm text-gray-500">
        Aucun service.
      </div>

      <div v-else class="divide-y divide-gray-100">
        <div
          v-for="service in services"
          :key="service.id"
          class="flex items-center justify-between gap-6 px-6 py-5 transition hover:bg-gray-50"
        >
          <div class="min-w-0">
            <h3 class="font-semibold text-gray-900">
              <div class="font-normal text-gray-400">{{ service.id }}</div>
              {{ service.name }}
            </h3>

            <p class="mt-1 text-sm text-gray-400">
              Département : {{ service.departmentId }} · Directeur #{{ service.directorId }}
            </p>
          </div>

          <div class="flex shrink-0 items-center gap-2">
            <button
              type="button"
              @click="openEditForm(service)"
              class="rounded-xl border border-gray-200 px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-100"
            >
              Modifier
            </button>

            <button
              type="button"
              @click="askDeleteService(service)"
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
      :visible="serviceToDelete !== null"
      :message="`Êtes-vous sûr de vouloir supprimer le service « ${serviceToDelete?.name ?? ''} » ?`"
      @cancel="serviceToDelete = null"
      @confirm="confirmDeleteService"
    />
  </section>
</template>
