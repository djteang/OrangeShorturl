<template>
  <div class="min-h-screen flex items-center justify-center px-4" :style="pageStyle">
    <div class="max-w-lg w-full">
      <!-- 自定义中转页 -->
      <div v-if="transitConfig && !error" class="text-center">
        <div class="mb-8">
          <svg 
            class="w-20 h-20 mx-auto mb-4 animate-spin" 
            :style="{ color: transitConfig.buttonColor }" 
            fill="none" 
            stroke="currentColor" 
            viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
          <h1 
            class="text-3xl font-bold mb-4" 
            :style="{ color: transitConfig.textColor }">
            {{ transitConfig.title }}
          </h1>
          <p 
            class="text-lg mb-6" 
            :style="{ color: transitConfig.textColor }">
            {{ transitConfig.content }}
          </p>
          <div class="mb-6" :style="{ color: transitConfig.textColor }">
            <span class="text-4xl font-bold">{{ countdown }}</span>
            <span class="text-xl ml-2">秒后自动跳转</span>
          </div>
        </div>
        <button
          @click="jumpNow"
          class="px-8 py-3 rounded-lg text-white font-semibold shadow-lg hover:shadow-xl transition-all transform hover:scale-105"
          :style="{ backgroundColor: transitConfig.buttonColor }">
          立即跳转
        </button>
        
        <!-- Powered By -->
        <div class="mt-8 pt-6 border-t" :style="{ borderColor: transitConfig.textColor + '30' }">
          <div class="flex items-center justify-center gap-2" :style="{ color: transitConfig.textColor, opacity: 0.7 }">
            <span class="text-xs">Powered by</span>
            <img 
              v-if="transitConfig.poweredByIcon" 
              :src="transitConfig.poweredByIcon" 
              alt="icon"
              class="w-4 h-4 rounded object-cover"
            />
            <span class="text-xs font-medium">{{ transitConfig.poweredByName || 'Orange ShortUrl' }}</span>
          </div>
        </div>
      </div>

      <!-- 默认跳转页 -->
      <div v-else-if="!transitConfig" class="glass-effect rounded-2xl p-12 text-center border dark:border-slate-600 border-slate-300 shadow-xl">
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

      <!-- 错误提示（自定义中转页模式） -->
      <div v-else-if="error" class="glass-effect rounded-2xl p-12 text-center border dark:border-slate-600 border-slate-300 shadow-xl">
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
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import api from '../api'

const route = useRoute()
const loading = ref(true)
const error = ref('')
const targetUrl = ref('')
const transitConfig = ref(null)
const countdown = ref(0)
let timer = null

// 计算页面样式（自定义中转页模式）
const pageStyle = computed(() => {
  if (transitConfig.value) {
    return {
      backgroundColor: transitConfig.value.backgroundColor
    }
  }
  return {}
})

// 立即跳转
const jumpNow = () => {
  if (timer) {
    clearInterval(timer)
  }
  if (targetUrl.value) {
    window.location.href = targetUrl.value
  }
}

// 开始倒计时
const startCountdown = (waitTime) => {
  countdown.value = waitTime
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
      jumpNow()
    }
  }, 1000)
}

onMounted(async () => {
  const shortCode = route.params.shortCode
  
  if (!shortCode) {
    error.value = '短码不能为空'
    loading.value = false
    return
  }

  try {
    // 1. 先获取中转页配置
    const transitResponse = await api.getTransitPageByShortCode(shortCode)
    if (transitResponse.code === 200 && transitResponse.data) {
      transitConfig.value = transitResponse.data
    }

    // 2. 获取目标URL
    const urlResponse = await axios.get(`/api/url/resolve/${shortCode}`)

    if (urlResponse.data.code === 200 && urlResponse.data.data) {
      targetUrl.value = urlResponse.data.data
      loading.value = false
      
      // 如果有自定义中转页，使用配置的等待时间
      if (transitConfig.value) {
        startCountdown(transitConfig.value.waitTime)
      } else {
        // 默认2秒后跳转
        setTimeout(() => {
          window.location.href = targetUrl.value
        }, 2000)
      }
    } else {
      error.value = urlResponse.data.message || '短链接不存在或已过期'
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
    transitConfig.value = null
  }
})
</script>

