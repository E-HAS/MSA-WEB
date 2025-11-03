<template>
  <div class="card mt-4 shadow border-0">
    <div class="card-body">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <div>
          <h5 class="card-title fw-bold"> Test Title</h5>
          <h6 class="card-subtitle text-muted"> Sub Title </h6>
        </div>
      </div>
      <div class="row flex-nowrap overflow-auto gap-2">
        <div class="col-auto">
          <!--<h2>Line Chart</h2>-->
          <div id="chart-area"></div>
        </div>
        <div class="col-auto">
          <!--<h2>Line Chart</h2>-->
          <div id="chart-area2"></div>
        </div>
        <div class="col-auto">
          <!--<h2>Line Chart</h2>-->
          <div id="chart-area3"></div>
        </div>
        <div class="col-auto">
          <!--<h2>Line Chart</h2>-->
          <div id="chart-area4"></div>
        </div>
        <div class="col-auto">
          <!--<h2>Line Chart</h2>-->
          <div id="chart-area5"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount } from 'vue';
import { AreaChart } from '@toast-ui/chart';
import '@toast-ui/chart/dist/toastui-chart.min.css';  // CSS 필수

function onChart(divChartName){
  let chart = null;
  let intervalId = null;
  const el = document.getElementById(divChartName);

  const data = {
    categories: ['1', '2', '3', '4', '5'],
    series: [
      {
        name: 'Series A',
        data: [10, 20, 30, 40, 50],
      },
      {
        name: 'Series B',
        data: [50, 40, 30, 20, 10],
      },
    ],
  };

  const options = {
    chart: {
      //title: 'Live Updating Area Chart',
      width: 400,
      height: 250,
    },
    xAxis: {
      title: 'Time',
    },
    yAxis: {
      title: 'Value',
    },
    tooltip: {
      formatter: value => `${value}`,
    },
    exportMenu: {
    visible: false  // ← export 버튼 제거
    },
  };

  chart = new AreaChart({ el, data, options });

  // 라이브 업데이트: 1초마다 새로운 데이터 추가
  intervalId = setInterval(() => {
    const nextCategory = (parseInt(data.categories[data.categories.length - 1]) + 1).toString();
    const newA = Math.floor(Math.random() * 100);
    const newB = Math.floor(Math.random() * 100);

    chart.addData([newA, newB], nextCategory);

    // 데이터가 너무 많아지면 처음 항목 제거
    if (chart.getOptions().chart.width && data.categories.length > 20) {
      data.categories.shift();
      data.series.forEach(s => s.data.shift());
    }
  }, 1000);
}


onMounted(() =>{
  onChart('chart-area');
  onChart('chart-area2');
  onChart('chart-area3');
  onChart('chart-area4');
  onChart('chart-area5');
});

onBeforeUnmount(() => {
  //clearInterval(intervalId);
  //chart?.destroy();
});
</script>