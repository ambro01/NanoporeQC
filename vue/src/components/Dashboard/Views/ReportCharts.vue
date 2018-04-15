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
        <div class="column">
          <h3>Read category counts</h3>
          <read-category-counts :chart-data="this.dataReadCategoryCounts"></read-category-counts>
        </div>
        <div class="column">
          <h3>Read category quality</h3>
          <read-category-quality :chart-data="this.dataReadCategoryQuality"></read-category-quality>
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
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'
  import { AXIOS } from 'src/http-common'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      ReadAccumulation,
      ActiveChannels,
      ReadCategoryCounts,
      ReadCategoryQuality
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
        dataReadCategoryQuality: null
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
                backgroundColor: '#f87979',
                fill: false,
                showLine: false,
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
            yNames: ['count'],
            id: this.id
          }
        }).then(response => {
          this.dataReadCategoryCounts = {
            labels: response.data.xvalues,
            datasets: [
              {
                backgroundColor: '#f87979',
                fill: false,
                showLine: false,
                data: response.data.yvaluesList[0]
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
          this.dataReadCategoryCounts = {
            labels: response.data.xvalues,
            datasets: [
              {
                backgroundColor: '#f87979',
                fill: false,
                showLine: false,
                data: response.data.yvaluesList[0]
              },
              {
                backgroundColor: '#82c3f8',
                fill: false,
                showLine: false,
                data: response.data.yvaluesList[1]
              },
              {
                backgroundColor: '#f8ac5f',
                fill: false,
                showLine: false,
                data: response.data.yvaluesList[2]
              },
              {
                backgroundColor: '#47f889',
                fill: false,
                showLine: false,
                data: response.data.yvaluesList[3]
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
