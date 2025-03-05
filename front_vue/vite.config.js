import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'


// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  // // 配置服务器的代理设置
  // server: {
  //   // 代理配置，用于重定向请求到其他服务器
  //   proxy: {
  //     // 定义一个代理规则，将/hello-world路径下的请求代理到指定的目标服务器
  //     '/api': {
  //       // 目标服务器的地址
  //       target: 'http://localhost:8329',
  //       // 更改请求的origin为代理服务器的origin，以便与目标服务器交互
  //       changeOrigin: true,
  //       // 重写请求路径，移除/hello-world前缀
  //       // rewrite: (path) => path.replace(/^\/api/, '')
  //       rewrite: (path) => {
  //         console.log('代理请求:', path)  // 添加日志
  //         return path.replace(/^\/api/, '')
  //       }
  //     }
  //   }
  // }
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8329',
        secure: false,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
