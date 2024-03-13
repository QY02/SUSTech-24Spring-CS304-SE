<template>
  <t-layout>
    <t-header>
      <t-head-menu value="item1" height="120px">
        <template #logo>
          <img width="136" class="logo" src="https://www.tencent.com/img/index/menu_logo_hover.png" alt="logo" />
        </template>
        <t-menu-item value="item1"> 已选内容 </t-menu-item>
        <t-menu-item value="item2"> 菜单内容一 </t-menu-item>
        <t-menu-item value="item3"> 菜单内容二 </t-menu-item>
        <t-menu-item value="item4" :disabled="true"> 菜单内容三 </t-menu-item>
        <template #operations>
          <a href="javascript:;"><t-icon class="t-menu__operations-icon" name="search" /></a>
          <a href="javascript:;"><t-icon class="t-menu__operations-icon" name="notification-filled" /></a>
          <a href="javascript:;"><t-icon class="t-menu__operations-icon" name="home" /></a>
        </template>
      </t-head-menu>
    </t-header>
    <t-layout>
      <t-aside style="border-top: 1px solid var(--component-border)">
        <t-menu theme="light" value="dashboard" style="margin-right: 50px" height="550px">
          <t-menu-item value="dashboard">
            <template #icon>
              <t-icon name="dashboard" />
            </template>
            仪表盘
          </t-menu-item>
          <t-menu-item value="resource">
            <template #icon>
              <t-icon name="server" />
            </template>
            资源列表
          </t-menu-item>
          <t-menu-item value="root">
            <template #icon>
              <t-icon name="root-list" />
            </template>
            根目录
          </t-menu-item>
          <t-menu-item value="control-platform">
            <template #icon>
              <t-icon name="control-platform" />
            </template>
            调度平台
          </t-menu-item>
          <t-menu-item value="precise-monitor">
            <template #icon>
              <t-icon name="precise-monitor" />
            </template>
            调度平台
          </t-menu-item>
          <t-menu-item value="mail">
            <template #icon>
              <t-icon name="mail" />
            </template>
            消息区
          </t-menu-item>
          <t-menu-item value="user-circle">
            <template #icon>
              <t-icon name="user-circle" />
            </template>
            个人中心
          </t-menu-item>
          <t-menu-item value="play-circle">
            <template #icon>
              <t-icon name="play-circle" />
            </template>
            视频区
          </t-menu-item>
          <t-menu-item value="edit1">
            <template #icon>
              <t-icon name="edit-1" />
            </template>
            资源编辑
          </t-menu-item>
        </t-menu>
      </t-aside>
      <t-layout>
        <t-content>
          <div>Content</div>
        </t-content>
        <t-footer>Copyright @ 2019-{{ new Date().getFullYear() }} Tencent. All Rights Reserved</t-footer>
      </t-layout>
    </t-layout>
  </t-layout>
  <t-button>确定</t-button>

  <t-space>
    <t-button theme="default">填充按钮</t-button>
    <t-button variant="outline" theme="default">描边按钮</t-button>
    <t-button variant="dashed" theme="default">虚框按钮</t-button>
    <t-button variant="text" theme="default">文字按钮</t-button>
  </t-space>

</template>


<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
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
</style>
