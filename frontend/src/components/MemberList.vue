<script setup>
import { onMounted, ref } from 'vue'
import MemberListItem from './MemberListItem.vue'

const members = ref([])
const loading = ref(true)
const error = ref(null)

async function fetchMembers() {
  try {
    const response = await fetch('http://localhost:8080/members')

    if (!response.ok) {
      throw new Error('Impossible de récupérer les membres')
    }

    members.value = await response.json()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

onMounted(fetchMembers)
</script>

<template>
  <main class="min-h-screen px-4 py-10 mx-auto max-w-7xl">
    <section class="mx-auto w-full max-w-3xl space-y-4">
      <header class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">Membres</h1>
        <p class="mt-2 text-gray-500">Liste des membres de l'université</p>
      </header>

      <p
        v-if="loading"
        class="rounded-xl border border-gray-200 bg-white p-6 text-center text-gray-500 shadow-sm"
      >
        Chargement...
      </p>

      <p
        v-else-if="error"
        class="rounded-xl border border-red-200 bg-red-50 p-6 text-center text-red-600"
      >
        {{ error }}
      </p>

      <div v-else class="space-y-3">
        <MemberListItem v-for="member in members" :key="member.id" :member="member" />
      </div>
    </section>
  </main>
</template>
