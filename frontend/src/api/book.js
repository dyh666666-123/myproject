import request from '@/utils/request'

const BASE_URL = 'http://localhost:8081/api';

// 获取图书列表（分页）
export function getBookList(params) {
  return request({
    url: '/book/list',
    method: 'get',
    params
  })
}

// 新增图书
export function addBook(data) {
  return request({
    url: '/book/add',
    method: 'post',
    data
  })
}

// 更新图书
export function updateBook(data) {
  return request({
    url: '/book/update',
    method: 'put',
    data
  })
}

// 删除图书
export function deleteBook(id) {
  return request({
    url: `/book/${id}`,
    method: 'delete'
  })
}

// 获取图书分类列表
export function getCategoryList() {
  return request({
    url: '/book-category/list',
    method: 'get'
  })
}

export function exportBooks() {
  return request({
    url: '/books/export',
    method: 'get',
    responseType: 'blob' // 告诉axios期望一个二进制响应
  })
} 