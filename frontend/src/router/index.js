import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'
import Login from '../views/Login.vue'
import UserManage from '../views/UserManage.vue'
import BookManage from '../views/BookManage.vue'
import Report from '../views/Report.vue'
import ImportExport from '../views/ImportExport.vue'
import CategoryManage from '../views/CategoryManage.vue'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/',
        component: Layout,
        redirect: '/users', // 重定向到用户管理
        children: [
            {
                path: 'users',
                name: 'UserManage',
                component: UserManage,
                meta: { title: '用户管理', icon: 'el-icon-user' }
            },
            {
                path: 'books',
                name: 'BookManage',
                component: BookManage,
                meta: { title: '图书管理', icon: 'el-icon-notebook-2' }
            },
            {
                path: 'categories',
                name: 'CategoryManage',
                component: CategoryManage,
                meta: { title: '分类管理', icon: 'el-icon-s-grid' }
            },
             {
                path: 'reports',
                name: 'Report',
                component: Report,
                meta: { title: '统计报表', icon: 'el-icon-s-data' }
            },
            {
                path: 'import-export',
                name: 'ImportExport',
                component: ImportExport,
                meta: { title: '导入导出', icon: 'el-icon-s-promotion' }
            }
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (!token && to.path !== '/login' && to.path !== '/register') {
    // 如果没有token且访问的不是登录/注册页，则跳转到登录页
    return next('/login')
  }
  next()
})

export default router 