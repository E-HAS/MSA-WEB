import { useStore } from 'vuex';
import { translateForByte, truncatedForPercent } from '@/utils/helpers';
//import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export function useMonitoring() {
  const store = useStore();

  const translateItem = item => {
    let _label = item.label;
    let _value = item.value;

    if (_label.includes('cpu')){
      if(_label == 'process_cpu_usage'
      || _label == 'system_cpu_usage'){
        return { label : 'cpu', opt : _label, value : _value, point : '%'}
      }
    }

    if (_label.includes('jvm_threads')){
      if(_label=='jvm_threads_daemon_threads' || _label=='jvm_threads_live_threads'
			|| _label=='jvm_threads_peak_threads' || _label=='jvm_threads_started_threads_total'){
        return { label : 'jvm_threads', opt : _label, value : _value, point : null}
      }
    }

    if (_label.includes('jvm_memory')){
      if(_label=='jvm_memory_used_bytes' || _label=='jvm_memory_max_bytes' || _label=='jvm_memory_committed_bytes'){
        return { label : 'jvm_memory', opt : _label, value : _value, point : 'MB'}
      }
    }

    if (_label.includes('tomcat_session')){
      if(_label=='tomcat_sessions_active_current_sessions' || _label=='tomcat_sessions_active_max_sessions'
      || _label=='tomcat_sessions_alive_max_seconds' || _label=='tomcat_sessions_created_sessions_total'
      || _label=='tomcat_sessions_expired_sessions_total' || _label=='tomcat_sessions_rejected_sessions_total'){
        return { label : 'tomcat_session', opt : _label, value : _value, point : null}
      }
    }

    if (_label.includes('executor_pool')){
      if(_label=='executor_pool_core_threads' || _label=='executor_pool_size_threads' || _label=='executor_active_threads'){
        return { label : 'executor_pool', opt : _label, value : _value, point : null}
      }
    }

    return { label : null, opt : null, value : null, point : null}
  };

  async function loadServers() {
    const res = await fetch('/infra/api/servers');
    const data = await res.json();
    return store.dispatch('monitoring/loadServers', data);
  }

function connectStomp(onConnect) {
  const socket = new WebSocket('/monitoring/stomp');
  const stompClient = Stomp.over(socket);
  stompClient.debug = () => {};

  stompClient.reconnectDelay = 5000;
  stompClient.connect(
    {},
    () => onConnect(stompClient),
    (error) => {
      console.error('STOMP 연결 실패', error);
      // 모든 서버 상태를 Offline 처리
      Object.keys(store.state.monitoring.servers).forEach(name => {
        const list = store.state.monitoring.servers[name];
        list.forEach(s => store.dispatch('monitoring/setStatus', { serverId: s.seq, online: false }));
      });
    }
  );
}


  function handleMessage(serverId, message) {
    const parsed = JSON.parse(message.body);
    const time = parsed.time;
    //  {seq: 196, label: 'process_cpu_time_ns_total', opt:'', value: 30328125000}
    parsed.lists.forEach(item => {
      let { label, opt, value, point } = translateItem(item); // 필요 라벨 필터, 그룹화

      if (!label) return;

      store.dispatch('monitoring/initChart', { serverId, label, opt, point });

      const pointValue = point == '%' ? truncatedForPercent(value)
                        : point == 'MB' ? translateForByte(value)
                        : value;

      store.dispatch('monitoring/pushData', {
        serverId,
        label,
        opt,
        value: pointValue,
        time
      });
    });
  }

  return {
    loadServers,
    connectStomp,
    handleMessage
  };
}
