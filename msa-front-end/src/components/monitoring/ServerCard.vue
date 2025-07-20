<template>
  <div class="card mt-4 shadow-sm border-0">
    <div class="card-body">
      <h5 class="card-title fw-bold">{{ name }}</h5>
      <h6 class="card-subtitle mb-2 text-muted">{{ host }}</h6>
      <div class="row flex-nowrap overflow-auto gap-2">
        <ServerChart
          v-for="(data, key) in chartData"
            :key="key"
            :chart-data="data"
            :chart-id="key + String(id)"
            :chart-name="key"
            :chart-point="data.point"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import ServerChart from './ServerChart.vue';

const props = defineProps({
  id: Number,
  name: String,
  host: String,
  chartData: Object,
});

</script>

<style scoped>
.card {
  background-color: #ffffff;
  border-radius: 8px;
  transition: box-shadow 0.3s ease;
}

.card:hover {
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.card-title {
  color: #2c3e50;
}

.row.flex-nowrap {
  overflow-x: auto;
  scrollbar-width: thin;
  padding-bottom: 8px;
}

.row.flex-nowrap::-webkit-scrollbar {
  height: 6px;
}
.row.flex-nowrap::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 10px;
}
</style>