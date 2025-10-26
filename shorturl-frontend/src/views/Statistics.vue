<template>
  <div class="min-h-[calc(100vh-12rem)] py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <div class="flex items-center justify-center mb-4">
          <svg class="w-12 h-12 text-indigo-600 dark:text-indigo-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
          </svg>
        </div>
        <h1 class="text-3xl font-bold text-gray-900 dark:text-slate-50 mb-2">访问统计</h1>
        <p class="text-gray-600 dark:text-slate-400">查看您的短链接数据分析和访问趋势</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="text-center py-12">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-4 dark:border-slate-700 border-slate-300 border-t-indigo-500"></div>
        <p class="dark:text-slate-400 text-slate-600 mt-4">加载中...</p>
      </div>

      <!-- 错误状态 -->
      <div v-else-if="error" class="text-center py-12">
        <svg class="w-16 h-16 text-red-500 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
        </svg>
        <p class="dark:text-slate-400 text-slate-600">{{ error }}</p>
      </div>

      <!-- 统计内容 -->
      <div v-else class="space-y-6">
        <!-- 概览卡片 -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <!-- 总访问次数 -->
          <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center">
                <div class="w-12 h-12 bg-blue-500/20 dark:bg-blue-500/30 rounded-lg flex items-center justify-center">
                  <svg class="w-6 h-6 text-blue-600 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                </div>
              </div>
            </div>
            <div>
              <p class="text-sm dark:text-slate-400 text-slate-600 mb-1">总访问次数</p>
              <p class="text-3xl font-bold dark:text-slate-50 text-slate-900">{{ stats.totalVisits?.toLocaleString() || 0 }}</p>
            </div>
          </div>

          <!-- 总短链接数 -->
          <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center">
                <div class="w-12 h-12 bg-green-500/20 dark:bg-green-500/30 rounded-lg flex items-center justify-center">
                  <svg class="w-6 h-6 text-green-600 dark:text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                  </svg>
                </div>
              </div>
            </div>
            <div>
              <p class="text-sm dark:text-slate-400 text-slate-600 mb-1">短链接总数</p>
              <p class="text-3xl font-bold dark:text-slate-50 text-slate-900">{{ stats.totalUrls?.toLocaleString() || 0 }}</p>
            </div>
          </div>

          <!-- 今日访问 -->
          <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center">
                <div class="w-12 h-12 bg-orange-500/20 dark:bg-orange-500/30 rounded-lg flex items-center justify-center">
                  <svg class="w-6 h-6 text-orange-600 dark:text-orange-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
              </div>
            </div>
            <div>
              <p class="text-sm dark:text-slate-400 text-slate-600 mb-1">今日访问</p>
              <p class="text-3xl font-bold dark:text-slate-50 text-slate-900">{{ stats.todayVisits?.toLocaleString() || 0 }}</p>
            </div>
          </div>

          <!-- 昨日访问 -->
          <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center">
                <div class="w-12 h-12 bg-purple-500/20 dark:bg-purple-500/30 rounded-lg flex items-center justify-center">
                  <svg class="w-6 h-6 text-purple-600 dark:text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                </div>
              </div>
            </div>
            <div>
              <p class="text-sm dark:text-slate-400 text-slate-600 mb-1">昨日访问</p>
              <p class="text-3xl font-bold dark:text-slate-50 text-slate-900">{{ stats.yesterdayVisits?.toLocaleString() || 0 }}</p>
              <p v-if="stats.yesterdayVisits && stats.todayVisits" class="text-xs mt-1" :class="getTrendClass()">
                {{ getTrendText() }}
              </p>
            </div>
          </div>
        </div>

        <!-- 访问趋势图 -->
        <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
          <h2 class="text-xl font-semibold dark:text-slate-50 text-slate-900 mb-6 flex items-center gap-2">
            <svg class="w-6 h-6 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" />
            </svg>
            最近7天访问趋势
          </h2>
          <div v-if="stats.dailyTrends && stats.dailyTrends.length > 0" class="space-y-4">
            <div class="flex items-end justify-between gap-2 px-2" style="height: 256px;">
              <div v-for="trend in stats.dailyTrends" :key="trend.date" class="flex-1 flex flex-col items-center justify-end gap-1" style="height: 100%;">
                <!-- 柱子容器，占据大部分高度 -->
                <div class="w-full" style="height: calc(100% - 30px); display: flex; flex-direction: column; justify-content: flex-end; align-items: center;">
                  <!-- 数字显示在柱子上方 -->
                  <div class="text-xs dark:text-slate-400 text-slate-600 font-medium whitespace-nowrap mb-1">
                    {{ trend.count }}
                  </div>
                  <!-- 柱子 -->
                  <div 
                    class="w-full bg-gradient-to-t from-indigo-500 to-indigo-400 dark:from-indigo-600 dark:to-indigo-500 rounded-t-lg transition-all duration-500 hover:scale-105"
                    :style="{ height: getBarHeight(trend.count) }"
                  >
                  </div>
                </div>
                <!-- 日期标签 -->
                <div class="text-xs dark:text-slate-500 text-slate-500 font-medium" style="height: 24px; display: flex; align-items: center;">
                  {{ formatDate(trend.date) }}
                </div>
              </div>
            </div>
          </div>
          <div v-else class="text-center py-8 dark:text-slate-400 text-slate-600">
            暂无访问数据
          </div>
        </div>

        <!-- 地域分布和设备类型 -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <!-- 地域分布 -->
          <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
            <h2 class="text-xl font-semibold dark:text-slate-50 text-slate-900 mb-6 flex items-center gap-2">
              <svg class="w-6 h-6 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              地域分布 Top 10
            </h2>
            <div v-if="stats.locationStats && stats.locationStats.length > 0" class="space-y-3">
              <div v-for="(location, index) in stats.locationStats" :key="index" class="flex items-center gap-3">
                <div class="w-8 h-8 flex-shrink-0 bg-green-500/20 dark:bg-green-500/30 rounded-lg flex items-center justify-center text-green-600 dark:text-green-400 font-bold text-sm">
                  {{ index + 1 }}
                </div>
                <div class="flex-1">
                  <div class="flex items-center justify-between mb-1">
                    <span class="text-sm dark:text-slate-300 text-slate-700 font-medium">{{ location.location || '未知' }}</span>
                    <span class="text-sm dark:text-slate-400 text-slate-600">{{ location.count }}</span>
                  </div>
                  <div class="w-full bg-slate-200 dark:bg-slate-700 rounded-full h-2">
                    <div 
                      class="bg-gradient-to-r from-green-500 to-green-400 dark:from-green-600 dark:to-green-500 h-2 rounded-full transition-all duration-500"
                      :style="{ width: getPercentage(location.count, getMaxCount(stats.locationStats)) + '%' }"
                    ></div>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8 dark:text-slate-400 text-slate-600">
              暂无地域数据
            </div>
          </div>

          <!-- 设备类型 -->
          <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
            <h2 class="text-xl font-semibold dark:text-slate-50 text-slate-900 mb-6 flex items-center gap-2">
              <svg class="w-6 h-6 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
              </svg>
              设备类型分布
            </h2>
            <div v-if="stats.deviceStats && stats.deviceStats.length > 0" class="space-y-4">
              <div v-for="(device, index) in stats.deviceStats" :key="index" class="flex items-center justify-between p-4 dark:bg-slate-800/50 bg-slate-50 rounded-lg border dark:border-slate-700 border-slate-300">
                <div class="flex items-center gap-3">
                  <svg v-if="device.deviceType === 'Mobile'" class="w-8 h-8 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
                  </svg>
                  <svg v-else-if="device.deviceType === 'Desktop'" class="w-8 h-8 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                  </svg>
                  <svg v-else class="w-8 h-8 text-orange-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                  </svg>
                  <div>
                    <p class="text-sm font-medium dark:text-slate-300 text-slate-700">{{ device.deviceType || '未知' }}</p>
                    <p class="text-xs dark:text-slate-500 text-slate-500">{{ device.count }} 次访问</p>
                  </div>
                </div>
                <div class="text-right">
                  <p class="text-lg font-bold dark:text-slate-200 text-slate-800">{{ device.percentage?.toFixed(1) }}%</p>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8 dark:text-slate-400 text-slate-600">
              暂无设备数据
            </div>
          </div>
        </div>

        <!-- 浏览器和操作系统统计 -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <!-- 浏览器统计 -->
          <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
            <h2 class="text-xl font-semibold dark:text-slate-50 text-slate-900 mb-6 flex items-center gap-2">
              <svg class="w-6 h-6 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9" />
              </svg>
              浏览器统计 Top 10
            </h2>
            <div v-if="stats.browserStats && stats.browserStats.length > 0" class="space-y-3">
              <div v-for="(browser, index) in stats.browserStats" :key="index" class="flex items-center gap-3">
                <div class="w-8 h-8 flex-shrink-0 bg-blue-500/20 dark:bg-blue-500/30 rounded-lg flex items-center justify-center text-blue-600 dark:text-blue-400 font-bold text-sm">
                  {{ index + 1 }}
                </div>
                <div class="flex-1">
                  <div class="flex items-center justify-between mb-1">
                    <span class="text-sm dark:text-slate-300 text-slate-700 font-medium">{{ browser.browser || '未知' }}</span>
                    <span class="text-sm dark:text-slate-400 text-slate-600">{{ browser.count }}</span>
                  </div>
                  <div class="w-full bg-slate-200 dark:bg-slate-700 rounded-full h-2">
                    <div 
                      class="bg-gradient-to-r from-blue-500 to-blue-400 dark:from-blue-600 dark:to-blue-500 h-2 rounded-full transition-all duration-500"
                      :style="{ width: getPercentage(browser.count, getMaxCount(stats.browserStats)) + '%' }"
                    ></div>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8 dark:text-slate-400 text-slate-600">
              暂无浏览器数据
            </div>
          </div>

          <!-- 操作系统统计 -->
          <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
            <h2 class="text-xl font-semibold dark:text-slate-50 text-slate-900 mb-6 flex items-center gap-2">
              <svg class="w-6 h-6 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
              操作系统统计 Top 10
            </h2>
            <div v-if="stats.osStats && stats.osStats.length > 0" class="space-y-3">
              <div v-for="(os, index) in stats.osStats" :key="index" class="flex items-center gap-3">
                <div class="w-8 h-8 flex-shrink-0 bg-indigo-500/20 dark:bg-indigo-500/30 rounded-lg flex items-center justify-center text-indigo-600 dark:text-indigo-400 font-bold text-sm">
                  {{ index + 1 }}
                </div>
                <div class="flex-1">
                  <div class="flex items-center justify-between mb-1">
                    <span class="text-sm dark:text-slate-300 text-slate-700 font-medium">{{ os.os || '未知' }}</span>
                    <span class="text-sm dark:text-slate-400 text-slate-600">{{ os.count }}</span>
                  </div>
                  <div class="w-full bg-slate-200 dark:bg-slate-700 rounded-full h-2">
                    <div 
                      class="bg-gradient-to-r from-indigo-500 to-indigo-400 dark:from-indigo-600 dark:to-indigo-500 h-2 rounded-full transition-all duration-500"
                      :style="{ width: getPercentage(os.count, getMaxCount(stats.osStats)) + '%' }"
                    ></div>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8 dark:text-slate-400 text-slate-600">
              暂无操作系统数据
            </div>
          </div>
        </div>

        <!-- 热门短链接 Top 10 -->
        <div class="glass-effect rounded-2xl p-6 border dark:border-slate-600 border-slate-300 shadow-lg">
          <h2 class="text-xl font-semibold dark:text-slate-50 text-slate-900 mb-6 flex items-center gap-2">
            <svg class="w-6 h-6 text-orange-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
            访问量 Top 10 短链接
          </h2>
          <div v-if="stats.topUrls && stats.topUrls.length > 0" class="overflow-x-auto">
            <table class="w-full">
              <thead class="dark:bg-slate-800/70 bg-slate-200 border-b-2 dark:border-slate-600 border-slate-300">
                <tr>
                  <th class="px-4 py-3 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">排名</th>
                  <th class="px-4 py-3 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">短码</th>
                  <th class="px-4 py-3 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">原始URL</th>
                  <th class="px-4 py-3 text-right text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">访问次数</th>
                </tr>
              </thead>
              <tbody class="divide-y dark:divide-slate-700 divide-slate-300">
                <tr v-for="(url, index) in stats.topUrls" :key="index" class="dark:hover:bg-slate-800/50 hover:bg-slate-100 transition-all">
                  <td class="px-4 py-3 whitespace-nowrap">
                    <div class="w-8 h-8 bg-orange-500/20 dark:bg-orange-500/30 rounded-lg flex items-center justify-center text-orange-600 dark:text-orange-400 font-bold text-sm">
                      {{ index + 1 }}
                    </div>
                  </td>
                  <td class="px-4 py-3 whitespace-nowrap">
                    <span class="dark:text-orange-400 text-orange-600 font-semibold font-mono">{{ url.shortCode }}</span>
                  </td>
                  <td class="px-4 py-3">
                    <div class="text-sm dark:text-slate-300 text-slate-700 max-w-md truncate" :title="url.originalUrl">
                      {{ url.originalUrl }}
                    </div>
                  </td>
                  <td class="px-4 py-3 text-right whitespace-nowrap">
                    <span class="px-3 py-1 bg-indigo-500/20 dark:bg-indigo-500/30 text-indigo-600 dark:text-indigo-400 rounded-full text-sm font-semibold">
                      {{ url.visitCount?.toLocaleString() || 0 }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-else class="text-center py-8 dark:text-slate-400 text-slate-600">
            暂无数据
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const loading = ref(false)
const error = ref('')
const stats = ref({})

onMounted(() => {
  loadStats()
})

const loadStats = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await api.getGlobalStats()
    
    if (response.code === 200) {
      stats.value = response.data
      // 填充7天趋势数据，确保每天都有数据
      stats.value.dailyTrends = fillMissingDays(stats.value.dailyTrends || [], 7)
    } else {
      error.value = response.message || '加载失败'
    }
  } catch (err) {
    if (err.response?.status === 401) {
      error.value = '请先登录'
    } else {
      error.value = '加载失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}

// 填充缺失的日期数据，确保返回完整的N天数据
const fillMissingDays = (trends, days) => {
  const result = []
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  
  // 格式化日期为本地时区的 YYYY-MM-DD 格式
  const formatLocalDate = (date) => {
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }
  
  // 创建日期到计数的映射
  const trendMap = {}
  trends.forEach(trend => {
    trendMap[trend.date] = trend.count
  })
  
  // 填充最近N天的数据（包含今天，从今天往前推6天，共7天）
  for (let i = days - 1; i >= 0; i--) {
    const date = new Date(today)
    date.setDate(date.getDate() - i)
    const dateStr = formatLocalDate(date)
    
    result.push({
      date: dateStr,
      count: trendMap[dateStr] || 0
    })
  }
  
  return result
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}/${date.getDate()}`
}

const getBarHeight = (count) => {
  if (!stats.value.dailyTrends || stats.value.dailyTrends.length === 0) return '8px'
  const maxCount = Math.max(...stats.value.dailyTrends.map(t => t.count))
  
  // 如果所有数据都是0，显示最小高度
  if (maxCount === 0) return '8px'
  
  // 如果当前值是0，显示最小高度
  if (count === 0) return '8px'
  
  // 计算百分比，最小10%，最大90%
  const percentage = (count / maxCount) * 90
  return Math.max(percentage, 10) + '%'
}

const getMaxCount = (items) => {
  if (!items || items.length === 0) return 1
  return Math.max(...items.map(item => item.count))
}

const getPercentage = (count, maxCount) => {
  if (maxCount === 0) return 0
  return Math.min((count / maxCount) * 100, 100)
}

const getTrendClass = () => {
  const today = stats.value.todayVisits || 0
  const yesterday = stats.value.yesterdayVisits || 0
  if (today > yesterday) return 'text-green-600 dark:text-green-400'
  if (today < yesterday) return 'text-red-600 dark:text-red-400'
  return 'text-slate-600 dark:text-slate-400'
}

const getTrendText = () => {
  const today = stats.value.todayVisits || 0
  const yesterday = stats.value.yesterdayVisits || 0
  if (yesterday === 0) return '—'
  const diff = today - yesterday
  const percentage = ((diff / yesterday) * 100).toFixed(1)
  if (diff > 0) return `↑ ${percentage}%`
  if (diff < 0) return `↓ ${Math.abs(percentage)}%`
  return '—'
}
</script>

