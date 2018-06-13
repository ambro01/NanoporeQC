<template>
  <div>
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
        columns: ['fileName', 'occurrences', 'reads'],
        data: [],
        options: {
          headings: {
            fileName: 'Lane',
            occurrences: 'Occurrences',
            reads: 'Reads'
          },
          headingsTooltips: {
            fileName: 'Molecule sequence',
            occurrences: 'Number of molecule occurrences',
            reads: 'Number of reads (molecule’s base sequences)'
          },
          sortable: ['fileName', 'occurrences', 'reads'],
          filterable: ['fileName'],
          pagination: {
            edge: false
          },
          texts: {
            filter: 'Search in file names:'
          },
          columnsClasses: {
            occurrences: 'width-10',
            reads: 'width-10'
          }
        }
      }
    }
  }
</script>

<style lang="scss">

  .width-10 {
    width: 10%;
  }

</style>
