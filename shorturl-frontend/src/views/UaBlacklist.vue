<template>
  <div class="min-h-screen py-8 px-4">
    <div class="max-w-6xl mx-auto">
      <!-- 标题区 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold dark:text-slate-50 text-slate-900 mb-2">UA屏蔽管理</h1>
        <p class="dark:text-slate-400 text-slate-600">管理User-Agent黑名单，阻止恶意访问</p>
      </div>

      <!-- 添加按钮 -->
      <div class="mb-6">
        <button 
          @click="showAddDialog = true"
          class="px-6 py-3 bg-orange-600 hover:bg-orange-500 text-white rounded-lg font-medium transition-all shadow-sm hover:shadow-md flex items-center space-x-2">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
          </svg>
          <span>添加黑名单</span>
        </button>
      </div>

      <!-- 黑名单列表 -->
      <div v-if="loading" class="text-center py-12">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-4 border-orange-500 border-t-transparent"></div>
        <p class="mt-4 dark:text-slate-400 text-slate-600">加载中...</p>
      </div>

      <div v-else-if="blacklist.length === 0" class="text-center py-12 glass-effect rounded-2xl border-2 dark:border-slate-700 border-slate-300">
        <svg class="w-16 h-16 mx-auto dark:text-slate-600 text-slate-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"></path>
        </svg>
        <p class="dark:text-slate-400 text-slate-600 text-lg">暂无黑名单规则</p>
      </div>

      <div v-else class="space-y-4">
        <div 
          v-for="item in blacklist" 
          :key="item.id"
          class="glass-effect rounded-2xl border-2 dark:border-slate-700 border-slate-300 p-6 hover:shadow-lg transition-all">
          <div class="flex items-start justify-between">
            <div class="flex-1">
              <div class="flex items-center space-x-3 mb-2">
                <h3 class="text-lg font-bold dark:text-slate-50 text-slate-900">{{ item.uaPattern }}</h3>
                <span 
                  :class="item.status === 1 ? 'bg-green-100 dark:bg-green-900/30 text-green-600 dark:text-green-400' : 'bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-400'"
                  class="px-2 py-1 rounded text-xs font-medium">
                  {{ item.status === 1 ? '启用' : '禁用' }}
                </span>
              </div>
              <p class="dark:text-slate-400 text-slate-600 mb-2">{{ item.description || '暂无描述' }}</p>
              <p class="text-xs dark:text-slate-500 text-slate-500">
                创建时间：{{ formatDate(item.createTime) }}
              </p>
            </div>
            
            <div class="flex items-center space-x-2 ml-4">
              <button 
                @click="toggleStatus(item)"
                class="p-2 dark:hover:bg-slate-700 hover:bg-slate-200 rounded-lg transition-all"
                :title="item.status === 1 ? '禁用' : '启用'">
                <svg v-if="item.status === 1" class="w-5 h-5 text-orange-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 9v6m4-6v6m7-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
                <svg v-else class="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z"></path>
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
              </button>
              <button 
                @click="editItem(item)"
                class="p-2 dark:hover:bg-slate-700 hover:bg-slate-200 rounded-lg transition-all"
                title="编辑">
                <svg class="w-5 h-5 dark:text-slate-400 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                </svg>
              </button>
              <button 
                @click="deleteItem(item)"
                class="p-2 dark:hover:bg-red-900/20 hover:bg-red-50 rounded-lg transition-all"
                title="删除">
                <svg class="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <div 
      v-if="showAddDialog || showEditDialog"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click.self="closeDialogs">
      <div class="glass-effect rounded-2xl border-2 dark:border-slate-700 border-slate-300 p-6 max-w-lg w-full shadow-2xl">
        <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-6">
          {{ showAddDialog ? '添加黑名单' : '编辑黑名单' }}
        </h2>
        
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium dark:text-slate-300 text-slate-700 mb-2">
              UA匹配模式 <span class="text-red-500">*</span>
            </label>
            <input 
              v-model="formData.uaPattern"
              type="text"
              placeholder="例如：bot、crawler、spider"
              class="w-full px-4 py-2 dark:bg-slate-800 bg-white dark:text-slate-50 text-slate-900 border-2 dark:border-slate-700 border-slate-300 rounded-lg focus:outline-none focus:border-orange-500 transition-all"
            />
            <p class="text-xs dark:text-slate-500 text-slate-500 mt-1">输入要匹配的UA关键词（不区分大小写）</p>
          </div>
          
          <div>
            <label class="block text-sm font-medium dark:text-slate-300 text-slate-700 mb-2">
              描述
            </label>
            <textarea 
              v-model="formData.description"
              rows="3"
              placeholder="说明此规则的用途"
              class="w-full px-4 py-2 dark:bg-slate-800 bg-white dark:text-slate-50 text-slate-900 border-2 dark:border-slate-700 border-slate-300 rounded-lg focus:outline-none focus:border-orange-500 transition-all resize-none"
            ></textarea>
          </div>
          
          <div>
            <label class="flex items-center space-x-2 cursor-pointer">
              <input 
                v-model="formData.status"
                type="checkbox"
                :true-value="1"
                :false-value="0"
                class="w-5 h-5 text-orange-600 rounded focus:ring-orange-500"
              />
              <span class="text-sm font-medium dark:text-slate-300 text-slate-700">启用此规则</span>
            </label>
          </div>
        </div>
        
        <div class="flex space-x-3 mt-6">
          <button 
            @click="saveItem"
            :disabled="!formData.uaPattern"
            class="flex-1 px-4 py-2 bg-orange-600 hover:bg-orange-500 text-white rounded-lg font-medium transition-all disabled:opacity-50 disabled:cursor-not-allowed">
            保存
          </button>
          <button 
            @click="closeDialogs"
            class="flex-1 px-4 py-2 dark:bg-slate-700 bg-slate-200 dark:text-slate-50 text-slate-900 rounded-lg font-medium hover:opacity-80 transition-all">
            取消
          </button>
        </div>
      </div>
    </div>

    <!-- 删除确认对话框 -->
    <div 
      v-if="showDeleteDialog"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click.self="closeDialogs">
      <div class="glass-effect rounded-2xl border-2 dark:border-slate-700 border-slate-300 p-6 max-w-md w-full shadow-2xl">
        <div class="text-center">
          <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100 dark:bg-red-900/30 mb-4">
            <svg class="h-6 w-6 text-red-600 dark:text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
            </svg>
          </div>
          <h3 class="text-lg font-bold dark:text-slate-50 text-slate-900 mb-2">确认删除</h3>
          <p class="dark:text-slate-300 text-slate-600 mb-1">确定要删除这条黑名单规则吗？</p>
          <p class="text-sm dark:text-slate-400 text-slate-500 mb-6">
            规则：<span class="font-semibold">{{ itemToDelete?.uaPattern }}</span>
          </p>
        </div>
        
        <div class="flex space-x-3">
          <button 
            @click="closeDialogs"
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
import api from '../api'
import toast from '../utils/toast'

