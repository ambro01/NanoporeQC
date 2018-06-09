<template>
  <div class="card col-md-4">
    <div class="header">
      <h4 class="title">Change password</h4>
    </div>
    <div class="content">
      <form>
        <fg-input type="text"
                  label="Username"
                  :disabled="true"
                  v-model="this.userName">
        </fg-input>
        <fg-input type="text"
                  label="Old password"
                  v-model="this.oldPassword">
        </fg-input>
        <fg-input type="text"
                  label="New password"
                  v-model="this.newPassword">
        </fg-input>
        <fg-input type="text"
                  label="Confirm password"
                  v-model="this.confirmedPassword">
        </fg-input>
        <div class="text-center">
          <button type="submit" class="btn btn-info btn-fill btn-wd" @click.prevent="changePassword">
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
        oldPassword: null,
        newPassword: null,
        confirmedPassword: null
      }
    },
    methods: {
      changePassword () {
        if (this.userName !== null && this.userName !== '' &&
          this.newPassword !== null && this.newPassword !== '' &&
          this.oldPassword !== null && this.oldPassword !== '' &&
          this.newPassword === this.confirmedPassword) {
          this.$http.post(`api/users/change-password`, {
            username: this.userName,
            oldPassword: this.oldPassword,
            newPassword: this.newPassword
          }).then(response => {
            if (response.status === 200) {
              this.$toast.success({
                title: 'Success',
                message: 'Password has been created'
              })
            } else {
              this.$toast.error({
                title: 'Error',
                message: 'Password changing failed'
              })
            }
          })
        } else {
          this.$toast.error({
            title: 'Error',
            message: 'Password changing not allowed'
          })
        }
      }
    }
  }

</script>
<style>

</style>
