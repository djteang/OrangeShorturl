<template>
  <div class="min-h-[calc(100vh-12rem)] px-4 py-8">
    <div class="max-w-6xl mx-auto">
      <!-- 头部 -->
      <div class="flex justify-between items-center mb-8">
        <h1 class="text-3xl font-bold dark:text-slate-50 text-slate-900">链接分组管理</h1>
        <button
          @click="showCreateModal = true"
          class="px-6 py-2 bg-orange-600 hover:bg-orange-500 text-white rounded-lg font-medium transition-all shadow-lg hover:shadow-xl"
        >
          + 创建分组
        </button>
      </div>

      <!-- 分组列表 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="group in groups"
          :key="group.id"
          class="glass-effect rounded-xl p-6 shadow-lg border dark:border-slate-600 border-slate-300 hover:shadow-xl transition-all"
        >
          <div class="flex items-start justify-between mb-4">
            <div class="flex items-center space-x-3">
              <div
                class="w-12 h-12 rounded-lg flex items-center justify-center text-white font-bold text-xl"
                :style="{ backgroundColor: group.color }"
              >
                {{ group.icon || group.name.charAt(0) }}
              </div>
              <div>
                <h3 class="text-lg font-bold dark:text-slate-50 text-slate-900">{{ group.name }}</h3>
                <p class="text-sm dark:text-slate-400 text-slate-600">{{ group.urlCount }} 个链接</p>
              </div>
            </div>
            <div class="flex items-center space-x-2">
              <button
                @click="editGroup(group)"
                class="p-2 dark:hover:bg-slate-700 hover:bg-slate-200 rounded-lg transition-all"
                title="编辑"
              >
                <svg class="w-5 h-5 dark:text-slate-400 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                </svg>
              </button>
              <button
                @click="deleteGroup(group)"
                class="p-2 dark:hover:bg-red-900/50 hover:bg-red-100 rounded-lg transition-all"
                title="删除"
              >
                <svg class="w-5 h-5 dark:text-red-400 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                </svg>
              </button>
            </div>
          </div>
          <p class="text-sm dark:text-slate-400 text-slate-600 mb-4">{{ group.description || '暂无描述' }}</p>
          <div class="text-xs dark:text-slate-500 text-slate-500">
            创建于 {{ formatDate(group.createTime) }}
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="groups.length === 0" class="text-center py-20">
        <svg class="w-16 h-16 mx-auto mb-4 dark:text-slate-600 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 13h6m-3-3v6m5 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
        </svg>
        <p class="text-lg dark:text-slate-400 text-slate-600">暂无分组</p>
        <p class="text-sm dark:text-slate-500 text-slate-500 mt-2">点击上方按钮创建你的第一个分组</p>
      </div>
    </div>

    <!-- 创建/编辑分组弹窗 -->
    <div v-if="showCreateModal || showEditModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="glass-effect rounded-2xl p-8 max-w-md w-full shadow-2xl border dark:border-slate-600 border-slate-300">
        <h2 class="text-2xl font-bold mb-6 dark:text-slate-50 text-slate-900">
          {{ showEditModal ? '编辑分组' : '创建分组' }}
        </h2>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">分组名称</label>
            <input
              v-model="formData.name"
              type="text"
              placeholder="输入分组名称"
              class="w-full px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none"
            />
          </div>
          <div>
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">分组描述</label>
            <textarea
              v-model="formData.description"
              placeholder="输入分组描述（可选）"
              rows="3"
              class="w-full px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none resize-none"
            ></textarea>
          </div>
          <div>
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">分组颜色</label>
            <input
              v-model="formData.color"
              type="color"
              class="w-full h-12 rounded-lg border-2 dark:border-slate-600 border-slate-300 cursor-pointer"
            />
          </div>
          <div>
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">图标（可选）</label>
            <input
              v-model="formData.icon"
              type="text"
              placeholder="输入图标字符或emoji"
              maxlength="2"
              class="w-full px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none"
            />
          </div>
        </div>
        <div class="flex space-x-4 mt-8">
          <button
            @click="closeModal"
            class="flex-1 px-6 py-3 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 rounded-lg font-medium transition-all"
          >
            取消
          </button>
          <button
            @click="handleSubmit"
            :disabled="!formData.name"
            class="flex-1 px-6 py-3 bg-orange-600 hover:bg-orange-500 text-white rounded-lg font-medium transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ showEditModal ? '保存' : '创建' }}
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

const groups = ref([])
const showCreateModal = ref(false)
const showEditModal = ref(false)
const formData = ref({
  name: '',
  description: '',
  icon: '',
  color: '#3b82f6',
  sortOrder: 0
})
const editingGroupId = ref(null)

const loadGroups = async () => {
  try {
    const response = await api.getUrlGroups()
    if (response.code === 200) {
      groups.value = response.data || []
    }
  } catch (err) {
    toast.error('加载分组失败')
  }
}

const editGroup = (group) => {
  formData.value = {
    name: group.name,
    description: group.description,
    icon: group.icon,
    color: group.color,
    sortOrder: group.sortOrder
  }
  editingGroupId.value = group.id
  showEditModal.value = true
}

const deleteGroup = async (group) => {
  if (group.urlCount > 0) {
    toast.error('该分组下还有链接，无法删除')
    return
  }

  if (!confirm(`确定要删除分组"${group.name}"吗？`)) {
    return
  }

  try {
    const response = await api.deleteUrlGroup(group.id)
    if (response.code === 200) {
      toast.success('删除成功')
      loadGroups()
    } else {
      toast.error(response.message || '删除失败')
    }
  } catch (err) {
    toast.error('删除失败')
  }
}

const handleSubmit = async () => {
  if (!formData.value.name) {
    toast.error('请输入分组名称')
    return
  }

  try {
    let response
    if (showEditModal.value) {
      response = await api.updateUrlGroup(editingGroupId.value, formData.value)
    } else {
      response = await api.createUrlGroup(formData.value)
    }

    if (response.code === 200) {
      toast.success(showEditModal.value ? '更新成功' : '创建成功')
      closeModal()
      loadGroups()
    } else {
      toast.error(response.message || '操作失败')
    }
  } catch (err) {
    toast.error(err.response?.data?.message || '操作失败')
  }
}

const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  formData.value = {
    name: '',
    description: '',
    icon: '',
    color: '#3b82f6',
    sortOrder: 0
  }
  editingGroupId.value = null
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadGroups()
})
</script>

