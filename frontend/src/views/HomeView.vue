<template>
  <div>
    <MemberList />

    <RouterLink
        to="/admin/faculties"
        class="fixed right-6 top-6 rounded-xl border border-gray-200 bg-white px-4 py-3 font-medium shadow-sm transition hover:bg-gray-50"
    >
      Administration
    </RouterLink>
  </div>
</template>

<script setup lang="ts">
import MemberList from '@/components/MemberList.vue'
</script>