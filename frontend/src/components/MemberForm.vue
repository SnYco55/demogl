<script setup lang="ts">
import { ref } from 'vue'
import type { Member, Role, ServiceListItem } from '@/types/type'

const props = defineProps<{
  editingMember: Member | null
  services: ServiceListItem[]
  roles: Role[]
  initialServiceIds: string[]
  initialRoleIds: number[]
  saving: boolean
}>()

const emit = defineEmits<{
  save: [
    payload: {
      firstname: string
      lastname: string
      start: string
      end: string | null
      serviceIds: string[]
      roleIds: number[]
    },
  ]
  cancel: []
}>()

function toDatetimeLocal(value: string | null | undefined): string {
  if (!value) {
    return ''
  }

  return value.slice(0, 16)
}

const firstname = ref(props.editingMember?.firstname ?? '')
const lastname = ref(props.editingMember?.lastname ?? '')
const start = ref(toDatetimeLocal(props.editingMember?.start))
const end = ref(toDatetimeLocal(props.editingMember?.end))

const selectedServices = ref<string[]>([...props.initialServiceIds])
const selectedRoles = ref<number[]>([...props.initialRoleIds])

const error = ref<string | null>(null)

function submit() {
  const first = firstname.value.trim()
  const last = lastname.value.trim()

  if (!first || !last) {
    error.value = 'Le prénom et le nom sont requis'
    return
  }

  error.value = null

  emit('save', {
    firstname: first,
    lastname: last,
    start: start.value,
    end: end.value || null,
    serviceIds: selectedServices.value,
    roleIds: selectedRoles.value,
  })
}
</script>

<template>
  <div class="space-y-4">
    <div
      v-if="error"
      class="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
    >
      {{ error }}
    </div>

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
          :disabled="saving"
          placeholder="Prénom"
          class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
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
          :disabled="saving"
          placeholder="Nom"
          class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
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
          :disabled="saving"
          class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
        />
      </div>

      <div>
        <label for="member-end" class="mb-2 block text-sm font-medium text-gray-700"> Fin </label>

        <input
          id="member-end"
          v-model="end"
          type="datetime-local"
          :disabled="saving"
          class="w-full rounded-xl border border-gray-300 px-4 py-3 text-sm outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-100 disabled:bg-gray-100 disabled:text-gray-500"
        />
      </div>
    </div>

    <!-- Services -->
    <div>
      <label class="mb-2 block text-sm font-medium text-gray-700"> Services </label>

      <div
        v-if="services.length === 0"
        class="rounded-xl border border-gray-200 px-4 py-3 text-sm text-gray-400"
      >
        Aucun service disponible.
      </div>

      <div
        v-else
        class="grid max-h-40 grid-cols-1 gap-2 overflow-y-auto rounded-xl border border-gray-100 p-2 sm:grid-cols-2"
      >
        <label
          v-for="service in services"
          :key="service.id"
          class="flex items-center gap-2 rounded-xl border border-gray-200 px-4 py-3 text-sm transition hover:bg-gray-50 has-[:checked]:border-gray-400 has-[:checked]:bg-gray-50"
        >
          <input
            type="checkbox"
            :value="service.id"
            v-model="selectedServices"
            :disabled="saving"
            class="h-4 w-4 shrink-0 rounded border-gray-300 text-black focus:ring-2 focus:ring-gray-100 disabled:cursor-not-allowed"
          />

          <span class="truncate text-gray-700">{{ service.name }}</span>
        </label>
      </div>
    </div>

    <!-- Roles -->
    <div>
      <label class="mb-2 block text-sm font-medium text-gray-700"> Rôles </label>

      <div
        v-if="roles.length === 0"
        class="rounded-xl border border-gray-200 px-4 py-3 text-sm text-gray-400"
      >
        Aucun rôle disponible.
      </div>

      <div
        v-else
        class="grid max-h-40 grid-cols-1 gap-2 overflow-y-auto rounded-xl border border-gray-100 p-2 sm:grid-cols-2"
      >
        <label
          v-for="role in roles"
          :key="role.id"
          class="flex items-center gap-2 rounded-xl border border-gray-200 px-4 py-3 text-sm transition hover:bg-gray-50 has-[:checked]:border-gray-400 has-[:checked]:bg-gray-50"
        >
          <input
            type="checkbox"
            :value="role.id"
            v-model="selectedRoles"
            :disabled="saving"
            class="h-4 w-4 shrink-0 rounded border-gray-300 text-black focus:ring-2 focus:ring-gray-100 disabled:cursor-not-allowed"
          />

          <span class="truncate text-gray-700">{{ role.name }}</span>
        </label>
      </div>
    </div>

    <!-- Form actions -->
    <div class="flex justify-end gap-3 pt-2">
      <button
        type="button"
        @click="emit('cancel')"
        :disabled="saving"
        class="rounded-xl border border-gray-200 px-4 py-2.5 text-sm font-medium text-gray-700 transition hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-50"
      >
        Annuler
      </button>

      <button
        type="button"
        @click="submit"
        :disabled="saving"
        class="rounded-xl bg-black px-4 py-2.5 text-sm font-medium text-white transition hover:bg-gray-800 disabled:cursor-not-allowed disabled:opacity-50"
      >
        {{ saving ? 'Enregistrement...' : editingMember ? 'Enregistrer' : 'Ajouter' }}
      </button>
    </div>
  </div>
</template>
