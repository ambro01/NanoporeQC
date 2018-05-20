<template>
  <div>
    <div>
      <h3>Analysiss repository</h3>
      <p class="category">* Use shift key to enable multi sorting</p>
      <v-client-table :columns="columns" :data="data" :options="options">
        <a slot="viewResults" slot-scope="props">
          <button class="btn btn-sm btn-primary" @click="onShowDetails(props.row)">Show details</button>
        </a>
        <a slot="deleteRow" slot-scope="props">
          <button class="btn btn-sm btn-danger" @click="onDeleteRow(props.index, props.row.id)">Delete</button>
        </a>
        <a slot="runFastQ" slot-scope="props" v-if="props.row.type == 'Fast5'">
          <button class="btn btn-sm" @click="onRunAnalysis(props.row)">Run analysis</button>
        </a>
      </v-client-table>
    </div>
    <report-stats-fast5 :id="this.detailsId" v-if="this.detailsId > 0 && this.analysisType == 'Fast5'"></report-stats-fast5>
    <report-stats-fastQ :id="this.detailsId" v-if="this.detailsId > 0 && this.analysisType == 'FastQ'"></report-stats-fastQ>
  </div>
</template>

<script>
  import ReportStatsFast5 from 'src/components/Dashboard/Views/ReportStatsFast5.vue'
  import ReportStatsFastQ from 'src/components/Dashboard/Views/ReportStatsFastQ.vue'

  export default {
    components: {
      ReportStatsFast5,
      ReportStatsFastQ
    },
    data () {
      return {
        analysisType: '',
        detailsId: 0,
        columns: ['name', 'type', 'runTime', 'comment', 'viewResults', 'deleteRow', 'runFastQ'],
        data: [],
        options: {
          headings: {
            name: 'Analysis name',
            type: 'Type',
            runTime: 'Run time',
            comment: 'Comment',
            deleteRow: 'Delete analysis',
            viewResults: 'View results',
            runFastQ: 'Run FastQ analysis'
          },
          sortable: ['name', 'type', 'runTime'],
          filterable: ['name'],
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
      this.getAnalysisForCurrentUser()
    },
    methods: {
      getAnalysisForCurrentUser () {
        this.$http.get(`api/analysis/current-user`).then(response => {
          this.data = response.data
        }).catch(e => {
          console.error(e)
        })
      },
      onShowDetails (row) {
        console.log(row)
        this.detailsId = row.id
        this.analysisType = row.type
      },
      onDeleteRow (rowId, analyseId) {
        this.$http.post(`api/analysis/delete/` + analyseId).then(response => {
          if (response.status === 200) {
            this.data.splice(rowId - 1, 1)
            this.$toast.success({
              title: 'Success',
              message: 'Successful removing'
            })
          } else {
            this.$toast.error({
              title: 'Error',
              message: 'Removing failed'
            })
          }
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Removing failed'
          })
        })
      },
      onRunAnalysis (row) {
        this.$http.get(`api/analysis/from-fast5/` + row.id).then(response => {
          if (response.status === 200) {
            this.detailsId = row.id
            this.analysisType = 'FastQ'
            this.$toast.success({
              title: 'Success',
              message: 'Successful running'
            })
          } else {
            this.$toast.error({
              title: 'Error',
              message: 'Running failed'
            })
          }
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Running failed'
          })
        })
      }
    }
  }
</script>
<style>
</style>
