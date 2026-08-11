<script setup>
import { onMounted, ref } from 'vue'
import MemberListItem from './MemberListItem.vue'

const members = ref([])

async function fetchMembers() {
  const response = await fetch('http://localhost:8080/members')
  members.value = await response.json()
}

onMounted(fetchMembers)
</script>

<template>
  <section>
    <MemberListItem v-for="member in members" :key="member.id" :member="member" />
  </section>
</template>
