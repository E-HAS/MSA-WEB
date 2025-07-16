import { createStore } from 'vuex'
import monitoring from './storeMonitoring'

export default createStore({
  modules: {
    monitoring,
  }
})

/*
namespaced:true,
this.$store.commit('monitoring/setName')         
this.$store.dispatch('monitoring/fetchName')     
this.$store.getters['monitoring/upperName']   

import { mapGetters } from 'vuex'

전역 
computed: {
  ...mapGetters(['upperName'])
}

네임스페이스 모듈
computed: {
  ...mapGetters('user', ['upperName'])
}
*/