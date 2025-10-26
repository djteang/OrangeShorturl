<template>
  <div class="min-h-[calc(100vh-12rem)] py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-4xl mx-auto">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-slate-50 mb-2">自定义中转页</h1>
        <p class="text-gray-600 dark:text-slate-400">配置您的短链接中转页面，提升用户体验</p>
      </div>

      <!-- 配置表单 -->
      <div class="bg-white dark:bg-slate-800 rounded-lg shadow-lg p-6 mb-8">
        <form @submit.prevent="saveConfig">
          <!-- 基本设置 -->
          <div class="mb-6">
            <h2 class="text-xl font-semibold text-gray-800 dark:text-slate-200 mb-4 flex items-center">
              <svg class="w-6 h-6 mr-2 text-blue-500 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              基本设置
            </h2>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                  中转页标题 <span class="text-red-500">*</span>
                </label>
                <input
                  v-model="form.title"
                  type="text"
                  required
                  maxlength="100"
                  placeholder="例如：链接跳转中..."
                  class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                  等待时间（秒） <span class="text-red-500">*</span>
                </label>
                <input
                  v-model.number="form.waitTime"
                  type="number"
                  required
                  min="0"
                  max="60"
                  placeholder="3"
                  class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
            </div>

            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                提示内容 <span class="text-red-500">*</span>
              </label>
              <textarea
                v-model="form.content"
                required
                maxlength="500"
                rows="3"
                placeholder="例如：您即将离开本站，请注意安全！"
                class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
              ></textarea>
            </div>
          </div>

          <!-- Powered By 设置 -->
          <div class="mb-6">
            <h2 class="text-xl font-semibold text-gray-800 dark:text-slate-200 mb-4 flex items-center">
              <svg class="w-6 h-6 mr-2 text-indigo-500 dark:text-indigo-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
              品牌设置
            </h2>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                  Powered by 名称
                </label>
                <input
                  v-model="form.poweredByName"
                  type="text"
                  maxlength="50"
                  placeholder="例如：Orange ShortUrl"
                  class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                  品牌图标
                </label>
                <div class="flex items-center gap-2">
                  <input
                    type="file"
                    ref="iconInput"
                    @change="handleIconUpload"
                    accept="image/*"
                    class="hidden"
                  />
                  <button
                    type="button"
                    @click="$refs.iconInput.click()"
                    class="flex-1 px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg hover:bg-gray-50 dark:hover:bg-slate-600 transition-colors flex items-center justify-center gap-2"
                  >
                    <svg class="w-5 h-5 text-gray-600 dark:text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    <span class="text-sm">{{ form.poweredByIcon ? '重新上传' : '上传图标' }}</span>
                  </button>
                  <button
                    v-if="form.poweredByIcon"
                    type="button"
                    @click="form.poweredByIcon = null"
                    class="px-4 py-2 border border-red-300 dark:border-red-700 text-red-600 dark:text-red-400 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
                  >
                    清除
                  </button>
                </div>
                <div v-if="form.poweredByIcon" class="mt-2 flex items-center gap-2">
                  <img :src="form.poweredByIcon" alt="图标预览" class="w-8 h-8 rounded object-cover border border-gray-300 dark:border-slate-600">
                  <span class="text-xs text-gray-500 dark:text-slate-400">已上传</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 颜色设置 -->
          <div class="mb-6">
            <h2 class="text-xl font-semibold text-gray-800 dark:text-slate-200 mb-4 flex items-center">
              <svg class="w-6 h-6 mr-2 text-purple-500 dark:text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01" />
              </svg>
              颜色配置
            </h2>
            
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">背景颜色</label>
                <div class="flex items-center gap-2">
                  <input
                    v-model="form.backgroundColor"
                    type="color"
                    class="w-12 h-10 rounded cursor-pointer dark:bg-slate-700"
                  />
                  <input
                    v-model="form.backgroundColor"
                    type="text"
                    class="flex-1 px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">文字颜色</label>
                <div class="flex items-center gap-2">
                  <input
                    v-model="form.textColor"
                    type="color"
                    class="w-12 h-10 rounded cursor-pointer dark:bg-slate-700"
                  />
                  <input
                    v-model="form.textColor"
                    type="text"
                    class="flex-1 px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">按钮颜色</label>
                <div class="flex items-center gap-2">
                  <input
                    v-model="form.buttonColor"
                    type="color"
                    class="w-12 h-10 rounded cursor-pointer dark:bg-slate-700"
                  />
                  <input
                    v-model="form.buttonColor"
                    type="text"
                    class="flex-1 px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
              </div>
            </div>
          </div>

          <!-- 状态开关 -->
          <div class="mb-6">
            <label class="flex items-center cursor-pointer">
              <input
                v-model="form.status"
                type="checkbox"
                :true-value="1"
                :false-value="0"
                class="sr-only peer"
              />
              <div class="relative w-11 h-6 bg-gray-200 dark:bg-slate-600 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 dark:after:border-slate-500 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
              <span class="ml-3 text-sm font-medium text-gray-700 dark:text-slate-300">启用中转页</span>
            </label>
          </div>

          <!-- 按钮组 -->
          <div class="flex justify-end gap-4">
            <button
              v-if="hasConfig"
              type="button"
              @click="showDeleteDialog = true"
              class="px-6 py-2 border border-red-500 dark:border-red-700 text-red-500 dark:text-red-400 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
            >
              删除配置
            </button>
            <button
              type="submit"
              :disabled="loading"
              class="px-6 py-2 bg-blue-600 hover:bg-blue-700 dark:bg-blue-700 dark:hover:bg-blue-600 text-white rounded-lg transition-colors disabled:opacity-50"
            >
              {{ loading ? '保存中...' : '保存配置' }}
            </button>
          </div>
        </form>
      </div>

      <!-- 预览区域 -->
      <div class="bg-white dark:bg-slate-800 rounded-lg shadow-lg p-6">
        <h2 class="text-xl font-semibold text-gray-800 dark:text-slate-200 mb-4 flex items-center">
          <svg class="w-6 h-6 mr-2 text-green-500 dark:text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
          </svg>
          实时预览
        </h2>
        
        <div 
          class="min-h-[400px] rounded-lg flex items-center justify-center p-8"
          :style="{ backgroundColor: form.backgroundColor }"
        >
          <div class="text-center max-w-md">
            <div class="mb-8">
              <svg class="w-20 h-20 mx-auto mb-4 animate-spin" :style="{ color: form.buttonColor }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              <h1 class="text-3xl font-bold mb-4" :style="{ color: form.textColor }">
                {{ form.title || '链接跳转中...' }}
              </h1>
              <p class="text-lg mb-6" :style="{ color: form.textColor }">
                {{ form.content || '您即将离开本站，请注意安全！' }}
              </p>
              <div class="mb-6" :style="{ color: form.textColor }">
                <span class="text-4xl font-bold">{{ form.waitTime || 3 }}</span>
                <span class="text-xl ml-2">秒后自动跳转</span>
              </div>
            </div>
            <button
              class="px-8 py-3 rounded-lg text-white font-semibold shadow-lg hover:shadow-xl transition-all transform hover:scale-105"
              :style="{ backgroundColor: form.buttonColor }"
            >
              立即跳转
            </button>
            
            <!-- Powered By 预览 -->
            <div class="mt-8 pt-6 border-t" :style="{ borderColor: form.textColor + '30' }">
              <div class="flex items-center justify-center gap-2" :style="{ color: form.textColor, opacity: 0.7 }">
                <span class="text-xs">Powered by</span>
                <img 
                  v-if="form.poweredByIcon" 
                  :src="form.poweredByIcon" 
                  alt="icon"
                  class="w-4 h-4 rounded object-cover"
                />
                <span class="text-xs font-medium">{{ form.poweredByName || 'Orange ShortUrl' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 删除确认对话框 -->
    <div v-if="showDeleteDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-slate-800 rounded-lg p-6 max-w-md w-full mx-4">
        <h3 class="text-lg font-semibold text-gray-900 dark:text-slate-100 mb-4">确认删除</h3>
        <p class="text-gray-600 dark:text-slate-400 mb-6">确定要删除中转页配置吗？此操作不可恢复。</p>
        <div class="flex justify-end gap-4">
          <button
            @click="showDeleteDialog = false"
            class="px-4 py-2 text-gray-600 dark:text-slate-400 hover:bg-gray-100 dark:hover:bg-slate-700 rounded-lg transition-colors"
          >
            取消
          </button>
          <button
            @click="confirmDelete"
            class="px-4 py-2 bg-red-600 hover:bg-red-700 dark:bg-red-700 dark:hover:bg-red-600 text-white rounded-lg transition-colors"
          >
            确定删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import api from '../api'
import toast from '../utils/toast'

export default {
  name: 'TransitPage',
  setup() {
    const loading = ref(false)
    const hasConfig = ref(false)
    const showDeleteDialog = ref(false)
    
    const form = ref({
      title: '链接跳转中...',
      content: '您即将离开本站，请注意安全！',
      waitTime: 3,
      backgroundColor: '#f3f4f6',
      textColor: '#1f2937',
      buttonColor: '#3b82f6',
      poweredByName: 'Orange ShortUrl',
      poweredByIcon: null,
      status: 1
    })
    
    const iconInput = ref(null)

    // 处理图标上传
    const handleIconUpload = (event) => {
      const file = event.target.files[0]
      if (!file) return
      
      // 验证文件类型
      if (!file.type.startsWith('image/')) {
        toast.error('请上传图片文件')
        return
      }
      
      // 验证文件大小（限制为2MB）
      if (file.size > 2 * 1024 * 1024) {
        toast.error('图片大小不能超过2MB')
        return
      }
      
      // 转换为Base64
      const reader = new FileReader()
      reader.onload = (e) => {
        form.value.poweredByIcon = e.target.result
      }
      reader.onerror = () => {
        toast.error('图片读取失败')
      }
      reader.readAsDataURL(file)
    }
    
    // 加载配置
    const loadConfig = async () => {
      try {
        const response = await api.getTransitPage()
        if (response.code === 200 && response.data) {
          form.value = {
            title: response.data.title,
            content: response.data.content,
            waitTime: response.data.waitTime,
            backgroundColor: response.data.backgroundColor,
            textColor: response.data.textColor,
            buttonColor: response.data.buttonColor,
            poweredByName: response.data.poweredByName || 'Orange ShortUrl',
            poweredByIcon: response.data.poweredByIcon || null,
            status: response.data.status
          }
          hasConfig.value = true
        }
      } catch (error) {
        console.error('加载配置失败:', error)
      }
    }

    // 保存配置
    const saveConfig = async () => {
      loading.value = true
      try {
        const response = await api.saveTransitPage(form.value)
        if (response.code === 200) {
          toast.success(response.data || '保存成功')
          hasConfig.value = true
        } else {
          toast.error(response.data || '保存失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        toast.error('保存失败，请重试')
      } finally {
        loading.value = false
      }
    }

    // 确认删除
    const confirmDelete = async () => {
      try {
        const response = await api.deleteTransitPage()
        if (response.code === 200) {
          toast.success('删除成功')
          showDeleteDialog.value = false
          hasConfig.value = false
          // 重置表单为默认值
          form.value = {
            title: '链接跳转中...',
            content: '您即将离开本站，请注意安全！',
            waitTime: 3,
            backgroundColor: '#f3f4f6',
            textColor: '#1f2937',
            buttonColor: '#3b82f6',
            poweredByName: 'Orange ShortUrl',
            poweredByIcon: null,
            status: 1
          }
        } else {
          toast.error(response.message || '删除失败')
        }
      } catch (error) {
        console.error('删除失败:', error)
        toast.error('删除失败，请重试')
      }
    }

    onMounted(() => {
      loadConfig()
    })

    return {
      loading,
      hasConfig,
      showDeleteDialog,
      form,
      iconInput,
      handleIconUpload,
      saveConfig,
      confirmDelete
    }
  }
}
</script>

<style scoped>
/* 添加一些自定义样式 */
input[type="color"] {
  border: 2px solid #e5e7eb;
}

input[type="color"]::-webkit-color-swatch-wrapper {
  padding: 0;
}

input[type="color"]::-webkit-color-swatch {
  border: none;
  border-radius: 0.375rem;
}
</style>

