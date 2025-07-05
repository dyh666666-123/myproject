<template>
  <el-card>
    <h3>借阅量TOP10</h3>
    <div ref="top10Chart" style="height:400px"></div>
    <h3>分类统计</h3>
    <div ref="categoryChart" style="height:400px"></div>
  </el-card>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import * as echarts from 'echarts'
const top10Chart = ref(null)
const categoryChart = ref(null)
onMounted(() => {
  const chart1 = echarts.init(top10Chart.value)
  chart1.setOption({
    xAxis: { type: 'category', data: ['书1','书2'] },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: [10, 8] }]
  })
  const chart2 = echarts.init(categoryChart.value)
  chart2.setOption({
    series: [{ type: 'pie', data: [{ value: 10, name: 'A类' }, { value: 8, name: 'B类' }] }]
  })
})
</script> 