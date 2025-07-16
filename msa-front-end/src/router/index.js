import { createRouter, createWebHistory } from 'vue-router'
import main from '../views/main.vue'
import monitoring from '../views/viewMonitoring.vue'
import lotto from '../views/lotto.vue'

const routes = [
    { path: '/', component: main },
    { path: '/monitoring', component: monitoring },
    { path: '/lotto', component: lotto },
]

const router = createRouter({
  history: createWebHistory(),  // HTML5 히스토리 모드
  routes
})

export default router