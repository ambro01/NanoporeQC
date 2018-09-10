<template>
  <div class="card col-md-4">
    <div class="header">
      <h4 class="title">Add user</h4>
    </div>
    <div class="content">
      <form @submit.prevent="addUser">
        <fg-input type="email"
                  label="Username / email"
                  v-model="userName">
        </fg-input>
        <fg-input type="password"
                  label="Password"
                  v-model="password">
        </fg-input>
        <fg-input type="password"
                  label="Confirm password"
                  v-model="confirmedPassword">
        </fg-input>
        <div class="text-center">
          <button class="btn btn-info btn-fill btn-wd"
                  :disabled="this.userName === '' || this.password === '' || this.confirmedPassword === ''">
            Add user
          </button>
        </div>
        <div class="clearfix"></div>
      </form>
    </div>
  </div>
</template>
<script>
  export default {
    data () {
      return {
        userName: '',
        password: '',
        confirmedPassword: ''
      }
    },
    methods: {
      addUser () {
        if (this.password === this.confirmedPassword) {
          this.$http.post(`api/users/create`, {
            username: this.userName,
            password: this.password
          }).then(() => {
            this.$toast.success({
              title: 'Success',
              message: 'User ' + this.userName + ' has been created'
            })
            this.userName = ''
            this.password = ''
            this.confirmedPassword = ''
          }).catch(() => this.$toast.error())
        }
      }
    }
  }

</script>
<style>

</style>
