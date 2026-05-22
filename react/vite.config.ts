import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')

  return {
    plugins: [react()],
    test: {
      environment: "happy-dom",
      globals: true,
      setupFiles: "./vitest.setup.ts",
      coverage: {
        provider: "v8",
        reporter: ["lcov", "text"],
        reportsDirectory: "./coverage",
        include: ["src/**/*.{ts,tsx}"],
        exclude: [
          "src/**/*.test.{ts,tsx}",
          "src/main.tsx",
          "src/**/*.d.ts",
        ],
      },
    },
    server: {
      host: '0.0.0.0',
      proxy: {
        '/api': {
          target: `http://${env.VITE_API_HOST}:${env.VITE_API_PORT}`,
          changeOrigin: true
        }
      }
    }
  }
})
