<script setup>
import { computed, ref } from 'vue';
import { Input, MessagePlugin } from 'tdesign-vue-next';
import { useVModel } from "@vueuse/core";
import { LockOnIcon } from "tdesign-icons-vue-next";
const token = 'z';
const props = defineProps({
  visible: Boolean,
  old_email: String
})
const emit = defineEmits(['update:visible'])
const visibleBody = useVModel(props, 'visible', emit)

const formData = ref({
  old_email: props.old_email,
  new_email: '',
  code: '',
});


const FORM_RULES = ref({
  old_email: [{ disabled: true }],
  new_email: [{ required: true, message: '格式必须为邮箱', type: 'error' }],
  code: [{ required: true, message: '必须填写验证码', type: 'error' }],
});
const emailSuffix = ['@qq.com', '@163.com', '@gmail.com', '@mail.sustech.edu.cn, @outlook.com'];
const emailOptions = computed(() => {
  const emailPrefix = formData.email.split('@')[0];
  if (!emailPrefix) return [];
  return emailSuffix.map((suffix) => emailPrefix + suffix);
});

const onReset = () => {
  MessagePlugin.success('重置成功');
};

function throttle(func) {
  let inThrottle = false;

  return function (...args) {
    if (!inThrottle) {
      // 执行函数
      func.apply(this, args);
      inThrottle = true;

      // 设置节流结束的定时器
      setTimeout(() => {
        inThrottle = false;
      }, 60000);
    }
  };
}

const sendCode = () => {
  if (formData.email !== '') {
    axios.put("/user/forgetPass", {
      email: formData.email
    }, {
      headers: {
        token: token
      }
    }).then(() => {
      MessagePlugin.info("验证码已发送");
    }
    )
  } else {
    MessagePlugin.warning("邮箱不能为空");
  }
}

</script>

<template>
  <t-dialog v-model:visible="visibleBody" attach="body" header="修改邮箱" destroy-on-close:true width="40%" :cancel-btn=null
    :confirm-btn=null>
    <template #body>
      <t-space direction="vertical">
        <t-form ref="form" id="form" :data="formData" reset-type="initial" @reset="onReset" @submit="changePsw"
          :rules="FORM_RULES" label-width="100px" label-align="right">
          <t-form-item name="old_email" label="旧邮箱">
            <t-input v-model="formData.old_email" :placeholder="old_email" disabled>
            </t-input>
          </t-form-item>
          <t-form-item name="new_email" label="新邮箱">
            <t-input v-model="formData.new_email" clearable:true placeholder="请输入新邮箱">
            </t-input>
          </t-form-item>
          <t-form-item name="code" label="验证码">
            <t-input v-model="formData.code" clearable:true placeholder="请输入验证码">
              <template #prefix-icon>
                <lock-on-icon />
              </template>
            </t-input>
            <t-button theme="default" variant="base" @click="getCode">验证码</t-button>
          </t-form-item>
          <t-form-item>
            <t-space size="20px">
              <t-button theme="success" type="submit">提交</t-button>
              <t-button theme="default" variant="base" type="reset">重置</t-button>
            </t-space>
          </t-form-item>
        </t-form>
      </t-space>
    </template>
  </t-dialog>
</template>

<style scoped></style>