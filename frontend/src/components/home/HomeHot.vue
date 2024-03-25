<template>
  <div id="event">
    <t-list :split="true">
      <t-list-item v-for="(item,index) in events" :key="item" @click="getEvent(item)">
        <div style="display: flex;">
          <div v-if="index+1<=3">
            <t-tag theme="danger" variant="light" style="margin-right: 20px">{{ index + 1 }}
              <template #icon>
                <t-icon name="rocket"/>
              </template>
            </t-tag>
          </div>
          <div v-else>
            <t-tag theme="primary" variant="light" style="margin-right: 40px">{{ index + 1 }}</t-tag>
          </div>
          <t-image
              :src="item.cover"
              fit="cover"
              :style="{ width: '250px', height: '120px'}"
              position="left"
              style="margin-right: 50px"
          />
<!--          <t-list-item-meta :title="item.title" :description="item.content" style="display: flex; align-items: center;"/>-->
          <t-list-item-meta class="t-list-item-meta-description" :title="item.title" :description="item.content" style="display: flex; align-items: center;">
<!--            <p class="t-list-item-meta-description">{{ item.content }}</p>-->
          </t-list-item-meta>
        </div>
          <template #action>
            <t-button variant="text" shape="square">
              <arrow-right-icon/>
            </t-button>
          </template>



      </t-list-item>

    </t-list>
  </div>
</template>

<script setup>
import {ArrowRightIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from "tdesign-vue-next";
import rocket from "tdesign-icons-vue-next/lib/components/rocket.js";

const avatarUrl = 'https://tdesign.gtimg.com/site/avatar.jpg';
const events = [
  {
    title: "hhh",
    content: "haodeuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu",
    cover: "https://tdesign.gtimg.com/site/source/card-demo.png",

  },
  {
    title: "hhh222",
    content: "haode2345",
    cover: "https://th.bing.com/th/id/R.c927115ffa24dacf9616d608ad7fdc2b?rik=aBFhSrgygnmG1g&pid=ImgRaw&r=0",
  },
  {
    title: "hhh",
    content: "haode",
    cover: "https://tdesign.gtimg.com/site/source/card-demo.png",

  },
  {
    title: "hhh222",
    content: "haode2345",
    cover: "https://th.bing.com/th/id/R.c927115ffa24dacf9616d608ad7fdc2b?rik=aBFhSrgygnmG1g&pid=ImgRaw&r=0",
  },];

const getEvent = (item) => {
  MessagePlugin.success(`选中【${item.title}】`);
};
</script>

<style scoped>
#event {
  margin: 10px;
}
.t-list-item-meta-description {
  max-width: 700px; /* 设置description的最大宽度 */
  overflow:auto; /* 超出部分隐藏 */
  text-overflow: ellipsis; /* 文本溢出显示省略号 */
  word-wrap: initial; /* 强制换行 */
}
</style>