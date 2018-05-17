<template>
  <div>
    <p class="category">* Use shift key to enable multi sorting</p>
    <br>
    <v-client-table :columns="columns" :data="data" :options="options"></v-client-table>
  </div>
</template>

<script>
  export default {
    components: {},
    props: [
      'id',
      'reloadData'
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
    },
    mounted () {
      this.getReadsDistribution()
    },
    watch: {
      id: function (newVal, oldVal) {
      },
      reloadData: function (newVal, oldVal) {
        if (!oldVal && newVal) {
          this.getReadsDistribution()
          this.$emit('reloadData')
        }
      }
    },
    methods: {
      getReadsDistribution () {
        this.$http.get(`api/analysis/stats/reads-distribution`, {
          params: {
            xName: 'sequence',
            yNames: ['count']
          }
        }).then(response => {
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
