<template>
  <t-space direction="vertical" size="large" style="width: 100%">
    <div style="max-width: 1000px;margin: 30px auto;">
    <b>请输入活动信息</b>
      <br><br>
    <t-form ref="form" :data="formData" reset-type="initial" @reset="onReset" @submit="onSubmit" :rules="FORM_RULES">
      <div style="max-width: 1000px">
        <t-form-item label="标题" name="name" >
          <t-input v-model="formData.name">标题</t-input>
        </t-form-item>

        <t-form-item label="简介" name="content">
          <t-textarea v-model="formData.content" placeholder="简单描述项目内容" clearable />
        </t-form-item>
        <t-form-item label="类型" name="type">
          <t-radio-group v-model="formData.type">
            <t-radio value="1">表演</t-radio>
            <t-radio value="2">讲座</t-radio>
            <t-radio value="3">比赛</t-radio>
            <t-radio value="4">其他</t-radio>
          </t-radio-group>
        </t-form-item>

        <t-form-item label="海报" name="poster">
          <t-upload v-model="formData.poster"
                    action="https://service-bv448zsw-1257786608.gz.apigw.tencentcs.com/api/upload-demo" theme="image"
                    tips="请选择单张图片文件上传" accept="image/*"></t-upload>
        </t-form-item>
        <event-session v-model:sessionData="formData.eventSessionData"></event-session>
      </div>

      <t-form-item style="margin: 30px auto">
        <t-space size="small">
          <t-button theme="primary" type="submit">提交</t-button>
          <t-button theme="default" variant="base" type="reset">重置</t-button>
        </t-space>
      </t-form-item>
    </t-form>
    </div>
  </t-space>
</template>
<script setup>
import { ref, inject } from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import axios from "axios";
import EventSession from "@/components/event/EventSession.vue";

const apiUrl = inject('$API_URL');

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
  registration_time_range: ["2023-2-1 00:00:00", "2023-2-1 10:00:00"],
  event_time_range: ["2023-02-01 00:00:00", "2023-02-01 10:00:00"],
  count_range_of_people: [1000, 1000],
  seat_map_id: '12',
  venue: '12345ljkdq',
  location: 'ahgsjkd',
  visible: false,
}));

const formData = ref({
  name: '',
  content: '',
  type: '',
  poster: [{ url: 'https://tdesign.gtimg.com/site/avatar.jpg' }],
  eventSessionData:session
});

const updateSessionData=(val)=>{
  formData.value.eventSessionData=val
}
const onReset = () => {
  MessagePlugin.success('重置成功');
};

const onSubmit = ({ validateResult, firstError }) => {
  alert(JSON.stringify(formData.value))
  if (validateResult === true) {
    // console.log(formData)
    axios.post(`${apiUrl}/event/add`,{data:formData.value}).then(
      response => {
        console.log(response)
      }
    ).catch(error => alert(error))
    MessagePlugin.success('提交成功');
  } else {
    console.log('Errors: ', validateResult);
    MessagePlugin.warning(firstError);
  }
};
</script>
