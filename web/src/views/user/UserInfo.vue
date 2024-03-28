<template>

  <a-layout>
    <a-layout-content class="userInfo-content">
      <div style="background-color: #ececec; padding: 20px">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-card title="个人信息" :bordered="false">
              <div class="user-icon">aa</div>
              <a-list size="large" bordered :data-source="listData">
                <template #renderItem="{ item }">
                  <a-list-item class="list-fl">
                    <div class="pull-left">{{ item.listName }}</div>
                    <div class="pull-right">{{ item.listValue}}</div>
                  </a-list-item>
                </template>
                <template #header>
<!--                  <div>Header</div>-->
                </template>
                <template #footer>
<!--                  <div>Footer</div>-->
                </template>
              </a-list>
            </a-card>
          </a-col>
          <a-col :span="16">
            <a-card title="个人中心" :bordered="false">
              <a-tabs
                  v-model:activeKey="activeKey"
                  size="large"
              >
                <a-tab-pane key="1">
                  <template #tab>
                  <span>
                    <apple-outlined/>
                    个人资料
                  </span>
                  </template>
                  <a-form
                      ref="formRef"
                      :model="userInfoForm"
                      name="user-register"
                      class="user-security"
                      :label-col="{ span: 3 }"
                      :wrapper-col="{ offset: 0,span: 15 }"
                      autocomplete="off"
                      :rules="rules"
                      @finish="handleFinish"
                      @finishFailed="handleFinishFailed"
                      @validate="handleValidate"
                  >


                    <a-form-item
                        name="userName"
                        has-feedback
                        label="用户名"
                    >
                      <a-input :disabled="true" size="large" v-bind:placeholder="userInfoForm.userName" />
                    </a-form-item>
                    <a-form-item
                        name="nickName"
                        has-feedback
                        label="昵称"
                    >
                      <a-input size="large" placeholder="请输入" v-model:value="userInfoForm.nickName"/>
                    </a-form-item>

                    <a-form-item
                        name="email"
                        has-feedback
                        label='邮箱'
                    >
                      <a-input size="large"  v-model:value="userInfoForm.email"/>
                    </a-form-item>
                    <a-form-item
                        name="phonenember"
                        has-feedback
                        label='手机号'
                    >
                      <a-input size="large"  v-model:value="userInfoForm.phonenumber"/>
                    </a-form-item>

                    <a-form-item :wrapper-col="{ offset: 2, span: 0 }">
                      <a-button type="primary" class="register-button" html-type="submit">
                        提交
                      </a-button>
                    </a-form-item >
                  </a-form>

                </a-tab-pane>
                <a-tab-pane key="2">
                  <template #tab>
                  <span>
                    <android-outlined/>
                    账号安全
                  </span>
                  </template>
                  <a-form
                      ref="formRef"
                      :model="formState"
                      name="user-register"
                      class="user-security"
                      :label-col="{ span: 3 }"
                      :wrapper-col="{ offset: 0,span: 15 }"
                      autocomplete="off"
                      :rules="rules"
                      @finish="handleFinish2"
                      @finishFailed="handleFinishFailed"
                      @validate="handleValidate"
                  >
                    <h3><span>修改密码</span></h3>


                    <a-form-item
                        name="oldPassword"
                        has-feedback
                        label="旧密码"
                    >
                      <a-input-password size="large" placeholder="请输入密码" v-model:value="formState.oldPassword">
                        <template #prefix>
                          <LockOutlined/>
                        </template>
                      </a-input-password>
                    </a-form-item>
                    <a-form-item
                        name="newPassword"
                        has-feedback
                        label="新密码"
                    >
                      <a-input-password size="large" placeholder="请输入密码" v-model:value="formState.newPassword">
                        <template #prefix>
                          <LockOutlined/>
                        </template>
                      </a-input-password>

                    </a-form-item>

                    <a-form-item
                        name="checkPass"
                        has-feedback
                        label="确认密码"
                    >
                      <a-input-password size="large" placeholder="请再次输入密码" v-model:value="formState.checkPass">
                        <template #prefix>
                          <LockOutlined/>
                        </template>
                      </a-input-password>
                    </a-form-item>

                    <a-form-item :wrapper-col="{ offset: 2, span: 0 }">
                      <a-button type="primary" class="register-button" html-type="submit">
                        提交
                      </a-button>
                    </a-form-item >
                  </a-form>

                </a-tab-pane>
              </a-tabs>
            </a-card>
          </a-col>
        </a-row>
      </div>
    </a-layout-content>

  </a-layout>


