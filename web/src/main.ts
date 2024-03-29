import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd, {message} from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import axios from 'axios';
import {Tool} from "@/util/tool";

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/**
 * axios 拦截器
 */

const token  = localStorage.getItem("token") || '';
axios.interceptors.request.use((config) => {
    console.log('请求参数：', config);
    if (Tool.isNotEmpty(token)) {
        config.headers['token'] = token;
    }
    return config;
}, error => {
    return Promise.reject(error);
});


axios.interceptors.response.use((response) => {
    console.log('返回结果：', response);
    if (response.data.status === 401) {
        console.log("=======登陆异常")
        localStorage.removeItem('token');
        store.commit('clearUser');
        message.error("账号异常，请重新登陆");
        router.push('/')
    }
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});

const app = createApp(App);

    app.use(Antd).use(store).use(router).mount('#app');

const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons);
}

console.log('环境：', process.env.NODE_ENV);
console.log('服务地址：', process.env.VUE_APP_SERVER);
