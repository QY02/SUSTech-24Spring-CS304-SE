<template>
  <!--  <NavBarWithOnlyTitle></NavBarWithOnlyTitle>-->
  <div id="building">
    <div class="parent-container">
      <div class="form-container">
        <p class="title">Register</p>
        <t-form ref="form" :data="formData" :rules="rules" :colon="true" :label-width="5" @reset="onReset"
                @submit="handleSubmit" style="margin-top: 40px">
          <t-form-item name="username">
            <t-input type="text" clearable placeholder="请输入用户名"
                     v-model="formData.username"
                     label="用户名"
                     required-mark
            ></t-input>
          </t-form-item>

          <t-form-item name="id">
            <t-input type="text"
                     v-model="formData.id"
                     placeholder="请输入8位学号"
                     :rules="[(v) => /^(\d{8})$/.test(v) || 'ID must be 8 digits']"
                     label="学号"
                     required-mark
            ></t-input>
          </t-form-item>

          <t-form-item name="department" style="margin-left: 20px">
            <t-select v-model="formData.department" label="系别">

              <t-option key="Computer Science" label="Computer Science" value="Computer Science"/>
              <t-option key="Engineering" label="Engineering" value="Engineering"/>
              <t-option key="Mathematics" label="Mathematics" value="Mathematics"/>
              <t-option key="Biology" label="Biology" value="Biology"/>
              <t-option key="Physics" label="Physics" value="Physics"/>
              <t-option key="Chemistry" label="Chemistry" value="Chemistry"/>
              <!--              :options="['Computer Science','Engineering','Mathematics','Biology','Physics','Chemistry']"-->
              required-mark

            </t-select>
          </t-form-item>

          <t-form-item name="email">
            <t-input
                v-model="formData.email"
                placeholder="请输入邮箱"
                label="邮箱"
                required-mark
            ></t-input>
          </t-form-item>

          <t-form-item name="phoneNumber">
            <t-input type="text"
                     v-model="formData.phoneNumber"
                     placeholder="请输入电话号码"
                     label="电话"
            ></t-input>
          </t-form-item>


          <t-form-item name="password">
            <t-input v-model="formData.password" type="password" clearable placeholder="请输入密码" label="密码">
              <template #prefix-icon>
                <lock-on-icon/>
              </template>
            </t-input>
          </t-form-item>

          <t-form-item name="password2">
            <t-input v-model="formData.password2" type="password" clearable placeholder="请再次输入密码" label="密码">
              <template #prefix-icon>
                <lock-on-icon/>
              </template>
            </t-input>
          </t-form-item>
          <!--        <t-value v-slot="isPasswordVisible" :default-value="false">-->
          <!--          <t-input-->
          <!--              v-model="formData.password"-->
          <!--              :type="isPasswordVisible.value ? 'text' : 'password'"-->
          <!--              label="Password"-->
          <!--              placeholder="Enter your password"-->
          <!--              :rules="[(v) => /^[a-zA-Z0-9/]{8,}$/.test(v) || 'Only numbers, characters, and slash are allowed, minimum length is 8']"-->
          <!--              @click-append-inner="isPasswordVisible.value = !isPasswordVisible.value"-->
          <!--              required-mark-->
          <!--          >-->
          <!--            <template #appendInner>-->
          <!--              <va-icon-->
          <!--                  :name="isPasswordVisible.value ? 'visibility_off' : 'visibility'"-->
          <!--                  size="small"-->
          <!--                  color="primary"-->
          <!--              />-->
          <!--            </template>-->
          <!--          </t-input>-->
          <!--        </t-value>-->
          <!--        <va-value v-slot="isPasswordVisible" :default-value="false">-->
          <!--          <t-input-->
          <!--              v-model="password2"-->
          <!--              :type="isPasswordVisible.value ? 'text' : 'password'"-->
          <!--              label="Password check"-->
          <!--              placeholder="Enter your password again"-->
          <!--              :rules="[(v) => v===password || 'Different from the last password']"-->
          <!--              @click-append-inner="isPasswordVisible.value = !isPasswordVisible.value"-->
          <!--              required-mark-->
          <!--          >-->
          <!--            <template #appendInner>-->
          <!--              <va-icon-->
          <!--                  :name="isPasswordVisible.value ? 'visibility_off' : 'visibility'"-->
          <!--                  size="small"-->
          <!--                  color="primary"-->
          <!--              />-->
          <!--            </template>-->
          <!--          </t-input>-->
          <!--        </va-value>-->
          <t-form-item>
            <t-button theme="primary" shape="round" type="submit" block
                      style="height: 40px; margin-bottom: 8px;margin-top: 20px">注册
            </t-button>
          </t-form-item>
          <!--        <t-button type="submit" class="form-btn" color="priamry" size="small" :disabled="!isValid"-->
          <!--                  @click="handleSubmit">Register-->
          <!--        </t-button>-->
        </t-form>
        <p class="sign-up-label">
          Already have an account?
          <router-link to="Login"><span class="sign-up-link">Log in</span></router-link>
        </p>
      </div>
    </div>

    <t-dialog
        v-model:visible="visible"
        header="请输入手机验证码"
        body="自定义底部按钮，直接传入文字"
        :confirm-btn="null"
        :cancel-btn="null"
    >
      <t-form ref="form1" :data="formData1" :rules="rules1" :colon="true" :label-width="5" @reset="onReset"
              @submit="handleOK"
      >
        <t-form-item name="code">
          <t-input
              v-model="formData1.code"
              placeholder="Enter the verification code"
          >
          </t-input>
        </t-form-item>
        <div style="display: flex;">
          <t-form-item>
            <t-button theme="#f2f3ff" block @click="close" style="width: 60px;margin-right: 20px;margin-left: 240px">
              取消
            </t-button>
          </t-form-item>

          <t-form-item>
            <t-button theme="primary" type="submit" block style="width: 80px;margin-right: 0px">前往验证</t-button>
          </t-form-item>
        </div>
      </t-form>
    </t-dialog>
    <!--  modal-->
    <!--  <va-modal-->
    <!--      v-model="showModal"-->
    <!--      size="large"-->
    <!--      hide-default-actions-->
    <!--      close-button-->

    <!--  >-->
    <!--    <va-form ref="emailVeriRef" style="display: flex;flex-direction: column;gap: 10px">-->
    <!--      <h5 class="va-h5" style="color: grey">-->
    <!--        Verify Your E-mail-->
    <!--      </h5>-->
    <!--      <t-input-->
    <!--          v-model="code"-->
    <!--          type="text"-->
    <!--          class="input"-->
    <!--          placeholder="Enter the verification code"-->
    <!--          label="verification code"-->
    <!--          :rules="[(v) => /^[0-9]{6}$/.test(v) || 'Code must be a six-digit number']"-->
    <!--      ></t-input>-->
    <!--      <va-button-->
    <!--          :disabled="!isValidemailVeri"-->
    <!--          @click="handleOK"-->
    <!--      >-->
    <!--        Verify-->
    <!--      </va-button>-->
    <!--    </va-form>-->

    <!--  </va-modal>-->
  </div>
