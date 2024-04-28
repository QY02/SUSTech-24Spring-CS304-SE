<template>
<h2 style="margin-left: 20px">历史记录</h2>
  <div id="event">
    <t-card
        v-for="(item,index) in curEvents"
        :key="index"
        :title="item['name']" :subtitle="item['content']"  :style="{ width: '400px' }" hover-shadow
        @click="clickEvent(item['eventId'])">
      <template #actions>
<!--        <t-dropdown :options="options" :min-column-width="112" @click="clickHandler">-->
<!--          <div class="tdesign-demo-dropdown-trigger">-->

            <p v-if="item['type']===0">
              <t-tag theme="success" variant="light" style="margin-right: 20px">表演</t-tag>
            </p>
            <p v-if="item['type']===1">
              <t-tag theme="primary" variant="light" style="margin-right: 20px">讲座</t-tag>
            </p>
            <p v-if="item['type']===2">
              <t-tag theme="danger" variant="light" style="margin-right: 20px">比赛</t-tag>
            </p>
            <p v-if="item['type']===3">
              <t-tag variant="light" style="margin-right: 20px">其他</t-tag>
            </p>
            {{item['visitTime'].replace("T"," ")}}
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

</template>

<script setup>

import {ThumbUpIcon, ChatIcon, ShareIcon, MoreIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import axios from "axios";
import {getCurrentInstance, ref} from "vue";
import router from "@/routers/index.js";
// 获取全局变量 $apiBaseUrl
const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const apiBaseUrl = globalProperties.$apiBaseUrl;

// alert(apiBaseUrl)
axios.defaults.baseURL = apiBaseUrl;
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

axios.post(`/history/getByUserId`, {
  "userId": sessionStorage.getItem('uid')
}, {
  params: {},
  headers: {
    token: sessionStorage.getItem('token')
  }
})
    .then((response) => {
      // alert(response)
      events.value = response.data.data
      curEvents.value = response.data.data
      // tmpEvents.value = response.data.data
      // alert(JSON.stringify(curEvents.value))

    })
    .catch((error) => {
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