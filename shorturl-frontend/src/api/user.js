import axios from 'axios'
import { userStore } from '../store'
import toast from '../utils/toast'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('API Error:', error)
    
    // 处理401未授权错误
    if (error.response && error.response.status === 401) {
      // 清除本地登录状态
      userStore.logout()
      
      // 显示错误提示
      const message = error.response.data?.message || '登录已过期，请重新登录'
      toast.error(message)
      
      // 如果不在登录页面，跳转到登录页
      if (window.location.pathname !== '/login' && window.location.pathname !== '/') {
        setTimeout(() => {
          window.location.href = '/login'
        }, 1500)
      }
    }
    
    return Promise.reject(error)
  }
)

export default {
  // 注册
  register(data) {
    return api.post('/user/register', data)
  },

  // 登录
  login(data) {
    return api.post('/user/login', data)
  },

  // 获取用户信息
  getUserInfo() {
    return api.get('/user/info')
  },

  // OAuth登录
  oauthLogin(data) {
    return api.post('/user/oauth/login', null, { params: data })
  },

  // 获取Linux.do授权URL
  getLinuxDoAuthUrl() {
    return api.get('/auth/oauth2/linux-do/authorize')
  },

  // Linux.do OAuth登录（使用code）
  linuxDoLogin(code) {
    return api.post('/auth/oauth2/linux-do/login', null, { params: { code } })
  },

  // 发送邮箱验证码
  sendVerifyCode(data) {
    return api.post('/user/send-verify-code', data)
  },

  // 更新头像
  updateAvatar(data) {
    return api.post('/user/update-avatar', data)
  },

  // 提交评价
  submitReview(data) {
    return api.post('/review/submit', data)
  },

  // 获取评价列表
  getReviewList(limit = 10) {
    return api.get('/review/list', { params: { limit } })
  },

  // 登出
  logout() {
    return api.post('/user/logout')
  },

  // 修改用户名
  updateUsername(username) {
    return api.put('/user/update-username', { username })
  },

  // 修改密码
  changePassword(data) {
    return api.put('/user/change-password', data)
  }
}

