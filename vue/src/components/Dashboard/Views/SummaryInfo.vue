<template>
  <div>
    <button class="btn btn-primary" @click="getSummaryInfo">View results</button>
    <h3>Summary information about .fast5 files</h3>
    <p class="category">* Use shift key to enable multi sorting</p>
    <v-client-table :columns="columns" :data="data" :options="options"></v-client-table>
  </div>
</template>

<script>
  import { AXIOS } from 'src/http-common'

  export default {
    components: {},
    props: ['reloadData'],
    data () {
      return {
        columns: ['id', 'fileName', 'channelIndex', 'strandIndexInChannel', 'eventsNo', 'startTime', 'duration'],
        data: [],
        options: {
          headings: {
            id: 'Id',
            fileName: 'File name',
            channelIndex: 'Channel',
            strandIndexInChannel: 'Strand no',
            eventsNo: 'Events no',
            startTime: 'Start time'
          },
          sortable: ['id', 'channelIndex', 'strandIndexInChannel', 'eventsNo', 'startTime', 'duration'],
          filterable: ['fileName'],
          pagination: {
            edge: false
          },
          columnsDropdown: true,
          listColumns: {
            stat: [{}, {}]
          }
        }
      }
    },
    watch: {
      reloadData: function (newVal, oldVal) {
        console.log('newVal')
        console.log(newVal)
        console.log('oldVal')
        console.log(oldVal)
        if (!oldVal && newVal) {
          this.getSummaryInfo()
        }
      }
    },
    methods: {
      getSummaryInfo () {
        AXIOS.get(`api/r/info/0`, {
          params: {
            id: 0
          }
        }).then(response => {
          console.log(response.data)
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
