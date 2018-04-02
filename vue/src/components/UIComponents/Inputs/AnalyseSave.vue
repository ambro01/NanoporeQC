<template>
  <div class="form-group col-md-12">
    <label class="control-label">Run analyse</label>
    <br><br>
    <input v-model="analyseName" placeholder="Enter name"/>
    <button class="btn btn-primary" @click="submit">Run</button>
  </div>
</template>
<script>
  import {AXIOS} from 'src/http-common'

  export default {
    data () {
      return {
        analyseName: ''
      }
    },
    methods: {
      submit () {
        AXIOS.post(`api/analyse/new`, {analyseName: this.analyseName})
          .then(response => {
            if (response.data.success) {
              console.log('Successfull Upload')
              toastr.success('Files Uploaded!', 'Success')
              this.resetData()
            } else {
              console.log('Unsuccessful Upload')
              this.errors = response.data.errors
            }
          })
        // .bind(this) // Make sure we bind Vue Component object to this funtion so we get a handle of it in order to call its other methods
      },
      // We want to clear the FormData object on every upload so we can re-calculate new files again.
      // Keep in mind that we can delete files as well so in the future we will need to keep track of that as well
      resetData () {
        this.data.analyseName = '' // Reset it completely
      }
    }
  }
</script>
