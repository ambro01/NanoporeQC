<template>
  <div>
    <h3>Reads info</h3>
    <p class="category">* Use shift key to enable multi sorting</p>
    <br>
    <v-client-table :columns="columns"
                    :data="this.sourceData"
                    :options="options">
    </v-client-table>
    Quality information about reads
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
        columns: ['id', 'name', 'mode', 'mean', 'median', 'quantile25', 'quantile75', 'cgContent', 'length'],
        data: [],
        options: {
          headings: {
            id: 'Read id',
            name: 'Read name',
            mode: 'Mode',
            mean: 'Mean',
            median: 'Median',
            quantile25: '1st quantile',
            quantile75: '3rd quantile',
            cgContent: 'CG',
            length: 'Bases number'
          },
          headingsTooltips: {
            mode: 'Mode of mean bases quality in read',
            mean: 'Mean of mean bases quality in read',
            median: 'Mean median of  bases quality in read',
            quantile25: 'Mean first quantile of bases quality in read',
            quantile75: 'Mean third quantile of bases quality in read',
            cgContent: 'Proportion od C and G nucleotides',
            length: 'Bases number in read'
          },
          sortable: ['id', 'name', 'mode', 'mean', 'median', 'quantile25', 'quantile75', 'cgContent', 'length'],
          filterable: ['name'],
          pagination: {
            edge: false
          },
          columnsDropdown: true,
          texts: {
            filter: 'Search in names:'
          },
          columnsClasses: {
            id: 'width-8',
            mode: 'width-8',
            mean: 'width-8',
            median: 'width-8',
            quantile25: 'width-8',
            quantile75: 'width-8',
            cgContent: 'width-8',
            length: 'width-8'
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
