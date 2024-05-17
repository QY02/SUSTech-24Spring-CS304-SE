<template>

  <div id="event">
    <t-card
        v-for="(item,index) in curEvents"
        :key="index"
        :title="item['name']" :subtitle="item['content']" :cover="item['cover']" :style="{ width: '400px' }"
        hover-shadow
        @click="clickEvent(item['id'])">
      <!--      <template #cover>-->
      <!--        &lt;!&ndash; Image with native lazy loading &ndash;&gt;-->
      <!--        <img :src="item.imageUrl" loading="lazy" alt="Event Image">-->
      <!--      </template>-->
      <template #actions>

        <!--        <t-dropdown :options="options" :min-column-width="112" @click="clickHandler">-->
        <!--          <div class="tdesign-demo-dropdown-trigger">-->
        <a v-if="item['type']>=0&&item['type']<=2">
          <t-tag theme="success" variant="light" style="margin-right: 20px">{{ EVENT_TYPE_MAP[item['type']] }}</t-tag>
        </a>
        <a v-if="item['type']>=3&&item['type']<=5">
          <t-tag theme="primary" variant="light" style="margin-right: 20px">{{ EVENT_TYPE_MAP[item['type']] }}</t-tag>
        </a>
        <a v-if="item['type']>=6&&item['type']<=8">
          <t-tag theme="danger" variant="light" style="margin-right: 20px">{{ EVENT_TYPE_MAP[item['type']] }}</t-tag>
        </a>
        <a v-if="item['type']>=9&&item['type']<=12">
          <t-tag variant="light" style="margin-right: 20px">{{ EVENT_TYPE_MAP[item['type']] }}</t-tag>
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
            <t-button variant="text" shape="square" @click.stop="favEvent(item['id'])">

              <t-icon name="heart" :color="favColor[item['id']]"/>
            </t-button>
          </t-col>

          <t-col flex="auto" style="display: inline-flex; justify-content: center">
            <t-button variant="text" shape="square" @click.stop="clickComment(item['id'])">
              <chat-icon/>
            </t-button>
          </t-col>

          <t-col flex="auto" style="display: inline-flex; justify-content: center">
            <t-button variant="text" shape="square" @click.stop="clickShare(item['id'],item['name'])">
              <share-icon/>
            </t-button>
          </t-col>
        </t-row>
      </template>
    </t-card>


  </div>
  <t-dialog
      v-model:visible="visible"
      header="评论"
      body="自定义底部按钮，直接传入文字"
      :confirm-btn="null"
      :cancel-btn="null"
  >
    <a v-if="visible===true">
      <CommentPage></CommentPage>
    </a>

  </t-dialog>
</template>

<script setup>

