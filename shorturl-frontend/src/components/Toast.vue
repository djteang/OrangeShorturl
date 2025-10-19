<template>
  <transition name="toast-slide">
    <div v-if="visible" class="toast-container" :class="typeClass">
      <div class="toast-icon">
        <svg v-if="type === 'success'" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
        </svg>
        <svg v-else-if="type === 'error'" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
        </svg>
        <svg v-else-if="type === 'warning'" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
        </svg>
        <svg v-else class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
        </svg>
      </div>
      <div class="toast-content">
        <p class="toast-message">{{ message }}</p>
      </div>
      <button @click="close" class="toast-close">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
        </svg>
      </button>
    </div>
  </transition>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  message: {
    type: String,
    required: true
  },
  type: {
    type: String,
    default: 'info',
    validator: (value) => ['success', 'error', 'warning', 'info'].includes(value)
  },
  duration: {
    type: Number,
    default: 3000
  }
})

const emit = defineEmits(['close'])

const visible = ref(false)

const typeClass = computed(() => {
  return `toast-${props.type}`
})

const close = () => {
  visible.value = false
  setTimeout(() => {
    emit('close')
  }, 300)
}

onMounted(() => {
  visible.value = true
  if (props.duration > 0) {
    setTimeout(close, props.duration)
  }
})
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 300px;
  max-width: 500px;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(12px);
  z-index: 10000;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.toast-success {
  background: rgba(16, 185, 129, 0.95);
  border: 1px solid rgba(5, 150, 105, 0.5);
  color: white;
}

.toast-error {
  background: rgba(239, 68, 68, 0.95);
  border: 1px solid rgba(220, 38, 38, 0.5);
  color: white;
}

.toast-warning {
  background: rgba(245, 158, 11, 0.95);
  border: 1px solid rgba(217, 119, 6, 0.5);
  color: white;
}

.toast-info {
  background: rgba(59, 130, 246, 0.95);
  border: 1px solid rgba(37, 99, 235, 0.5);
  color: white;
}

.toast-icon {
  flex-shrink: 0;
}

.toast-content {
  flex: 1;
  min-width: 0;
}

.toast-message {
  font-weight: 500;
  line-height: 1.5;
  word-break: break-word;
}

.toast-close {
  flex-shrink: 0;
  padding: 4px;
  border-radius: 4px;
  transition: all 0.2s;
  opacity: 0.8;
}

.toast-close:hover {
  opacity: 1;
  background: rgba(255, 255, 255, 0.2);
}

/* 动画 */
.toast-slide-enter-active,
.toast-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.toast-slide-enter-from {
  transform: translateX(400px);
  opacity: 0;
}

.toast-slide-leave-to {
  transform: translateX(400px);
  opacity: 0;
}

/* 暗黑模式下的调整 */
:global(.dark) .toast-success {
  background: rgba(16, 185, 129, 0.9);
}

:global(.dark) .toast-error {
  background: rgba(239, 68, 68, 0.9);
}

:global(.dark) .toast-warning {
  background: rgba(245, 158, 11, 0.9);
}

:global(.dark) .toast-info {
  background: rgba(59, 130, 246, 0.9);
}
</style>




