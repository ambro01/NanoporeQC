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
              @click.prevent="exportCsv">
        Export CSV
      </button>
      <button class="btn btn-primary btn-wd refresh-button left-margin"
              @click.prevent="refreshData">
        Refresh
      </button>
    </div>
    <div class="content">
      <vue-tabs active-tab-color="#f4f3ef" @tab-change="handleTabChange">
        <v-tab title="Summary info"></v-tab>
        <v-tab title="Read accumulation"></v-tab>
        <v-tab title="Active channels"></v-tab>
        <v-tab title="Read quality"></v-tab>
        <v-tab title="Read quality density"></v-tab>
        <v-tab title="Read quality factors"></v-tab>
        <v-tab title="Read category counts"></v-tab>
        <v-tab title="Events"></v-tab>
        <v-tab title="Reads per channel"></v-tab>
        <v-tab title="Kilobases per channel"></v-tab>
      </vue-tabs>
      <div v-if="this.tabIndex === 0">
        <summary-info :sourceData="this.dataSummaryInfo" v-if="this.dataSummaryInfo != null"></summary-info>
        <label class="control-label">
          <p>Summary information about files of analysis</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 1">
        <read-accumulation :chart-data="this.dataReadAccumulation"></read-accumulation>
        <label class="control-label">
          <p>x - duration of an experiment [sec]</p>
          <p>y - accumulation of reads</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 2">
        <active-channels :chart-data="this.dataActiveChannels"></active-channels>
        <label class="control-label">
          <p>x - duration of an experiment [sec]</p>
          <p>y - number of active channels</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 3">
        <read-quality :chart-data="this.dataReadQuality"></read-quality>
        <label class="control-label">
          <p>x - read</p>
          <p>y - mean base quality</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 4">
        <read-quality-density :chart-data="this.dataReadQuality2D"></read-quality-density>
        <label class="control-label">
          <p>x - mean 2d base quality</p>
          <p>y - quality density</p>
        </label>
        <read-quality-density :chart-data="this.dataReadQualityTemplate"></read-quality-density>
        <label class="control-label">
          <p>x - mean template base quality</p>
          <p>y - quality density</p>
        </label>
        <read-quality-density :chart-data="this.dataReadQualityComplement"></read-quality-density>
        <label class="control-label">
          <p>x - mean complement base quality</p>
          <p>y - quality density</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 5">
        <read-category-quality :chart-data="this.dataReadCategoryQuality"></read-category-quality>
        <label class="control-label">
          <p>x - read categories</p>
          <p>y - mean base quality factors</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 6">
        <read-category-counts :chart-data="this.dataReadCategoryCounts"></read-category-counts>
        <label class="control-label">
          <p>y - number of readings for file and each strand category</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 7">
        <events-counts :chart-data="this.dataEventsCounts"></events-counts>
        <label class="control-label">
          <p>x - duration of an experiment [sec]</p>
          <p>y - number of events</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 8">
        <reads-per-channel :chart-data="this.dataReadsPerChannel"></reads-per-channel>
        <label class="control-label">
          <p>x - channel</p>
          <p>y - number of reads</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 9">
        <kb-per-channel :chart-data="this.dataKbPerChannel"></kb-per-channel>
        <label class="control-label">
          <p>x - channel</p>
          <p>y - kb (kilobase pair) of data </p>
        </label>
      </div>
    </div>
  </div>
</template>

