import { createApp } from 'vue'
import Toast from '../components/Toast.vue'

const toastQueue = []
let toastIndex = 0

const showToast = (message, type = 'info', duration = 3000) => {
  const container = document.createElement('div')
  container.style.position = 'fixed'
  container.style.top = `${20 + toastIndex * 80}px`
  container.style.right = '20px'
  container.style.zIndex = '10000'
  document.body.appendChild(container)

  const app = createApp(Toast, {
    message,
    type,
    duration,
    onClose: () => {
      app.unmount()
      document.body.removeChild(container)
      toastIndex--
      updateToastPositions()
    }
  })

  app.mount(container)
  toastQueue.push({ app, container })
  toastIndex++

  return app
}

const updateToastPositions = () => {
  toastQueue.forEach((toast, index) => {
    toast.container.style.top = `${20 + index * 80}px`
  })
}

export default {
  success(message, duration) {
    return showToast(message, 'success', duration)
  },
  error(message, duration) {
    return showToast(message, 'error', duration)
  },
  warning(message, duration) {
    return showToast(message, 'warning', duration)
  },
  info(message, duration) {
    return showToast(message, 'info', duration)
  }
}




