<template>
  <div>
    <a-dropdown v-show="true" class="menu">
      <span v-show="user.id" class="ant-pro-account-avatar">
      <a-avatar src="https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png"
                class="antd-pro-global-header-index-avatar"/>
<!--        <span style="color: #00ff22">{{ user.name }}</span>-->
      </span>
      <template #overlay>
        <a-menu @click="handleMenuClick">

          <a-menu-item key="1">
            <UserOutlined/>
            个人设置
          </a-menu-item>

          <a-popconfirm placement="right" ok-text="是" cancel-text="否" @confirm="logout">
            <template #title>
              <p>是否退出登陆</p>
            </template>
            <a-menu-item key="2">
              <UserOutlined/>
              退出登陆
            </a-menu-item>
          </a-popconfirm>
        </a-menu>
      </template>
      <!--      <a-button v-show="user.id">-->
      <!--        {{user.name}}-->
      <!--        <DownOutlined />-->
      <!--      </a-button>-->
    </a-dropdown>
    <a-dropdown class="menu">
      <template #overlay>
        <a-menu @click="handleLoginMenuClick">

          <a-menu-item key="1">
            <UserOutlined/>
            登陆
          </a-menu-item>

          <a-menu-item key="2">
            <UserOutlined/>
            注册
          </a-menu-item>
        </a-menu>
      </template>
      <span v-show="!user.id" class="ant-pro-account-avatar">
        <span>请登录</span>
        <RightOutlined />
      <a-avatar size="large" src="https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png"
                class="antd-pro-global-header-index-avatar"/>
      <span style="color: #00ff22">{{ user.name }}</span>
      </span>
    </a-dropdown>

  </div>

</template>

<script lang="ts" setup>
import {UserOutlined, DownOutlined, RightOutlined} from '@ant-design/icons-vue';
import type {MenuProps} from 'ant-design-vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import { jwtDecode } from 'jwt-decode';
const router = useRouter();
const user = ref({
  id: '',
  name: ''
});

console.log('user.id', user.value.id);
const handleMenuClick: MenuProps['onClick'] = e => {
  console.log('click', e);
  if (e.key === '2') {
    // logout();
  } else if (e.key === '1') {
    userInfo();
  }
};

const handleLoginMenuClick: MenuProps['onClick'] = e => {
  console.log('click', e);
  if (e.key === '1') {
    // logout();
    router.push('/user/login');
  } else if (e.key === '2') {
    router.push('/user/register');
  }
};

// 用户登出
function logout() {
  console.log('logout');
  // router.push('/user');
  axios.get("/user/logout").then((response) => {
    let data = response.data;
    if (data.status === 200) {
      message.success(data.msg);
      localStorage.setItem('token', '');
      localStorage.setItem('user', '');
      resetUser();
    } else {
      console.log('登出异常》', data);
      message.error(data.msg);
    }
  })
}

function resetUser() {
  let a = localStorage.getItem('token');
  if (a) {
    user.value.id = '1';
    user.value.name = a || '';
  } else {
    user.value.id = '';
    user.value.name = '';
  }
}

const jwt :any = localStorage.getItem('token');
const decodedToken = ref({});
console.log('jwt',jwt);
function decodeJwt() {
  try {
    decodedToken.value = jwtDecode(jwt);
    console.log('jwt',decodedToken.value);
  } catch (error) {
    console.error('jwt解析失败', error);
  }
}

onMounted(() => {
  resetUser()
  decodeJwt();
})


// 用户设置
function userInfo() {
  console.log('userInfo')
  // console.log('路由', route);
  router.push('/user/info');
}
</script>
<style scoped>
.demo-dropdown-wrap :deep(.ant-dropdown-button) {
  margin-right: 8px;
  margin-bottom: 8px;
}

.menu {
  float: right;
}
</style>