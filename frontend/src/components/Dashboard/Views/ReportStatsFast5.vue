<template>
  <div class="card">
    <div class="header">
      <h4 class="title">Report of analysis</h4>
    </div>
    <div class="content">
      <vue-tabs active-tab-color="#f4f3ef" @tab-change="handleTabChange">
        <v-tab title="Summary info"></v-tab>
        <v-tab title="Read accumulation"></v-tab>
        <v-tab title="Active channels"></v-tab>
        <v-tab title="Read quality"></v-tab>
        <v-tab title="Read category quality"></v-tab>
        <v-tab title="Read category counts"></v-tab>
        <v-tab title="Events"></v-tab>
        <v-tab title="Reads per channel"></v-tab>
        <v-tab title="kb per channel"></v-tab>
      </vue-tabs>
      <div v-if="this.tabIndex === 0">
        <summary-info :sourceData="this.dataSummaryInfo" v-if="this.dataSummaryInfo != null"></summary-info>
        <label class="control-label">Summary information about files of analysis</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getSummaryInfo">Refresh</button>
      </div>
      <div v-if="this.tabIndex === 1">
        <read-accumulation :chart-data="this.dataReadAccumulation"></read-accumulation>
        <label class="control-label">An accumulation of reads over the duration of an experiment</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getReadAccumulation">Refresh</button>
      </div>
      <div v-if="this.tabIndex === 2">
        <active-channels :chart-data="this.dataActiveChannels"></active-channels>
        <label class="control-label">A number of active channels for each minute of run time</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getActiveChannels">Refresh</button>
      </div>
      <div v-if="this.tabIndex === 3">
        <read-quality :chart-data="this.dataReadQuality"></read-quality>
        <label class="control-label">Mean reads' qualities of each file</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getReadQuality">Refresh</button>
      </div>
      <div v-if="this.tabIndex === 4">
        <read-category-quality :chart-data="this.dataReadCategoryQuality"></read-category-quality>
        <label class="control-label">A quality summary of an analysis. Min, mean, median and max values, broken down into the strand categories</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getReadCategoryQuality">Refresh</button>
      </div>
      <div v-if="this.tabIndex === 5">
        <read-category-counts :chart-data="this.dataReadCategoryCounts"></read-category-counts>
        <label class="control-label">A strand classification with numbers of readings</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getReadCategoryCounts">Refresh</button>
      </div>
      <div v-if="this.tabIndex === 6">
        <events-counts :chart-data="this.dataEventsCounts"></events-counts>
        <label class="control-label">Number of events over the duration of an experiment</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getEventsCounts">Refresh</button>
      </div>
      <div v-if="this.tabIndex === 7">
        <reads-per-channel :chart-data="this.dataReadsPerChannel"></reads-per-channel>
        <label class="control-label">Number of reads read for each channel</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getReadsPerChannel">Refresh</button>
      </div>
      <div v-if="this.tabIndex === 8">
        <kb-per-channel :chart-data="this.dataKbPerChannel"></kb-per-channel>
        <label class="control-label">The amount of data (kilobytes) read for each channel</label>
        <br>
        <button class="btn btn-primary btn-wd" @click.prevent="getKbPerChannel">Refresh</button>
      </div>
    </div>
  </div>
</template>

