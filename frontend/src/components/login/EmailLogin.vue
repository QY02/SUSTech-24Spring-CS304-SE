<template>
  <div style="width: 285px">
    <t-form ref="form" :data="formData" :rules="rules" :colon="true" :label-width="0" @reset="onReset"
            @submit="handleSubmit">
      <div style="margin-top: 18px">
        <!--     <div style="color: #2b54d9">ID</div>-->
        <t-form-item name="email" style="margin-bottom: 30px;">
        <t-input v-model="formData.email" clearable placeholder="请输入邮箱"

        >
          <template #prefix-icon>
            <desktop-icon/>
          </template>
        </t-input>
      </t-form-item>
      </div>

      <div style="display: flex;">
        <t-form-item name="code">
          <t-input v-model="formData.code" clearable placeholder="请输入验证码" style="width: 226px; margin-bottom: 12px">
            <template #prefix-icon>
              <lock-on-icon/>
            </template>
          </t-input>
        </t-form-item>

        <t-form-item>
          <t-button theme="primary" block @click="handleVeri" style="width: 50px; margin-left: 10px">code</t-button>
        </t-form-item>
      </div>
      <t-form-item>
        <t-button theme="primary" shape="round" type="submit" block style="height: 40px; margin-bottom: 8px">登录</t-button>
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

import {getCurrentInstance, inject, reactive} from 'vue';
import {MessagePlugin} from 'tdesign-vue-next';
import {DesktopIcon, LockOnIcon} from 'tdesign-icons-vue-next';
import axios from "axios";
import router from "@/routers";

const formData = reactive({
  email: '',
  code: '',
});

const onReset = () => {
  MessagePlugin.success('重置成功');
};



// const {isValid, validate} = useForm('formRef')
// const {init} = useToast();

// const password = ref("");
// const id = ref("");
const rules = {
  email: [{required: true}, {validator: (v) => /[^@]+@[^@]+\.[a-zA-Z]{2,}$/.test(v), message: 'Wrong format'}],
  code: [{required: true}, {validator: (v) => /^[0-9]{6}$/.test(v), message: 'Code must be a six-digit number'}],
};

//   const onSubmit = ({ validateResult, firstError }) => {
//   if (validateResult === true) {
//   MessagePlugin.success('提交成功');
// } else {
//   console.log('Validate Errors: ', firstError, validateResult);
//   MessagePlugin.warning(firstError);
// }


// };


// const {init} = useToast();
// const {isValid, validate} = useForm('formRef')
// const code = ref("");
// const email = ref("");
// const appConfig = ref(getCurrentInstance().appContext.config.globalProperties).value;
// 获取全局变量 $apiBaseUrl
const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const apiBaseUrl = globalProperties.$apiBaseUrl;
// alert(apiBaseUrl)
axios.defaults.baseURL = apiBaseUrl;
const handleSubmit = ({validateResult}) => {
  if (validateResult === true) {
    axios.post("/loginWithEmail", {
      //此接口返回完整用户信息
      email: formData.email,
      code: formData.code
    })
        .then((response) => {
          const rd = response.data.data.id;
          const type = response.data.data.type
          const token = response.data.data.password
          const themeColor = response.data.data.themeColor
          sessionStorage.setItem('primary-color', themeColor);
          sessionStorage.setItem('uid', rd);
          sessionStorage.setItem('username', response.data.data.name)
          sessionStorage.setItem('token', token);
          if (type === 0) {//管理员
            router.push("/admin/homepage");
          } else {//正常用户
            router.push("/HomePage");
          }
          MessagePlugin.success("Welcome! " + rd);

        })
        .catch((error) => {
          if (error.response) {
            // 请求已发出，但服务器响应的状态码不在 2xx 范围内
            MessagePlugin.warning(error.response.data.msg);
          } else {
            // 一些错误是在设置请求的时候触发
            MessagePlugin.warning(error.message);
          }
        });
  } else {
    MessagePlugin.warning("Please make sure the input format is correct!")

  }
};
const handleVeri = () => {
if(rules.email[1].validator(formData.email)) {
  axios.post(`/sendEmail/${formData.email}`, {}, {})
      .then(() => {
        MessagePlugin.info("Already send the code, please check and enter.");
      })
      .catch((error) => {
        if (error.response) {
          // 请求已发出，但服务器响应的状态码不在 2xx 范围内
          MessagePlugin.error(error.response.data.msg);
        } else {
          // 一些错误是在设置请求的时候触发
          MessagePlugin.error(error.message);
        }
      });

}else{
  MessagePlugin.warning("Please input correct email!");

}

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