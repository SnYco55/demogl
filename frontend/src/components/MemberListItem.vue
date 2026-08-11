<script setup>
defineProps({
  member: {
    type: Object,
    required: true,
  },
})
</script>

<template>
  <article>
    <h2>{{ member.firstname }} {{ member.lastname }}</h2>

    <p>Département : {{ member.departmentsId }}</p>
    <p>Début : {{ member.start }}</p>
    <p>Fin : {{ member.end }}</p>
  </article>
</template>
