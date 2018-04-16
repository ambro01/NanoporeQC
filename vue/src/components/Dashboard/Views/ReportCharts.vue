<template>
  <section class="content">
    <label class="control-label">Report of analyse</label>
    <div class="columns">
      <button class="btn btn-primary" @click="showAnalyseResults">View results</button>
      <div>
        <div class="column">
          <h3>Read accumulation</h3>
          <read-accumulation :chart-data="this.dataReadAccumulation"></read-accumulation>
        </div>
        <hr>
        <div class="column">
          <h3>Active channels</h3>
          <active-channels :chart-data="this.dataActiveChannels"></active-channels>
        </div>
        <hr>
        <div class="column">
          <h3>Read category counts</h3>
          <read-category-counts :chart-data="this.dataReadCategoryCounts"></read-category-counts>
        </div>
        <hr>
        <div class="column">
          <h3>Read category quality</h3>
          <read-category-quality :chart-data="this.dataReadCategoryQuality"></read-category-quality>
        </div>
        <hr>
        <div class="column">
          <h3>Events counts</h3>
          <events-counts :chart-data="this.dataEventsCounts"></events-counts>
        </div>
        <hr>
        <div class="column">
          <h3>Reads per channel</h3>
          <reads-per-channel :chart-data="this.dataReadsPerChannel"></reads-per-channel>
        </div>
        <hr>
        <div class="column">
          <h3>kb of data per channel</h3>
          <kb-per-channel :chart-data="this.dataKbPerChannel"></kb-per-channel>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
  import ReadAccumulation from 'src/components/Charts/Ioniser/ReadAccumulation.vue'
  import ActiveChannels from 'src/components/Charts/Ioniser/ActiveChannels.vue'
  import ReadCategoryCounts from 'src/components/Charts/Ioniser/ReadCategoryCounts.vue'
  import ReadCategoryQuality from 'src/components/Charts/Ioniser/ReadCategoryQuality.vue'
  import EventsCounts from 'src/components/Charts/Ioniser/EventsCounts.vue'
  import ReadsPerChannel from 'src/components/Charts/Ioniser/ReadsPerChannel.vue'
  import KbPerChannel from 'src/components/Charts/Ioniser/KbPerChannel.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'
  import { AXIOS } from 'src/http-common'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      ReadAccumulation,
      ActiveChannels,
      ReadCategoryCounts,
      ReadCategoryQuality,
      EventsCounts,
      ReadsPerChannel,
      KbPerChannel
    },
    props: [
      'id'
    ],
    data () {
      return {
        showResults: true,
        dataReadAccumulation: null,
        dataActiveChannels: null,
        dataReadCategoryCounts: null,
        dataReadCategoryQuality: null,
        dataEventsCounts: null,
        dataReadsPerChannel: null,
        dataKbPerChannel: null
      }
    },
    methods: {
      showAnalyseResults () {
        this.getAllDataSet()
        this.showSaveAndRun = false
      },
      getAllDataSet () {
        this.getReadAccumulation()
        this.getActiveChannels()
        this.getReadCategoryCounts()
        this.getReadCategoryQuality()
        this.getEventsCounts()
        this.getReadsPerChannel()
        this.getKbPerChannel()
      },
      getReadAccumulation () {
        AXIOS.get(`api/charts/readAccumulation`, {
          params: {
            xName: 'minute',
            yNames: ['accumulation'],
            id: this.id
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
        AXIOS.get(`api/charts/activeChannels`, {
          params: {
            xName: 'minute',
            yNames: ['channels'],
            id: this.id
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
        AXIOS.get(`api/charts/readCategoryCounts`, {
          params: {
            xName: 'category',
            yNames: ['files_count', 'template_count', 'complement_count', 'full_2d_count'],
            id: this.id
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
        AXIOS.get(`api/charts/readCategoryQuality`, {
          params: {
            xName: 'category',
            yNames: ['min', 'max', 'mean', 'median'],
            id: this.id
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
        AXIOS.get(`api/charts/eventsCounts`, {
          params: {
            xName: 'time',
            yNames: ['events'],
            id: this.id
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
      getReadsPerChannel () {
        AXIOS.get(`api/charts/readsPerChannel`, {
          params: {
            xName: 'channel',
            yNames: ['reads'],
            id: this.id
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
        AXIOS.get(`api/charts/kbPerChannel`, {
          params: {
            xName: 'channel',
            yNames: ['kb'],
            id: this.id
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
