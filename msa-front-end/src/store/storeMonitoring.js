import { getLast10SecondsTimestamps } from '@/utils/helpers';

export default {
  namespaced: true,
  state: () => ({
    servers: {},   // 서버 목록 { [serverId]: {name, host} }
    data: {}       // 실시간 모니터링 데이터
  }),
  getters:{
  },
  mutations: {
    LOAD_SERVERS(state, servers) {
      servers.forEach(server => {
        if(!state.servers[server.name]) state.servers[server.name]=[]; // 서버 리스트 존재 여부
        state.servers[server.name].push(server);                       // 서버 리스트에 추가
      });
      /*
      host: "https://192.168.1.102:443/"
      name: "CMSSERVICE"
      regDate: "2025-06-27T18:20:23.214"
      seq: 11
      */
    },
    INIT_CHART(state, { serverId, label, opt }) { //  { label : 'cpu' , opt: 'process_cpu_time_ns_total'}

      if (!state.data[serverId]) state.data[serverId] = {}; // 서버 ID 존재 여부
      const serverData = state.data[serverId];
      
      if (!serverData[label]) {
        serverData[label] = { time: getLast10SecondsTimestamps() };
      }
      if (!serverData[label][opt]) {
        serverData[label][opt] = Array(10).fill(0);
      }

    },
    PUSH_DATA(state, { serverId, label, opt, value, time }) {
      const chart = state.data[serverId][label];
      chart[opt].shift();
      chart[opt].push(value);
      chart.time.shift();
      chart.time.push(time);
    }
  },
  actions: {
    loadServers({ commit }, servers) {
      commit('LOAD_SERVERS', servers);
      return servers;
    },
    initChart({ commit }, payload) {
      commit('INIT_CHART', payload);
    },
    pushData({ commit }, payload) {
      commit('PUSH_DATA', payload);
    }
  }
};