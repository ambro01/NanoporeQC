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
        <v-tab title="Reads quality"></v-tab>
        <v-tab title="Reads quality density"></v-tab>
        <v-tab title="Bases calls"></v-tab>
        <v-tab title="Bases quality"></v-tab>
        <v-tab title="Bases quality density"></v-tab>
        <v-tab title="Bases CG content"></v-tab>
        <v-tab title="Sequences distribution"></v-tab>
        <v-tab title="Duplicated sequences"></v-tab>
      </vue-tabs>
      <div v-if="this.tabIndex === 0">
        <nucleotides-counts :chart-data="this.dataNucleotidesCounts"></nucleotides-counts>
        <label class="control-label">
          <p>A - adenine, G - guanine, C - cytosine. T - thymine, N - undefined</p>
          <p>y - number of nucleotides in each category</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 1">
        <reads-quality :chart-data="this.dataReadsQuality"></reads-quality>
        <label class="control-label">
          <p>x - read index</p>
          <p>y - mean base quality</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 2">
        <reads-quality-density :chart-data="this.dataReadsQualityDensity"></reads-quality-density>
        <label class="control-label">
          <p>x - mean base quality</p>
          <p>y - quality density</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 3">
        <bases-calls :chart-data="this.dataBasesCalls"></bases-calls>
        <label class="control-label">
          <p>x - base index in read</p>
          <p>y - number of read nucleotides</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 4">
        <bases-quality :chart-data="this.dataBasesQuality"></bases-quality>
        <label class="control-label">
          <p>x - base index in read</p>
          <p>y - quality factors</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 5">
        <bases-quality-density :chart-data="this.dataBasesQualityDensity"></bases-quality-density>
        <label class="control-label">
          <p>x - base index in read</p>
          <p>y - quality factors</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 6">
        <bases-cg-content :chart-data="this.dataBasesCgContent"></bases-cg-content>
        <label class="control-label">
          <p>x - base index in read</p>
          <p>y - CG content</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 7">
        <sequences-distribution :sourceData="this.dataSequencesDistribution"
                                v-if="this.dataSequencesDistribution != null"></sequences-distribution>
        <label class="control-label"></label>
      </div>
      <div v-if="this.tabIndex === 8">
        <duplicated-sequences :sourceData="this.dataDuplicatedSequences"
                              v-if="this.dataDuplicatedSequences != null"></duplicated-sequences>
        <label class="control-label">Duplicated reads</label>
      </div>
    </div>
  </div>
</template>

