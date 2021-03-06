import Vue from 'vue'
import VueRouter from 'vue-router'
import vClickOutside from 'v-click-outside'
import { ClientTable } from 'vue-tables-2'
import VueTabs from 'vue-nav-tabs'
import VueLocalStorage from 'vue-localstorage'
import VueCookies from 'vue-cookies'
import CxltToastr from 'cxlt-vue2-toastr'
import BootstrapVue from 'bootstrap-vue'
import 'vue-nav-tabs/themes/vue-tabs.css'

// Plugins
import GlobalComponents from './gloablComponents'
import SideBar from './components/UIComponents/SidebarPlugin'
import App from './App'

// router setup
import routes from './routes/routes'
import axios from './vue-axios/'

// library imports
import Chartist from 'chartist'
import 'bootstrap/dist/css/bootstrap.css'
import './assets/sass/paper-dashboard.scss'
import 'es6-promise/auto'
import 'cxlt-vue2-toastr/dist/css/cxlt-vue2-toastr.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

// plugin setup
Vue.use(VueRouter)
Vue.use(GlobalComponents)
Vue.use(vClickOutside)
Vue.use(SideBar)
Vue.use(VueTabs)
Vue.use(ClientTable)
Vue.use(VueLocalStorage)
Vue.use(VueCookies)
Vue.use(BootstrapVue)

var toastrConfigs = {
  position: 'bottom right',
  showDuration: 3000
}
Vue.use(CxltToastr, toastrConfigs)

// configure router
const router = new VueRouter({
  routes, // short for routes: routes
  linkActiveClass: 'active'
})

// global library setup
Object.defineProperty(Vue.prototype, '$Chartist', {
  get () {
    return this.$root.Chartist
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  router,
  axios,
  data: {
    Chartist: Chartist
  }
})
