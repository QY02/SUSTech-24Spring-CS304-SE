<template>
  <t-layout>
    <t-aside>
      <t-space direction="vertical" class="card-with-margin">
        <t-space :break-line="true" align="center"  :style="{ height: '750px', 'overflow-y': 'scroll' }">
          <t-image
              v-for="item in list"
              :key="item"
              src="https://tdesign.gtimg.com/demo/demo-image-1.png"
              :style="{ width: '200px', height: '200px' }"
              fit="cover"
              shape="round"
              :lazy="true"
          />
        </t-space>
      </t-space>
    </t-aside>

    <t-content>
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
</template>

<script setup lang="jsx">
const commentsData = [
  {
    id: 'A',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名A',
    datetime: '今天16:38',
    content: '评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。评论作者名A写的评论内容。',
  }
];

import { ref } from 'vue';
const list = ref(Array.from({ length: 24 }).map((_, index) => index));

import { PrintIcon } from 'tdesign-icons-vue-next';
import { Tag } from 'tdesign-vue-next';

const renderButton = () => (
    <Tag
        shape="mark"
        theme="primary"
        variant="light"
        style={{
          position: 'absolute',
          right: '8px',
          bottom: '8px',
          borderRadius: '3px',
        }}
    >
      <PrintIcon size="16" /> 高清
    </Tag>
);
</script>


<style scoped>

.card-with-margin {
  margin:  15px 15px 15px 15px;
  height: max-content;
  display: flex;
}


.spacing {
  height: 30px; /* 调整这个值以改变间距大小 */
}

</style>
