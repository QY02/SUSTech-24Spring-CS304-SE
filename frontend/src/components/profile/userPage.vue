<template>
    <el-card :bordered="false" shadow v-if="!editYes"
        style="display:flex;  align-items: center; justify-content: center; flex-direction: row ; height: 55%;padding-left: 30px; padding-right:30px; margin: 30px;">
        <br>
        <div style="width: 100%;">
            <t-space align="center" :size="40">
                <t-avatar image="https://tdesign.gtimg.com/site/avatar.jpg" size="100px" />
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
        <div style="display: flex; justify-content: flex-end; align-items: center;">
            <t-button style="" @click="() => {
                editYes = !editYes;
            }">
                修改个人信息
            </t-button>
            <ChangePassword></ChangePassword>
        </div>
        <br>
    </el-card>

    <el-card :bordered="false" shadow v-else
        style=" align-items: center; justify-content: center; height: 85%; margin: 30px;">
        <br>
        <br>
        <t-form ref="form" :data="formData" :rules="FORM_RULES" reset-type="initial" colon @reset="onReset" @submit="onSubmit"
            style="width: 80%; margin-left: 10%;">
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
                    <t-button theme="success" type="submit">提交</t-button>
                    <t-button theme="default" variant="base" type="reset">重置</t-button>
                    <t-button theme="default" variant="base" @click="() => {
                        editYes = !editYes;
                    }">
                        取消</t-button>
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
import { ref, onMounted, getCurrentInstance, computed, reactive } from "vue";
import axios from "axios";
import { useModal, useToast, useForm, useColors } from "vuestic-ui";
import { MessagePlugin } from 'tdesign-vue-next';
const { colors } = useColors();
colors.primary = sessionStorage.getItem('primary-color')
// alert(colors.primary)
const { init } = useToast();
const { confirm } = useModal();
const { isValid, validate } = useForm('formRef')
const { isValid: isValidemailVeri, validate: validateemailVeri } = useForm('emailVeriRef')

const visibleEmail = ref(false)

const info = ref({});

const showModalCode = ref(false)

const appConfig = getCurrentInstance().appContext.config.globalProperties;


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
    .catch(() => { })
const FORM_RULES = ref({
    name: [{ required: true, message: '名字不可为空' }],
    phoneNumber: [
        { telnumber: true, message: '请输入正确的手机号码'}
    ],
    department: [{ required: true, message: '学院不可为空' },],
    email:[{ required: true, message: '学院不可为空' },]
});

const onButtonClick = async () => {
    if (validate()) {
        const result = await confirm({
            message: "After submitting, your personal information will update",
            title: "Are you sure to submit?",
            okText: "Yes",
            cancelText: "No",
        });

        if (result) {
            if (email.value === emailOriginal.value) {//邮箱没有改动
                MessagePlugin.info("提交中");
                axios.put("/user/update", {
                    "id": info.value.id,
                    "name": name.value,
                    "phoneNumber": phoneNumber.value,
                },
                    {
                        headers: {
                            token: sessionStorage.getItem('token'),
                        },
                    }
                )
                    .then(() => {
                        MessagePlugin.success("修改成功");
                        location.reload()
                    })
            } else {
                showModalCode.value = !showModalCode.value
            }

        }
    }

};

const formData = reactive({
    name: '',
    email: '',
    phoneNumber: '',
    department: '',
    avatar: [{ url: 'https://tdesign.gtimg.com/site/avatar.jpg' }],
});



const onReset = () => {
    MessagePlugin.success('重置成功');
};

const onSubmit = ({ validateResult, firstError }) => {
    if (validateResult === true) {
        MessagePlugin.success('提交成功');
    } else {
        console.log('Errors: ', validateResult);
        MessagePlugin.warning(firstError);
    }

};



const editYes = ref(false);
</script>



<style>
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
</style>