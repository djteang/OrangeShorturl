import { reactive } from 'vue'

// 用户状态管理
export const userStore = reactive({
  user: null,
  token: null,
  
  // 初始化（从localStorage读取）
  init() {
    const token = localStorage.getItem('token')
    const user = localStorage.getItem('user')
    
    if (token && user) {
      this.token = token
      this.user = JSON.parse(user)
    }
  },
  
  // 登录
  login(user, token) {
    this.user = user
    this.token = token
    localStorage.setItem('token', token)
    localStorage.setItem('user', JSON.stringify(user))
  },
  
  // 登出
  logout() {
    this.user = null
    this.token = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  },
  
  // 更新头像
  updateAvatar(avatarBase64) {
    if (this.user) {
      this.user.avatarBase64 = avatarBase64
      localStorage.setItem('user', JSON.stringify(this.user))
    }
  },
  
  // 是否已登录
  isLoggedIn() {
    return !!this.token
  }
})

// 主题状态管理
export const themeStore = reactive({
  isDark: false,
  
  // 初始化
  init() {
    const theme = localStorage.getItem('theme')
    this.isDark = theme === 'dark'
    if (this.isDark) {
      document.documentElement.classList.add('dark')
    }
  },
  
  // 切换主题（带平滑过渡动画）
  toggle() {
    this.isDark = !this.isDark
    localStorage.setItem('theme', this.isDark ? 'dark' : 'light')
    
    // 检查是否支持 View Transitions API
    if (document.startViewTransition) {
      // 使用 View Transitions API 实现平滑过渡
      document.startViewTransition(() => {
        if (this.isDark) {
          document.documentElement.classList.add('dark')
        } else {
          document.documentElement.classList.remove('dark')
        }
      })
    } else {
      // 降级方案：直接切换主题类，依赖 CSS transition
      if (this.isDark) {
        document.documentElement.classList.add('dark')
      } else {
        document.documentElement.classList.remove('dark')
      }
    }
  }
})

// 初始化所有store
export function initStores() {
  userStore.init()
  themeStore.init()
}

