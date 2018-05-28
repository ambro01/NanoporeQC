import Sidebar from './SideBar.vue'

const SidebarStore = {
  showSidebar: false,
  sidebarLinks: [
    {
      name: 'Overview',
      icon: 'ti-panel',
      path: '/view/overview'
    },
    {
      name: 'New Analysis',
      icon: 'ti-pencil',
      path: '/view/new-analysis'
    },
    {
      name: 'My Analyses',
      icon: 'ti-server',
      path: '/view/my-analyses'
    },
    {
      name: 'User panel',
      icon: 'ti-user',
      path: '/view/user-panel'
    }
  ],
  displaySidebar (value) {
    this.showSidebar = value
  }
}

const SidebarPlugin = {

  install (Vue) {
    Vue.mixin({
      data () {
        return {
          sidebarStore: SidebarStore
        }
      }
    })

    Object.defineProperty(Vue.prototype, '$sidebar', {
      get () {
        return this.$root.sidebarStore
      }
    })
    Vue.component('side-bar', Sidebar)
  }
}

export default SidebarPlugin
