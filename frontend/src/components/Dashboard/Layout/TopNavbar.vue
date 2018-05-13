<template>
  <nav class="navbar navbar-default">
    <div class="container-fluid">
      <div class="navbar-right-menu">
        <ul class="nav navbar-nav navbar-right">
          <li>
            <a class="btn-rotate">
              <i class="ti-user"></i>
              <p>
                {{this.userName}}
              </p>
            </a>
          </li>
          <li>
            <a href="#" class="btn-rotate" v-on:click="logout">
              <i class="ti-close"></i>
              <p>
                Log out
              </p>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
<script>
  export default {
    computed: {
      routeName () {
        const {name} = this.$route
        return this.capitalizeFirstLetter(name)
      }
    },
    data () {
      return {
        userName: '',
        activeNotifications: false
      }
    },
    mounted () {
      this.$http.get(`api/users/current-user`).then(response => {
        if (response.status === 200) {
          this.userName = response.data
        } else {
          this.errors = response.data.errors
        }
      })
    },
    methods: {
      capitalizeFirstLetter (string) {
        return string.charAt(0).toUpperCase() + string.slice(1)
      },
      toggleNotificationDropDown () {
        this.activeNotifications = !this.activeNotifications
      },
      closeDropDown () {
        this.activeNotifications = false
      },
      toggleSidebar () {
        this.$sidebar.displaySidebar(!this.$sidebar.showSidebar)
      },
      hideSidebar () {
        this.$sidebar.displaySidebar(false)
      },
      logout () {
        delete localStorage.getItem('token')
        this.$session.destroy()
        this.$router.replace('/')
        this.userName = ''
      }
    }
  }

</script>
<style>

</style>
