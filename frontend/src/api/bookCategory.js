import axios from 'axios';

const BASE_URL = 'http://localhost:8081/api';

// 获取图书分类列表
export function getCategoryList() {
  return axios.get(`${BASE_URL}/book-category/list`);
}

// 新增分类
export function addCategory(data) {
  return axios.post(`${BASE_URL}/book-category/add`, data);
}

// 更新分类
export function updateCategory(data) {
  return axios.put(`${BASE_URL}/book-category/update`, data);
}

// 删除分类
export function deleteCategory(id) {
  return axios.delete(`${BASE_URL}/book-category/${id}`);
} 