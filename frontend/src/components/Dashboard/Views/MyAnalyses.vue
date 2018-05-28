<template>
  <div>
    <div>
      <h3>Analysis repository</h3>
      <p class="category">* Use shift key to enable multi sorting</p>
      <v-client-table :columns="columns" :data="data" :options="options">
        <a slot="viewResults" slot-scope="props">
          <button class="btn btn-sm btn-primary button" @click="onShowDetails(props.row)">Show details</button>
        </a>
        <a slot="deleteRow" slot-scope="props">
          <button class="btn btn-sm btn-danger button" @click="onDeleteRow(props.index, props.row.id)">Delete</button>
        </a>
        <a slot="runFastQ" slot-scope="props" v-if="props.row.type == 'Fast5'">
          <button class="btn btn-sm button" @click="onRunAnalysis(props.row)">Run analysis</button>
        </a>
      </v-client-table>
    </div>
    <report-stats-fast5 :id="this.detailsId"
                        v-if="this.detailsId > 0 && this.analysisType == 'Fast5'"></report-stats-fast5>
    <report-stats-fastQ :id="this.detailsId"
                        v-if="this.detailsId > 0 && this.analysisType == 'FastQ'"></report-stats-fastQ>
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
        columns: ['name', 'comment', 'type', 'runTime', 'viewResults', 'deleteRow', 'runFastQ'],
        data: [],
        options: {
          headings: {
            name: 'Analysis name',
            type: 'Type',
            runTime: 'Run time',
            comment: 'Comment',
            deleteRow: 'Delete analysis',
            viewResults: 'View results',
            runFastQ: 'Run FastQ'
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
          compileTemplates: true,
          orderBy: {
            column: 'runTime',
            ascending: false
          },
          columnsClasses: {
            viewResults: 'view-results',
            deleteRow: 'delete-row',
            runFastQ: 'run-fastq',
            type: 'type',
            runTime: 'run-time'
          }
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
          this.$toast.success({
            title: 'Success',
            message: 'Successful loading saved analyses'
          })
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Running failed'
          })
        })
      },
      onShowDetails (row) {
        this.loadAnalysis(row.id, row.type)
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
        this.$http.post(`api/analysis/from-fast5/` + row.id).then(response => {
          this.$toast.success({
            title: 'Success',
            message: 'Successful running'
          })
          this.detailsId = row.id
          this.analysisType = 'FastQ'
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Running failed'
          })
        })
      },
      loadAnalysis (id, type) {
        this.$http.get(`api/analysis/` + id, {
          params: {
            type: type
          }
        }).then(response => {
          this.reloadData = true
          this.$toast.success({
            title: 'Success',
            message: 'Successful data loading'
          })
          this.detailsId = id
          this.analysisType = type
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Data loading failed'
          })
        })
      }
    }
  }
</script>

<style lang="css">
  .btn.btn-sm {
    margin: 0 auto;
    display: block;
  }

  .button {
    width: 120px;
  }

  .delete-row {
    width: 8%;
  }

  .view-results {
    width: 8%;
  }

  .run-fastq {
    width: 8%;
  }

  .type {
    width: 6%;
  }

  .run-time {
    width: 10%;
  }
</style>
