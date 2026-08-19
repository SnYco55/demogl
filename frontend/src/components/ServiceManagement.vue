<script setup lang="ts">
import { ref } from 'vue'
import ConfirmDeleteModal from '@/components/ConfirmDeleteModal.vue'

interface Service {
  id: string
  name: string
  departmentId: string
  directorId: number | null
}

const services = ref<Service[]>([
  {
    id: 'cardio',
    name: 'Cardiologie',
    departmentId: 'med',
    directorId: 1,
  },
  {
    id: 'dermato',
    name: 'Dermatologie',
    departmentId: 'med',
    directorId: null,
  },
])

const showForm = ref(false)
const editingService = ref<Service | null>(null)
const serviceToDelete = ref<Service | null>(null)

const serviceId = ref('')
const serviceName = ref('')
const departmentId = ref('')
const directorId = ref('')

function openCreateForm() {
  editingService.value = null
  serviceId.value = ''
  serviceName.value = ''
  departmentId.value = ''
  directorId.value = ''
  showForm.value = true
}

function openEditForm(service: Service) {
  editingService.value = service
  serviceId.value = service.id
  serviceName.value = service.name
  departmentId.value = service.departmentId
  directorId.value = service.directorId?.toString() ?? ''
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  editingService.value = null
}

function saveService() {
  if (!serviceId.value.trim() || !serviceName.value.trim() || !departmentId.value.trim()) {
    return
  }

  const parsedDirectorId = directorId.value.trim() ? Number(directorId.value) : null

  if (editingService.value) {
    editingService.value.name = serviceName.value.trim()
    editingService.value.departmentId = departmentId.value.trim()
    editingService.value.directorId = parsedDirectorId
  } else {
    services.value.push({
      id: serviceId.value.trim(),
      name: serviceName.value.trim(),
      departmentId: departmentId.value.trim(),
      directorId: parsedDirectorId,
    })
  }

  closeForm()
}

function askDeleteService(service: Service) {
  serviceToDelete.value = service
}

function confirmDeleteService() {
  if (!serviceToDelete.value) {
    return
  }

  services.value = services.value.filter((service) => service.id !== serviceToDelete.value?.id)

  serviceToDelete.value = null
}
</script>

<template>
  <section class="space-y-6">
    <header class="flex items-center justify-between">
      <div>
        <h2 class="text-2xl font-bold text-gray-900">Services</h2>

        <p class="mt-1 text-sm text-gray-500">Gérez les services de l'université.</p>
      </div>

      <button
        type="button"
        @click="openCreateForm"
        class="flex items-center gap-2 rounded-xl bg-black px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-gray-800"
      >
        <span class="text-lg">+</span>
        Ajouter
      </button>
    </header>

    <section v-if="showForm" class="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm">
      <h3 class="text-lg font-semibold text-gray-900">
        {{ editingService ? 'Modifier le service' : 'Ajouter un service' }}
      </h3>

      <div class="mt-5 space-y-4">
        <div>
          <label class="mb-2 block text-sm font-medium text-gray-700"> ID </label>

          <input
            v-model="serviceId"
            type="text"
            :disabled="!!editingService"
            placeholder="Ex. cardio"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm disabled:bg-gray-100"
          />
        </div>

        <div>
          <label class="mb-2 block text-sm font-medium text-gray-700"> Nom </label>

          <input
            v-model="serviceName"
            type="text"
            placeholder="Ex. Cardiologie"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm"
          />
        </div>

        <div>
          <label class="mb-2 block text-sm font-medium text-gray-700"> ID du département </label>

          <input
            v-model="departmentId"
            type="text"
            placeholder="Ex. med"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm"
          />
        </div>

        <div>
          <label class="mb-2 block text-sm font-medium text-gray-700"> ID du directeur </label>

          <input
            v-model="directorId"
            type="number"
            placeholder="Ex. 1"
            class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm"
          />
        </div>

        <div class="flex justify-end gap-3">
          <button
            type="button"
            @click="closeForm"
            class="rounded-xl border border-gray-200 px-4 py-2.5 text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Annuler
          </button>

          <button
            type="button"
            @click="saveService"
            class="rounded-xl bg-black px-4 py-2.5 text-sm font-medium text-white hover:bg-gray-800"
          >
            {{ editingService ? 'Enregistrer' : 'Ajouter' }}
          </button>
        </div>
      </div>
    </section>

    <section class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
      <div v-if="services.length === 0" class="p-8 text-center text-sm text-gray-500">
        Aucun service.
      </div>

      <div v-else class="divide-y divide-gray-100">
        <div
          v-for="service in services"
          :key="service.id"
          class="flex items-center justify-between gap-6 px-6 py-5 hover:bg-gray-50"
        >
          <div class="min-w-0">
            <h3 class="font-semibold text-gray-900">
              {{ service.name }}
            </h3>

            <p class="mt-1 text-sm text-gray-400">
              {{ service.id }} · Département : {{ service.departmentId }}
            </p>
          </div>

          <div class="flex shrink-0 gap-2">
            <button
              type="button"
              @click="openEditForm(service)"
              class="rounded-xl border border-gray-200 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-100"
            >
              Modifier
            </button>

            <button
              type="button"
              @click="askDeleteService(service)"
              class="rounded-xl border border-red-200 px-4 py-2 text-sm font-medium text-red-600 hover:bg-red-50"
            >
              Supprimer
            </button>
          </div>
        </div>
      </div>
    </section>

    <ConfirmDeleteModal
      :visible="serviceToDelete !== null"
      :message="`Êtes-vous sûr de vouloir supprimer le service « ${serviceToDelete?.name ?? ''} » ?`"
      @cancel="serviceToDelete = null"
      @confirm="confirmDeleteService"
    />
  </section>
</template>
