<template>

  <div id="event">
    <t-card
        v-for="(item,index) in events"
        :key="index"
        :title="item['content']" :subtitle="item['eventPolicy']" :cover=cover :style="{ width: '400px' }" hover-shadow>
      <template #actions>
        <t-dropdown :options="options" :min-column-width="112" @click="clickHandler">
          <div class="tdesign-demo-dropdown-trigger">
            <t-button variant="text" shape="square">
              <more-icon/>
            </t-button>
          </div>
        </t-dropdown>
      </template>
      <template #footer>
        <t-row :align="'middle'" justify="center" style="gap: 24px;">
          <t-col flex="auto" style="display: inline-flex; justify-content: center;">
            <t-button variant="text" shape="square">
              <thumb-up-icon/>
            </t-button>
          </t-col>

          <t-col flex="auto" style="display: inline-flex; justify-content: center">
            <t-button variant="text" shape="square">
              <chat-icon/>
            </t-button>
          </t-col>

          <t-col flex="auto" style="display: inline-flex; justify-content: center">
            <t-button variant="text" shape="square">
              <share-icon/>
            </t-button>
          </t-col>
        </t-row>
      </template>
    </t-card>


  </div>

</template>


<script setup>

import {ThumbUpIcon, ChatIcon, ShareIcon, MoreIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import axios from "axios";
import {defineComponent, inject, ref} from "vue";
import router from "@/routers/index.js";

const cover = 'https://tdesign.gtimg.com/site/source/card-demo.png';
const events = ref([]);
const options = [
  {
    content: '操作一',
    value: 1,
  },
  {
    content: '操作二',
    value: 2,
  },
];

const clickHandler = (data) => {
  MessagePlugin.success(`选中【${data.content}】`);
};

const apiUrl = inject('$API_URL');

// 获取全局变量 $apiBaseUrl
axios.defaults.baseURL = apiUrl;
axios.post(`/event/getAllEvents`, {}, {
  params: {},
  headers: {
    token: sessionStorage.getItem('token')
  }
})
    .then((response) => {
      // alert(response)
      events.value = response.data.data
      // MessagePlugin.warning(JSON.stringify(events));

      // alert(JSON.stringify(events))

    })
    .catch((error) => {
      if (error.response) {
        // 请求已发出，但服务器响应的状态码不在 2xx 范围内
        MessagePlugin.warning(error.response.data.msg);
      } else {
        // 一些错误是在设置请求的时候触发
        MessagePlugin.warning(error.message);
      }
    });


// const events = [
//   {
//     title: "hhh",
//     content: "haode",
//     cover: "https://tdesign.gtimg.com/site/source/card-demo.png",
//
//   },
//
//   {
//     title: "hhh222",
//     content: "haode2345",
//     cover: "https://th.bing.com/th/id/R.c927115ffa24dacf9616d608ad7fdc2b?rik=aBFhSrgygnmG1g&pid=ImgRaw&r=0",
//   }
// ];


// const {colors} = useColors();
// colors.primary = sessionStorage.getItem('primary-color')
// alert(colors.primary)


</script>
<style scoped>
#event {
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  gap: 20px;
  margin: 20px;

}
</style>