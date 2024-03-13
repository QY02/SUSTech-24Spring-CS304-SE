<template >
  <div style="display:flex;justify-content: center;">
     <t-space direction="vertical" size="large" style="width:90%;">
       <b>请输入活动信息</b>
        <t-form
            ref="form"
            :data="formData"
            reset-type="initial"
            @reset="onReset"
            @submit="onSubmit"
            :rules="FORM_RULES"
        >
          <t-form-item label="标题" name="name">
            <t-input v-model="formData.name">标题</t-input>
          </t-form-item>

          <t-form-item label="简介" name="content">
            <t-textarea v-model="formData.content" placeholder="简单描述项目内容" clearable/>
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
            <t-upload
                v-model="formData.poster"
                action="https://service-bv448zsw-1257786608.gz.apigw.tencentcs.com/api/upload-demo"
                theme="image"
                tips="请选择单张图片文件上传"
                accept="image/*"
            ></t-upload>
          </t-form-item>
          <div style="width: 100%;">
            <session-event></session-event>
          </div>

          <t-form-item style="display: flex;justify-content: center">
            <t-space size="small">
              <t-button theme="primary" type="submit">提交</t-button>
              <t-button theme="default" variant="base" type="reset">重置</t-button>
            </t-space>
          </t-form-item>
        </t-form>
      </t-space>
  </div>
</template>
<script setup>
import {ref, inject} from 'vue';
import {MessagePlugin} from 'tdesign-vue-next';
import axios from "axios";
import SessionEvent from "@/components/Event/EventSession.vue";

const apiUrl = inject('$API_URL');


const FORM_RULES = {
  name: [{required: true, message: '标题必填'}],
  content: [{required: true, message: '简介必填'}],
  type: [{required: true, message: '类型必填'}],
  poster: [{required: true, message: '必须上传海报'}],
};


const formData = ref({
  name: '',
  content: '',
  type: '',
  poster: [{url: 'https://tdesign.gtimg.com/site/avatar.jpg'}],
});


const onReset = () => {
  MessagePlugin.success('重置成功');
};

const onSubmit = ({validateResult, firstError}) => {
  if (validateResult === true) {
    // console.log(formData)
    axios.post(`${apiUrl}/event/add`).then(
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
