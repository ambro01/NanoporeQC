<template>
  <div class="wrapper">
    <div class="row">
      <div class="col-md-4">
      </div>
      <div class="col-md-4">
        <div class="card">
          <div class="login-wrapper border border-light">
            <form class="form-signin" @submit.prevent="login">
              <h2>Please log in</h2>
              <hr>
              <div class="alert alert-danger" v-if="error">{{ error }}</div>
              <fg-input v-model="email" type="email" label="Email" placeholder="Email address" required
                        autofocus></fg-input>
              <fg-input v-model="password" type="password" label="Password" placeholder="Password" required></fg-input>
              <button class="btn btn-lg btn-primary btn-block"
                      :disabled="this.email === '' || this.password === '' || this.loginPending">
                Log in
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Login',
    data () {
      return {
        loginPending: false,
        email: '',
        password: '',
        error: false
      }
    },
    methods: {
      login () {
        this.loginPending = true
        this.$http.post(`login`, {username: this.email, password: this.password})
          .then(response => this.loginSuccessful(response))
          .catch(() => this.loginFailed())
      },
      loginSuccessful (res) {
        this.$toast.success({
          title: 'Success',
          message: 'Login successful'
        })
        if (!res.headers.token) {
          this.loginFailed()
          return
        }
        localStorage.setItem('token', res.headers.token)
        this.error = false
        this.$router.replace('/view/overview')
        this.loginPending = false
        this.$http.get(`api/users/current-user`).then(response => {
          if (response.status === 200) {
            this.$cookies.set('user', response.data)
          }
        })
      },
      loginFailed () {
        this.$toast.error({
          title: 'Error',
          message: 'Login failed'
        })
        this.error = 'Login failed!'
        delete localStorage.token
        this.loginPending = false
      }
    }
  }
</script>

<style lang="css">
  html {
    background: #f4f3ef;
  }

  body {
    background: #f4f3ef;
  }

  input:-webkit-autofill {
    background-color: #f4f3ef !important;
  }

  .wrapper {
    background-color: #f4f3ef;
  }

  .login-wrapper {
    width: 70%;
    margin: 12% auto;
  }

  .form-signin {
    max-width: 330px;
    padding: 10% 15px;
    margin: 0 auto;
  }

  .form-signin input[type="email"] {
    margin-bottom: 10px;
  }

  .form-signin input[type="password"] {
    margin-bottom: 40px;
  }
</style>