<script>
  import ReadAccumulation from 'src/components/Charts/Ioniser/fast5/ReadAccumulation.vue'
  import ActiveChannels from 'src/components/Charts/Ioniser/fast5/ActiveChannels.vue'
  import ReadQuality from 'src/components/Charts/Ioniser/fast5/ReadQuality.vue'
  import ReadCategoryQuality from 'src/components/Charts/Ioniser/fast5/ReadCategoryQuality.vue'
  import ReadQualityDensity from 'src/components/Charts/Ioniser/fast5/ReadQualityDensity.vue'
  import ReadCategoryCounts from 'src/components/Charts/Ioniser/fast5/ReadCategoryCounts.vue'
  import EventsCounts from 'src/components/Charts/Ioniser/fast5/EventsCounts.vue'
  import ReadsPerChannel from 'src/components/Charts/Ioniser/fast5/ReadsPerChannel.vue'
  import KbPerChannel from 'src/components/Charts/Ioniser/fast5/KbPerChannel.vue'
  import SummaryInfo from 'src/components/Stats/Ioniser/SummaryInfo.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'

  const TEXT_CSV = 'text/csv'
  const TEXT_HTML = 'text/html'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      ReadAccumulation,
      ActiveChannels,
      ReadQuality,
      ReadCategoryQuality,
      ReadQualityDensity,
      ReadCategoryCounts,
      EventsCounts,
      ReadsPerChannel,
      KbPerChannel,
      SummaryInfo
    },
    props: [
      'id'
    ],
    data () {
      return {
        reloadDataSummaryInfo: false,
        dataSummaryInfo: null,
        dataReadAccumulation: null,
        dataActiveChannels: null,
        dataReadCategoryCounts: null,
        dataReadCategoryQuality: null,
        dataEventsCounts: null,
        dataReadsPerChannel: null,
        dataKbPerChannel: null,
        dataReadQuality: null,
        dataReadQualityTemplate: null,
        dataReadQualityComplement: null,
        dataReadQuality2D: null,
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
      getSummaryInfo () {
        this.$http.get(`api/analysis/stats/info`).then(response => {
          this.dataSummaryInfo = response.data
        }).catch(e => {
          console.error(e)
        })
      },
      getReadAccumulation () {
        this.$http.get(`api/analysis/stats/readAccumulation/`, {
          params: {
            valuesNames: ['minute', 'accumulation']
          }
        }).then(response => {
          this.dataReadAccumulation = {
            labels: response.data.values['minute'],
            datasets: [
              {
                backgroundColor: '#f87979',
                data: response.data.values['accumulation'],
                pointRadius: 0
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getActiveChannels () {
        this.$http.get(`api/analysis/stats/activeChannels`, {
          params: {
            valuesNames: ['minute', 'channels']
          }
        }).then(response => {
          this.dataActiveChannels = {
            labels: response.data.values['minute'],
            datasets: [
              {
                backgroundColor: '#0096f8',
                pointBackgroundColor: '#0096f8',
                showLine: true,
                borderColor: '#0096f8',
                steppedLine: true,
                fill: false,
                pointRadius: 0,
                data: response.data.values['channels']
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getReadCategoryCounts () {
        this.$http.get(`api/analysis/stats/readCategoryCounts`, {
          params: {
            valuesNames: ['category', 'files_count', 'template_count', 'complement_count', 'full_2d_count']
          }
        }).then(response => {
          this.dataReadCategoryCounts = {
            labels: [],
            datasets: [
              {
                label: 'files',
                backgroundColor: '#f87979',
                data: response.data.values['files_count']
              },
              {
                label: 'template',
                backgroundColor: '#f8ac5f',
                data: response.data.values['template_count']
              },
              {
                label: 'complement',
                backgroundColor: '#47f889',
                data: response.data.values['complement_count']
              },
              {
                label: 'full 2d',
                backgroundColor: '#82c3f8',
                data: response.data.values['full_2d_count']
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getReadCategoryQuality () {
        this.$http.get(`api/analysis/stats/readCategoryQuality`, {
          params: {
            valuesNames: ['category', 'min', 'max', 'mean', 'median']
          }
        }).then(response => {
          this.dataReadCategoryQuality = {
            labels: response.data.values['category'],
            datasets: [
              {
                label: 'min',
                backgroundColor: '#f87979',
                data: response.data.values['min']
              },
              {
                label: 'mean',
                backgroundColor: '#f8ac5f',
                data: response.data.values['mean']
              },
              {
                label: 'median',
                backgroundColor: '#47f889',
                data: response.data.values['median']
              },
              {
                label: 'max',
                backgroundColor: '#82c3f8',
                data: response.data.values['max']
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getEventsCounts () {
        this.$http.get(`api/analysis/stats/eventsCounts`, {
          params: {
            valuesNames: ['time', 'events']
          }
        }).then(response => {
          this.dataEventsCounts = {
            labels: response.data.values['time'],
            datasets: [
              {
                label: 'Events in time',
                fill: false,
                borderColor: '#f8ac5f',
                pointRadius: 0,
                steppedLine: true,
                data: response.data.values['events']
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getReadQuality () {
        this.$http.get(`api/analysis/stats/readQuality`, {
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
      getReadQualityDensity () {
        this.$http.get(`api/analysis/stats/readQualityDensity`, {
          params: {
            valuesNames: ['quality_template', 'quality_complement', 'quality_2D',
              'density_template', 'density_complement', 'density_2D']
          }
        }).then(response => {
          this.dataReadQuality2D = {
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
          this.dataReadQualityTemplate = {
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
          this.dataReadQualityComplement = {
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
      getReadsPerChannel () {
        this.$http.get(`api/analysis/stats/readsPerChannel`, {
          params: {
            valuesNames: ['channel', 'reads']
          }
        }).then(response => {
          this.dataReadsPerChannel = {
            labels: response.data.values['channel'],
            datasets: [
              {
                borderColor: '#47f889',
                data: response.data.values['reads'],
                steppedLine: true,
                pointRadius: 5,
                fill: false
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getKbPerChannel () {
        this.$http.get(`api/analysis/stats/kbPerChannel`, {
          params: {
            xName: 'channel',
            valuesNames: ['channel', 'kb']
          }
        }).then(response => {
          this.dataKbPerChannel = {
            labels: response.data.values['channel'],
            datasets: [
              {
                borderColor: '#f8ac5f',
                data: response.data.values['kb'],
                steppedLine: true,
                pointRadius: 5,
                fill: false
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },

      getAllData () {
        this.getSummaryInfo()
        this.getReadAccumulation()
        this.getActiveChannels()
        this.getReadCategoryCounts()
        this.getReadCategoryQuality()
        this.getEventsCounts()
        this.getReadQuality()
        this.getReadQualityDensity()
        this.getReadsPerChannel()
        this.getKbPerChannel()
      },

      refreshData () {
        switch (this.tabIndex) {
          case 0:
            this.getSummaryInfo()
            break
          case 1:
            this.getReadAccumulation()
            break
          case 2:
            this.getActiveChannels()
            break
          case 3:
            this.getReadQuality()
            break
          case 4:
            this.getReadQualityDensity()
            break
          case 5:
            this.getReadCategoryQuality()
            break
          case 6:
            this.getReadCategoryCounts()
            break
          case 7:
            this.getEventsCounts()
            break
          case 8:
            this.getReadsPerChannel()
            break
          case 9:
            this.getKbPerChannel()
            break
        }
      },

      csvSummaryInfo () {
        this.$http.get(`api/csv/info`).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'summary_info.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadAccumulation () {
        this.$http.get(`api/csv/readAccumulation`, {
          params: {
            valuesNames: ['minute', 'accumulation']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'read_accumulation.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvActiveChannels () {
        this.$http.pgetost(`api/csv/activeChannels`, {
          params: {
            valuesNames: ['minute', 'channels']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'active_channels.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadCategoryCounts () {
        this.$http.get(`api/csv/readCategoryCounts`, {
          params: {
            valuesNames: ['category', 'files_count', 'template_count', 'complement_count', 'full_2d_count']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'read_category_counts.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadCategoryQuality () {
        this.$http.get(`api/csv/readCategoryQuality`, {
          params: {
            valuesNames: ['category', 'min', 'max', 'mean', 'median']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'read_category_quality.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvEventsCounts () {
        this.$http.get(`api/csv/eventsCounts`, {
          params: {
            valuesNames: ['time', 'events']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'events_counts.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvReadQuality () {
        this.$http.get(`api/csv/readQuality`, {
          params: {
            valuesNames: ['id', 'quality', 'min_', 'max_', 'mean_', 'median_']
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
        this.$http.get(`api/csv/readQualityDensity`, {
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
      csvReadsPerChannel () {
        this.$http.get(`api/csv/readsPerChannel`, {
          params: {
            valuesNames: ['channel', 'reads']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'reads_per_channel.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvKbPerChannel () {
        this.$http.get(`api/csv/kbPerChannel`, {
          params: {
            valuesNames: ['channel', 'kb']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'kb_per_channel.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },

      exportCsv () {
        switch (this.tabIndex) {
          case 0:
            this.csvSummaryInfo()
            break
          case 1:
            this.csvReadAccumulation()
            break
          case 2:
            this.csvActiveChannels()
            break
          case 3:
            this.csvReadQuality()
            break
          case 4:
            this.csvReadQualityDensity()
            break
          case 5:
            this.csvReadCategoryQuality()
            break
          case 6:
            this.csvReadCategoryCounts()
            break
          case 7:
            this.csvEventsCounts()
            break
          case 8:
            this.csvReadsPerChannel()
            break
          case 9:
            this.csvKbPerChannel()
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
