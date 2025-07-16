<template>
  <div :id="chartId" class="chart-col" style="min-width: 300px;"></div>
</template>

<script setup>
import { onMounted, watch } from 'vue';
import { Chart } from '@toast-ui/chart';

const props = defineProps({
  chartData: Object,
  chartId: String,
  chartName: String,
  unit: String
});

let chartInstance = null;

onMounted(() => {
  initChart();
});

watch(() => props.chartData, () => {
  if (chartInstance && props.chartData) {
    const latestValues = Object.keys(props.chartData)
      .filter(k => k !== 'init' && k !== 'time')
      .map(k => props.chartData[k].at(-1));
    const newTime = props.chartData.time.at(-1);
    chartInstance.addData(latestValues, newTime);
  }
});

function initChart() {
  const el = document.getElementById(props.chartId);
  const data = {
    categories: props.chartData.time,
    series: Object.keys(props.chartData)
      .filter(k => k !== 'init' && k !== 'time')
      .map(k => ({
        name: k,
        data: props.chartData[k]
      }))
  };
  const options = {
    chart: { title: props.chartName, width: 300, height: 300 },
    xAxis: { title: '시간', pointOnColumn: false },
    yAxis: { title: props.unit, min: 0 },
    series: { shift: true },
    legend: { visible: false }
  };
  chartInstance = Chart.areaChart({ el, data, options });
}
</script>
