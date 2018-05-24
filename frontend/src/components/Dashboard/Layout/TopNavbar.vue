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
        userName: ''
      }
    },
    mounted () {
      if (!this.$cookies.isKey('user')) {
        this.$http.get(`api/users/current-user`).then(response => {
          if (response.status === 200) {
            this.userName = response.data
          }
        })
      }
      this.userName = this.$cookies.get('user')
    },
    methods: {
      logout () {
        delete localStorage.getItem('token')
        this.$session.destroy()
        this.$router.replace('/')
        this.userName = ''
        this.$cookies.remove('user')
      }
    }
  }

</script>
<style>

</style>
