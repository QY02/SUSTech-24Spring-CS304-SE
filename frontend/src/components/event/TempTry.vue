<script setup>
import {ref} from 'vue';
import ChangePasswordByOld from "@/components/event/ChangePasswordByOld.vue";
import ChangePasswordByEmail from "@/components/event/ChangePasswordByEmail.vue";

const visibleOld=ref(false)
const visibleEmail=ref(false)
</script>
<template>
  <div class="part">
    <div class="inform">
      <t-button
          class="btn"
          @click="()=>visibleOld=true"
      >
        通过旧密码
      </t-button>
      <t-button
          class="btn"
          @click="()=>visibleEmail=true"
      >
        通过邮箱
      </t-button>
    </div>
  <change-password-by-old v-model:visible="visibleOld"></change-password-by-old>
    <change-password-by-email v-model:visible="visibleEmail"></change-password-by-email>
  </div>

</template>
