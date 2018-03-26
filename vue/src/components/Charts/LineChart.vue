<script>
  // Importing Line class from the vue-chartjs wrapper
  import {Line} from 'vue-chartjs'
  import {HTTP} from 'src/http-common'
  // Exporting this so it can be used in other components
  export default {
    extends: Line,
    data () {
      return {
        datacollection: {
          // Data to be represented on x-axis
          labels: [],
          datasets: [{
            label: 'Data One',
            backgroundColor: '#f87979',
            pointBackgroundColor: 'white',
            borderWidth: 1,
            pointBorderColor: '#249EBF',
            // Data to be represented on y-axis
            data: []
          }]
        },
        // Chart.js options that controls the appearance of the chart
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
                display: false
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

    created () {

    },

    mounted () {
      HTTP.get(`api/charts/1`)
        .then(response => {
          this.datacollection.labels = response.data.xvalues
          this.datacollection.datasets.data = response.data.yvalues
          console.log(this)
        })
        .catch(e => {
          console.error(e)
        })
      // renderChart function renders the chart with the datacollection and options object.
      this.renderChart(this.datacollection, this.options)
    }
  }
</script>
