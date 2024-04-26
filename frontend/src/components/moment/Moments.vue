<template>
  <t-layout>
    <t-aside>
      <t-space v-loading="asideLoading" :break-line="true" class="card-with-margin scroll-container" align="center"
               :style="{height: 'calc(100vh - 96px)', 'overflow-y': 'scroll' }">
        <t-image
            v-for="item in list"
            :key="item.id"
            :src="item.img"
            :style="{ width: '120px', height: '120px' }"
            fit="cover"
            shape="round"
            :lazy="true"
            class="image-shadow image-with-margin"
            @click="selectMoment(item)"
        >
          <template #overlayContent>
            <Tag
                shape="mark"
                theme="primary"
                variant="light"
                :style="{ position: 'absolute', right: '8px', bottom: '8px', borderRadius: '4px' }"
            >
              @{{ item.name }}
            </Tag>
          </template>
        </t-image>
        <t-button shape="round" variant="outline" :disabled="noMoreImage" class="image-with-margin" @click="loadMore">
          点击加载更多
        </t-button>
      </t-space>
    </t-aside>

    <t-content>
      <t-radio-group class="card-with-margin" variant="default-filled" default-value="1" @change="onTypeChange">
        <t-radio-button value="1">动态</t-radio-button>
        <t-radio-button value="2">我的发布</t-radio-button>
      </t-radio-group>
      <t-card class="card-with-margin" hoverShadow v-loading="contentLoading" >
        <t-space>
          <t-button variant="outline" theme="success" @click="showEvent">点击跳转相关活动：{{momentData.relatedEvent}}</t-button>
          <t-button v-if="radioGroupValue === '2'" @click="editPost">
            <template #icon>
              <edit-icon/>
            </template>
            编辑
          </t-button>
          <t-button v-if="radioGroupValue === '2'" @click="deletePost" theme="danger">
            <template #icon>
              <delete-icon/>
            </template>
            删除
          </t-button>
        </t-space>
        <h1>
        {{momentData.title}}
        </h1>
        <t-comment :avatar="momentData.avatar" :author="momentData.userName" :datetime="momentData.publishDate"
                   :content="momentData.content">
          <template #actions>
            <t-space key="thumbUp" :size="10" @click="thumbUp">
              <t-icon name="thumb-up" :color="thumbUpColor"/>
              <span>{{momentData.upVote}}</span>
            </t-space>
            <t-space key="thumbDown" :size="10" @click="thumbDown">
              <t-icon name="thumb-down" :color="thumbDownColor"/>
              <span>{{momentData.downVote}}</span>
            </t-space>
            <t-space key="chat" :size="10" @click="viewComment">
              <t-icon name="chat"/>
              <span>评论</span>
            </t-space>
          </template>
        </t-comment>
        <div class="spacing"></div>
        <t-swiper
            class="tdesign-demo-block--swiper"
            :autoplay="false"
        >
          <t-swiper-item v-for="item in photoList" :key="item" >
            <t-image-viewer :key="item" v-model:visible="photoPreviewVisible" :default-index="0" :images="photoUrlList">
              <template #trigger>
            <t-image @click="photoPreviewVisible = true"
                :src=item
                fit="cover"
                shape="round"
            />
              </template>
            </t-image-viewer>
          </t-swiper-item>
        </t-swiper>
      </t-card>
    </t-content>
  </t-layout>
  <t-popup content="发布动态">
    <t-button shape="circle" theme="primary" size="large" style="z-index:100;position: fixed;right: 30px;bottom: 40px"
              @click="router.push('/newMoment');">
      <template #icon>
        <add-icon/>
      </template>
    </t-button>
  </t-popup>
  <t-drawer v-model:visible="commentVisible" header="评论区" :confirm-btn="null" :cancel-btn="null" size="42vw">
    <t-list :split="true" v-loading="commentLoading">
      <t-list-item v-for="(item, index) in commentsData" :key="index">
        <template #content>
          <t-comment style="margin-bottom: 5px" :avatar="item.avatar" :author="item.author" :datetime="item.publishDate"
                     :content="item.content">
            <template #actions>
              <t-space v-if="item.publisherId===user" key="delete" :size="6" @click="deleteComment(item)">
                <t-icon name="delete" />
                <span>删除</span>
              </t-space>
            </template>
          </t-comment>
        </template>
      </t-list-item>
    </t-list>
    <div style="background-color: #ffffff ;position: fixed;bottom:0;width: 40vw ;height: auto">
      <t-divider/>
      <t-comment avatar="https://tdesign.gtimg.com/site/avatar.jpg">
        <template #content>
          <div class="form-container">
            <t-textarea v-model="replyData" placeholder="请输入内容"/>
            <t-button class="form-submit" @click="submitReply">回复</t-button>
          </div>
        </template>
      </t-comment>
    </div>
  </t-drawer>
  <t-dialog
      v-model:visible="deleteVisible"
      theme="danger"
      header="确认删除动态"
      :on-close="closeDelete"
      @confirm="onDeleteClickConfirm"
  />