const loading = ref(true)
const blacklist = ref([])
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)
const itemToDelete = ref(null)
const formData = ref({
  id: null,
  uaPattern: '',
  description: '',
  status: 1
})

// 加载黑名单列表
const loadBlacklist = async () => {
  try {
    loading.value = true
    const response = await api.getUaBlacklist()
    if (response.code === 200) {
      blacklist.value = response.data
    } else {
      toast.error(response.message || '加载失败')
    }
  } catch (error) {
    toast.error('加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 添加/编辑黑名单
const saveItem = async () => {
  if (!formData.value.uaPattern) {
    toast.error('请输入UA匹配模式')
    return
  }
  
  try {
    let response
    if (showEditDialog.value) {
      // 编辑
      response = await api.updateUaBlacklist(formData.value.id, {
        uaPattern: formData.value.uaPattern,
        description: formData.value.description,
        status: formData.value.status
      })
    } else {
      // 添加
      response = await api.addUaBlacklist({
        uaPattern: formData.value.uaPattern,
        description: formData.value.description,
        status: formData.value.status
      })
    }
    
    if (response.code === 200) {
      toast.success(showEditDialog.value ? '更新成功' : '添加成功')
      closeDialogs()
      loadBlacklist()
    } else {
      toast.error(response.message || '操作失败')
    }
  } catch (error) {
    toast.error('操作失败，请稍后重试')
  }
}

// 编辑
const editItem = (item) => {
  formData.value = {
    id: item.id,
    uaPattern: item.uaPattern,
    description: item.description,
    status: item.status
  }
  showEditDialog.value = true
}

// 切换状态
const toggleStatus = async (item) => {
  try {
    const response = await api.toggleUaBlacklist(item.id)
    if (response.code === 200) {
      toast.success('状态已更新')
      loadBlacklist()
    } else {
      toast.error(response.message || '操作失败')
    }
  } catch (error) {
    toast.error('操作失败，请稍后重试')
  }
}

// 显示删除确认对话框
const deleteItem = (item) => {
  itemToDelete.value = item
  showDeleteDialog.value = true
}

// 确认删除
const confirmDelete = async () => {
  if (!itemToDelete.value) return
  
  try {
    const response = await api.deleteUaBlacklist(itemToDelete.value.id)
    if (response.code === 200) {
      toast.success('删除成功')
      showDeleteDialog.value = false
      itemToDelete.value = null
      loadBlacklist()
    } else {
      toast.error(response.message || '删除失败')
    }
  } catch (error) {
    toast.error('删除失败，请稍后重试')
  }
}

// 关闭对话框
const closeDialogs = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  showDeleteDialog.value = false
  itemToDelete.value = null
  formData.value = {
    id: null,
    uaPattern: '',
    description: '',
    status: 1
  }
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return d.toLocaleString('zh-CN')
}

onMounted(() => {
  loadBlacklist()
})
</script>

