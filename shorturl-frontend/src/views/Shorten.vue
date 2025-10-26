<template>
  <div class="min-h-[calc(100vh-12rem)] flex items-center justify-center px-4 py-12">
    <div class="max-w-4xl w-full">
      <!-- 标题 -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold dark:text-slate-50 text-slate-900 mb-4">创建短链接</h1>
        <p class="text-lg dark:text-slate-400 text-slate-600">输入长链接，一键生成短链接</p>
      </div>

      <!-- 主卡片 -->
      <div class="glass-effect rounded-2xl p-8 md:p-12 shadow-lg border dark:border-slate-600 border-slate-300">
        <!-- 输入框 -->
        <div class="mb-6">
          <label class="block dark:text-slate-300 text-slate-700 text-sm font-semibold mb-3">
            长链接
          </label>
          <input
            v-model="form.originalUrl"
            type="text"
            placeholder="https://example.com/very/long/url"
            class="w-full px-4 py-3 text-lg dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all dark:placeholder-slate-500 placeholder-slate-400"
            @keyup.enter="handleSubmit"
          />
        </div>

        <!-- 高级选项 -->
        <div class="mb-6">
          <button
            @click="showAdvanced = !showAdvanced"
            class="dark:text-orange-400 text-orange-600 dark:hover:text-orange-300 hover:text-orange-500 text-sm font-medium flex items-center transition-all"
          >
            <span>{{ showAdvanced ? '隐藏' : '显示' }}高级选项</span>
            <svg class="w-4 h-4 ml-1 transform transition-all" :class="{'rotate-180': showAdvanced}" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
            </svg>
          </button>

          <div v-show="showAdvanced" class="mt-4 space-y-4">
            <!-- 分组选择 -->
            <div v-if="isLoggedIn">
              <label class="block dark:text-slate-300 text-slate-700 text-sm font-semibold mb-2">
                选择分组（可选）
              </label>
              <select
                v-model="form.groupId"
                class="w-full px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all"
              >
                <option :value="null">未分组</option>
                <option v-for="group in groups" :key="group.id" :value="group.id">
                  {{ group.name }} ({{ group.urlCount }})
                </option>
              </select>
            </div>

            <div>
              <label class="block dark:text-slate-300 text-slate-700 text-sm font-semibold mb-2">
                自定义短码（可选）
              </label>
              <input
                v-model="form.customCode"
                type="text"
                placeholder="mylink123"
                class="w-full px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all dark:placeholder-slate-500 placeholder-slate-400"
              />
              <p class="text-xs dark:text-slate-400 text-slate-600 mt-1">只能包含字母和数字，长度4-20位</p>
            </div>

            <div>
              <label class="block dark:text-slate-300 text-slate-700 text-sm font-semibold mb-2">
                过期时间（可选）
              </label>
              <input
                v-model="form.expireTime"
                type="datetime-local"
                class="w-full px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all"
              />
            </div>

            <!-- 二维码样式自定义 -->
            <div>
              <label class="block dark:text-slate-300 text-slate-700 text-sm font-semibold mb-2">
                二维码样式自定义
              </label>
              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label class="block text-xs dark:text-slate-400 text-slate-600 mb-1">前景色</label>
                  <input
                    v-model="form.qrConfig.colorDark"
                    type="color"
                    class="w-full h-10 rounded-lg border-2 dark:border-slate-600 border-slate-300 cursor-pointer"
                  />
                </div>
                <div>
                  <label class="block text-xs dark:text-slate-400 text-slate-600 mb-1">背景色</label>
                  <input
                    v-model="form.qrConfig.colorLight"
                    type="color"
                    class="w-full h-10 rounded-lg border-2 dark:border-slate-600 border-slate-300 cursor-pointer"
                  />
                </div>
                <div>
                  <label class="block text-xs dark:text-slate-400 text-slate-600 mb-1">大小</label>
                  <input
                    v-model.number="form.qrConfig.size"
                    type="number"
                    min="100"
                    max="500"
                    class="w-full px-3 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none"
                  />
                </div>
                <div>
                  <label class="block text-xs dark:text-slate-400 text-slate-600 mb-1">容错率</label>
                  <select
                    v-model="form.qrConfig.correctLevel"
                    class="w-full px-3 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none"
                  >
                    <option value="L">低(L)</option>
                    <option value="M">中(M)</option>
                    <option value="Q">高(Q)</option>
                    <option value="H">最高(H)</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 生成按钮 -->
        <button
          @click="handleSubmit"
          :disabled="loading"
          class="w-full bg-orange-600 hover:bg-orange-500 text-white font-bold py-4 px-8 rounded-lg text-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 border border-orange-600"
        >
          {{ loading ? '生成中...' : '生成短链接' }}
        </button>

        <!-- 结果展示 -->
        <div v-if="result" class="mt-8 p-6 dark:bg-green-900/20 bg-green-50 rounded-xl border-2 dark:border-green-600/50 border-green-500/50 shadow-lg">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-bold dark:text-slate-50 text-slate-900">生成成功！</h3>
            <svg class="w-6 h-6 dark:text-green-400 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
            </svg>
          </div>

          <div class="space-y-3">
            <div>
              <label class="block text-sm dark:text-slate-400 text-slate-700 font-semibold mb-1">短链接</label>
              <div class="flex items-center space-x-2">
                <input
                  :value="result.shortUrl"
                  readonly
                  class="flex-1 px-4 py-2 dark:bg-slate-900/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-orange-400 text-orange-600 rounded-lg font-semibold"
                />
                <button
                  @click="copyToClipboard(result.shortUrl)"
                  class="px-4 py-2 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 rounded-lg transition-all font-medium shadow-sm hover:shadow border dark:border-slate-600 border-slate-400"
                >
                  {{ copied ? '已复制' : '复制' }}
                </button>
              </div>
            </div>

            <div>
              <label class="block text-sm dark:text-slate-400 text-slate-700 font-semibold mb-1">原始链接</label>
              <div class="px-4 py-2 dark:bg-slate-900/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-200 text-slate-800 rounded-lg text-sm break-all">
                {{ result.originalUrl }}
              </div>
            </div>

            <!-- 二维码 -->
            <div class="text-center pt-4">
              <canvas ref="qrcodeCanvas" class="mx-auto border-4 dark:border-slate-600 border-slate-300 rounded-lg shadow-md"></canvas>
              <p class="text-xs dark:text-slate-400 text-slate-600 mt-2 font-medium">扫描二维码访问短链接</p>
            </div>
          </div>
        </div>

        <!-- 错误提示 -->
        <div v-if="error" class="mt-4 p-4 dark:bg-red-900/40 bg-red-50 border-2 dark:border-red-600 border-red-400 rounded-lg dark:text-red-300 text-red-700 font-medium">
          {{ error }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, computed } from 'vue'
