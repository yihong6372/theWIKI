<template>

  <a-layout>
    <a-layout-content class="userInfo-content">
      <div style="background-color: #ececec; padding: 20px">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-card title="个人信息" :bordered="false">
              <div class="user-icon"></div>
              <a-list size="large" bordered :data-source="listData">
                <template #renderItem="{ item }">
                  <a-list-item class="list-fl">
                    <div class="pull-left">{{ item.title}}</div>
                    <div class="pull-right">{{ item.title}}</div>
                  </a-list-item>
                </template>s
                <template #header>
                  <div>Header</div>
                </template>
                <template #footer>
                  <div>Footer</div>
                </template>
              </a-list>
            </a-card>
          </a-col>
          <a-col :span="16">
            <a-card title="信息编辑" :bordered="false">
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
                  Tab 1
                  <a-form
                      ref="formRef"
                      :model="formState"
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
                    <h3><span>修改密码</span></h3>


                    <a-form-item
                        name="password"
                        has-feedback
                        label="旧密码"
                    >
                      <a-input-password size="large" placeholder="请输入密码" v-model:value="formState.oldPassword"/>
                    </a-form-item>
                    <a-form-item
                        name="password"
                        has-feedback
                        label="旧密码"
                    >
                      <a-input-password size="large" placeholder="请输入密码" v-model:value="formState.newPassword"/>
                    </a-form-item>

                    <a-form-item
                        name="checkPass"
                        has-feedback
                        label="确认密码"
                    >
                      <a-input-password size="large" placeholder="请再次输入密码" v-model:value="formState.checkPass"/>
                    </a-form-item>

                    <a-form-item :wrapper-col="{ offset: 2, span: 0 }">
                      <a-button type="primary" class="register-button" html-type="submit">
                        注册
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
                  Tab 2
                  <a-form
                      ref="formRef"
                      :model="formState"
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
import {reactive, ref} from 'vue';
import { AppleOutlined, AndroidOutlined, UserOutlined, LockOutlined} from '@ant-design/icons-vue';
import {Rule} from "ant-design-vue/es/form";
import {FormInstance} from "ant-design-vue";
const activeKey = ref('1');


const listData = reactive([
  {
    title: '用户名称',
  },
  {
    title: '用户名称2',
  },
  {
    title: '用户名称3',
  },
  {
    title: '用户名称',
  },
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
  email: [{required: true, trigger: 'change',message: '用户名不能为空'},{type: 'email', message: '请输入正确的邮箱格式'}],
  oldPassword: [{required: true, trigger: 'change',message: '密码不能为空'}],
  newPassword: [{required: true, validator: validatePass, trigger: 'change'}],
  checkPass: [{required: true, validator: validatePass2, trigger: 'change'}],
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