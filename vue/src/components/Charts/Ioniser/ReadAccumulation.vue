<script>
  // Importing Bar and mixins class from the vue-chartjs wrapper
  import {Line, mixins} from 'vue-chartjs'
  import {AXIOS} from 'src/http-common'
  // Getting the reactiveProp mixin from the mixins module.
  const { reactiveProp } = mixins
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
            xAxes: [ {
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
        },
        dataCollection: null
      }
    },
    props: [
      'id'
    ],
    created () {
      AXIOS.get(`api/charts/readAccumulation/minute/accumulation/` + this.id)
        .then(response => {
          this.dataCollection = {
            labels: response.data.xvalues,
            datasets: [
              {
                label: 'Reads',
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
    mounted () {
      // this.chartData is created in the mixin and contains all the data needed to build the chart.
      this.renderChart(this.dataCollection, this.options)
    }
  }
</script>
