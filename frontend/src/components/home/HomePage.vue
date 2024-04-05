<template>
  <el-backtop :right="100" :bottom="100"/>
  <el-affix ref="affix" :offset="55">

    <t-tabs class="container_home" :default-value="'最新'" @change="handlerChange" size="large" style="z-index: 1;">
      <t-tab-panel v-for="tab in TABS" :key="tab.title" :value="tab.title">
        <template #label style="margin-right: 5px">
          <t-icon :name="tab.icon"/>
          {{ tab.title }}
        </template>
        <!--        {{ tab.title }}-->
      </t-tab-panel>
    </t-tabs>


    <t-select-input
        v-model:inputValue="inputValue"
        :value="value"
        :allow-input="false"
        :placeholder="'筛选活动'"
        :tag-input-props="{ excessTagsDisplayType: 'scroll' }"
        :popup-props="popupProps"
        clearable
        multiple
        @tag-change="onTagChange"
        @input-change="onInputChange"
        class="custom_select"
    >
      <template #panel>
        <t-checkbox-group
            v-if="options.length"
            :value="checkboxValue"
            :options="options"
            class="tdesign-demo__panel-options-multiple"
            @change="onCheckedChange"
        />
        <div v-else class="tdesign-demo__select-empty-multiple">暂无数据</div>
      </template>
      <template #suffixIcon>
        <chevron-down-icon/>
      </template>
    </t-select-input>


    <t-input class="customer" v-model="formData.search" clearable placeholder="请输入搜索内容  回车搜索"
             @enter="getSearch" size="large" auto-width style="z-index: 2; ">
      <template #prefix-icon>

        <search-icon/>
      </template>
    </t-input>

  </el-affix>
  <!--发布活动按钮-->
  <t-popup content="发布活动">
    <t-button shape="circle" theme="primary" size="large" style="position: fixed;right: 30px;bottom: 40px">
      <template #icon>
        <add-icon/>
      </template>

    </t-button>
  </t-popup>
  <component :is="currentTab.component"></component>


</template>


<script setup lang="jsx">
import axios from "axios";
import {computed, inject, reactive, ref} from "vue";
// import router from "@/routers";
import {AddIcon, SearchIcon} from "tdesign-icons-vue-next";

import {ThumbUpIcon, ChatIcon, ShareIcon, MoreIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import HomeHot from "@/components/home/HomeHot.vue";
import HomeNew from "@/components/home/HomeNew.vue";
import HomeRecommend from "@/components/home/HomeRecommend.vue";

const title = '标题';


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
    title: "最新",
    component: HomeNew,
    icon: "home"
  },
  {
    title: "热搜",
    component: HomeHot,
    icon: "rocket"

  },

  {
    title: "推荐",
    component: HomeRecommend,
    icon: "star"
  },
  // {
  //   title: "GitHub",
  //   component: GitHubLogin
  // }
];
const value_tab = ref(TABS[0].title);
const handlerChange = (newValue) => {
  value_tab.value = newValue;
};
const currentTab = computed(() => {
  // alert(currentTab)
  return TABS.find((tab) => tab.title === value_tab.value);
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

import {ChevronDownIcon} from 'tdesign-icons-vue-next';

const OPTIONS = [
  // 全选
  {label: '全选', checkAll: true},
  {label: '讲座', value: 1},
  {label: '校园活动', value: 2},
  {label: '竞赛', value: 3},

];

const inputValue = ref('');
// 全量数据
const options = ref([...OPTIONS]);
const value = ref([
  {label: '讲座', value: 1},
  {label: '校园活动', value: 2},
  {label: '竞赛', value: 3},
]);

const popupProps = ref({
  overlayInnerClassName: ['narrow-scrollbar'],
  overlayInnerStyle: {
    maxHeight: '280px',
    overflowY: 'auto',
    overscrollBehavior: 'contain',
    padding: '6px',
  },
});

const checkboxValue = computed(() => {
  const arr = [];
  const list = value.value;
  // 此处不使用 forEach，减少函数迭代
  for (let i = 0, len = list.length; i < len; i++) {
    list[i].value && arr.push(list[i].value);
  }
  return arr;
});

// 直接 checkboxgroup 组件渲染输出下拉选项
const onCheckedChange = (val, {current, type}) => {
  console.log(current);
  // current 不存在，则表示操作全选
  if (!current) {
    value.value = type === 'check' ? options.value.slice(1) : [];
    return;
  }
  // 普通操作
  if (type === 'check') {
    const option = options.value.find((t) => t.value === current);
    value.value.push(option);
  } else {
    value.value = value.value.filter((v) => v.value !== current);
  }
};

// 可以根据触发来源，自由定制标签变化时的筛选器行为
const onTagChange = (currentTags, context) => {
  console.log(currentTags, context);
  const {trigger, index, item} = context;
  if (trigger === 'clear') {
    value.value = [];
  }
  if (['tag-remove', 'backspace'].includes(trigger)) {
    value.value.splice(index, 1);
  }
  // 如果允许创建新条目

};
const onInputChange = (val, context) => {
  console.log(val, context);
};
</script>


<style scoped>

.container_home {
  position: relative;
}

.customer {
  position: absolute;
  width: 300px;
  top: 10px;
  right: 20px;
}
.custom_select {
  position: absolute;
  width: 250px;
  top: 15px;
  right: 350px;
  z-index: 2;
}
.tdesign-demo__panel-options-multiple {
  width: 100%;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.tdesign-demo__panel-options-multiple .t-checkbox {
  display: flex;
  border-radius: 3px;
  line-height: 22px;
  cursor: pointer;
  padding: 3px 8px;
  color: var(--td-text-color-primary);
  transition: background-color 0.2s linear;
  white-space: nowrap;
  word-wrap: normal;
  overflow: hidden;
  text-overflow: ellipsis;
  margin: 0;
}

.tdesign-demo__panel-options-multiple .t-checkbox:hover {
  background-color: var(--td-bg-color-container-hover);
}
</style>
