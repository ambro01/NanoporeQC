<template>
  <div class="card">
    <div class="header">
      <h4 class="title">Report of analysis</h4>
    </div>
    <div class="content">
      <vue-tabs active-tab-color="#f4f3ef" @tab-change="handleTabChange">
        <v-tab title="Nucleotide counts"></v-tab>
        <v-tab title="Read quality"></v-tab>
        <v-tab title="Cycle base call"></v-tab>
        <v-tab title="Cycle quality"></v-tab>
        <v-tab title="Reads distribution"></v-tab>
        <v-tab title="Duplicated reads"></v-tab>
      </vue-tabs>
      <div v-if="this.tabIndex === 0">
        <nucleotide-counts :chart-data="this.dataNucleotideCounts"></nucleotide-counts>
        <label class="control-label">Number of each nucleotide (N - undefined)</label>
      </div>
      <div v-if="this.tabIndex === 1">
        <read-quality-score :chart-data="this.dataReadQualityScore"></read-quality-score>
        <label class="control-label">Proportion of reads per average base quality</label>
      </div>
      <div v-if="this.tabIndex === 2">
        <per-cycle-base-call :chart-data="this.dataCycleBaseCall"></per-cycle-base-call>
        <label class="control-label">Number of nucleotides read per each cycle</label>
      </div>
      <div v-if="this.tabIndex === 3">
        <per-cycle-quality :chart-data="this.dataCycleQuality"></per-cycle-quality>
        <label class="control-label">Quality factors per each cycle</label>
      </div>
      <div v-if="this.tabIndex === 4">
        <read-distribution :id="this.id" :reloadData="this.reloadDataReadDistribution"
                           @reloadDataEvent='resetReloadDataReadDistribution'></read-distribution>
      </div>
      <div v-if="this.tabIndex === 5">
        <duplicated-reads :id="this.id" :reloadData="this.reloadDataDuplicatedReads"
                          @reloadDataEvent='resetReloadDataDuplicatedReads'></duplicated-reads>
      </div>
    </div>
  </div>
</template>

