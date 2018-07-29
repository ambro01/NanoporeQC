<template>
  <div class="card">
    <div class="content">
      <form>
        <div v-if="this.qualityStatus === 'Success'" class="row" style="margin-left: 20px">
          <h3>Quality status: <strong style="color: #229863">{{this.qualityStatus}}</strong></h3>
          <p class="text-success">
            First quartile of 2D reads is grater than 10 and quality median is grater than 25
          </p>
        </div>
        <div v-if="this.qualityStatus === 'Warning'" class="row" style="margin-left: 20px">
          <h3>Quality status: <strong style="color: #BB992F">{{this.qualityStatus}}</strong></h3>
          <p class="text-warning">
            First quartile of 2D reads quality is less than 5 or median is less than 15
          </p>
        </div>
        <div v-if="this.qualityStatus === 'Failure'" class="row" style="margin-left: 20px">
          <h3>Quality status: <strong style="color: #B33C12">{{this.qualityStatus}}</strong></h3>
          <p class="text-danger">
            First quartile of 2D reads quality is less than 3 or median is less than 10
          </p>
        </div>
      </form>
    </div>
  </div>
</template>
<script>
  export default {
    props: [
      'qualityStatus'
    ],
    data () {
      return {}
    }
  }
</script>
<style lang="scss">
</style>
