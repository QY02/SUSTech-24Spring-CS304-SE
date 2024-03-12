<template>
  <div style="width: 350px">
    <t-form ref="form" :data="formData" :colon="true" :label-width="0" @reset="onReset" @submit="onSubmit">
      <t-form-item name="account">
        <t-input v-model="formData.account" clearable placeholder="hhh">
          <template #prefix-icon>
            <desktop-icon />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="password">
        <t-input v-model="formData.password" type="password" clearable placeholder="请输入密码">
          <template #prefix-icon>
            <lock-on-icon />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item>
        <t-button theme="primary" type="submit" block>登录</t-button>
      </t-form-item>
    </t-form>
  </div>
<!--  <va-form class="form" ref="formRef">-->
<!--    <va-input type="email" label="e-mail" class="va-input"-->
<!--              v-model="email"-->
<!--              placeholder="Enter your e-mail"-->
<!--              :rules="[(v) => /[^@]+@[^@]+\.[a-zA-Z]{2,}$/.test(v) || 'Wrong format']"-->
<!--    ></va-input>-->
<!--    <va-button-->
<!--        preset="secondary"-->
<!--        class="form-btn"-->
<!--        color="colors.primary"-->
<!--        size="small"-->
<!--        @click="handleVeri"-->
<!--    >-->
<!--      Get code-->
<!--    </va-button>-->
<!--    <va-input-->
<!--        v-model="code"-->
<!--        type="text"-->
<!--        class="input"-->
<!--        placeholder="Enter the verification code"-->
<!--        label="verification code"-->
<!--        :rules="[(v) => /^[0-9]{6}$/.test(v) || 'Code must be a six-digit number']"-->
<!--    ></va-input>-->


<!--  </va-form>-->
<!--  <va-button-->
<!--      type="submit"-->
<!--      class="form-btn"-->
<!--      color="primary"-->
<!--      size="small"-->
<!--      @click="handleSubmit"-->
<!--      :disabled="!isValid"-->
<!--  >-->
<!--    Log in-->
<!--  </va-button>-->

</template>


<script setup>

  import { reactive } from 'vue';
  import { MessagePlugin } from 'tdesign-vue-next';
  import { DesktopIcon, LockOnIcon } from 'tdesign-icons-vue-next';

  const formData = reactive({
  account: '',
  password: '',
});

  const onReset = () => {
  MessagePlugin.success('重置成功');
};

  const onSubmit = ({ validateResult, firstError }) => {
  if (validateResult === true) {
  MessagePlugin.success('提交成功');
} else {
  console.log('Validate Errors: ', firstError, validateResult);
  MessagePlugin.warning(firstError);
}


// };
// import axios from "axios";
// import router from "@/routers";
// import {getCurrentInstance, ref} from "vue";
// import {useForm, useToast} from "vuestic-ui";
// import {DesktopIcon, LockOnIcon} from "tdesign-icons-vue-next";
//
// export default {
//   components: {LockOnIcon, DesktopIcon},
//   setup() {
//     const {init} = useToast();
//     const {isValid, validate} = useForm('formRef')
//     const code = ref("");
//     const email = ref("");
//     const appConfig = ref(getCurrentInstance().appContext.config.globalProperties).value;
//     // 获取全局变量 $apiBaseUrl
//     axios.defaults.baseURL = appConfig.$apiBaseUrl;
//     const handleSubmit = () => {
//       if (validate()) {
//         axios.post("/loginWithEmail", {
//           //此接口返回完整用户信息
//           email: email.value,
//           code: code.value
//         })
//             .then((response) => {
//               const rd = response.data.data.id;
//               const type = response.data.data.type
//               const token = response.data.data.password
//               const themeColor = response.data.data.themeColor
//               sessionStorage.setItem('primary-color', themeColor);
//               sessionStorage.setItem('uid', rd);
//               sessionStorage.setItem('username', response.data.data.name)
//               sessionStorage.setItem('token', token);
//               if (type === 1) {//老师
//                 router.push("/teacher/welcomePage");
//               } else if (type === 2) {//学生
//                 router.push("/welcomePage");
//               } else if (type === 0) {//管理员
//                 router.push("/admin/homepage");
//               } else {
//                 router.push("/welcomePage");
//               }
//               init({message: "Welcome! " + rd, color: "success"});
//
//             })
//             .catch((error) => {
//               if (error.response) {
//                 // 请求已发出，但服务器响应的状态码不在 2xx 范围内
//                 init({message: error.response.data.msg, color: "danger"})
//               } else {
//                 // 一些错误是在设置请求的时候触发
//                 init({message: error.message, color: "danger"})
//               }
//             });
//       }
//
//     };
//     const handleVeri = () => {
//       axios.post(`/sendEmail/${email.value}`, {}, {})
//           .then(() => {
//             init({
//               message: "Already send the code, please check and enter.", color: "info"
//             });
//           })
//           .catch((error) => {
//             if (error.response) {
//               // 请求已发出，但服务器响应的状态码不在 2xx 范围内
//               init({message: error.response.data.msg, color: "danger"})
//             } else {
//               // 一些错误是在设置请求的时候触发
//               init({message: error.message, color: "danger"})
//             }
//           });
//     };
//     return {
//       handleSubmit,
//       handleVeri,
//       email,
//       code,
//       isValid,
//       validate,
//     }
//   }


};
</script>

<style scoped>
.form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 18px;
  margin-bottom: 15px;
}


.form-btn {
  padding: 10px 15px;
  font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
  "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
  border-radius: 20px;
  border: 0 !important;
  outline: 0 !important;
  background: teal;
  color: white;
  cursor: pointer;
  box-shadow: rgba(0, 0, 0, 0.24) 0 3px 8px;
}

.form-btn:active {
  box-shadow: none;
}
</style>