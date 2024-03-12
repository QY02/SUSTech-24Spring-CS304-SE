<template>
  <div style="width: 285px">
    <t-form ref="form" :data="formData" :colon="true" :label-width="0" @reset="onReset" @submit="handleSubmit">
      <t-form-item name="account">
        <t-input v-model="id" clearable placeholder="请输入账户名">
          <template #prefix-icon>
            <desktop-icon/>
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="password">
        <t-input v-model="password" type="password" clearable placeholder="请输入密码">
          <template #prefix-icon>
            <lock-on-icon/>
          </template>
        </t-input>
      </t-form-item>

      <t-form-item>
        <t-button theme="primary" shape="round" type="submit" block>登录</t-button>
      </t-form-item>
    </t-form>
  </div>
  <!--  <va-form class="form" ref="formRef">-->
  <!--    <va-input type="text" label="id" class="va-input"-->
  <!--              v-model="id"-->
  <!--              placeholder="Enter your 8-digit ID"-->
  <!--              :rules="[(v) => /^(\d{8})$/.test(v) || 'ID must be 8 digits']"-->
  <!--    ></va-input>-->
  <!--    <va-value v-slot="isPasswordVisible" :default-value="false">-->
  <!--      <va-input-->
  <!--          v-model="password"-->
  <!--          :type="isPasswordVisible.value ? 'text' : 'password'"-->
  <!--          label="Password"-->
  <!--          placeholder="Enter your password"-->
  <!--          :rules="[(v) => /^[a-zA-Z0-9/]{8,}$/.test(v) || 'Only numbers, characters, and slash are allowed, minimum length is 8']"-->
  <!--          @click-append-inner="isPasswordVisible.value = !isPasswordVisible.value"-->
  <!--      >-->
  <!--        <template #appendInner>-->
  <!--          <va-icon-->
  <!--              :name="isPasswordVisible.value ? 'visibility_off' : 'visibility'"-->
  <!--              size="small"-->
  <!--              color="primary"-->
  <!--          />-->
  <!--        </template>-->
  <!--      </va-input>-->
  <!--    </va-value>-->
  <!--    <va-button-->
  <!--        type="submit"-->
  <!--        class="form-btn"-->
  <!--        color="primary"-->
  <!--        size="small"-->
  <!--        :disabled="!isValid"-->
  <!--        @click="  handleSubmit"-->
  <!--    >-->
  <!--      Log in-->
  <!--    </va-button>-->
  <!--  </va-form>-->

</template>

<script setup>
import {inject} from 'vue';
import {MessagePlugin} from 'tdesign-vue-next';
import {DesktopIcon, LockOnIcon} from 'tdesign-icons-vue-next';
import axios from "axios";
import router from "@/routers";
import { ref} from "vue";

// const formData = reactive({
//   account: '',
//   password: '',
// });

const onReset = () => {
  MessagePlugin.success('重置成功');
};


const apiUrl = inject('$API_URL');
// const {isValid, validate} = useForm('formRef')
// const {init} = useToast();

const password = ref("");
const id = ref("");

// const appConfig = ref(getCurrentInstance().appContext.config.globalProperties).value;
// 获取全局变量 $apiBaseUrl
// alert(apiUrl)
axios.defaults.baseURL = apiUrl;
const handleSubmit = () => {
  // alert("in")
  // if (true) {
    axios.post("/login", {
      id: id.value,
      password: password.value
    })
        .then((response) => {
          // alert(JSON.stringify(response.data.data))
          const rd = response.data.data.id;
          const type = response.data.data.type
          const token = response.data.data.password
          const themeColor = response.data.data.themeColor
          sessionStorage.setItem('primary-color', themeColor);

          sessionStorage.setItem('uid', rd);
          sessionStorage.setItem('token', token);
          sessionStorage.setItem('username', response.data.data.name)

          // alert(sessionStorage.getItem('username'))
          MessagePlugin.success("Welcome! " + rd);

          if (type === 1) {//老师
            router.push("/teacher/welcomePage");
          } else if (type === 2) {//学生
            router.push("/welcomePage");
            // alert("push")
          } else if (type === 0) {//管理员
            router.push("/admin/homepage");
          } else {
            router.push("/welcomePage");
          }

          // alert(appConfig.$userId)
          // alert(appConfig.$token)
        })
        .catch((error) => {
          if (error.response) {
            // 请求已发出，但服务器响应的状态码不在 2xx 范围内
            MessagePlugin.warning(error.response.data.msg);
            // init({message: error.response.data.msg, color: "danger"})
          } else {
            // 一些错误是在设置请求的时候触发
            MessagePlugin.warning(error.message);
            // init({message: error.message, color: "danger"})
          }
        });
  // }


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