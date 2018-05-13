<script>
  // Importing Bar and mixins class from the vue-chartjs wrapper
  import {
    Line,
    mixins
  } from 'vue-chartjs'
  // Getting the reactiveProp mixin from the mixins module.
  const {reactiveProp} = mixins
  export default {
    extends: Line,
    mixins: [reactiveProp],
    data () {
      return {
        // Chart.js options that control the appearance of the chart
        options: {
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true
              },
              gridLines: {
                display: true
              }
            }],
            xAxes: [{
              gridLines: {
                display: true
              }
            }]
          },
          legend: {
            display: true
          },
          responsive: true,
          maintainAspectRatio: false
        }
      }
    },
    mounted () {
      // this.chartData is created in the mixin and contains all the data needed to build the chart.
      this.renderChart(this.chartData, this.options)
    }
  }
</script>
