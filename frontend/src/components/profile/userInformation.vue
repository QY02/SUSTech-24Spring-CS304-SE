<template>
  <el-card :bordered="false" shadow v-if="!editYes"
           style="display:flex;  align-items: center; justify-content: center; flex-direction: row ;padding-left: 30px; padding-right:30px; margin: 30px;">
    <br>
    <div style="width: 100%;">
      <t-space align="center" :size="40">
        <t-avatar image="https://tdesign.gtimg.com/site/avatar.jpg" size="100px"/>
        <t-descriptions title="个人信息" :column="1">
          <t-descriptions-item label="Name">{{ formData.name }}</t-descriptions-item>
          <br>
          <t-descriptions-item label="Telephone Number">{{ formData.phoneNumber }}</t-descriptions-item>
          <t-descriptions-item label="Email">{{ formData.email }}</t-descriptions-item>
          <t-descriptions-item label="Department">{{ formData.department }}</t-descriptions-item>
        </t-descriptions>
        <br>
        <br>
      </t-space>
    </div>
    <br>
    <!--    <div style="display: flex; justify-content: flex-end; align-items: center;">-->
    <div >

      <t-button style="" class="btn1" @click="() => {
                editYes = !editYes;
            }">
        修改个人信息
      </t-button>
      <t-popup content="退出登录">
      <t-button
          style="position: fixed; right: 50px; top: 100px"
          theme="default"
          variant="base"
          @click="logout"
      >
        <logout-icon></logout-icon>
      </t-button>
      </t-popup>

      <ChangePassword></ChangePassword>
    </div>
<!--    <div class="button-container">-->
<!--      <t-button-->
<!--          class="btn"-->
<!--          theme="primary"-->
<!--          variant="outline"-->
<!--          @click="router.push('/historyEvents');"-->
<!--      >-->
<!--        历史记录-->
<!--      </t-button>-->

<!--      <t-button-->
<!--          class="btn"-->
<!--          theme="default"-->
<!--          variant="outline"-->
<!--          @click="router.push('/myPublishes');"-->
<!--      >-->
<!--        我的发布-->
<!--      </t-button>-->
<!--      <t-button-->
<!--          class="btn"-->
<!--          theme="warning"-->
<!--          variant="outline"-->
<!--          @click="router.push('/myFavorites');"-->
<!--      >-->
<!--        我的收藏-->
<!--      </t-button>-->
<!--      <t-button-->
<!--          class="btn"-->
<!--          theme="success"-->
<!--          variant="outline"-->
<!--          @click="router.push('/myOrderRecords');"-->
<!--      >-->
<!--        我的预定-->
<!--      </t-button>-->
<!--    </div>-->
    <!--    </div>-->
    <br>
  </el-card>

  <el-card :bordered="false" shadow v-else
           style=" align-items: center; justify-content: center; height: 85%; margin: 30px;">
    <br>
    <br>
    <t-form ref="form" :data="formData" :rules="FORM_RULES" reset-type="initial" colon @reset="onReset"
            @submit="onSubmit" style="width: 80%; margin-left: 10%;">
      <t-form-item label="姓名" name="name">
        <t-input v-model="formData.name"></t-input>
      </t-form-item>

      <t-form-item label="学院" name="department">
        <t-input v-model="formData.department"></t-input>
      </t-form-item>

      <t-form-item label="邮箱" name="email">
        <t-input :disabled="true" v-model="formData.email"></t-input>
        <t-button @click="() => visibleEmail = true">修改邮箱</t-button>
      </t-form-item>

      <t-form-item label="电话" name="phoneNumber">
        <t-input v-model="formData.phoneNumber"></t-input>
      </t-form-item>

      <t-form-item label="头像" name="avatar">
        <t-upload v-model="formData.avatar"
                  action="https://service-bv448zsw-1257786608.gz.apigw.tencentcs.com/api/upload-demo" theme="image"
                  tips="请选择单张图片文件上传" accept="image/*"></t-upload>
      </t-form-item>

      <t-form-item>
        <t-space :size="20">
          <t-button theme="success" type="submit" :loading="loadingg">提交</t-button>
          <t-button theme="default" variant="base" type="reset">重置</t-button>
          <t-button theme="default" variant="base" @click="() => {
                        editYes = !editYes;
                    }">
            取消
          </t-button>
        </t-space>
      </t-form-item>
    </t-form>
    <br>
  </el-card>
  <ChangeEmail v-model:visible="visibleEmail" v-model:old_email="formData.email"></ChangeEmail>
</template>


<script setup>
import ChangePassword from "@/components/profile/ChangePassword.vue";
import ChangeEmail from "./ChangeEmail.vue";
import {ref, onMounted, reactive} from "vue";
import axios from "axios";
import {MessagePlugin} from 'tdesign-vue-next';
import router from "@/routers/index.js";
import {LogoutIcon} from "tdesign-icons-vue-next";

const visibleEmail = ref(false)

const info = ref({});

const showModalCode = ref(false)
const logout= () =>{
  sessionStorage.removeItem('token');
  MessagePlugin.success('退出登录成功！');
  router.push('/login');
}

const loadingg = ref(false)
axios.post(`/user/get/${sessionStorage.getItem('uid')}`, {}, {
  headers: {
    token: sessionStorage.getItem('token'),
  },
})
    .then((response) => {
      info.value = response.data.data;
      formData.name = info.value.name;
      formData.email = info.value.email;
      formData.phoneNumber = info.value.phoneNumber;
      formData.department = info.value.department;
    })
    .catch(() => {
    })
const FORM_RULES = ref({
  name: [{required: true, message: '名字不可为空'}],
  phoneNumber: [
    {telnumber: true, message: '请输入正确的手机号码'}
  ],
  department: [{required: true, message: '学院不可为空'},],
  email: [{required: true, message: '学院不可为空'},]
});

const formData = reactive({
  name: '',
  email: '',
  phoneNumber: '',
  department: '',
  avatar: [{url: 'https://tdesign.gtimg.com/site/avatar.jpg'}],
});


const onReset = () => {
  MessagePlugin.success('重置成功');
};

const submitInfo = () => {
  loadingg.value = true
  axios.put(`/user/update`, {
    "id": sessionStorage.getItem('uid'),
    "phoneNumber": formData.phoneNumber,
    "department": formData.department,
  }, {
    headers: {
      token: sessionStorage.getItem('token'),
    },
  }).then(() => {
    MessagePlugin.success('提交成功');
    editYes.value = false;
    loadingg.value = false;
  }).catch(() => {
    loadingg.value = false;
  })
}

const onSubmit = ({validateResult, firstError}) => {
  if (validateResult === true) {
    submitInfo()
  } else {
    console.log('Errors: ', validateResult);
    MessagePlugin.warning(firstError);
  }

};


const editYes = ref(false);
</script>


<style scoped>
.inform {
  padding: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  /* 控制元素之间的间距 */
}

.px-2 {
  font-size: large;
  font-weight: bolder;
}

.github-login-button {
  border-radius: 10px;
  box-sizing: border-box;
  padding: 5px 10px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  gap: 5px;
  border: 2px solid #747474;
}

.personalInformationForm {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.btn1 {
  margin-left: 20px;
  flex-grow: 1;
  width: 120px;
}
.btn {
  margin-left: 20px;
  flex-grow: 1;
}

.button-container {
  display: flex;
  justify-content: space-around; /* 按钮之间平均分配空间 */
  align-items: center; /* 垂直居中对齐按钮 */
  margin-top: 20px; /* 与上面的组件保持一定距离 */
}
</style>