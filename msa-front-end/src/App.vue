<template>
  <div style="min-width:1650px">
    <nav class="navbar bg-dark navbar-dark">
      <div class="container-fluid d-flex justify-content-between align-items-center">
        <!-- 왼쪽 영역 -->
        <div class="d-flex align-items-center">
          <a class="navbar-brand" href="/">TEST MSA SERVER</a>
          <ul class="navbar-nav flex-row ms-3">
            <li class="nav-item me-3">
              <router-link class="nav-link text-white" to="/">메인</router-link>
            </li>
            <li class="nav-item me-3">
              <router-link class="nav-link text-white" to="/monitoring">모니터링</router-link>
            </li>
            <li class="nav-item me-3">
              <router-link class="nav-link text-white" to="/lotto">로또</router-link>
            </li>
          </ul>
        </div>

        <!-- 오른쪽 영역 -->
        <div>
          <form class="d-flex" role="search">
            <button class="btn btn-success btn-sm" type="submit">Login</button>
          </form>
        </div>
      </div>
    </nav>
  </div>
  <div class="container-fluid" id="container-body">
      <router-view></router-view>
  </div>
</template>


<script>
import router from './router';
export default {
  name: 'App',
  router,
}
</script>

<style></style>
