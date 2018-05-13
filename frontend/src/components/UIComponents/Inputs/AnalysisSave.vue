<template>
  <div class="card">
    <div class="header">
      <h4 class="title">Save analysis</h4>
      <a href="#" class="btn-rotate" v-on:click="showAll" v-if="!this.isShowingAll">
        <i class="ti-angle-down"></i>
      </a>
      <a href="#" class="btn-rotate" v-on:click="hideAll" v-if="this.isShowingAll">
        <i class="ti-angle-up"></i>
      </a>
    </div>
    <div class="content">
      <form>
        <div class="row" v-if="this.isShowingAll">
          <div class="col-md-12">
            <div class="col-md-2">
              <fg-input type="text"
                        v-model="analysisName"
                        label="Analysis name"
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
            <div class="col-md-2">
              <div class="col-md-2">
                <button class="btn btn-primary btn-fill save-button" :disabled="!this.analysisName.length > 0" @click.prevent="submit">
                  Save
                </button>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
  export default {
    props: [
      'analysisType'
    ],
    data () {
      return {
        isShowingAll: true,
        analysisName: '',
        comment: ''
      }
    },
    methods: {
      showAll () {
        this.isShowingAll = true
      },
      hideAll () {
        this.isShowingAll = false
      },
      submit () {
        this.$http.post(`api/analysis/save-new`, {
          name: this.analysisName,
          comment: this.comment,
          type: this.analysisType
        }).then(response => {
          if (response.status === 200) {
            // toastr.success('Files Uploaded!', 'Success')
            this.resetData()
            this.$emit('savedAnalysis')
          } else {
            this.errors = response.data.errors
          }
        })
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
<style lang="scss">
  .save-button {
    margin-top: 20px;
    width: 100px;
  }
</style>
