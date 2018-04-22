<template>
  <div>
    <h3>Summary information about .fast5 files</h3>
    <p class="category">* Use shift key to enable multi sorting</p>
    <br>
    <v-client-table :columns="columns" :data="data" :options="options"></v-client-table>
  </div>
</template>

<script>
  import { AXIOS } from 'src/http-common'

  export default {
    components: {},
    data () {
      return {
        columns: ['id', 'fileName', 'channelIndex', 'strandIndexInChannel', 'eventsNo', 'startTime', 'duration',
          'hasTemplate', 'hasComplement', 'eventsNoTemplate', 'eventsNoComplement', 'is2d'],
        data: [],
        options: {
          headings: {
            id: 'Id',
            fileName: 'File name',
            channelIndex: 'Channel',
            strandIndexInChannel: 'StrandNo',
            eventsNo: 'Events',
            startTime: 'Start time',
            hasTemplate: 'T',
            eventsNoTemplate: 'T events',
            hasComplement: 'C',
            eventsNoComplement: 'C events',
            is2d: '2d'
          },
          headingsTooltips: {
            channelIndex: 'Channel number 1:512',
            strandIndexInChannel: 'Strand number in channel',
            eventsNo: 'Changes in the measured current',
            eventsNoTemplate: 'Events of template strand',
            eventsNoComplement: 'Events of complement strand',
            hasTemplate: 'Template strand',
            hasComplement: 'Complement strand',
            is2d: 'Full 2d strand (template & complement)'
          },
          sortable: ['id', 'channelIndex', 'strandIndexInChannel', 'eventsNo', 'startTime', 'duration'],
          filterable: ['fileName'],
          pagination: {
            edge: false
          },
          columnsDropdown: true
        }
      }
    },
    mounted () {
      // this.chartData is created in the mixin and contains all the data needed to build the chart.
      this.getSummaryInfo()
    },
    methods: {
      getSummaryInfo () {
        AXIOS.get(`api/r/info/0`, {
          params: {
            id: 0
          }
        }).then(response => {
          this.data = response.data
        }).catch(e => {
          console.error(e)
        })
      }
    }
  }
</script>
<style>
</style>
