<template>
  <section class="container">
    <div class="columns">
      <div class="column">
        <h3>Line Chart</h3>
        <read-accumulation></read-accumulation>
      </div>
      <div class="column">
        <h3>Bar Chart</h3>
        <bar-chart></bar-chart>
      </div>
      <div class="column">
        <h3>Scatter Chart</h3>
        <scatter-chart></scatter-chart>
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
  import ReadAccumulation from 'src/components/Charts/Ioniser/ReadAccumulation.vue'
  import BarChart from 'src/components/Charts/BarChart.vue'
  import ScatterChart from 'src/components/Charts/ScatterChart.vue'
  import BubbleChart from 'src/components/Charts/BubbleChart.vue'
  import Reactive from 'src/components/Charts/Reactive.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'

  export default {
    name: 'VueChartJS',
    components: {
      StatsCard,
      ReadAccumulation,
      BarChart,
      ScatterChart,
      BubbleChart,
      Reactive
    },
    props: [
      'id'
    ]
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
