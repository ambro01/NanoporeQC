<template>
  <div>
    <files-management :analysisType="this.typeName"
                      v-on:filesupload="onFilesUploaded"
                      v-if="!this.showSave && !this.showReport">
    </files-management>
    <analysis-save :analysisType="this.typeName"
                   :qualityStatus="this.readsQualityStatus"
                   @savedAnalysis="onSavedAnalysis"
                   v-if=this.showSave>
    </analysis-save>
    <div class="card" v-if=this.showReport>
      <report-stats-fast5 :id=0
                          :qualityStatus="this.readsQualityStatus"
                          v-if=this.showReport>
      </report-stats-fast5>
    </div>
  </div>
</template>

<script>
  import FilesManagement from 'src/components/Parts/FilesManagement.vue'
  import AnalysisSave from 'src/components/Parts/AnalysisSave.vue'
  import ReportStatsFast5 from 'src/components/Dashboard/Views/ReportStatsFast5.vue'

  export default {
    components: {
      FilesManagement,
      AnalysisSave,
      ReportStatsFast5
    },
    data () {
      return {
        typeName: 'Fast5',
        loading: false,
        percent: null,
        showReport: false,
        showSave: false,
        readsQualityStatus: null
      }
    },
    methods: {
      onSavedAnalysis () {
        this.showSave = false
      },
      onFilesUploaded () {
        this.$http.post(`api/analysis/run-new`, {
          type: this.typeName
        }).then(() => {
          this.$toast.success({
            title: 'Success',
            message: 'Successful data loading'
          })
          this.showSave = true
          this.showReport = true
          this.getReadsQualityStatus()
        }).catch(() => {
          this.$toast.error({
            title: 'Error',
            message: 'Files loading failed'
          })
        })
      },
      getReadsQualityStatus () {
        this.$http.get(`api/analysis/stats/readsQualityStatus`, {
          params: {
            valuesNames: ['status']
          }
        }).then(response => {
          this.readsQualityStatus = response.data.values['status'][0]
        }).catch(() => this.$toast.error())
      }
    }
  }
</script>
