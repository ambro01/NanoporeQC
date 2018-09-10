<template>
  <div class="card">
    <div class="header">
    </div>
    <div class="content">
      <form>
        <div class="row">
          <div class="col-md-12">
            <div class="col-md-6">
              <h4 class="title">2D reads detection. Reads are clustered into two groups: 2D and Template/Complement.</h4>
              <div class="col-md-6">
                <div class="col-md-6">
                  <button class="btn btn-primary btn-fill clustering-button"
                          @click.prevent="runD2Detection">
                    Run detection
                  </button>
                </div>
              </div>
            </div>
            <button class="btn btn-primary btn-wd refresh-button"
                    title="Download CSV data set"
                    @click.prevent="csvD2Detection">
              Export CSV
            </button>
          </div>
          <div class="col-md-12">
            <d2-detection
              :sourceData="this.dataD2Detection">
            </d2-detection>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
  import D2Detection from 'src/components/Stats/D2Detection.vue'
  import CsvService from 'src/components/Services/CsvService.vue'

  export default {
    components: {
      D2Detection
    },
    props: [
      'analysisType'
    ],
    data () {
      return {
        dataD2Detection: []
      }
    },
    methods: {
      runD2Detection () {
        this.$http.get(`api/analysis/d2-detection/reads/` + this.analysisType)
          .then(response => {
            this.dataD2Detection = response.data
            this.$toast.success({
              title: 'Success',
              message: 'Successful clustering'
            })
          })
          .catch(() => this.$toast.error())
      },

      csvD2Detection () {
        CsvService.methods.csvD2Detection()
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
