import {createApp, reactive} from 'vue';
import App from './App.vue';
import router from './routers';
import TDesign, {MessagePlugin} from 'tdesign-vue-next';
import 'tdesign-vue-next/es/style/index.css';
import './style/layout.less';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import VueDraggableResizable from 'vue-draggable-resizable';
import "vue-draggable-resizable/style.css";
import axios from 'axios'; // 导入axios

const app = createApp(App);
app.use(ElementPlus);
app.use(TDesign);



axios.defaults.baseURL = 'http://localhost:8083'
// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    console.log(response)
    if(response.status !== 200){
        return Promise.reject(response);
    }
    return response;
}, function (error) {
    if(error){
        // console.log(error)
        if(error.response.status === 401){
            MessagePlugin.error("登录状态失效，请重新登陆")
            router.push('/login')
            return Promise.reject(error);
        }
        else{
            // 响应错误
            if (error.response) {
                MessagePlugin.error(error.response.data.msg)
            }
            else{
                MessagePlugin.error(error.message)
            }
        }
    }
    return Promise.reject(error);
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
    webSocketBaseUrl: 'ws://localhost:8083',
    token: 'a'
});








// 创建axios实例
const instance = axios.create({
  baseURL: 'http://localhost:8083', // 假设你的后端服务在这个地址
});

// 添加响应拦截器
instance.interceptors.response.use(function (response) {
  // 对响应数据做些什么
  return response;
}, function (error) {
  // 对响应错误做些什么
  console.log("有进来！！")
  if (error.response && error.response.status === 401) {
    MessagePlugin.error("登录状态失效，请重新登陆");
    // 在这里进行登录状态失效的处理，比如跳转到登录页面
    router.push('/login');
  } else {
    // 其他HTTP错误的处理
    if (error.response) {
      MessagePlugin.error(error.response.data.msg);
    } else {
      MessagePlugin.error(error.message);
    }
  }
  return Promise.reject(error);
});

// 导出axios实例
export default instance;
