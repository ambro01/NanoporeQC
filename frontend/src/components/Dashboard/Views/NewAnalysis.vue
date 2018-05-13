<template>
  <div>
    <div class="row" v-if="!this.isFast5 && !this.isFastQ">
      <div class="col-md-3">
      </div>
      <div class="col-md-6">
        <div class="card">
          <div class="header">
            <h3 class="tittle" align="center">Which analysis do you want to perform?</h3>
          </div>
          <div class="content">
            <form>
              <div class="col-md-1"></div>
              <div class="col-md-4">
                <div class="text-center">
                  <button class="my-select-button btn btn-lg btn-block" @click.prevent="showFast5">
                    Fast5
                  </button>
                </div>
              </div>
              <div class="col-md-2"></div>
              <div class="col-md-4">
                <div class="text-center">
                  <button class="btn btn-lg" @click.prevent="showFastQ">
                    FastQ
                  </button>
                </div>
              </div>
              <div class="clearfix"></div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <new-fast5-analysis v-if="this.isFast5"></new-fast5-analysis>
    <new-fast-q-analysis v-if="this.isFastQ"></new-fast-q-analysis>
  </div>
</template>

<script>
  import NewFast5Analysis from 'src/components/Dashboard/Views/NewFast5Analysis.vue'
  import NewFastQAnalysis from 'src/components/Dashboard/Views/NewFastQAnalysis.vue'

  export default {
    components: {
      NewFast5Analysis,
      NewFastQAnalysis
    },
    data () {
      return {
        isFast5: false,
        isFastQ: false
      }
    },
    methods: {
      showFast5 () {
        this.isFast5 = true
      },
      showFastQ () {
        this.isFastQ = true
      }
    }
  }
</script>

<style lang="css" scoped>
  .btn.btn-lg {
    padding: 30px 100px;
  }
</style>
