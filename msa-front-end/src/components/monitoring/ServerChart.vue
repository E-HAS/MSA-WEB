<template>
  <div :id="chartId" class="chart-col col" style="min-width: 300px;"></div>
</template>

<script setup>

import { onMounted, watch, ref } from 'vue';
import Chart from '@toast-ui/chart/dist/toastui-chart.min.js';

const props = defineProps({
  chartData: Object,
  chartId : String,
  chartName : String,
  chartPoint : String,
});

onMounted(() => {
  initChart();
});

let chartInstance = null;
let chartYMax = ref(0);

watch(() => props.chartData?.time?.[9], () => {
  if (chartInstance && props.chartData?.time) {
    const latestValues = Object.keys(props.chartData)
      .filter(k => k !== 'init' && k !== 'time' && k !== 'point')
      .map(k => props.chartData[k].at(-1));
    const newTime = props.chartData.time.at(-1);
    chartInstance.addData(latestValues, newTime);
   
    const checkValues = Object.keys(props.chartData)
      .filter(k => k !== 'init' && k !== 'time' && k !== 'point')
      .map(k => props.chartData[k]);

    const maxValue = Math.max(...checkValues.flat());
    const yMax = Math.ceil(maxValue * 1.5); // 최대값보다 약간 크게
    if ( yMax > chartYMax.value) {
      chartInstance.updateOptions({
        yAxis: {
          title: props.chartPoint || '',
          min: 0,
          max: yMax,
        }
      });
      chartYMax.value = yMax;
    }else {
      if(props.chartName === 'jvm_memory' ){
        //console.log(props.chartData);
      }
      chartInstance.updateOptions({
        yAxis: {
          title: props.chartPoint || '',
          min: 0,
          max: yMax,
        }
      });
    }
  }
});

function initChart() {
  const el = document.getElementById(props.chartId);
  const data = {
    categories: props.chartData.time,
    series: Object.keys(props.chartData)
      .filter(k => k !== 'point' && k !== 'time')
      .map(k => ({
        name: k,
        data: props.chartData[k]
      }))
  };
  const options = {
    chart: { title: props.chartName, width: 300, height: 300 },
    xAxis: { title: '시간', pointOnColumn: false },
    yAxis: { title: props.point, min: 0 },
    series: { shift: true },
    legend: { visible: false }
  };
  chartInstance = Chart.areaChart({ el, data, options });
}
</script>


<style scoped>
 .toastui-chart-tooltip{
  position: absolute !important;
  top: 0;
  left: 0;
  z-index: 9999;
  pointer-events: none;
}

.toastui-chart-tooltip-series-wrapper {
  position: absolute !important;
  top: 0;
  left: 0;
  z-index: 9999;
  pointer-events: none;
}

.toastui-chart-tooltip-series {
  position: absolute !important;
  top: 0;
  left: 0;
  z-index: 9999;
  pointer-events: none;
}


.chart-col {
  position: relative; /* 툴팁 기준점 */
  min-width: 260px;
  background: #f4f8fb;
  padding: 12px;
  border-left: 4px solid #eeeeee;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.06);
  overflow: hidden;
}
</style>