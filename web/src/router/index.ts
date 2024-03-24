import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/homeView.vue'
import DocView from '../views/doc.vue'
import AboutView from '../views/aboutView.vue'
import AdminEbook from "../views/admin/admin-ebook.vue";
import AdminCategory from "../views/admin/admin-category.vue";
import AdminDoc from "@/views/admin/admin-doc.vue";
import UserLayout from "@/views/user/UserLayout.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/doc',
    name: 'Doc',
    component: DocView
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/aboutView.vue')
    component: AboutView
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: AdminEbook
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: AdminCategory
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: AdminDoc
  },
  {
    path: '/user',
    name: 'userView',
    component: UserLayout,
    redirect: '/user/login',
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/UserLogin.vue')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/UserRegister.vue')
      },
      {
        path: '/user/info',
        name: 'userInfo',
        component: () => import('@/views/user/UserInfo.vue')
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
