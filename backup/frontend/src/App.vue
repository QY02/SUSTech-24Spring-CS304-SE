<template>
  <div>
    <router-view></router-view>
  </div>
</template>

<script>
import { provide } from 'vue';

export default {
  name: 'App',
  setup() {
    // 设置全局变量
    provide('$API_URL', 'http://47.107.113.54:8083');
  }
}
</script>


