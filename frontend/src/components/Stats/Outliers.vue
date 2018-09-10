<template>
  <div>
    <h3>Outliers detection results</h3>
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
        columns: ['outlierPlace', 'distanceFromCentre', 'readId', 'name'],
        data: [],
        options: {
          headings: {
            outlierPlace: 'Outliers order',
            distanceFromCentre: 'Distance',
            readId: 'Read id',
            name: 'Read name'
          },
          headingsTooltips: {
            cluster: 'Place of read in descending distance order',
            distanceFromCentre: 'Distance from centre of cluster'
          },
          sortable: ['outlierPlace', 'distanceFromCentre', 'readId', 'name'],
          filterable: ['name'],
          pagination: {
            edge: false
          },
          columnsDropdown: true,
          texts: {
            filter: 'Search in reads names:'
          },
          columnsClasses: {
            outlierPlace: 'width-10',
            distanceFromCentre: 'width-10',
            readId: 'width-10'
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

  .width-10 {
    width: 10%;
  }

</style>
