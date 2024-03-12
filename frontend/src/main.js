import { createApp } from 'vue'
import App from './App.vue'
import TDesign from 'tdesign-vue-next';
import 'tdesign-vue-next/es/style/index.css';
import router from './routers';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// import {createVuestic, createIconsConfig} from "vuestic-ui";
// import "vuestic-ui/css";
const app = createApp(App)
app.use(ElementPlus)
app.use(TDesign);
app.use(router);
app.mount('#app')