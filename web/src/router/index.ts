import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import HomeView from '../views/homeView.vue'
import DocView from '../views/doc.vue'
import AboutView from '../views/aboutView.vue'
import AdminEbook from "../views/admin/admin-ebook.vue";
import AdminCategory from "../views/admin/admin-category.vue";
import AdminDoc from "@/views/admin/admin-doc.vue";
import UserLayout from "@/views/user/UserLayout.vue";
import AdminUser from "@/views/admin/admin-user.vue";
import {Tool} from "@/util/tool";
import store from "@/store";

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
        path: '/category',
        name: 'AdminCategory',
        component: AdminCategory,
        meta: {requiresAuth: true, permissions: ['admin']}
    },
    {
        path: '/doc',
        name: 'AdminDoc',
        component: AdminDoc
    },
    {
        path: '/user',
        name: 'AdminUser',
        component: AdminUser,

    },
    {
        path: '/admin',
        name: 'Admin',
        component: () => import('../components/AdminLayout.vue'),
        children: [
            {path: '/errorLog', name: 'errorLog', component: () => import('../views/admin/ErrorLog.vue')},
            {path: '/opLog', name: 'opLog', component: () => import('../views/admin/OpLog.vue')},
            {path: '/loginLog', name: 'loginLog', component: () => import('../views/admin/LoginLog.vue')},
            {path: '/overview', name: 'overview', component: () => import('../components/the-welcome.vue')},
            {
                path: 'category',
                name: 'category',
                component: AdminCategory,
                // meta: {requiresAuth: true, permissions: ['admin']}
            },
            {
                path: 'doc',
                name: 'doc',
                component: AdminDoc
            },
            {
                path: 'user',
                name: 'user',
                component: AdminUser,

            },{
                path: 'ebook',
                name: 'ebook',
                component: AdminEbook
            },
        ]

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

router.beforeEach((to, from, next) => {
    //
    // if (to.meta.requiresAuth && Tool.isEmpty(store.state.user)) {
    //
    // }


    if (to.matched.some(function (item) {
        console.log(item, "是否需要登录校验：", item.meta.requiresAuth);
        return item.meta.requiresAuth
    })) {
        const loginUser = store.state.user;
        console.log('loginUser', loginUser);
        console.log('store', store.state);
        if (Tool.isEmpty(loginUser)) {
            console.log("用户未登录！");
            next('/user');
        } else {
            next();
        }
    } else {
        next();
    }
})

export default router
