import {createApp, reactive, ref} from 'vue';
import App from './App.vue';
import router from './routers';
import TDesign, {MessagePlugin} from 'tdesign-vue-next';
import 'tdesign-vue-next/es/style/index.css';
import './style/layout.less';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import VueDraggableResizable from 'vue-draggable-resizable';
import "vue-draggable-resizable/style.css";
import axios from 'axios';
import AMapLoader from "@amap/amap-jsapi-loader";

const app = createApp(App);
app.use(ElementPlus);
app.use(TDesign);

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    // 响应错误
    if (error.response.status === 401) {
        MessagePlugin.error('登录状态失效，请重新登录');
        router.push('/login');
    }
    return Promise.reject(error);
});

window._AMapSecurityConfig = {
    serviceHost: "http://localhost:8083/_AMapService",
};

export const AMap = ref(null);

AMapLoader.load({
    key: "76ed3d07815c638a64f5678ed9c833d3",
    version: "2.0",
    plugins: ["AMap.Scale", "AMap.ToolBar", "AMap.ControlBar", "AMap.MapType", "AMap.PlaceSearch", "AMap.AutoComplete"],
}).then((AMapInstance) => {
    AMap.value = AMapInstance;
}).catch((e) => {
    console.log(e);
});

app.use(router);
// 初始化全局变量 $userRole、$apiBaseUr
app.config.globalProperties.$userRole = null;
app.config.globalProperties.$userId = null;
app.config.globalProperties.$token = "a";
app.config.globalProperties.$apiBaseUrl = 'http://localhost:8083';
app.config.globalProperties.$webSocketBaseUrl = 'ws://localhost:8083';
app.component("vue-draggable-resizable", VueDraggableResizable);
app.mount('#app');

export const globalProperties = reactive({
    apiBaseUrl: 'http://localhost:8083',
    webSocketBaseUrl: 'ws://localhost:8083',
    token: 'a'
});
