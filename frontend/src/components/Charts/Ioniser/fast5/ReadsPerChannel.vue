<script>
  // Importing Bar class from the vue-chartjs wrapper
  import {
    Line,
    mixins
  } from 'vue-chartjs'
  import zoom from 'chartjs-plugin-zoom'
  // Exporting this so it can be used in other components
  const {reactiveProp} = mixins
  export default {
    extends: Line,
    mixins: [reactiveProp],
    data () {
      return {
        // Chart.js options that controls the appearance of the chart
        options: {
          scales: {
            yAxes: [{
              ticks: {
                maxTicksLimit: 20
              },
              gridLines: {
                display: true
              }
            }],
            xAxes: [{
              ticks: {
                maxTicksLimit: 50
              },
              gridLines: {
                display: true
              }
            }]
          },
          legend: {
            display: false
          },
          responsive: true,
          maintainAspectRatio: false,
          pan: {
            enabled: true,
            mode: 'xy'
          },
          zoom: {
            enabled: true,
            mode: 'xy',
            drag: false
          }
        }
      }
    },

    mounted () {
      this.addPlugin(zoom)
      this.renderChart(this.chartData, this.options)
    }
  }
</script>
