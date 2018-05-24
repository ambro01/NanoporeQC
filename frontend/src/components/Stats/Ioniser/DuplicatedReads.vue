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
        columns: ['sequence', 'count'],
        data: [],
        options: {
          headings: {
            sequence: 'Sequence',
            count: 'Count'
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
    },
    mounted () {
      this.getDuplicatedReadso()
    },
    watch: {
      id: function (newVal, oldVal) {
      },
      reloadData: function (newVal, oldVal) {
        if (!oldVal && newVal) {
          this.getDuplicatedReads()
          this.$emit('reloadDataEvent')
        }
      }
    },
    methods: {
      getDuplicatedReads () {
        this.$http.get(`api/analysis/stats/duplicated-sequences`, {
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