</template>

<script setup>
// import {getCurrentInstance, ref} from "vue";
// import axios from "axios";
// import NavBarWithOnlyTitle from "@/components/layouts/NavBarWithOnlyTitle.vue";
// // import {useForm, useToast} from "vuestic-ui";

import {getCurrentInstance, inject, reactive, ref} from "vue";
import {LockOnIcon} from "tdesign-icons-vue-next";
import axios from "axios";
import {MessagePlugin} from "tdesign-vue-next";
import router from "@/routers";

// const apiUrl = inject('$API_URL');
const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const apiBaseUrl = globalProperties.$apiBaseUrl;
// alert(apiBaseUrl)
axios.defaults.baseURL = apiBaseUrl;


const rules = {
  username: [{required: true}, {
    validator: (v) => /^([A-Za-z\u4e00-\u9fa5\s]){1,50}$/.test(v),
    message: 'Please enter Chinese, English, and spaces, and the length should be less than 50.'
  }],
  id: [{required: true}, {validator: (v) => /^(\d{8})$/.test(v), message: 'ID must be 8 digits'}],
  email: [{required: true}, {validator: (v) => /[^@]+@[^@]+\.[a-zA-Z]{2,}$/.test(v), message: 'Wrong format'}],
  phoneNumber: [{required: true}, {validator: (v) => /^(\d{11})?$/.test(v), message: 'Phone number must be 11 digits'}],

  password: [{required: true}, {
    validator: (v) => /^[a-zA-Z0-9/]{8,}$/.test(v),
    message: 'Only numbers, characters, and slash are allowed, minimum length is 8'
  }],
  password2: [{required: true}, {
    validator: (v) => v === formData.password,
    message: 'Different from the last password'
  }],
};
const rules1 = {
  code: [{required: true}, {validator: (v) => /^[0-9]{6}$/.test(v), message: 'Code must be a six-digit number'}]
}
const onReset = () => {
  MessagePlugin.success('重置成功');
};

