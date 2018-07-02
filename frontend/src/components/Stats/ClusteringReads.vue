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
        columns: ['id', 'readsIndices', 'mean', 'median', 'quantile25', 'quantile75', 'outliersRatio'],
        data: [],
        options: {
          headings: {
            id: 'Cluster id',
            mean: 'Mean',
            median: 'Median',
            quantile25: '1st quantile',
            quantile75: '3rd quantile',
            outliersRatio: 'Outliers ratio [%]',
            readsIndices: 'Reads indices'
          },
          headingsTooltips: {
            mean: 'Mean of mean bases quality in cluster',
            median: 'Mean median of  bases quality in cluster',
            quantile25: 'Mean first quantile of bases quality in cluster',
            quantile75: 'Mean third quantile of bases quality in cluster',
            outliersRatio: 'Mean proportion of outliers bases in cluster reads',
            readsIndices: 'Indices of reads in cluster'
          },
          sortable: ['id', 'mean', 'median', 'q25', 'q75', 'outliersRatio'],
          filterable: ['readsIndices'],
          pagination: {
            edge: false
          },
          columnsDropdown: true,
          texts: {
            filter: 'Search in indices:'
          },
          columnsClasses: {
            id: 'width-8',
            mean: 'width-8',
            median: 'width-8',
            quantile25: 'width-8',
            quantile75: 'width-8',
            outliersRatio: 'width-10'
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
