<template>
  <div class="min-h-screen flex items-center justify-center px-4 py-12">
    <div class="max-w-md w-full">
      <div class="glass-effect rounded-2xl border dark:border-slate-600 border-slate-300 p-8 shadow-xl">
        <!-- 标题 -->
        <div class="text-center mb-8">
          <h1 class="text-3xl font-bold dark:text-slate-50 text-slate-900 mb-2">
            {{ isLogin ? '登录' : '注册' }}
          </h1>
          <p class="dark:text-slate-400 text-slate-600">
            {{ isLogin ? '欢迎回来！' : '创建您的账号' }}
          </p>
        </div>

        <!-- 表单 -->
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <!-- 用户名 -->
          <div>
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">
              用户名
            </label>
            <input
              v-model="form.username"
              type="text"
              required
              class="w-full px-4 py-3 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all dark:placeholder-slate-500 placeholder-slate-400"
              placeholder="请输入用户名"
            />
          </div>

          <!-- 邮箱（仅注册） -->
          <div v-if="!isLogin">
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">
              邮箱 <span class="text-red-500">*</span>
            </label>
            <input
              v-model="form.email"
              type="email"
              required
              class="w-full px-4 py-3 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all dark:placeholder-slate-500 placeholder-slate-400"
              placeholder="请输入邮箱"
            />
          </div>

          <!-- 邮箱验证码（仅注册） -->
          <div v-if="!isLogin">
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">
              验证码 <span class="text-red-500">*</span>
            </label>
            <div class="flex gap-2">
              <input
                v-model="form.verifyCode"
                type="text"
                required
                maxlength="6"
                class="flex-1 px-4 py-3 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all dark:placeholder-slate-500 placeholder-slate-400"
                placeholder="请输入验证码"
              />
              <button
                type="button"
                @click="sendVerifyCode"
                :disabled="countdown > 0 || !isValidEmail"
                class="px-6 py-3 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 rounded-lg transition-all font-medium shadow-sm hover:shadow border dark:border-slate-600 border-slate-400 disabled:opacity-50 disabled:cursor-not-allowed whitespace-nowrap"
              >
                {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
              </button>
            </div>
            <p v-if="!isValidEmail && form.email" class="text-xs text-red-500 mt-1">请输入有效的邮箱地址</p>
          </div>

          <!-- 密码 -->
          <div>
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">
              密码
            </label>
            <input
              v-model="form.password"
              type="password"
              required
              class="w-full px-4 py-3 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all dark:placeholder-slate-500 placeholder-slate-400"
              placeholder="请输入密码"
            />
          </div>

          <!-- 提交按钮 -->
          <button
            type="submit"
            :disabled="loading"
            class="w-full bg-orange-600 hover:bg-orange-500 text-white font-bold py-3 rounded-lg transition-all disabled:opacity-50 shadow-md hover:shadow-lg transform hover:-translate-y-0.5 border border-orange-600"
          >
            {{ loading ? '处理中...' : (isLogin ? '登录' : '注册') }}
          </button>
        </form>

        <!-- 错误提示 -->
        <div v-if="error" class="mt-4 p-3 dark:bg-red-900/40 bg-red-50 border-2 dark:border-red-600 border-red-400 rounded-lg dark:text-red-300 text-red-700 text-sm font-medium">
          {{ error }}
        </div>

        <!-- 切换登录/注册 -->
        <div class="mt-6 text-center">
          <button
            @click="isLogin = !isLogin"
            class="dark:text-orange-400 text-orange-600 dark:hover:text-orange-300 hover:text-orange-500 text-sm font-medium transition-all"
          >
            {{ isLogin ? '还没有账号？立即注册' : '已有账号？立即登录' }}
          </button>
        </div>

        <!-- OAuth登录 -->
        <div class="mt-6">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t dark:border-slate-600 border-slate-300"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 dark:bg-slate-800/90 bg-white dark:text-slate-400 text-slate-600">
                或使用以下方式登录
              </span>
            </div>
          </div>

          <div class="mt-6">
            <button
              @click="handleLinuxDoLogin"
              :disabled="oauthLoading"
              class="w-full flex items-center justify-center gap-2 px-4 py-3 border-2 dark:border-slate-600 border-slate-300 rounded-lg dark:hover:bg-slate-800/50 hover:bg-slate-100 transition-all shadow-sm hover:shadow disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <img src="../../pubilc/linuxdo.png" alt="Linux.do" class="w-5 h-5" />
              <span class="dark:text-slate-200 text-slate-800 font-semibold">
                {{ oauthLoading ? '跳转中...' : 'Linux.do 登录' }}
              </span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { userStore } from '../store'
import userApi from '../api/user'
import toast from '../utils/toast'

const router = useRouter()

const isLogin = ref(true)
const loading = ref(false)
const error = ref('')
const countdown = ref(0)
const oauthLoading = ref(false)

const form = ref({
  username: '',
  email: '',
  password: '',
  verifyCode: ''
})

// 验证邮箱格式
const isValidEmail = computed(() => {
  if (!form.value.email) return false
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(form.value.email)
})

// 发送验证码
const sendVerifyCode = async () => {
  if (!isValidEmail.value) {
    error.value = '请输入有效的邮箱地址'
    return
  }

  try {
    const response = await userApi.sendVerifyCode({ email: form.value.email })
    if (response.code === 200) {
      // 开始倒计时
      countdown.value = 60
      const timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          clearInterval(timer)
        }
      }, 1000)
      error.value = ''
      toast.success('验证码已发送，请查收邮箱')
    } else {
      error.value = response.message || '发送验证码失败'
      toast.error(response.message || '发送验证码失败')
    }
  } catch (err) {
    error.value = err.response?.data?.message || '发送验证码失败，请稍后重试'
    toast.error(err.response?.data?.message || '发送验证码失败，请稍后重试')
  }
}

const handleSubmit = async () => {
  error.value = ''
  
  // 注册时验证邮箱和验证码
  if (!isLogin.value) {
    if (!isValidEmail.value) {
      error.value = '请输入有效的邮箱地址'
      return
    }
    if (!form.value.verifyCode) {
      error.value = '请输入验证码'
      return
    }
  }
  
  loading.value = true

  try {
    const api = isLogin.value ? userApi.login : userApi.register
    const response = await api(form.value)

    if (response.code === 200) {
      userStore.login(response.data, response.data.token)
      router.push('/manage')
    } else {
      error.value = response.message
    }
  } catch (err) {
    error.value = err.response?.data?.message || '操作失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const handleLinuxDoLogin = async () => {
  try {
    oauthLoading.value = true
    error.value = ''
    
    // 调用后端接口获取授权URL
    const response = await userApi.getLinuxDoAuthUrl()
    
    if (response.code === 200) {
      // 跳转到Linux.do授权页面
      window.location.href = response.data
    } else {
      error.value = response.message || '获取授权链接失败'
      toast.error(response.message || '获取授权链接失败')
      oauthLoading.value = false
    }
  } catch (err) {
    console.error('OAuth登录失败:', err)
    error.value = err.response?.data?.message || '跳转失败，请稍后重试'
    toast.error(err.response?.data?.message || '跳转失败，请稍后重试')
    oauthLoading.value = false
  }
}
</script>

