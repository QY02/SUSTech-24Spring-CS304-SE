<template>
  <NavBarWithOnlyTitle></NavBarWithOnlyTitle>
<!--  <div class="parent-container">-->
<!--    <div class="form-container">-->
<!--      <p class="title">Register</p>-->
<!--      <va-form class="form" ref="formRef">-->
<!--        <va-input type="text" label="user name" class="va-input"-->
<!--                  v-model="username"-->
<!--                  placeholder="Enter your user name"-->
<!--                  :rules="[(v) => /^([A-Za-z\u4e00-\u9fa5\s]){1,50}$/.test(v) || 'Please enter Chinese, English, and spaces, and the length should be less than 50.']"-->
<!--                  required-mark-->
<!--        ></va-input>-->
<!--        <va-input type="text" label="id" class="va-input"-->
<!--                  v-model="id"-->
<!--                  placeholder="Enter your 8-digit ID"-->
<!--                  :rules="[(v) => /^(\d{8})$/.test(v) || 'ID must be 8 digits']"-->
<!--                  required-mark-->
<!--        ></va-input>-->
<!--        <va-select-->
<!--            v-model="department"-->
<!--            class="mb-6"-->
<!--            label="department"-->
<!--            :options="['Computer Science','Engineering','Mathematics','Biology','Physics','Chemistry']"-->
<!--            required-mark-->
<!--        />-->
<!--        <va-input type="email" label="e-mail" class="va-input"-->
<!--                  v-model="email"-->
<!--                  placeholder="Enter your e-mail"-->
<!--                  :rules="[(v) => /[^@]+@[^@]+\.[a-zA-Z]{2,}$/.test(v) || 'Wrong format']"-->
<!--                  required-mark-->
<!--        ></va-input>-->
<!--        <va-input type="text" label="phone Number" class="va-input"-->
<!--                  v-model="phoneNumber"-->
<!--                  placeholder="Enter your phone number"-->
<!--                  :rules="[(v) => /^(\d{11})?$/.test(v) || 'Phone number must be 11 digits']"-->
<!--        ></va-input>-->
<!--        <va-value v-slot="isPasswordVisible" :default-value="false">-->
<!--          <va-input-->
<!--              v-model="password"-->
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
<!--          </va-input>-->
<!--        </va-value>-->
<!--        <va-value v-slot="isPasswordVisible" :default-value="false">-->
<!--          <va-input-->
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
<!--          </va-input>-->
<!--        </va-value>-->
<!--        <va-button type="submit" class="form-btn" color="priamry" size="small" :disabled="!isValid"-->
<!--                   @click="handleSubmit">Register-->
<!--        </va-button>-->
<!--      </va-form>-->
<!--      <p class="sign-up-label">-->
<!--        Already have an account?-->
<!--        <router-link to="Login"><span class="sign-up-link">Log in</span></router-link>-->
<!--      </p>-->
<!--    </div>-->
<!--  </div>-->

<!--  &lt;!&ndash;  modal&ndash;&gt;-->
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
<!--      <va-input-->
<!--          v-model="code"-->
<!--          type="text"-->
<!--          class="input"-->
<!--          placeholder="Enter the verification code"-->
<!--          label="verification code"-->
<!--          :rules="[(v) => /^[0-9]{6}$/.test(v) || 'Code must be a six-digit number']"-->
<!--      ></va-input>-->
<!--      <va-button-->
<!--          :disabled="!isValidemailVeri"-->
<!--          @click="handleOK"-->
<!--      >-->
<!--        Verify-->
<!--      </va-button>-->
<!--    </va-form>-->

<!--  </va-modal>-->

</template>

<script>
// import {getCurrentInstance, ref} from "vue";
// import axios from "axios";
import NavBarWithOnlyTitle from "@/components/layouts/NavBarWithOnlyTitle.vue";
// // import {useForm, useToast} from "vuestic-ui";
// import router from "@/routers";


export default {
  components: {NavBarWithOnlyTitle},

  setup() {
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
  //   // 获取全局变量 $apiBaseUrl
  //   axios.defaults.baseURL = appConfig.$apiBaseUrl;
  //   const handleSubmit = () => {
  //     if (validate()) {
  //       const data = ref({})
  //       data.value = {
  //         "id": id.value,
  //         "name": username.value,
  //         "department": department.value,
  //         "password": password.value,
  //         "email": email.value,
  //         "phoneNumber": phoneNumber.value,
  //         "githubUserId": sessionStorage.getItem('git-id'),
  //         "githubUserName": sessionStorage.getItem('git-name'),
  //       }
  //
  //       axios.post("/register", data.value)
  //           .then(() => {
  //             init({
  //               message: "Already send the code, please check and enter.", color: "info"
  //             });
  //             showModal.value = true
  //           })
  //           .catch(error => {
  //             if (error.response) {
  //               // 请求已发出，但服务器响应的状态码不在 2xx 范围内
  //               init({message: error.response.data.msg, color: "danger"})
  //             } else {
  //               // 一些错误是在设置请求的时候触发
  //               init({message: error.message, color: "danger"})
  //             }
  //           });
  //     }
  //
  //   };
  //   const handleOK = () => {
  //     if (validateemailVeri()) {
  //       axios.post("/registerEmailVerify", {
  //         "email": email.value,
  //         "code": code.value
  //       })
  //           .then(() => {
  //             init({
  //               message: "User created!", color: "success"
  //             });
  //             showModal.value = !showModal.value
  //             router.push("/login");
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
  //     }
  //
  //   }
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
  },
};
</script>


<style scoped>
.parent-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
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

.form {
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