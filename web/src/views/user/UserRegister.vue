<template>
  <div class="main">
    <a-form
        ref="formRef"
        :model="formState"
        name="user-register"
        class="user-layout-register"
        :label-col="{ span: 8 }"
        :wrapper-col="{ offset: 2,span: 30 }"
        autocomplete="off"
        :rules="rules"
        @finish="handleFinish"
        @finishFailed="handleFinishFailed"
        @validate="handleValidate"
    >
      <h3><span>注册</span></h3>

<!--          label="邮箱"-->
      <a-form-item
          name="email"
          has-feedback
      >
        <a-input size="large" placeholder="请输入邮箱" v-model:value="formState.email"/>
      </a-form-item>
      <a-form-item
          name="userName"
          has-feedback
      >
        <a-input size="large" placeholder="请输入用户名" v-model:value="formState.userName"/>
      </a-form-item>

      <a-form-item
          name="password"
          has-feedback
      >
        <a-input-password size="large" placeholder="请输入密码" v-model:value="formState.password"/>
      </a-form-item>

      <a-form-item
          name="checkPass"
          has-feedback
      >
        <a-input-password size="large" placeholder="请再次输入密码" v-model:value="formState.checkPass"/>
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 2, span: 0 }">
        <a-button type="primary" class="register-button" html-type="submit">
          注册
        </a-button>
      </a-form-item >

      <a-form-item class="user-register-other">
        <router-link class="login" :to="'login'">已有账户，立即登陆</router-link>
      </a-form-item>
    </a-form>
  </div>

</template>

<script lang="ts" setup>
import { reactive, ref} from "vue";
import {FormInstance, message} from "ant-design-vue";
import {Rule} from "ant-design-vue/es/form";
import {useRouter} from "vue-router";
import axios from "axios";
import store from "@/store";

const router = useRouter();

interface FormState {
  userName: string
  password: string;
  checkPass: string;
  email: string;
}

const formRef = ref<FormInstance>();
const formState = reactive<FormState>({
  userName: '',
  password: '',
  checkPass: '',
  email: ''
});
const validatePass = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('密码不能为空');
  } else {
    if (formState.checkPass !== '') {
      formRef.value!.validateFields('checkPass');
    }
    return Promise.resolve();
  }
};
const validatePass2 = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('请再次输入密码');
  } else if (value !== formState.password) {
    return Promise.reject("两次密码不一致");
  } else {
    return Promise.resolve();
  }
};


const rules: Record<string, Rule[]> = {
  email: [{required: true, trigger: 'change',message: '用户名不能为空'},{type: 'email', message: '请输入正确的邮箱格式'}],
  userName: [{required: true, trigger: 'change',message: '用户名不能为空'}],
  password: [{required: true, validator: validatePass, trigger: 'change'}],
  checkPass: [{validator: validatePass2, trigger: 'change'}],
};

const handleFinish = (values: FormState) => {
  console.log('handleFinish=>',values, formState, formRef);

  axios.post('/user/register',formState).then((response) => {
    const data = response.data;
    if (data.code === 200) {
      message.success(data.msg);

      store.dispatch('updateUserNamePassword', {
        userName: formState.userName,
        password: formState.password
      });
      console.log('设置完成store', store.state);

      //注册成功跳转
      console.log('注册成功，开始跳转/login');
      router.push('login')
    }else {
      console.log('注册失败==》',data)
      message.error(data.msg);
    }
  })

};
const handleFinishFailed = errors => {
  console.log('handleFinishFailed =>',errors);
};
const resetForm = () => {
  formRef.value.resetFields();
};
const handleValidate = (...args) => {
  console.log('handleValidate=>',args);
};


</script>

<style lang="less" scoped>

.user-layout-register {
  background-color: rgba(255, 255, 255, 0.4); /* 白色背景，50% 透明度 */
  backdrop-filter: blur(5px); /* 模糊效果 */
  padding: 20px 30px 20px 10px; /* 内边距，*/
  border-radius: 30px; /* 圆角边框，增加美观性 */


  label {
    font-size: 20px;
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

  button.register-button {
    padding: 0 15px;
    border-radius: 4px;
    font-size: 16px;
    height: 40px;
    width: 100%;
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

    .login {
      float: right;
    }
  }
}
</style>
