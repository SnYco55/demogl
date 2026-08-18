<script setup lang="ts">
import { RouterLink } from 'vue-router'
import type { Member } from '@/types/member'

defineProps<{
  member: Member
}>()

function formatDate(date: string | null): string {
  if (!date) return '—'

  return new Intl.DateTimeFormat('fr-BE', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(new Date(date))
}
</script>

<template>
  <RouterLink
    :to="`/members/${member.id}`"
    class="block rounded-xl border border-gray-200 bg-white p-5 shadow-sm transition hover:-translate-y-0.5 hover:border-gray-300 hover:shadow-md"
  >
    <div class="flex items-center justify-between gap-6">
      <div>
        <h2 class="text-lg font-semibold text-gray-900">
          {{ member.firstname }} {{ member.lastname }}
        </h2>
      </div>

      <div class="flex items-center gap-15">
        <div class="shrink-0 text-left text-sm text-gray-500 bg-gray-100 p-2 rounded-2xl">
          <p>
            <span class="font-medium text-gray-700">Début :</span>
            {{ formatDate(member.start) }}
          </p>

          <p>
            <span class="font-medium text-gray-700">Fin :</span>
            {{ formatDate(member.end) }}
          </p>
        </div>

        <span class="text-3xl text-black"> > </span>
      </div>
    </div>
  </RouterLink>
</template>
