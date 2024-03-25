<template>
  <div class="main">
    <a-form
        :model="formState"
        name="user-login"
        class="user-layout-login"
        :label-col="{ span: 8 }"
        :wrapper-col="{offset:2 ,span: 30 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
    >
      <h3 class="title">用户登陆</h3>
      <a-form-item
          class="userName"
          name="userName"
          :rules="[{ required: true, message: 'Please input your username!' }]"
      >
        <a-input placeholder="请输入用户名" size="large" v-model:value="formState.userName"/>
      </a-form-item>

      <a-form-item
          name="password"
          :rules="[{ required: true, message: 'Please input your password!' }]"
      >
        <a-input-password placeholder="请输入密码" size="large" v-model:value="formState.password"/>
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 2, span: 0 }">
        <a-button
            type="primary"
            class="login-button"
            html-type="submit">Submit
        </a-button>
      </a-form-item>

      <a-form-item class="remember" name="remember" :wrapper-col="{ offset: 8, span: 3 }">
        <a-checkbox v-model:checked="formState.remember">Remember</a-checkbox>

      </a-form-item>

      <a-form-item class="user-register-other">
        <router-link class="register" :to="'register'">没有账户，立即注册</router-link>
      </a-form-item>

    </a-form>
    <div>
<!--      <TestView/>-->
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, defineComponent, onMounted, reactive} from "vue";
import {message} from "ant-design-vue";
import axios from "axios";
import store from "@/store";
import TestView from "@/views/user/TestView.vue";
import { useRouter} from "vue-router";
import jwtDecode from 'jwt-decode';



console.log('UserLoginView');

interface FormState {
  userName: string;
  password: string;
  remember: boolean;
}

const formState = reactive<FormState>({
  userName: '',
  password: '',
  remember: true,
});
const onFinish = (values: any) => {
  console.log('FormState->', formState);
  login();
  console.log('Success:', values);
};

const onFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo);
};
const router = useRouter();
const login = () => {
  axios.post('/user/login', formState).then((response) => {
    let respData = response.data;
    console.log(respData);
    if (respData.status === 200) {
      // store.commit("setUser", respData.data.token);
      localStorage.setItem('token', respData.data.token);
      localStorage.setItem('user', 'testLogin');
      message.success('登陆成功');
      console.log(localStorage.getItem('token'));
      router.push('/user/info')
    } else {
      message.error(respData.msg);
    }
  })
};

onMounted(() => {
  console.log('填充前', formState);
  formState.userName = store.state.userName;
  formState.password = store.state.password;
  console.log('填充后', formState);
})

</script>

<style lang="less" scoped>
.user-layout-login {
  background-color: rgba(255, 255, 255, 0.4); /* 白色背景，50% 透明度 */
  backdrop-filter: blur(5px); /* 模糊效果 */
  padding: 20px 30px 20px 10px; /* 内边距，*/
  border-radius: 30px; /* 圆角边框，增加美观性 */


  label {
    font-size: 14px;
  }

  .userName {

  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    border-radius: 4px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .remember {
    float: left;
  }

  .user-register-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}
</style>