</template>

<script setup lang="jsx">
import {Tag} from 'tdesign-vue-next';
import {AddIcon, DeleteIcon, EditIcon} from "tdesign-icons-vue-next";
import {onMounted, ref} from 'vue';
import router from "@/routers/index.js";
import axios from "axios";
import { fileServerAxios } from "@/main.js"
import {MessagePlugin} from "tdesign-vue-next";

const user = sessionStorage.getItem("uid") ? sessionStorage.getItem("uid") : '';//当前用户

// ###### 动态列表 开始 ######

const list = ref([]);// 左侧动态列表
const lastId = ref(-1);// 上一次请求的最后一个动态的id
const noMoreImage = ref(false);// 是否还有更多图片
const asideLoading = ref(false);
const contentLoading = ref(false);

const getMomentBatch = async (id) => {
  try {
    asideLoading.value = true;
    contentLoading.value = true;
    const response = await axios.get(`/comment/getMomentBatch/${id}/${radioGroupValue.value}`, {
      headers: {
        token: sessionStorage.getItem('token'),
      }
    });
    if (response.data.data.length < 20) {
      noMoreImage.value = true;
    }
    for (let i = 0; i < response.data.data.length; i++) {
      const fileServerResponse = await fileServerAxios.get(`/file/download`, {
        responseType: 'blob',
        headers: {
          token: response.data.data[i].attachment,
        }
      });
      const image = fileServerResponse.data;
        list.value.push({
          id: response.data.data[i].comment_id,
          img: image,
          name: response.data.data[i].publisher_id,
        });
    }
    lastId.value = response.data.data[response.data.data.length - 1].comment_id;
    asideLoading.value = false;
    contentLoading.value = false;
  } catch (error) {
  }
};

onMounted(async() => {
  await getMomentBatch(-1);
  await selectMoment(list.value[0]);
});

const loadMore = async () => {
  await getMomentBatch(lastId.value);
};

// ###### 动态列表 结束 ######

// ###### 动态详情 开始 ######

const momentData = ref({
  id: 'A',
  avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
  title: '示例标题',
  userName: '示例评论作者',
  publishDate: '示例时间',
  content: '示例评论内容。',
  relatedEvent: '示例活动名称',
  eventId: '示例活动ID',
  upVote: 0,
  downVote: 0,
  mediaType: 0,
  mediaUrl: [],
});

const photoList = ref([]);
const photoUrlList = ref([]);
const photoPreviewVisible = ref(false);

const showEvent = () => {
  sessionStorage.setItem('eventId',momentData.value.eventId);
  router.push('/event');
};

const selectMoment = async (item) => {
  try {
    contentLoading.value = true;
    photoList.value = [];
    photoUrlList.value = [];
    photoPreviewVisible.value = false;
    const response = await axios.get(`/comment/getMomentById?commentId=${item.id}`, {
      headers: {
        token: sessionStorage.getItem('token')
      }
    });
    momentData.value = response.data.data;
    const response2 = await axios.get(`/blog/get/${item.id}`, {
      headers: {
        token: sessionStorage.getItem('token')
      }
    });
    momentData.value.avatar = 'https://tdesign.gtimg.com/site/avatar.jpg';
    thumbUpColor.value = response2.data.data.voteType === 1 ? 'red' : 'grey';
    thumbDownColor.value = response2.data.data.voteType === -1 ? 'blue' : 'grey';
    for (let i = 0; i < momentData.value.mediaUrl.length; i++) {
      const fileServerResponse = await fileServerAxios.get(`/file/download`, {
        responseType: 'blob',
        headers: {
          token: momentData.value.mediaUrl[i],
        }
      });
      const image = fileServerResponse.data;
      photoList.value.push(image);
      photoUrlList.value.push(URL.createObjectURL(image));
    }
    contentLoading.value = false;
  } catch (error) {
  }
};

let radioGroupValue = ref('1');// 1: 动态 2: 我的发布
const onTypeChange = async (checkedValues) => {
  radioGroupValue.value = checkedValues;
  list.value = [];
  lastId.value = -1;
  noMoreImage.value = false;
  await getMomentBatch(-1);
  await selectMoment(list.value[0]);
};

const editPost = () => {
  console.log('Edit post');
};

const deleteVisible = ref(false);

