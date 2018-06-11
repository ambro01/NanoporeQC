<template>
  <div class="card">
    <div class="header">
      <h4 class="title">Report of analysis</h4>
      <button class="btn btn-primary btn-wd refresh-button left-margin" @click.prevent="exportCsv">Export CSV</button>
      <button class="btn btn-primary btn-wd refresh-button left-margin" @click.prevent="refreshData">Refresh</button>
    </div>
    <div class="content">
      <vue-tabs active-tab-color="#f4f3ef" @tab-change="handleTabChange">
        <v-tab title="Nucleotides counts"></v-tab>
        <v-tab title="Read quality distribution"></v-tab>
        <v-tab title="Cycle base call"></v-tab>
        <v-tab title="Cycle quality"></v-tab>
        <v-tab title="Reads distribution"></v-tab>
        <v-tab title="Multiplicated reads"></v-tab>
      </vue-tabs>
      <div v-if="this.tabIndex === 0">
        <nucleotide-counts :chart-data="this.dataNucleotideCounts"></nucleotide-counts>
        <label class="control-label">
          <p>A - adenine, G - guanine, C - cytosine. T - thymine, N - undefined</p>
          <p>y - number of nucleotides in each category</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 1">
        <read-quality-score :chart-data="this.dataReadQualityScore"></read-quality-score>
        <label class="control-label">
          <p>x - mean base quality</p>
          <p>y - proportion of reads</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 2">
        <per-cycle-base-call :chart-data="this.dataCycleBaseCall"></per-cycle-base-call>
        <label class="control-label">
          <p>x - cycle of base calling</p>
          <p>y - number of read nucleotides</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 3">
        <per-cycle-quality :chart-data="this.dataCycleQuality"></per-cycle-quality>
        <label class="control-label">
          <p>x - cycle of base calling</p>
          <p>y - quality factors</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 4">
        <read-distribution :sourceData="this.dataReadsDistribution"
                           v-if="this.dataReadsDistribution != null"></read-distribution>
        <label class="control-label"></label>
      </div>
      <div v-if="this.tabIndex === 5">
        <duplicated-reads :sourceData="this.dataDuplicatedReads" v-if="dataDuplicatedReads != null"></duplicated-reads>
        <label class="control-label">Duplicated reads</label>
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

  const TEXT_CSV = 'text/csv'

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
      'id'
    ],
    data () {
      return {
        dataNucleotideCounts: null,
        dataCycleBaseCall: null,
        dataCycleQuality: null,
        dataReadQualityScore: null,
        dataReadsDistribution: null,
        dataDuplicatedReads: null,
        tabIndex: 0
      }
    },
    watch: {
      id: function (newVal, oldVal) {
        if (oldVal !== newVal && newVal > 0) {
          this.getAllData()
        }
      }
    },
    mounted () {
      this.getAllData()
    },
    methods: {
      handleTabChange (tabIndex, newTab, oldTab) {
        this.tabIndex = tabIndex
      },
      getNucleotideCounts () {
        this.$http.get(`api/analysis/stats/nucleotideCounts`, {
          params: {
            xName: 'counts',
            yNames: ['A', 'G', 'C', 'T', 'N']
          }
        }).then(response => {
          this.dataNucleotideCounts = {
            labels: [ ],
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
      getReadQualityScore () {
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
                pointRadius: 0,
                steppedLine: false
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
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'G',
                borderColor: '#f8ac5f',
                fill: false,
                data: response.data.yvaluesList[1],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'C',
                borderColor: '#47f889',
                fill: false,
                data: response.data.yvaluesList[2],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'T',
                borderColor: '#82c3f8',
                fill: false,
                data: response.data.yvaluesList[3],
                pointRadius: 0,
                borderWidth: 1
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
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'median',
                borderColor: '#82c3f8',
                fill: false,
                data: response.data.yvaluesList[1],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'quantile 25',
                borderColor: '#47f889',
                fill: false,
                data: response.data.yvaluesList[2],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'quantile 50',
                borderColor: '#f8ac5f',
                fill: false,
                data: response.data.yvaluesList[3],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'quantile 75',
                borderColor: '#8f8792',
                fill: false,
                data: response.data.yvaluesList[3],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getReadsDistribution () {
        this.$http.get(`api/analysis/stats/reads-distribution`, {
          params: {
            xName: 'sequence',
            yNames: ['count']
          }
        }).then(response => {
          this.dataReadsDistribution = response.data
        }).catch(e => {
          console.error(e)
        })
      },
      getDuplicatedReads () {
        this.$http.get(`api/analysis/stats/duplicated-sequences`, {
          params: {
            xName: 'sequence',
            yNames: ['count']
          }
        }).then(response => {
          this.dataDuplicatedReads = response.data
        }).catch(e => {
          console.error(e)
        })
      },

      getAllData () {
        this.getNucleotideCounts()
        this.getReadQualityScore()
        this.getCycleBaseCall()
        this.getCycleQuality()
        this.getReadsDistribution()
        this.getDuplicatedReads()
      },

      refreshData () {
        switch (this.tabIndex) {
          case 0:
            this.getNucleotideCounts()
            break
          case 1:
            this.getReadQualityScore()
            break
          case 2:
            this.getCycleBaseCall()
            break
          case 3:
            this.getCycleQuality()
            break
          case 4:
            this.getReadsDistribution()
            break
          case 5:
            this.getDuplicatedReads()
            break
        }
      },

      csvNucleotideCounts () {
        this.$http.get(`api/csv/nucleotideCounts`, {
          params: {
            xName: 'counts',
            yNames: ['A', 'G', 'C', 'T', 'N']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'nucleotide_counts.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadQualityScore () {
        this.$http.get(`api/csv/readQualityScore`, {
          params: {
            xName: 'quality',
            yNames: ['density']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'read_quality_score.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvCycleBaseCall () {
        this.$http.get(`api/csv/perCycleBaseCall`, {
          params: {
            xName: 'cycle',
            yNames: ['countA', 'countG', 'countC', 'countT']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'per_cycle_base_call.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvCycleQuality () {
        this.$http.get(`api/csv/perCycleQuality`, {
          params: {
            xName: 'cycle',
            yNames: ['mean_', 'median_', 'q25', 'q50', 'q75']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'per_cycle_quality.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadsDistribution () {
        this.$http.get(`api/csv/reads-distribution`, {
          params: {
            xName: 'sequence',
            yNames: ['count']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'reads_distribution.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvDuplicatedReads () {
        this.$http.get(`api/csv/duplicated-sequences`, {
          params: {
            xName: 'sequence',
            yNames: ['count']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'duplicated_sequences.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },

      exportCsv () {
        switch (this.tabIndex) {
          case 0:
            this.csvNucleotideCounts()
            break
          case 1:
            this.csvReadQualityScore()
            break
          case 2:
            this.csvCycleBaseCall()
            break
          case 3:
            this.csvCycleQuality()
            break
          case 4:
            this.csvReadsDistribution()
            break
          case 5:
            this.csvDuplicatedReads()
            break
        }
      }
    }
  }
</script>

<style lang="css" scoped>
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

  .refresh-button {
    float: right;
  }

  .left-margin {
    margin-left: 20px;
  }
</style>
