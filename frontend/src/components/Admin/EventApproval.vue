<template>
  <t-card :bordered="false" shadow class="card-with-margin">
    <h1 class="title"> 审核 </h1>
    <t-divider/>
    <div class="spacing"></div>
    <t-space direction="vertical" class="centered">
      <t-list :split="true">
        <t-list-item v-for="n in 9" :key="n">
          <t-list-item-meta :image="avatarUrl" title="林俊杰世界巡回演唱会" description="林俊杰世界巡回演唱会深圳站会在5.4-5.5举办" />
          <t-space direction="vertical">
            <t-text>2024-05-04</t-text>
            <t-text>深圳市大运中心体育场</t-text>
          </t-space>
          <t-space direction="horizontal">
            <t-tag theme="primary" variant="light">演唱会</t-tag>
            <t-tag theme="warning" variant="light">￥380起</t-tag>
          </t-space>
          <template #action>
            <t-button variant="text" shape="square">
              <icon name="task-1" />
            </t-button>
            <t-button variant="text" shape="square">
              <icon name="check" color="green" />
            </t-button>
            <t-button variant="text" shape="square">
              <icon name="close" color="red" />
            </t-button>
          </template>
        </t-list-item>
      </t-list>
    </t-space>
    <div class="spacing"></div>
    <t-pagination
        :total="36"
        :default-current="2"
        :default-page-size="10"
        show-first-and-last-page-btn
        @current-change="onCurrentChange"
        @page-size-change="onPageSizeChange"
        @change="onChange"
    />
    <div class="spacing"></div>
  </t-card>
</template>

<script setup>
import { ref } from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import { Icon } from 'tdesign-icons-vue-next';


// ###### 列表 开始 ######
const avatarUrl = 'https://tdesign.gtimg.com/site/avatar.jpg';
// ###### 列表 结束 ######

// ###### 分页 开始 ######
const current = ref(1);
const pageSize = ref(5);

const onPageSizeChange = (size) => {
  console.log('page-size:', size);
  MessagePlugin.success(`pageSize变化为${size}`);
};

const onCurrentChange = (index, pageInfo) => {
  MessagePlugin.success(`转到第${index}页`);
  console.log(pageInfo);
};

const onChange = (pageInfo) => {
  console.log(pageInfo);
};
// ###### 分页 结束 ######


</script>

<style scoped>

.card-with-margin {
  margin: 20px; /* 调整这个值以改变外边距大小 */
}

.centered {
  width: 98%;
  //display: flex;
  //justify-content: center;
  align-items: center;
  //height: vh; /* Adjust this value as needed */
}

.spacing {
  height: 30px; /* 调整这个值以改变间距大小 */
}

.title {
  margin: 50px 40px;
  font-size: 50px;
}
</style>
