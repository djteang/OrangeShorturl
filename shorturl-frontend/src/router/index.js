import { createRouter, createWebHistory } from 'vue-router'
import { userStore } from '../store'
import Landing from '../views/Landing.vue'
import Shorten from '../views/Shorten.vue'
import Manage from '../views/Manage.vue'
import Detail from '../views/Detail.vue'
import ApiDocs from '../views/ApiDocs.vue'
import Redirect from '../views/Redirect.vue'
import Login from '../views/Login.vue'
import OAuthCallback from '../views/OAuthCallback.vue'
import VisitLogs from '../views/VisitLogs.vue'
import Statistics from '../views/Statistics.vue'
import UaBlacklist from '../views/UaBlacklist.vue'
import TransitPage from '../views/TransitPage.vue'
import AiConfig from '../views/AiConfig.vue'
import ProfileSettings from '../views/ProfileSettings.vue'
import UrlGroups from '../views/UrlGroups.vue'

const routes = [
  {
    path: '/',
    name: 'Landing',
    component: Landing
  },
  {
    path: '/shorten',
    name: 'Shorten',
    component: Shorten
  },
  {
    path: '/manage',
    name: 'Manage',
    component: Manage,
    meta: { requiresAuth: true }
  },
  {
    path: '/visit-logs',
    name: 'VisitLogs',
    component: VisitLogs,
    meta: { requiresAuth: true }
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/ua-blacklist',
    name: 'UaBlacklist',
    component: UaBlacklist,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/transit-page',
    name: 'TransitPage',
    component: TransitPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/ai-config',
    name: 'AiConfig',
    component: AiConfig,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/profile',
    name: 'ProfileSettings',
    component: ProfileSettings,
    meta: { requiresAuth: true }
  },
  {
    path: '/url-groups',
    name: 'UrlGroups',
    component: UrlGroups,
    meta: { requiresAuth: true }
  },
  {
    path: '/detail/:shortCode',
    name: 'Detail',
    component: Detail,
    meta: { requiresAuth: true }
  },
  {
    path: '/api-docs',
    name: 'ApiDocs',
    component: ApiDocs
  },
  {
    path: '/r/:shortCode',
    name: 'Redirect',
    component: Redirect,
    meta: { standalone: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/oauth-callback',
    name: 'OAuthCallback',
    component: OAuthCallback
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 检查是否需要登录
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!userStore.isLoggedIn()) {
      // 未登录，跳转到登录页
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    } else {
      // 检查是否需要管理员权限
      if (to.matched.some(record => record.meta.requiresAdmin)) {
        if (!userStore.isAdmin()) {
          // 不是管理员，跳转到首页
          next('/')
        } else {
          next()
        }
      } else {
        next()
      }
    }
  } else {
    next()
  }
})

export default router

