<template>
  <t-descriptions title="预定内容" column="1">
    <t-descriptions-item label="活动名称">{{ eventDetail.name }}</t-descriptions-item>
    <t-descriptions-item label="活动时间">{{ sessionInformation[bookingInformation.chosenSession].startTime }} - {{
      sessionInformation[bookingInformation.chosenSession].endTime }}</t-descriptions-item>
    <t-descriptions-item label="活动场地">{{ sessionInformation[bookingInformation.chosenSession].venue
      }}</t-descriptions-item>
    <t-descriptions-item label="座位">{{ bookingInformation.chosenSeat }}</t-descriptions-item>
  </t-descriptions>
  <t-descriptions-item label="价格">{{ bookingInformation.seatPrice }}</t-descriptions-item>
  <t-space size="medium">
    <t-button @click="currentStep--">上一步</t-button>
    <t-button @click="prePay">前往付款</t-button>
  </t-space>


</template>
<script setup lang="ts">
import axios, { AxiosRequestConfig } from 'axios';
import { currentStep, submitData } from '@/components/book/Steps.vue';
import { onMounted, onUnmounted, reactive, Ref, ref, watch } from "vue";
import { sessionInformation, bookingInformation } from '@/components/book/Steps.vue';
import { MessagePlugin, NotifyPlugin } from "tdesign-vue-next";

const eventDetail = ref({
  name: ''
})
const getEventDetail = () => {
  axios.get(`/event/getEventDetail/${sessionStorage.getItem('eventId')}`, {
    headers: {
      token: sessionStorage.getItem('token')
    }
  }).then((response) => {
    eventDetail.value = response.data.data
  }).catch(() => { })
}
getEventDetail();
const result = ref(0);
const orderId = ref(-1)
const prePay = async () => {
  axios.post("/orderRecord/prePay", {
    eventId: bookingInformation.eventId,
    eventSessionId: sessionInformation[bookingInformation.chosenSession].eventSessionId,
    seatId: bookingInformation.chosenSeat,
    additionalInformation: JSON.stringify(bookingInformation.additionalInformation.map(item => ({
      name: item.name,
      nameEng: item.nameEng,
      value: item.value
    })))
  }, { headers: { token: sessionStorage.getItem('token') } } as AxiosRequestConfig).then((response) => {
    orderId.value = response.data.data;
    MessagePlugin.success('提交支付信息成功');
    window.open(`http://localhost:5173/orderRecord/pay/${orderId}`)
    startPolling();
    // currentStep.value++;
  })
    .catch(error => { })
}

const payResult = ref(0);

let intervalId = null;
let timeoutId = null;

const startPolling = () => {
  if (timeoutId) {
    clearTimeout(timeoutId);
    timeoutId = null; // 清除后将timeoutId设为null
  }
    // 定义轮询函数
  const poll = () => {
    if (payResult.value === 1) { // 当外部条件满足时
      clearInterval(intervalId); // 清除间隔定时器
      return; // 结束轮询
    }

    // 每10秒请求一次
    getPayResult(); // 执行轮询请求
  };

  // 每10秒请求一次
  intervalId = setInterval(poll, 10000);
  // 10分钟后停止轮询
  timeoutId = setTimeout(() => {
    clearInterval(intervalId);
  }, 10 * 60 * 1000); // 10分钟
};

onUnmounted(() => {
  if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
      }
});

  function getPayResult() {
    axios.post("/orderRecord/getPayResultById", { id: orderId },
      { headers: { token: sessionStorage.getItem('token') } } as AxiosRequestConfig)
      .then(response => {
        payResult.value = response.data.data;
      })
      .catch(error => {
      });
  }
</script>
