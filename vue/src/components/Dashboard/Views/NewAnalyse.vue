<template>
  <div>
      <div class="card">
          <files-management v-on:filesupload="onFilesUploaded"></files-management>
          <!--<attachment-list></attachment-list>-->
      </div>
    <div class="card">
      <analyse-save v-on:analyseResults="onAnalyseResults" v-if=this.filesUploaded></analyse-save>
    </div>
    <div class="card" v-if=this.analyseResults>
      <report-charts :id=0></report-charts>
    </div>
  </div>
</template>

<script>
  import FilesManagement from 'src/components/UIComponents/Inputs/FilesManagement.vue'
  import AnalyseSave from 'src/components/UIComponents/Inputs/AnalyseSave.vue'
  import ReportCharts from 'src/components/Dashboard/Views/ReportCharts.vue'

  export default {
    components: {
      FilesManagement,
      AnalyseSave,
      ReportCharts
    },
    data () {
      return {
        loading: false,
        percent: null,
        analyseResults: false,
        filesUploaded: false
      }
    },
    methods: {
      onAnalyseResults (newValue) {
        if (this.analyseResults === newValue) {
          this.analyseResults = !newValue
        }
        this.analyseResults = newValue
      },
      onFilesUploaded (newValue) {
        this.filesUploaded = newValue
      }
    },

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
