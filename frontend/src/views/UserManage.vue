<template>
  <el-card>
    <!-- 搜索区域 -->
    <el-row :gutter="20" class="search-row">
      <el-col :span="8">
        <el-input v-model="searchParams.username" placeholder="按用户名搜索" clearable @clear="fetchData" />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="fetchData">搜索</el-button>
      </el-col>
    </el-row>
    
    <!-- 新增按钮 -->
    <el-button type="primary" @click="openDialog()" style="margin-bottom: 20px;">新增用户</el-button>

    <!-- 用户表格 -->
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="180" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      layout="prev, pager, next, total"
      :total="total"
      :page-size="searchParams.pageSize"
      v-model:current-page="searchParams.pageNum"
      @current-change="fetchData"
      style="margin-top: 20px; justify-content: flex-end;"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="30%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码" v-if="!isEdit">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user';
import { ElMessage, ElMessageBox } from 'element-plus';

// 响应式数据
const tableData = ref([]);
const total = ref(0);
const searchParams = ref({
  pageNum: 1,
  pageSize: 10,
  username: '',
});
const dialogVisible = ref(false);
const form = ref({});
const isEdit = ref(false);

const dialogTitle = computed(() => (isEdit.value ? '编辑用户' : '新增用户'));

// 获取数据
const fetchData = async () => {
  try {
    const res = await getUserList(searchParams.value);
    tableData.value = res.data.records;
    total.value = res.data.total;
  } catch (error) {
    console.error('获取用户列表失败:', error);
  }
};

// 打开对话框
const openDialog = (row) => {
  isEdit.value = !!row;
  if (row) {
    // 编辑状态：复制整行数据
    form.value = { ...row };
  } else {
    // 新增状态：创建一个包含所有字段的空对象
    form.value = { username: '', password: '', email: '', status: 1 };
  }
  dialogVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  console.log('即将提交的表单数据:', JSON.stringify(form.value));
  try {
    if (isEdit.value) {
      await updateUser(form.value);
      ElMessage.success('更新成功');
    } else {
      await addUser(form.value);
      ElMessage.success('新增成功');
    }
    dialogVisible.value = false;
    fetchData();
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '新增失败');
  }
};

// 删除用户
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除此用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteUser(id);
      ElMessage.success('删除成功');
      fetchData();
    } catch (error) {
      ElMessage.error('删除失败');
    }
  });
};

// 组件挂载时获取数据
onMounted(fetchData);
</script>

<style scoped>
.search-row {
  margin-bottom: 20px;
}
</style> 