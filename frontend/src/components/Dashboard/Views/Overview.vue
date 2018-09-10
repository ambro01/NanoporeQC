<template>
  <div>
    <div class="row">
      <div class="col-lg-3 col-sm-6" v-for="stats in statsCardsFast5">
        <stats-card>
          <div class="icon-big text-center" :class="`icon-${stats.type}`" slot="header">
            <i :class="stats.icon"></i>
          </div>
          <div class="numbers" slot="content">
            <p>{{stats.title}}</p>
            {{stats.value}}
          </div>
          <div class="stats" slot="footer">
            <i :class="stats.footerIcon"></i> {{stats.footerText}}
          </div>
        </stats-card>
      </div>
    </div>
    <div class="row">
      <div class="col-lg-3 col-sm-6" v-for="stats in statsCardsFastQ">
        <stats-card>
          <div class="icon-big text-center" :class="`icon-${stats.type}`" slot="header">
            <i :class="stats.icon"></i>
          </div>
          <div class="numbers" slot="content">
            <p>{{stats.title}}</p>
            {{stats.value}}
          </div>
          <div class="stats" slot="footer">
            <i :class="stats.footerIcon"></i> {{stats.footerText}}
          </div>
        </stats-card>
      </div>
    </div>
    <div class="main-card">
      <div class="col-md-3">
      </div>
      <div class="col-md-6">
        <div class="card">
          <div class="header">
            <h3 class="tittle" align="center">Which analysis do you want to run?</h3>
          </div>
          <div class="content">
            <br>
            <form>
              <div class="col-md-1"></div>
              <div class="col-md-4">
                <div class="text-center">
                  <button class="btn btn-lg select-button" @click.prevent="goToNewAnalysis">
                    New
                  </button>
                </div>
              </div>
              <div class="col-md-2"></div>
              <div class="col-md-4">
                <div class="text-center">
                  <button class="btn btn-lg select-button" @click.prevent="goToSavedAnalyses">
                    Saved
                  </button>
                </div>
              </div>
              <div class="clearfix"></div>
            </form>
            <br>
            <br>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import StatsCard from 'components/UIComponents/Cards/StatsCard.vue'

  const fast5 = 'Fast5'
  const fastQ = 'FastQ'
  export default {
    components: {
      StatsCard
    },
    /**
     * Chart data used to render stats, charts. Should be replaced with server data
     */
    data () {
      return {
        statsCardsFast5: [
          {
            type: 'info',
            icon: 'ti-archive',
            title: 'Fast5 analyses',
            value: null
          },
          {
            type: 'warning',
            icon: 'ti-pulse',
            title: 'Last analysis run',
            value: null
          },
          {
            type: 'success',
            icon: 'ti-flag-alt',
            title: 'Last success quality analysis run',
            value: null
          },
          {
            type: 'danger',
            icon: 'ti-stats-up',
            title: 'Success quality analyses ratio',
            value: null
          }
        ],
        statsCardsFastQ: [
          {
            type: 'info',
            icon: 'ti-archive',
            title: 'FastQ analyses',
            value: null
          },
          {
            type: 'warning',
            icon: 'ti-pulse',
            title: 'Last analysis run',
            value: null
          },
          {
            type: 'success',
            icon: 'ti-flag-alt',
            title: 'Last success quality analysis run',
            value: null
          },
          {
            type: 'danger',
            icon: 'ti-stats-up',
            title: 'Success quality analyses rate',
            value: null
          }
        ]
      }
    },
    mounted () {
      this.getAnalysesAmount(fast5)
      this.getAnalysesAmount(fastQ)
      this.getLastAnalysisTime(fast5)
      this.getLastAnalysisTime(fastQ)
      this.getLastSuccessAnalysisTime(fast5)
      this.getLastSuccessAnalysisTime(fastQ)
      this.getSuccessAnalysesRatio(fast5)
      this.getSuccessAnalysesRatio(fastQ)
    },
    methods: {
      goToNewAnalysis () {
        this.$router.push('new-analysis')
      },
      goToSavedAnalyses () {
        this.$router.push('my-analyses')
      },
      getAnalysesAmount (type) {
        this.$http.get(`api/analysis/amount/` + type)
          .then(response => {
            if (type === fast5) {
              this.statsCardsFast5[0].value = response.data
            }
            if (type === fastQ) {
              this.statsCardsFastQ[0].value = response.data
            }
          }).catch(() => this.$toast.error())
      },
      getLastAnalysisTime (type) {
        this.$http.get(`api/analysis/last/` + type)
          .then(response => {
            if (type === fast5) {
              this.statsCardsFast5[1].value = response.data
            }
            if (type === fastQ) {
              this.statsCardsFastQ[1].value = response.data
            }
          }).catch(() => this.$toast.error())
      },
      getLastSuccessAnalysisTime (type) {
        this.$http.get(`api/analysis/last-success/` + type)
          .then(response => {
            if (type === fast5) {
              this.statsCardsFast5[2].value = response.data
            }
            if (type === fastQ) {
              this.statsCardsFastQ[2].value = response.data
            }
          }).catch(() => this.$toast.error())
      },
      getSuccessAnalysesRatio (type) {
        this.$http.get(`api/analysis/success-ratio/` + type)
          .then(response => {
            if (type === fast5) {
              this.statsCardsFast5[3].value = response.data + ' %'
            }
            if (type === fastQ) {
              this.statsCardsFastQ[3].value = response.data + ' %'
            }
          }).catch(() => this.$toast.error())
      }
    }
  }

</script>

<style lang="css" scoped>
  .select-button {
    font-size: 30px;
    width: 300px;
    height: 150px;
  }

  .main-card {
    padding-top: 100px;
  }
</style>
