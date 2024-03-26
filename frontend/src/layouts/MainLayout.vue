<template>
  <div>
    <t-layout>
      <t-aside :width="isSidebarCollapsed ? '64px' : null">
        <t-menu class="side-nav" theme="light" :value='highlightItem' :collapsed="isSidebarCollapsed">
          <template #logo>
            <img height="28" src="https://tdesign.gtimg.com/site/baseLogo-light.png" alt="logo"/>
          </template>
          <t-menu-item value="home" @click="handleNav('home')">
            <template #icon>
              <HomeIcon/>
            </template>
            首页
          </t-menu-item>
          <t-menu-item value="book" @click="handleNav('book')">
            <template #icon>
              <HomeIcon/>
            </template>
            book
          </t-menu-item>
          <t-menu-item value="event" @click="handleNav('event')">
            <template #icon>
              <HomeIcon/>
            </template>
            event
          </t-menu-item>
          <t-menu-item value="approval" @click="handleNav('approval')">
            <template #icon>
              <HomeIcon/>
            </template>
            审批
          </t-menu-item>
          <t-menu-item value="moments" @click="handleNav('moments')">
            <template #icon>
              <HomeIcon/>
            </template>
            动态
          </t-menu-item>
        </t-menu>
      </t-aside>
      <t-layout>
        <t-header>
          <t-head-menu class="header-menu">
            <template #logo>
              <div class="header-operate-left">
                <t-button theme="default" shape="square" variant="text" @click="changeCollapsed">
                  <ViewListIcon />
                </t-button>
              </div>
            </template>
            <template #operations>
              <div class="operations-container">
                <t-tooltip placement="bottom" content="用户信息">
                  <t-button theme="default" shape="square" variant="text">
                    <UserCircleIcon class="header-menu-icon"/>
                  </t-button>
                </t-tooltip>
                <t-tooltip placement="bottom" content="通知">
                  <t-button theme="default" shape="square" variant="text">
                    <NotificationIcon class="header-menu-icon"/>
                  </t-button>
                </t-tooltip>
                <t-tooltip placement="bottom" content="设置">
                  <t-button theme="default" shape="square" variant="text">
                    <SettingIcon class="header-menu-icon"/>
                  </t-button>
                </t-tooltip>
              </div>
            </template>
          </t-head-menu>
        </t-header>
        <t-content >
          <t-layout class="content-layout" >
            <t-content>
              <router-view></router-view>
            </t-content>
          </t-layout>
        </t-content>
      </t-layout>
    </t-layout>
  </div>
</template>

<script setup lang="ts">
import {
  NotificationIcon,
  UserCircleIcon,
  SettingIcon,
  HomeIcon,
  ViewListIcon
} from 'tdesign-icons-vue-next';
import config from '@/config/style.js';
import {onMounted, onBeforeUnmount, ref} from "vue";
import router from '@/routers';
const highlightItem = ref('home')
let isSidebarCollapsed = ref(config.isSidebarCollapsed);
const sidebarElement = ref(null);
const contentElement = ref(null);

const updateWidth = () => {
  if (sidebarElement.value && contentElement.value) {
    const sidebarElementWidth = sidebarElement.value.offsetWidth;
    contentElement.value.style.width = 'calc(100vw - ' + sidebarElementWidth + 'px)';
  }
};

const resizeObserver = new ResizeObserver(() => {
  updateWidth();
});

onMounted(() => {
  sidebarElement.value = document.querySelector('.side-nav');
  contentElement.value = document.querySelector('.content-layout');
  window.addEventListener('resize', updateWidth);
  resizeObserver.observe(sidebarElement.value);
  updateWidth();
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateWidth);
  resizeObserver.unobserve(sidebarElement.value);
});

const changeCollapsed = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};

const handleNav = (value: string) => {
  highlightItem.value = value
  switch (value) {
    case 'home':
      router.push('/HomePage');
      break;
    case 'book':
      router.push('/book');
      break;
    case 'event':
      router.push('/event');
      break;
    case 'approval':
      router.push('/approval');
      break;
    case 'moments':
      router.push('/moments');
      break;
  }
}
</script>

<style scoped>

</style>