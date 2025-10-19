<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="py-20 px-4">
      <div class="max-w-6xl mx-auto text-center">
        <div class="mb-8">
          <h1 class="text-5xl md:text-6xl font-bold dark:text-slate-50 text-slate-900 mb-6 drop-shadow-sm">
            Orange ShortUrl
          </h1>
          <p class="text-xl md:text-2xl dark:text-slate-300 text-slate-700 mb-8 font-medium">
            简洁、高效、易用的短链接服务
          </p>
          <p class="text-lg dark:text-slate-400 text-slate-600 max-w-2xl mx-auto">
            快速将长URL转换为简短易记的链接，支持自定义短码、访问统计、二维码生成等功能
          </p>
        </div>

        <div class="flex flex-col sm:flex-row gap-4 justify-center mb-12">
          <router-link 
            to="/shorten" 
            class="px-8 py-4 bg-orange-600 hover:bg-orange-500 text-white rounded-lg font-semibold transition-all transform hover:scale-105 shadow-lg hover:shadow-xl border border-orange-600">
            开始使用
          </router-link>
          <router-link 
            to="/api-docs" 
            class="px-8 py-4 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 rounded-lg font-semibold transition-all transform hover:scale-105 border dark:border-slate-600 border-slate-400 shadow-lg hover:shadow-xl">
            查看文档
          </router-link>
        </div>

        <!-- 示例展示 -->
        <div class="max-w-3xl mx-auto mt-16 glass-effect rounded-2xl p-8 transition-all hover:shadow-lg border dark:border-slate-700 border-slate-300">
          <div class="flex flex-col md:flex-row items-center justify-between gap-6">
            <div class="flex-1 text-left">
              <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-semibold">长链接</p>
              <p class="dark:text-slate-200 text-slate-800 break-all font-medium">https://orange-shorturl.czdgg.top/api-docs</p>
            </div>
            <div class="flex-shrink-0">
              <svg class="w-8 h-8 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6"></path>
              </svg>
            </div>
            <div class="flex-1 text-left">
              <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-semibold">短链接</p>
              <p class="text-orange-600 dark:text-orange-400 font-bold">https://orange-shorturl.czdgg.top/r/v02yHF9</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="py-20 px-4">
      <div class="max-w-6xl mx-auto">
        <h2 class="text-3xl md:text-4xl font-bold text-center dark:text-slate-50 text-slate-900 mb-16">
          核心功能
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <!-- Feature 1 -->
          <div class="glass-effect rounded-2xl p-8 border-2 dark:border-slate-700 border-slate-300 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-xl transform hover:-translate-y-1">
            <div class="w-14 h-14 dark:bg-slate-700 bg-orange-100 rounded-lg flex items-center justify-center mb-4 transition-all border dark:border-slate-600 border-orange-200">
              <svg class="w-7 h-7 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900 mb-3">快速生成</h3>
            <p class="dark:text-slate-300 text-slate-700">毫秒级生成短链接，支持批量处理，让您的工作更高效</p>
          </div>

          <!-- Feature 2 -->
          <div class="glass-effect rounded-2xl p-8 border-2 dark:border-slate-700 border-slate-300 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-xl transform hover:-translate-y-1">
            <div class="w-14 h-14 dark:bg-slate-700 bg-orange-100 rounded-lg flex items-center justify-center mb-4 transition-all border dark:border-slate-600 border-orange-200">
              <svg class="w-7 h-7 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900 mb-3">自定义短码</h3>
            <p class="dark:text-slate-300 text-slate-700">支持自定义短码，让您的链接更具品牌特色和易记性</p>
          </div>

          <!-- Feature 3 -->
          <div class="glass-effect rounded-2xl p-8 border-2 dark:border-slate-700 border-slate-300 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-xl transform hover:-translate-y-1">
            <div class="w-14 h-14 dark:bg-slate-700 bg-orange-100 rounded-lg flex items-center justify-center mb-4 transition-all border dark:border-slate-600 border-orange-200">
              <svg class="w-7 h-7 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900 mb-3">访问统计</h3>
            <p class="dark:text-slate-300 text-slate-700">详细的访问数据分析，包含访问次数、来源、时间趋势等</p>
          </div>

          <!-- Feature 4 -->
          <div class="glass-effect rounded-2xl p-8 border-2 dark:border-slate-700 border-slate-300 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-xl transform hover:-translate-y-1">
            <div class="w-14 h-14 dark:bg-slate-700 bg-orange-100 rounded-lg flex items-center justify-center mb-4 transition-all border dark:border-slate-600 border-orange-200">
              <svg class="w-7 h-7 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900 mb-3">二维码生成</h3>
            <p class="dark:text-slate-300 text-slate-700">自动生成二维码，方便移动端扫码访问和分享</p>
          </div>

          <!-- Feature 5 -->
          <div class="glass-effect rounded-2xl p-8 border-2 dark:border-slate-700 border-slate-300 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-xl transform hover:-translate-y-1">
            <div class="w-14 h-14 dark:bg-slate-700 bg-orange-100 rounded-lg flex items-center justify-center mb-4 transition-all border dark:border-slate-600 border-orange-200">
              <svg class="w-7 h-7 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900 mb-3">过期时间</h3>
            <p class="dark:text-slate-300 text-slate-700">支持设置链接过期时间，自动管理临时链接</p>
          </div>

          <!-- Feature 6 -->
          <div class="glass-effect rounded-2xl p-8 border-2 dark:border-slate-700 border-slate-300 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-xl transform hover:-translate-y-1">
            <div class="w-14 h-14 dark:bg-slate-700 bg-orange-100 rounded-lg flex items-center justify-center mb-4 transition-all border dark:border-slate-600 border-orange-200">
              <svg class="w-7 h-7 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900 mb-3">API接口</h3>
            <p class="dark:text-slate-300 text-slate-700">提供完整的RESTful API，轻松集成到您的应用中</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Pricing Section -->
    <section class="py-20 px-4">
      <div class="max-w-6xl mx-auto">
        <h2 class="text-3xl md:text-4xl font-bold text-center dark:text-slate-50 text-slate-900 mb-4">
          选择您的计划
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mt-12">
          <!-- 免费版 -->
          <div class="glass-effect rounded-2xl p-8 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-lg transform hover:-translate-y-1 border dark:border-slate-700 border-slate-300">
            <div class="text-center mb-6">
              <div class="w-16 h-16 dark:bg-green-900/30 bg-green-100 rounded-full mx-auto mb-4 flex items-center justify-center shadow-sm border dark:border-green-700 border-green-300">
                <svg class="w-8 h-8 text-green-600 dark:text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
              </div>
              <h3 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-2">免费版</h3>
            </div>
            <ul class="space-y-3 mb-8">
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                无限创建短链接
              </li>
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                基本点击统计
              </li>
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                支持自定义短码
              </li>
              <li class="flex items-center dark:text-orange-300 text-orange-600">
                <svg class="w-5 h-5 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                </svg>
                轻度速率限制
              </li>
            </ul>
            <div class="text-center">
              <div class="text-4xl font-bold dark:text-green-400 text-green-600 mb-4">$0<span class="text-lg dark:text-slate-400 text-slate-600"> / 月</span></div>
              <router-link to="/shorten" class="block w-full dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 py-3 rounded-lg font-semibold transition-all shadow-md hover:shadow-lg border dark:border-slate-600 border-slate-400">
                立即开始
              </router-link>
            </div>
          </div>

          <!-- 注册用户套餐 - 推荐 -->
          <div class="glass-effect rounded-2xl p-10 border-3 border-orange-500 hover:border-orange-400 dark:hover:border-orange-400 transition-all relative transform hover:scale-105 shadow-2xl dark:shadow-orange-500/30 shadow-orange-500/40">
            <div class="absolute -top-6 left-1/2 transform -translate-x-1/2">
              <span class="bg-gradient-to-r from-orange-500 to-orange-600 text-white px-6 py-2 rounded-full text-base font-bold shadow-lg animate-pulse">⭐ 推荐 ⭐</span>
            </div>
            <div class="text-center mb-6">
              <div class="w-20 h-20 bg-gradient-to-r from-orange-500 to-orange-600 rounded-full mx-auto mb-6 flex items-center justify-center shadow-lg">
                <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                </svg>
              </div>
              <h3 class="text-3xl font-bold dark:text-slate-50 text-slate-900 mb-2">注册用户套餐</h3>
            </div>
            <ul class="space-y-3 mb-8">
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                个人链接管理面板
              </li>
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                详细访问统计
              </li>
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                无限自定义短码
              </li>
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                自定义中转页设计
              </li>
            </ul>
            <div class="text-center">
              <div class="text-5xl font-bold text-orange-600 dark:text-orange-500 mb-6">$0<span class="text-xl dark:text-slate-400 text-slate-600"> / 月</span></div>
              <button 
                @click="handleRegisterClick"
                class="block w-full bg-gradient-to-r from-orange-500 to-orange-600 hover:from-orange-600 hover:to-orange-700 text-white py-4 rounded-lg font-bold text-lg transition-all transform hover:scale-105 shadow-xl hover:shadow-2xl">
                ✨ 立即注册 ✨
              </button>
            </div>
          </div>

          <!-- 自建用户套餐 -->
          <div class="glass-effect rounded-2xl p-8 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-lg transform hover:-translate-y-1 border dark:border-slate-700 border-slate-300">
            <div class="text-center mb-6">
              <div class="w-16 h-16 dark:bg-purple-900/30 bg-purple-100 rounded-full mx-auto mb-4 flex items-center justify-center shadow-sm border dark:border-purple-700 border-purple-300">
                <svg class="w-8 h-8 dark:text-purple-400 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path>
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                </svg>
              </div>
              <h3 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-2">自建用户套餐</h3>
            </div>
            <ul class="space-y-3 mb-8">
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                完全数据控制权
              </li>
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                一键自托管部署
              </li>
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                自由扩展功能
              </li>
              <li class="flex items-center dark:text-slate-200 text-slate-800">
                <svg class="w-5 h-5 dark:text-green-400 text-green-600 mr-3 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                100% 开源免费
              </li>
            </ul>
            <div class="text-center">
              <div class="text-4xl font-bold dark:text-purple-400 text-purple-700 mb-4">$0<span class="text-lg dark:text-slate-400 text-slate-600"> / 月</span></div>
              <a href="https://github.com/djteang/OrangeShortUrl" target="_blank" class="block w-full dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 py-3 rounded-lg font-semibold transition-all shadow-md hover:shadow-lg border dark:border-slate-600 border-slate-400">
                Fork 项目
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- User Reviews Section -->
    <section class="py-20 px-4">
      <div class="max-w-6xl mx-auto">
        <h2 class="text-3xl md:text-4xl font-bold text-center dark:text-slate-50 text-slate-900 mb-4">
          用户评价
        </h2>
        <p class="text-center dark:text-slate-400 text-slate-600 mb-12">
          看看其他用户怎么说
        </p>
        
        <div v-if="loading" class="text-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-4 dark:border-slate-700 border-slate-300 border-t-orange-500"></div>
        </div>
        
        <!-- 根据评价数量和设备类型决定显示方式 -->
        <div v-else-if="reviews.length > 0">
          <!-- 评价数量较少或不需要自动滚动时使用grid布局 -->
          <div v-if="!shouldAutoScroll" class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div v-for="review in reviews" :key="review.id" class="glass-effect rounded-2xl p-6 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-lg transform hover:-translate-y-1 border dark:border-slate-700 border-slate-300">
              <div class="flex items-center mb-4">
                <img 
                  v-if="review.avatarBase64" 
                  :src="review.avatarBase64" 
                  class="w-12 h-12 rounded-full mr-3 object-cover ring-2 dark:ring-slate-700 ring-slate-300" 
                  alt="用户头像"
                />
                <div v-else class="w-12 h-12 bg-gradient-to-br from-orange-500 to-orange-600 rounded-full mr-3 flex items-center justify-center text-white font-bold shadow-md">
                  {{ review.username.charAt(0).toUpperCase() }}
                </div>
                <div class="flex-1">
                  <div class="font-semibold dark:text-slate-50 text-slate-900">{{ review.username }}</div>
                  <div class="flex dark:text-yellow-400 text-yellow-500">
                    <svg v-for="i in review.rating" :key="i" class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path>
                    </svg>
                  </div>
                </div>
              </div>
              <p class="dark:text-slate-300 text-slate-700 text-sm leading-relaxed">
                "{{ review.content }}"
              </p>
            </div>
          </div>
          
          <!-- 需要自动滚动时使用横向滚动（隐藏滚动条） -->
          <div v-else class="relative overflow-hidden">
            <div 
              ref="reviewsContainer"
              class="flex space-x-6 px-1 overflow-x-hidden"
              @mouseenter="pauseAutoScroll"
              @mouseleave="resumeAutoScroll">
              <!-- 原始评价列表 -->
              <div v-for="review in displayedReviews" :key="review.id + '-' + review.index" class="flex-shrink-0 w-80 glass-effect rounded-2xl p-6 hover:border-orange-500 dark:hover:border-orange-500 transition-all hover:shadow-lg transform hover:-translate-y-1 border dark:border-slate-700 border-slate-300">
                <div class="flex items-center mb-4">
                  <img 
                    v-if="review.avatarBase64" 
                    :src="review.avatarBase64" 
                    class="w-12 h-12 rounded-full mr-3 object-cover ring-2 dark:ring-slate-700 ring-slate-300" 
                    alt="用户头像"
                  />
                  <div v-else class="w-12 h-12 bg-gradient-to-br from-orange-500 to-orange-600 rounded-full mr-3 flex items-center justify-center text-white font-bold shadow-md">
                    {{ review.username.charAt(0).toUpperCase() }}
                  </div>
                  <div class="flex-1">
                    <div class="font-semibold dark:text-slate-50 text-slate-900">{{ review.username }}</div>
                    <div class="flex dark:text-yellow-400 text-yellow-500">
                      <svg v-for="i in review.rating" :key="i" class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path>
                      </svg>
                    </div>
                  </div>
                </div>
                <p class="dark:text-slate-300 text-slate-700 text-sm leading-relaxed">
                  "{{ review.content }}"
                </p>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-12">
          <p class="dark:text-slate-400 text-slate-600 mb-4">暂无评价，成为第一个评价的用户吧！</p>
        </div>
        
        <!-- 评价按钮 - 始终显示 -->
        <div class="text-center mt-8">
          <button 
            v-if="userStore.isLoggedIn()"
            @click="scrollToReviewForm"
            class="inline-flex items-center px-8 py-3 bg-gradient-to-r from-orange-500 to-orange-600 hover:from-orange-600 hover:to-orange-700 text-white rounded-xl font-semibold transition-all shadow-lg hover:shadow-xl transform hover:scale-105">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
            </svg>
            写下您的评价
          </button>
          <router-link 
            v-else
            to="/login" 
            class="inline-flex items-center px-8 py-3 bg-gradient-to-r from-orange-500 to-orange-600 hover:from-orange-600 hover:to-orange-700 text-white rounded-xl font-semibold transition-all shadow-lg hover:shadow-xl transform hover:scale-105">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1"></path>
            </svg>
            登录并评价
          </router-link>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="py-20 px-4">
      <div class="max-w-4xl mx-auto text-center">
        <h2 class="text-3xl md:text-4xl font-bold dark:text-slate-50 text-slate-900 mb-6">
          准备好开始了吗？
        </h2>
        <p class="text-xl dark:text-slate-300 text-slate-700 mb-8">
          立即体验 Orange ShortUrl，让您的链接管理更加简单
        </p>
        <router-link 
          to="/shorten" 
          class="inline-block px-10 py-4 bg-orange-600 hover:bg-orange-500 text-white rounded-lg font-semibold transition-all transform hover:scale-105 shadow-lg hover:shadow-xl border border-orange-600">
          免费开始使用
        </router-link>
      </div>
    </section>

    <!-- 评价弹窗 -->
    <transition name="dialog">
      <div v-if="showReviewDialog" class="fixed inset-0 z-50 flex items-center justify-center p-4" @click="closeReviewDialog">
        <div class="absolute inset-0 bg-black bg-opacity-50 backdrop-blur-sm"></div>
        <div class="relative glass-effect rounded-2xl border dark:border-slate-600 border-slate-300 shadow-2xl max-w-md w-full p-6" @click.stop>
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-2xl font-bold dark:text-slate-50 text-slate-900">
              写下您的评价
            </h3>
            <button @click="closeReviewDialog" class="p-2 dark:hover:bg-slate-700 hover:bg-slate-200 rounded-lg transition-all">
              <svg class="w-6 h-6 dark:text-slate-400 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <!-- 评分 -->
          <div class="mb-4">
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">评分</label>
            <div class="flex items-center space-x-2">
              <button 
                v-for="star in 5" 
                :key="star"
                @click="reviewForm.rating = star"
                class="text-3xl transition-all">
                <span :class="star <= reviewForm.rating ? 'text-yellow-400' : 'dark:text-slate-600 text-slate-300'">★</span>
              </button>
            </div>
          </div>

          <!-- 评价内容 -->
          <div class="mb-6">
            <label class="block text-sm font-semibold dark:text-slate-300 text-slate-700 mb-2">评价内容</label>
            <textarea
              v-model="reviewForm.content"
              rows="4"
              class="w-full px-4 py-3 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all dark:placeholder-slate-500 placeholder-slate-400"
              placeholder="分享您的使用体验..."
            ></textarea>
          </div>

          <!-- 按钮 -->
          <div class="flex space-x-3">
            <button
              @click="closeReviewDialog"
              class="flex-1 px-4 py-3 dark:bg-slate-700 dark:hover:bg-slate-600 bg-slate-200 hover:bg-slate-300 dark:text-slate-50 text-slate-900 rounded-lg transition-all font-medium">
              取消
            </button>
            <button
              @click="submitReview"
              :disabled="submitting"
              class="flex-1 px-4 py-3 bg-orange-600 hover:bg-orange-500 text-white rounded-lg transition-all font-medium shadow-sm hover:shadow-md disabled:opacity-50 disabled:cursor-not-allowed">
              {{ submitting ? '提交中...' : '提交评价' }}
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { userStore } from '../store'
import userApi from '../api/user'
import toast from '../utils/toast'

