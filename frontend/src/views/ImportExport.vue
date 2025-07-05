<template>
  <el-card>
    <el-upload
      class="upload-demo"
      action="/api/books/import"
      :headers="{ Authorization: 'Bearer ' + token }"
      :show-file-list="false"
      :on-success="handleSuccess"
      accept=".xlsx"
    >
      <el-button type="primary">批量导入图书（Excel）</el-button>
    </el-upload>
    <el-button type="success" @click="handleExport" style="margin-left: 10px;">导出图书数据</el-button>
  </el-card>
</template>
<script setup>
import { ElMessage } from 'element-plus';
import { exportBooks } from '@/api/book.js'; // 导入新的API方法

const token = localStorage.getItem('token');

function handleSuccess(res) {
  if (res.code === 200) {
    ElMessage.success('图书导入成功！正在刷新列表...');
    setTimeout(() => {
        location.reload();
    }, 1500); // 延迟1.5秒以便用户看到提示
  } else {
    ElMessage.error(res.msg || '导入失败！');
  }
}

async function handleExport() {
  try {
    const res = await exportBooks();
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = 'books.xlsx';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(link.href);
    ElMessage.success('导出任务已开始');
  } catch (error) {
    ElMessage.error('导出失败，请检查网络或联系管理员');
    console.error('Export failed:', error);
  }
}
</script>

<style scoped>
.upload-demo {
  display: inline-block;
}
</style>