<template>
  <div>
    <h3>2D detection results</h3>
    <p class="category">* Use shift key to enable multi sorting</p>
    <br>
    <v-client-table :columns="columns"
                    :data="this.sourceData"
                    :options="options">
    </v-client-table>
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
        columns: ['clusterName', 'mode', 'readIds'],
        data: [],
        options: {
          headings: {
            clusterName: 'Cluster id',
            mode: 'Mode',
            readIds: 'Reads ids'
          },
          headingsTooltips: {
            clusterName: 'Cluster id',
            mode: 'Mean mode of reads',
            readIds: 'Ids of reads in cluster'
          },
          sortable: ['clusterName', 'mode'],
          filterable: ['readIds'],
          pagination: {
            edge: false
          },
          columnsDropdown: true,
          texts: {
            filter: 'Search in reads ids:'
          },
          columnsClasses: {
            clusterName: 'width-10',
            mode: 'width-8'
          }
        }
      }
    }
  }
</script>

<style lang="scss">

  td {
    overflow: auto;
  }

  table {
    table-layout: fixed;
  }

  .width-8 {
    width: 8%;
  }

  .width-10 {
    width: 10%;
  }

</style>
