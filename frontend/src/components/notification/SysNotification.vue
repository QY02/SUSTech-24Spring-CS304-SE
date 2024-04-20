<template>
  <div>
    <!-- @cancel 和 :onCancel 两种写法都支持； :onOverlayClick 和 @overlay-click 两种写法都支持--->
    <t-drawer
        v-model:visible="visible"
        header="标题名称"
        :on-overlay-click="() => (visible = false)"
        placement="right"
        :size-draggable="true"
        :confirm-btn="null"
        cancel-btn="收起"
        @cancel="visible = false"
    >
        <t-space direction="vertical" style="width: 100%">
            <t-collapse v-model="activeNames" accordion expand-mutex=true>
              <!-- 使用 v-for 循环渲染折叠面板 -->
              <t-collapse-panel v-for="(item, index) in notifications" :key="index" :value="index.toString()">
                <!-- 根据数组中的类型值动态设置折叠面板的标题 -->
                <template #header>
                  {{ getHeaderByType(item.type) }}
                </template>
                <!-- 折叠面板展开或折叠的内容 -->
                {{ item.content }}
              </t-collapse-panel>
            </t-collapse>
        </t-space>
    </t-drawer>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {useVModel} from "@vueuse/core";

const props = defineProps({
  visible: Boolean
})
const emit = defineEmits(['update:visible'])
const visible = useVModel(props, 'visible', emit)

// 模拟数据，包含通知信息和对应的类型
const notifications = [
  { type: 'start', content: 'start' },
  { type: 'cancel', content: 'cancel' },
  { type: 'subscribe', content: 'subscribe' },
  { type: 'join', content: 'join' },
  { type: 'modify', content: 'modify' }
];

// 当前展开的折叠面板名称
const activeNames = ref([]);
const HEADERS = {
  start: '开始通知',
  cancel: '取消通知',
  subscribe: '订阅成功通知',
  join: '参加成功通知',
  modify: '活动修改通知'
};
// 根据通知类型获取对应的标题
const getHeaderByType = (type) => {
  return HEADERS[type] || '未知标题';
};
</script>
