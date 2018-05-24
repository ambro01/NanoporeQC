<template>
  <div :class="{'nav-open': $sidebar.showSidebar}">
    <router-view></router-view>
    <!--This sidebar appears only for screens smaller than 992px-->
    <side-bar type="navbar" :sidebar-links="$sidebar.sidebarLinks">
      <ul class="nav navbar-nav">
        <li class="divider"></li>
      </ul>
    </side-bar>
  </div>
</template>

<script>
  export default {}
</script>

<style lang="scss">
  @import "assets/sass/paper-dashboard";
  @import "assets/sass/filemanagement";
  @import "assets/sass/v-table-client.scss";
</style>
