<template>
  <div class="row">
    <button class="btn btn-primary" @click="getSummaryInfo">View results</button>
    <div class="col-md-12">
      <div class="card">
        <paper-table :title="myTable.title" :sub-title="myTable.subTitle" :data="myTable.data" :columns="myTable.columns">

        </paper-table>
      </div>
    </div>

    <div class="col-md-12">
      <div class="card card-plain">
        <paper-table type="hover" :title="myTable.title" :sub-title="myTable.subTitle" :data="myTable.data" :columns="myTable.columns">

        </paper-table>
      </div>
    </div>

  </div>
</template>

<script>
  import PaperTable from 'components/UIComponents/PaperTable.vue'
  import { AXIOS } from 'src/http-common'

  const tableColumns = ['Id', 'Name', 'Salary', 'Country', 'City']

  export default {
    components: {
      PaperTable
    },
    mounted () {
      this.getSummaryInfo()
    },
    data () {
      return {
        tableData: null,
        myTable: {
          title: 'Fast5 files information summary',
          subTitle: '',
          columns: [...tableColumns],
          data: tableData
        }
      }
    },
    methods: {
      getSummaryInfo () {
        AXIOS.get(`api/r/info`, {
          params: {
            id: 0
          }
        }).then(response => {
          this.tableData = response.data
        }).catch(e => {
          console.error(e)
        })
      }
    }
  }
</script>
<style>

</style>
