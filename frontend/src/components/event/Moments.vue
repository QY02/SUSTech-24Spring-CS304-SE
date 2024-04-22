<template>
  <t-layout>
    <t-aside>
      <t-space direction="vertical" class="card-with-margin">
        <t-space :break-line="true" align="center"  :style="{height: '100vh', 'overflow-y': 'scroll' }">
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
                  :style="{ position: 'absolute', right: '8px', bottom: '8px', borderRadius: '3px' }"
              >
                @雷军
              </Tag>
            </template>
          </t-image>
        </t-space>
      </t-space>
    </t-aside>

    <t-content>
      <t-radio-group class="card-with-margin" variant="default-filled" default-value="1">
        <t-radio-button value="1">动态</t-radio-button>
        <t-radio-button value="2">我的发布</t-radio-button>
      </t-radio-group>
      <t-card class="card-with-margin" hoverShadow >
        <div class="spacing"></div>
        <t-comment :avatar="commentsData[0].avatar" :author="commentsData[0].author" :datetime="commentsData[0].datetime" :content="commentsData[0].content">
          <template #actions>
            <t-space key="thumbUp" :size="6">
              <t-icon name="thumb-up"/>
              <span>6</span>
            </t-space>
            <t-space key="chat" :size="6">
              <t-icon name="chat"/>
              <span>回复</span>
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
              @click="router.push('/applyEvent');">
      <template #icon>
        <add-icon/>
      </template>

    </t-button>
  </t-popup>
</template>

<script setup lang="jsx">
import { Tag } from 'tdesign-vue-next';
import { AddIcon} from "tdesign-icons-vue-next";
import { ref } from 'vue';
import router from "@/routers/index.js";

const commentsData = [
  {
    id: 'A',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名A',
    datetime: '今天16:38',
    content: '评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。',
  }
];

const list = ref(Array.from({ length: 24 }).map((_, index) => index));

// 当前选中的动态
let selectedItem = ref(null);



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
  box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.5);
  transition: box-shadow 0.3s ease;
}

.image-shadow:hover {
  box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.8);
}

</style>
