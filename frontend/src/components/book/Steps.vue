<template>
  <div class="main-div-steps">
    <t-steps style="width: 70%" :current="currentStep" :readonly="currentStep===3" @change="stepChange">
      <t-step-item title="选择场次">
        <template #icon>
          <TimeIcon size="24" class="icon-margin"/>
        </template>
      </t-step-item>
      <t-step-item title="选择座位">
        <template #icon>
          <svg class="t-icon t-icon-verify icon-margin" style="margin-top: 1px" xmlns="http://www.w3.org/2000/svg"
               viewBox="0 0 512 512" width="1.5em" height="1.5em">
            <path fill="currentColor"
                  d="M176 80H336c44.2 0 80 35.8 80 80v34.8c7.7-1.8 15.7-2.8 24-2.8s16.3 1 24 2.8V160c0-70.7-57.3-128-128-128H176C105.3 32 48 89.3 48 160v34.8c7.7-1.8 15.7-2.8 24-2.8s16.3 1 24 2.8V160c0-44.2 35.8-80 80-80zM462.5 227.6c-7.1-2.3-14.6-3.6-22.5-3.6c-9.5 0-18.5 1.8-26.8 5.2c-24.1 9.7-41.8 32-44.7 58.8H143.6c-3-26.8-20.6-49.1-44.7-58.8C90.5 225.8 81.5 224 72 224c-7.9 0-15.4 1.3-22.5 3.6C20.7 237 0 264.1 0 296V432c0 26.5 21.5 48 48 48H96c20.9 0 38.7-13.4 45.3-32H370.7c6.6 18.6 24.4 32 45.3 32h48c26.5 0 48-21.5 48-48V296c0-31.9-20.7-59-49.5-68.4zM368 400H144V336h32H336h32v64zM96 400v32H48l0-136c0-13.3 10.7-24 24-24s24 10.7 24 24v40 64zM464 296V432H416V296c0-13.3 10.7-24 24-24s24 10.7 24 24z"/>
          </svg>
        </template>
      </t-step-item>
      <t-step-item title="填写信息">
        <template #icon>
          <VerifyIcon size="24" class="icon-margin"/>
        </template>
      </t-step-item>
      <t-step-item title="完成" class="finish-step-item">
        <template #icon>
          <CheckCircleIcon size="24" class="icon-margin"/>
        </template>
      </t-step-item>
    </t-steps>
    <div v-show="currentStep === 0"><ChooseSession></ChooseSession></div>
    <div v-show="currentStep === 1"><ChooseSeat></ChooseSeat></div>
    <div v-show="currentStep === 2"><InputInformation></InputInformation></div>
    <div v-show="currentStep === 3"><Finish></Finish></div>
  </div>
</template>

<script setup lang="ts">

import {onMounted, ref} from "vue";
import {TimeIcon, VerifyIcon, CheckCircleIcon} from 'tdesign-icons-vue-next';
import ChooseSession from '@/components/book/ChooseSession.vue';
import ChooseSeat from '@/components/book/ChooseSeat.vue';
import InputInformation from '@/components/book/InputInformation.vue';
import Finish from '@/components/book/Finish.vue';

const stepChange = (current: number, previous: number) => {
  if ((previous != 3) && (current != 3)) {
    currentStep.value = current;
  }
}

const removeClickableOnFinishStepItem = () => {
  const parentDiv = document.querySelector('.finish-step-item');
  if (parentDiv && parentDiv.firstElementChild) {
    parentDiv.firstElementChild.classList.remove('t-steps-item--clickable');
  }
}

onMounted(() => {
  removeClickableOnFinishStepItem();
});

</script>

<script lang="ts">
import {ref} from "vue";

export let currentStep = ref(0);

</script>

<style scoped lang="less">
.main-div-steps {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  top: 5%;
}
</style>