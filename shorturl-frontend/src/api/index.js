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
  // 生成短链接
  shorten(data) {
    return api.post('/url/shorten', data)
  },

  // 获取链接列表
  getUrlList(params) {
    return api.get('/url/list', { params })
  },

  // 获取链接详情
  getUrlDetail(shortCode) {
    return api.get(`/url/detail/${shortCode}`)
  },

  // 删除链接
  deleteUrl(shortCode) {
    return api.delete(`/url/${shortCode}`)
  },

  // 获取访问统计
  getStats(shortCode) {
    return api.get(`/url/stats/${shortCode}`)
  },

  // 获取访问日志列表
  getVisitLogs(params) {
    return api.get('/visit-log/list', { params })
  }
}

