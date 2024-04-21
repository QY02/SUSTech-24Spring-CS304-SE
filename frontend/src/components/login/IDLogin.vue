<template>

    <div style="width: 285px">
      <t-form ref="form" :data="formData" :rules="rules" :colon="true" :label-width="0" @reset="onReset"
              @submit="handleSubmit">
        <div style="margin-top: 18px">
          <!--     <div style="color: #2b54d9">ID</div>-->
          <t-form-item name="account" style="margin-bottom: 30px;">
            <t-input v-model="formData.account" clearable placeholder="请输入账户名">
              <template #prefix-icon>
                <desktop-icon/>
              </template>
            </t-input>
          </t-form-item>
        </div>
        <!--      <div style="color: #2b54d9">密码</div>-->
        <t-form-item name="password">
          <t-input v-model="formData.password" type="password" clearable placeholder="请输入密码"
                   style="margin-bottom: 12px">
            <template #prefix-icon>
              <lock-on-icon/>
            </template>
          </t-input>
        </t-form-item>

        <t-form-item>
          <t-button theme="primary" shape="round" type="submit" block style="height: 40px; margin-bottom: 8px">登录
          </t-button>
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
import {getCurrentInstance, inject, reactive} from 'vue';
import {MessagePlugin} from 'tdesign-vue-next';
import {DesktopIcon, LockOnIcon} from 'tdesign-icons-vue-next';
import axios from "axios";
import router from "@/routers";
// import { ref} from "vue";

const formData = reactive({
  account: '',
  password: '',
});

const onReset = () => {
  MessagePlugin.success('重置成功');
};

// const apiUrl = inject('$API_URL');
// const {isValid, validate} = useForm('formRef')
// const {init} = useToast();

// const password = ref("");
// const id = ref("");
const rules = {
  // account: [{ required: true }, { validator: (v) => /^(\d{8})$/.test(v) , message: 'ID must be 8 digits' }],
  account: [{required: true}, {validator: (v) => /^(\d{8})$/.test(v), message: 'ID must be 8 digits'}],
  // description: [{ validator: (val) => val.length < 10, message: '不能超过 20 个字，中文长度等于英文长度' }],
  password: [{required: true}, {
    validator: (v) => /^[a-zA-Z0-9/]{8,}$/.test(v),
    message: 'Only numbers, characters, and slash are allowed, minimum length is 8'
  }],
};

const handleSubmit = ({validateResult}) => {
  if (validateResult === true) {
    axios.post("/login", {
      id: formData.account,
      password: formData.password
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
          // alert(sessionStorage.getItem('token'));
          if (type === 0) {//管理员
            router.push("/admin/homepage");
          } else {//正常用户
            router.push("/HomePage");
          }
        })
  } else {
    MessagePlugin.warning("Please make sure the input format is correct!")
    // alert('lll')
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