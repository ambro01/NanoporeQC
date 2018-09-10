<template>
  <div>
    <div class="card">
      <div class="header"
           v-if="this.tabIndex < 11">
        <div class="row">
          <h4 class="title" style="margin-left: 20px">Report of analysis</h4>
          <button class="btn btn-primary btn-wd refresh-button left-margin"
                  title="Download HTML report from FastQC tool"
                  @click.prevent="downloadHtmlReport">
            HTML report
          </button>
          <button class="btn btn-primary btn-wd refresh-button left-margin"
                  title="Download CSV data set"
                  @click.prevent="exportCsv">
            Export CSV
          </button>
          <button class="btn btn-primary btn-wd refresh-button left-margin"
                  title="Send request again"
                  @click.prevent="refreshData">
            Refresh
          </button>
        </div>
      </div>
      <div class="content">
        <vue-tabs active-tab-color="#f4f3ef" @tab-change="handleTabChange">
          <v-tab title="Nucleotides counts"></v-tab>
          <v-tab title="Reads quality"></v-tab>
          <v-tab title="Reads quality density"></v-tab>
          <v-tab title="Bases calls"></v-tab>
          <v-tab title="Bases quality"></v-tab>
          <v-tab title="Bases quality density"></v-tab>
          <v-tab title="Bases CG content"></v-tab>
          <v-tab title="Bases CG density"></v-tab>
          <v-tab title="Sequences distribution"></v-tab>
          <v-tab title="Duplicated sequences"></v-tab>
          <v-tab title="Reads info"></v-tab>
          <v-tab title="Clustering"></v-tab>
          <v-tab title="Outliers detection"></v-tab>
          <v-tab title="2D detection"></v-tab>
        </vue-tabs>
        <div v-if="this.tabIndex === 0">
          <nucleotides-counts :chart-data="this.dataNucleotidesCounts"></nucleotides-counts>
          <label class="control-label">
            <p>A - adenine, G - guanine, C - cytosine. T - thymine, N - undefined</p>
            <p>y - number of nucleotides in each category</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 1">
          <reads-quality :chart-data="this.dataReadsQuality"></reads-quality>
          <label class="control-label">
            <p>x - read index</p>
            <p>y - mean base quality</p>
          </label>
          <div class="row">
            <h3 style="margin-left: 20px">Outliers summary</h3>
            <div class="col-sm-4">
            <pre style="display: block; white-space: pre-line; font-size: 15px">
                Outliers identified: {{this.dataReadsQualityOutliers['outliersCount'][0]}} from {{this.dataReadsQualityOutliers['total'][0]}} observations
                Proportion (%) of outliers: {{this.dataReadsQualityOutliers['proportion'][0]}}
                Mean of the outliers: {{this.dataReadsQualityOutliers['outliersMean'][0]}}
                Mean without removing outliers: {{this.dataReadsQualityOutliers['meanWithOutliers'][0]}}
                Mean if we remove outliers: {{this.dataReadsQualityOutliers['meanWithoutOutliers'][0]}}
                <button style="margin-top:20px" class="btn btn-primary btn-wd code-button"
                        @click.prevent="csvReadsQualityOutliers">
                  Get outliers
                </button>
                <button class="btn btn-primary btn-wd refresh-button"
                        @click.prevent="csvReadsQualityDataWithoutOutliers">
                  Get data without outliers
                </button>
              </pre>
            </div>
          </div>
        </div>
        <div v-if="this.tabIndex === 2">
          <reads-quality-density :chart-data="this.dataReadsQualityDensity"></reads-quality-density>
          <label class="control-label">
            <p>x - mean base quality</p>
            <p>y - quality density</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 3">
          <bases-calls :chart-data="this.dataBasesCalls"></bases-calls>
          <label class="control-label">
            <p>x - base index in read</p>
            <p>y - number of read nucleotides</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 4">
          <bases-quality :chart-data="this.dataBasesQuality"></bases-quality>
          <label class="control-label">
            <p>x - base index in read</p>
            <p>y - quality factors</p>
          </label>
          <div class="row">
            <div class="col-sm-4">
            <pre style="display: block; white-space: pre-line; font-size: 15px">
                Outliers identified: {{this.dataBasesQualityOutliers['outliersCount'][0]}} from {{this.dataBasesQualityOutliers['total'][0]}} observations
                Proportion (%) of outliers: {{this.dataBasesQualityOutliers['proportion'][0]}}
                Mean of the outliers: {{this.dataBasesQualityOutliers['outliersMean'][0]}}
                Mean without removing outliers: {{this.dataBasesQualityOutliers['meanWithOutliers'][0]}}
                Mean if we remove outliers: {{this.dataBasesQualityOutliers['meanWithoutOutliers'][0]}}
                <button style="margin-top:20px" class="btn btn-primary btn-wd code-button"
                        @click.prevent="csvBasesQualityOutliers">
                  Get outliers
                </button>
                <button class="btn btn-primary btn-wd refresh-button"
                        @click.prevent="csvBasesQualityDataWithoutOutliers">
                  Get data without outliers
                </button>
              </pre>
            </div>
          </div>
        </div>
        <div v-if="this.tabIndex === 5">
          <bases-quality-density :chart-data="this.dataBasesQualityDensity"></bases-quality-density>
          <label class="control-label">
            <p>x - quality</p>
            <p>y - density</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 6">
          <bases-cg-content :chart-data="this.dataBasesCgContent"></bases-cg-content>
          <label class="control-label">
            <p>x - base index in read</p>
            <p>y - CG content</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 7">
          <bases-cg-density :chart-data="this.dataBasesCgDensity"></bases-cg-density>
          <label class="control-label">
            <p>x - CG content</p>
            <p>y - density</p>
          </label>
        </div>
        <div v-if="this.tabIndex === 8">
          <sequences-distribution :sourceData="this.dataSequencesDistribution"
                                  v-if="this.dataSequencesDistribution != null"></sequences-distribution>
          <label class="control-label"></label>
        </div>
        <div v-if="this.tabIndex === 9">
          <duplicated-sequences :sourceData="this.dataDuplicatedSequences"
                                v-if="this.dataDuplicatedSequences != null"></duplicated-sequences>
          <label class="control-label">Duplicated reads</label>
        </div>
        <div v-if="this.tabIndex === 10">
          <reads-info :sourceData="this.dataReadsInfo"
                      v-if="this.dataReadsInfo != null"></reads-info>
        </div>
        <div v-if="this.tabIndex === 11">
          <clustering-panel
            :analysisType="'FastQ'">
          </clustering-panel>
        </div>
        <div v-if="this.tabIndex === 12">
          <outliers-detection
            :analysisType="'FastQ'">
          </outliers-detection>
        </div>
        <div v-if="this.tabIndex === 13">
          <d2-detection
            :analysisType="'FastQ'">
          </d2-detection>
        </div>
      </div>
    </div>
    <quality-status v-if="this.tabIndex < 11"
                    :qualityStatus="this.basesQualityStatus">
    </quality-status>
  </div>
