<template>
  <div>
    <h3>Summary information about .fast5 files</h3>
    <p class="category">* Use shift key to enable multi sorting</p>
    <br>
    <v-client-table :columns="columns" :data="this.sourceData" :options="options"></v-client-table>
  </div>
</template>

<script>
  export default {
    components: {},
    props: [
      'sourceData'
    ],
    data () {
      return {
        columns: ['id', 'fileName', 'channelIndex', 'strandIndexInChannel', 'eventsNo', 'startTime', 'duration',
          'hasTemplate', 'eventsNoTemplate', 'hasComplement', 'eventsNoComplement', 'is2d'],
        data: [],
        options: {
          headings: {
            id: 'Id',
            fileName: 'File name',
            channelIndex: 'Channel',
            strandIndexInChannel: 'Read no',
            eventsNo: 'Events',
            startTime: 'Start time',
            duration: 'Duration',
            hasTemplate: 'T',
            eventsNoTemplate: 'T events',
            hasComplement: 'C',
            eventsNoComplement: 'C events',
            is2d: '2d'
          },
          headingsTooltips: {
            channelIndex: 'Channel number',
            strandIndexInChannel: 'Read number from channel (read index)',
            eventsNo: 'Changes in the measured current',
            startTime: 'Time in seconds after the reading started',
            duration: 'Time in seconds the reading lasted',
            eventsNoTemplate: 'Events of template strand',
            eventsNoComplement: 'Events of complement strand',
            hasTemplate: 'Template strand',
            hasComplement: 'Complement strand',
            is2d: 'Full 2d strand (template & complement)'
          },
          sortable: ['id', 'fileName', 'channelIndex', 'strandIndexInChannel', 'eventsNo', 'startTime', 'duration',
            'eventsNoTemplate', 'eventsNoComplement'],
          filterable: ['fileName'],
          pagination: {
            edge: false
          },
          columnsDropdown: true,
          texts: {
            filter: 'Search in names:'
          },
          columnsClasses: {
            is2d: 'width-3',
            hasTemplate: 'width-3',
            hasComplement: 'width-3',
            eventsNoTemplate: 'width-6',
            eventsNoComplement: 'width-6',
            eventsNo: 'width-6',
            strandIndexInChannel: 'width-6',
            channelIndex: 'width-6',
            id: 'width-3',
            duration: 'width-6',
            startTime: 'width-6'
          }
        }
      }
    }
  }
</script>

<style lang="scss">

  .width-6 {
    width: 6%;
  }

  .width-3 {
    width: 3%;
  }

</style>
