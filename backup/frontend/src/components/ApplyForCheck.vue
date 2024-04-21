<template>
  <t-space>
    <t-button theme="primary" @click="visibleBody = true">申请</t-button>
  </t-space>
  <t-dialog
      v-model:visible="visibleBody"
      attach="body"
      header="请填写活动信息"
      destroy-on-close="true"
      width="50%"
      :cancel-btn="null"
      :confirm-btn="null"
  >
    <template #body>
      <t-space direction="vertical" size="large">
        <t-form
            ref="form"
            :data="formData"
            reset-type="initial"
            @reset="onReset"
            @submit="onSubmit"
            :rules="FORM_RULES"
            label-width="190px"
        >
          <t-form-item label="标题" name="name">
            <t-input v-model="formData.name">标题</t-input>
          </t-form-item>

          <t-form-item label="简介" name="profile">
            <t-textarea v-model="formData.profile" placeholder="简单描述项目内容" clearable/>
          </t-form-item>

          <t-form-item label="类型" name="type">
            <t-radio-group v-model="formData.type">
              <t-radio value="1">表演</t-radio>
              <t-radio value="2">讲座</t-radio>
              <t-radio value="3">比赛</t-radio>
              <t-radio value="4">其他</t-radio>
            </t-radio-group>
          </t-form-item>
          <t-form-item label="是否需要报名" name="registration_required">
            <t-switch v-model="formData.registration_required" :label="['是', '否']"></t-switch>
          </t-form-item>
          <t-form-item label="报名开始时间-报名结束时间" name="register_time">
            <t-date-range-picker enable-time-picker allow-input clearable v-model="formData.register_time" @pick="onPick" @change="onChange"
                                 :disabled="!formData.registration_required"/>
          </t-form-item>
          <t-form-item label="开始时间-结束时间" name="time">
            <t-date-range-picker enable-time-picker allow-input clearable v-model="formData.time" @pick="onPick" @change="onChange"/>
          </t-form-item>

          <t-form-item label="人数" name="countOfPeople">
            <t-range-input v-model="formData.countOfPeople" :placeholder="['最小值','最大值']"/>
          </t-form-item>
          <t-form-item label="地址" name="venue">
            <t-input v-model="formData.venue">地址</t-input>
          </t-form-item>
          <t-form-item label="是否可见" name="visible">
            <t-switch v-model="formData.visible" :label="['是', '否']"></t-switch>
          </t-form-item>
          <t-form-item label="海报" name="avatar">
            <t-upload
                v-model="formData.poster"
                action="https://service-bv448zsw-1257786608.gz.apigw.tencentcs.com/api/upload-demo"
                theme="image"
                tips="请选择单张图片文件上传"
                accept="image/*"
            ></t-upload>
          </t-form-item>

          <t-form-item>
            <t-space size="small">
              <t-button theme="primary" type="submit">提交</t-button>
              <t-button theme="default" variant="base" type="reset">重置</t-button>
            </t-space>
          </t-form-item>
        </t-form>
      </t-space>

    </template>
  </t-dialog>
</template>
<script setup>
import {ref, reactive, inject} from 'vue';
import {MessagePlugin} from 'tdesign-vue-next';
import axios from "axios";

const apiUrl = inject('$API_URL');


const FORM_RULES = {
  name: [{required: true, message: '标题必填'}],
  profile: [{required: true, message: '简介必填'}],
  type: [{required: true, message: '类型必填'}],
  time: [{required: true, message: '活动时间必填'}],
  countOfPeople: [{required: true, message: '人数必填'}],
  venue: [{required: true, message: '地址必填'}],
};

const onPick = (value, context) => console.log('onPick:', value, context);
const onChange = (value, context) => {
  console.log('onChange:', value, context);
  console.log(
      'timestamp:',
      context.dayjsValue.map((d) => d.valueOf()),
  );
  console.log(
      'YYYYMMDD:',
      context.dayjsValue.map((d) => d.format('YYYYMMDD')),
  );
};
const visibleBody = ref(false);
const formData = reactive({
  name: '',
  profile: '',
  type: '',
  countOfPeople:undefined,
  registration_required: false,
  register_time: '',
  time: '',
  venue:'',
  poster: [{url: 'https://tdesign.gtimg.com/site/avatar.jpg'}],
  visible: true,
});


const onReset = () => {
  MessagePlugin.success('重置成功');
};

const onSubmit = ({validateResult, firstError}) => {
  if (validateResult === true) {
    // console.log(formData)
    axios.get(`${apiUrl}/bug/single`).then(
        response => {
          console.log(response)
        }
    )
    .catch(error => {})
    MessagePlugin.success('提交成功');
  } else {
    console.log('Errors: ', validateResult);
    MessagePlugin.warning(firstError);
  }
};
</script>
