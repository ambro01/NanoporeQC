<template>
  <div class="card col-md-4">
    <div class="header">
      <h4 class="title">Add user</h4>
    </div>
    <div class="content">
      <form>
        <fg-input type="text"
                  label="Username"
                  v-model="userName">
        </fg-input>
        <fg-input type="text"
                  label="Password"
                  v-model="password">
        </fg-input>
        <fg-input type="text"
                  label="Confirm password"
                  v-model="confirmedPassword">
        </fg-input>
        <div class="text-center">
          <button type="submit" class="btn btn-info btn-fill btn-wd" @click.prevent="addUser">
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
        userName: null,
        password: null,
        confirmedPassword: null
      }
    },
    methods: {
      addUser () {
        if (this.userName !== null && this.userName !== '' &&
          this.password !== null && this.password !== '' &&
          this.password === this.confirmedPassword) {
          this.$http.post(`api/users/create`, {
            username: this.userName,
            password: this.password
          }).then(response => {
            if (response.status === 200) {
              this.$toast.success({
                title: 'Success',
                message: 'User ' + this.userName + ' has been created'
              })
            } else {
              this.$toast.error({
                title: 'Error',
                message: 'User creation failed'
              })
            }
          })
        } else {
          this.$toast.error({
            title: 'Error',
            message: 'User creation not allowed'
          })
        }
      }
    }
  }

</script>
<style>

</style>
