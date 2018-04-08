<template>
  <section class="content">
    <label class="control-label">Report of analyse</label>
    <div class="columns">
      <button class="btn btn-primary" @click="showAnalyseResults">View results</button>
      <div>
        <div class="column">
          <h3>Read accumulation</h3>
          <!--<read-accumulation :chart-data="this.dataReadAccumulation"></read-accumulation>-->
        </div>
        <hr>
        <div class="column">
          <h3>Active channels</h3>
          <active-channels :chart-data="this.dataActiveChannels"></active-channels>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
  import ReadAccumulation from 'src/components/Charts/Ioniser/ReadAccumulation.vue'
  import ActiveChannels from 'src/components/Charts/Ioniser/ActiveChannels.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'
  import { AXIOS } from 'src/http-common'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      ReadAccumulation,
      ActiveChannels
    },
    props: [
      'id'
    ],
    data () {
      return {
        showResults: true,
        dataReadAccumulation: null,
        dataActiveChannels: null
      }
    },
    methods: {
      showAnalyseResults () {
        this.getAllDataSet()
        this.showSaveAndRun = false
      },
      getAllDataSet () {
//        this.getReadAccumulation()
        this.getActiveChannels()
      },
      getReadAccumulation () {
        AXIOS.get(`api/charts/readAccumulation/minute/accumulation` + (this.id > 0 ? ('/' + this.id) : ''))
          .then(response => {
            this.dataReadAccumulation = {
              labels: response.data.xvalues,
              datasets: [
                {
                  label: 'Reads accumulation in time',
                  backgroundColor: '#f87979',
                  data: response.data.yvalues
                }
              ]
            }
          })
          .catch(e => {
            console.error(e)
          })
      },
      getActiveChannels () {
        AXIOS.get(`api/charts/activeChannels/minute/channels` + (this.id > 0 ? ('/' + this.id) : ''))
          .then(response => {
            this.dataActiveChannels = {
              labels: response.data.xvalues,
              datasets: [
                {
                  label: 'Active channels in time',
                  backgroundColor: '#f87979',
                  fill: false,
                  showLine: false,
                  data: response.data.yvalues
                }
              ]
            }
          })
          .catch(e => {
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
