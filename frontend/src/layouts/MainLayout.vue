<script setup lang="ts">
import {
  NotificationIcon,
  UserCircleIcon,
  SettingIcon,
  HomeIcon,
  ViewListIcon
} from 'tdesign-icons-vue-next';
</script>

<template>
  <div>
    <t-layout>
      <t-aside :width="isSidebarCollapsed ? '64px' : null">
        <t-menu class="side-nav" theme="light" value="home" :collapsed="isSidebarCollapsed">
          <template #logo>
            <img height="28" src="https://tdesign.gtimg.com/site/baseLogo-light.png" alt="logo"/>
          </template>
          <t-menu-item value="home" @click="handleNav('home')">
            <template #icon>
              <HomeIcon/>
            </template>
            首页
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

<script lang="ts">
import config from '@/config/style.js';
import {computed, ref} from "vue";
import router from '@/routers';

let isSidebarCollapsed = ref(config.isSidebarCollapsed);

const changeCollapsed = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};

const handleNav = (value: string) => {
  switch (value) {
    case 'home':
      router.push('/');
      break;
  }
}
</script>

<style scoped>

</style>