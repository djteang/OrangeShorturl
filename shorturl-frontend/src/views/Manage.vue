<template>
  <div class="min-h-[calc(100vh-12rem)] px-4 py-12">
    <div class="max-w-7xl mx-auto">
      <!-- 标题和搜索 -->
      <div class="glass-effect rounded-2xl p-6 mb-6 shadow-lg border dark:border-slate-600 border-slate-300">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <h1 class="text-3xl font-bold dark:text-slate-50 text-slate-900 mb-2">链接管理</h1>
            <p class="dark:text-slate-400 text-slate-600">管理您创建的所有短链接</p>
          </div>
          <div class="flex items-center space-x-2">
            <input
              v-model="keyword"
              type="text"
              placeholder="搜索短码或URL..."
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

      <!-- 链接列表 -->
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"></path>
          </svg>
          <p class="dark:text-slate-400 text-slate-600">暂无数据</p>
        </div>

        <div v-else class="overflow-x-auto">
          <table class="w-full">
            <thead class="dark:bg-slate-800/70 bg-slate-200 border-b-2 dark:border-slate-600 border-slate-300">
              <tr>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">短码</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">标题</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">原始URL</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">创建时间</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">访问次数</th>
                <th class="px-6 py-4 text-left text-xs font-bold dark:text-slate-200 text-slate-800 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="divide-y dark:divide-slate-700 divide-slate-300">
              <tr v-for="item in list" :key="item.shortCode" class="dark:hover:bg-slate-800/50 hover:bg-slate-100 transition-all">
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="dark:text-orange-400 text-orange-600 font-semibold">{{ item.shortCode }}</span>
                </td>
                <td class="px-6 py-4">
                  <div class="text-sm dark:text-slate-300 text-slate-700 max-w-xs truncate" :title="item.title || '未获取到标题'">
                    {{ item.title || '未获取到标题' }}
                  </div>
                </td>
                <td class="px-6 py-4">
                  <div class="text-sm dark:text-slate-200 text-slate-800 max-w-md truncate" :title="item.originalUrl">
                    {{ item.originalUrl }}
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm dark:text-slate-400 text-slate-600">
                  {{ formatDate(item.createTime) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-3 py-1 dark:bg-blue-900/50 bg-blue-100 dark:text-blue-300 text-blue-700 text-sm font-semibold rounded-full border dark:border-blue-700 border-blue-300">
                    {{ item.visitCount }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm">
                  <div class="flex items-center space-x-2">
                    <button
                      @click="copyUrl(item.shortUrl)"
                      class="dark:text-blue-300 text-blue-600 dark:hover:text-blue-200 hover:text-blue-700 font-medium transition-all hover:underline"
                      title="复制"
                    >
                      复制
                    </button>
                    <button
                      @click="viewDetail(item.shortCode)"
                      class="dark:text-green-300 text-green-600 dark:hover:text-green-200 hover:text-green-700 font-medium transition-all hover:underline"
                      title="详情"
                    >
                      详情
                    </button>
                    <button
                      @click="deleteItem(item)"
                      class="dark:text-red-300 text-red-600 dark:hover:text-red-200 hover:text-red-700 font-medium transition-all hover:underline"
                      title="删除"
                    >
                      删除
                    </button>
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

    <!-- 删除确认对话框 -->
    <div 
      v-if="showDeleteDialog"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click.self="closeDeleteDialog">
      <div class="glass-effect rounded-2xl border-2 dark:border-slate-700 border-slate-300 p-6 max-w-md w-full shadow-2xl">
        <div class="text-center">
          <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100 dark:bg-red-900/30 mb-4">
            <svg class="h-6 w-6 text-red-600 dark:text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
            </svg>
          </div>
          <h3 class="text-lg font-bold dark:text-slate-50 text-slate-900 mb-2">确认删除</h3>
          <p class="dark:text-slate-300 text-slate-600 mb-1">确定要删除这个短链接吗？</p>
          <p class="text-sm dark:text-slate-400 text-slate-500 mb-2">
            短码：<span class="font-semibold text-orange-500">{{ itemToDelete?.shortCode }}</span>
          </p>
          <p class="text-xs dark:text-slate-500 text-slate-500 mb-6 truncate px-4" :title="itemToDelete?.originalUrl">
            {{ itemToDelete?.originalUrl }}
          </p>
        </div>
        
        <div class="flex space-x-3">
          <button 
            @click="closeDeleteDialog"
            class="flex-1 px-4 py-2 dark:bg-slate-700 bg-slate-200 dark:text-slate-50 text-slate-900 rounded-lg font-medium hover:opacity-80 transition-all">
            取消
          </button>
          <button 
            @click="confirmDelete"
            class="flex-1 px-4 py-2 bg-red-600 hover:bg-red-500 text-white rounded-lg font-medium transition-all">
            删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'
import toast from '../utils/toast'

const router = useRouter()

const list = ref([])
const loading = ref(false)
const error = ref('')
const keyword = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const showDeleteDialog = ref(false)
const itemToDelete = ref(null)

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await api.getUrlList({
      page: page.value,
      size: size.value,
      keyword: keyword.value
    })
    
    if (response.code === 200) {
      list.value = response.data.list
      total.value = response.data.total
    } else {
      error.value = response.message || '加载失败'
    }
  } catch (err) {
    error.value = '加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const changePage = (newPage) => {
  page.value = newPage
  loadData()
}

const copyUrl = async (url) => {
  try {
    await navigator.clipboard.writeText(url)
    toast.success('复制成功！')
  } catch (err) {
    console.error('复制失败', err)
    toast.error('复制失败，请重试')
  }
}

const viewDetail = (shortCode) => {
  router.push(`/detail/${shortCode}`)
}

const deleteItem = (item) => {
  itemToDelete.value = item
  showDeleteDialog.value = true
}

const confirmDelete = async () => {
  if (!itemToDelete.value) return
  
  try {
    const response = await api.deleteUrl(itemToDelete.value.shortCode)
    if (response.code === 200) {
      toast.success('删除成功！')
      closeDeleteDialog()
      loadData()
    } else {
      toast.error(response.message || '删除失败')
    }
  } catch (err) {
    toast.error('删除失败，请稍后重试')
  }
}

const closeDeleteDialog = () => {
  showDeleteDialog.value = false
  itemToDelete.value = null
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}
</script>

