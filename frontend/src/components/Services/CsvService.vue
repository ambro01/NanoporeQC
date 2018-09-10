<script>
  import axios from '../../vue-axios/axios'
  export default {
    methods: {
      //  ############ common ############
      csvSendRequest (response, fileName) {
        const blob = new Blob([response.data], {type: 'text/csv'})
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = fileName + '.csv'
        link.click()
      },

      //  ############ Clustering ############
      csvClusteringReads (clusteringAlgorithm, clustersNumber) {
        axios.get(`api/csv/clustering/` + clusteringAlgorithm, {
          params: {
            clustersNumber: clustersNumber
          }
        }).then(response => this.csvSendRequest(response, 'clustering_reads'))
          .catch(() => this.$toast.error())
      },

      //  ############ 2D detection ############
      csvD2Detection () {
        axios.get(`api/csv/d2-detection`).then(response => this.csvSendRequest(response, '2d-detection'))
          .catch(() => this.$toast.error())
      },

      //  ############ Outliers detection ############
      csvOutliersDetection () {
        axios.get(`api/csv/outliers-detection`).then(response => this.csvSendRequest(response, 'outliers-detection'))
          .catch(() => this.$toast.error())
      },

      //  ############ FastQReport ############
      csvNucleotidesCounts () {
        axios.get(`api/csv/nucleotidesCounts`, {
          params: {
            valuesNames: ['counts', 'A', 'G', 'C', 'T', 'N']
          }
        }).then(response => {
          this.csvSendRequest(response, 'nucleotides_counts')
        }).catch(() => this.$toast.error())
      },
      csvBaseCalls () {
        axios.get(`api/csv/basesCalls`, {
          params: {
            valuesNames: ['id', 'A', 'G', 'C', 'T']
          }
        }).then(response => {
          this.csvSendRequest(response, 'bases_calls')
        }).catch(() => this.$toast.error())
      },
      csvBasesQuality () {
        axios.get(`api/csv/basesQuality`, {
          params: {
            valuesNames: ['id', 'quality', 'mean', 'median', 'q25', 'q50', 'q75']
          }
        }).then(response => {
          this.csvSendRequest(response, 'bases_quality')
        }).catch(() => this.$toast.error())
      },
      csvBasesQualityDensity () {
        axios.get(`api/csv/basesQualityDensity`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          this.csvSendRequest(response, 'bases_quality_density')
        }).catch(() => this.$toast.error())
      },
      csvBasesCGContent () {
        axios.get(`api/csv/basesCgContent`, {
          params: {
            valuesNames: ['id', 'cgContent']
          }
        }).then(response => {
          this.csvSendRequest(response, 'bases_cg_content')
        }).catch(() => this.$toast.error())
      },
      csvBasesCGDensity () {
        axios.get(`api/csv/basesCgDensity`, {
          params: {
            valuesNames: ['cgContent', 'density']
          }
        }).then(response => {
          this.csvSendRequest(response, 'bases_cg_density')
        }).catch(() => this.$toast.error())
      },
      csvSequencesDistribution () {
        axios.get(`api/csv/sequences-distribution`, {
          params: {
            valuesNames: ['sequence', 'count']
          }
        }).then(response => {
          this.csvSendRequest(response, 'sequences_distribution')
        }).catch(() => this.$toast.error())
      },
      csvDuplicatedSequences () {
        axios.get(`api/csv/duplicated-sequences`, {
          params: {
            valuesNames: ['sequence', 'count']
          }
        }).then(response => {
          this.csvSendRequest(response, 'duplicated_sequences')
        }).catch(() => this.$toast.error())
      },
      csvReadsInfo () {
        axios.get(`api/csv/reads-info`, {
          params: {
            valuesNames: ['id', 'name', 'mean', 'median', 'q25', 'q75', 'outliersRatio', 'count']
          }
        }).then(response => {
          this.csvSendRequest(response, 'reads_info')
        }).catch(() => this.$toast.error())
      },
      csvReadsQuality () {
        axios.get(`api/csv/readsQuality`, {
          params: {
            valuesNames: ['id', 'quality', 'mean', 'median', 'q25', 'q50', 'q75']
          }
        }).then(response => {
          this.csvSendRequest(response, 'reads_quality')
        }).catch(() => this.$toast.error())
      },
      csvReadsQualityDensity () {
        axios.get(`api/csv/readsQualityDensity`, {
          params: {
            valuesNames: ['quality', 'density']
          }
        }).then(response => {
          this.csvSendRequest(response, 'reads_quality_density')
        }).catch(() => this.$toast.error())
      },
      csvBasesQualityOutliers () {
        axios.get(`api/csv/readsQualityOutliers`, {
          params: {
            valuesNames: ['outliersId', 'outliersValues']
          }
        }).then(response => {
          this.csvSendRequest(response, 'reads_quality_outliers')
        }).catch(() => this.$toast.error())
      },
      csvBasesQualityDataWithoutOutliers () {
        axios.get(`api/csv/readsQualityOutliers`, {
          params: {
            valuesNames: ['notOutliersId', 'notOutliersValues']
          }
        }).then(response => {
          this.csvSendRequest(response, 'reads_quality_without_outliers')
        }).catch(() => this.$toast.error())
      },
      csvReadsQualityOutliers () {
        axios.get(`api/csv/readsQualityOutliers`, {
          params: {
            valuesNames: ['outliersId', 'outliersValues']
          }
        }).then(response => {
          this.csvSendRequest(response, 'reads_quality_outliers')
        }).catch(() => this.$toast.error())
      },
      csvReadsQualityDataWithoutOutliers () {
        axios.get(`api/csv/readsQualityOutliers`, {
          params: {
            valuesNames: ['notOutliersId', 'notOutliersValues']
          }
        }).then(response => {
          this.csvSendRequest(response, 'reads_quality_without_outliers')
        }).catch(() => this.$toast.error())
      }
    },

    //  ############ Fast5Report ############

    csvSummaryInfo () {
      axios.get(`api/csv/info`)
        .then(response => this.csvSendRequest(response, 'summary_info'))
        .catch(() => this.$toast.error())
    },
    csvReadsAccumulation () {
      axios.get(`api/csv/readsAccumulation`, {
        params: {
          valuesNames: ['minute', 'accumulation']
        }
      }).then(response => this.csvSendRequest(response, 'reads_accumulation'))
        .catch(() => this.$toast.error())
    },
    csvActiveChannels () {
      axios.get(`api/csv/activeChannels`, {
        params: {
          valuesNames: ['minute', 'channels']
        }
      }).then(response => this.csvSendRequest(response, 'active_channels'))
        .catch(() => this.$toast.error())
    },
    csvReadsCategoryCounts () {
      axios.get(`api/csv/readsCategoryCounts`, {
        params: {
          valuesNames: ['category', 'files_count', 'template_count', 'complement_count', 'full_2d_count']
        }
      }).then(response => this.csvSendRequest(response, 'read_category_counts'))
        .catch(() => this.$toast.error())
    },
    csvReadsCategoryQuality () {
      axios.get(`api/csv/readsCategoryQuality`, {
        params: {
          valuesNames: ['category', 'min', 'max', 'mean', 'median']
        }
      }).then(response => this.csvSendRequest(response, 'read_category_quality'))
        .catch(() => this.$toast.error())
    },
    csvEventsCounts () {
      axios.get(`api/csv/eventsCounts`, {
        params: {
          valuesNames: ['time', 'events']
        }
      }).then(response => this.csvSendRequest(response, 'events_counts'))
        .catch(() => this.$toast.error())
    },
    csvReadsQualityMulti () {
      axios.get(`api/csv/readsQualityMulti`, {
        params: {
          valuesNames: ['id', 'quality', 'min_', 'max_', 'mean_', 'median_']
        }
      }).then(response => this.csvSendRequest(response, 'reads_quality'))
        .catch(() => this.$toast.error())
    },
    csvReadsQualityDensityMulti () {
      axios.get(`api/csv/readsQualityDensityMulti`, {
        params: {
          valuesNames: ['quality_template', 'quality_complement', 'quality_2D',
            'density_template', 'density_complement', 'density_2D']
        }
      }).then(response => this.csvSendRequest(response, 'read_quality_density'))
        .catch(() => this.$toast.error())
    },
    csvReadsPerChannel () {
      axios.get(`api/csv/readsPerChannel`, {
        params: {
          valuesNames: ['channel', 'reads']
        }
      }).then(response => this.csvSendRequest(response, 'reads_per_channel'))
        .catch(() => this.$toast.error())
    },
    csvKbPerChannel () {
      axios.get(`api/csv/kbPerChannel`, {
        params: {
          valuesNames: ['channel', 'kb']
        }
      }).then(response => this.csvSendRequest(response, 'kb_per_channel'))
        .catch(() => this.$toast.error())
    },
    csvReadsQualityOutliers (type) {
      axios.get(`api/csv/reads` + type + `QualityOutliers`, {
        params: {
          valuesNames: ['outliersId', 'outliersValues']
        }
      }).then(response => this.csvSendRequest(response, 'reads_quality_outliers'))
        .catch(() => this.$toast.error())
    },
    csvReadsQualityDataWithoutOutliers (type) {
      axios.get(`api/csv/reads` + type + `QualityOutliers`, {
        params: {
          valuesNames: ['notOutliersId', 'notOutliersValues']
        }
      }).then(response => this.csvSendRequest(response, 'reads_quality_without_outliers'))
        .catch(() => this.$toast.error())
    }
  }
</script>
