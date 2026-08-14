<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import MemberListItemDetails from '../components/MemberListItemDetails.vue'

const route = useRoute()

const member = ref(null)
const loading = ref(true)
const error = ref(null)

async function fetchMember() {
  try {
    const response = await fetch(`http://localhost:8080/members/${route.params.id}`)

    if (!response.ok) {
      throw new Error('Membre introuvable')
    }

    member.value = await response.json()
    console.log(member.value)
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

onMounted(fetchMember)
</script>

<template>
  <main class="mx-auto max-w-4xl px-6 py-8">
    <p v-if="loading">Chargement...</p>

    <p v-else-if="error">
      {{ error }}
    </p>

    <MemberListItemDetails v-else-if="member" :member="member" />
  </main>
</template>
