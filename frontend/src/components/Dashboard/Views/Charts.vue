<template>
  <section class="container">
    <ul>
      <li><router-link to="/">Home</router-link></li>
      <li><router-link to="/chartjs">vue-chartjs</router-link></li>
      <li><router-link to="/stats">vue-charts</router-link></li>
      <li><router-link to="/chartkick">vue-chartkick</router-link></li>
    </ul>
    <h1>Demo examples of vue-chartjs</h1>
    <div class="columns">
      <div class="column">
        <h3>Line Chart</h3>
        <line-chart></line-chart>
      </div>
      <div class="column">
        <h3>Bar Chart</h3>
        <bar-chart></bar-chart>
      </div>
    </div>
    <div class="columns">
      <div class="column">
        <h3>Bubble Chart</h3>
        <bubble-chart></bubble-chart>
      </div>
      <div class="column">
        <h3>Reactivity - Live update upon change in datasets</h3>
        <reactive :chart-data="datacollection"></reactive>
      </div>
    </div>
  </section>
</template>

<script>
  import LineChart from 'src/components/Charts/LineChart.vue'
  import BarChart from 'src/components/Charts/BarChart.vue'
  import BubbleChart from 'src/components/Charts/BubbleChart.vue'
  import Reactive from 'src/components/Charts/Reactive.vue'

  export default {
    name: 'VueChartJS',
    components: {
      LineChart,
      BarChart,
      BubbleChart,
      Reactive
    },
    data () {
      return {
        datacollection: null
      }
    },
    created () {
      this.$http.get(`api/analysis/stats/readAccumulation/minute/accumulation`)
        .then(response => {
          this.datacollection = {
            labels: response.data.xvalues,
            datasets: [
              {
                label: 'Data One',
                backgroundColor: '#f87979',
                data: response.data.yvalues
              }
            ]
          }
//          this.datacollection.labels = response.data.xvalues
//          this.datacollection.datasets.data = response.data.yvalues
          console.log(this)
        })
        .catch(e => {
          console.error(e)
        })
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
