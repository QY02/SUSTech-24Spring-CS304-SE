<template>
  <t-popup expand-animation placement="bottom-right" trigger="click">
    <template #content>
      <div class="header-msg">
        <div class="header-msg-top">
          <p>{{ '通知中心' }}</p>
          <t-button
              v-if="unreadMsg.length > 0"
              class="clear-btn"
              variant="text"
              theme="primary"
              @click="setRead('all','')"
          >{{ '全部设为已读' }}
          </t-button>
        </div>
        <t-list v-if="unreadMsg.length > 0" class="narrow-scrollbar" :split="false">
          <t-list-item v-for="(item, index) in unreadMsg" :key="index">
            <div>
              <p class="msg-content">{{ item.content }}</p>
              <p class="msg-type">{{ statusMapping[item.type] }}</p>
            </div>
            <p class="msg-time">{{ item.notifyTime }}</p>
            <template #action>
              <t-button size="small" variant="outline" @click="setRead('radio', item)">
                {{ '设为已读' }}
              </t-button>
            </template>
          </t-list-item>
        </t-list>

        <div v-else class="empty-list">
          <img src="https://tdesign.gtimg.com/pro-template/personal/nothing.png" alt="空"/>
          <p>{{ '没有未读通知' }}</p>
        </div>

        <div v-if="unreadMsg.length > 0" class="header-msg-bottom">
          <t-button class="header-msg-bottom-link" variant="text" theme="default" block @click="goDetail">
            {{ '查看全部' }}
          </t-button>
        </div>
      </div>
    </template>
    <t-badge :count="unreadMsg.length" :offset="[4, 4]">
      <t-button theme="default" shape="square" variant="text">
        <t-icon name="mail"/>
      </t-button>
    </t-badge>
  </t-popup>
</template>

<script setup>
import {ref, computed, onMounted, watchEffect} from 'vue';
import {useRouter} from 'vue-router';
import axios from "axios";
import {MessagePlugin} from "tdesign-vue-next";

const token = sessionStorage.getItem('token')
const router = useRouter();
const statusMapping = {
  0: '其他',
  1: '审核通过',
  2: '审核未通过',
  3: '成功参加活动',
  4: '活动修改',
  5: '活动取消'
};
const msgData = ref([]);
const getNotice = () => {
  axios.get(`/notification/all`, {
    headers: {
      token: token
    },
  }).then(response => {
    console.log(response)
    msgData.value = response.data.data
  }).catch();
}

let unreadMsg = computed(() => msgData.value.filter(item => item.status === 0));

const setRead = (type, item) => {
  const changeMsg = msgData.value;
  if (type === 'all') {
    changeMsg.forEach(e => {
      e.status = 1;
    });
  } else {
    const targetMsg = changeMsg.find(e => e.id === item.id);
    if (targetMsg) {
      targetMsg.status = 1;
    }
  }
  msgData.value = changeMsg;
};

const goDetail = () => {
  router.push('/notification');
};

onMounted(() => {
  getNotice(); // 调用 getNotice 方法
})

</script>

<style lang="less" scoped>
.header-msg {
  width: 400px;
  //height: 440px;
  margin: calc(0px - var(--td-comp-paddingTB-xs)) calc(0px - var(--td-comp-paddingLR-s));

  .empty-list {
    height: calc(100% - 120px);
    text-align: center;
    padding: var(--td-comp-paddingTB-xxl) 0;
    font: var(--td-font-body-medium);
    color: var(--td-text-color-secondary);

    img {
      width: var(--td-comp-size-xxxxl);
    }

    p {
      margin-top: var(--td-comp-margin-xs);
    }
  }

  &-top {
    position: relative;
    font: var(--td-font-title-medium);
    color: var(--td-text-color-primary);
    text-align: left;
    padding: var(--td-comp-paddingTB-l) var(--td-comp-paddingLR-xl) 0;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .clear-btn {
      right: calc(var(--td-comp-paddingTB-l) - var(--td-comp-paddingLR-xl));
    }
  }

  &-bottom {
    align-items: center;
    display: flex;
    justify-content: center;
    padding: var(--td-comp-paddingTB-s) var(--td-comp-paddingLR-s);
    border-top: 1px solid var(--td-component-stroke);

    &-link {
      text-decoration: none;
      cursor: pointer;
      color: var(--td-text-color-placeholder);
    }
  }

  .t-list {
    //height: calc(100% - 104px);
    padding: var(--td-comp-margin-s) var(--td-comp-margin-s);
  }

  .t-list-item {
    overflow: hidden;
    width: 100%;
    padding: var(--td-comp-paddingTB-l) var(--td-comp-paddingLR-l);
    border-radius: var(--td-radius-default);
    font: var(--td-font-body-medium);
    color: var(--td-text-color-primary);
    cursor: pointer;
    transition: background-color 0.2s linear;

    &:hover {
      background-color: var(--td-bg-color-container-hover);

      .msg-content {
        color: var(--td-brand-color);
      }

      .t-list-item__action {
        button {
          bottom: var(--td-comp-margin-l);
          opacity: 1;
        }
      }

      .msg-time {
        bottom: -6px;
        opacity: 0;
      }
    }

    .msg-content {
      margin-bottom: var(--td-comp-margin-s);
    }

    .msg-type {
      color: var(--td-text-color-secondary);
    }

    .t-list-item__action {
      button {
        opacity: 0;
        position: absolute;
        right: var(--td-comp-margin-xxl);
        bottom: -6px;
      }
    }

    .msg-time {
      transition: all 0.2s ease;
      opacity: 1;
      position: absolute;
      right: var(--td-comp-margin-xxl);
      bottom: var(--td-comp-margin-l);
      color: var(--td-text-color-secondary);
    }
  }
}
</style>