import QRCode from 'qrcode'
import api from '../api'
import { themeStore, userStore } from '../store'

const form = ref({
  originalUrl: '',
  customCode: '',
  expireTime: '',
  groupId: null,
  qrConfig: {
    size: 200,
    colorDark: '#1e293b',
    colorLight: '#ffffff',
    correctLevel: 'M'
  }
})

const showAdvanced = ref(false)
const loading = ref(false)
const result = ref(null)
const error = ref('')
const copied = ref(false)
const qrcodeCanvas = ref(null)
const groups = ref([])

const isLoggedIn = computed(() => userStore.isLoggedIn)

// 加载分组列表
const loadGroups = async () => {
  if (!isLoggedIn.value) return
  try {
    const response = await api.getUrlGroups()
    if (response.code === 200) {
      groups.value = response.data || []
    }
  } catch (err) {
    console.error('加载分组失败', err)
  }
}

const handleSubmit = async () => {
  if (!form.value.originalUrl) {
    error.value = '请输入URL'
    return
  }

  if (!form.value.originalUrl.match(/^(http|https):\/\/.+/)) {
    error.value = 'URL格式不正确，必须以http://或https://开头'
    return
  }

  loading.value = true
  error.value = ''
  result.value = null

  try {
    const data = {
      originalUrl: form.value.originalUrl
    }

    if (form.value.customCode) {
      data.customCode = form.value.customCode
    }

    if (form.value.expireTime) {
      const date = new Date(form.value.expireTime)
      data.expireTime = date.getFullYear() + '-' + 
        String(date.getMonth() + 1).padStart(2, '0') + '-' +
        String(date.getDate()).padStart(2, '0') + ' ' +
        String(date.getHours()).padStart(2, '0') + ':' +
        String(date.getMinutes()).padStart(2, '0') + ':00'
    }

    if (form.value.groupId) {
      data.groupId = form.value.groupId
    }

    // 添加二维码配置
    data.qrConfig = {
      size: form.value.qrConfig.size,
      colorDark: form.value.qrConfig.colorDark,
      colorLight: form.value.qrConfig.colorLight,
      correctLevel: form.value.qrConfig.correctLevel
    }

    const response = await api.shorten(data)
    
    if (response.code === 200) {
      result.value = response.data
      await nextTick()
      if (qrcodeCanvas.value) {
        // 使用自定义的二维码配置
        QRCode.toCanvas(qrcodeCanvas.value, response.data.shortUrl, {
          width: form.value.qrConfig.size,
          margin: 2,
          color: {
            dark: form.value.qrConfig.colorDark,
            light: form.value.qrConfig.colorLight
          },
          errorCorrectionLevel: form.value.qrConfig.correctLevel
        })
      }
    } else {
      error.value = response.message || '生成失败'
    }
  } catch (err) {
    error.value = err.response?.data?.message || '生成失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text)
    copied.value = true
    setTimeout(() => {
      copied.value = false
    }, 2000)
  } catch (err) {
    console.error('复制失败', err)
  }
}

onMounted(() => {
  loadGroups()
})
</script>

