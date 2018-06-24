<template>
  <div class="card">
    <div class="header">
      <h4 class="title">Report of analysis</h4>
      <button class="btn btn-primary btn-wd refresh-button left-margin"
              title="Download HTML report from FastQC tool"
              @click.prevent="downloadHtmlReport">
        HTML report
      </button>
      <button class="btn btn-primary btn-wd refresh-button left-margin"
              title="Download CSV data set"
              @click.prevent="exportCsv">
        Export CSV
      </button>
      <button class="btn btn-primary btn-wd refresh-button left-margin"
              title="Send request again"
              @click.prevent="refreshData">
        Refresh
      </button>
    </div>
    <div class="content">
      <vue-tabs active-tab-color="#f4f3ef" @tab-change="handleTabChange">
        <v-tab title="Nucleotides counts"></v-tab>
        <v-tab title="Quality"></v-tab>
        <v-tab title="Quality density"></v-tab>
        <v-tab title="Cycle base call"></v-tab>
        <v-tab title="Cycle quality"></v-tab>
        <v-tab title="Cycle CG Content"></v-tab>
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
        <read-quality-score :chart-data="this.dataReadQuality"></read-quality-score>
        <label class="control-label">
          <p>x - read index</p>
          <p>y - mean base quality</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 2">
        <div v-if="!this.isFromFast5">
          <read-quality-score :chart-data="this.dataReadQualityDensity"></read-quality-score>
          <label class="control-label">
            <p>x - mean base quality</p>
            <p>y - quality density</p>
          </label>
        </div>
        <div v-if="this.isFromFast5">
          <read-quality-density :chart-data="this.dataReadQualityDensity2D"></read-quality-density>
          <label class="control-label">
            <p>x - mean 2d base quality</p>
            <p>y - quality density</p>
          </label>
          <read-quality-density :chart-data="this.dataReadQualityDensityTemplate"></read-quality-density>
          <label class="control-label">
            <p>x - mean template base quality</p>
            <p>y - quality density</p>
          </label>
          <read-quality-density :chart-data="this.dataReadQualityDensityComplement"></read-quality-density>
          <label class="control-label">
            <p>x - mean complement base quality</p>
            <p>y - quality density</p>
          </label>
        </div>
      </div>
      <div v-if="this.tabIndex === 3">
        <per-cycle-base-call :chart-data="this.dataCycleBaseCall"></per-cycle-base-call>
        <label class="control-label">
          <p>x - cycle of base calling</p>
          <p>y - number of read nucleotides</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 4">
        <per-cycle-quality :chart-data="this.dataCycleQuality"></per-cycle-quality>
        <label class="control-label">
          <p>x - cycle of base calling</p>
          <p>y - quality factors</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 5">
        <per-cycle-c-g-content :chart-data="this.dataCycleCGContent"></per-cycle-c-g-content>
        <label class="control-label">
          <p>x - cycle of base calling</p>
          <p>y - CG content</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 6">
        <read-distribution :sourceData="this.dataReadsDistribution"
                           v-if="this.dataReadsDistribution != null"></read-distribution>
        <label class="control-label"></label>
      </div>
      <div v-if="this.tabIndex === 7">
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
  import PerCycleCGContent from 'src/components/Charts/Ioniser/fastq/PerCycleCGContent.vue'
  import ReadQualityScore from 'src/components/Charts/Ioniser/fastq/ReadQualityScore.vue'
  import ReadDistribution from 'src/components/Stats/Ioniser/ReadDistribution.vue'
  import DuplicatedReads from 'src/components/Stats/Ioniser/DuplicatedReads.vue'
  import ReadQualityDensity from 'src/components/Charts/Ioniser/fast5/ReadQualityDensity.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'

  const TEXT_CSV = 'text/csv'
  const TEXT_HTML = 'text/html'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      NucleotideCounts,
      PerCycleBaseCall,
      PerCycleQuality,
      PerCycleCGContent,
      ReadDistribution,
      ReadQualityScore,
      DuplicatedReads,
      ReadQualityDensity
    },
    props: [
      'id',
      'isFromFast5'
    ],
    data () {
      return {
        dataNucleotideCounts: null,
        dataCycleBaseCall: null,
        dataCycleQuality: null,
        dataCycleCGContent: null,
        dataReadQualityDensity: null,
        dataReadsDistribution: null,
        dataDuplicatedReads: null,
        dataReadQualityDensityTemplate: null,
        dataReadQualityDensityComplement: null,
        dataReadQualityDensity2D: null,
        dataReadQuality: null,
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
            valuesNames: ['counts', 'A', 'G', 'C', 'T', 'N']
          }
        }).then(response => {
          this.dataNucleotideCounts = {
            labels: [],
            datasets: [
              {
                label: 'A',
                backgroundColor: '#f87979',
                data: response.data.values['A']
              },
              {
                label: 'G',
                backgroundColor: '#f8ac5f',
                data: response.data.values['G']
              },
              {
                label: 'C',
                backgroundColor: '#47f889',
                data: response.data.values['C']
              },
              {
                label: 'T',
                backgroundColor: '#82c3f8',
                data: response.data.values['T']
              },
              {
                label: 'N',
                backgroundColor: '#8f8792',
                data: response.data.values['N']
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
            valuesNames: ['cycle', 'countA', 'countG', 'countC', 'countT']
          }
        }).then(response => {
          this.dataCycleBaseCall = {
            labels: response.data.values['cycle'],
            datasets: [
              {
                label: 'A',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['countA'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'G',
                borderColor: '#f8ac5f',
                fill: false,
                data: response.data.values['countG'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'C',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['countC'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'T',
                borderColor: '#82c3f8',
                fill: false,
                data: response.data.values['countT'],
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
            valuesNames: ['cycle', 'mean_', 'median_', 'q25', 'q50', 'q75']
          }
        }).then(response => {
          this.dataCycleQuality = {
            labels: response.data.values['cycle'],
            datasets: [
              {
                label: 'mean',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['mean_'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'median',
                borderColor: '#82c3f8',
                fill: false,
                data: response.data.values['median_'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'quantile 25',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['q25'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'quantile 50',
                borderColor: '#f8ac5f',
                fill: false,
                data: response.data.values['q50'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'quantile 75',
                borderColor: '#8f8792',
                fill: false,
                data: response.data.values['q75'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getCycleCGContent () {
        this.$http.get(`api/analysis/stats/perCycleCGContent`, {
          params: {
            valuesNames: ['cycle', 'contentCG']
          }
        }).then(response => {
          this.dataCycleCGContent = {
            labels: response.data.values['cycle'],
            datasets: [
              {
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['contentCG'],
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
            valuesNames: ['file_name', 'occurrences', 'reads']
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
            valuesNames: ['sequence', 'count']
          }
        }).then(response => {
          this.dataDuplicatedReads = response.data
        }).catch(e => {
          console.error(e)
        })
      },
      getReadQualityDensity () {
        if (this.isFromFast5) {
          this.getReadQualityDensityFastQFromFast5()
        } else {
          this.getReadQualityDensityFastQ()
        }
      },
      getReadQualityDensityFastQ () {
        this.$http.get(`api/analysis/stats/readQualityDensityFastQ`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          this.dataReadQualityDensity = {
            labels: response.data.values['quality'],
            datasets: [
              {
                borderColor: '#f8ac5f',
                data: response.data.values['density'],
                pointRadius: 0,
                borderWidth: 2,
                fill: false,
                steppedLine: false
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getReadQualityDensityFastQFromFast5 () {
        this.$http.get(`api/analysis/stats/readQualityDensityFastQFromFast5`, {
          params: {
            valuesNames: ['quality_template', 'quality_complement', 'quality_2D',
              'density_template', 'density_complement', 'density_2D']
          }
        }).then(response => {
          this.dataReadQualityDensity2D = {
            labels: response.data.values['quality_2D'],
            datasets: [
              {
                label: '2D quality density',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['density_2D'],
                pointRadius: 0,
                borderWidth: 2
              }
            ]
          }
          this.dataReadQualityDensityTemplate = {
            labels: response.data.values['quality_template'],
            datasets: [
              {
                label: 'Template quality density',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['density_template'],
                pointRadius: 0,
                borderWidth: 2
              }
            ]
          }
          this.dataReadQualityDensityComplement = {
            labels: response.data.values['quality_complement'],
            datasets: [
              {
                label: 'Complement quality density',
                borderColor: '#d392f8',
                fill: false,
                data: response.data.values['density_complement'],
                pointRadius: 0,
                borderWidth: 2
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getReadQuality () {
        if (this.isFromFast5) {
          this.getReadQualityFastQFromFast5()
        } else {
          this.getReadQualityFastQ()
        }
      },
      getReadQualityFastQ () {
        this.$http.get(`api/analysis/stats/readQualityFastQ`, {
          params: {
            valuesNames: ['id', 'quality']
          }
        }).then(response => {
          this.dataReadQuality = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'Quality',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['quality'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getReadQualityFastQFromFast5 () {
        this.$http.get(`api/analysis/stats/readQualityFastQFromFast5`, {
          params: {
            valuesNames: ['id', 'q_template', 'q_complement', 'q_2D']
          }
        }).then(response => {
          this.dataReadQuality = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'Template quality',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['q_template'],
                pointRadius: 0,
                borderWidth: 2
              },
              {
                label: 'Complement quality',
                borderColor: '#d392f8',
                fill: false,
                data: response.data.values['q_complement'],
                pointRadius: 0,
                borderWidth: 2
              },
              {
                label: '2d quality',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['q_2D'],
                pointRadius: 0,
                borderWidth: 2
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },

      getAllData () {
        this.getNucleotideCounts()
        this.getCycleBaseCall()
        this.getCycleQuality()
        this.getCycleCGContent()
        this.getReadsDistribution()
        this.getDuplicatedReads()
        this.getReadQualityDensity()
        this.getReadQuality()
      },

      refreshData () {
        switch (this.tabIndex) {
          case 0:
            this.getNucleotideCounts()
            break
          case 1:
            this.getReadQuality()
            break
          case 2:
            this.getReadQualityDensity()
            break
          case 3:
            this.getCycleBaseCall()
            break
          case 4:
            this.getCycleQuality()
            break
          case 5:
            this.getCycleCGContent()
            break
          case 6:
            this.getReadsDistribution()
            break
          case 7:
            this.getDuplicatedReads()
            break
        }
      },

      csvNucleotideCounts () {
        this.$http.get(`api/csv/nucleotideCounts`, {
          params: {
            valuesNames: ['counts', 'A', 'G', 'C', 'T', 'N']
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
      csvCycleBaseCall () {
        this.$http.get(`api/csv/perCycleBaseCall`, {
          params: {
            valuesNames: ['cycle', 'countA', 'countG', 'countC', 'countT']
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
            valuesNames: ['cycle', 'mean_', 'median_', 'q25', 'q50', 'q75']
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
      csvCycleCGContent () {
        this.$http.get(`api/csv/perCycleCGContent`, {
          params: {
            valuesNames: ['cycle', 'contentCG']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'per_cycle_cg_content.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadsDistribution () {
        this.$http.get(`api/csv/reads-distribution`, {
          params: {
            valuesNames: ['sequence', 'count']
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
            valuesNames: ['sequence', 'count']
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
      csvReadQuality () {
        if (this.isFromFast5) {
          this.csvReadQualityFastQFromFast5()
        } else {
          this.csvReadQualityFastQ()
        }
      },
      csvReadQualityFastQ () {
        this.$http.get(`api/csv/readQualityFastQ`, {
          params: {
            valuesNames: ['is', 'quality']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'read_quality.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadQualityFastQFromFast5 () {
        this.$http.get(`api/csv/readQualityFastQFromFast5`, {
          params: {
            valuesNames: ['id', 'q_template', 'q_complement', 'q_2D']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'read_quality.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadQualityDensity () {
        if (this.isFromFast5) {
          this.csvReadQualityDensityFastQFromFast5()
        } else {
          this.csvReadQualityDensityFastQ()
        }
      },
      csvReadQualityDensityFastQ () {
        this.$http.get(`api/csv/readQualityDensityFastQ`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'read_quality_density.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadQualityDensityFastQFromFast5 () {
        this.$http.get(`api/csv/readQualityFastQFromFast5`, {
          params: {
            valuesNames: ['quality_template', 'quality_complement', 'quality_2D',
              'density_template', 'density_complement', 'density_2D']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'read_quality_density.csv'
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
            this.csvReadQuality()
            break
          case 2:
            this.csvReadQualityDensity()
            break
          case 3:
            this.csvCycleBaseCall()
            break
          case 4:
            this.csvCycleQuality()
            break
          case 5:
            this.csvCycleCGContent()
            break
          case 6:
            this.csvReadsDistribution()
            break
          case 7:
            this.csvDuplicatedReads()
            break
        }
      },

      downloadHtmlReport () {
        this.$http.get(`api/analysis/download-current-report`).then(response => {
          const blob = new Blob([response.data], {type: TEXT_HTML})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'fastqc_report.html'
          link.click()
        }).catch(e => {
          console.error(e)
        })
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
