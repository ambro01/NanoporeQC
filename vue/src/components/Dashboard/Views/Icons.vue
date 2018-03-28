<template>
  <div class="row">
    <div class="col-md-12">
      <div class="card">
        <div>
          <files-management></files-management>
          <attachemnt-list></attachemnt-list>
        </div>
      </div>
    </div>

  </div>
</template>
<script>

  import FilesManagement from 'src/components/UIComponents/Inputs/FilesManagement.vue'
  import AttachemntList from 'src/components/UIComponents/Inputs/AttachmentList.vue'

  export default {
    components: {
      FilesManagement,
      AttachemntList
    },
    data () {
      return {
        loading: false,
        percent: null
      }
    },
    methods: {},

    created () {
      console.log('App created')
      Event.listen('percent', function (percent) {
        console.log('Received Upload Percent Status!')
        this.percent = percent
      }.bind(this))

      Event.listen('loading_on', function () {
        console.log('Received Loading ON Event!')
        this.loading = true
      }.bind(this))

      Event.listen('loading_off', function () {
        console.log('Received Loading OFF Event!')
        this.loading = false
      }.bind(this))
    }}
</script>
