<template>
  <div id="container-body">
    <div v-for="(serverList, serverName) in servers" :key="serverName">
      <div v-for="(info, index) in serverList" :key="index">
        <ServerCard
          :id="info.seq"
          :name="info.name"
          :host="info.host"
          :chart-data="chartData[info.seq]"
        />
       </div>
    </div>
  </div>
</template>

<script setup>
import ServerCard from '@/components/monitoring/ServerCard.vue';
import { onMounted, computed } from 'vue';
import { useStore } from 'vuex';
import { useMonitoring } from '@/composables/useMonitoring';

const store = useStore();

const { loadServers, connectStomp, handleMessage } = useMonitoring();

const servers = computed(() => store.state.monitoring.servers);
const chartData = computed(() => store.state.monitoring.data);


onMounted(async () => {
  const serverList = await loadServers(); // 서버 리스트 구성
  //let serverList=[];
  //serverList.push({seq: 11, name: 'CMSSERVICE', host: 'https://192.168.1.102:443/', regDate: '2025-06-27T18:20:23.214'});
  if (Array.isArray(serverList)) {
    connectStomp(stompClient => {  // 서버 웹소켓 연결
      serverList.forEach(server => {
        stompClient.subscribe(`/topic/monitoring/${server.seq}`, message => // 서버 STOMP 구독
          handleMessage(server.seq, message)  // 서버로부터 온 메시지 구성
        );
      });
    });
  }
});
</script>

<style scoped>
  #container-body .row {
		display: flex;
		flex-wrap: wrap;
		gap: 1rem; /* 카드 사이 간격 */
	}
	
</style>