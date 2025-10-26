<template>
  <div class="min-h-screen px-4 py-12">
    <div class="max-w-5xl mx-auto">
      <!-- 标题 -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold dark:text-slate-50 text-slate-900 mb-4">API 文档</h1>
        <p class="text-lg dark:text-slate-400 text-slate-600">Orange ShortUrl RESTful API 接口文档</p>
        <div class="mt-4 inline-flex items-center px-4 py-2 bg-orange-100 dark:bg-orange-900/30 text-orange-700 dark:text-orange-300 rounded-lg">
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
          </svg>
          <span class="text-sm font-medium">所有接口已添加速率限制保护</span>
        </div>
      </div>

      <!-- API列表 -->
      <div class="space-y-6">
        <!-- 生成短链接 -->
        <div class="glass-effect rounded-2xl p-8 shadow-lg border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">生成短链接</h3>
              <p class="text-sm text-orange-500 dark:text-orange-400 mt-1">限流：10次/分钟/IP</p>
            </div>
            <span class="px-3 py-1 dark:bg-green-900/50 bg-green-100 dark:text-green-300 text-green-600 rounded-full text-sm font-semibold border dark:border-green-700 border-green-300">POST</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/shorten</code>
          </div>
          
          <!-- API测试区 -->
          <div class="mt-6 p-6 bg-blue-50 dark:bg-blue-900/20 rounded-lg border-2 border-blue-200 dark:border-blue-800">
            <h4 class="font-bold text-blue-700 dark:text-blue-300 mb-4 flex items-center">
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path>
              </svg>
              在线测试
            </h4>
            <div class="space-y-3">
              <div>
                <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">原始URL *</label>
                <input 
                  v-model="tests.shorten.originalUrl" 
                  type="text" 
                  placeholder="https://example.com/very/long/url"
                  class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                />
              </div>
              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">自定义短码（可选）</label>
                  <input 
                    v-model="tests.shorten.customCode" 
                    type="text" 
                    placeholder="abc123"
                    class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">过期时间（可选）</label>
                  <input 
                    v-model="tests.shorten.expireTime" 
                    type="text" 
                    placeholder="2025-12-31 23:59:59"
                    class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                  />
                </div>
              </div>
              <button 
                @click="testShorten" 
                :disabled="loading.shorten"
                class="w-full py-2 bg-blue-600 hover:bg-blue-500 text-white rounded font-medium transition-all disabled:opacity-50">
                {{ loading.shorten ? '测试中...' : '测试接口' }}
              </button>
              <div v-if="results.shorten" class="p-3 bg-white dark:bg-slate-800 rounded border border-blue-300 dark:border-blue-700">
                <p class="text-xs text-blue-600 dark:text-blue-400 mb-1 font-medium">响应结果：</p>
                <pre class="text-xs dark:text-green-300 text-green-700 overflow-x-auto">{{ JSON.stringify(results.shorten, null, 2) }}</pre>
              </div>
              <div v-if="errors.shorten" class="p-3 bg-red-50 dark:bg-red-900/30 border border-red-300 dark:border-red-700 rounded text-red-600 dark:text-red-400 text-sm">
                {{ errors.shorten }}
              </div>
            </div>
          </div>
        </div>

        <!-- 获取链接列表 -->
        <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">获取链接列表</h3>
              <p class="text-sm text-orange-500 dark:text-orange-400 mt-1">限流：30次/分钟/IP</p>
            </div>
            <span class="px-3 py-1 dark:bg-blue-900/50 bg-blue-100 dark:text-blue-300 text-blue-600 rounded-full text-sm font-semibold border dark:border-blue-700 border-blue-300">GET</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/list?page=1&size=10&keyword=</code>
          </div>

          <!-- API测试区 -->
          <div class="mt-6 p-6 bg-blue-50 dark:bg-blue-900/20 rounded-lg border-2 border-blue-200 dark:border-blue-800">
            <h4 class="font-bold text-blue-700 dark:text-blue-300 mb-4 flex items-center">
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path>
              </svg>
              在线测试
            </h4>
            <div class="space-y-3">
              <div class="grid grid-cols-3 gap-3">
                <div>
                  <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">页码</label>
                  <input 
                    v-model="tests.list.page" 
                    type="number" 
                    placeholder="1"
                    class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">每页数量</label>
                  <input 
                    v-model="tests.list.size" 
                    type="number" 
                    placeholder="10"
                    class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">关键词</label>
                  <input 
                    v-model="tests.list.keyword" 
                    type="text" 
                    placeholder="搜索..."
                    class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                  />
                </div>
              </div>
              <button 
                @click="testList" 
                :disabled="loading.list"
                class="w-full py-2 bg-blue-600 hover:bg-blue-500 text-white rounded font-medium transition-all disabled:opacity-50">
                {{ loading.list ? '测试中...' : '测试接口' }}
              </button>
              <div v-if="results.list" class="p-3 bg-white dark:bg-slate-800 rounded border border-blue-300 dark:border-blue-700 max-h-64 overflow-auto">
                <p class="text-xs text-blue-600 dark:text-blue-400 mb-1 font-medium">响应结果：</p>
                <pre class="text-xs dark:text-green-300 text-green-700">{{ JSON.stringify(results.list, null, 2) }}</pre>
              </div>
              <div v-if="errors.list" class="p-3 bg-red-50 dark:bg-red-900/30 border border-red-300 dark:border-red-700 rounded text-red-600 dark:text-red-400 text-sm">
                {{ errors.list }}
              </div>
            </div>
          </div>
        </div>

        <!-- 获取链接详情 -->
        <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">获取链接详情</h3>
              <p class="text-sm text-orange-500 dark:text-orange-400 mt-1">限流：30次/分钟/IP</p>
            </div>
            <span class="px-3 py-1 dark:bg-blue-900/50 bg-blue-100 dark:text-blue-300 text-blue-600 rounded-full text-sm font-semibold border dark:border-blue-700 border-blue-300">GET</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/detail/{shortCode}</code>
          </div>

          <!-- API测试区 -->
          <div class="mt-6 p-6 bg-blue-50 dark:bg-blue-900/20 rounded-lg border-2 border-blue-200 dark:border-blue-800">
            <h4 class="font-bold text-blue-700 dark:text-blue-300 mb-4 flex items-center">
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path>
              </svg>
              在线测试
            </h4>
            <div class="space-y-3">
              <div>
                <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">短码 *</label>
                <input 
                  v-model="tests.detail.shortCode" 
                  type="text" 
                  placeholder="abc123"
                  class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                />
              </div>
              <button 
                @click="testDetail" 
                :disabled="loading.detail"
                class="w-full py-2 bg-blue-600 hover:bg-blue-500 text-white rounded font-medium transition-all disabled:opacity-50">
                {{ loading.detail ? '测试中...' : '测试接口' }}
              </button>
              <div v-if="results.detail" class="p-3 bg-white dark:bg-slate-800 rounded border border-blue-300 dark:border-blue-700">
                <p class="text-xs text-blue-600 dark:text-blue-400 mb-1 font-medium">响应结果：</p>
                <pre class="text-xs dark:text-green-300 text-green-700 overflow-x-auto">{{ JSON.stringify(results.detail, null, 2) }}</pre>
              </div>
              <div v-if="errors.detail" class="p-3 bg-red-50 dark:bg-red-900/30 border border-red-300 dark:border-red-700 rounded text-red-600 dark:text-red-400 text-sm">
                {{ errors.detail }}
              </div>
            </div>
          </div>
        </div>

        <!-- 删除链接 -->
        <!-- <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">删除链接</h3>
            <span class="px-3 py-1 dark:bg-red-900/50 bg-red-100 dark:text-red-300 text-red-600 rounded-full text-sm font-semibold border dark:border-red-700 border-red-300">DELETE</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/{shortCode}</code>
          </div>

          
          <div class="mt-6 p-6 bg-blue-50 dark:bg-blue-900/20 rounded-lg border-2 border-blue-200 dark:border-blue-800">
            <h4 class="font-bold text-blue-700 dark:text-blue-300 mb-4 flex items-center">
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path>
              </svg>
              在线测试
            </h4>
            <div class="space-y-3">
              <div>
                <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">短码 *</label>
                <input 
                  v-model="tests.delete.shortCode" 
                  type="text" 
                  placeholder="abc123"
                  class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                />
              </div>
              <div class="p-3 bg-orange-50 dark:bg-orange-900/20 border border-orange-300 dark:border-orange-700 rounded text-orange-700 dark:text-orange-300 text-xs">
                ⚠️ 注意：此操作不可逆，请谨慎使用
              </div>
              <button 
                @click="testDelete" 
                :disabled="loading.delete"
                class="w-full py-2 bg-red-600 hover:bg-red-500 text-white rounded font-medium transition-all disabled:opacity-50">
                {{ loading.delete ? '删除中...' : '测试删除' }}
              </button>
              <div v-if="results.delete" class="p-3 bg-white dark:bg-slate-800 rounded border border-blue-300 dark:border-blue-700">
                <p class="text-xs text-blue-600 dark:text-blue-400 mb-1 font-medium">响应结果：</p>
                <pre class="text-xs dark:text-green-300 text-green-700 overflow-x-auto">{{ JSON.stringify(results.delete, null, 2) }}</pre>
              </div>
              <div v-if="errors.delete" class="p-3 bg-red-50 dark:bg-red-900/30 border border-red-300 dark:border-red-700 rounded text-red-600 dark:text-red-400 text-sm">
                {{ errors.delete }}
              </div>
            </div>
          </div>
        </div> -->

        <!-- 获取访问统计 -->
        <div class="glass-effect rounded-2xl p-8 border dark:border-slate-600 border-slate-300">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-xl font-bold dark:text-slate-50 text-slate-900">获取访问统计</h3>
              <p class="text-sm text-orange-500 dark:text-orange-400 mt-1">限流：30次/分钟/IP</p>
            </div>
            <span class="px-3 py-1 dark:bg-blue-900/50 bg-blue-100 dark:text-blue-300 text-blue-600 rounded-full text-sm font-semibold border dark:border-blue-700 border-blue-300">GET</span>
          </div>
          
          <div class="mb-4">
            <p class="text-sm dark:text-slate-400 text-slate-600 mb-2 font-medium">请求地址</p>
            <code class="block p-3 dark:bg-slate-900/70 bg-slate-100 rounded-lg dark:text-orange-400 text-orange-600 text-sm border dark:border-slate-700 border-slate-300">/api/url/stats/{shortCode}</code>
          </div>

          <!-- API测试区 -->
          <div class="mt-6 p-6 bg-blue-50 dark:bg-blue-900/20 rounded-lg border-2 border-blue-200 dark:border-blue-800">
            <h4 class="font-bold text-blue-700 dark:text-blue-300 mb-4 flex items-center">
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path>
              </svg>
              在线测试
            </h4>
            <div class="space-y-3">
              <div>
                <label class="block text-sm font-medium text-blue-700 dark:text-blue-300 mb-1">短码 *</label>
                <input 
                  v-model="tests.stats.shortCode" 
                  type="text" 
                  placeholder="abc123"
                  class="w-full px-3 py-2 bg-white dark:bg-slate-800 border border-blue-300 dark:border-blue-700 rounded text-sm focus:outline-none focus:border-blue-500"
                />
              </div>
              <button 
                @click="testStats" 
                :disabled="loading.stats"
                class="w-full py-2 bg-blue-600 hover:bg-blue-500 text-white rounded font-medium transition-all disabled:opacity-50">
                {{ loading.stats ? '测试中...' : '测试接口' }}
              </button>
              <div v-if="results.stats" class="p-3 bg-white dark:bg-slate-800 rounded border border-blue-300 dark:border-blue-700 max-h-64 overflow-auto">
                <p class="text-xs text-blue-600 dark:text-blue-400 mb-1 font-medium">响应结果：</p>
                <pre class="text-xs dark:text-green-300 text-green-700">{{ JSON.stringify(results.stats, null, 2) }}</pre>
              </div>
              <div v-if="errors.stats" class="p-3 bg-red-50 dark:bg-red-900/30 border border-red-300 dark:border-red-700 rounded text-red-600 dark:text-red-400 text-sm">
                {{ errors.stats }}
              </div>
            </div>
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
            <span class="dark:text-red-300 text-red-600 w-20 font-semibold">429</span>
            <span class="dark:text-slate-200 text-slate-700">请求过于频繁（触发限流）</span>
          </div>
          <div class="flex items-center p-3 dark:bg-slate-900/70 bg-slate-100 rounded border dark:border-slate-700 border-slate-300">
            <span class="dark:text-red-300 text-red-600 w-20 font-semibold">500</span>
            <span class="dark:text-slate-200 text-slate-700">服务器内部错误</span>
          </div>
        </div>
      </div>

      <!-- 限流说明 -->
      <!-- <div class="glass-effect rounded-2xl p-8 mt-8 border dark:border-slate-600 border-slate-300">
        <h2 class="text-2xl font-bold dark:text-slate-50 text-slate-900 mb-6">接口限流说明</h2>
        <div class="space-y-3 text-sm dark:text-slate-300 text-slate-700">
          <p>• <strong>生成短链接：</strong> 每个IP每分钟最多10次请求</p>
          <p>• <strong>短链重定向：</strong> 每个IP每分钟最多30次请求</p>
          <p>• <strong>前端重定向：</strong> 每个IP每分钟最多50次请求</p>
          <p class="text-orange-600 dark:text-orange-400 mt-4">⚠️ 超过限流次数将返回 "请求过于频繁，请稍后再试" 错误</p>
        </div>
      </div> -->
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import api from '../api'

