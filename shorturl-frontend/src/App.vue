<template>
  <div class="min-h-screen gradient-bg">
    <!-- 顶部导航（仅在非独立页面显示） -->
    <nav v-if="!isStandalonePage" class="fixed top-0 left-0 w-screen z-50 glass-effect border-b dark:border-slate-600 border-slate-300">
      <div class="px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16 max-w-7xl mx-auto">
          <div class="flex items-center space-x-8">
            <router-link to="/" class="flex items-center space-x-2 group">
              <div class="w-8 h-8 dark:bg-slate-700 bg-orange-100 rounded-lg flex items-center justify-center border dark:border-slate-600 border-orange-300 transition-all group-hover:scale-105">
                <span class="text-orange-500 font-bold text-xl">🍊</span>
              </div>
              <h1 class="text-xl font-bold dark:text-slate-50 text-slate-900">Orange ShortUrl</h1>
            </router-link>
            <div class="hidden md:flex space-x-1">
              <router-link 
                to="/" 
                class="dark:text-slate-300 text-slate-600 dark:hover:text-slate-50 hover:text-slate-900 dark:hover:bg-slate-700/70 hover:bg-slate-200 px-4 py-2 rounded-lg text-sm font-medium transition-all"
                :class="{'dark:bg-slate-700 dark:text-slate-50 dark:border dark:border-slate-600 bg-slate-200 text-slate-900 border border-slate-300': $route.path === '/'}">
                首页
              </router-link>
              <router-link 
                to="/shorten" 
                class="dark:text-slate-300 text-slate-600 dark:hover:text-slate-50 hover:text-slate-900 dark:hover:bg-slate-700/70 hover:bg-slate-200 px-4 py-2 rounded-lg text-sm font-medium transition-all"
                :class="{'dark:bg-slate-700 dark:text-slate-50 dark:border dark:border-slate-600 bg-slate-200 text-slate-900 border border-slate-300': $route.path === '/shorten'}">
                创建短链
              </router-link>
              <router-link 
                to="/api-docs" 
                class="dark:text-slate-300 text-slate-600 dark:hover:text-slate-50 hover:text-slate-900 dark:hover:bg-slate-700/70 hover:bg-slate-200 px-4 py-2 rounded-lg text-sm font-medium transition-all"
                :class="{'dark:bg-slate-700 dark:text-slate-50 dark:border dark:border-slate-600 bg-slate-200 text-slate-900 border border-slate-300': $route.path === '/api-docs'}">
                API文档
              </router-link>
              <router-link 
                v-if="userStore.isAdmin()"
                to="/ua-blacklist" 
                class="dark:text-slate-300 text-slate-600 dark:hover:text-slate-50 hover:text-slate-900 dark:hover:bg-slate-700/70 hover:bg-slate-200 px-4 py-2 rounded-lg text-sm font-medium transition-all flex items-center gap-1"
                :class="{'dark:bg-slate-700 dark:text-slate-50 dark:border dark:border-slate-600 bg-slate-200 text-slate-900 border border-slate-300': $route.path === '/ua-blacklist'}">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728A9 9 0 015.636 5.636m12.728 12.728L5.636 5.636"></path>
                </svg>
                UA屏蔽
              </router-link>
              <router-link 
                v-if="userStore.isAdmin()"
                to="/ai-config" 
                class="dark:text-slate-300 text-slate-600 dark:hover:text-slate-50 hover:text-slate-900 dark:hover:bg-slate-700/70 hover:bg-slate-200 px-4 py-2 rounded-lg text-sm font-medium transition-all flex items-center gap-1"
                :class="{'dark:bg-slate-700 dark:text-slate-50 dark:border dark:border-slate-600 bg-slate-200 text-slate-900 border border-slate-300': $route.path === '/ai-config'}">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                </svg>
                AI配置
              </router-link>
            </div>
          </div>

          <!-- 右侧功能区 -->
          <div class="flex items-center space-x-3">
            <!-- 移动端菜单按钮 -->
            <button 
              @click="toggleMobileMenu"
              class="md:hidden p-2 dark:hover:bg-slate-700 hover:bg-slate-200 rounded-lg transition-all border dark:border-slate-700 border-slate-300"
              title="菜单">
              <svg class="w-6 h-6 dark:text-slate-300 text-slate-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
              </svg>
            </button>
            
            <!-- 主题切换按钮 -->
            <button 
              @click="toggleTheme" 
              class="p-2 dark:hover:bg-slate-700 hover:bg-slate-200 rounded-lg transition-all border dark:border-slate-700 border-slate-300 dark:hover:border-slate-600 hover:border-slate-400"
              title="切换主题">
              <svg v-if="themeStore.isDark" class="w-5 h-5 text-yellow-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"></path>
              </svg>
              <svg v-else class="w-5 h-5 text-slate-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"></path>
              </svg>
            </button>

            <!-- 用户信息 -->
            <div v-if="userStore.isLoggedIn()" class="relative">
              <button 
                @click="toggleUserMenu"
                class="flex items-center space-x-2 px-3 py-1.5 dark:bg-slate-700/70 bg-slate-200 rounded-lg border dark:border-slate-600 border-slate-300 hover:dark:bg-slate-700 hover:bg-slate-300 transition-all cursor-pointer">
                <img 
                  v-if="userStore.user.avatarBase64" 
                  :src="userStore.user.avatarBase64" 
                  class="w-7 h-7 rounded-full object-cover ring-2 dark:ring-slate-600 ring-slate-300" 
                  alt="头像"
                />
                <div v-else class="w-7 h-7 bg-gradient-to-br from-orange-500 to-orange-600 rounded-full flex items-center justify-center text-white font-bold text-sm shadow-md">
                  {{ userStore.user.username.charAt(0).toUpperCase() }}
                </div>
                <span class="text-sm font-medium dark:text-slate-50 text-slate-900">
                  {{ userStore.user.username }}
                </span>
                <span 
                  class="text-xs px-2 py-0.5 rounded-full font-medium"
                  :class="userStore.user.role === 'admin' 
                    ? 'bg-gradient-to-r from-amber-400 to-orange-500 text-white shadow-sm' 
                    : 'dark:bg-slate-600 bg-slate-300 dark:text-slate-200 text-slate-700'">
                  {{ userStore.user.role === 'admin' ? '站长' : '用户' }}
                </span>
                <svg 
                  class="w-4 h-4 dark:text-slate-400 text-slate-600 transition-transform"
                  :class="{'rotate-180': showUserMenu}"
                  fill="none" 
                  stroke="currentColor" 
                  viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
              </button>

              <!-- 下拉菜单 -->
              <transition name="dropdown">
                <div 
                  v-if="showUserMenu"
                  v-click-outside="closeUserMenu"
                  class="absolute right-0 mt-2 w-80 glass-effect rounded-2xl border-2 dark:border-slate-600 border-slate-300 shadow-2xl overflow-hidden z-50">
                  
                  <!-- 用户信息头部 - 渐变背景 -->
                  <div class="relative px-6 py-5 bg-gradient-to-br from-orange-500 to-orange-600 dark:from-orange-600 dark:to-orange-700">
                    <!-- 背景装饰 -->
                    <div class="absolute inset-0 opacity-10">
                      <div class="absolute top-0 right-0 w-32 h-32 bg-white rounded-full -mr-16 -mt-16"></div>
                      <div class="absolute bottom-0 left-0 w-24 h-24 bg-white rounded-full -ml-12 -mb-12"></div>
                    </div>
                    
                    <div class="relative flex items-center space-x-4">
                      <div class="relative group">
                        <img 
                          v-if="userStore.user.avatarBase64" 
                          :src="userStore.user.avatarBase64" 
                          class="w-16 h-16 rounded-full object-cover ring-4 ring-white/30 shadow-lg" 
                          alt="头像"
                        />
                        <div v-else class="w-16 h-16 bg-white rounded-full flex items-center justify-center text-orange-600 font-bold text-2xl shadow-lg ring-4 ring-white/30">
                          {{ userStore.user.username.charAt(0).toUpperCase() }}
                        </div>
                      </div>
                      <div class="flex-1 min-w-0">
                        <p class="text-base font-bold text-white mb-1 truncate drop-shadow-sm flex items-center gap-2">
                          {{ userStore.user.username }}
                          <span 
                            class="text-xs px-2 py-0.5 rounded-full font-medium"
                            :class="userStore.user.role === 'admin' 
                              ? 'bg-white/90 text-orange-600 shadow-sm' 
                              : 'bg-white/20 text-white'">
                            {{ userStore.user.role === 'admin' ? '站长' : '用户' }}
                          </span>
                        </p>
                        <p class="text-sm text-orange-50 truncate">{{ userStore.user.email || '未绑定邮箱' }}</p>
                      </div>
                    </div>
                  </div>

                  <!-- 菜单项 -->
                  <div class="py-2">
                    <router-link 
                      to="/profile"
                      @click="closeUserMenu"
                      class="w-full text-left px-5 py-3 text-sm dark:text-slate-200 text-slate-700 dark:hover:bg-slate-700/70 hover:bg-orange-50 transition-all flex items-center space-x-3 group">
                      <div class="w-9 h-9 rounded-lg dark:bg-slate-700 bg-orange-100 flex items-center justify-center group-hover:scale-110 transition-transform">
                        <svg class="w-5 h-5 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path>
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                        </svg>
                      </div>
                      <span class="font-medium">个人设置</span>
                    </router-link>
                    
                    <router-link 
                      to="/manage"
                      @click="closeUserMenu"
                      class="block px-5 py-3 text-sm dark:text-slate-200 text-slate-700 dark:hover:bg-slate-700/70 hover:bg-orange-50 transition-all group">
                      <div class="flex items-center space-x-3">
                        <div class="w-9 h-9 rounded-lg dark:bg-slate-700 bg-orange-100 flex items-center justify-center group-hover:scale-110 transition-transform">
                          <svg class="w-5 h-5 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"></path>
                          </svg>
                        </div>
                        <span class="font-medium">链接管理</span>
                      </div>
                    </router-link>

                    <router-link 
                      to="/url-groups"
                      @click="closeUserMenu"
                      class="block px-5 py-3 text-sm dark:text-slate-200 text-slate-700 dark:hover:bg-slate-700/70 hover:bg-orange-50 transition-all group">
                      <div class="flex items-center space-x-3">
                        <div class="w-9 h-9 rounded-lg dark:bg-slate-700 bg-orange-100 flex items-center justify-center group-hover:scale-110 transition-transform">
                          <svg class="w-5 h-5 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
                          </svg>
                        </div>
                        <span class="font-medium">链接分组</span>
                      </div>
                    </router-link>
                    
                    <router-link 
                      to="/visit-logs"
                      @click="closeUserMenu"
                      class="block px-5 py-3 text-sm dark:text-slate-200 text-slate-700 dark:hover:bg-slate-700/70 hover:bg-orange-50 transition-all group">
                      <div class="flex items-center space-x-3">
                        <div class="w-9 h-9 rounded-lg dark:bg-slate-700 bg-orange-100 flex items-center justify-center group-hover:scale-110 transition-transform">
                          <svg class="w-5 h-5 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
                          </svg>
                        </div>
                        <span class="font-medium">访问日志</span>
                      </div>
                    </router-link>
                    
                    <router-link 
                      to="/transit-page"
                      @click="closeUserMenu"
                      class="block px-5 py-3 text-sm dark:text-slate-200 text-slate-700 dark:hover:bg-slate-700/70 hover:bg-orange-50 transition-all group">
                      <div class="flex items-center space-x-3">
                        <div class="w-9 h-9 rounded-lg dark:bg-slate-700 bg-orange-100 flex items-center justify-center group-hover:scale-110 transition-transform">
                          <svg class="w-5 h-5 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                          </svg>
                        </div>
                        <span class="font-medium">自定义中转页</span>
                      </div>
                    </router-link>
                  </div>

                  <!-- 登出按钮 -->
                  <div class="border-t-2 dark:border-slate-700 border-slate-200 pt-2 mt-2 px-2 pb-2">
                    <button 
                      @click="handleLogout"
                      class="w-full text-left px-4 py-3 text-sm text-red-500 dark:text-red-400 dark:hover:bg-red-900/20 hover:bg-red-50 rounded-xl transition-all flex items-center space-x-3 group font-medium">
                      <div class="w-9 h-9 rounded-lg bg-red-100 dark:bg-red-900/30 flex items-center justify-center group-hover:scale-110 transition-transform">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                        </svg>
                      </div>
                      <span>退出登录</span>
                    </button>
                  </div>
                </div>
              </transition>
            </div>
            
            <!-- 登录按钮 -->
            <router-link 
              v-else 
              to="/login" 
              class="px-4 py-2 bg-orange-600 hover:bg-orange-500 dark:bg-orange-600 dark:hover:bg-orange-500 text-white text-sm font-semibold rounded-lg transition-all shadow-sm hover:shadow-md border dark:border-orange-600 border-orange-500">
              登录
            </router-link>
          </div>
        </div>
      </div>
      
      <!-- 移动端菜单面板 -->
      <transition name="mobile-menu">
        <div 
          v-if="showMobileMenu && !isStandalonePage"
          class="md:hidden absolute top-16 left-0 right-0 glass-effect border-b dark:border-slate-600 border-slate-300 shadow-lg">
          <div class="px-4 py-4 space-y-2">
            <router-link 
              to="/" 
              @click="closeMobileMenu"
              class="block dark:text-slate-300 text-slate-600 dark:hover:bg-slate-700/70 hover:bg-slate-200 px-4 py-3 rounded-lg text-sm font-medium transition-all"
              :class="{'dark:bg-slate-700 dark:text-slate-50 bg-slate-200 text-slate-900': $route.path === '/'}">
              首页
            </router-link>
            <router-link 
              to="/shorten" 
              @click="closeMobileMenu"
              class="block dark:text-slate-300 text-slate-600 dark:hover:bg-slate-700/70 hover:bg-slate-200 px-4 py-3 rounded-lg text-sm font-medium transition-all"
              :class="{'dark:bg-slate-700 dark:text-slate-50 bg-slate-200 text-slate-900': $route.path === '/shorten'}">
              创建短链
            </router-link>
            <router-link 
              to="/api-docs" 
              @click="closeMobileMenu"
              class="block dark:text-slate-300 text-slate-600 dark:hover:bg-slate-700/70 hover:bg-slate-200 px-4 py-3 rounded-lg text-sm font-medium transition-all"
              :class="{'dark:bg-slate-700 dark:text-slate-50 bg-slate-200 text-slate-900': $route.path === '/api-docs'}">
              API文档
            </router-link>
            
            <!-- 移动端登录按钮 -->
            <router-link 
              v-if="!userStore.isLoggedIn()" 
              to="/login" 
              @click="closeMobileMenu"
              class="block px-4 py-3 bg-orange-600 hover:bg-orange-500 text-white text-sm font-semibold rounded-lg transition-all text-center">
              登录
            </router-link>
          </div>
        </div>
      </transition>
    </nav>

    <!-- 主要内容 -->
    <main :class="{'pt-16': !isStandalonePage}">
      <router-view />
    </main>

    <!-- 页脚（仅在非独立页面显示） -->
    <footer v-if="!isStandalonePage" class="border-t dark:border-slate-700 border-slate-300 py-8 mt-12 dark:bg-slate-900/30 bg-white/30">
      <div class="max-w-7xl mx-auto px-4 text-center">
        <p class="dark:text-slate-400 text-slate-700 mb-2 font-medium">
          © 2025 Orange ShortUrl - 简洁、高效、易用
        </p>
        <p class="dark:text-slate-500 text-slate-600 text-sm">
          Made with ❤️ by 
          <a href="https://github.com/djteang/OrangeShortUrl" target="_blank" class="text-orange-500 hover:text-orange-400 transition-colors font-medium">
            程序员橙子
          </a>
        </p>
        <div class="mt-3">
          <a href="https://github.com/djteang/OrangeShortUrl" target="_blank" class="inline-flex items-center dark:text-slate-400 text-slate-700 dark:hover:text-slate-200 hover:text-slate-900 transition-all px-3 py-1.5 rounded-lg dark:hover:bg-slate-800/50 hover:bg-slate-200/50">
            <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
              <path fill-rule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" clip-rule="evenodd" />
            </svg>
            GitHub
          </a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { userStore, themeStore } from './store'
