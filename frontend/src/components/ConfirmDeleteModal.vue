<script setup lang="ts">
defineProps<{
  visible: boolean
  title?: string
  message?: string
}>()

const emit = defineEmits<{
  cancel: []
  confirm: []
}>()
</script>

<template>
  <div
      v-if="visible"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4"
  >
    <div
        class="w-full max-w-md rounded-2xl border border-gray-200 bg-white p-6 shadow-xl"
    >
      <h2 class="text-lg font-semibold text-gray-900">
        {{ title ?? 'Confirmer la suppression' }}
      </h2>

      <p class="mt-2 text-sm leading-6 text-gray-500">
        {{ message ?? 'Êtes-vous sûr de vouloir supprimer cet élément ?' }}
      </p>

      <div class="mt-6 flex justify-end gap-3">
        <button
            type="button"
            @click="emit('cancel')"
            class="rounded-xl border border-gray-200 px-4 py-2.5 text-sm font-medium text-gray-700 transition hover:bg-gray-50"
        >
          Annuler
        </button>

        <button
            type="button"
            @click="emit('confirm')"
            class="rounded-xl bg-red-600 px-4 py-2.5 text-sm font-medium text-white transition hover:bg-red-700"
        >
          Supprimer
        </button>
      </div>
    </div>
  </div>
</template>