<script>
  import ReadAccumulation from 'src/components/Charts/Ioniser/fast5/ReadAccumulation.vue'
  import ActiveChannels from 'src/components/Charts/Ioniser/fast5/ActiveChannels.vue'
  import ReadQuality from 'src/components/Charts/Ioniser/fast5/ReadQuality.vue'
  import ReadCategoryQuality from 'src/components/Charts/Ioniser/fast5/ReadCategoryQuality.vue'
  import ReadCategoryCounts from 'src/components/Charts/Ioniser/fast5/ReadCategoryCounts.vue'
  import EventsCounts from 'src/components/Charts/Ioniser/fast5/EventsCounts.vue'
  import ReadsPerChannel from 'src/components/Charts/Ioniser/fast5/ReadsPerChannel.vue'
  import KbPerChannel from 'src/components/Charts/Ioniser/fast5/KbPerChannel.vue'
  import SummaryInfo from 'src/components/Stats/Ioniser/SummaryInfo.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      ReadAccumulation,
      ActiveChannels,
      ReadQuality,
      ReadCategoryQuality,
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
            xName: 'minute',
            yNames: ['accumulation']
          }
        }).then(response => {
          this.dataReadAccumulation = {
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
      getActiveChannels () {
        this.$http.get(`api/analysis/stats/activeChannels`, {
          params: {
            xName: 'minute',
            yNames: ['channels']
          }
        }).then(response => {
          this.dataActiveChannels = {
            labels: response.data.xvalues,
            datasets: [
              {
                backgroundColor: '#0096f8',
                pointBackgroundColor: '#0096f8',
                showLine: true,
                borderColor: '#0096f8',
                steppedLine: true,
                fill: false,
                lineTension: 0,
                data: response.data.yvaluesList[0]
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
            xName: 'category',
            yNames: ['files_count', 'template_count', 'complement_count', 'full_2d_count']
          }
        }).then(response => {
          this.dataReadCategoryCounts = {
            labels: ['counts'],
            datasets: [
              {
                label: 'files',
                backgroundColor: '#f87979',
                data: response.data.yvaluesList[0]
              },
              {
                label: 'template',
                backgroundColor: '#f8ac5f',
                data: response.data.yvaluesList[2]
              },
              {
                label: 'complement',
                backgroundColor: '#47f889',
                data: response.data.yvaluesList[3]
              },
              {
                label: 'full 2d',
                backgroundColor: '#82c3f8',
                data: response.data.yvaluesList[1]
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
            xName: 'category',
            yNames: ['min', 'max', 'mean', 'median']
          }
        }).then(response => {
          this.dataReadCategoryQuality = {
            labels: response.data.xvalues,
            datasets: [
              {
                label: 'min',
                backgroundColor: '#f87979',
                data: response.data.yvaluesList[0]
              },
              {
                label: 'mean',
                backgroundColor: '#f8ac5f',
                data: response.data.yvaluesList[2]
              },
              {
                label: 'median',
                backgroundColor: '#47f889',
                data: response.data.yvaluesList[3]
              },
              {
                label: 'max',
                backgroundColor: '#82c3f8',
                data: response.data.yvaluesList[1]
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
            xName: 'time',
            yNames: ['events']
          }
        }).then(response => {
          this.dataEventsCounts = {
            datasets: [
              {
                label: 'Events in time',
                backgroundColor: '#f8ac5f',
                fill: false,
                showLine: true,
                borderColor: '#f8ac5f',
                lineTension: 0,
                steppedLine: true,
                data: response.data.yvaluesList[0]
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
            xName: 'id',
            yNames: ['quality', 'min_', 'max_', 'mean_', 'median_']
          }
        }).then(response => {
          this.dataReadQuality = {
            labels: response.data.xvalues,
            datasets: [
              {
                label: 'quality',
                borderColor: '#f87979',
                fill: false,
                data: response.data.yvaluesList[0]
              },
              {
                label: 'min',
                borderColor: '#d392f8',
                pointRadius: 0,
                fill: false,
                data: response.data.yvaluesList[1]
              },
              {
                label: 'max',
                borderColor: '#f8ac5f',
                pointRadius: 0,
                fill: false,
                data: response.data.yvaluesList[2]
              },
              {
                label: 'mean',
                borderColor: '#47f889',
                pointRadius: 0,
                fill: false,
                data: response.data.yvaluesList[3]
              },
              {
                label: 'median',
                borderColor: '#82c3f8',
                pointRadius: 0,
                fill: false,
                data: response.data.yvaluesList[4]
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
            xName: 'channel',
            yNames: ['reads']
          }
        }).then(response => {
          this.dataReadsPerChannel = {
            labels: response.data.xvalues,
            datasets: [
              {
                backgroundColor: '#82c3f8',
                data: response.data.yvaluesList[0]
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
            yNames: ['kb']
          }
        }).then(response => {
          this.dataKbPerChannel = {
            labels: response.data.xvalues,
            datasets: [
              {
                backgroundColor: '#f8ac5f',
                data: response.data.yvaluesList[0]
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
        this.getReadsPerChannel()
        this.getKbPerChannel()
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
