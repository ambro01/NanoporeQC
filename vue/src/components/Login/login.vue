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
              <div class="alert alert-danger" v-if="error">{{ error }}</div>
              <fg-input v-model="email" type="email" label="Email" placeholder="Email address" required autofocus></fg-input>
              <fg-input v-model="password" type="password" label="Password" placeholder="Password" required></fg-input>
              <button class="btn btn-lg btn-primary btn-block">Log in</button>
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
        email: '',
        password: '',
        error: false
      }
    },
    methods: {
      login () {
        this.$http.post(`login`, {username: this.email, password: this.password})
          .then(response => this.loginSuccessful(response))
          .catch(() => this.loginFailed())
      },
      loginSuccessful (res) {
        if (!res.headers.token) {
          this.loginFailed()
          return
        }

        localStorage.token = res.headers.token
        this.error = false
        this.$router.replace('/view/overview')
      },
      loginFailed () {
        this.error = 'Login failed!'
        delete localStorage.token
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

  .form-control {
    background-color: #E3E3E3;
    position: relative;
    height: auto;
    padding: 10px;
    font-size: 16px;
  }

  .form-signin input[type="email"] {
    margin-bottom: 10px;
  }

  .form-signin input[type="password"] {
    margin-bottom: 10px;
  }
</style>
