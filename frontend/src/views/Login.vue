<template>
  <div class="login-container">
    <el-card class="login-card">
      <el-form :model="form" class="login-form">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
        <el-form-item class="register-form-item">
          <el-button type="info" plain class="register-btn" @click="goRegister">去注册</el-button>
        </el-form-item>
        <div v-if="errorMsg" style="color:red;">{{ errorMsg }}</div>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/user'
import 'element-plus/dist/index.css'

const form = ref({ username: '', password: '' })
const router = useRouter()
const loading = ref(false)
const errorMsg = ref('')

async function handleLogin() {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await login(form.value)
    // 此时res的结构是 { code: 200, data: "token string", msg: "..." }
    localStorage.setItem('token', res.data) // 正确提取token字符串
    router.push('/') // 跳转到主页，会自动重定向到/users
  } catch (e) {
    // 错误已由响应拦截器统一处理并提示，这里无需额外操作
    console.error(e) // 可以在控制台打印错误供调试
  }
  loading.value = false
}
function goRegister() {
  router.push('/register')
}
</script>
<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f6fa;
}
.login-card {
  width: 400px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}
.login-form {
  margin-top: 20px;
}
.login-btn {
  width: 100%;
  margin-bottom: 32px;
}
.register-form-item {
  margin-top: 16px !important;
}
.register-btn {
  width: 100%;
  color: #409EFF;
  border: 1px solid #409EFF;
  background: #fff;
}
</style> 