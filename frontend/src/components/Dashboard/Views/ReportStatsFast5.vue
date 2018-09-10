<template>
  <div>
    <div class="card">
      <div class="header">
        <div class="row">
          <h4 class="title" style="margin-left: 20px">Report of analysis</h4>
          <button class="btn btn-primary btn-wd refresh-button left-margin"
                  title="Download HTML report from FastQC tool"
                  @click.prevent="downloadHtmlReport">
            HTML report
          </button>
          <button class="btn btn-primary btn-wd refresh-button left-margin"
                  @click.prevent="exportCsv">
            Export CSV
          </button>
          <button class="btn btn-primary btn-wd refresh-button left-margin"
                  @click.prevent="refreshData">
            Refresh
          </button>
        </div>
      </div>
      <div class="content">
        <vue-tabs active-tab-color="#f4f3ef" @tab-change="handleTabChange">
          <v-tab title="Summary info"></v-tab>
          <v-tab title="Reads accumulation"></v-tab>
          <v-tab title="Active channels"></v-tab>
          <v-tab title="Reads quality"></v-tab>
          <v-tab title="Reads quality density"></v-tab>
          <v-tab title="Reads quality factors"></v-tab>
          <v-tab title="Reads category counts"></v-tab>
          <v-tab title="Events"></v-tab>
          <v-tab title="Reads per channel"></v-tab>
          <v-tab title="Kilobases per channel"></v-tab>
          <!--<v-tab title="Clustering"></v-tab>-->
        </vue-tabs>
        <div v-if="this.tabIndex === 0">
          <summary-info :sourceData="this.dataSummaryInfo" v-if="this.dataSummaryInfo != null"></summary-info>
          <label class="control-label">
            <p>Summary information about files of analysis</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 1">
          <reads-accumulation :chart-data="this.dataReadsAccumulation"></reads-accumulation>
          <label class="control-label">
            <p>x - duration of an experiment [sec]</p>
            <p>y - accumulation of reads</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 2">
          <active-channels :chart-data="this.dataActiveChannels"></active-channels>
          <label class="control-label">
            <p>x - duration of an experiment [sec]</p>
            <p>y - number of active channels</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 3">
          <reads-quality-multi :chart-data="this.dataReadsQualityMulti"></reads-quality-multi>
          <label class="control-label">
            <p>x - read</p>
            <p>y - mean base quality</p>
          </label>
          <hr>
          <div class="row">
            <h3 style="margin-left: 20px">Outliers summary</h3>
            <div class="col-sm-4">
          <pre style="display: block; white-space: pre-line; font-size: 15px">
            <strong>2D reads</strong>
            Outliers identified: {{this.dataReadsQualityOutliers2D['outliersCount'][0]}} from {{this.dataReadsQualityOutliers2D['total'][0]}} observations
            Proportion (%) of outliers: {{this.dataReadsQualityOutliers2D['proportion'][0]}}
            Mean of the outliers: {{this.dataReadsQualityOutliers2D['outliersMean'][0]}}
            Mean without removing outliers: {{this.dataReadsQualityOutliers2D['meanWithOutliers'][0]}}
            Mean if we remove outliers: {{this.dataReadsQualityOutliers2D['meanWithoutOutliers'][0]}}
            <button style="margin-top:20px" class="btn btn-primary btn-wd code-button"
                    @click.prevent="csvReadsQualityOutliers('2D')">
              Get outliers
            </button>
            <button class="btn btn-primary btn-wd refresh-button"
                    @click.prevent="csvReadsQualityDataWithoutOutliers('2D')">
              Get data without outliers
            </button>
          </pre>
            </div>
            <div class="col-sm-4">
          <pre style="display: block; white-space: pre-line; font-size: 15px">
            <strong>Template reads</strong>
            Outliers identified: {{this.dataReadsQualityOutliers2D['outliersCount'][0]}} from {{this.dataReadsQualityOutliers2D['total'][0]}} observations
            Proportion (%) of outliers: {{this.dataReadsQualityOutliers2D['proportion'][0]}}
            Mean of the outliers: {{this.dataReadsQualityOutliers2D['outliersMean'][0]}}
            Mean without removing outliers: {{this.dataReadsQualityOutliers2D['meanWithOutliers'][0]}}
            Mean if we remove outliers: {{this.dataReadsQualityOutliers2D['meanWithoutOutliers'][0]}}
            <button style="margin-top:20px" class="btn btn-primary btn-wd code-button"
                    @click.prevent="csvReadsQualityOutliers('Template')">
              Get outliers
            </button>
            <button class="btn btn-primary btn-wd refresh-button"
                    @click.prevent="csvReadsQualityDataWithoutOutliers('Template')">
              Get data without outliers
            </button>
          </pre>
            </div>
            <div class="col-sm-4">
          <pre style="display: block; white-space: pre-line; font-size: 15px">
            <strong>Complement reads</strong>
            Outliers identified: {{this.dataReadsQualityOutliersTemplate['outliersCount'][0]}} from {{this.dataReadsQualityOutliersTemplate['total'][0]}} observations
            Proportion (%) of outliers: {{this.dataReadsQualityOutliersTemplate['proportion'][0]}}
            Mean of the outliers: {{this.dataReadsQualityOutliersTemplate['outliersMean'][0]}}
            Mean without removing outliers: {{this.dataReadsQualityOutliersTemplate['meanWithOutliers'][0]}}
            Mean if we remove outliers: {{this.dataReadsQualityOutliersTemplate['meanWithoutOutliers'][0]}}
            <button style="margin-top:20px" class="btn btn-primary btn-wd code-button"
                    @click.prevent="csvReadsQualityOutliers('Complement')">
              Get outliers
            </button>
            <button class="btn btn-primary btn-wd refresh-button"
                    @click.prevent="csvReadsQualityDataWithoutOutliers('Complement')">
              Get data without outliers
            </button>
          </pre>
            </div>
          </div>
        </div>
        <div v-if="this.tabIndex === 4">
          <reads-quality-density-multi :chart-data="this.dataReadsQualityDensity2D"></reads-quality-density-multi>
          <label class="control-label">
            <p>x - mean 2d base quality</p>
            <p>y - quality density</p>
          </label>
          <reads-quality-density-multi :chart-data="this.dataReadsQualityDensityTemplate"></reads-quality-density-multi>
          <label class="control-label">
            <p>x - mean template base quality</p>
            <p>y - quality density</p>
          </label>
          <reads-quality-density-multi
            :chart-data="this.dataReadsQualityDensityComplement"></reads-quality-density-multi>
          <label class="control-label">
            <p>x - mean complement base quality</p>
            <p>y - quality density</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 5">
          <reads-category-quality :chart-data="this.dataReadsCategoryQuality"></reads-category-quality>
          <label class="control-label">
            <p>x - read categories</p>
            <p>y - mean base quality factors</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 6">
          <reads-category-counts :chart-data="this.dataReadsCategoryCounts"></reads-category-counts>
          <label class="control-label">
            <p>y - number of readings for file and each strand category</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 7">
          <events-counts :chart-data="this.dataEventsCounts"></events-counts>
          <label class="control-label">
            <p>x - duration of an experiment [sec]</p>
            <p>y - number of events</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 8">
          <reads-per-channel :chart-data="this.dataReadsPerChannel"></reads-per-channel>
          <label class="control-label">
            <p>x - channel</p>
            <p>y - number of reads</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 9">
          <kb-per-channel :chart-data="this.dataKbPerChannel"></kb-per-channel>
          <label class="control-label">
            <p>x - channel</p>
            <p>y - kb (kilobase pair) of data </p>
          </label>
        </div>
        <div v-if="this.tabIndex === 10">
          <clustering-panel
            :analysisType="'Fast5'">
          </clustering-panel>
        </div>
      </div>
    </div>
    <quality-status v-if="this.tabIndex !== 10"
                    :qualityStatus="this.qualityStatus">
    </quality-status>
  </div>
