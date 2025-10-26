<template>
  <div class="min-h-[calc(100vh-12rem)] py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-4xl mx-auto">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <div class="flex items-center justify-center mb-4">
          <svg class="w-12 h-12 text-purple-600 dark:text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
          </svg>
        </div>
        <h1 class="text-3xl font-bold text-gray-900 dark:text-slate-50 mb-2">AI内容审核配置</h1>
        <p class="text-gray-600 dark:text-slate-400">配置AI接口，自动检测URL内容是否合规</p>
      </div>

      <!-- 配置表单 -->
      <div class="bg-white dark:bg-slate-800 rounded-lg shadow-lg p-6 mb-8">
        <form @submit.prevent="saveConfig">
          <!-- AI厂商选择 -->
          <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
              AI厂商 <span class="text-red-500">*</span>
            </label>
            <select
              v-model="form.provider"
              @change="onProviderChange"
              required
              class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            >
              <option value="openai">OpenAI</option>
              <option value="zhipu">智谱AI (ChatGLM)</option>
              <option value="deepseek">DeepSeek</option>
              <option value="qwen">通义千问</option>
              <option value="custom">自定义</option>
            </select>
            <p class="mt-2 text-xs text-gray-500 dark:text-slate-400">
              选择厂商后将自动填充推荐的API配置，您也可以手动修改
            </p>
          </div>

          <!-- API配置 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                API地址 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="form.apiUrl"
                type="url"
                required
                placeholder="https://api.openai.com/v1/chat/completions"
                class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                模型名称 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="form.modelName"
                type="text"
                required
                placeholder="gpt-3.5-turbo"
                class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              />
            </div>
          </div>

          <!-- API密钥 -->
          <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
              API密钥 <span class="text-red-500">*</span>
            </label>
            <div class="flex gap-2">
              <div class="relative flex-1">
                <input
                  v-model="form.apiKey"
                  :type="showApiKey ? 'text' : 'password'"
                  required
                  placeholder="sk-xxxxxxxxxxxxxxxxxxxxxxxx"
                  class="w-full px-4 py-2 pr-12 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                />
                <button
                  type="button"
                  @click="showApiKey = !showApiKey"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 dark:text-slate-400 hover:text-gray-700 dark:hover:text-slate-200"
                >
                  <svg v-if="!showApiKey" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                  <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
                  </svg>
                </button>
              </div>
              <button
                type="button"
                @click="testApiConnection"
                :disabled="!form.apiKey || testingApi"
                class="px-4 py-2 bg-green-600 hover:bg-green-700 dark:bg-green-700 dark:hover:bg-green-600 text-white rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2 whitespace-nowrap"
              >
                <svg v-if="!testingApi" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <svg v-else class="w-5 h-5 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                {{ testingApi ? '测试中' : '测试连接' }}
              </button>
            </div>
            <p v-if="testResult" class="mt-2 text-sm" :class="testResult.success ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400'">
              {{ testResult.message }}
            </p>
          </div>

          <!-- 审核提示词 -->
          <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
              审核提示词 <span class="text-red-500">*</span>
            </label>
            <textarea
              v-model="form.prompt"
              required
              rows="8"
              placeholder="请检测以下URL的内容是否合规。URL: {url}&#10;&#10;请在第一行仅回答&quot;合规&quot;或&quot;不合规&quot;，然后另起一行简要说明判断理由。&#10;&#10;判断标准：如果URL内容包含或涉及暴力、色情、赌博、诈骗、恶意软件等违法违规信息，则为不合规；正常的技术、学习、新闻、商业内容均为合规。"
              class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent resize-none font-mono text-sm"
            ></textarea>
            <div class="mt-2 space-y-2">
              <p class="text-xs text-gray-500 dark:text-slate-400">
                提示：使用 <code class="bg-gray-100 dark:bg-slate-700 dark:text-slate-300 px-1 py-0.5 rounded">{url}</code> 作为URL占位符
              </p>
              <div class="bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg p-3">
                <p class="text-xs text-blue-700 dark:text-blue-300 font-medium mb-1">💡 提示词建议</p>
                <p class="text-xs text-blue-600 dark:text-blue-400">为确保AI回复能被正确解析，建议在提示词中明确要求AI在<span class="font-semibold">第一行仅回答"合规"或"不合规"</span>，避免在说明理由时混入违规关键词导致误判。</p>
              </div>
            </div>
          </div>

          <!-- 超时时间 -->
          <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
              超时时间（秒） <span class="text-red-500">*</span>
            </label>
            <input
              v-model.number="form.timeout"
              type="number"
              required
              min="5"
              max="120"
              class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <!-- 启用开关 -->
          <div class="mb-6">
            <label class="flex items-center cursor-pointer">
              <input
                v-model="form.enabled"
                type="checkbox"
                :true-value="1"
                :false-value="0"
                class="sr-only peer"
              />
              <div class="relative w-11 h-6 bg-gray-200 dark:bg-slate-600 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-purple-300 dark:peer-focus:ring-purple-800 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 dark:after:border-slate-500 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-purple-600"></div>
              <span class="ml-3 text-sm font-medium text-gray-700 dark:text-slate-300">启用AI内容审核</span>
            </label>
            <p class="mt-2 text-xs text-gray-500 dark:text-slate-400">
              禁用后，将不会调用AI接口进行内容审核
            </p>
          </div>

          <!-- 按钮组 -->
          <div class="flex justify-end gap-4">
            <button
              type="submit"
              :disabled="loading"
              class="px-6 py-2 bg-purple-600 hover:bg-purple-700 dark:bg-purple-700 dark:hover:bg-purple-600 text-white rounded-lg transition-colors disabled:opacity-50"
            >
              {{ loading ? '保存中...' : '保存配置' }}
            </button>
          </div>
        </form>
      </div>

      <!-- 说明文档 -->
      <div class="bg-white dark:bg-slate-800 rounded-lg shadow-lg p-6">
        <h2 class="text-xl font-semibold text-gray-800 dark:text-slate-200 mb-4 flex items-center">
          <svg class="w-6 h-6 mr-2 text-blue-500 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          使用说明
        </h2>
        <div class="space-y-4 text-sm text-gray-600 dark:text-slate-400">
          <div>
            <h3 class="font-semibold text-gray-800 dark:text-slate-200 mb-2">1. 选择AI厂商</h3>
            <p>根据您的需求选择合适的AI服务商，不同厂商的API格式和能力可能有所不同。</p>
          </div>
          <div>
            <h3 class="font-semibold text-gray-800 dark:text-slate-200 mb-2">2. 配置API信息</h3>
            <p>填写API地址、密钥和模型名称。确保API密钥有足够的权限和额度。</p>
          </div>
          <div>
            <h3 class="font-semibold text-gray-800 dark:text-slate-200 mb-2">3. 编写提示词</h3>
            <p>提示词应该明确要求AI检测URL内容是否包含违规信息。使用 <code class="bg-gray-100 dark:bg-slate-700 dark:text-slate-300 px-1 py-0.5 rounded">{url}</code> 作为URL占位符。</p>
            <p class="mt-2 text-yellow-600 dark:text-yellow-400"><strong>重要：</strong>建议要求AI在第一行仅回答"合规"或"不合规"，避免在说明中包含违规关键词导致误判。</p>
          </div>
          <div>
            <h3 class="font-semibold text-gray-800 dark:text-slate-200 mb-2">4. 测试连接</h3>
            <p>配置完成后，点击"测试连接"按钮验证API密钥和配置是否正确。</p>
          </div>
          <div>
            <h3 class="font-semibold text-gray-800 dark:text-slate-200 mb-2">5. 审核机制</h3>
            <p>当用户创建短链接时，系统会自动调用AI接口审核URL内容。如果检测到违规内容，将拒绝创建并提示用户。</p>
          </div>
          <div>
            <h3 class="font-semibold text-gray-800 dark:text-slate-200 mb-2">6. 容错机制</h3>
            <p>如果AI接口调用失败或超时，系统会自动允许创建链接，避免影响正常业务。</p>
          </div>
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
  name: 'AiConfig',
  setup() {
    const loading = ref(false)
    const showApiKey = ref(false)
    const testingApi = ref(false)
    const testResult = ref(null)
    
    const form = ref({
      provider: 'openai',
      apiKey: '',
      apiUrl: 'https://api.openai.com/v1/chat/completions',
      modelName: 'gpt-3.5-turbo',
      prompt: '请检测以下URL的内容是否合规。URL: {url}\n\n请在第一行仅回答"合规"或"不合规"，然后另起一行简要说明判断理由。\n\n判断标准：如果URL内容包含或涉及暴力、色情、赌博、诈骗、恶意软件等违法违规信息，则为不合规；正常的技术、学习、新闻、商业内容均为合规。',
      enabled: 1,
      timeout: 30
    })

    // 厂商配置映射
    const providerConfigs = {
      openai: {
        apiUrl: 'https://api.openai.com/v1/chat/completions',
        modelName: 'gpt-3.5-turbo'
      },
      zhipu: {
        apiUrl: 'https://open.bigmodel.cn/api/paas/v4/chat/completions',
        modelName: 'glm-4'
      },
      deepseek: {
        apiUrl: 'https://api.deepseek.com/v1/chat/completions',
        modelName: 'deepseek-chat'
      },
      qwen: {
        apiUrl: 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation',
        modelName: 'qwen-turbo'
      },
      custom: {
        apiUrl: '',
        modelName: ''
      }
    }

    // 厂商变更时自动填充配置
    const onProviderChange = () => {
      const config = providerConfigs[form.value.provider]
      if (config) {
        form.value.apiUrl = config.apiUrl
        form.value.modelName = config.modelName
      }
      // 清空测试结果
      testResult.value = null
    }

    // 测试API连接
    const testApiConnection = async () => {
      if (!form.value.apiKey) {
        toast.error('请先填写API密钥')
        return
      }

      testingApi.value = true
      testResult.value = null

      try {
        const response = await api.testAiConfig(form.value)
        
        if (response.code === 200) {
          testResult.value = {
            success: true,
            message: '✓ ' + response.data
          }
          toast.success('API连接测试成功')
        } else {
          testResult.value = {
            success: false,
            message: '✗ ' + (response.message || '连接失败')
          }
          toast.error('API连接测试失败')
        }
      } catch (error) {
        console.error('测试失败:', error)
        testResult.value = {
          success: false,
          message: '✗ 连接失败: ' + (error.response?.data?.message || error.message || '未知错误')
        }
        toast.error('API连接测试失败')
      } finally {
        testingApi.value = false
      }
    }

    // 加载配置
    const loadConfig = async () => {
      try {
        const response = await api.getAiConfig()
        if (response.code === 200 && response.data) {
          form.value = {
            provider: response.data.provider,
            apiKey: response.data.apiKey,
            apiUrl: response.data.apiUrl,
            modelName: response.data.modelName,
            prompt: response.data.prompt,
            enabled: response.data.enabled,
            timeout: response.data.timeout
          }
        }
      } catch (error) {
        if (error.response?.status === 403) {
          toast.error('无权限访问')
          // 跳转到首页
          setTimeout(() => {
            window.location.href = '/'
          }, 1500)
        } else {
          console.error('加载配置失败:', error)
        }
      }
    }

    // 保存配置
    const saveConfig = async () => {
      loading.value = true
      try {
        const response = await api.saveAiConfig(form.value)
        if (response.code === 200) {
          toast.success(response.data || '保存成功')
        } else {
          toast.error(response.data || '保存失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        if (error.response?.status === 403) {
          toast.error('无权限操作')
        } else {
          toast.error('保存失败，请重试')
        }
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      loadConfig()
    })

    return {
      loading,
      showApiKey,
      testingApi,
      testResult,
      form,
      onProviderChange,
      testApiConnection,
      saveConfig
    }
  }
}
</script>

