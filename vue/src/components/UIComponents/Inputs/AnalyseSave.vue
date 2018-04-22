<template>
  <div class="card">
    <div class="header">
      <h4 class="title">Run analyse</h4>
    </div>
    <div class="content">
      <form>
        <div class="row">
          <div class="col-md-12">
            <div class="col-md-2">
              <fg-input type="text"
                        v-model="analyseName"
                        label="Analyse name"
                        placeholder="Enter name">
              </fg-input>
            </div>
            <div class="col-md-4">
              <fg-input type="text"
                        v-model="comment"
                        label="Comment"
                        placeholder="Enter comment">
              </fg-input>
            </div>
          </div>
          <div class="col-md-12">
            <div class="col-md-2">
              <button class="btn btn-primary btn-fill btn-wd" v-if="this.analyseName.length" @click="submit">
                Save and run
              </button>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
  import { AXIOS } from 'src/http-common'

  export default {
    data () {
      return {
        analyseName: '',
        comment: ''
      }
    },
    methods: {
      submit () {
        AXIOS.post(`api/analyse/new`, {
          name: this.analyseName,
          comment: this.comment
        }).then(response => {
          if (response.status === 200) {
            console.log('Successfull Upload')
            // toastr.success('Files Uploaded!', 'Success')
            this.resetData()
          } else {
            console.log('Unsuccessful Upload')
            this.errors = response.data.errors
          }
        })
        this.$emit('runAnalyse')
        // .bind(this) // Make sure we bind Vue Component object to this funtion so we get a handle of it in order to call its other methods
      },
      // We want to clear the FormData object on every upload so we can re-calculate new files again.
      // Keep in mind that we can delete files as well so in the future we will need to keep track of that as well
      resetData () {
        this.analyseName = '' // Reset it completely
        this.comment = ''
      }
    }
  }
</script>
