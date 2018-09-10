<template>
  <div class="card col-md-4">
    <div class="header">
      <h4 class="title">Change password</h4>
    </div>
    <div class="content">
      <form @submit.prevent="changePassword">
        <fg-input type="email"
                  label="Username / email"
                  :disabled="true"
                  v-model="userName">
        </fg-input>
        <fg-input type="password"
                  label="Old password"
                  v-model="oldPassword">
        </fg-input>
        <fg-input type="password"
                  label="New password"
                  v-model="newPassword">
        </fg-input>
        <fg-input type="password"
                  label="Confirm password"
                  v-model="confirmedPassword">
        </fg-input>
        <div class="text-center">
          <button class="btn btn-info btn-fill btn-wd"
                  :disabled="this.newPassword === '' || this.oldPassword === '' || this.confirmedPassword === ''">
            Change password
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
        userName: this.$cookies.get('user'),
        oldPassword: '',
        newPassword: '',
        confirmedPassword: ''
      }
    },
    methods: {
      changePassword () {
        if (this.newPassword === this.confirmedPassword) {
          this.$http.post(`api/users/change-password`, {
            username: this.userName,
            oldPassword: this.oldPassword,
            newPassword: this.newPassword
          }).then(() => {
            this.$toast.success({
              title: 'Success',
              message: 'Password has been created'
            })
            this.userName = ''
            this.newPassword = ''
            this.oldPassword = ''
            this.confirmedPassword = ''
          }).catch(() => this.$toast.error())
        }
      }
    }
  }

</script>
<style>

</style>
