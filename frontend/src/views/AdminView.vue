<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import router from "@/router";
</script>

<template>
  <main class="min-h-screen bg-gray-50">
    <div class="mx-auto max-w-6xl px-6 py-10">
      <!-- Header -->
      <header class="mb-8">
        <button
            type="button"
            @click="router.back()"
            class="rounded-xl border border-gray-200 bg-white mb-4 px-4 py-2 text-sm font-medium text-gray-700 shadow-sm transition hover:border-gray-300 hover:bg-gray-50"
        >
          Retour
        </button>
        <h1 class="text-3xl font-bold tracking-tight text-gray-900">Administration</h1>

        <p class="mt-2 text-sm text-gray-500">
          Gérez les facultés, départements, services et membres.
        </p>
      </header>

      <!-- Navigation -->
      <nav class="mb-8 overflow-x-auto rounded-2xl border border-gray-200 bg-white p-1.5 shadow-sm">
        <div class="flex min-w-max gap-1">
          <RouterLink
            to="/admin/faculties"
            class="rounded-xl px-5 py-2.5 text-sm font-medium text-gray-500 transition hover:bg-gray-100 hover:text-gray-900"
            active-class="bg-gray-900 text-white hover:bg-gray-900 hover:text-white"
          >
            Facultés
          </RouterLink>

          <RouterLink
            to="/admin/departments"
            class="rounded-xl px-5 py-2.5 text-sm font-medium text-gray-500 transition hover:bg-gray-100 hover:text-gray-900"
            active-class="bg-gray-900 text-white hover:bg-gray-900 hover:text-white"
          >
            Départements
          </RouterLink>

          <RouterLink
            to="/admin/services"
            class="rounded-xl px-5 py-2.5 text-sm font-medium text-gray-500 transition hover:bg-gray-100 hover:text-gray-900"
            active-class="bg-gray-900 text-white hover:bg-gray-900 hover:text-white"
          >
            Services
          </RouterLink>

          <RouterLink
            to="/admin/members"
            class="rounded-xl px-5 py-2.5 text-sm font-medium text-gray-500 transition hover:bg-gray-100 hover:text-gray-900"
            active-class="bg-gray-900 text-white hover:bg-gray-900 hover:text-white"
          >
            Membres
          </RouterLink>
        </div>
      </nav>

      <!-- Current management page -->
      <RouterView />
    </div>
  </main>
</template>
