<template>
  <div>
    <h3>Clustering results</h3>
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
        columns: ['cluster', 'readIds'],
        data: [],
        options: {
          headings: {
            cluster: 'Cluster id',
            readIds: 'Reads ids'
          },
          headingsTooltips: {
            cluster: 'Cluster id',
            readIds: 'Ids of reads in cluster'
          },
          sortable: ['cluster'],
          filterable: ['readIds'],
          pagination: {
            edge: false
          },
          columnsDropdown: true,
          texts: {
            filter: 'Search in reads ids:'
          },
          columnsClasses: {
            cluster: 'width-8'
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
