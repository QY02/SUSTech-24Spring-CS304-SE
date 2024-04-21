<template>
    <el-card :bordered="false" shadow v-if="!editYes"
        style="display:flex;  align-items: center; justify-content: center; flex-direction: row ; height: 50%;padding-left: 30px; padding-right:30px; margin: 30px;">
        <br>
        <div style="width: 100%;">
            <t-space align="center" :size="40">
                <t-avatar image="https://tdesign.gtimg.com/site/avatar.jpg" size="100px" />
                <t-descriptions title="个人信息" :column="1">
                    <t-descriptions-item label="Name">TDesign</t-descriptions-item>
                    <br>
                    <t-descriptions-item label="Telephone Number">139****0609</t-descriptions-item>
                    <t-descriptions-item label="Email">China Tencent Headquarters</t-descriptions-item>
                    <t-descriptions-item label="Department">Shenzhen Penguin Island D1 4A Mail
                        Center</t-descriptions-item>
                </t-descriptions>
                <br>
                <br>
            </t-space>
        </div>
        <div style="display: flex; justify-content: flex-end; align-items: center;">
            <t-button style="" @click="() => {
                editYes = !editYes;
            }">
                修改个人信息
            </t-button>
            <ChangePassword></ChangePassword>
        </div>
    </el-card>

    <el-card :bordered="false" shadow v-else
        style=" align-items: center; justify-content: center; height: 80%; margin: 30px;">
        <br>
        <br>
        <t-form ref="form" :data="formData" reset-type="initial" colon @reset="onReset" @submit="onSubmit"
            style="width: 80%; margin-left: 10%;">
            <t-form-item label="姓名" name="name">
                <t-input v-model="formData.name"></t-input>
            </t-form-item>

            <t-form-item label="学院" name="college">
                <t-select v-model="formData.college" :options="COLLEGE_OPTIONS" clearable></t-select>
            </t-form-item>

            <t-form-item label="电话" name="phone_number">
                <t-input></t-input>
            </t-form-item>

            <t-form-item label="邮箱" name="email">
                <t-input :disabled="true" :placeholder="old_email"></t-input>
                <t-button @click="()=>visibleEmail=true">修改邮箱</t-button>
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
    </el-card>
    <ChangeEmail v-model:visible="visibleEmail" v-model:old_email="old_email"></ChangeEmail>
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
const { isValid: isValidmailPsw, validate: validatemailPsw } = useForm('mailPswRef')
const { isValid: isValidoldPsw, validate: validateoldPsw } = useForm('oldPswRef')

const visibleEmail=ref(false)

const info = ref({});
const name = ref("");
const email = ref("");
const emailOriginal = ref("")
const phoneNumber = ref("");
const department = ref("");

const showModalCode = ref(false)

const appConfig = getCurrentInstance().appContext.config.globalProperties;




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


const userTypeText = computed(() => {
    const userTypeMap = {
        0: 'administrator',
        1: 'teacher',
        2: 'student',
        '-1': 'visitor'
    };
    return userTypeMap[info.value.type] || '';
});


const handleGithub = () => {
    const token = sessionStorage.getItem('token'); // 替换为你的实际 token
    window.location.href = `${baseURL}/connectToGithub?token=` + token;
};

const formDisabled = ref(true);
const old_email = ref('25499')
const formData = reactive({
    name: '',
    message: true,
    gender: '',
    course: [],
    college: '',
    personalProfile: '',
    address1: undefined,
    address2: undefined,
    gradePoint: undefined,
    date: '',
    avatar: [{ url: 'https://tdesign.gtimg.com/site/avatar.jpg' }],
});

const COLLEGE_OPTIONS = [
    { label: '学院 A', value: 1 },
    { label: '学院 B', value: 2 },
    { label: '学院 C', value: 3 },
];


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

onMounted(
            () => {
                // 发起请求
                // alert(sessionStorage.getItem('uid'))
                // alert(sessionStorage.getItem('token'))
                axios.post(`/user/get/${sessionStorage.getItem('uid')}`, {}, {
                    headers: {
                        token: sessionStorage.getItem('token'),
                    },
                })
                    .then((response) => {
                        info.value = response.data.data;
                        name.value = info.value.name;
                        email.value = info.value.email;
                        emailOriginal.value = email.value;
                        phoneNumber.value = info.value.phoneNumber;
                        department.value = info.value.department;
                    })
            });


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