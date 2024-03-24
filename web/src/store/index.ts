import { createStore } from 'vuex'

export default createStore({
  state: {
    userName: '',
    password: '',
    user: {}
  },
  getters: {
  },
  mutations: {
    setUser (state, user) {
      state.user = user
    },
    setUserNamePassword (state,{userName, password}) {
      state.userName = userName;
      state.password = password
    },
    clearUserNamePassword(state) {
      state.userName = '';
      state.password = '';
    }
  },
  actions: {
    updateUserNamePassword({ commit }, user) {
      commit('setUserNamePassword', user);
    },
    clearUserNamePassword({ commit }) {
      commit('clearUserNamePassword');
    }
  },
  modules: {
  }
})
