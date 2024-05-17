<template>
<!--  <h2 style="margin-left: 20px">我的发布</h2>-->
<!--  <t-tag style="margin-left: 20px;height: 40px; margin-top: 15px;font-size: 20px" size="large" theme="default" variant="light">我的发布</t-tag>-->

  <div id="event">
    <t-card
        v-for="(item,index) in curEvents"
        :key="index"
        :title="item['name']" :subtitle="item['content']" :cover=cover :style="{ width: '400px' }" hover-shadow
        @click="clickEvent(item['id'])">
      <template #actions>
        <!--        <t-dropdown :options="options" :min-column-width="112" @click="clickHandler">-->
        <!--          <div class="tdesign-demo-dropdown-trigger">-->
        <a v-if="item['type']===0">
          <t-tag theme="success" variant="light" style="margin-right: 20px">表演</t-tag>
        </a>
        <a v-if="item['type']===1">
          <t-tag theme="primary" variant="light" style="margin-right: 20px">讲座</t-tag>
        </a>
        <a v-if="item['type']===2">
          <t-tag theme="danger" variant="light" style="margin-right: 20px">比赛</t-tag>
        </a>
        <a v-if="item['type']===3">
          <t-tag variant="light" style="margin-right: 20px">其他</t-tag>
        </a>
        <!--            <t-button variant="text" shape="square">-->
        <!--              <more-icon/>-->
        <!--            </t-button>-->
        <!--          </div>-->
        <!--        </t-dropdown>-->
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
  <t-popup content="返回上一页">
    <t-button shape="circle" theme="primary" size="large" style="position: fixed;right: 30px;bottom: 40px"
              @click="router.push('/user');">
      <template #icon>
        <rollback-icon/>
      </template>

    </t-button>
  </t-popup>
</template>

<script setup>

import {ThumbUpIcon, ChatIcon, ShareIcon, MoreIcon, RollbackIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import axios from "axios";
import {computed, defineComponent, getCurrentInstance, inject, ref, watch} from "vue";
import router from "@/routers/index.js";

const cover = 'https://tdesign.gtimg.com/site/source/card-demo.png';
const events = ref([]);
const tmpEvents = ref([]);
const curEvents = ref([]);
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
  MessagePlugin.success(`选中【${data.content}】 `);
};
const clickEvent = (eventId) => {
  MessagePlugin.success(`${sessionStorage.getItem('uid')} 选中【${eventId}】`);

  // router.push('/event');
  axios.post(`/history/add`, {
    "eventId": eventId,
    "userId": sessionStorage.getItem('uid')
  }, {
    params: {},
    headers: {
      token: sessionStorage.getItem('token')
    }
  })
      .then((response) => {
        sessionStorage.setItem('eventId',eventId)
        router.push('/event');
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
};
// const eventType = inject('eventType')
const eventType = ref(sessionStorage.getItem('eventType'))

const typeValue = ref([]);  // Initialize with a default value

// Function to handle incoming messages from other tabs
window.addEventListener('setItem', () => {
  typeValue.value = sessionStorage.getItem('eventType');
  curEvents.value = events.value.filter(events => typeValue.value.includes(events['type'] + 1));
  tmpEvents.value = events.value.filter(events => typeValue.value.includes(events['type'] + 1));
  // alert(JSON.stringify(tmpEvents.value))
  // events.value=[]
  // alert(typeValue.value)
})

function getSearchNew(message) {
  // alert(JSON.stringify(tmpEvents.value))

  // curEvents.value =event.content.includes(searchText.value) || event.title.includes(searchText.value)
  // alert(message)
  curEvents.value = tmpEvents.value.filter(events => events['content'].includes(message) || events['name'].includes(message));
  // curEvents.value = tmpEvents.value.filter(tmpEvents => tmpEvents['content'].includes(message) || tmpEvents['eventPolicy'].includes(message));
}

defineExpose({getSearchNew});
// 获取全局变量 $apiBaseUrl
const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const apiBaseUrl = globalProperties.$apiBaseUrl;
// alert(apiBaseUrl)
axios.defaults.baseURL = apiBaseUrl;
const publisherId=sessionStorage.getItem('uid')
axios.get(`/event/getMyPost/${publisherId}`, {
  params: {},
  headers: {
    token: sessionStorage.getItem('token')
  }
})
    .then((response) => {
      // alert(response)
      events.value = response.data.data
      curEvents.value = events.value
      tmpEvents.value = events.value
      // alert(JSON.stringify(events.value))
      for (let i = 0; i < events.value.length; i++) {//获取每个活动的海报
        let id=events.value[i]['id'];
        // alert(id)
      }
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