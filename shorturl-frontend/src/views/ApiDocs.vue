<template>
  <div class="min-h-screen px-4 py-12">
    <div class="max-w-5xl mx-auto">
      <!-- 标题 -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold dark:text-slate-50 text-slate-900 mb-4">API 文档</h1>
        <p class="text-lg dark:text-slate-400 text-slate-600">Orange ShortUrl RESTful API 接口文档</p>
      </div>

      <!-- API测试工具 -->
      <div class="glass-effect rounded-2xl p-8 mb-8 shadow-lg border dark:border-slate-600 border-slate-300">
        <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-6 flex items-center">
          <svg class="w-6 h-6 mr-2 dark:text-orange-400 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z"></path>
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
          </svg>
          API 测试工具
        </h2>
        
        <div class="space-y-4">
          <div>
            <label class="block text-sm dark:text-slate-300 text-slate-700 font-semibold mb-2">API端点</label>
            <input 
              v-model="testApi.baseUrl" 
              type="text" 
              class="w-full px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all"
              placeholder="https://orange-shorturl.czdgg.top"
            />
          </div>
          
          <div>
            <label class="block text-sm dark:text-slate-300 text-slate-700 font-semibold mb-2">请求URL</label>
            <input 
              v-model="testApi.url" 
              type="text" 
              class="w-full px-4 py-2 dark:bg-slate-800/70 bg-white border-2 dark:border-slate-600 border-slate-300 dark:text-slate-50 text-slate-900 rounded-lg focus:border-orange-500 focus:outline-none transition-all"
              placeholder="https://orange-shorturl.czdgg.top/long/url"
            />
          </div>
          
          <button 
            @click="testShortenApi" 
            class="w-full px-6 py-3 bg-orange-600 hover:bg-orange-500 text-white rounded-lg font-semibold transition-all shadow-md hover:shadow-lg transform hover:-translate-y-0.5 border border-orange-600">
            测试生成短链接 API
          </button>
          
          <div v-if="testApi.result" class="p-4 dark:bg-slate-900/70 bg-slate-100 rounded-lg border dark:border-slate-700 border-slate-300">
            <p class="text-sm dark:text-slate-400 text-slate-700 mb-2 font-medium">响应结果：</p>
            <pre class="text-sm dark:text-green-300 text-green-700 overflow-x-auto">{{ JSON.stringify(testApi.result, null, 2) }}</pre>
          </div>
          
          <div v-if="testApi.error" class="p-4 dark:bg-red-900/40 bg-red-50 border-2 dark:border-red-600 border-red-400 rounded-lg dark:text-red-300 text-red-700 font-medium">
            {{ testApi.error }}
          </div>
        </div>
      </div>

      <!-- API列表 -->
      <div class="space-y-6">
        <!-- 生成短链接 -->
        <div class="glass-effect rounded-2xl p-8 shadow-lg border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">生成短链接</h3>
            <span class="px-3 py-1 dark:bg-green-900/50 bg-green-100 dark:text-green-300 text-green-600 rounded-full text-sm font-semibold border dark:border-green-700 border-green-300">POST</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/shorten</code>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求体</p>
            <pre class="p-4 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 text-sm overflow-x-auto border dark:border-slate-700 border-slate-300">{
  "originalUrl": "https://example.com/very/long/url",
  "customCode": "abc123",  // 可选
  "expireTime": "2025-12-31 23:59:59"  // 可选
}</pre>
          </div>
          
          <div>
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">响应示例</p>
            <pre class="p-4 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 text-sm overflow-x-auto border dark:border-slate-700 border-slate-300">{
  "code": 200,
  "message": "success",
  "data": {
    "shortCode": "abc123",
    "shortUrl": "http://localhost:8080/abc123",
    "originalUrl": "https://example.com/very/long/url"
  }
}</pre>
          </div>
        </div>

        <!-- 短链重定向 -->
        <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">短链重定向</h3>
            <span class="px-3 py-1 dark:bg-blue-900/50 bg-blue-100 dark:text-blue-300 text-blue-600 rounded-full text-sm font-semibold border dark:border-blue-700 border-blue-300">GET</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/r/{shortCode}</code>
          </div>
          
          <div>
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">说明</p>
            <p class="dark:text-slate-200 text-slate-700">访问短链接，自动302重定向到原始URL</p>
          </div>
        </div>

        <!-- 获取链接列表 -->
        <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">获取链接列表</h3>
            <span class="px-3 py-1 dark:bg-blue-900/50 bg-blue-100 dark:text-blue-300 text-blue-600 rounded-full text-sm font-semibold border dark:border-blue-700 border-blue-300">GET</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/list?page=1&size=10&keyword=</code>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求参数</p>
            <div class="space-y-2">
              <div class="flex p-2 dark:bg-slate-900/70 bg-slate-100 rounded border dark:border-slate-700 border-slate-300">
                <span class="dark:text-orange-400 text-orange-600 w-24">page</span>
                <span class="dark:text-slate-400 text-slate-600 w-20">int</span>
                <span class="dark:text-slate-200 text-slate-700">页码（默认1）</span>
              </div>
              <div class="flex p-2 dark:bg-slate-900 bg-slate-100 rounded">
                <span class="dark:text-orange-400 text-orange-600 w-24">size</span>
                <span class="dark:text-slate-400 text-slate-600 w-20">int</span>
                <span class="dark:text-slate-200 text-slate-700">每页数量（默认10）</span>
              </div>
              <div class="flex p-2 dark:bg-slate-900 bg-slate-100 rounded">
                <span class="dark:text-orange-400 text-orange-600 w-24">keyword</span>
                <span class="dark:text-slate-400 text-slate-600 w-20">string</span>
                <span class="dark:text-slate-200 text-slate-700">搜索关键词（可选）</span>
              </div>
            </div>
          </div>
          
          <div>
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">响应示例</p>
            <pre class="p-4 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 text-sm overflow-x-auto border dark:border-slate-700 border-slate-300">{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "list": [...]
  }
}</pre>
          </div>
        </div>

        <!-- 获取链接详情 -->
        <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">获取链接详情</h3>
            <span class="px-3 py-1 dark:bg-blue-900/50 bg-blue-100 dark:text-blue-300 text-blue-600 rounded-full text-sm font-semibold border dark:border-blue-700 border-blue-300">GET</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/detail/{shortCode}</code>
          </div>
          
          <div>
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">响应示例</p>
            <pre class="p-4 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 text-sm overflow-x-auto border dark:border-slate-700 border-slate-300">{
  "code": 200,
  "message": "success",
  "data": {
    "shortCode": "abc123",
    "originalUrl": "https://example.com",
    "createTime": "2025-10-17 12:00:00",
    "visitCount": 100,
    "lastVisitTime": "2025-10-17 14:30:00"
  }
}</pre>
          </div>
        </div>

        <!-- 删除链接 -->
        <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">删除链接</h3>
            <span class="px-3 py-1 dark:bg-red-900/50 bg-red-100 dark:text-red-300 text-red-600 rounded-full text-sm font-semibold border dark:border-red-700 border-red-300">DELETE</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/{shortCode}</code>
          </div>
          
          <div>
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">响应示例</p>
            <pre class="p-4 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 text-sm overflow-x-auto border dark:border-slate-700 border-slate-300">{
  "code": 200,
  "message": "删除成功"
}</pre>
          </div>
        </div>

        <!-- 获取访问统计 -->
        <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">获取访问统计</h3>
            <span class="px-3 py-1 dark:bg-blue-900/50 bg-blue-100 dark:text-blue-300 text-blue-600 rounded-full text-sm font-semibold border dark:border-blue-700 border-blue-300">GET</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/stats/{shortCode}</code>
          </div>
          
          <div>
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">响应示例</p>
            <pre class="p-4 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-slate-200 text-slate-700 text-sm overflow-x-auto border dark:border-slate-700 border-slate-300">{
  "code": 200,
  "message": "success",
  "data": {
    "totalVisits": 100,
    "recentVisits": [...],
    "dailyStats": [...]
  }
}</pre>
          </div>
        </div>
      </div>

      <!-- 错误码说明 -->
      <div class="glass-effect rounded-2xl p-8 mt-8 border dark:border-slate-600 border-slate-300">
        <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-6">错误码说明</h2>
        <div class="space-y-2">
          <div class="flex items-center p-3 dark:bg-slate-900/70 bg-slate-100 rounded border dark:border-slate-700 border-slate-300">
            <span class="dark:text-green-300 text-green-600 w-20 font-semibold">200</span>
            <span class="dark:text-slate-200 text-slate-700">请求成功</span>
          </div>
          <div class="flex items-center p-3 dark:bg-slate-900/70 bg-slate-100 rounded border dark:border-slate-700 border-slate-300">
            <span class="dark:text-yellow-300 text-yellow-600 w-20 font-semibold">400</span>
            <span class="dark:text-slate-200 text-slate-700">请求参数错误</span>
          </div>
          <div class="flex items-center p-3 dark:bg-slate-900/70 bg-slate-100 rounded border dark:border-slate-700 border-slate-300">
            <span class="dark:text-red-300 text-red-600 w-20 font-semibold">404</span>
            <span class="dark:text-slate-200 text-slate-700">短链接不存在</span>
          </div>
          <div class="flex items-center p-3 dark:bg-slate-900/70 bg-slate-100 rounded border dark:border-slate-700 border-slate-300">
            <span class="dark:text-red-300 text-red-600 w-20 font-semibold">500</span>
            <span class="dark:text-slate-200 text-slate-700">服务器内部错误</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '../api'

const testApi = ref({
  baseUrl: 'https://orange-shorturl.czdgg.top/',
  url: '',
  result: null,
  error: ''
})

const testShortenApi = async () => {
  if (!testApi.value.url) {
    testApi.value.error = '请输入测试URL'
    return
  }

  testApi.value.result = null
  testApi.value.error = ''

  try {
    const response = await api.shorten({
      originalUrl: testApi.value.url
    })
    testApi.value.result = response
  } catch (err) {
    testApi.value.error = '请求失败：' + (err.response?.data?.message || err.message)
  }
}
</script>

