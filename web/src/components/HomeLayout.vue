<template>
  <a-layout id="components-layout-demo-custom-trigger">
    <a-layout-header style="background: #fff; padding: 0">
            <div class="header">
      <!--        <img class="header-logo" :src="logo">-->
              <span class="header-title">在线知识库系统</span>
              <div class="empty">
                <a-menu mode="horizontal" style="padding-top: 0">
                  <a-menu-item key="home">
                    <router-link to="/home"/>
                    电子书首页
                  </a-menu-item>
                  <a-menu-item key="about">
                    关于我们
                    <router-link to="/about"/>
                  </a-menu-item>
                </a-menu>
              </div>
<!--              <a-button style="margin-right: 24px;" @click="handlePreview">前台界面</a-button>-->
<!--              <span>管理员</span>-->
              <HeaderDropDown/>
<!--              <a class="header-quit" @click="handleLogout">退出</a>-->
            </div>
    </a-layout-header>
    <a-layout>
      <a-layout-content :style="{ margin: '16px 16px', minHeight: '200px' }">
        <router-view/>
      </a-layout-content>
    </a-layout>
  </a-layout>

</template>
<script setup lang="ts">
import {useRouter, useRoute} from 'vue-router'
// import logo from '/@/assets/images/k-logo.png'

import {
  HomeOutlined,
  AppstoreOutlined,
  FolderOutlined,
  UserOutlined,
  CommentOutlined,
  InfoCircleOutlined,
  TagOutlined,
  PieChartOutlined,
  DollarOutlined,
  LayoutOutlined,
  DatabaseOutlined
} from '@ant-design/icons-vue';

import {onMounted, ref} from 'vue';
import HeaderDropDown from "@/components/HeaderDropDown.vue";
// import {useUserStore} from "/@/store";

// const userStore = useUserStore();

const selectedKeys = ref<any[]>([])
const collapsed = ref<boolean>(false)

const router = useRouter()
const route = useRoute()

const handleClick = ({item, key, keyPath}) => {
  console.log('点击路由===>', key)
  router.push({
    name: key,
  })
}

const handlePreview = ()=>{
  let text = router.resolve({name: 'Home'})
  window.open(text.href, '_blank')
}

onMounted(() => {
  console.log('当前路由===>', route.name)
  selectedKeys.value = [route.name]
})


// const handleLogout = () => {
//   userStore.adminLogout().then(res => {
//     router.push({name: 'adminLogin'})
//   })
// }

</script>
<style scoped lang="less">

// header样式
.header {
  display: flex;
  flex-direction: row;
  align-items: center; // 垂直居中
  padding-left: 24px;
  padding-right: 24px;

  .header-logo {
    width: 32px;
    height: 32px;
    cursor: pointer;
  }

  .header-title {
    margin-left: 16px;
    font-size: 20px;
    font-weight: bold;
    text-align: center;
  }

  .empty {
    flex: 1;
  }

  .header-quit {
    margin-left: 12px;
  }
}


#components-layout-demo-custom-trigger {
  height: 100%;
}

#components-layout-demo-custom-trigger .trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

#components-layout-demo-custom-trigger .trigger:hover {
  color: #1890ff;
}


:deep(.ant-layout-content) {
  overflow-x: hidden;
}

:deep(.ant-layout-sider) {
  padding: 16px 0;
  background-color: #f0f2f5;
}

:deep(.ant-menu) {
  padding-top: 16px;
  height: 100%;
}

:deep(.ant-layout-sider-trigger) {
  background-color: #fff;
  height: 0px; // 故意设置0 隐藏
}
</style>
