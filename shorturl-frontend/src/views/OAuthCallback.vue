<template>
  <div class="min-h-screen flex items-center justify-center gradient-bg">
    <div class="glass-effect rounded-2xl border dark:border-slate-600 border-slate-300 p-12 max-w-md w-full shadow-2xl text-center">
      <div v-if="loading" class="space-y-6">
        <div class="inline-block animate-spin rounded-full h-16 w-16 border-4 dark:border-slate-700 border-slate-300 border-t-orange-500"></div>
        <h2 class="text-2xl font-bold dark:text-slate-100 text-slate-900">正在登录...</h2>
        <p class="dark:text-slate-400 text-slate-600">请稍候，正在处理您的登录请求</p>
      </div>

      <div v-else-if="error" class="space-y-6">
        <svg class="w-16 h-16 text-red-500 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
        </svg>
        <h2 class="text-2xl font-bold dark:text-slate-100 text-slate-900">登录失败</h2>
        <p class="dark:text-slate-400 text-slate-600">{{ error }}</p>
        <button
          @click="$router.push('/login')"
          class="px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg font-medium hover:shadow-lg transition-all"
        >
          返回登录
        </button>
      </div>

      <div v-else class="space-y-6">
        <svg class="w-16 h-16 text-green-500 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
        </svg>
        <h2 class="text-2xl font-bold dark:text-slate-100 text-slate-900">登录成功！</h2>
        <p class="dark:text-slate-400 text-slate-600">正在跳转...</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { userStore } from '../store'
import toast from '../utils/toast'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const error = ref('')

onMounted(() => {
  handleOAuthCallback()
})

const handleOAuthCallback = () => {
  try {
    // 从URL参数获取token和用户信息
    const token = route.query.token
    const username = route.query.username
    const userId = route.query.userId
    const email = route.query.email
    const avatar = route.query.avatar
    const errorMessage = route.query.error

    if (errorMessage) {
      error.value = decodeURIComponent(errorMessage)
      loading.value = false
      toast.error('登录失败: ' + error.value)
      return
    }

    if (!token || !username || !userId) {
      error.value = '登录信息不完整，请重试'
      loading.value = false
      toast.error(error.value)
      return
    }

    // 构造用户信息对象
    const userInfo = {
      id: parseInt(userId),
      username: username,
      email: email || '', // 从URL参数中获取邮箱
      avatarBase64: avatar || '' // 从URL参数中获取头像URL
    }

    // 保存登录状态
    userStore.login(userInfo, token)
    
    loading.value = false
    toast.success('登录成功！')

    // 延迟跳转，让用户看到成功提示
    setTimeout(() => {
      router.push('/manage')
    }, 1000)

  } catch (err) {
    loading.value = false
    error.value = '处理登录信息失败: ' + err.message
    toast.error(error.value)
  }
}
</script>