<script>
  import NucleotideCounts from 'src/components/Charts/Ioniser/fastq/NucleotideCounts.vue'
  import PerCycleBaseCall from 'src/components/Charts/Ioniser/fastq/PerCycleBaseCall.vue'
  import PerCycleQuality from 'src/components/Charts/Ioniser/fastq/PerCycleQuality.vue'
  import ReadQualityScore from 'src/components/Charts/Ioniser/fastq/ReadQualityScore.vue'
  import ReadDistribution from 'src/components/Stats/Ioniser/ReadDistribution.vue'
  import DuplicatedReads from 'src/components/Stats/Ioniser/DuplicatedReads.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      NucleotideCounts,
      PerCycleBaseCall,
      PerCycleQuality,
      ReadDistribution,
      ReadQualityScore,
      DuplicatedReads
    },
    props: [
      'id',
      'updateTrigger'
    ],
    data () {
      return {
        reloadDataDuplicatedReads: false,
        reloadDataReadDistribution: false,
        dataNucleotideCounts: null,
        dataCycleBaseCall: null,
        dataCycleQuality: null,
        dataReadDistribution: null,
        dataReadQualityScore: null,
        tabIndex: 0
      }
    },
    watch: {
      id: function (newVal, oldVal) {
        if (oldVal !== newVal) {
          if (newVal > 0) {
            this.loadData()
          }
        }
      },
      updateTrigger: function (newVal, oldVal) {
        if (!oldVal && newVal) {
          this.loadData()
        }
      }
    },
    mounted () {
      if (this.id > 0) {
        this.loadData()
      }
      this.getNucleotideCounts()
    },
    methods: {
      handleTabChange (tabIndex, newTab, oldTab) {
        this.tabIndex = tabIndex
        switch (tabIndex) {
          case 0:
            this.getNucleotideCounts()
            break
          case 1:
            this.getReadQuality()
            break
          case 2:
            this.getCycleBaseCall()
            break
          case 3:
            this.getCycleQuality()
            break
          case 4:
            this.reloadDataReadDistribution = true
            break
          case 5:
            this.reloadDataDuplicatedReads = true
            break
        }
      },
      loadData () {
        console.log('aaa')
        this.$http.get(`api/analysis/` + this.id, {
          params: {
            type: 'FastQ'
          }
        }).then(response => {
          if (response.status === 200) {
            this.reloadData = true
            this.$toast.success({
              title: 'Success',
              message: 'Successful data loading'
            })
          } else {
            this.$toast.error({
              title: 'Error',
              message: 'Data loading failed'
            })
          }
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Data loading failed'
          })
        })
      },
      resetReloadDataDuplicatedReads () {
        this.reloadDataDuplicatedReads = false
      },
      resetReloadDataReadDistribution () {
        this.reloadDataReadDistribution = false
      },
      getNucleotideCounts () {
        this.$http.get(`api/analysis/stats/nucleotideCounts/`, {
          params: {
            xName: 'counts',
            yNames: ['A', 'G', 'C', 'T', 'N']
          }
        }).then(response => {
          this.dataNucleotideCounts = {
            labels: ['counts'],
            datasets: [
              {
                label: 'A',
                backgroundColor: '#f87979',
                data: response.data.yvaluesList[0]
              },
              {
                label: 'G',
                backgroundColor: '#f8ac5f',
                data: response.data.yvaluesList[1]
              },
              {
                label: 'C',
                backgroundColor: '#47f889',
                data: response.data.yvaluesList[2]
              },
              {
                label: 'T',
                backgroundColor: '#82c3f8',
                data: response.data.yvaluesList[3]
              },
              {
                label: 'N',
                backgroundColor: '#8f8792',
                data: response.data.yvaluesList[4]
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getReadQuality () {
        this.$http.get(`api/analysis/stats/readQualityScore`, {
          params: {
            xName: 'quality',
            yNames: ['density']
          }
        }).then(response => {
          this.dataReadQualityScore = {
            labels: response.data.xvalues,
            datasets: [
              {
                backgroundColor: '#f87979',
                data: response.data.yvaluesList[0],
                pointRadius: 0
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getCycleBaseCall () {
        this.$http.get(`api/analysis/stats/perCycleBaseCall`, {
          params: {
            xName: 'cycle',
            yNames: ['countA', 'countG', 'countC', 'countT']
          }
        }).then(response => {
          this.dataCycleBaseCall = {
            labels: response.data.xvalues,
            datasets: [
              {
                label: 'A',
                borderColor: '#f87979',
                fill: false,
                data: response.data.yvaluesList[0],
                pointRadius: 0
              },
              {
                label: 'G',
                borderColor: '#f8ac5f',
                fill: false,
                data: response.data.yvaluesList[1],
                pointRadius: 0
              },
              {
                label: 'C',
                borderColor: '#47f889',
                fill: false,
                data: response.data.yvaluesList[2],
                pointRadius: 0
              },
              {
                label: 'T',
                borderColor: '#82c3f8',
                fill: false,
                data: response.data.yvaluesList[3],
                pointRadius: 0
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getCycleQuality () {
        this.$http.get(`api/analysis/stats/perCycleQuality`, {
          params: {
            xName: 'cycle',
            yNames: ['mean_', 'median_', 'q25', 'q50', 'q75']
          }
        }).then(response => {
          this.dataCycleQuality = {
            labels: response.data.xvalues,
            datasets: [
              {
                label: 'mean',
                borderColor: '#f87979',
                fill: false,
                data: response.data.yvaluesList[0],
                pointRadius: 0
              },
              {
                label: 'median',
                borderColor: '#82c3f8',
                fill: false,
                data: response.data.yvaluesList[1],
                pointRadius: 0
              },
              {
                label: 'quantile 25',
                borderColor: '#47f889',
                fill: false,
                data: response.data.yvaluesList[2],
                pointRadius: 0
              },
              {
                label: 'quantile 50',
                borderColor: '#f8ac5f',
                fill: false,
                data: response.data.yvaluesList[3],
                pointRadius: 0
              },
              {
                label: 'quantile 75',
                borderColor: '#8f8792',
                fill: false,
                data: response.data.yvaluesList[3],
                pointRadius: 0
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      }
    }
  }
</script>

<style scoped>
  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
</style>
