<template>
  <div class="register-container">
    <el-card class="register-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" class="register-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleRegister" style="width:100%;">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="goLogin" style="width:100%;">返回登录</el-button>
        </el-form-item>
        <div v-if="errorMsg" style="color:red;">{{ errorMsg }}</div>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/user'
import { ElMessage } from 'element-plus'

const form = ref({ username: '', password: '', confirmPassword: '' })
const formRef = ref()
const router = useRouter()
const loading = ref(false)
const errorMsg = ref('')

const rules = {
  username: [ { required: true, message: '请输入用户名', trigger: 'blur' } ],
  password: [ { required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' } ],
  confirmPassword: [ { required: true, message: '请确认密码', trigger: 'blur' }, { validator: validateConfirm, trigger: 'blur' } ]
}
function validateConfirm(rule, value, callback) {
  if (value !== form.value.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

async function handleRegister() {
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    errorMsg.value = ''
    try {
      const res = await register({ username: form.value.username, password: form.value.password })
      if (res.data.code === 200) {
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } else {
        errorMsg.value = res.data.msg || '注册失败'
      }
    } catch (e) {
      errorMsg.value = '网络错误'
    }
    loading.value = false
  })
}
function goLogin() {
  router.push('/login')
}
</script>
<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f6fa;
}
.register-card {
  width: 400px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}
.register-form {
  margin-top: 20px;
}
</style> 