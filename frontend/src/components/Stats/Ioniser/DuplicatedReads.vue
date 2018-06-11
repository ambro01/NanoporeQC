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
        columns: ['sequence', 'count'],
        data: [],
        options: {
          headings: {
            sequence: 'Sequence',
            count: 'Occurrences'
          },
          headingsTooltips: {
            sequence: 'Sequence of nucleotides',
            count: 'Number of occurrences'
          },
          sortable: ['count'],
          filterable: ['sequence'],
          pagination: {
            edge: false
          },
          texts: {
            filter: 'Search in sequences:'
          }
        }
      }
    }
  }
</script>
<style>
</style>
