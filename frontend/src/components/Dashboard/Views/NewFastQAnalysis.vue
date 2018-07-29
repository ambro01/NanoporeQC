<template>
  <div>
    <files-management :analysisType="this.typeName"
                      v-on:filesupload="onFilesUploaded"
                      v-if="!this.showSave && !this.showReport">
    </files-management>
    <analysis-save :analysisType="this.typeName"
                   :qualityStatus="this.basesQualityStatus"
                   @savedAnalysis="onSavedAnalysis"
                   v-if=this.showSave>
    </analysis-save>
    <div class="card" v-if=this.showReport>
      <report-stats-fast-q :id=0
                           :qualityStatus="this.basesQualityStatus"
                           v-if=this.showReport>
      </report-stats-fast-q>
    </div>
  </div>
</template>

<script>
  import FilesManagement from 'src/components/Parts/FilesManagement.vue'
  import AnalysisSave from 'src/components/Parts/AnalysisSave.vue'
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
        showSave: false,
        basesQualityStatus: null
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
          this.getBasesQualityStatus()
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Files loading failed'
          })
        })
      },
      getBasesQualityStatus () {
        this.$http.get(`api/analysis/stats/basesQualityStatus`, {
          params: {
            valuesNames: ['status']
          }
        }).then(response => {
          this.basesQualityStatus = response.data.values['status'][0]
        }).catch(e => {
          console.error(e)
        })
      }
    }
  }
</script>
