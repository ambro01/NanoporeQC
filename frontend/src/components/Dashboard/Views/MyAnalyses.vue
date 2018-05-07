<template>
  <div>
    <div>
      <h3>Analyses repository</h3>
      <p class="category">* Use shift key to enable multi sorting</p>
      <v-client-table :columns="columns" :data="data" :options="options">
        <a slot="deleteRow" slot-scope="props">
          <button class="btn btn-sm btn-danger" @click="onDeleteRow(props.index, props.row.id)">Delete</button>
        </a>
        <a slot="viewResults" slot-scope="props">
          <button class="btn btn-sm btn-primary" @click="onShowDetails(props.row.id)">Show details</button>
        </a>
      </v-client-table>
    </div>
    <report-stats :id="this.detailsId" v-if="this.detailsId > 0"></report-stats>
  </div>
</template>

<script>
  import ReportStats from 'src/components/Dashboard/Views/ReportStats.vue'

  export default {
    components: {
      ReportStats
    },
    data () {
      return {
        detailsId: 0,
        columns: ['name', 'runTime', 'comment', 'deleteRow', 'viewResults'],
        data: [],
        options: {
          headings: {
            name: 'Analyse name',
            runTime: 'Run time',
            comment: 'Comment',
            erase: 'Delete',
            viewResults: 'View results'
          },
          sortable: ['analyseName', 'runTime'],
          filterable: ['analyseName'],
          pagination: {
            edge: false
          },
          texts: {
            filter: 'Search in names:'
          },
          columnsDropdown: true,
          compileTemplates: true
        }
      }
    },
    mounted () {
      this.getAnalysesForCurrentUser()
    },
    methods: {
      getAnalysesForCurrentUser () {
        this.$http.get(`api/analyse/current-user`).then(response => {
          this.data = response.data
        }).catch(e => {
          console.error(e)
        })
      },
      onShowDetails (analyseId) {
        this.detailsId = analyseId
      },
      onDeleteRow (rowId, analyseId) {
        this.$http.post(`api/analyse/delete/` + analyseId).then(response => {
          this.data.splice(rowId - 1, 1)
        }).catch(e => {
          console.error(e)
        })
      }
    }
  }
</script>
<style>
</style>
