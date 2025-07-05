<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <el-scrollbar>
        <el-menu :default-openeds="['1']" router>
          <div class="logo-title">图书管理系统</div>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><message /></el-icon>核心功能
            </template>
            <el-menu-item-group>
              <el-menu-item index="/users">用户管理</el-menu-item>
              <el-menu-item index="/books">图书管理</el-menu-item>
              <el-menu-item index="/categories">分类管理</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><setting /></el-icon>高级功能
            </template>
            <el-menu-item-group>
              <el-menu-item index="/reports">统计报表</el-menu-item>
              <el-menu-item index="/import-export">导入导出</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div class="toolbar">
          <el-dropdown>
            <el-icon style="margin-right: 8px; margin-top: 1px"
              ><setting
            /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <span>Admin</span>
        </div>
      </el-header>

      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { Message, Setting } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const logout = () => {
  localStorage.removeItem('token');
  router.push('/login');
};
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.logo-title {
  text-align: center;
  padding: 20px;
  font-size: 1.2em;
  font-weight: bold;
}
.layout-header {
  text-align: right;
  font-size: 12px;
  background-color: #f5f6fa;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
.el-aside {
  color: var(--el-text-color-primary);
  background: #fff !important;
  border-right: solid 1px #e6e6e6;
  box-sizing: border-box;
}
.el-menu {
  border-right: none;
}
.toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style> 