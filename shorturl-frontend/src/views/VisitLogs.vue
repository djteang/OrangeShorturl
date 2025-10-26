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
                <th class="px-6 py-4 text-center text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">操作</th>
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
                <td class="px-6 py-4 text-center">
                  <button
                    @click="showDetail(item)"
                    class="px-3 py-1 bg-blue-600 hover:bg-blue-500 dark:bg-blue-700 dark:hover:bg-blue-600 text-white text-sm rounded-lg transition-all shadow-sm hover:shadow-md"
                  >
                    详情
                  </button>
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

    <!-- 详情模态框 -->
    <div
      v-if="showDetailModal"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm"
    >
      <div class="glass-effect rounded-2xl shadow-2xl max-w-3xl w-full max-h-[90vh] overflow-y-auto border dark:border-slate-600 border-slate-300">
        <!-- 标题栏 -->
        <div class="sticky top-0 dark:bg-slate-800/95 bg-white/95 backdrop-blur-md px-6 py-4 border-b dark:border-slate-600 border-slate-300 flex items-center justify-between">
          <div class="flex items-center gap-3">
            <svg class="w-6 h-6 text-blue-500 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900">访问详情</h2>
          </div>
          <button
            @click="closeDetail"
            class="text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 transition-colors"
          >
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <!-- 详情内容 -->
        <div v-if="selectedLog" class="p-6 space-y-6">
          <!-- 基本信息 -->
          <div class="dark:bg-slate-800/50 bg-slate-50 rounded-lg p-4 border dark:border-slate-700 border-slate-300">
            <h3 class="text-lg font-semibold dark:text-slate-200 text-slate-800 mb-4 flex items-center gap-2">
              <svg class="w-5 h-5 text-orange-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              基本信息
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">短码</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 font-mono bg-orange-500/10 dark:bg-orange-500/20 px-3 py-2 rounded border dark:border-orange-500/30 border-orange-500/50 mt-1">
                  {{ selectedLog.shortCode || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">访问时间</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ formatDate(selectedLog.visitTime) }}
                </p>
              </div>
            </div>
          </div>

          <!-- 网络信息 -->
          <div class="dark:bg-slate-800/50 bg-slate-50 rounded-lg p-4 border dark:border-slate-700 border-slate-300">
            <h3 class="text-lg font-semibold dark:text-slate-200 text-slate-800 mb-4 flex items-center gap-2">
              <svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9" />
              </svg>
              网络信息
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">IP地址</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 font-mono dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.ipAddress || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">运营商</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.isp || '-' }}
                </p>
              </div>
              <div class="md:col-span-2">
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">来源页面</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1 break-all">
                  {{ selectedLog.referer || '直接访问' }}
                </p>
              </div>
            </div>
          </div>

          <!-- 地理位置 -->
          <div class="dark:bg-slate-800/50 bg-slate-50 rounded-lg p-4 border dark:border-slate-700 border-slate-300">
            <h3 class="text-lg font-semibold dark:text-slate-200 text-slate-800 mb-4 flex items-center gap-2">
              <svg class="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              地理位置
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">国家</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.country || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">省份</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.province || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">城市</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.city || '-' }}
                </p>
              </div>
            </div>
          </div>

          <!-- 设备信息 -->
          <div class="dark:bg-slate-800/50 bg-slate-50 rounded-lg p-4 border dark:border-slate-700 border-slate-300">
            <h3 class="text-lg font-semibold dark:text-slate-200 text-slate-800 mb-4 flex items-center gap-2">
              <svg class="w-5 h-5 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
              </svg>
              设备信息
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">设备类型</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.deviceType || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">设备品牌</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.deviceBrand || '-' }}
                </p>
              </div>
              <div class="md:col-span-2">
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">设备型号</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.deviceModel || '-' }}
                </p>
              </div>
            </div>
          </div>

          <!-- 浏览器和系统 -->
          <div class="dark:bg-slate-800/50 bg-slate-50 rounded-lg p-4 border dark:border-slate-700 border-slate-300">
            <h3 class="text-lg font-semibold dark:text-slate-200 text-slate-800 mb-4 flex items-center gap-2">
              <svg class="w-5 h-5 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
              浏览器和系统
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">浏览器</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.browser || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">浏览器版本</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.browserVersion || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">操作系统</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.os || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">系统版本</label>
                <p class="text-sm dark:text-slate-200 text-slate-800 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1">
                  {{ selectedLog.osVersion || '-' }}
                </p>
              </div>
              <div class="md:col-span-2">
                <label class="text-xs font-medium dark:text-slate-400 text-slate-600">完整User-Agent</label>
                <p class="text-xs dark:text-slate-300 text-slate-700 dark:bg-slate-700 bg-white px-3 py-2 rounded border dark:border-slate-600 border-slate-300 mt-1 font-mono break-all">
                  {{ selectedLog.userAgent || '-' }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- 底部按钮 -->
        <div class="sticky bottom-0 dark:bg-slate-800/95 bg-white/95 backdrop-blur-md px-6 py-4 border-t dark:border-slate-600 border-slate-300 flex justify-end">
          <button
            @click="closeDetail"
            class="px-6 py-2 bg-slate-600 hover:bg-slate-500 dark:bg-slate-700 dark:hover:bg-slate-600 text-white rounded-lg transition-all shadow-sm hover:shadow-md"
          >
            关闭
          </button>
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
const showDetailModal = ref(false)
const selectedLog = ref(null)

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

const showDetail = (log) => {
  selectedLog.value = log
  showDetailModal.value = true
}

const closeDetail = () => {
  showDetailModal.value = false
  setTimeout(() => {
    selectedLog.value = null
  }, 300)
}
</script>

