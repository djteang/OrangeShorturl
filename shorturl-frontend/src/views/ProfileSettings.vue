<template>
  <div class="min-h-[calc(100vh-12rem)] py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-3xl mx-auto">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-slate-50 mb-2">个人设置</h1>
        <p class="text-gray-600 dark:text-slate-400">管理您的个人信息和账户安全</p>
      </div>

      <!-- 设置卡片 -->
      <div class="bg-white dark:bg-slate-800 rounded-lg shadow-lg p-6">
        <!-- 头像设置 -->
        <div class="mb-8 pb-8 border-b border-gray-200 dark:border-slate-700">
          <h2 class="text-xl font-semibold text-gray-800 dark:text-slate-200 mb-4 flex items-center">
            <svg class="w-6 h-6 mr-2 text-blue-500 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            头像设置
          </h2>
          <div class="flex items-center gap-6">
            <div 
              class="relative w-24 h-24 rounded-full overflow-hidden cursor-pointer group border-4 border-gray-200 dark:border-slate-600 hover:border-blue-500 dark:hover:border-blue-400 transition-all"
              @click="showAvatarCropper = true"
            >
              <img 
                v-if="userStore.user?.avatarBase64" 
                :src="userStore.user.avatarBase64" 
                alt="用户头像"
                class="w-full h-full object-cover"
              />
              <div 
                v-else 
                class="w-full h-full bg-gradient-to-br from-orange-400 to-pink-500 flex items-center justify-center"
              >
                <span class="text-3xl font-bold text-white">
                  {{ userStore.user?.username?.charAt(0).toUpperCase() }}
                </span>
              </div>
              <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-50 flex items-center justify-center transition-all">
                <svg class="w-8 h-8 text-white opacity-0 group-hover:opacity-100 transition-opacity" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
            </div>
            <div>
              <button
                @click="showAvatarCropper = true"
                class="px-4 py-2 bg-blue-600 hover:bg-blue-700 dark:bg-blue-700 dark:hover:bg-blue-600 text-white rounded-lg transition-colors flex items-center gap-2"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                {{ userStore.user?.avatarBase64 ? '更换头像' : '上传头像' }}
              </button>
              <p class="mt-2 text-sm text-gray-500 dark:text-slate-400">
                点击头像或按钮上传/更换头像
              </p>
            </div>
          </div>
        </div>

        <!-- 基本信息 -->
        <div class="mb-8 pb-8 border-b border-gray-200 dark:border-slate-700">
          <h2 class="text-xl font-semibold text-gray-800 dark:text-slate-200 mb-4 flex items-center">
            <svg class="w-6 h-6 mr-2 text-green-500 dark:text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V8a2 2 0 00-2-2h-5m-4 0V5a2 2 0 114 0v1m-4 0a2 2 0 104 0m-5 8a2 2 0 100-4 2 2 0 000 4zm0 0c1.306 0 2.417.835 2.83 2M9 14a3.001 3.001 0 00-2.83 2M15 11h3m-3 4h2" />
            </svg>
            基本信息
          </h2>
          
          <div class="space-y-4">
            <!-- 用户名 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                用户名
              </label>
              <div class="flex gap-2">
                <input
                  v-model="basicInfo.username"
                  type="text"
                  :disabled="!editingBasicInfo"
                  class="flex-1 px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:opacity-50 disabled:cursor-not-allowed"
                />
                <button
                  v-if="!editingBasicInfo"
                  @click="editingBasicInfo = true"
                  class="px-4 py-2 border border-blue-600 text-blue-600 dark:border-blue-500 dark:text-blue-400 rounded-lg hover:bg-blue-50 dark:hover:bg-blue-900/20 transition-colors"
                >
                  编辑
                </button>
                <template v-else>
                  <button
                    @click="saveBasicInfo"
                    :disabled="savingBasicInfo"
                    class="px-4 py-2 bg-blue-600 hover:bg-blue-700 dark:bg-blue-700 dark:hover:bg-blue-600 text-white rounded-lg transition-colors disabled:opacity-50"
                  >
                    {{ savingBasicInfo ? '保存中' : '保存' }}
                  </button>
                  <button
                    @click="cancelEditBasicInfo"
                    class="px-4 py-2 border border-gray-300 dark:border-slate-600 text-gray-700 dark:text-slate-300 rounded-lg hover:bg-gray-50 dark:hover:bg-slate-700 transition-colors"
                  >
                    取消
                  </button>
                </template>
              </div>
            </div>

            <!-- 邮箱（只读） -->
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                邮箱
              </label>
              <input
                :value="userStore.user?.email || '未设置'"
                type="text"
                disabled
                class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg opacity-50 cursor-not-allowed"
              />
            </div>
          </div>
        </div>

        <!-- 修改密码 -->
        <div>
          <h2 class="text-xl font-semibold text-gray-800 dark:text-slate-200 mb-4 flex items-center">
            <svg class="w-6 h-6 mr-2 text-purple-500 dark:text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
            </svg>
            修改密码
          </h2>

          <form @submit.prevent="changePassword" class="space-y-4">
            <!-- 旧密码 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                当前密码 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="passwordForm.oldPassword"
                type="password"
                required
                placeholder="请输入当前密码"
                class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              />
            </div>

            <!-- 新密码 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                新密码 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="passwordForm.newPassword"
                type="password"
                required
                minlength="6"
                placeholder="请输入新密码（至少6位）"
                class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              />
            </div>

            <!-- 确认密码 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-slate-300 mb-2">
                确认新密码 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="passwordForm.confirmPassword"
                type="password"
                required
                placeholder="请再次输入新密码"
                class="w-full px-4 py-2 border border-gray-300 dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              />
            </div>

            <button
              type="submit"
              :disabled="changingPassword"
              class="px-6 py-2 bg-purple-600 hover:bg-purple-700 dark:bg-purple-700 dark:hover:bg-purple-600 text-white rounded-lg transition-colors disabled:opacity-50"
            >
              {{ changingPassword ? '修改中...' : '修改密码' }}
            </button>
          </form>
        </div>
      </div>
    </div>

    <!-- 头像裁剪器 -->
    <AvatarCropper 
      v-if="showAvatarCropper" 
      :existingAvatar="userStore.user?.avatarBase64"
      @close="showAvatarCropper = false" 
      @update="handleAvatarUpdate"
    />
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { userStore } from '../store'
import api from '../api/user'
import toast from '../utils/toast'
import AvatarCropper from '../components/AvatarCropper.vue'