const onDeleteClickConfirm = async () => {
  try {
    contentLoading.value = true;
    asideLoading.value = true;
    await axios.delete(`/blog/deleteMoment/${momentData.value.id}`, {
      headers: {
        token: sessionStorage.getItem('token'),
      }
    });
    await getMomentBatch(-1);
    await selectMoment(list.value[0]);
    contentLoading.value = false;
    asideLoading.value = false;
    await MessagePlugin.success('删除成功');
  } catch (error) {
  }
};

const deletePost = () => {
  deleteVisible.value = true;
};

const closeDelete = () => {
  deleteVisible.value = false;
};

// ###### 动态详情 结束 ######

// ###### 点赞 开始 ######

const thumbUpColor = ref('grey');
const thumbDownColor = ref('grey');

const thumbUp = async () => {
  try {
    contentLoading.value = true;
  if (thumbUpColor.value === 'grey') {
    thumbUpColor.value = 'red';
    await axios.get(`/blog/change/${momentData.value.id}/${user}/1`, {
      headers: {
        token: sessionStorage.getItem('token'),
      }
    });
    if (thumbDownColor.value === 'blue') {
      thumbDownColor.value = 'grey';
      momentData.value.downVote--;
    }
    momentData.value.upVote++;
  } else {
    thumbUpColor.value = 'grey';
    await axios.get(`/blog/change/${momentData.value.id}/${user}/0`, {
      headers: {
        token: sessionStorage.getItem('token'),
      }
    });
    momentData.value.upVote--;
  }
    contentLoading.value = false;
  } catch (error) {
  }
};

const thumbDown = async () => {
  try {
    contentLoading.value = true;
    if (thumbDownColor.value === 'grey') {
      thumbDownColor.value = 'blue';
      await axios.get(`/blog/change/${momentData.value.id}/${user}/-1`, {
        headers: {
          token: sessionStorage.getItem('token'),
        }
      });
      if (thumbUpColor.value === 'red') {
        thumbUpColor.value = 'grey';
        momentData.value.upVote--;
      }
      momentData.value.downVote++;
    } else {
      thumbDownColor.value = 'grey';
      await axios.get(`/blog/change/${momentData.value.id}/${user}/0`, {
        headers: {
          token: sessionStorage.getItem('token'),
        }
      });
      momentData.value.downVote--;
    }
    contentLoading.value = false;
  } catch (error) {
  }
};

// ###### 点赞 结束 ######

// ###### 评论区 开始 ######
// 评论区是否可见
const commentVisible = ref(false);
const commentLoading = ref(false);

const viewComment = async () => {
  try {
    const response = await axios.get(`/reply/getByComment/${momentData.value.id}`, {
      headers: {
        token: sessionStorage.getItem('token'),
      }
    });
    commentsData.value = response.data.data;
    for (let i = 0; i < commentsData.value.length; i++) {
      commentsData.value[i].avatar = 'https://tdesign.gtimg.com/site/avatar.jpg';
    }
    commentVisible.value = true;
  } catch (error) {
  }
};

const deleteComment = async (item) => {
  try {
    commentLoading.value = true;
    await axios.delete(`/reply/delete/${item.id}`, {
      headers: {
        token: sessionStorage.getItem('token'),
      }
    });
    await viewComment();
    commentLoading.value = false;
    MessagePlugin.success('删除成功');
  } catch (error) {
  }
};

const commentsData = ref([
  {
    id: 'A',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    publisherId: '评论作者ID',
    author: '评论作者名A',
    publishDate: '今天16:38',
    content: '评论作者名A写的评论内容。',
  },
]);


const replyData = ref('');
const submitReply = async () => {
  if (replyData.value === '') {
    MessagePlugin.error('请输入内容');
    return;
  }
  commentLoading.value = true;
  await axios.post(`/reply/add`, {
    commentId: momentData.value.id,
    publisherId: user,
    content: replyData.value,
    publishDate: new Date(),
    upVote: 0,
    downVote: 0,
  }, {
    headers: {
      token: sessionStorage.getItem('token'),
    }
  }).catch();
  replyData.value = '';
  await viewComment();
  commentLoading.value = false;
};

// ###### 评论区 结束 ######

</script>


<style scoped>

.card-with-margin {
  margin: 20px;
  height: max-content;
  display: flex;
}

.image-with-margin {
  margin: 5px 10px;
  height: max-content;
  display: flex;
}

.spacing {
  height: 30px; /* 调整这个值以改变间距大小 */
}

.image-shadow {
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.5);
  transition: box-shadow 0.3s ease;
}

.image-shadow:hover {
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.8);
}

.form-container {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-bottom: 20px;

  .form-submit {
    margin-top: 8px;
  }
}

h1 {
  font-size: 24px;
  font-weight: bold;
}

</style>
