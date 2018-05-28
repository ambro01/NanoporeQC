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
            fileName: 'File name',
            occurrences: 'Occurrences',
            reads: 'Reads'
          },
          sortable: ['fileName', 'occurrences', 'reads'],
          filterable: ['fileName'],
          pagination: {
            edge: false
          },
          texts: {
            filter: 'Search in file names:'
          }
        }
      }
    }
  }
</script>
<style>
</style>
