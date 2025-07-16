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
          :chart-id="key + id"
          :chart-name="key"
          unit=""
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import ServerChart from './ServerChart.vue';

const props = defineProps({
  id: String,
  name: String,
  host: String,
  chartData: Object
});

onMounted(() => {
  console.log('id:', props.id);
  console.log('name:', props.name);
  console.log('host:', props.host);
  console.log('chartData:', props.chartData);
});
</script>
