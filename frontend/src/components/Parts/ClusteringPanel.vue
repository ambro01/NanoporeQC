<template>
  <div class="card">
    <div class="header">
      <h4 class="title">Reads clustering</h4>
    </div>
    <div class="content">
      <form>
        <div class="row">
          <div class="col-md-12">
            <div class="col-md-2">
              <fg-input type="number"
                        v-model="clustersNumber"
                        label="Clusters number"
                        placeholder="Enter value">
              </fg-input>
            </div>
            <div class="col-md-2">
              <div class="col-md-2">
                <button class="btn btn-primary btn-fill clustering-button"
                        :disabled="this.clustersNumber <= 0"
                        @click.prevent="getClusteringReads">
                  Run clustering
                </button>
              </div>
            </div>
          </div>
          <div class="col-md-12">
            <clustering-reads
              :sourceData="this.dataClusteringReads">
            </clustering-reads>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
  import ClusteringReads from 'src/components/Stats/ClusteringReads.vue'

  export default {
    components: {
      ClusteringReads
    },
    props: [
      'analysisType'
    ],
    data () {
      return {
        clustersNumber: null,
        dataClusteringReads: []
      }
    },
    methods: {
      getClusteringReads () {
        this.$http.get(`api/analysis/clustering/reads/` + this.analysisType, {
          params: {
            clustersNumber: this.clustersNumber
          }
        }).then(response => {
          this.dataClusteringReads = response.data
          if (response.status === 200) {
            this.$emit('savedAnalysis')
            this.$toast.success({
              title: 'Success',
              message: 'Successful clustering'
            })
          } else {
            this.$toast.error({
              title: 'Error',
              message: 'Clustering failed'
            })
          }
        })
      }
    }
  }
</script>
<style lang="scss">
  .clustering-button {
    margin-top: 20px;
    width: 150px;
  }
</style>
