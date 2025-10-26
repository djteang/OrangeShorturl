<template>
  <transition name="dialog">
    <div class="fixed inset-0 z-[100] flex items-center justify-center p-4" @click.self="handleCancel">
      <div class="absolute inset-0 bg-black bg-opacity-60 backdrop-blur-sm"></div>
      <div class="relative glass-effect rounded-2xl border-2 dark:border-slate-600 border-slate-300 shadow-2xl max-w-2xl w-full overflow-hidden">
        <!-- 头部 -->
        <div class="px-6 py-4 border-b-2 dark:border-slate-700 border-slate-200 bg-gradient-to-r from-orange-500 to-orange-600">
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-white flex items-center">
              <svg class="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
              </svg>
              编辑头像
            </h3>
            <button @click="handleCancel" class="p-2 hover:bg-white/20 rounded-lg transition-all">
              <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
        </div>

        <!-- 内容 -->
        <div class="p-6">
          <!-- 隐藏的文件输入框（始终保留在DOM中） -->
          <input 
            ref="fileInput" 
            type="file" 
            accept="image/*" 
            class="hidden" 
            @change="handleFileChange"
          />
          
          <!-- 图片选择区域 -->
          <div v-if="!imageSrc" class="text-center py-12">
            <div 
              @click="fileInput.click()"
              class="cursor-pointer border-3 border-dashed dark:border-slate-600 border-slate-300 rounded-2xl p-12 dark:hover:border-orange-500 hover:border-orange-500 dark:hover:bg-slate-800/50 hover:bg-orange-50 transition-all">
              <svg class="w-16 h-16 dark:text-slate-500 text-slate-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
              </svg>
              <p class="text-lg dark:text-slate-300 text-slate-700 font-medium mb-2">点击上传图片</p>
              <p class="text-sm dark:text-slate-400 text-slate-600">支持 JPG、PNG、GIF 格式，最大 2MB</p>
            </div>
          </div>

          <!-- 裁剪区域 -->
          <div v-else>
            <!-- 重新选择图片按钮 -->
            <div class="mb-3 flex justify-end">
              <button
                @click="changeImage"
                class="text-sm px-4 py-2 dark:text-orange-400 text-orange-600 dark:hover:text-orange-300 hover:text-orange-700 dark:hover:bg-slate-700/50 hover:bg-orange-50 rounded-lg transition-all flex items-center gap-2 border border-transparent dark:hover:border-slate-600 hover:border-orange-200">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
                </svg>
                重新选择图片
              </button>
            </div>
            
            <div class="mb-4">
              <div class="relative bg-black rounded-xl overflow-hidden" style="height: 400px;">
                <img ref="cropperImage" :src="imageSrc" alt="cropper" class="max-w-full" />
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="flex items-center justify-center space-x-2 mb-4">
              <button
                @click="rotateLeft"
                class="p-3 dark:bg-slate-700 bg-slate-200 dark:hover:bg-slate-600 hover:bg-slate-300 rounded-lg transition-all border dark:border-slate-600 border-slate-400 group"
                title="向左旋转">
                <svg class="w-5 h-5 dark:text-slate-200 text-slate-700 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6"></path>
                </svg>
              </button>
              <button
                @click="rotateRight"
                class="p-3 dark:bg-slate-700 bg-slate-200 dark:hover:bg-slate-600 hover:bg-slate-300 rounded-lg transition-all border dark:border-slate-600 border-slate-400 group"
                title="向右旋转">
                <svg class="w-5 h-5 dark:text-slate-200 text-slate-700 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 10H11a8 8 0 00-8 8v2m18-10l-6 6m6-6l-6-6"></path>
                </svg>
              </button>
              <button
                @click="flipHorizontal"
                class="p-3 dark:bg-slate-700 bg-slate-200 dark:hover:bg-slate-600 hover:bg-slate-300 rounded-lg transition-all border dark:border-slate-600 border-slate-400 group"
                title="水平翻转">
                <svg class="w-5 h-5 dark:text-slate-200 text-slate-700 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4"></path>
                </svg>
              </button>
              <button
                @click="flipVertical"
                class="p-3 dark:bg-slate-700 bg-slate-200 dark:hover:bg-slate-600 hover:bg-slate-300 rounded-lg transition-all border dark:border-slate-600 border-slate-400 group"
                title="垂直翻转">
                <svg class="w-5 h-5 dark:text-slate-200 text-slate-700 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4"></path>
                </svg>
              </button>
              <button
                @click="zoomIn"
                class="p-3 dark:bg-slate-700 bg-slate-200 dark:hover:bg-slate-600 hover:bg-slate-300 rounded-lg transition-all border dark:border-slate-600 border-slate-400 group"
                title="放大">
                <svg class="w-5 h-5 dark:text-slate-200 text-slate-700 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v6m3-3H7"></path>
                </svg>
              </button>
              <button
                @click="zoomOut"
                class="p-3 dark:bg-slate-700 bg-slate-200 dark:hover:bg-slate-600 hover:bg-slate-300 rounded-lg transition-all border dark:border-slate-600 border-slate-400 group"
                title="缩小">
                <svg class="w-5 h-5 dark:text-slate-200 text-slate-700 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM13 10H7"></path>
                </svg>
              </button>
              <button
                @click="reset"
                class="p-3 dark:bg-slate-700 bg-slate-200 dark:hover:bg-slate-600 hover:bg-slate-300 rounded-lg transition-all border dark:border-slate-600 border-slate-400 group"
                title="重置">
                <svg class="w-5 h-5 dark:text-slate-200 text-slate-700 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                </svg>
              </button>
            </div>
          </div>
        </div>

        <!-- 底部按钮 -->
        <div class="px-6 py-4 border-t-2 dark:border-slate-700 border-slate-200 flex justify-end space-x-3">
          <button
            @click="handleCancel"
            class="px-6 py-3 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 rounded-lg transition-all font-medium shadow-sm hover:shadow border dark:border-slate-600 border-slate-400">
            取消
          </button>
          <button
            v-if="imageSrc"
            @click="handleConfirm"
            :disabled="uploading"
            class="px-6 py-3 bg-orange-600 hover:bg-orange-500 text-white rounded-lg transition-all font-medium shadow-md hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed border border-orange-600">
            {{ uploading ? '上传中...' : '确认上传' }}
          </button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import Cropper from 'cropperjs'
