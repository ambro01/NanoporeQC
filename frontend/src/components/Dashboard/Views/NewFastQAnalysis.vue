<template>
  <div>
    <files-management :analysisType="this.typeName" v-on:filesupload="onFilesUploaded"
                      v-if="!this.showSave && !this.showReport"></files-management>
    <analysis-save :analysisType="this.typeName" @savedAnalysis="onSavedAnalysis" v-if=this.showSave></analysis-save>
    <div class="card" v-if=this.showReport>
      <report-stats-fast-q :id=0 v-if=this.showReport></report-stats-fast-q>
    </div>
  </div>
</template>

<script>
  import FilesManagement from 'src/components/UIComponents/Inputs/FilesManagement.vue'
  import AnalysisSave from 'src/components/UIComponents/Inputs/AnalysisSave.vue'
  import ReportStatsFastQ from 'src/components/Dashboard/Views/ReportStatsFastQ.vue'

  export default {
    components: {
      FilesManagement,
      AnalysisSave,
      ReportStatsFastQ
    },
    data () {
      return {
        typeName: 'FastQ',
        loading: false,
        percent: null,
        showReport: false,
        showSave: false
      }
    },
    methods: {
      onSavedAnalysis () {
        this.showSave = false
      },
      onFilesUploaded () {
        this.$http.post(`api/analysis/run-new`, {
          type: this.typeName
        }).then(response => {
          this.showSave = true
          this.showReport = true
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Files loading failed'
          })
        })
      },
      loadAnalysis () {
        this.$http.get(`api/analysis/0`, {
          params: {
            type: 'FastQ'
          }
        }).then(response => {
          this.reloadData = true
          this.$toast.success({
            title: 'Success',
            message: 'Successful data loading'
          })
          this.showSave = true
          this.showReport = true
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Data loading failed'
          })
        })
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