const tests = reactive({
  shorten: { originalUrl: '', customCode: '', expireTime: '' },
  list: { page: 1, size: 10, keyword: '' },
  detail: { shortCode: '' },
  delete: { shortCode: '' },
  stats: { shortCode: '' }
})

const loading = reactive({
  shorten: false,
  list: false,
  detail: false,
  delete: false,
  stats: false
})

const results = reactive({
  shorten: null,
  list: null,
  detail: null,
  delete: null,
  stats: null
})

const errors = reactive({
  shorten: '',
  list: '',
  detail: '',
  delete: '',
  stats: ''
})

const clearResult = (type) => {
  results[type] = null
  errors[type] = ''
}

const testShorten = async () => {
  if (!tests.shorten.originalUrl) {
    errors.shorten = '请输入原始URL'
    return
  }
  
  clearResult('shorten')
  loading.shorten = true
  
  try {
    const data = { originalUrl: tests.shorten.originalUrl }
    if (tests.shorten.customCode) data.customCode = tests.shorten.customCode
    if (tests.shorten.expireTime) data.expireTime = tests.shorten.expireTime
    
    const response = await api.shorten(data)
    results.shorten = response
  } catch (err) {
    errors.shorten = err.response?.data?.message || err.message || '请求失败'
  } finally {
    loading.shorten = false
  }
}

