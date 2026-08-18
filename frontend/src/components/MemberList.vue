<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import MemberListItem from './MemberListItem.vue'
import type { Member } from '@/types/type.ts'

const members = ref<Member[]>([])
const loading = ref(true)
const error = ref<string | null>(null)

const selectedDate = ref('')
const showDateFilter = ref(false)

async function fetchMembers(): Promise<void> {
  try {
    const response = await fetch('http://localhost:8080/members')

    if (!response.ok) {
      throw new Error('Impossible de récupérer les membres')
    }

    members.value = await response.json()
  } catch (e: unknown) {
    error.value = e instanceof Error
        ? e.message
        : 'Une erreur est survenue'
  } finally {
    loading.value = false
  }
}

function toDateOnly(date: string | null): Date | null {
  if (!date) return null

  const value = new Date(date)

  return new Date(
      value.getFullYear(),
      value.getMonth(),
      value.getDate(),
  )
}

function isMemberActiveOnDate(
    member: Member,
    date: string,
): boolean {
  if (!date) return true

  const selected = toDateOnly(`${date}T00:00:00`)
  const start = toDateOnly(member.start)

  if (!selected || !start) {
    return false
  }

  if (selected < start) {
    return false
  }

  if (!member.end) {
    return true
  }

  const end = toDateOnly(member.end)

  if (!end) {
    return true
  }

  return selected <= end
}

const filteredMembers = computed(() => {
  return members.value.filter((member) =>
      isMemberActiveOnDate(member, selectedDate.value),
  )
})

function formatSelectedDate(date: string): string {
  if (!date) return ''

  return new Intl.DateTimeFormat('fr-BE', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(new Date(`${date}T00:00:00`))
}

onMounted(fetchMembers)
</script>

<template>
  <main class="min-h-screen px-4 py-10 mx-auto max-w-7xl">
    <section class="mx-auto w-full max-w-3xl space-y-4">

      <!-- Header -->
      <header class="mb-4 flex items-end justify-between gap-4">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">
            Membres
          </h1>

          <p class="mt-2 text-gray-500">
            Liste des membres de l'université
          </p>
        </div>

        <!-- Settings -->
        <button
            type="button"
            @click="showDateFilter = !showDateFilter"
            class="rounded-xl border border-gray-200 bg-white px-4 py-2 text-lg shadow-sm transition hover:border-gray-300 hover:bg-gray-50"
            title="Filtrer par date"
        >
          Filtrer
        </button>
      </header>

      <!-- Date filter -->
      <section
          v-if="showDateFilter"
          class="rounded-xl border border-gray-200 bg-white p-4 shadow-sm"
      >
        <div class="flex items-center justify-between gap-4">
          <div>
            <label
                for="member-date"
                class="block text-sm font-medium text-gray-700"
            >
              Membres présents à la date du
            </label>
          </div>

          <input
              id="member-date"
              v-model="selectedDate"
              type="date"
              class="rounded-lg border border-gray-300 px-3 py-2 text-sm"
          />
        </div>
      </section>

      <!-- Selected date -->
      <div
          v-if="selectedDate"
          class="flex items-center justify-between rounded-xl bg-gray-100 px-4 py-3"
      >
        <p class="text-sm text-gray-600">
          Membres présents le
          <span class="font-semibold text-gray-900">
            {{ formatSelectedDate(selectedDate) }}
          </span>
          :
          <span class="font-semibold text-gray-900">
            {{ filteredMembers.length }}
          </span>
        </p>

        <button
            type="button"
            @click="selectedDate = ''"
            class="text-sm font-medium text-gray-500 hover:text-gray-900"
        >
          Réinitialiser
        </button>
      </div>

      <!-- Loading -->
      <p
          v-if="loading"
          class="rounded-xl border border-gray-200 bg-white p-6 text-center text-gray-500 shadow-sm"
      >
        Chargement...
      </p>

      <!-- Error -->
      <p
          v-else-if="error"
          class="rounded-xl border border-red-200 bg-red-50 p-6 text-center text-red-600"
      >
        {{ error }}
      </p>

      <!-- Empty -->
      <p
          v-else-if="filteredMembers.length === 0"
          class="rounded-xl border border-gray-200 bg-white p-6 text-center text-gray-500 shadow-sm"
      >
        Aucun membre présent à cette date.
      </p>

      <!-- Members -->
      <div v-else class="space-y-3">
        <MemberListItem
            v-for="member in filteredMembers"
            :key="member.id"
            :member="member"
        />
      </div>

    </section>
  </main>
</template>