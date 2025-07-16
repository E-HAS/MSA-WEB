import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// app.use() 플러그인 등록
// app.component('', ) 글로벌 컴포넌트 등록
// import axios from 'axios'
// app.config.globalProperties.$axios = axios 전역 프로포티 등록
// app.config.errorHandler = (err, vm, info) => { console.error('Global Error:', err)} 전역 에러헨들러 등록

const app = createApp(App)
app.use(router)
app.use(store)
app.mount('#app')