</template>

<script lang="ts" setup>
import {computed, onMounted, reactive, ref} from 'vue';
import { AppleOutlined, AndroidOutlined, UserOutlined, LockOutlined} from '@ant-design/icons-vue';
import {Rule} from "ant-design-vue/es/form";
import {FormInstance, message} from "ant-design-vue";
import axios from "axios";
import * as util from "util";
import {Tool} from "@/util/tool";
const activeKey = ref('1');

const userInfo = ref({
  userName: '',
  nickName: '',
  email: '',
  phonenumber: '',
  sex: '',
  avatar: ''
});
const userInfoForm = ref({
  userName: '',
  nickName: '',
  email: '',
  phonenumber: '',
  sex: '',
  avatar: ''
});

const listData = computed(() => [
  {
    listName: '用户名',
    listValue: userInfo.value.userName,
  },
  {
    listName: '昵称',
    listValue: userInfo.value.nickName || '无',
  },
  {
    listName: '邮箱',
    listValue: userInfo.value.email || '无',
  },
  {
    listName: '手机号',
    listValue: userInfo.value.phonenumber || '无',
  }
]);
const formState = reactive({
  oldPassword: '',
  newPassword: '',
  checkPass: '',
  email: ''
});
const formRef = ref<FormInstance>();
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
  } else if (value !== formState.newPassword) {
    return Promise.reject("两次密码不一致");
  } else {
    return Promise.resolve();
  }
};


const rules: Record<string, Rule[]> = {
  email: [{type: 'email', message: '请输入正确的邮箱格式'}],
  oldPassword: [{required: true, trigger: 'change',message: '密码不能为空'}],
  newPassword: [{required: true, validator: validatePass, trigger: 'change'}],
  checkPass: [{required: true, validator: validatePass2, trigger: 'change'}],
};

const getUserInfo =  () => {
  axios.get('/user/userInfo').then((resp) => {
    const respData = resp.data;
    if (respData.status === 200) {
      userInfo.value = respData.data;
      userInfoForm.value = Tool.copy(respData.data)
      console.log('userInfo',listData)
    } else {
      message.error("查询个人数据错误")
    }
  })
}

const putUserInfo =  (values: any) => {
  console.log('handleFinish=>',values, formState, formRef);
  axios.put('/user/userInfo',values).then((resp) => {
    const respData = resp.data;
    if (respData.status === 200) {
      message.success('操作成功');
      getUserInfo();
    } else {
      message.error("查询个人数据错误")
    }
  })
}
const putUserPassword =  (values: any) => {
  console.log('handleFinish=>',values, formState, formRef);
  axios.put('/user/userPassword',values).then((resp) => {
    const respData = resp.data;
    if (respData.status === 200) {
      message.success('操作成功');
      getUserInfo();
    } else {
      message.error("查询个人数据错误")
    }
  })
}
onMounted(() => {
  getUserInfo();
})

const handleFinish = (values: any) => {
  putUserInfo(values);
}
const handleFinish2 = (values: any) => {
  putUserPassword(values);
}

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

<style scoped>
.userInfo-content {
  margin: 60px auto;
  min-width: 800px;
  max-width: 1200px;
}
.user-security {
  padding: 20px 20px 20px 70px;
}
.list-fl {
  display: flex;
  justify-content: flex-start;
}


.pull-right {
  width: 400px;
}
.pull-left {
  width: 200px;
}

</style>