<script>
  import NucleotidesCounts from 'src/components/Charts/Ioniser/fastq/NucleotidesCounts.vue'
  import BasesCalls from 'src/components/Charts/Ioniser/fastq/BasesCalls.vue'
  import BasesCgContent from 'src/components/Charts/Ioniser/fastq/BasesCgContent.vue'
  import BasesQuality from 'src/components/Charts/Ioniser/fastq/BasesQuality.vue'
  import BasesQualityDensity from 'src/components/Charts/Ioniser/fastq/BasesQualityDensity.vue'
  import ReadsQuality from 'src/components/Charts/Ioniser/fastq/ReadsQuality.vue'
  import ReadsQualityDensity from 'src/components/Charts/Ioniser/fastq/ReadsQualityDensity.vue'
  import SequencesDistribution from 'src/components/Stats/Ioniser/SequencesDistribution.vue'
  import DuplicatedSequences from 'src/components/Stats/Ioniser/DuplicatedSequences.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'

  const TEXT_CSV = 'text/csv'
  const TEXT_HTML = 'text/html'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      NucleotidesCounts,
      BasesCalls,
      BasesCgContent,
      BasesQuality,
      BasesQualityDensity,
      ReadsQuality,
      ReadsQualityDensity,
      SequencesDistribution,
      DuplicatedSequences
    },
    props: [
      'id'
    ],
    data () {
      return {
        dataNucleotidesCounts: null,
        dataBasesCalls: null,
        dataBasesQuality: null,
        dataBasesQualityDensity: null,
        dataBasesCgContent: null,
        dataReadsQuality: null,
        dataReadsQualityDensity: null,
        dataSequencesDistribution: null,
        dataDuplicatedSequences: null,

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
      getNucleotidesCounts () {
        this.$http.get(`api/analysis/stats/nucleotidesCounts`, {
          params: {
            valuesNames: ['counts', 'A', 'G', 'C', 'T', 'N']
          }
        }).then(response => {
          this.dataNucleotidesCounts = {
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
      getBasesCalls () {
        this.$http.get(`api/analysis/stats/basesCalls`, {
          params: {
            valuesNames: ['id', 'A', 'G', 'C', 'T']
          }
        }).then(response => {
          this.dataBasesCalls = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'A',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['A'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'G',
                borderColor: '#f8ac5f',
                fill: false,
                data: response.data.values['G'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'C',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['C'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'T',
                borderColor: '#82c3f8',
                fill: false,
                data: response.data.values['T'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getBasesQuality () {
        this.$http.get(`api/analysis/stats/basesQuality`, {
          params: {
            valuesNames: ['id', 'quality']
          }
        }).then(response => {
          this.dataBasesQuality = {
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
      getBasesQualityDensity () {
        this.$http.get(`api/analysis/stats/basesQualityDensity`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          this.dataBasesQualityDensity = {
            labels: response.data.values['quality'],
            datasets: [
              {
                label: '',
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
      getBasesCgContent () {
        this.$http.get(`api/analysis/stats/basesCgContent`, {
          params: {
            valuesNames: ['id', 'cgContent']
          }
        }).then(response => {
          this.dataBasesCgContent = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'CG content',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['cgContent'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getSequencesDistribution () {
        this.$http.get(`api/analysis/stats/sequences-distribution`, {
          params: {
            valuesNames: ['fileName', 'occurrences', 'reads']
          }
        }).then(response => {
          this.dataSequencesDistribution = response.data
        }).catch(e => {
          console.error(e)
        })
      },
      getDuplicatedSequences () {
        this.$http.get(`api/analysis/stats/duplicated-sequences`, {
          params: {
            valuesNames: ['sequence', 'count']
          }
        }).then(response => {
          this.dataDuplicatedSequences = response.data
        }).catch(e => {
          console.error(e)
        })
      },
      getReadsQualityDensity () {
        this.$http.get(`api/analysis/stats/readsQualityDensity`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          this.dataReadsQualityDensity = {
            labels: response.data.values['quality'],
            datasets: [
              {
                label: '',
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
      getReadsQuality () {
        this.$http.get(`api/analysis/stats/readsQuality`, {
          params: {
            valuesNames: ['id', 'quality']
          }
        }).then(response => {
          this.dataReadsQuality = {
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

      getAllData () {
        this.getNucleotidesCounts()
        this.getBasesCalls()
        this.getBasesQuality()
        this.getBasesQualityDensity()
        this.getBasesCgContent()
        this.getReadsQuality()
        this.getReadsQualityDensity()
        this.getSequencesDistribution()
        this.getDuplicatedSequences()
      },

      refreshData () {
        switch (this.tabIndex) {
          case 0:
            this.getNucleotidesCounts()
            break
          case 1:
            this.getReadsQuality()
            break
          case 2:
            this.getReadsQualityDensity()
            break
          case 3:
            this.getBasesCalls()
            break
          case 4:
            this.getBasesQuality()
            break
          case 5:
            this.getBasesQualityDensity()
            break
          case 6:
            this.getBasesCgContent()
            break
          case 7:
            this.getSequencesDistribution()
            break
          case 8:
            this.getDuplicatedSequences()
            break
        }
      },

      csvNucleotidesCounts () {
        this.$http.get(`api/csv/nucleotidesCounts`, {
          params: {
            valuesNames: ['counts', 'A', 'G', 'C', 'T', 'N']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'nucleotides_counts.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvBaseCalls () {
        this.$http.get(`api/csv/basesCalls`, {
          params: {
            valuesNames: ['id', 'A', 'G', 'C', 'T']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'bases_calls.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvBasesQuality () {
        this.$http.get(`api/csv/basesQuality`, {
          params: {
            valuesNames: ['id', 'quality', 'mean', 'median', 'q25', 'q50', 'q75']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'bases_quality.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvBasesQualityDensity () {
        this.$http.get(`api/csv/basesQualityDensity`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'bases_quality_density.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvBasesCGContent () {
        this.$http.get(`api/csv/basesCgContent`, {
          params: {
            valuesNames: ['id', 'cgContent']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'bases_cg_content.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvSequencesDistribution () {
        this.$http.get(`api/csv/sequences-distribution`, {
          params: {
            valuesNames: ['sequence', 'count']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'rsequences_distribution.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvDuplicatedSequences () {
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
      csvReadsQuality () {
        this.$http.get(`api/csv/readsQuality`, {
          params: {
            valuesNames: ['id', 'quality', 'mean', 'median', 'q25', 'q50', 'q75']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'reads_quality.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadsQualityDensity () {
        this.$http.get(`api/csv/readsQualityDensity`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'reads_quality_density.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },

      exportCsv () {
        switch (this.tabIndex) {
          case 0:
            this.csvNucleotidesCounts()
            break
          case 1:
            this.csvReadsQuality()
            break
          case 2:
            this.csvReadsQualityDensity()
            break
          case 3:
            this.csvBaseCalls()
            break
          case 4:
            this.csvBasesQuality()
            break
          case 5:
            this.csvBasesQualityDensity()
            break
          case 6:
            this.csvBasesCGContent()
            break
          case 7:
            this.csvSequencesDistribution()
            break
          case 8:
            this.csvDuplicatedSequences()
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
