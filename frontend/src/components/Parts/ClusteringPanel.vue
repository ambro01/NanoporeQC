<template>
  <div class="card">
    <div class="header">
    </div>
    <div class="content">
      <form>
        <div class="row">

        </div>
        <div class="row">
          <div class="col-md-12">
            <div class="col-md-4">
              <h4 class="title">Kmeans clustering of reads</h4>
              <br>
              <div class="col-md-6">
                <fg-input type="number"
                          v-model="kmeansClustersNumber"
                          label="Clusters number"
                          placeholder="Enter value">
                </fg-input>
              </div>
              <div class="col-md-6">
                <div class="col-md-6">
                  <button class="btn btn-primary btn-fill clustering-button"
                          :disabled="this.kmeansClustersNumber <= 0"
                          @click.prevent="getKmeansClusteringReads">
                    Run clustering
                  </button>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <h4 class="title">EM clustering of reads</h4>
              <br>
              <div class="col-md-6">
                <fg-input type="number"
                          v-model="emClustersNumber"
                          label="Clusters number"
                          placeholder="Enter value">
                </fg-input>
              </div>
              <div class="col-md-6">
                <div class="col-md-6">
                  <button class="btn btn-primary btn-fill clustering-button"
                          @click.prevent="getEmClusteringReads">
                    Run clustering
                  </button>
                </div>
              </div>
            </div>
            <button class="btn btn-primary btn-wd refresh-button"
                    title="Download CSV data set"
                    :disabled="lastUsedMethod == null"
                    @click.prevent="csvClusteringReads">
              Export CSV
            </button>
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
  import CsvService from 'src/components/Services/CsvService.vue'

  export default {
    components: {
      ClusteringReads
    },
    props: [
      'analysisType'
    ],
    data () {
      return {
        kmeansClustersNumber: null,
        emClustersNumber: null,
        dataClusteringReads: [],
        lastUsedMethod: null,
        lastUsedClusters: null
      }
    },
    methods: {
      getKmeansClusteringReads () {
        this.$http.get(`api/analysis/clustering/reads/kmeans/` + this.analysisType, {
          params: {
            clustersNumber: this.kmeansClustersNumber
          }
        }).then(response => {
          this.dataClusteringReads = response.data
          this.lastUsedMethod = 'kmeans'
          this.lastUsedClusters = this.kmeansClustersNumber
          if (response.status === 200) {
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
      },

      getEmClusteringReads () {
        const clusters = this.emClustersNumber > 0 ? this.emClustersNumber : 0
        this.$http.get(`api/analysis/clustering/reads/mclust/` + this.analysisType, {
          params: {
            clustersNumber: clusters
          }
        })
          .then(response => {
            this.lastUsedMethod = 'mclust'
            this.lastUsedClusters = clusters
            this.dataClusteringReads = response.data
            this.$toast.success({
              title: 'Success',
              message: 'Successful clustering'
            })
          })
          .catch(() => this.$toast.error())
      },

      csvClusteringReads () {
        CsvService.methods.csvClusteringReads(this.lastUsedMethod, this.lastUsedClusters)
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