const router = useRouter()
const reviews = ref([])
const loading = ref(false)
const showReviewDialog = ref(false)
const reviewForm = ref({
  rating: 5,
  content: ''
})
const submitting = ref(false)
const reviewsContainer = ref(null)
const scrollInterval = ref(null)
const isPaused = ref(false)

// 检测是否为移动端
const isMobile = ref(false)
const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
}

// 判断是否需要自动滚动
const shouldAutoScroll = computed(() => {
  if (isMobile.value) {
    return reviews.value.length > 2
  } else {
    return reviews.value.length > 3
  }
})

// 用于显示的评价列表（复制多份以实现无缝滚动）
const displayedReviews = computed(() => {
  if (!shouldAutoScroll.value) return reviews.value
  
  // 复制3份评价列表以实现无缝循环
  const tripled = []
  for (let i = 0; i < 3; i++) {
    reviews.value.forEach((review, index) => {
      tripled.push({ ...review, index: i * reviews.value.length + index })
    })
  }
  return tripled
})

onMounted(async () => {
  loading.value = true
  try {
    const response = await userApi.getReviewList(6)
    if (response.code === 200) {
      reviews.value = response.data || []
      
      // 检测设备类型
      checkMobile()
      window.addEventListener('resize', checkMobile)
      
      // 如果需要自动滚动，启动滚动
      if (shouldAutoScroll.value) {
        nextTick(() => {
          startAutoScroll()
        })
      }
    }
  } catch (err) {
    console.error('加载评价失败', err)
  } finally {
    loading.value = false
  }
})

