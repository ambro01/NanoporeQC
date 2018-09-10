<template>
  <div class="card">
    <div class="header">
    </div>
    <div class="content">
      <form>
        <div class="row">
          <div class="col-md-12">
            <div class="col-md-4">
              <h4 class="title">Outlying reads are detected by kmeans algorithm.</h4>
              <br>
              <h4 class="title">Results are sorted by descending distance from center of cluster.</h4>
              <br>
              <div class="col-md-6">
                <fg-input type="number"
                          v-model="outliersProportion"
                          label="Percentage proportion of outlying reads"
                          placeholder="Enter value in %">
                </fg-input>
              </div>
              <div class="col-md-6">
                <div class="col-md-6">
                  <button class="btn btn-primary btn-fill clustering-button"
                          :disabled="this.outliersProportion < 0 || this.outliersProportion >= 100"
                          @click.prevent="runOutliersDetection">
                    Detect outliers
                  </button>
                </div>
              </div>
            </div>
            <button class="btn btn-primary btn-wd refresh-button"
                    title="Download CSV data set"
                    @click.prevent="csvOutliersDetection">
              Export CSV
            </button>
          </div>
          <div class="col-md-12">
            <outliers
              :sourceData="this.dataOutliersDetection">
            </outliers>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
  import Outliers from 'src/components/Stats/Outliers.vue'
  import CsvService from 'src/components/Services/CsvService.vue'

  export default {
    components: {
      Outliers
    },
    props: [
      'analysisType'
    ],
    data () {
      return {
        outliersProportion: null,
        dataOutliersDetection: []
      }
    },
    methods: {
      runOutliersDetection () {
        const outliers = this.outliersProportion > 0 ? this.outliersProportion : 100
        this.$http.get(`api/analysis/outliers-detection/reads/` + this.analysisType, {
          params: {
            outliersProportion: outliers
          }
        })
          .then(response => {
            this.dataOutliersDetection = response.data
            this.$toast.success({
              title: 'Success',
              message: 'Successful clustering'
            })
          })
          .catch(() => this.$toast.error())
      },

      csvOutliersDetection () {
        CsvService.methods.csvOutliersDetection()
      }
    }
  }
</script>
<style lang="scss">
  .clustering-button {
    margin-top: 20px;
    width: 150px;
  }

  .refresh-button {
    float: right;
  }
</style>
