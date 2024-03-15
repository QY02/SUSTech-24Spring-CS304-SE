<template>
  <t-button @click="currentStep--">上一步</t-button>
  <t-button @click="currentStep++">下一步</t-button>
</template>

<script setup>
import { currentStep } from '@/components/book/Steps.vue';
</script>

<style scoped>

</style>