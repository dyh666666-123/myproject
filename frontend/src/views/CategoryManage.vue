<template>
  <el-card>
    <el-button type="primary" @click="openDialog()" style="margin-bottom: 20px;">Add Category</el-button>

    <el-table :data="tableData" row-key="id" stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="180" />
      <el-table-column prop="name" label="Category Name" />
      <el-table-column prop="parentId" label="Parent Category ID" />
      <el-table-column label="Actions" width="180">
        <template #default="scope">
          <el-button size="small" @click="openDialog(scope.row)">Edit</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="30%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="Category Name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Parent Category">
          <el-select v-model="form.parentId" placeholder="Select Parent Category (Optional)" clearable>
            <el-option
              v-for="category in tableData"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit">Confirm</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/bookCategory';
import { ElMessage, ElMessageBox } from 'element-plus';

const tableData = ref([]);
const dialogVisible = ref(false);
const form = ref({});
const isEdit = ref(false);

const dialogTitle = computed(() => (isEdit.value ? 'Edit Category' : 'Add Category'));

const fetchData = async () => {
  try {
    const res = await getCategoryList();
    if (res.data.code === 200) {
      tableData.value = res.data.data;
    }
  } catch (error) {
    ElMessage.error('Failed to fetch category list');
  }
};

const openDialog = (row) => {
  isEdit.value = !!row;
  form.value = row ? { ...row } : {};
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateCategory(form.value);
      ElMessage.success('Update successful');
    } else {
      await addCategory(form.value);
      ElMessage.success('Add successful');
    }
    dialogVisible.value = false;
    fetchData();
  } catch (error) {
    ElMessage.error(isEdit.value ? 'Update failed' : 'Add failed');
  }
};

const handleDelete = (id) => {
  ElMessageBox.confirm('Are you sure you want to delete this category? This may affect related books.', 'Warning', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteCategory(id);
      ElMessage.success('Delete successful');
      fetchData();
    } catch (error) {
      ElMessage.error('Delete failed');
    }
  });
};

onMounted(fetchData);
</script>