export default {
  name: 'ProfileSettings',
  components: {
    AvatarCropper
  },
  setup() {
    const showAvatarCropper = ref(false)
    const editingBasicInfo = ref(false)
    const savingBasicInfo = ref(false)
    const changingPassword = ref(false)

    const basicInfo = reactive({
      username: ''
    })

    const passwordForm = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    // 初始化用户信息
    onMounted(() => {
      basicInfo.username = userStore.user?.username || ''
    })

    // 处理头像更新
    const handleAvatarUpdate = (avatarBase64) => {
      showAvatarCropper.value = false
      // userStore会自动更新
    }

    // 保存基本信息
    const saveBasicInfo = async () => {
      if (!basicInfo.username.trim()) {
        toast.error('用户名不能为空')
        return
      }

      savingBasicInfo.value = true
      try {
        const response = await api.updateUsername(basicInfo.username)
        if (response.code === 200) {
          // 更新store中的用户名
          userStore.user.username = basicInfo.username
          localStorage.setItem('user', JSON.stringify(userStore.user))
          toast.success('用户名修改成功')
          editingBasicInfo.value = false
        } else {
          toast.error(response.message || '修改失败')
        }
      } catch (error) {
        console.error('修改失败:', error)
        toast.error('修改失败，请重试')
      } finally {
        savingBasicInfo.value = false
      }
    }

    // 取消编辑基本信息
    const cancelEditBasicInfo = () => {
      basicInfo.username = userStore.user?.username || ''
      editingBasicInfo.value = false
    }

    // 修改密码
    const changePassword = async () => {
      if (passwordForm.newPassword !== passwordForm.confirmPassword) {
        toast.error('两次输入的新密码不一致')
        return
      }

      if (passwordForm.newPassword.length < 6) {
        toast.error('新密码至少6位')
        return
      }

      changingPassword.value = true
      try {
        const response = await api.changePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })

        if (response.code === 200) {
          toast.success('密码修改成功，请重新登录')
          // 清空表单
          passwordForm.oldPassword = ''
          passwordForm.newPassword = ''
          passwordForm.confirmPassword = ''
          // 2秒后退出登录
          setTimeout(() => {
            userStore.logout()
            window.location.href = '/login'
          }, 2000)
        } else {
          toast.error(response.message || '密码修改失败')
        }
      } catch (error) {
        console.error('密码修改失败:', error)
        toast.error(error.response?.data?.message || '密码修改失败，请重试')
      } finally {
        changingPassword.value = false
      }
    }

    return {
      userStore,
      showAvatarCropper,
      editingBasicInfo,
      savingBasicInfo,
      changingPassword,
      basicInfo,
      passwordForm,
      handleAvatarUpdate,
      saveBasicInfo,
      cancelEditBasicInfo,
      changePassword
    }
  }
}
</script>

