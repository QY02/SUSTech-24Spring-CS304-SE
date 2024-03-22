<template>
  <el-backtop :right="100" :bottom="100"/>
  <el-affix ref="affix" :offset="55" >

  <t-tabs class="container_home" :default-value="'热搜'" @change="handlerChange" size="medium" style="z-index: 1;">
    <t-tab-panel v-for="tab in TABS" :key="tab.title" :value="tab.title" :label="tab.title">
      <!--        {{ tab.title }}-->
    </t-tab-panel>
  </t-tabs>


  <t-input class="customer" v-model="formData.search" clearable placeholder="请输入搜索内容  回车搜索"
           @enter="getSearch" size="large" auto-width style="z-index: 2; ">
    <template #prefix-icon>

      <search-icon/>
    </template>
  </t-input>

  </el-affix>
  <component :is="currentTab.component"></component>





</template>


<script setup lang="jsx">
import axios from "axios";
import {computed, inject, reactive, ref} from "vue";
// import router from "@/routers";
import {SearchIcon} from "tdesign-icons-vue-next";
import IDLogin from "@/components/login/IDLogin.vue";
import EmailLogin from "@/components/login/EmailLogin.vue";

import {ThumbUpIcon, ChatIcon, ShareIcon, MoreIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import HomeHot from "@/components/home/HomeHot.vue";
import HomeNew from "@/components/home/HomeNew.vue";
import HomeRecommend from "@/components/home/HomeRecommend.vue";

const title = '标题';

const subtitle = '副标题';

const cover = 'https://tdesign.gtimg.com/site/source/card-demo.png';

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
const apiUrl = inject('$API_URL');

// 获取全局变量 $apiBaseUrl
axios.defaults.baseURL = apiUrl;
const formData = reactive({
  search: '',
});

const getSearch = () => {
  alert('event')

  // axios.post('/course/getMyCourse', {}, {
  //   headers: {
  //     token: sessionStorage.getItem('token'),
  //   },
  // }).then(() => {
  //
  // }).catch((error) => {
  //   if (error.response) {
  //     // 请求已发出，但服务器响应的状态码不在 2xx 范围内
  //     MessagePlugin.error(error.response.data.msg);
  //   } else {
  //     // 一些错误是在设置请求的时候触发
  //     MessagePlugin.error(error.message);
  //   }
  //
  // });
};
const TABS = [
  {
    title: "热搜",
    component: HomeHot

  },
  {
    title: "最新",
    component: HomeNew
  },
  {
    title: "推荐",
    component: HomeRecommend
  },
  // {
  //   title: "GitHub",
  //   component: GitHubLogin
  // }
];
const value = ref(TABS[0].title);
const handlerChange = (newValue) => {
  value.value = newValue;
};
const currentTab = computed(() => {
  // alert(currentTab)
  return TABS.find((tab) => tab.title === value.value);
});

// //type
//
// const navigateToCourse = (courseId) => {
//   // Use router to navigate to CourseDetail with parameters
//   sessionStorage.setItem('cid', courseId)
//   // router.push({name: '/CourseDetail', params: {id: courseId}});
//   router.push('/layoutCourse')
// };
//
// const navigateToCourseTeacher = (courseId) => {
//   // Use router to navigate to CourseDetail with parameters
//   sessionStorage.setItem('cid', courseId)
//   sessionStorage.setItem('userType', '1')
//   // router.push({name: '/CourseDetail', params: {id: courseId}});
//   router.push({name: 'CourseDetailTeacher', params: {id: courseId}});
// };
//
// onMounted(() => {
//   // Fetch courses when the component is mounted
//   fetchCourses();
// });


</script>


<style scoped>

.container_home {
  position: relative;
}

.customer {
  position: absolute;
  width: 300px;
  top: 3px;
  right: 20px;
}
</style>
