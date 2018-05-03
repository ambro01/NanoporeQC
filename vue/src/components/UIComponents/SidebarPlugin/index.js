import Sidebar from './SideBar.vue'

const SidebarStore = {
  showSidebar: false,
  sidebarLinks: [
    {
      name: 'Dashboard',
      icon: 'ti-panel',
      path: '/view/overview'
    },
    {
      name: 'User Profile',
      icon: 'ti-user',
      path: '/view/stats'
    },
    {
      name: 'Table List',
      icon: 'ti-view-list-alt',
      path: '/view/table-list'
    },
    {
      name: 'Typography',
      icon: 'ti-text',
      path: '/view/typography'
    },
    {
      name: 'New Analyse',
      icon: 'ti-pencil',
      path: '/view/new-analyse'
    },
    {
      name: 'My Analyses',
      icon: 'ti-archive',
      path: '/view/my-analyses'
    },
    {
      name: 'Maps',
      icon: 'ti-map',
      path: '/view/maps'
    },
    {
      name: 'Notifications',
      icon: 'ti-bell',
      path: '/view/notifications'
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
