/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // 自定义颜色以确保更好的对比度
        dark: {
          bg: {
            primary: '#0f172a',
            secondary: '#1e293b',
            tertiary: '#334155',
          },
          text: {
            primary: '#f8fafc',
            secondary: '#e2e8f0',
            tertiary: '#cbd5e1',
          },
          border: {
            primary: '#475569',
            secondary: '#334155',
          }
        },
        light: {
          bg: {
            primary: '#ffffff',
            secondary: '#f8fafc',
            tertiary: '#f1f5f9',
          },
          text: {
            primary: '#0f172a',
            secondary: '#1e293b',
            tertiary: '#475569',
          },
          border: {
            primary: '#cbd5e1',
            secondary: '#e2e8f0',
          }
        }
      }
    },
  },
  plugins: [],
}

