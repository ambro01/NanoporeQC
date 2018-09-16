import Vue from 'vue'
import VueAxios from 'vue-axios'

import axios from './axios'

Vue.use(VueAxios, axios)

Vue.prototype.$http = axios

axios.interceptors.request.use(function (config) {
  // Do something before request is sent
  config.baseURL = window.location.origin + '/'
  if (window.location.host === 'localhost:5000') {
    config.baseURL = 'http://localhost:8080/'
  }
  let jwt = localStorage.getItem('token')
  if (jwt) {
    config.headers.Authorization = 'Bearer ' + jwt
  }
  return config
}, function (error) {
  // Do something with request error
  return Promise.reject(error)
})

axios.interceptors.response.use(function (response) {
  let jwt = response.headers.token
  if (jwt) {
    axios.defaults.headers.Authorization = 'Bearer ' + jwt
  }
  return response
}, function (error) {
  // Do something with response error
  return Promise.reject(error)
})
