<template>
  <div class="input-information-main-div">
    <h1 class="title-input-information">信息填写</h1>
    <t-form :rules="FORM_RULES" :data="formData" :colon="true" @submit="onSubmit">
      <t-form-item v-for="information in additionalInformation" :label="information.name" :name="information.nameEng">
        <t-input v-model="formData[information.nameEng]" :placeholder="`请输入${information.name}`"></t-input>
      </t-form-item>
      <div class="input-information-button-div">
        <t-space size="medium">
          <t-button @click="currentStep--">上一步</t-button>
          <t-button type="submit">提交</t-button>
        </t-space>
      </div>
    </t-form>
  </div>
</template>

<script setup lang="ts">
import {currentStep, toNextStep} from '@/components/book/Steps.vue';
import {AdditionalInformationItem, bookingInformation} from '@/components/book/Steps.vue';
import {reactive} from "vue";
import {FormProps, FormRule, MessagePlugin} from "tdesign-vue-next";

const additionalInformation: AdditionalInformationItem[] = bookingInformation.additionalInformation;

const formData: FormProps['data'] = reactive(additionalInformation.reduce((acc, item) => {
  acc[item.nameEng] = item.value;
  return acc;
}, {} as Record<string, string>));

const FORM_RULES: FormProps['rules'] = additionalInformation.reduce((acc, item) => {
  acc[item.nameEng] = [
    {
      required: item.required,
      message: `${item.name}必填`,
    },
  ];
  if (item.rules !== null) {
    acc[item.nameEng].push(...item.rules);
  }
  return acc;
}, {} as Record<string, Array<FormRule>>);

const onSubmit: FormProps['onSubmit'] = ({validateResult, firstError}) => {
  if (validateResult === true) {
    for (let i = 0; i < additionalInformation.length; i++) {
      additionalInformation[i].value = formData[i];
    }
    MessagePlugin.success('提交成功');
    toNextStep();
  } else {
    MessagePlugin.warning(firstError);
  }
};
</script>

<style scoped lang="less">
.input-information {
  &-main-div {
    position: relative;
    top: 5vh;
  }

  &-button-div {
    display: flex;
    justify-content: center;
  }
}


.title-input-information {
  text-align: center;
  font-size: 25px;
  line-height: 0;
}
</style>