<script>
  import axios from '../../vue-axios/axios'
  export default {
    methods: {
      downloadHtmlReport () {
        axios.get(`api/analysis/download-current-report`).then(response => {
          const blob = new Blob([response.data], {type: 'text/html'})
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = 'fastqc_report.html'
          link.click()
        }).catch(() => this.$toast.error())
      }
    }
  }
</script>