onUnmounted(() => {
  stopAutoScroll()
  window.removeEventListener('resize', checkMobile)
})

// 启动自动滚动
const startAutoScroll = () => {
  if (!reviewsContainer.value) {
    setTimeout(startAutoScroll, 100)
    return
  }
  
  scrollInterval.value = setInterval(() => {
    if (!isPaused.value && reviewsContainer.value) {
      const container = reviewsContainer.value
      const scrollAmount = 1 // 每次滚动的像素
      
      container.scrollLeft += scrollAmount
      
      // 当滚动到第一份列表的末尾时，重置到开始位置（无缝循环）
      const maxScroll = container.scrollWidth / 3
      if (container.scrollLeft >= maxScroll) {
        container.scrollLeft = 0
      }
    }
  }, 20) // 每20ms滚动一次，实现平滑效果
}

// 停止自动滚动
const stopAutoScroll = () => {
  if (scrollInterval.value) {
    clearInterval(scrollInterval.value)
    scrollInterval.value = null
  }
}

// 暂停自动滚动
const pauseAutoScroll = () => {
  isPaused.value = true
}

// 恢复自动滚动
const resumeAutoScroll = () => {
  isPaused.value = false
}


const scrollToReviewForm = () => {
  showReviewDialog.value = true
}

const closeReviewDialog = () => {
  showReviewDialog.value = false
  reviewForm.value = {
    rating: 5,
    content: ''
  }
}

