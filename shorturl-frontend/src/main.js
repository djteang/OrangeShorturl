import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import { initStores } from './store'

// 初始化状态管理
initStores()

createApp(App)
  .use(router)
  .mount('#app')