</template>

<script>
  import ReadsAccumulation from 'src/components/Charts/Ioniser/fast5/ReadsAccumulation.vue'
  import ActiveChannels from 'src/components/Charts/Ioniser/fast5/ActiveChannels.vue'
  import ReadsQualityMulti from 'src/components/Charts/Ioniser/fast5/ReadsQualityMulti.vue'
  import ReadsCategoryQuality from 'src/components/Charts/Ioniser/fast5/ReadsCategoryQuality.vue'
  import ReadsQualityDensityMulti from 'src/components/Charts/Ioniser/fast5/ReadsQualityDensityMulti.vue'
  import ReadsCategoryCounts from 'src/components/Charts/Ioniser/fast5/ReadsCategoryCounts.vue'
  import EventsCounts from 'src/components/Charts/Ioniser/fast5/EventsCounts.vue'
  import ReadsPerChannel from 'src/components/Charts/Ioniser/fast5/ReadsPerChannel.vue'
  import KbPerChannel from 'src/components/Charts/Ioniser/fast5/KbPerChannel.vue'
  import SummaryInfo from 'src/components/Stats/SummaryInfo.vue'
  import QualityStatus from 'src/components/Parts/QualityStatus.vue'
  import ClusteringPanel from 'src/components/Parts/ClusteringPanel.vue'
  import CsvService from 'src/components/Services/CsvService.vue'
  import PoretoolsReportService from 'src/components/Services/PoretoolsReportService.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'

  export default {
    components: {
      StatsCard,
      ReadsAccumulation,
      ActiveChannels,
      ReadsQualityMulti,
      ReadsQualityDensityMulti,
      ReadsCategoryCounts,
      ReadsCategoryQuality,
      EventsCounts,
      ReadsPerChannel,
      KbPerChannel,
      SummaryInfo,
      QualityStatus,
      ClusteringPanel
    },
    props: [
      'id',
      'qualityStatus'
    ],
    data () {
      return {
        reloadDataSummaryInfo: false,
        dataSummaryInfo: null,
        dataReadsAccumulation: null,
        dataActiveChannels: null,
        dataReadsCategoryCounts: null,
        dataReadsCategoryQuality: null,
        dataEventsCounts: null,
        dataReadsPerChannel: null,
        dataKbPerChannel: null,
        dataReadsQualityMulti: null,
        dataReadsQualityDensityTemplate: null,
        dataReadsQualityDensityComplement: null,
        dataReadsQualityDensity2D: null,
        dataReadsQualityOutliers2D: null,
        dataReadsQualityOutliersTemplate: null,
        dataReadsQualityOutliersComplement: null,
        tabIndex: 0
      }
    },
    watch: {
      id: function (newVal, oldVal) {
        if (oldVal !== newVal && newVal > 0) {
          this.getAllData()
        }
      }
    },
    mounted () {
      this.getAllData()
    },
    methods: {
      handleTabChange (tabIndex, newTab, oldTab) {
        this.tabIndex = tabIndex
      },
      getSummaryInfo () {
        this.$http.get(`api/analysis/stats/info`).then(response => {
          this.dataSummaryInfo = response.data
        })
          .catch(() => this.$toast.error())
      },
      getReadsAccumulation () {
        this.$http.get(`api/analysis/stats/readsAccumulation/`, {
          params: {
            valuesNames: ['minute', 'accumulation']
          }
        }).then(response => {
          this.dataReadsAccumulation = {
            labels: response.data.values['minute'],
            datasets: [
              {
                backgroundColor: '#f87979',
                data: response.data.values['accumulation'],
                pointRadius: 0
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getActiveChannels () {
        this.$http.get(`api/analysis/stats/activeChannels`, {
          params: {
            valuesNames: ['minute', 'channels']
          }
        }).then(response => {
          this.dataActiveChannels = {
            labels: response.data.values['minute'],
            datasets: [
              {
                backgroundColor: '#0096f8',
                pointBackgroundColor: '#0096f8',
                showLine: true,
                borderColor: '#0096f8',
                steppedLine: true,
                fill: false,
                pointRadius: 0,
                data: response.data.values['channels']
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getReadsCategoryCounts () {
        this.$http.get(`api/analysis/stats/readsCategoryCounts`, {
          params: {
            valuesNames: ['category', 'files_count', 'template_count', 'complement_count', 'full_2d_count']
          }
        }).then(response => {
          this.dataReadsCategoryCounts = {
            labels: [],
            datasets: [
              {
                label: 'files',
                backgroundColor: '#f87979',
                data: response.data.values['files_count']
              },
              {
                label: 'template',
                backgroundColor: '#f8ac5f',
                data: response.data.values['template_count']
              },
              {
                label: 'complement',
                backgroundColor: '#47f889',
                data: response.data.values['complement_count']
              },
              {
                label: 'full 2d',
                backgroundColor: '#82c3f8',
                data: response.data.values['full_2d_count']
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getReadsCategoryQuality () {
        this.$http.get(`api/analysis/stats/readsCategoryQuality`, {
          params: {
            valuesNames: ['category', 'min', 'max', 'mean', 'median', 'q25', 'q75']
          }
        }).then(response => {
          this.dataReadsCategoryQuality = {
            labels: response.data.values['category'],
            datasets: [
              {
                label: 'min',
                backgroundColor: '#f87979',
                data: response.data.values['min']
              },
              {
                label: 'mean',
                backgroundColor: '#f8ac5f',
                data: response.data.values['mean']
              },
              {
                label: 'q25',
                backgroundColor: '#d392f8',
                data: response.data.values['q25']
              },
              {
                label: 'median',
                backgroundColor: '#47f889',
                data: response.data.values['median']
              },
              {
                label: 'q75',
                backgroundColor: '#8f8792',
                data: response.data.values['q75']
              },
              {
                label: 'max',
                backgroundColor: '#82c3f8',
                data: response.data.values['max']
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getEventsCounts () {
        this.$http.get(`api/analysis/stats/eventsCounts`, {
          params: {
            valuesNames: ['time', 'events']
          }
        }).then(response => {
          this.dataEventsCounts = {
            labels: response.data.values['time'],
            datasets: [
              {
                label: 'Events in time',
                fill: false,
                borderColor: '#f8ac5f',
                pointRadius: 0,
                steppedLine: true,
                data: response.data.values['events']
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getReadsQualityMulti () {
        this.$http.get(`api/analysis/stats/readsQualityMulti`, {
          params: {
            valuesNames: ['id', 'q_template', 'q_complement', 'q_2D']
          }
        }).then(response => {
          this.dataReadsQualityMulti = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'Template quality',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['q_template'],
                pointRadius: 0,
                borderWidth: 2
              },
              {
                label: 'Complement quality',
                borderColor: '#d392f8',
                fill: false,
                data: response.data.values['q_complement'],
                pointRadius: 0,
                borderWidth: 2
              },
              {
                label: '2d quality',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['q_2D'],
                pointRadius: 0,
                borderWidth: 2
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getReadsQualityDensityMulti () {
        this.$http.get(`api/analysis/stats/readsQualityDensityMulti`, {
          params: {
            valuesNames: ['quality_template', 'quality_complement', 'quality_2D',
              'density_template', 'density_complement', 'density_2D']
          }
        }).then(response => {
          this.dataReadsQualityDensity2D = {
            labels: response.data.values['quality_2D'],
            datasets: [
              {
                label: '2D quality density',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['density_2D'],
                pointRadius: 0,
                borderWidth: 2
              }
            ]
          }
          this.dataReadsQualityDensityTemplate = {
            labels: response.data.values['quality_template'],
            datasets: [
              {
                label: 'Template quality density',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['density_template'],
                pointRadius: 0,
                borderWidth: 2
              }
            ]
          }
          this.dataReadsQualityDensityComplement = {
            labels: response.data.values['quality_complement'],
            datasets: [
              {
                label: 'Complement quality density',
                borderColor: '#d392f8',
                fill: false,
                data: response.data.values['density_complement'],
                pointRadius: 0,
                borderWidth: 2
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getReadsPerChannel () {
        this.$http.get(`api/analysis/stats/readsPerChannel`, {
          params: {
            valuesNames: ['channel', 'reads']
          }
        }).then(response => {
          this.dataReadsPerChannel = {
            labels: response.data.values['channel'],
            datasets: [
              {
                borderColor: '#47f889',
                data: response.data.values['reads'],
                steppedLine: true,
                pointRadius: 5,
                fill: false
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getKbPerChannel () {
        this.$http.get(`api/analysis/stats/kbPerChannel`, {
          params: {
            xName: 'channel',
            valuesNames: ['channel', 'kb']
          }
        }).then(response => {
          this.dataKbPerChannel = {
            labels: response.data.values['channel'],
            datasets: [
              {
                borderColor: '#f8ac5f',
                data: response.data.values['kb'],
                steppedLine: true,
                pointRadius: 5,
                fill: false
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getReadsQualityOutliers2D () {
        this.$http.get(`api/analysis/stats/reads2DQualityOutliers`, {
          params: {
            valuesNames: ['outliersCount', 'total', 'proportion', 'outliersMean', 'meanWithOutliers', 'meanWithoutOutliers']
          }
        }).then(response => {
          this.dataReadsQualityOutliers2D = response.data.values
        })
          .catch(() => this.$toast.error())
      },
      getReadsQualityOutliersTemplate () {
        this.$http.get(`api/analysis/stats/readsTemplateQualityOutliers`, {
          params: {
            valuesNames: ['outliersCount', 'total', 'proportion', 'outliersMean', 'meanWithOutliers', 'meanWithoutOutliers']
          }
        }).then(response => {
          this.dataReadsQualityOutliersTemplate = response.data.values
        }).catch(() => this.$toast.error())
      },
      getReadsQualityOutliersComplement () {
        this.$http.get(`api/analysis/stats/readsComplementQualityOutliers`, {
          params: {
            valuesNames: ['outliersCount', 'total', 'proportion', 'outliersMean', 'meanWithOutliers', 'meanWithoutOutliers']
          }
        }).then(response => {
          this.dataReadsQualityOutliersComplement = response.data.values
        }).catch(() => this.$toast.error())
      },

      getAllData () {
        this.getSummaryInfo()
        this.getReadsAccumulation()
        this.getActiveChannels()
        this.getReadsCategoryCounts()
        this.getReadsCategoryQuality()
        this.getEventsCounts()
        this.getReadsQualityMulti()
        this.getReadsQualityDensityMulti()
        this.getReadsPerChannel()
        this.getKbPerChannel()
        this.getReadsQualityOutliers2D()
        this.getReadsQualityOutliersTemplate()
        this.getReadsQualityOutliersComplement()
      },

      refreshData () {
        switch (this.tabIndex) {
          case 0:
            this.getSummaryInfo()
            break
          case 1:
            this.getReadsAccumulation()
            break
          case 2:
            this.getActiveChannels()
            break
          case 3:
            this.getReadsQualityMulti()
            this.getReadsQualityOutliers2D()
            this.getReadsQualityOutliersTemplate()
            this.getReadsQualityOutliersComplement()
            break
          case 4:
            this.getReadsQualityDensityMulti()
            break
          case 5:
            this.getReadsCategoryQuality()
            break
          case 6:
            this.getReadsCategoryCounts()
            break
          case 7:
            this.getEventsCounts()
            break
          case 8:
            this.getReadsPerChannel()
            break
          case 9:
            this.getKbPerChannel()
            break
        }
      },

      exportCsv () {
        switch (this.tabIndex) {
          case 0:
            CsvService.methods.csvSummaryInfo()
            break
          case 1:
            CsvService.methods.csvReadsAccumulation()
            break
          case 2:
            CsvService.methods.csvActiveChannels()
            break
          case 3:
            CsvService.methods.csvReadsQualityMulti()
            break
          case 4:
            CsvService.methods.csvReadsQualityDensityMulti()
            break
          case 5:
            CsvService.methods.csvReadsCategoryQuality()
            break
          case 6:
            CsvService.methods.csvReadsCategoryCounts()
            break
          case 7:
            CsvService.methods.csvEventsCounts()
            break
          case 8:
            CsvService.methods.csvReadsPerChannel()
            break
          case 9:
            CsvService.methods.csvKbPerChannel()
            break
        }
      },

      downloadHtmlReport () {
        PoretoolsReportService.methods.downloadHtmlReport()
      }

    }
  }
</script>

<style lang="css" scoped>
  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }

  .refresh-button {
    float: right;
  }

  .code-button {
    float: left;
  }

  .left-margin {
    margin-left: 20px;
  }

</style>