import {ChatIcon, HeartIcon, ShareIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import axios from "axios";
import {getCurrentInstance, ref} from "vue";
import router from "@/routers/index.js";
import {EVENT_TYPE_MAP} from "../../constants/index.js";
import CommentPage from "@/components/event/CommentPage.vue";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const apiBaseUrl = globalProperties.$apiBaseUrl;
// const fileUrl = fileServerAxios;
const fileUrl = 'http://localhost:8084';
const visible = ref(false);

// alert(apiBaseUrl)
axios.defaults.baseURL = apiBaseUrl;

const cover = ref('https://tdesign.gtimg.com/site/source/card-demo.png');
const events = ref([]);
const attachToken = ref([]);
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
const favColor = ref({});

const isFavorite = ref(false);

const getFavorite = (eventId) => {
  axios.post(`/favorite/isFavorite`, {
    "eventId": eventId,
    "userId": sessionStorage.getItem('uid')
  }, {
    headers: {
      token: sessionStorage.getItem('token')
    }
  }).then((response) => {
    isFavorite.value = response.data.data
  }).catch(() => {
  })
}


const deleteFavorite = (eventId) => {
  axios.post(`/favorite/delete`, {
    "eventId": eventId,
    "userId": sessionStorage.getItem('uid')
  }, {
    headers: {
      token: sessionStorage.getItem('token')
    }
  }).then((response) => {
  }).catch(() => {
  })
}

axios.post(`/event/getAllEvents`, {}, {
  params: {},
  headers: {
    token: sessionStorage.getItem('token')
  }
})
    .then((response) => {
      // alert(response)
      events.value = response.data.data.filter(events => events['status'] === 1)
      curEvents.value = events.value
      tmpEvents.value = events.value
      // alert(JSON.stringify(events.value))
      for (let i = 0; i < 1; i++) {//获取每个活动的海报
        let id = events.value[i]['id'];
        axios.post(`/favorite/isFavorite`, {
          "eventId": id,
          "userId": sessionStorage.getItem('uid')
        }, {
          headers: {
            token: sessionStorage.getItem('token')
          }
        }).then((response) => {
          // alert(JSON.stringify(response.data.data))
          if (response.data.data === 1) {
            favColor.value[id] = 'red'
          } else {
            favColor.value[id] = 'grey'
          }
        }).catch(() => {
        })
        // favColor.value[i] = isFavorite.value;

        // alert(id)

        axios.post(`/postAttachmentRelation/getAttachment`, {
          "entity_type": 1,
          "entity_id": id,
          "attachment_type": 0,
        }, {
          params: {},
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
            .then((response) => {
              attachToken.value = response.data.data['filePath']
              // alert(JSON.stringify(response.data.data))
              let attachToken1 = attachToken.value
              // alert(attachToken1)
              // 47.107.113.54:25572 文件服务器地址
              axios.get(`${fileUrl}/file/download`, {
                params: {},
                headers: {
                  token: attachToken1
                },
                responseType: 'blob'
              })
                  .then((response) => {//
                    // alert(JSON.stringify(response.data))
                    // 将图片 URL 赋值给 cover 变量
                    const blob = new Blob([response.data], {type: 'application/octet-stream'});

                    // 创建一个 Blob 对象的 URL
                    const imageUrl = URL.createObjectURL(blob);
                    events.value[i]['cover'] = imageUrl

                    // 将图片 URL 赋值给 cover 变量
                    // cover.value = imageUrl;

                  })
                  .catch((error) => {
                    if (error.response) {
                      // 请求已发出，但服务器响应的状态码不在 2xx 范围内
                      // MessagePlugin.warning(error.response.data.msg);
                    } else {
                      // 一些错误是在设置请求的时候触发
                      // MessagePlugin.warning(error.message);
                    }
                  });
            })
            .catch((error) => {
              if (error.response) {
                // 请求已发出，但服务器响应的状态码不在 2xx 范围内
                // MessagePlugin.warning(error.response.data.msg);
              } else {
                // 一些错误是在设置请求的时候触发
                // MessagePlugin.warning(error.message);
              }
            });
        // events.value[i].imageUrl =
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


// const eventType = inject('eventType')
const eventType = ref(sessionStorage.getItem('eventType'))
const typeValue = []  // Initialize with a default value

// Function to handle incoming messages from other tabs
window.addEventListener('setItem', () => {
  typeValue.value = sessionStorage.getItem('eventType').split(",").map(Number);//传进来的是个字符串！！！
  // console.log(typeof typeValue.value)
  // console.log(typeValue.value)
  // console.log(typeValue.value.indexOf(1) !== -1)
  // console.log(JSON.stringify(curEvents.value))
  curEvents.value = events.value.filter(events => typeValue.value.indexOf(events['type'] + 1) !== -1);
  // console.log(JSON.stringify(curEvents.value))

  tmpEvents.value = events.value.filter(events => typeValue.value.includes(events['type'] + 1));
})

const clickComment = (eventId) => {
  sessionStorage.setItem('eventId', eventId)
  visible.value = true
}
const clickShare = (eventId, eventName) => {
  sessionStorage.setItem('eventId', eventId)
  sessionStorage.setItem('MomentName', eventName)
  router.push('/newMoment');
}
// alert(sessionStorage.getItem('MomentName'))
const clickEvent = (eventId) => {
  MessagePlugin.success(`${sessionStorage.getItem('uid')} 选中【${eventId}】`);
  sessionStorage.setItem('eventId', eventId)
  router.push('/event');
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
const favEvent = (eventId) => {
  // MessagePlugin.success(`${sessionStorage.getItem('uid')} 喜欢【${eventId}】`);
  if (favColor.value[eventId] === 'grey') {//妹收藏过
    axios.post(`/favorite/add`, {
      "eventId": eventId,
      "userId": sessionStorage.getItem('uid'),
    }, {
      headers: {
        token: sessionStorage.getItem('token')
      }
    })
        .then(() => {
          favColor.value[eventId] = 'red'
          MessagePlugin.success("Add favorite successfully!");
        })
        .catch((error) => {
          // thumbUpColor.value = 'red'
          if (error.response) {

            // 请求已发出，但服务器响应的状态码不在 2xx 范围内
            // MessagePlugin.warning(error.response.data.msg);
          } else {
            // 一些错误是在设置请求的时候触发
            MessagePlugin.warning(error.message);
          }
        });
  } else {
    // alert('hhh')
    axios.post(`/favorite/delete`, {
      "eventId": eventId,
      "userId": sessionStorage.getItem('uid')
    }, {
      headers: {
        token: sessionStorage.getItem('token')
      }
    }).then((response) => {
      favColor.value[eventId] = 'grey'
      MessagePlugin.success("Delete favorite successfully!");

    }).catch(() => {
    })
  }
};


function getSearchNew(message) {
  // alert(JSON.stringify(tmpEvents.value))

  // curEvents.value =event.content.includes(searchText.value) || event.title.includes(searchText.value)
  // alert(message)
  curEvents.value = tmpEvents.value.filter(events => events['content'].includes(message) || events['name'].includes(message));
  // curEvents.value = tmpEvents.value.filter(tmpEvents => tmpEvents['content'].includes(message) || tmpEvents['eventPolicy'].includes(message));
}

defineExpose({getSearchNew});
// 获取全局变量 $apiBaseUrl


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