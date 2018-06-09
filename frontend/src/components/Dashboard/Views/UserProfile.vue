<template>
  <div class="row">
    <div class="col-md-12">
      <div class="col-md-1">
      </div>
      <change-password-form>
      </change-password-form>
      <div class="col-md-2">
      </div>
      <add-user-form>
      </add-user-form>
    </div>
  </div>
</template>
<script>
  import ChangePasswordForm from './UserProfile/ChangePasswordForm.vue'
  import AddUserForm from './UserProfile/AddUserForm.vue'

  export default {
    components: {
      ChangePasswordForm,
      AddUserForm
    }
  }

</script>
<style>

</style>
