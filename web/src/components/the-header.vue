<template>
  <a-layout-header class="header" style="display: flex; justify-content: space-between; align-items: center;">
    <div class="logo">在线知识库</div>
      <a-menu

          theme="dark"
          mode="horizontal"
          style="flex-grow: 1;"
          :style="{ lineHeight: '64px'}"
      >
        <a-menu-item key="/">
          <router-link to="/">首页</router-link>
        </a-menu-item>
        <a-menu-item key="/about">
          <router-link to="/about">关于我们</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/ebook">
          <router-link to="/admin/ebook">电子书管理</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/category">
          <router-link to="/admin/category">分类管理</router-link>
        </a-menu-item>
        <!--      <a-menu-item key="/admin/doc">-->
        <!--        <router-link to="/admin/doc">文档管理</router-link>-->
        <!--      </a-menu-item>-->

      </a-menu>


    <div style="display: inline-block">
      <HeaderDropDown/>
    </div>



<!--    <span style="color: #00ff22">5426136 bk4j</span>-->

  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import HeaderDropDown from "@/components/HeaderDropDown.vue";

export  default defineComponent({
  name: 'the-header',
  components: {HeaderDropDown}
});
</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 28px 16px 0;
  float: left;
  color: white;
  font-size: 18px;
}
.drop {
  width: 120px;
  height: 31px;
  float: right;
/*  background: rgba(255, 255, 255, 0.2);
  margin: 16px 28px 16px 0;

  color: white;
  font-size: 18px;*/
}
</style>