<template>
  <div>
      <div class="card">
          <files-management v-on:filesupload="onFilesUploaded"></files-management>
          <!--<attachment-list></attachment-list>-->
      </div>
    <div class="card">
      <analyse-save @runAnalyse="onRunAnalyse" v-if=this.showSaveAndRun></analyse-save>
    </div>
    <div class="card" v-if=this.showReport>
      <report-charts :id=0 :updateTrigger="this.showReport"></report-charts>
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
        showReport: false,
        showSaveAndRun: false
      }
    },
    methods: {
      onRunAnalyse () {
        this.showReport = true
        this.showSaveAndRun = false
      },
      onFilesUploaded () {
        this.showSaveAndRun = true
        this.showReport = false
      }
    },
    watch: {
      percent: function (percent) {
        console.log('Received Upload Percent Status!')
        this.percent = percent
      },
      loading_on: function () {
        console.log('Received Loading ON Event!')
        this.loading = true
      },
      loading_off: function () {
        console.log('Received Loading OFF Event!')
        this.loading = false
      }
    }
}
</script>
