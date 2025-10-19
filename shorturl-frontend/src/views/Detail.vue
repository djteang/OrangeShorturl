<template>
  <div class="min-h-[calc(100vh-12rem)] px-4 py-12">
    <div class="max-w-6xl mx-auto">
      <!-- 返回按钮 -->
      <button
        @click="$router.back()"
        class="mb-6 flex items-center dark:text-slate-300 text-slate-700 dark:hover:text-slate-100 hover:text-slate-900 transition-all font-medium"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
        </svg>
        返回
      </button>

      <div v-if="loading" class="glass-effect rounded-2xl border dark:border-slate-600 border-slate-300 p-12 text-center shadow-lg">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-4 dark:border-slate-700 border-slate-300 border-t-orange-500"></div>
        <p class="dark:text-slate-400 text-slate-600 mt-4">加载中...</p>
      </div>

      <div v-else-if="error" class="glass-effect rounded-2xl border dark:border-slate-600 border-slate-300 p-12 text-center shadow-lg">
        <svg class="w-16 h-16 text-red-500 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
        </svg>
        <p class="dark:text-slate-400 text-slate-600">{{ error }}</p>
      </div>

      <div v-else class="space-y-6">
        <!-- 基本信息 -->
        <div class="glass-effect rounded-2xl border dark:border-slate-600 border-slate-300 p-8 shadow-lg">
          <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-6">基本信息</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-semibold dark:text-slate-400 text-slate-600 mb-2">短码</label>
              <div class="flex items-center space-x-2">
                <div class="flex-1 px-4 py-3 dark:bg-slate-800/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 font-bold text-lg border dark:border-slate-700 border-slate-300">
                  {{ detail.shortCode }}
                </div>
                <button
                  @click="copyUrl(detail.shortUrl)"
                  class="px-4 py-3 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 rounded-lg transition-all font-medium shadow-sm hover:shadow border dark:border-slate-600 border-slate-400"
                >
                  复制链接
                </button>
              </div>
            </div>

            <div>
              <label class="block text-sm font-semibold dark:text-slate-400 text-slate-600 mb-2">创建时间</label>
              <div class="px-4 py-3 dark:bg-slate-800/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 border dark:border-slate-700 border-slate-300">
                {{ formatDate(detail.createTime) }}
              </div>
            </div>

            <div class="md:col-span-2">
              <label class="block text-sm font-semibold dark:text-slate-400 text-slate-600 mb-2">原始URL</label>
              <div class="px-4 py-3 dark:bg-slate-800/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 break-all border dark:border-slate-700 border-slate-300">
                {{ detail.originalUrl }}
              </div>
            </div>

            <div>
              <label class="block text-sm font-semibold dark:text-slate-400 text-slate-600 mb-2">访问次数</label>
              <div class="px-4 py-3 bg-gradient-to-r dark:from-blue-900/40 dark:to-purple-900/40 from-blue-100 to-purple-100 rounded-lg border dark:border-blue-700 border-blue-300">
                <span class="text-3xl font-bold dark:text-orange-400 text-orange-600">{{ detail.visitCount }}</span>
                <span class="dark:text-slate-400 text-slate-600 ml-2">次</span>
              </div>
            </div>

            <div>
              <label class="block text-sm font-semibold dark:text-slate-400 text-slate-600 mb-2">最后访问时间</label>
              <div class="px-4 py-3 dark:bg-slate-800/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 border dark:border-slate-700 border-slate-300">
                {{ detail.lastVisitTime ? formatDate(detail.lastVisitTime) : '暂无访问' }}
              </div>
            </div>
          </div>
        </div>

        <!-- 访问统计 -->
        <div v-if="stats" class="glass-effect rounded-2xl border dark:border-slate-600 border-slate-300 p-8 shadow-lg">
          <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-6">访问统计</h2>
          
          <!-- 每日访问趋势 -->
          <div class="mb-8">
            <h3 class="text-lg font-semibold dark:text-slate-300 text-slate-700 mb-4">最近7天访问趋势</h3>
            <div v-if="stats.dailyStats && stats.dailyStats.length > 0" class="space-y-2">
              <div v-for="stat in stats.dailyStats" :key="stat.date" class="flex items-center">
                <div class="w-24 text-sm dark:text-slate-400 text-slate-600 font-medium">{{ stat.date }}</div>
                <div class="flex-1 flex items-center">
                  <div
                    class="bg-gradient-to-r from-orange-500 to-orange-600 h-8 rounded-r flex items-center justify-end px-3 text-white font-semibold transition-all shadow-sm"
                    :style="{width: (stat.count / maxCount * 100) + '%', minWidth: stat.count > 0 ? '60px' : '0'}"
                  >
                    {{ stat.count }}
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8 dark:text-slate-500 text-slate-500">
              暂无访问数据
            </div>
          </div>

          <!-- 最近访问记录 -->
          <div>
            <h3 class="text-lg font-semibold dark:text-slate-300 text-slate-700 mb-4">最近访问记录</h3>
            <div v-if="stats.recentVisits && stats.recentVisits.length > 0" class="space-y-3">
              <div
                v-for="(visit, index) in stats.recentVisits"
                :key="index"
                class="flex items-start p-4 dark:bg-slate-800/60 bg-slate-100 rounded-lg dark:hover:bg-slate-800/80 hover:bg-slate-200 transition-all border dark:border-slate-700 border-slate-300"
              >
                <div class="flex-shrink-0 w-10 h-10 dark:bg-slate-700 bg-orange-100 rounded-full flex items-center justify-center dark:text-orange-400 text-orange-600 font-bold mr-4 border dark:border-slate-600 border-orange-200">
                  {{ index + 1 }}
                </div>
                <div class="flex-1 min-w-0">
                  <div class="flex items-center justify-between mb-1">
                    <span class="text-sm font-semibold dark:text-slate-200 text-slate-700">{{ visit.ipAddress }}</span>
                    <span class="text-xs dark:text-slate-400 text-slate-500">{{ visit.visitTime }}</span>
                  </div>
                  <div class="text-xs dark:text-slate-400 text-slate-600 truncate" :title="visit.userAgent">
                    {{ visit.userAgent || '未知设备' }}
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8 dark:text-slate-500 text-slate-500">
              暂无访问记录
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api'
import toast from '../utils/toast'

const route = useRoute()
const shortCode = route.params.shortCode

const loading = ref(true)
const error = ref('')
const detail = ref(null)
const stats = ref(null)

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  loading.value = true
  error.value = ''

  try {
    // 获取详情
    const detailResponse = await api.getUrlDetail(shortCode)
    if (detailResponse.code === 200) {
      detail.value = detailResponse.data
    } else {
      error.value = detailResponse.message || '加载失败'
      loading.value = false
      return
    }

    // 获取统计
    const statsResponse = await api.getStats(shortCode)
    if (statsResponse.code === 200) {
      stats.value = statsResponse.data
    }
  } catch (err) {
    error.value = '加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const maxCount = computed(() => {
  if (!stats.value || !stats.value.dailyStats || stats.value.dailyStats.length === 0) {
    return 1
  }
  return Math.max(...stats.value.dailyStats.map(s => s.count))
})

const copyUrl = async (url) => {
  try {
    await navigator.clipboard.writeText(url)
    toast.success('复制成功！')
  } catch (err) {
    console.error('复制失败', err)
    toast.error('复制失败，请重试')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}
</script>