const handleRegisterClick = () => {
  if (userStore.isLoggedIn()) {
    toast.success('您已经登录了，可以直接使用所有功能！')
  } else {
    router.push('/login')
  }
}

const submitReview = async () => {
  if (!reviewForm.value.content.trim()) {
    toast.error('请输入评价内容')
    return
  }

  submitting.value = true
  try {
    const response = await userApi.submitReview(reviewForm.value)
    if (response.code === 200) {
      toast.success('评价提交成功！')
      closeReviewDialog()
      // 重新加载评价列表
      const listResponse = await userApi.getReviewList(6)
      if (listResponse.code === 200) {
        reviews.value = listResponse.data || []
        
        // 如果之前在自动滚动，先停止再重启
        if (shouldAutoScroll.value) {
          stopAutoScroll()
          nextTick(() => {
            setTimeout(() => {
              startAutoScroll()
            }, 100)
          })
        }
      }
    } else {
      toast.error(response.message || '评价提交失败')
    }
  } catch (err) {
    toast.error(err.response?.data?.message || '评价提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}
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

.dialog-enter-active .glass-effect,
.dialog-leave-active .glass-effect {
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.dialog-enter-from .glass-effect {
  transform: scale(0.9) translateY(-20px);
}

.dialog-leave-to .glass-effect {
  transform: scale(0.9) translateY(-20px);
}

/* 自定义滚动条样式 */
.scrollbar-custom {
  scrollbar-width: thin;
  scrollbar-color: #f97316 transparent;
}

.scrollbar-custom::-webkit-scrollbar {
  height: 8px;
}

.scrollbar-custom::-webkit-scrollbar-track {
  background: transparent;
  border-radius: 10px;
}

.scrollbar-custom::-webkit-scrollbar-thumb {
  background: linear-gradient(90deg, #f97316 0%, #fb923c 100%);
  border-radius: 10px;
}

.scrollbar-custom::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(90deg, #ea580c 0%, #f97316 100%);
}

/* 暗色主题下的滚动条 */
:global(.dark) .scrollbar-custom::-webkit-scrollbar-thumb {
  background: linear-gradient(90deg, #ea580c 0%, #f97316 100%);
}
</style>