import toast from './utils/toast'
import userApi from './api/user'

const router = useRouter()
const route = useRoute()
const showUserMenu = ref(false)
const showMobileMenu = ref(false)

// 判断是否是独立页面（不需要导航和页脚）
const isStandalonePage = computed(() => {
  return route.meta.standalone === true
})

// 监听路由变化，自动关闭移动端菜单
watch(() => route.path, () => {
  closeMobileMenu()
})

const toggleTheme = () => {
  themeStore.toggle()
}

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
}

const closeUserMenu = () => {
  showUserMenu.value = false
}

const toggleMobileMenu = () => {
  showMobileMenu.value = !showMobileMenu.value
}

const closeMobileMenu = () => {
  showMobileMenu.value = false
}

const handleLogout = async () => {
  try {
    // 调用后端登出接口，清除Redis中的Token
    await userApi.logout()
  } catch (err) {
    console.error('登出失败:', err)
  } finally {
    // 无论后端是否成功，都清除前端登录状态
    userStore.logout()
    closeUserMenu()
    router.push('/')
    toast.success('已退出登录')
  }
}

// 点击外部关闭下拉菜单的指令
const vClickOutside = {
  mounted(el, binding) {
    el.clickOutsideEvent = (event) => {
      // 检查点击是否在元素外部
      if (!(el === event.target || el.contains(event.target))) {
        binding.value()
      }
    }
    // 延迟添加监听器，避免立即触发
    setTimeout(() => {
      document.addEventListener('click', el.clickOutsideEvent)
    }, 100)
  },
  unmounted(el) {
    document.removeEventListener('click', el.clickOutsideEvent)
  }
}
</script>

<style scoped>
/* 下拉菜单动画 */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.dropdown-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 移动端菜单动画 */
.mobile-menu-enter-active,
.mobile-menu-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.mobile-menu-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.mobile-menu-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>

