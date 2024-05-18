<template>
<!--  <h2 style="margin-left: 20px">我的预定</h2>-->
<!--  <t-tag style="margin-left: 20px;height: 40px; margin-top: 15px;font-size: 20px" size="large" theme="success" variant="light">我的预定</t-tag>-->

  <div id="MyOrderEvent">

    <!--  <t-list :split="true" stripe>-->
    <!--    <t-list-item>-->
    <!--      <t-list-item-meta :image="imageUrl" title="列表标题" description="列表内容的描述性文字" />-->
    <!--    </t-list-item>-->
    <!--    <t-list-item>-->
    <!--      <t-list-item-meta :image="imageUrl" title="列表标题" description="列表内容的描述性文字" />-->
    <!--    </t-list-item>-->
    <!--    <t-list-item>-->
    <!--      <t-list-item-meta :image="imageUrl" title="列表标题" description="列表内容的描述性文字" />-->
    <!--    </t-list-item>-->
    <!--  </t-list>-->

    <t-list :split="true" stripe>
      <t-list-item v-for="(item,index) in events" :key="item" @click="getEvent(item)">
        <div style="display: flex;">

          <!--          <t-list-item-meta :title="item.title" :description="item.content" style="display: flex; align-items: center;"/>-->
          <t-list-item-meta class="t-list-item-meta-description"  :title="item.name" :description="records[index].seatId"
                            style="display: flex; align-items: center;">
            <!--            <p class="t-list-item-meta-description">{{ item.content }}</p>-->
          </t-list-item-meta>
          <t-tag theme="primary" variant="light"  style="display: flex; margin-left: 30px;">
            {{ records[index].submitTime.replace('T', ' ') }}
          </t-tag>
          <t-tag theme="success" variant="light"  style="display: flex; margin-left: 30px;">
            活动开始时间： {{ records[index].eventSession.startTime.replace('T', ' ') }}
          </t-tag>
          <t-tag theme="warning" variant="light"  style="display: flex; margin-left: 30px;">
            活动地点： {{ records[index].eventSession.venue.replace('T', ' ') }}
          </t-tag>

        </div>
        <template #action>
          <t-button variant="text" shape="square">
            <arrow-right-icon/>
          </t-button>
        </template>


      </t-list-item>

    </t-list>
  </div>
<!--  <t-popup content="返回上一页">-->
<!--    <t-button shape="circle" theme="primary" size="large" style="position: fixed;right: 30px;bottom: 40px"-->
<!--              @click="router.push('/user');">-->
<!--      <template #icon>-->
<!--        <rollback-icon/>-->
<!--      </template>-->

<!--    </t-button>-->
<!--  </t-popup>-->
</template>

<script setup>

import {ThumbUpIcon, ChatIcon, ShareIcon, MoreIcon, ArrowRightIcon, RollbackIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import axios from "axios";
import {computed, defineComponent, getCurrentInstance, inject, ref, watch} from "vue";
import router from "@/routers/index.js";
import heart from "tdesign-icons-vue-next/lib/components/heart.js";

const cover = 'https://tdesign.gtimg.com/site/source/card-demo.png';
const events = ref([]);
const tmpEvents = ref([]);
const curEvents = ref([]);
const records = ref([]);
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

// 获取全局变量 $apiBaseUrl
const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const apiBaseUrl = globalProperties.$apiBaseUrl;
// alert(apiBaseUrl)
axios.defaults.baseURL = apiBaseUrl;
const publisherId = sessionStorage.getItem('uid')
axios.post(`/orderRecord/getMyOrderRecord`, {
  "mode": 3,
}, {
  params: {},

  headers: {
    token: sessionStorage.getItem('token')
  }
})
    .then((response) => {
      // alert(response)
      events.value = response.data.data.map(item => item.event);
      records.value = response.data.data;

      // alert(JSON.stringify(response.data.data))
      for (let i = 0; i < events.value.length; i++) {//获取每个活动的海报
        let id = events.value[i]['id'];
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
        sessionStorage.setItem('eventId', eventId)
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
// window.addEventListener('setItem', () => {
//   typeValue.value = sessionStorage.getItem('eventType');
//   curEvents.value = events.value.filter(events => typeValue.value.includes(events['type'] + 1));
//   tmpEvents.value = events.value.filter(events => typeValue.value.includes(events['type'] + 1));
//   // alert(JSON.stringify(tmpEvents.value))
//   // events.value=[]
//   // alert(typeValue.value)
// })

// function getSearchNew(message) {
//   // alert(JSON.stringify(tmpEvents.value))
//
//   // curEvents.value =event.content.includes(searchText.value) || event.title.includes(searchText.value)
//   // alert(message)
//   curEvents.value = tmpEvents.value.filter(events => events['content'].includes(message) || events['name'].includes(message));
//   // curEvents.value = tmpEvents.value.filter(tmpEvents => tmpEvents['content'].includes(message) || tmpEvents['eventPolicy'].includes(message));
// }

// defineExpose({getSearchNew});


// const {colors} = useColors();
// colors.primary = sessionStorage.getItem('primary-color')
// alert(colors.primary)


</script>
<style scoped>
#MyOrderEvent {

  gap: 20px;
  margin: 20px;

}

.t-list-item-meta-description {
  max-width: 700px; /* 设置description的最大宽度 */
  overflow: auto; /* 超出部分隐藏 */
  text-overflow: ellipsis; /* 文本溢出显示省略号 */
  word-wrap: initial; /* 强制换行 */
}
</style>