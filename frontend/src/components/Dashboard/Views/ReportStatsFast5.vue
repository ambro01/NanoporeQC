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
        <v-tab title="Reads accumulation"></v-tab>
        <v-tab title="Active channels"></v-tab>
        <v-tab title="Reads quality"></v-tab>
        <v-tab title="Reads quality density"></v-tab>
        <v-tab title="Reads quality factors"></v-tab>
        <v-tab title="Reads category counts"></v-tab>
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
        <reads-accumulation :chart-data="this.dataReadsAccumulation"></reads-accumulation>
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
        <reads-quality-multi :chart-data="this.dataReadsQualityMulti"></reads-quality-multi>
        <label class="control-label">
          <p>x - read</p>
          <p>y - mean base quality</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 4">
        <reads-quality-density-multi :chart-data="this.dataReadsQualityDensity2D"></reads-quality-density-multi>
        <label class="control-label">
          <p>x - mean 2d base quality</p>
          <p>y - quality density</p>
        </label>
        <reads-quality-density-multi :chart-data="this.dataReadsQualityDensityTemplate"></reads-quality-density-multi>
        <label class="control-label">
          <p>x - mean template base quality</p>
          <p>y - quality density</p>
        </label>
        <reads-quality-density-multi :chart-data="this.dataReadsQualityDensityComplement"></reads-quality-density-multi>
        <label class="control-label">
          <p>x - mean complement base quality</p>
          <p>y - quality density</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 5">
        <reads-category-quality :chart-data="this.dataReadsCategoryQuality"></reads-category-quality>
        <label class="control-label">
          <p>x - read categories</p>
          <p>y - mean base quality factors</p>
        </label>
      </div>
      <div v-if="this.tabIndex === 6">
        <reads-category-counts :chart-data="this.dataReadsCategoryCounts"></reads-category-counts>
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
  import ReadsAccumulation from 'src/components/Charts/Ioniser/fast5/ReadsAccumulation.vue'
  import ActiveChannels from 'src/components/Charts/Ioniser/fast5/ActiveChannels.vue'
  import ReadsQualityMulti from 'src/components/Charts/Ioniser/fast5/ReadsQualityMulti.vue'
  import ReadsCategoryQuality from 'src/components/Charts/Ioniser/fast5/ReadsCategoryQuality.vue'
  import ReadsQualityDensityMulti from 'src/components/Charts/Ioniser/fast5/ReadsQualityDensityMulti.vue'
  import ReadsCategoryCounts from 'src/components/Charts/Ioniser/fast5/ReadsCategoryCounts.vue'
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
      ReadsAccumulation,
      ActiveChannels,
      ReadsQualityMulti,
      ReadsQualityDensityMulti,
      ReadsCategoryCounts,
      ReadsCategoryQuality,
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
        dataReadsAccumulation: null,
        dataActiveChannels: null,
        dataReadsCategoryCounts: null,
        dataReadsCategoryQuality: null,
        dataEventsCounts: null,
        dataReadsPerChannel: null,
        dataKbPerChannel: null,
        dataReadsQualityMulti: null,
        dataReadsQualityDensityTemplate: null,
        dataReadsQualityDensityComplement: null,
        dataReadsQualityDensity2D: null,
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
      getReadsAccumulation () {
        this.$http.get(`api/analysis/stats/readsAccumulation/`, {
          params: {
            valuesNames: ['minute', 'accumulation']
          }
        }).then(response => {
          this.dataReadsAccumulation = {
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
      getReadsCategoryCounts () {
        this.$http.get(`api/analysis/stats/readsCategoryCounts`, {
          params: {
            valuesNames: ['category', 'files_count', 'template_count', 'complement_count', 'full_2d_count']
          }
        }).then(response => {
          this.dataReadsCategoryCounts = {
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
      getReadsCategoryQuality () {
        this.$http.get(`api/analysis/stats/readsCategoryQuality`, {
          params: {
            valuesNames: ['category', 'min', 'max', 'mean', 'median']
          }
        }).then(response => {
          this.dataReadsCategoryQuality = {
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
      getReadsQualityMulti () {
        this.$http.get(`api/analysis/stats/readsQualityMulti`, {
          params: {
            valuesNames: ['id', 'q_template', 'q_complement', 'q_2D']
          }
        }).then(response => {
          this.dataReadsQualityMulti = {
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
      getReadsQualityDensityMulti () {
        this.$http.get(`api/analysis/stats/readsQualityDensityMulti`, {
          params: {
            valuesNames: ['quality_template', 'quality_complement', 'quality_2D',
              'density_template', 'density_complement', 'density_2D']
          }
        }).then(response => {
          this.dataReadsQualityDensity2D = {
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
          this.dataReadsQualityDensityTemplate = {
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
          this.dataReadsQualityDensityComplement = {
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
        this.getReadsAccumulation()
        this.getActiveChannels()
        this.getReadsCategoryCounts()
        this.getReadsCategoryQuality()
        this.getEventsCounts()
        this.getReadsQualityMulti()
        this.getReadsQualityDensityMulti()
        this.getReadsPerChannel()
        this.getKbPerChannel()
      },

      refreshData () {
        switch (this.tabIndex) {
          case 0:
            this.getSummaryInfo()
            break
          case 1:
            this.getReadsAccumulation()
            break
          case 2:
            this.getActiveChannels()
            break
          case 3:
            this.getReadsQualityMulti()
            break
          case 4:
            this.getReadsQualityDensityMulti()
            break
          case 5:
            this.getReadsCategoryQuality()
            break
          case 6:
            this.getReadsCategoryCounts()
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
      csvReadsAccumulation () {
        this.$http.get(`api/csv/readsAccumulation`, {
          params: {
            valuesNames: ['minute', 'accumulation']
          }
        }).then(response => {
          const blob = new Blob([response.data], {type: TEXT_CSV})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'reads_accumulation.csv'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      },
      csvActiveChannels () {
        this.$http.get(`api/csv/activeChannels`, {
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
      csvReadsCategoryCounts () {
        this.$http.get(`api/csv/readsCategoryCounts`, {
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
      csvReadsCategoryQuality () {
        this.$http.get(`api/csv/readsCategoryQuality`, {
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
      csvReadsQualityMulti () {
        this.$http.get(`api/csv/readsQualityMulti`, {
          params: {
            valuesNames: ['id', 'quality', 'min_', 'max_', 'mean_', 'median_']
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
      csvReadsQualityDensityMulti () {
        this.$http.get(`api/csv/readsQualityDensityMulti`, {
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
            this.csvReadsAccumulation()
            break
          case 2:
            this.csvActiveChannels()
            break
          case 3:
            this.csvReadsQualityMulti()
            break
          case 4:
            this.csvReadsQualityDensityMulti()
            break
          case 5:
            this.csvReadsCategoryQuality()
            break
          case 6:
            this.csvReadsCategoryCounts()
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
