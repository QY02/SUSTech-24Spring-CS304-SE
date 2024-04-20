<template>
  <t-space direction="vertical" size="large" style="width: 100%">
    <div style="max-width: 1000px;margin: 30px auto;">
      <h1 style="font-size:30px;">申请新活动</h1>
    <b>请输入活动信息</b>
      <br><br>
    <t-form ref="form" :data="formData" reset-type="initial" @reset="onReset" @submit="onSubmit" :rules="FORM_RULES">
      <div style="max-width: 1000px">
        <t-form-item label="标题" name="name" >
          <t-input v-model="formData.name">标题</t-input>
        </t-form-item>

        <t-form-item label="简介" name="content">
          <t-textarea v-model="formData.content" placeholder="请简单描述项目内容" clearable />
        </t-form-item>
        <t-form-item label="类型" name="type">
          <t-radio-group v-model="formData.type">
            <t-radio value="0">表演</t-radio>
            <t-radio value="1">讲座</t-radio>
            <t-radio value="2">比赛</t-radio>
            <t-radio value="3">其他</t-radio>
          </t-radio-group>
        </t-form-item>

        <t-form-item label="海报" name="poster">
          <t-upload v-model="formData.poster"
                    action="https://service-bv448zsw-1257786608.gz.apigw.tencentcs.com/api/upload-demo" theme="image"
                    tips="请选择单张图片文件上传" accept="image/*"></t-upload>
        </t-form-item>
        <t-form-item label="是否可见" name="visible_event">
          <t-switch v-model="formData.visible" :label="['是', '否']"></t-switch>
        </t-form-item>
        <t-form-item label="规则" name="event_policy">
          <t-textarea v-model="formData.event_policy" placeholder="请简单描述项目规则" clearable />
        </t-form-item>
        <event-session v-model:sessionData="eventSessionData"></event-session>
      </div>
<!--      <div>-->
<!--        {{formData}}-->
<!--        {{eventSessionData}}-->
<!--      </div>-->
      <!--    改密码-->


      <div style="display: flex; justify-content: center; align-items: center;">
        <t-form-item style="margin: 30px auto;">
          <t-space size="large">
            <t-button theme="success" type="submit" size="large">提交</t-button>
            <t-tooltip content="仅重置活动相关信息，场次信息不重置" theme="warning">
              <t-button variant="outline" type="reset" size="large">重置</t-button>
            </t-tooltip>
          </t-space>
        </t-form-item>
      </div>

    </t-form>
    </div>
  </t-space>
</template>
<script setup>
import {ref,getCurrentInstance} from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import axios from "axios";
import EventSession from "@/components/event/EventSession.vue";
import router from "@/routers/index.js";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const apiBaseUrl = globalProperties.$apiBaseUrl;
const token = sessionStorage.getItem('token')
const uid = sessionStorage.getItem('uid')
axios.defaults.baseURL = apiBaseUrl;

const FORM_RULES = {
  name: [{ required: true, message: '标题必填' }],
  content: [{ required: true, message: '简介必填' }],
  type: [{ required: true, message: '类型必填' }],
  poster: [{ required: true, message: '必须上传海报' }],
};

let cnt=1
const session=new Array(8).fill(null).map(() => ({
  key: String(cnt++),
  registration_required: true,
  registration_time_range:["2023-02-01 00:00:00", "2023-02-01 10:00:00"],
  event_time_range: ["2023-02-01 00:00:00", "2023-02-01 10:00:00"],
  count_range_of_people: [1000, 1000],
  seat_map_id: '12',
  venue: '12345ljkdq',
  location: 'ahgsjkd',
  visible: false,
  additional_information_required:[
      {"name": "手机号", "nameEng": "phoneNumber", "required": true, "rules": [{"telnumber": true ,"message": "请输入正确的手机号码"}], "value": ""},
    {"name": "书院", "nameEng": "college", "required": true, "rules": null, "value": ""}],
}));

const formData = ref({
  name: '',
  content: '',
  type: '0',
  publisher_id: uid,
  poster: [{ url: 'https://tdesign.gtimg.com/site/avatar.jpg' }],
  visible: false,
  event_policy:'',
});
const eventSessionData= ref(session)
const onReset = () => {
  MessagePlugin.success('重置成功');
};

const onSubmit = ({ validateResult, firstError }) => {
  alert(JSON.stringify(formData.value))
  if (validateResult === true) {
    // console.log(formData)
    axios.post(`/event/add`,{
      "event":formData.value,
      "sessions":eventSessionData.value
    },{
      headers: {
        token: token
      }
    }
    ).then(
      response => {
        console.log(response)
        MessagePlugin.success('提交成功');
        router.push("/HomePage");
      }
    ).catch((error) => {
      if (error.response) {
        // 请求已发出，但服务器响应的状态码不在 2xx 范围内
        MessagePlugin.error(error.response.data.msg)
      } else {
        // 一些错误是在设置请求的时候触发
        MessagePlugin.error(error.message)
      }
    });
  } else {
    console.log('Errors: ', validateResult);
    MessagePlugin.warning(firstError);
  }
};
</script>
