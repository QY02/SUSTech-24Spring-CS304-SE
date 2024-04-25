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

            <t-upload
                ref="uploadRef"
                accept="image/*"
                v-model="formData.poster"
                :action="upload.actionUrl"
                :data="upload.data"
                :abridge-name="[10, 8]"
                :auto-upload="false"
                :size-limit="{ size: 10, unit: 'MB' }"
                tips="请选择单张图片文件上传"
                @fail="handleFail"
                @success="handleSuccess"
                @one-file-success="onOneFileSuccess"
                theme="image"
                :format-response="formatResponse"
                :showThumbnail="true"
                @remove="deleteImg"
                class="upload_image"
                :uploadButton="null"
            />
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
import {ref, getCurrentInstance, reactive} from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import axios from "axios";
import EventSession from "@/components/event/EventSession.vue";
import router from "@/routers/index.js";


const token = sessionStorage.getItem('token')
const uid = sessionStorage.getItem('uid')
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
  count_range_of_people: [20, 100],
  seat_map_id: '12',
  venue: '12345ljkdq',
  location: 'ahgsjkd',
  visible: false,
  additional_information_required:[
      {"name": "手机号", "nameEng": "phoneNumber", "required": true,
        "rules": [{"telnumber": true ,"message": "请输入正确的手机号码"}], "value": ""},
    {"name": "书院", "nameEng": "college", "required": true, "rules": null, "value": ""}],
}));

const formData = ref({
  name: '',
  content: '',
  type: '0',
  publisher_id: uid,
  poster: [

  ],
  visible: false,
  event_policy:'',
});


const uploadRef = ref();
const upload = reactive({
  actionUrl: window.location.origin + `这里写后端给的接口`,
  data: {
    意见反馈id: "",
  },//data里面是上传图片时，需要带的参数
});
const uploadFiles = () => {
  uploadRef.value.uploadFiles();
};
const handleFail = ({ file }) => {
  MessagePlugin.error(`文件 ${file.name} 上传失败`);
};
const handleSuccess = (params) => {
  MessagePlugin.success("提交成功");//这个函数是在uploadFiles 成功调用之后才会调用
};
const onOneFileSuccess = (params) => {
  //   console.log("onOneFileSuccess", params);
};

//删除图片
const deleteImg = (e) => {

};

//提交反馈
const submitPoster = () => {
  //这里先请求文字内容部分的接口，获取后端返回的id
  // $_updateFeedbackDetail({
  //   意见反馈id: "",
  //   data: {
  //     子系统名: palte.value,
  //     内容: textareaValue.value,
  //   },
  // }).then((res) => {
  //   if (res.data.msg == "成功") {
  //     upload.data.意见反馈id = res.data.data;
  //     if (files.value.length > 0) {//为什么要写>0，因为有可能用户不上传图片，那就不需要调用上传图片的函数了
  //       uploadFiles();//后端返回id成功之后，再调用上传图片的函数
  //     }
  //   }
  // });
};

// res.url 图片地址；res.uploadTime 文件上传时间；res.error 上传失败的原因
function formatResponse(res) {
  // 响应结果添加上传时间字段，用于 UI 显示
  res.uploadTime = getCurrentDate();
  return res;
}
function getCurrentDate(needTime = false) {
  const d = new Date();
  let month = d.getMonth() + 1;
  month = month < 10 ? Number(`0${month}`) : month;
  const date = `${d.getFullYear()}-${month}-${d.getDate()}`;
  const time = `${d.getHours()}:${d.getMinutes()}:${d.getSeconds()}`;
  if (needTime) return [date, time].join(' ');
  return date;
}
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
    ).catch();
  } else {
    console.log('Errors: ', validateResult);
    MessagePlugin.warning(firstError);
  }
};
</script>
