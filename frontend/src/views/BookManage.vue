<template>
  <el-card>
    <!-- 搜索与新增区域 -->
    <el-row :gutter="20" class="search-row">
      <el-col :span="8">
        <el-input v-model="searchParams.title" placeholder="按书名搜索" clearable @clear="fetchData" />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="fetchData">搜索</el-button>
      </el-col>
      <el-col :span="12" style="text-align: right;">
        <el-button type="primary" @click="openDialog()">新增图书</el-button>
      </el-col>
    </el-row>

    <!-- 图书表格 -->
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="100" />
      <el-table-column prop="isbn" label="ISBN" />
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="categoryId" label="分类">
        <template #default="scope">
          {{ getCategoryName(scope.row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" />
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="40%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="ISBN">
          <el-input v-model="form.isbn" />
        </el-form-item>
        <el-form-item label="书名">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="form.author" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
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
import { getBookList, addBook, updateBook, deleteBook, getCategoryList } from '@/api/book';
import { ElMessage, ElMessageBox } from 'element-plus';

const tableData = ref([]);
const total = ref(0);
const searchParams = ref({
  pageNum: 1,
  pageSize: 10,
  title: '',
});
const dialogVisible = ref(false);
const form = ref({});
const isEdit = ref(false);
const categories = ref([]);

const dialogTitle = computed(() => (isEdit.value ? '编辑图书' : '新增图书'));

const fetchData = async () => {
  try {
    const res = await getBookList(searchParams.value);
    tableData.value = res.data.records;
    total.value = res.data.total;
  } catch (error) {
    console.error('获取图书列表失败:', error);
  }
};

const openDialog = (row) => {
  isEdit.value = !!row;
  form.value = row ? { ...row } : { stock: 1 };
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateBook(form.value);
      ElMessage.success('更新成功');
    } else {
      await addBook(form.value);
      ElMessage.success('新增成功');
    }
    dialogVisible.value = false;
    fetchData();
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '新增失败');
  }
};

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除此图书吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteBook(id);
      ElMessage.success('删除成功');
      fetchData();
    } catch (error) {
      ElMessage.error('删除失败');
    }
  });
};

// 获取分类数据
const fetchCategories = async () => {
  try {
    const res = await getCategoryList();
    categories.value = res.data;
  } catch (error) {
    console.error('获取分类列表失败:', error);
  }
};

// 根据 ID 获取分类名
const getCategoryName = (categoryId) => {
  const category = categories.value.find(c => c.id === categoryId);
  return category ? category.name : '未分类';
};

const statusText = (status) => {
    switch(status) {
        case '0': return '已借阅';
        case '1': return '已归还';
        case '2': return '已逾期';
        default: return '未知';
    }
};

const statusTag = (status) => {
    switch(status) {
        case '0': return 'warning';
        case '1': return 'success';
        case '2': return 'danger';
        default: return 'info';
    }
};

onMounted(() => {
  fetchData();
  fetchCategories();
});
</script>

<style scoped>
.search-row {
  margin-bottom: 20px;
}
</style> 