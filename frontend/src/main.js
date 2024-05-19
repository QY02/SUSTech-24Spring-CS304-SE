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
import {createVuestic, createIconsConfig} from "vuestic-ui";
import "vuestic-ui/css";
import "material-design-icons-iconfont/dist/material-design-icons.min.css";

import axios from 'axios';
import AMapLoader from "@amap/amap-jsapi-loader";

const app = createApp(App);
app.use(ElementPlus);
app.use(TDesign);
app.use(
    createVuestic({
        config: {
            icons: createIconsConfig({
                aliases: [
                    {
                        name: "bell",
                        color: "#FFD43A",
                        to: "fa4-bell",
                    },
                    {
                        name: "ru",
                        to: "flag-icon-ru small",
                    },
                ],
                fonts: [
                    {
                        name: "fa4-{iconName}",
                        resolve: ({iconName}) => ({class: `fa fa-${iconName}`}),
                    },
                    {
                        name: "flag-icon-{countryCode} {flagSize}",
                        resolve: ({countryCode, flagSize}) => ({
                            class: `flag-icon flag-icon-${countryCode} flag-icon-${flagSize}`,
                        }),
                    },
                ],
            }),
            // ...
        },
    })
)


axios.defaults.baseURL = 'http://localhost:8083'
// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    // console.log(response)
    if (response.status !== 200) {
        return Promise.reject(response);
    }
    return response;
}, function (error) {
    if (error) {
        if(error.response!==undefined && error.response.status === 401){
            MessagePlugin.error("登录状态失效，请重新登陆")
            router.push('/login')
            return Promise.reject(error);
        } else {
            // 响应错误
            if (error.response) {
                MessagePlugin.error(error.response.data.msg)
            } else {
                MessagePlugin.error(error.message)
            }
        }
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
    webSocketBaseUrl: 'ws://localhost:8083', token: 'a'
});



const fileServerAxios = axios.create({
    baseURL: 'http://47.107.113.54:25572',
});

fileServerAxios.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    if (error.response) {
        MessagePlugin.error(error.response.data.msg);
    } else {
        MessagePlugin.error(error.message);
    }
    return Promise.reject(error);
});

export { fileServerAxios };
