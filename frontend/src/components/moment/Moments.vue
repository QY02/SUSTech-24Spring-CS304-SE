<template>
  <t-layout>
    <t-aside>
        <t-space :break-line="true" class="card-with-margin" align="center"  :style="{height: 'calc(100vh - 96px)', 'overflow-y': 'scroll' }">
          <t-image
              v-for="item in list"
              :key="item"
              src="https://tdesign.gtimg.com/demo/demo-image-1.png"
              :style="{ width: '120px', height: '120px' }"
              fit="cover"
              shape="round"
              :lazy="true"
              class="image-shadow image-with-margin"
              @click="selectedItem = item"
          >
            <template #overlayContent>
              <Tag
                  shape="mark"
                  theme="primary"
                  variant="light"
                  :style="{ position: 'absolute', right: '8px', bottom: '8px', borderRadius: '4px' }"
              >
                @雷军
              </Tag>
            </template>
          </t-image>
        </t-space>
    </t-aside>

    <t-content>
      <t-radio-group class="card-with-margin" variant="default-filled" default-value="1" @change="onTypeChange">
        <t-radio-button value="1">动态</t-radio-button>
        <t-radio-button value="2">我的发布</t-radio-button>
      </t-radio-group>
      <t-card class="card-with-margin" hoverShadow >
        <t-space>
          <t-button variant="outline" theme="success">点击跳转相关活动：南方科技大学10次升旗仪式</t-button>
          <t-button v-if="radioGroupValue === '2'" @click="editPost">
            <template #icon><edit-icon /></template>
              编辑
          </t-button>
          <t-button v-if="radioGroupValue === '2'" @click="deletePost" theme="danger">
            <template #icon><delete-icon /></template>
              删除
          </t-button>
        </t-space>
        <div class="spacing"></div>
        <t-comment :avatar="momentData.avatar" :author="momentData.author" :datetime="momentData.datetime" :content="momentData.content">
          <template #actions>
            <t-space key="thumbUp" :size="10">
              <t-icon name="thumb-up"/>
              <span>6</span>
            </t-space>
            <t-space key="thumbDown" :size="10">
              <t-icon name="thumb-down"/>
              <span>1</span>
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
            direction="vertical"
            :navigation="{ showSlideBtn: 'never' }"
            :height="480"
            :autoplay="false"
        >
          <t-swiper-item v-for="item in 6" :key="item">
            <t-image
                src="https://tdesign.gtimg.com/demo/demo-image-1.png"
                fit="cover"
                shape="round"
            />
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
    <t-list :split="true">
      <t-list-item v-for="(item, index) in commentsData" :key="index">
        <template #content>
          <t-comment style="margin-bottom: 5px" :avatar="item.avatar" :author="item.author" :datetime="item.datetime" :content="item.content">
          </t-comment>
        </template>
      </t-list-item>
    </t-list>
    <div style="background-color: #ffffff ;position: fixed;bottom:0;width: 40vw ;height: auto" >
      <t-divider/>
      <t-comment avatar="https://tdesign.gtimg.com/site/avatar.jpg">
        <template #content>
          <div class="form-container">
            <t-textarea v-model="replyData" placeholder="请输入内容" />
            <t-button class="form-submit" @click="submitReply">回复</t-button>
          </div>
        </template>
      </t-comment>
    </div>
  </t-drawer>
</template>

<script setup lang="jsx">
import { Tag } from 'tdesign-vue-next';
import {AddIcon, DeleteIcon, EditIcon} from "tdesign-icons-vue-next";
import { ref } from 'vue';
import router from "@/routers/index.js";

const momentData = {
    id: 'A',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名A',
    datetime: '今天16:38',
    content: '评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。',
  };

const list = ref(Array.from({ length: 24 }).map((_, index) => index));

// 当前选中的动态
let selectedItem = ref(null);

let radioGroupValue = ref('1');// 1: 动态 2: 我的发布
const onTypeChange = (checkedValues) => {
  radioGroupValue.value = checkedValues;
};

const editPost = () => {
  console.log('Edit post');
};

const deletePost = () => {
  console.log('Delete post');
};


// ###### 评论区 开始 ######
// 评论区是否可见
const commentVisible = ref(false);

const viewComment = () => {
  commentVisible.value = true;
};

const commentsData = [
  {
    id: 'A',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名A',
    datetime: '今天16:38',
    content: '评论作者名A写的评论内容。',
  },
  {
    id: 'B',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名B',
    datetime: '今天16:38',
    content: '评论作者名B写的评论内容。',
  },
  {
    id: 'C',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名C',
    datetime: '今天16:38',
    content: '评论作者名C写的评论内容。',
  },
];


const replyData = ref('');
const submitReply = () => {
  console.log('回复内容：', replyData.value);
  replyData.value = '';
};

// ###### 评论区 结束 ######

</script>


<style scoped>

.card-with-margin {
  margin:  20px;
  height: max-content;
  display: flex;
}

.image-with-margin {
  margin:  5px 10px;
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

</style>