import 'cropperjs/dist/cropper.css'
import { userStore } from '../store'
import api from '../api/user'
import toast from '../utils/toast'

const props = defineProps({
  existingAvatar: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['close', 'update'])

const fileInput = ref(null)
const cropperImage = ref(null)
const imageSrc = ref('')
const cropper = ref(null)
const uploading = ref(false)
const scaleX = ref(1)
const scaleY = ref(1)

// 组件挂载时，如果有已有头像，显示在预览区
onMounted(() => {
  if (props.existingAvatar) {
    imageSrc.value = props.existingAvatar
  }
})

watch(imageSrc, async (newVal) => {
  if (newVal) {
    await nextTick()
    initCropper()
  }
})

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证文件类型
  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }

  // 验证文件大小（限制为2MB）
  if (file.size > 2 * 1024 * 1024) {
    alert('图片大小不能超过2MB')
    return
  }

  // 读取文件
  const reader = new FileReader()
  reader.onload = (e) => {
    imageSrc.value = e.target.result
  }
  reader.readAsDataURL(file)

  // 清空input
  event.target.value = ''
}

const initCropper = () => {
  if (!cropperImage.value) return

  destroyCropper()

  cropper.value = new Cropper(cropperImage.value, {
    aspectRatio: 1,
    viewMode: 1,
    dragMode: 'move',
    autoCropArea: 0.8,
    restore: false,
    guides: true,
    center: true,
    highlight: false,
    cropBoxMovable: true,
    cropBoxResizable: true,
    toggleDragModeOnDblclick: false,
  })
}

const destroyCropper = () => {
  if (cropper.value) {
    cropper.value.destroy()
    cropper.value = null
  }
}

const rotateLeft = () => {
  if (cropper.value) {
    cropper.value.rotate(-90)
  }
}

const rotateRight = () => {
  if (cropper.value) {
    cropper.value.rotate(90)
  }
}

const flipHorizontal = () => {
  if (cropper.value) {
    scaleX.value = -scaleX.value
    cropper.value.scaleX(scaleX.value)
  }
}

const flipVertical = () => {
  if (cropper.value) {
    scaleY.value = -scaleY.value
    cropper.value.scaleY(scaleY.value)
  }
}

const zoomIn = () => {
  if (cropper.value) {
    cropper.value.zoom(0.1)
  }
}

const zoomOut = () => {
  if (cropper.value) {
    cropper.value.zoom(-0.1)
  }
}

const reset = () => {
  if (cropper.value) {
    cropper.value.reset()
    scaleX.value = 1
    scaleY.value = 1
  }
}

const changeImage = () => {
  fileInput.value.click()
}

const handleConfirm = async () => {
  if (!cropper.value) return

  uploading.value = true
  
  try {
    // 获取裁剪后的canvas（使用更高的分辨率以保持清晰度）
    const canvas = cropper.value.getCroppedCanvas({
      width: 512,
      height: 512,
      imageSmoothingEnabled: true,
      imageSmoothingQuality: 'high'
    })

    // 转换为Base64（使用PNG格式保持最佳质量，或使用更高的JPEG质量）
    const croppedImage = canvas.toDataURL('image/png')
    
    // 调用API更新头像
    const response = await api.updateAvatar({ avatarBase64: croppedImage })
    
    if (response.code === 200) {
      // 更新store中的头像
      userStore.user.avatarBase64 = croppedImage
      localStorage.setItem('user', JSON.stringify(userStore.user))
      toast.success('头像更新成功')
      emit('update', croppedImage)
    } else {
      toast.error(response.message || '头像更新失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    toast.error('头像上传失败，请重试')
  } finally {
    uploading.value = false
  }
}

const handleCancel = () => {
  emit('close')
}

onUnmounted(() => {
  destroyCropper()
})
</script>

<style scoped>
/* 弹窗动画 */
.dialog-enter-active,
.dialog-leave-active {
  transition: opacity 0.3s ease;
}

.dialog-enter-from,
.dialog-leave-to {
  opacity: 0;
}

.dialog-enter-active > div:last-child,
.dialog-leave-active > div:last-child {
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.dialog-enter-from > div:last-child {
  transform: scale(0.9) translateY(-20px);
}

.dialog-leave-to > div:last-child {
  transform: scale(0.9) translateY(-20px);
}
</style>

