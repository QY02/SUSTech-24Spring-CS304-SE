<script setup>
import {computed, reactive, ref} from 'vue';
import {Input, MessagePlugin} from 'tdesign-vue-next';
import {useVModel} from "@vueuse/core";
import {LockOnIcon} from "tdesign-icons-vue-next";
const token='z';
const props = defineProps({
  visible: Boolean
})
const emit = defineEmits(['update:visible'])
const visibleBody = useVModel(props,'visible',emit)

const formData = reactive({
    email:'',
    code:'',
    new_psw_1:'',
    new_psw_2:'',
});


const changePsw = ({validateResult, firstError}) => {
  if (validateResult === true) {
    formData.value = {
      email:'',
      code:'',
      new_psw_1:'',
      new_psw_2:'',
    }
    visibleBody.value = false;
    MessagePlugin.success('提交成功');
  } else {
    console.log('Validate Errors: ', firstError, validateResult);
    MessagePlugin.warning(firstError);
  }
}


const rePassword = (val) =>
    new Promise((resolve) => {
      const timer = setTimeout(() => {
        resolve(formData.value.new_psw_1 === val);
        clearTimeout(timer);
      });
    });
const passwordValidator = (val) => {
  if (val.length > 0 && val.length <= 4) {
    return { result: false, message: '太简单了！再开动一下你的小脑筋吧！', type: 'error' };
  }
  if (val.length > 4 && val.length < 8) {
    return { result: false, message: '还差一点点，就是一个完美的密码了！', type: 'warning' };
  }
  return { result: true, message: '太强了，你确定自己记得住吗！', type: 'success' };
};
const FORM_RULES = ref({
  email: [{ required: true, message: '格式必须为邮箱', type: 'warning' }],
  code:[{required: true, message: '验证码必填'}],
  new_psw_1: [
      {required: true, message: '新密码必填'},
    { validator: passwordValidator }
  ],
  new_psw_2: [
      {required: true, message: '新密码必填'},
      { validator: rePassword, message: '两次密码不一致' }
  ]
});
const emailSuffix = ['@qq.com', '@163.com', '@gmail.com','@mail.sustech.edu.cn'];
const emailOptions = computed(() => {
  const emailPrefix = formData.email.split('@')[0];
  if (!emailPrefix) return [];
  return emailSuffix.map((suffix) => emailPrefix + suffix);
});
const onReset = () => {
  MessagePlugin.success('重置成功');
};

</script>

<template>
  <t-dialog
      v-model:visible="visibleBody"
      attach="body"
      header="修改密码"
      destroy-on-close:true
      width="30%"
      :cancel-btn=null
      :confirm-btn=null
  >
    <template #body>
      <t-space direction="vertical">
        <t-form
            ref="form"
            id="form"
            :data="formData"
            reset-type="initial"
            @reset="onReset"
            @submit="changePsw"
            :rules="FORM_RULES"
            label-width="100px"
            label-align="right"
        >
          <t-form-item  name="email" label="邮箱">
            <t-auto-complete v-model="formData.email" :options="emailOptions" filterable:true></t-auto-complete>
          </t-form-item>

          <t-form-item name="code" label="验证码">
            <t-input v-model="formData.code" clearable:true placeholder="请输入验证码">
              <template #prefix-icon>
                <lock-on-icon />
              </template>
            </t-input>
            <t-button theme="default" variant="base">验证码</t-button>
          </t-form-item>
          <t-form-item name="new_psw_1" label="新密码">
            <t-input v-model="formData.new_psw_1" type="password" clearable:true placeholder="请输入密码">
              <template #prefix-icon>
                <lock-on-icon />
              </template>
            </t-input>
          </t-form-item>
          <t-form-item name="new_psw_2" label="确认新密码">
          <t-input v-model="formData.new_psw_2" type="password" clearable:true placeholder="请输入密码">
            <template #prefix-icon>
              <lock-on-icon />
            </template>
          </t-input>
        </t-form-item>

          <t-form-item>
            <t-space size="small">
              <t-button theme="primary" type="submit">提交</t-button>
              <t-button theme="default" variant="base" type="reset">重置</t-button>
            </t-space>
          </t-form-item>
        </t-form>
      </t-space>
    </template>
  </t-dialog>
</template>

<style scoped>

</style>