const testList = async () => {
  clearResult('list')
  loading.list = true
  
  try {
    const params = {
      page: tests.list.page || 1,
      size: tests.list.size || 10
    }
    if (tests.list.keyword) params.keyword = tests.list.keyword
    
    const response = await api.getUrlList(params)
    results.list = response
  } catch (err) {
    errors.list = err.response?.data?.message || err.message || '请求失败'
  } finally {
    loading.list = false
  }
}

const testDetail = async () => {
  if (!tests.detail.shortCode) {
    errors.detail = '请输入短码'
    return
  }
  
  clearResult('detail')
  loading.detail = true
  
  try {
    const response = await api.getUrlDetail(tests.detail.shortCode)
    results.detail = response
  } catch (err) {
    errors.detail = err.response?.data?.message || err.message || '请求失败'
  } finally {
    loading.detail = false
  }
}

const testDelete = async () => {
  if (!tests.delete.shortCode) {
    errors.delete = '请输入短码'
    return
  }
  
  if (!confirm('确定要删除这个短链接吗？')) {
    return
  }
  
  clearResult('delete')
  loading.delete = true
  
  try {
    const response = await api.deleteUrl(tests.delete.shortCode)
    results.delete = response
  } catch (err) {
    errors.delete = err.response?.data?.message || err.message || '请求失败'
  } finally {
    loading.delete = false
  }
}

const testStats = async () => {
  if (!tests.stats.shortCode) {
    errors.stats = '请输入短码'
    return
  }
  
  clearResult('stats')
  loading.stats = true
  
  try {
    const response = await api.getStats(tests.stats.shortCode)
    results.stats = response
  } catch (err) {
    errors.stats = err.response?.data?.message || err.message || '请求失败'
  } finally {
    loading.stats = false
  }
}
</script>
