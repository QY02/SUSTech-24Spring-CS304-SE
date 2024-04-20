<template>

  <div id="event">
    <t-card
        v-for="(item,index) in events"
        :key="index"
        :title="item.title" :subtitle="item.content" :cover="item.cover" :style="{ width: '400px' }" hover-shadow>
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
import {getCurrentInstance, inject} from "vue";

const title = '标题';

const subtitle = '副标题';

const cover = 'https://tdesign.gtimg.com/site/source/card-demo.png';

const events = [
  {
    title: "hhh",
    content: "haode",
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
  }
];

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


// const {colors} = useColors();
// colors.primary = sessionStorage.getItem('primary-color')
// alert(colors.primary)
const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const apiBaseUrl = globalProperties.$apiBaseUrl;
// alert(apiBaseUrl)
axios.defaults.baseURL = apiBaseUrl;

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