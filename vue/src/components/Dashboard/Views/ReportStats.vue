<template>
  <div class="card">
    <div class="header">
      <h4 class="title">Report of analyse</h4>
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
        <summary-info :id="this.id" :reloadData="this.reloadData" @reloadData='resetReloadData'></summary-info>
      </div>
      <div v-if="this.tabIndex === 1">
        <read-accumulation :chart-data="this.dataReadAccumulation"></read-accumulation>
        <label class="control-label">An accumulation of reads over the duration of an experiment</label>
      </div>
      <div v-if="this.tabIndex === 2">
        <active-channels :chart-data="this.dataActiveChannels"></active-channels>
        <label class="control-label">A number of active channels for each minute of run time</label>
      </div>
      <div v-if="this.tabIndex === 3">
        <read-quality :chart-data="this.dataReadQuality"></read-quality>
        <label class="control-label">Mean qualities of file with mean factors of all files</label>
      </div>
      <div v-if="this.tabIndex === 4">
        <read-category-quality :chart-data="this.dataReadCategoryQuality"></read-category-quality>
        <label class="control-label">A quality summary of an analyse. Min, max, max and median values, broken down into the strand categories</label>
      </div>
      <div v-if="this.tabIndex === 5">
        <read-category-counts :chart-data="this.dataReadCategoryCounts"></read-category-counts>
        <label class="control-label">A strand classification with numbers of readings</label>
      </div>
      <div v-if="this.tabIndex === 6">
        <events-counts :chart-data="this.dataEventsCounts"></events-counts>
        <label class="control-label">Number of events over the duration of an experiment</label>
      </div>
      <div v-if="this.tabIndex === 7">
        <reads-per-channel :chart-data="this.dataReadsPerChannel"></reads-per-channel>
        <label class="control-label">Number of reads read for each channel</label>
      </div>
      <div v-if="this.tabIndex === 8">
        <kb-per-channel :chart-data="this.dataKbPerChannel"></kb-per-channel>
        <label class="control-label">The amount of data (kilobytes) read for each channel</label>
      </div>
    </div>
  </div>
</template>

<script>
  import ReadAccumulation from 'src/components/Charts/Ioniser/ReadAccumulation.vue'
  import ActiveChannels from 'src/components/Charts/Ioniser/ActiveChannels.vue'
  import ReadQuality from 'src/components/Charts/Ioniser/ReadQuality.vue'
  import ReadCategoryQuality from 'src/components/Charts/Ioniser/ReadCategoryQuality.vue'
  import ReadCategoryCounts from 'src/components/Charts/Ioniser/ReadCategoryCounts.vue'
  import EventsCounts from 'src/components/Charts/Ioniser/EventsCounts.vue'
  import ReadsPerChannel from 'src/components/Charts/Ioniser/ReadsPerChannel.vue'
  import KbPerChannel from 'src/components/Charts/Ioniser/KbPerChannel.vue'
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
      'id',
      'updateTrigger'
    ],
    data () {
      return {
        reloadData: false,
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
    methods: {
      handleTabChange (tabIndex, newTab, oldTab) {
        this.tabIndex = tabIndex
        switch (tabIndex) {
          case 0:
            this.reloadData = true
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
            this.getReadCategoryQuality()
            break
          case 5:
            this.getReadCategoryCounts()
            break
          case 6:
            this.getEventsCounts()
            break
          case 7:
            this.getReadsPerChannel()
            break
          case 8:
            this.getKbPerChannel()
            break
        }
      },
      loadData () {
        this.$http.get(`api/analyse/` + this.id).then(response => {
          this.reloadData = true
        }).catch(e => {
          console.error(e)
        })
      },
      resetReloadData () {
        this.reloadData = false
      },
      getReadAccumulation () {
        this.$http.get(`api/r/charts/readAccumulation/` + this.id, {
          params: {
            xName: 'minute',
            yNames: ['accumulation']
          }
        }).then(response => {
          this.dataReadAccumulation = {
            labels: response.data.xvalues,
            datasets: [
              {
                label: 'Reads accumulation in time',
                backgroundColor: '#f87979',
                data: response.data.yvaluesList[0]
              }
            ]
          }
        }).catch(e => {
          console.error(e)
        })
      },
      getActiveChannels () {
        this.$http.get(`api/r/charts/activeChannels`, {
          params: {
            xName: 'minute',
            yNames: ['channels']
          }
        }).then(response => {
          this.dataActiveChannels = {
            labels: response.data.xvalues,
            datasets: [
              {
                label: 'Active channels in time',
                backgroundColor: '#0096f8',
                pointBackgroundColor: '#0096f8',
                fill: false,
                showLine: false,
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
        this.$http.get(`api/r/charts/readCategoryCounts`, {
          params: {
            xName: 'category',
            yNames: ['files_count', 'template_count', 'complement_count', 'full_2d_count']
          }
        }).then(response => {
          this.dataReadCategoryCounts = {
            labels: [],
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
        this.$http.get(`api/r/charts/readCategoryQuality`, {
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
        this.$http.get(`api/r/charts/eventsCounts`, {
          params: {
            xName: 'time',
            yNames: ['events']
          }
        }).then(response => {
          this.dataEventsCounts = {
            labels: response.data.xvalues,
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
        this.$http.get(`api/r/charts/readQuality`, {
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
        this.$http.get(`api/r/charts/readsPerChannel`, {
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
        this.$http.get(`api/r/charts/kbPerChannel`, {
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
