<template>
  <div class="min-h-[calc(100vh-12rem)] px-4 py-12">
    <div class="max-w-7xl mx-auto">
      <!-- 标题和搜索 -->
      <div class="glass-effect rounded-2xl p-6 mb-6 shadow-lg border dark:border-slate-600 border-slate-300">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <h1 class="text-3xl font-bold dark:text-slate-50 text-slate-900 mb-2">访问日志</h1>
            <p class="dark:text-slate-400 text-slate-600">查看所有短链接的访问记录</p>
          </div>
          <div class="flex items-center space-x-2">
            <input
              v-model="shortCode"
              type="text"
              placeholder="按短码筛选..."
              class="px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all dark:placeholder-slate-500 placeholder-slate-400"
              @keyup.enter="loadData"
            />
            <button
              @click="loadData"
              class="px-6 py-2 bg-orange-600 hover:bg-orange-500 text-white rounded-lg transition-all shadow-sm hover:shadow-md border border-orange-600"
            >
              搜索
            </button>
          </div>
        </div>
      </div>

      <!-- 访问日志列表 -->
      <div class="glass-effect rounded-2xl overflow-hidden shadow-lg border dark:border-slate-600 border-slate-300">
        <div v-if="loading" class="text-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-4 dark:border-slate-700 border-slate-300 border-t-orange-500"></div>
          <p class="dark:text-slate-400 text-slate-600 mt-4">加载中...</p>
        </div>

        <div v-else-if="error" class="text-center py-12">
          <svg class="w-16 h-16 text-red-500 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
          </svg>
          <p class="dark:text-slate-400 text-slate-600">{{ error }}</p>
        </div>

        <div v-else-if="list.length === 0" class="text-center py-12">
          <svg class="w-16 h-16 dark:text-slate-500 text-slate-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
          </svg>
          <p class="dark:text-slate-400 text-slate-600">暂无访问记录</p>
        </div>

        <div v-else class="overflow-x-auto">
          <table class="w-full">
            <thead class="dark:bg-slate-800/70 bg-slate-200 border-b-2 dark:border-slate-600 border-slate-300">
              <tr>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">短码</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">访问时间</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">IP地址</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">浏览器</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">来源</th>
              </tr>
            </thead>
            <tbody class="divide-y dark:divide-slate-700 divide-slate-300">
              <tr v-for="item in list" :key="item.id" class="dark:hover:bg-slate-800/50 hover:bg-slate-100 transition-all">
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="dark:text-orange-400 text-orange-600 font-semibold">{{ item.shortCode }}</span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm dark:text-slate-300 text-slate-700">
                  {{ formatDate(item.visitTime) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-2 py-1 dark:bg-slate-700 bg-slate-200 dark:text-slate-300 text-slate-700 text-xs font-mono rounded border dark:border-slate-600 border-slate-300">
                    {{ item.ipAddress || '-' }}
                  </span>
                </td>
                <td class="px-6 py-4">
                  <div class="text-xs dark:text-slate-400 text-slate-600 max-w-xs truncate" :title="item.userAgent">
                    {{ formatUserAgent(item.userAgent) }}
                  </div>
                </td>
                <td class="px-6 py-4">
                  <div class="text-xs dark:text-slate-400 text-slate-600 max-w-xs truncate" :title="item.referer">
                    {{ item.referer || '直接访问' }}
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="px-6 py-4 dark:bg-slate-800/50 bg-slate-100 border-t dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between">
            <div class="text-sm dark:text-slate-300 text-slate-700 font-medium">
              共 {{ total }} 条记录，第 {{ page }} / {{ Math.ceil(total / size) }} 页
            </div>
            <div class="flex space-x-2">
              <button
                @click="changePage(page - 1)"
                :disabled="page === 1"
                class="px-4 py-2 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 border dark:border-slate-600 border-slate-400 dark:text-slate-200 text-slate-800 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed transition-all font-medium shadow-sm hover:shadow"
              >
                上一页
              </button>
              <button
                @click="changePage(page + 1)"
                :disabled="page >= Math.ceil(total / size)"
                class="px-4 py-2 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 border dark:border-slate-600 border-slate-400 dark:text-slate-200 text-slate-800 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed transition-all font-medium shadow-sm hover:shadow"
              >
                下一页
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const list = ref([])
const loading = ref(false)
const error = ref('')
const shortCode = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await api.getVisitLogs({
      page: page.value,
      size: size.value,
      shortCode: shortCode.value || null
    })
    
    if (response.code === 200) {
      list.value = response.data.list
      total.value = response.data.total
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

const changePage = (newPage) => {
  page.value = newPage
  loadData()
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const formatUserAgent = (ua) => {
  if (!ua) return '-'
  
  // 简化User-Agent显示
  if (ua.includes('Chrome') && !ua.includes('Edge')) {
    return 'Chrome'
  } else if (ua.includes('Firefox')) {
    return 'Firefox'
  } else if (ua.includes('Safari') && !ua.includes('Chrome')) {
    return 'Safari'
  } else if (ua.includes('Edge')) {
    return 'Edge'
  } else if (ua.includes('MSIE') || ua.includes('Trident')) {
    return 'Internet Explorer'
  } else {
    // 截取前50个字符
    return ua.substring(0, 50) + (ua.length > 50 ? '...' : '')
  }
}
</script>

