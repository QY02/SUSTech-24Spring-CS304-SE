<template>
<!--  <va-navbar color="colors.priamry" class="mb-33">-->
<!--    <template #center>-->
<!--      <va-navbar-item class="logo">-->
<!--        SUSTech Project Helper-->
<!--      </va-navbar-item>-->
<!--    </template>-->
<!--  </va-navbar>-->
  <t-head-menu default-value="2-1" expand-type="popup">
    <template #logo>
      <img height="28" src="https://tdesign.gtimg.com/site/baseLogo-light.png" alt="logo" />
    </template>
    <t-submenu value="1">
      <template #title>
        <span>菜单1</span>
      </template>
      <t-submenu value="1-0" title="子菜单1-1">
        <t-menu-item value="1-1-1"> 子菜单1-1-1 </t-menu-item>
        <t-menu-item value="1-1-3"> 子菜单1-1-3 </t-menu-item>
      </t-submenu>
      <t-menu-item value="1-2"> 子菜单1-2 </t-menu-item>
      <t-menu-item value="1-3"> 子菜单1-3 </t-menu-item>
      <t-menu-item value="1-4"> 子菜单1-4 </t-menu-item>

    </t-submenu>
    <t-submenu value="2" title="菜单2">
      <t-menu-item value="2-1"> 子菜单2-1 </t-menu-item>
      <t-menu-item value="2-2"> 子菜单2-2 </t-menu-item>
      <t-menu-item value="2-3"> 子菜单2-3 </t-menu-item>
    </t-submenu>
    <template #operations>
      <t-button variant="text" shape="square">
        <template #icon><t-icon name="search" /></template>
      </t-button>
      <t-button variant="text" shape="square">
        <template #icon><t-icon name="mail" /></template>
      </t-button>
      <t-button variant="text" shape="square">
        <template #icon><t-icon name="user" /></template>
      </t-button>
      <t-button variant="text" shape="square">
        <template #icon><t-icon name="ellipsis" /></template>
      </t-button>
    </template>
  </t-head-menu>
</template>
<script>
export default {
  name: 'NavBarWithOnlyTitle',
}
</script>
<!--<style scoped>-->
<!--.logo {-->
<!--  font-weight: 600;-->
<!--  font-size: 1.5rem;-->
<!--  color: #FFFFFF;-->
<!--}-->

<!--.mb-33 {-->
<!--  margin-bottom: 30px; /* 通常等于 16px */-->
<!--}-->
<!--</style>-->
<style lang="less" scoped>
.t-menu__operations {
  .t-button {
    margin-left: 8px;
  }
}

.t-demo-menu--dark {
  .t-button {
    color: #fff;
    &:hover {
      background-color: #4b4b4b;
      border-color: transparent;
      --ripple-color: #383838;
    }
  }
}
</style>