<template>
    <div class="main-div-finish">
      <t-space direction="vertical" align="center" style="gap: 1px">
        <CloseIcon size="100px" style="color: red"/>
        <h1 class="success-prompt-finish">预定失败</h1>
        <t-space>
          <t-button @click="rebook">重新预约</t-button>
          <t-button @click="pushRouter('HomePage')">返回首页</t-button>
        </t-space>
      </t-space>
    </div>
  </template>
  
  <script setup>
  import router from '@/routers';
  import {CheckCircleIcon, CloseIcon} from 'tdesign-icons-vue-next';
import { currentStep } from './Steps.vue';
  
  const pushRouter = (value) => {
    sessionStorage.setItem('currentStep', 0)
    switch (value) {
      case 'HomePage':
        router.push('/HomePage');
        break;
      case 'user':
        router.push('/user');
        break;
    }
  }

  const rebook = (value) => {
    sessionStorage.setItem('currentStep', 0);
    currentStep.value = 0
  }
  
  </script>
  
  <style scoped lang="less">
  .main-div-finish {
    position: relative;
    top: 25vh;
  }
  
  .success-prompt-finish {
    font-size: 25px;
    line-height: 0.1;
  }
  </style>