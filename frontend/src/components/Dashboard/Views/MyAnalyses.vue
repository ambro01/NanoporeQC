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
        <a slot="htmlReport" slot-scope="props" v-if="props.row.hasHtmlReport">
          <button class="btn btn-sm btn-info button" @click="onDownloadHtmlReport(props.row)">Download</button>
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

  const TEXT_HTML = 'text/html'

  export default {
    components: {
      ReportStatsFast5,
      ReportStatsFastQ
    },
    data () {
      return {
        analysisType: '',
        detailsId: 0,
        columns: ['name', 'comment', 'type', 'runTime', 'viewResults', 'deleteRow', 'runFastQ', 'htmlReport'],
        data: [],
        options: {
          headings: {
            name: 'Analysis name',
            type: 'Type',
            runTime: 'Run time',
            comment: 'Comment',
            deleteRow: 'Delete',
            viewResults: 'Results',
            runFastQ: 'Run FastQ',
            htmlReport: 'HTML report'
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
            viewResults: 'button-width',
            deleteRow: 'button-width',
            runFastQ: 'button-width',
            htmlReport: 'button-width',
            type: 'type',
            runTime: 'run-time',
            comment: 'comment'
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
        }).catch(e => {
          this.$toast.error({
            title: 'Error',
            message: 'Analyses loading failed'
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
      },
      onDownloadHtmlReport (row) {
        this.$http.get(`api/analysis/download-report/` + row.id).then(response => {
          const blob = new Blob([response.data], {type: TEXT_HTML})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = row.name + '.html'
          link.click()
        }).catch(e => {
          console.error(e)
        })
      }
    }
  }
</script>

<style lang="scss">
  .btn.btn-sm {
    margin: 0 auto;
    display: block;
  }

  .button {
    width: 100px;
  }

  .button-width {
    width: 6%;
  }

  .type {
    width: 6%;
  }

  .run-time {
    width: 10%;
  }

  .comment {
    width: 20%;
  }
</style>