</template>

<script>
  import NucleotidesCounts from 'src/components/Charts/Ioniser/fastq/NucleotidesCounts.vue'
  import BasesCalls from 'src/components/Charts/Ioniser/fastq/BasesCalls.vue'
  import BasesCgContent from 'src/components/Charts/Ioniser/fastq/BasesCgContent.vue'
  import BasesCgDensity from 'src/components/Charts/Ioniser/fastq/BasesCgDensity.vue'
  import BasesQuality from 'src/components/Charts/Ioniser/fastq/BasesQuality.vue'
  import BasesQualityDensity from 'src/components/Charts/Ioniser/fastq/BasesQualityDensity.vue'
  import ReadsQuality from 'src/components/Charts/Ioniser/fastq/ReadsQuality.vue'
  import ReadsQualityDensity from 'src/components/Charts/Ioniser/fastq/ReadsQualityDensity.vue'
  import SequencesDistribution from 'src/components/Stats/SequencesDistribution.vue'
  import DuplicatedSequences from 'src/components/Stats/DuplicatedSequences.vue'
  import QualityStatus from 'src/components/Parts/QualityStatus.vue'
  import ClusteringPanel from 'src/components/Parts/ClusteringPanel.vue'
  import ReadsInfo from 'src/components/Stats/ReadsInfo.vue'
  import OutliersDetection from 'src/components/Parts/OutliersDetectionPanel.vue'
  import D2Detection from 'src/components/Parts/D2DetectionPanel.vue'
  import CsvService from 'src/components/Services/CsvService.vue'
  import PoretoolsReportService from 'src/components/Services/PoretoolsReportService.vue'
  import StatsCard from '../../UIComponents/Cards/StatsCard.vue'

  export default {
    components: {
      StatsCard,
      NucleotidesCounts,
      BasesCalls,
      BasesCgContent,
      BasesCgDensity,
      BasesQuality,
      BasesQualityDensity,
      ReadsQuality,
      ReadsQualityDensity,
      SequencesDistribution,
      DuplicatedSequences,
      QualityStatus,
      ReadsInfo,
      ClusteringPanel,
      OutliersDetection,
      D2Detection,
      CsvService,
      PoretoolsReportService
    },
    props: [
      'id',
      'qualityStatus'
    ],
    data () {
      return {
        dataNucleotidesCounts: null,
        dataBasesCalls: null,
        dataBasesQuality: null,
        dataBasesQualityDensity: null,
        dataBasesCgContent: null,
        dataBasesCgDensity: null,
        dataReadsQuality: null,
        dataReadsQualityDensity: null,
        dataSequencesDistribution: null,
        dataDuplicatedSequences: null,
        dataBasesQualityOutliers: null,
        dataReadsQualityOutliers: null,
        dataReadsInfo: null,
        basesQualityStatus: null,
        clusteringType: null,

        tabIndex: 0
      }
    },
    watch: {
      id: function (newVal, oldVal) {
        if (oldVal !== newVal && newVal > 0) {
          this.getAllData()
        }
      },
      qualityStatus: function (newVal, oldVal) {
        this.basesQualityStatus = newVal
      }
    },
    mounted () {
      this.getAllData()
      if (this.basesQualityStatus == null) {
        this.getBasesQualityStatus()
      }
    },
    methods: {
      handleTabChange (tabIndex, newTab, oldTab) {
        this.tabIndex = tabIndex
      },
      getNucleotidesCounts () {
        this.$http.get(`api/analysis/stats/nucleotidesCounts`, {
          params: {
            valuesNames: ['counts', 'A', 'G', 'C', 'T', 'N']
          }
        }).then(response => {
          this.dataNucleotidesCounts = {
            labels: [],
            datasets: [
              {
                label: 'A',
                backgroundColor: '#f87979',
                data: response.data.values['A']
              },
              {
                label: 'G',
                backgroundColor: '#f8ac5f',
                data: response.data.values['G']
              },
              {
                label: 'C',
                backgroundColor: '#47f889',
                data: response.data.values['C']
              },
              {
                label: 'T',
                backgroundColor: '#82c3f8',
                data: response.data.values['T']
              },
              {
                label: 'N',
                backgroundColor: '#8f8792',
                data: response.data.values['N']
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getBasesCalls () {
        this.$http.get(`api/analysis/stats/basesCalls`, {
          params: {
            valuesNames: ['id', 'A', 'G', 'C', 'T']
          }
        }).then(response => {
          this.dataBasesCalls = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'A',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['A'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'G',
                borderColor: '#f8ac5f',
                fill: false,
                data: response.data.values['G'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'C',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['C'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'T',
                borderColor: '#82c3f8',
                fill: false,
                data: response.data.values['T'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getBasesQuality () {
        this.$http.get(`api/analysis/stats/basesQuality`, {
          params: {
            valuesNames: ['id', 'quality']
          }
        }).then(response => {
          this.dataBasesQuality = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'Quality',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['quality'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getBasesQualityDensity () {
        this.$http.get(`api/analysis/stats/basesQualityDensity`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          this.dataBasesQualityDensity = {
            labels: response.data.values['quality'],
            datasets: [
              {
                label: '',
                borderColor: '#f8ac5f',
                data: response.data.values['density'],
                pointRadius: 0,
                borderWidth: 2,
                fill: false,
                steppedLine: false
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getBasesCgContent () {
        this.$http.get(`api/analysis/stats/basesCgContent`, {
          params: {
            valuesNames: ['id', 'cgContent']
          }
        }).then(response => {
          this.dataBasesCgContent = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'CG content',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['cgContent'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getBasesCgDensity () {
        this.$http.get(`api/analysis/stats/basesCgDensity`, {
          params: {
            valuesNames: ['cgContent', 'density']
          }
        }).then(response => {
          this.dataBasesCgDensity = {
            labels: response.data.values['cgContent'],
            datasets: [
              {
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['density'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getSequencesDistribution () {
        this.$http.get(`api/analysis/stats/sequences-distribution`).then(response => {
          this.dataSequencesDistribution = response.data
        }).catch(() => this.$toast.error())
      },
      getDuplicatedSequences () {
        this.$http.get(`api/analysis/stats/duplicated-sequences`).then(response => {
          this.dataDuplicatedSequences = response.data
        }).catch(() => this.$toast.error())
      },
      getReadsInfo () {
        this.$http.get(`api/analysis/stats/reads-info`).then(response => {
          this.dataReadsInfo = response.data
        }).catch(() => this.$toast.error())
      },
      getReadsQualityDensity () {
        this.$http.get(`api/analysis/stats/readsQualityDensity`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          this.dataReadsQualityDensity = {
            labels: response.data.values['quality'],
            datasets: [
              {
                label: '',
                borderColor: '#f8ac5f',
                data: response.data.values['density'],
                pointRadius: 0,
                borderWidth: 2,
                fill: false,
                steppedLine: false
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },
      getReadsQuality () {
        this.$http.get(`api/analysis/stats/readsQuality`, {
          params: {
            valuesNames: ['id', 'quality', 'mean', 'median']
          }
        }).then(response => {
          this.dataReadsQuality = {
            labels: response.data.values['id'],
            datasets: [
              {
                label: 'Quality',
                borderColor: '#f87979',
                fill: false,
                data: response.data.values['quality'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'Mean',
                borderColor: '#0096f8',
                fill: false,
                data: response.data.values['mean'],
                pointRadius: 0,
                borderWidth: 1
              },
              {
                label: 'Median',
                borderColor: '#47f889',
                fill: false,
                data: response.data.values['median'],
                pointRadius: 0,
                borderWidth: 1
              }
            ]
          }
        }).catch(() => this.$toast.error())
      },

      getAllData () {
        this.getNucleotidesCounts()
        this.getBasesCalls()
        this.getBasesQuality()
        this.getBasesQualityDensity()
        this.getBasesCgContent()
        this.getBasesCgDensity()
        this.getReadsQuality()
        this.getReadsQualityDensity()
        this.getSequencesDistribution()
        this.getDuplicatedSequences()
        this.getReadsQualityOutliers()
        this.getBasesQualityOutliers()
        this.getReadsInfo()
      },

      refreshData () {
        switch (this.tabIndex) {
          case 0:
            this.getNucleotidesCounts()
            break
          case 1:
            this.getReadsQuality()
            this.getReadsQualityOutliers()
            break
          case 2:
            this.getReadsQualityDensity()
            break
          case 3:
            this.getBasesCalls()
            break
          case 4:
            this.getBasesQuality()
            this.getBasesQualityOutliers()
            break
          case 5:
            this.getBasesQualityDensity()
            break
          case 6:
            this.getBasesCgContent()
            break
          case 7:
            this.getBasesCgDensity()
            break
          case 8:
            this.getSequencesDistribution()
            break
          case 9:
            this.getDuplicatedSequences()
            break
          case 10:
            this.getReadsInfo()
            break
        }
      },

      exportCsv () {
        switch (this.tabIndex) {
          case 0:
            CsvService.methods.csvNucleotidesCounts()
            break
          case 1:
            CsvService.methods.csvReadsQuality()
            break
          case 2:
            CsvService.methods.csvReadsQualityDensity()
            break
          case 3:
            CsvService.methods.csvBaseCalls()
            break
          case 4:
            CsvService.methods.csvBasesQuality()
            break
          case 5:
            CsvService.methods.csvBasesQualityDensity()
            break
          case 6:
            CsvService.methods.csvBasesCGContent()
            break
          case 7:
            CsvService.methods.csvBasesCGDensity()
            break
          case 8:
            CsvService.methods.csvSequencesDistribution()
            break
          case 9:
            CsvService.methods.csvDuplicatedSequences()
            break
          case 10:
            CsvService.methods.csvReadsInfo()
            break
        }
      },

      getBasesQualityOutliers () {
        this.$http.get(`api/analysis/stats/basesQualityOutliers`, {
          params: {
            valuesNames: ['outliersCount', 'total', 'proportion', 'outliersMean', 'meanWithOutliers', 'meanWithoutOutliers']
          }
        }).then(response => {
          this.dataBasesQualityOutliers = response.data.values
        }).catch(() => this.$toast.error())
      },
      getReadsQualityOutliers () {
        this.$http.get(`api/analysis/stats/readsQualityOutliers`, {
          params: {
            valuesNames: ['outliersCount', 'total', 'proportion', 'outliersMean', 'meanWithOutliers', 'meanWithoutOutliers']
          }
        }).then(response => {
          this.dataReadsQualityOutliers = response.data.values
        }).catch(() => this.$toast.error())
      },

      getBasesQualityStatus () {
        this.$http.get(`api/analysis/stats/basesQualityStatus`, {
          params: {
            valuesNames: ['status']
          }
        }).then(response => {
          this.basesQualityStatus = response.data.values['status'][0]
        }).catch(() => this.$toast.error())
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