// const code = ref("");
const formData = reactive({
  username: '',
  password: '',
  password2: '',
  department: '',
  id: '',
  email: '',
  phoneNumber: '',
  code: '',
});
const formData1 = reactive({
  code: '',
});
const visible = ref(false);

const close = () => {
  visible.value = false;
};

// const onConfirm = () => {
//   alert(formData1.code)
//   visible.value = false;
//   alert('跳转支付~');
// };
//   const {init} = useToast();
//   const {isValid, validate} = useForm('formRef')
//   const {isValid: isValidemailVeri, validate: validateemailVeri} = useForm('emailVeriRef')
//
//   const username = ref("");
//   const password = ref("");
//   const password2 = ref("");
//   const department = ref("Computer Science");
//   const id = ref("");
//   const email = ref("");
//   const phoneNumber = ref(null)
//   const code = ref("")
//
//   const showModal = ref(false)
//
//   const appConfig = ref(getCurrentInstance().appContext.config.globalProperties).value;
// 获取全局变量 $apiBaseUrl

const handleSubmit = ({validateResult}) => {
  if (validateResult === true) {
    const data = {
      "id": formData.id,
      "name": formData.username,
      "department": formData.department,
      "password": formData.password,
      "email": formData.email,
      "phoneNumber": formData.phoneNumber,

      // "githubUserId": sessionStorage.getItem('git-id'),
      // "githubUserName": sessionStorage.getItem('git-name'),
    }

    axios.post("/register", data)
        .then(() => {
          MessagePlugin.info("Already send the code, please check and enter.");
          visible.value = true;
        })
        // .catch(error => {
        //   if (error.response) {
        //     // 请求已发出，但服务器响应的状态码不在 2xx 范围内
        //     MessagePlugin.error(error.response.data.msg);

        //   } else {
        //     // 一些错误是在设置请求的时候触发
        //     MessagePlugin.error(error.message);

        //   }
        // });
  } else {
    MessagePlugin.warning("Please make sure the input format is correct!")
    // alert('lll')
  }

};
const handleOK = ({validateResult}) => {
  alert(formData1.code)
  if (validateResult === true) {
    axios.post("/registerEmailVerify", {
      "email": formData.email,
      "code": formData1.code
    })
        .then(() => {
          MessagePlugin.success("User created!");
          visible.value = !visible.value
          router.push("/login");
        })
        // .catch((error) => {
        //   if (error.response) {
        //     // 请求已发出，但服务器响应的状态码不在 2xx 范围内
        //     MessagePlugin.error(error.response.data.msg);

        //   } else {
        //     // 一些错误是在设置请求的时候触发
        //     MessagePlugin.error(error.message);

        //   }
        // });
  } else {
    MessagePlugin.warning("Please make sure the input format is correct!")

  }
};
//
// }
//   return {
//     id,
//     email,
//     username,
//     password,
//     password2,
//     department,
//     phoneNumber,
//     code,
//     handleSubmit,
//     handleOK,
//     showModal,
//     isValid,
//     validate, isValidemailVeri, validateemailVeri
//   };


</script>


<style scoped>
.parent-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  margin-top: 50px;
}

.form-container {
  width: 500px;
  background-color: #fff;
  box-shadow: rgba(0, 0, 0, 0.35) 0 5px 15px;
  border-radius: 10px;
  box-sizing: border-box;
  padding: 20px 30px;
  margin-top: 10px; /* 与页面顶部的距离为50px */
}

.title {
  text-align: center;
  font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
  "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
  margin: 10px 0 30px 0;
  font-size: 28px;
  font-weight: 800;
}

#building {
  //background: url("@/assets/login.jpg");
  background-image: linear-gradient(rgba(0,0,0, 0.1), rgba(0,0,0, 0.1)), url("@/assets/login.jpg");

  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}

.sign-up-label {
  margin: 0;
  font-size: 10px;
  color: #747474;
  font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
  "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
}

.sign-up-link {
  margin-left: 1px;
  font-size: 11px;
  text-decoration: underline;
  text-decoration-color: teal;
  color: teal;
  cursor: pointer;
  font-weight: 800;
  font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
  "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
}

</style>