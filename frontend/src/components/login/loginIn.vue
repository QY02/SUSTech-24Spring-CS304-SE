<template>
  <NavBarWithOnlyTitle v-if="showNavBar"></NavBarWithOnlyTitle>
  <div class="parent-container">
    <div class="form-container">
      <p class="title">Welcome!</p>
      <t-tabs :default-value="'ID'">
        <t-tab-panel v-for="tab in tabs" :key="tab.title" :value="tab.title" :label="tab.title">
  <!--        {{ tab.title }}-->
        </t-tab-panel>
      </t-tabs>

<!--      <va-tabs v-model="value" grow>-->
<!--        <template #tabs>-->
<!--          <va-tab v-for="tab in tabs" :key="tab.title" :name="tab.title">-->
<!--            {{ tab.title }}-->
<!--          </va-tab>-->
<!--        </template>-->

      <div class="demo-card">
<!--        <t-card :title="title" hover-shadow :style="{ width: '340px' }">-->
<!--          {{ infoMessage }}-->
<!--          <template #actions>-->
<!--            <a href="javascript:void(0)" @click="clickHandler">操作</a>-->
<!--          </template>-->
<!--        </t-card>-->
      </div>
        <t-card class="va-card" hover-shadow>
          <template class="va-card-content">
            <component :is="currentTab.component"></component>
            <p class="sign-up-label">
              Don't have an account?
              <router-link to="/register"><span class="sign-up-link">Sign up</span></router-link>
            </p>
          </template>
        </t-card>
<!--      </va-tabs>-->
    </div>
  </div>

</template>

<script>
import {computed, ref, watchEffect} from "vue";
import NavBarWithOnlyTitle from "@/components/layouts/NavBarWithOnlyTitle.vue";
import {useRoute} from "vue-router";
import IDLogin from "./IDLogin.vue"
import EmailLogin from "./EmailLogin.vue"
// import GitHubLogin from "./GitHubLogin.vue"
import { MessagePlugin } from 'tdesign-vue-next';


const TABS = [
  {
    title: "ID",
    component: IDLogin

  },
  {
    title: "Email",
    component: EmailLogin
  },
  // {
  //   title: "GitHub",
  //   component: GitHubLogin
  // }
];
export default {
  components: {
    NavBarWithOnlyTitle
  },

  setup() {
    sessionStorage.setItem('primary-color', '#154EC1')
    const clickHandler = () => {
      MessagePlugin.success('操作');
    };

    const title = '标题';
    const infoMessage = `卡片内容，以描述性为主，可以是文字、图片或图文组合的形式。按业务需求进行自定义组合。`;
    const showNavBar = ref(true);
    const tabs = TABS;
    const value = ref(TABS[0].title);

    const currentTab = computed(() => {
      return tabs.find((tab) => tab.title === value.value);
    });

    const route = useRoute()

    watchEffect(() => {
      showNavBar.value = route.matched.some((route) => route.name === "login");
    },
        // {immediate: true}
    );

    return {
      clickHandler,
      title,
      infoMessage,
      showNavBar,
      currentTab,
      value,
      tabs
    };
  }
};
</script>

<style scoped>
.parent-container {
  display: flex;
  justify-content:right; /* 水平居中 */
  margin-right: 9%;
  margin-top: 5%;
  align-items: center; /* 垂直居中 */
}

.form-container {
  width: 400px;
  height: 500px;
  background-color: #fff;
  box-shadow: rgba(0, 0, 0, 0.35) 0 5px 15px;
  border-radius: 10px;
  box-sizing: border-box;
  padding: 20px 30px;
//margin-top: 50px; /* 与页面顶部的距离为50px */
}

.title {
  text-align: center;
  font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
  "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
  margin: 10px 0 30px 0;
  font-size: 28px;
  font-weight: 800;
}


.sign-up-label {
  margin: 0;
  font-size: 10px;
  color: #747474;
  font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
  "Lucida Sans Unicode", Geneva, Verdana, sans-serif;;
  align-content: flex-end
}

.sign-up-link {
  margin-left: 1px;
  font-size: 11px;
  text-decoration: underline;
  text-decoration-color: teal;
  color: teal;
  cursor: pointer;
  font-weight: 800;
  font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
  "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
}

.buttons-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  margin-top: 20px;
  gap: 15px;
}


.va-card {
  width: 340px;
  height: 320px;
}

.va-card-content {
  display: flex;
  flex-direction: column;
}

</style>