<template>
  <div>
    <h3>Summary information about .fast5 files</h3>
    <p class="category">* Use shift key to enable multi sorting</p>
    <br>
    <v-client-table :columns="columns" :data="data" :options="options"></v-client-table>
  </div>
</template>

<script>
  export default {
    components: {},
    props: [
      'id',
      'reloadData'
    ],
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
            strandIndexInChannel: 'Strand no',
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
          sortable: ['id', 'fileName', 'channelIndex', 'strandIndexInChannel', 'eventsNo', 'startTime', 'duration'],
          filterable: ['fileName'],
          pagination: {
            edge: false
          },
          columnsDropdown: true,
          texts: {
            filter: 'Search in names:'
          }
        }
      }
    },
    watch: {
      id: function (newVal, oldVal) {
      },
      reloadData: function (newVal, oldVal) {
        if (!oldVal && newVal) {
          this.getSummaryInfo()
          this.$emit('reloadData')
        }
      }
    },
    methods: {
      getSummaryInfo () {
        this.$http.get(`api/analysis/stats/info`).then(response => {
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
