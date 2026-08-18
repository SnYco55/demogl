<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { MemberDetails } from '@/types/member'

const route = useRoute()
const router = useRouter()

const member = ref<MemberDetails | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)

function formatDate(date: string | null): string {
  if (!date) return '—'

  return new Intl.DateTimeFormat('fr-BE', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(new Date(date))
}

async function fetchMember(): Promise<void> {
  try {
    const response = await fetch(
        `http://localhost:8080/members/${route.params.id}`,
    )

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Membre introuvable')
      }

      throw new Error('Impossible de récupérer le membre')
    }

    member.value = await response.json() as MemberDetails
  } catch (e: unknown) {
    if (e instanceof Error) {
      error.value = e.message
    } else {
      error.value = 'Une erreur est survenue'
    }
  } finally {
    loading.value = false
  }
}

onMounted(fetchMember)
</script>

<template>
  <main class="mx-auto min-h-screen max-w-7xl px-4 py-10">

    <!-- Top bar -->
    <div class="mb-6 flex justify-start">
      <button
          type="button"
          @click="router.back()"
          class="rounded-xl border border-gray-200 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm transition hover:border-gray-300 hover:bg-gray-50"
      >
        Retour
      </button>
    </div>

    <!-- Loading -->
    <p
        v-if="loading"
        class="rounded-xl border border-gray-200 bg-white p-6 text-center text-gray-500"
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

    <!-- Member -->
    <div v-else-if="member" class="space-y-6">

      <!-- Header -->
      <header class="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm">
        <p class="text-sm font-medium text-gray-500">
          Membre #{{ member.id }}
        </p>

        <h1 class="mt-1 text-3xl font-bold text-gray-900">
          {{ member.firstname }} {{ member.lastname }}
        </h1>

        <div class="mt-4 flex gap-6 text-sm text-gray-600">
          <p>
            <span class="font-medium text-gray-900">Début :</span>
            {{ formatDate(member.start) }}
          </p>

          <p>
            <span class="font-medium text-gray-900">Fin :</span>
            {{ formatDate(member.end) }}
          </p>
        </div>
      </header>

      <!-- Roles -->
      <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm">
        <h2 class="text-lg font-semibold text-gray-900">
          Rôles
        </h2>

        <div class="mt-4 flex flex-wrap gap-2">
          <span
              v-for="role in member.roles"
              :key="role.id"
              class="rounded-full bg-gray-100 px-3 py-1 text-sm font-medium text-gray-700"
          >
            {{ role.name }}
          </span>
        </div>
      </section>

      <!-- Services -->
      <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm">
        <h2 class="text-lg font-semibold text-gray-900">
          Services
        </h2>

        <div class="mt-4 space-y-3">
          <div
              v-for="service in member.services"
              :key="service.id"
              class="rounded-xl border border-gray-100 bg-gray-50 p-4"
          >
            <div class="flex items-start justify-between gap-4">

              <div>
                <h3 class="font-semibold text-gray-900">
                  {{ service.name }}
                </h3>

                <p class="mt-1 text-sm text-gray-500">
                  Département :
                  {{ service.department.id }}
                </p>

                <p class="text-sm text-gray-500">
                  Faculté :
                  {{ service.faculty.name }}
                </p>

                <p class="text-sm text-gray-500">
                  Directeur :
                  {{ service.director.firstname }}
                  {{ service.director.lastname }}
                </p>
              </div>

              <span
                  v-if="service.director.id === member.id"
                  class="rounded-full bg-blue-100 px-3 py-1 text-xs font-semibold text-blue-700"
              >
                Directeur
              </span>

            </div>
          </div>
        </div>
      </section>

    </div>
  </main>
</template>