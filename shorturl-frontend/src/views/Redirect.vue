<template>
  <div class="min-h-screen flex items-center justify-center px-4">
    <div class="max-w-lg w-full">
      <div class="glass-effect rounded-2xl p-12 text-center border dark:border-slate-600 border-slate-300 shadow-xl">
        <div v-if="loading">
          <div class="w-16 h-16 border-4 dark:border-slate-700 border-slate-300 border-t-orange-500 rounded-full animate-spin mx-auto mb-6"></div>
          <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-3">正在跳转...</h2>
          <p class="dark:text-slate-400 text-slate-600">请稍候，正在为您重定向到目标页面</p>
        </div>

        <div v-else-if="error" class="text-center">
          <svg class="w-16 h-16 text-red-500 mx-auto mb-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
          </svg>
          <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-3">链接无效</h2>
          <p class="dark:text-slate-400 text-slate-600 mb-6">{{ error }}</p>
          <router-link 
            to="/" 
            class="inline-block px-6 py-3 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 rounded-lg font-semibold transition-all shadow-md hover:shadow-lg border dark:border-slate-600 border-slate-400">
            返回首页
          </router-link>
        </div>

        <div v-else class="text-center">
          <svg class="w-16 h-16 text-green-500 mx-auto mb-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
          </svg>
          <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-3">跳转成功</h2>
          <p class="dark:text-slate-400 text-slate-600 mb-2">正在前往</p>
          <p class="dark:text-orange-400 text-orange-600 text-sm break-all mb-6 font-medium">{{ targetUrl }}</p>
          <a 
            :href="targetUrl" 
            class="inline-block px-6 py-3 bg-orange-600 hover:bg-orange-500 text-white rounded-lg font-semibold transition-all shadow-md hover:shadow-lg transform hover:-translate-y-0.5 border border-orange-600">
            如未自动跳转，请点击这里
          </a>
        </div>

        <div class="mt-8 pt-6 border-t dark:border-slate-600 border-slate-300">
          <p class="text-xs dark:text-slate-400 text-slate-500">
            Powered by <span class="dark:text-orange-400 text-orange-600 font-medium">Orange ShortUrl</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const loading = ref(true)
const error = ref('')
const targetUrl = ref('')

onMounted(async () => {
  const shortCode = route.params.shortCode
  
  if (!shortCode) {
    error.value = '短码不能为空'
    loading.value = false
    return
  }

  try {
    // 调用后端的解析接口获取原始URL
    const response = await axios.get(`/api/url/resolve/${shortCode}`)

    if (response.data.code === 200 && response.data.data) {
      targetUrl.value = response.data.data
      loading.value = false
      
      // 等待2秒后跳转
      setTimeout(() => {
        window.location.href = targetUrl.value
      }, 2000)
    } else {
      error.value = response.data.message || '短链接不存在或已过期'
      loading.value = false
    }
  } catch (err) {
    if (err.response?.status === 404) {
      error.value = '短链接不存在'
    } else if (err.response?.status === 410) {
      error.value = '短链接已过期'
    } else {
      error.value = err.response?.data?.message || '访问失败，请稍后重试'
    }
    loading.value = false
  }
})
</script>

