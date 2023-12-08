import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';

const app = createApp(App)

    app.use(Antd).use(store).use(router).mount('#app');

const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons);
}

console.log('环境：', process.env.NODE_ENV);
console.log('服务地址：', process.env.VUE_APP_SERVE);
