<template>
  <div class="col-md-12">
    <div class="col-md-3">
    </div>
    <div class="card col-md-5">
      <div class="header">
        <h4 class="title">Files of analysis</h4>
      </div>
      <hr>
      <div class="content">
        <form>
          <div class="row" align="center">
            <table>
              <tr v-for="(attachment, index) in attachments">
                <td>
              <span class="label label-default my-label">
                {{ attachment.name + ' (' + Number((attachment.size / 1024 / 1024).toFixed(1)) + 'MB)'}}
              </span>
                </td>
                <td>
                <span class="margin-left-10" @click="removeAttachment(attachment)">
                  <button class="btn btn-xs btn-danger">Remove</button>
                </span>
                </td>
              </tr>
            </table>
            <div v-if="this.uploadPending">
              <hr>
              <b-progress :value="percentCompleted" :max="100" show-progress animated></b-progress>
            </div>
            <hr>
            <label class="btn btn-secondary btn-fill btn-wd" for="attachments">
              <input type="file"
                     multiple="multiple"
                     id="attachments"
                     @change="uploadFieldChange"
                     style="display:none;">
              Browse files ...
            </label>
            <button class="btn btn-primary btn-fill btn-wd margin-left-100"
                    v-if=this.attachments.length
                    @click.prevent="submit"
                    :disabled="this.uploadPending">
              Upload
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    props: [
      'settings',
      'analysisType'
    ],
    data () {
      return {
        uploadPending: false,
        // You can store all your files here
        attachments: [],
        // Each file will need to be sent as FormData element
        data: new FormData(),
        percentCompleted: 0 // You can store upload progress 0-100 in value, and show it on the screen
      }
    },
    mounted () {
      switch (this.analysisType) {
        case 'Fast5':
          document.getElementById('attachments').accept = '.fast5'
          break
        case 'FastQ':
          document.getElementById('attachments').accept = '.fastq,.fastq.gz'
          break
      }
    },
    methods: {
      getAttachmentSize () {
        this.upload_size = 0 // Reset to beginningƒ
        this.attachments.map((item) => { this.upload_size += parseInt(item.size) })
        this.upload_size = Number((this.upload_size).toFixed(1))
        this.$forceUpdate()
      },
      prepareFields () {
        if (this.attachments.length > 0) {
          for (var i = 0; i < this.attachments.length; i++) {
            let attachment = this.attachments[i]
            this.data.append('files', attachment)
          }
        }
      },
      removeAttachment (attachment) {
        this.attachments.splice(this.attachments.indexOf(attachment), 1)
        this.getAttachmentSize()
      },
      // This function will be called every time you add a file
      uploadFieldChange (e) {
        var files = e.target.files || e.dataTransfer.files
        if (!files.length) {
          return
        }
        for (var i = files.length - 1; i >= 0; i--) {
          this.attachments.push(files[i])
        }
        // Reset the form to avoid copying these files multiple times into this.attachments
        document.getElementById('attachments').value = []
      },
      submit () {
        this.uploadPending = true
        this.prepareFields()
        const config = {
          headers: {'Content-Type': 'multipart/form-data'},
          onUploadProgress: function (progressEvent) {
            this.percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
            this.$forceUpdate()
          }.bind(this)
        }
        this.$http.post(`api/files`, this.data, config)
          .then(response => {
            this.resetData()
            this.$emit('filesupload')
            this.$toast.success({
              title: 'Success',
              message: 'Successful upload. Please wait'
            })
            this.uploadPending = false
          })
          .catch(e => {
            this.$toast.error({
              title: 'Error',
              message: 'Upload failed'
            })
          })
      },
      // We want to clear the FormData object on every upload so we can re-calculate new files again.
      // Keep in mind that we can delete files as well so in the future we will need to keep track of that as well
      resetData () {
        this.data = new FormData() // Reset it completely
      }
    }
  }
</script>

<style lang="css" scoped>
  .my-label {
    font-size: 90%;
    line-height: 2;
    font-weight: normal;
    background-color: #9a9a9a;
  }

  .margin-left-10 {
    margin-left: 10px;
  }

  .margin-left-100 {
    margin-left: 100px;
  }
</style>
