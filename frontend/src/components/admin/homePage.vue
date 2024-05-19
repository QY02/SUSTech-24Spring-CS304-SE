<template>
      <div class="card-with-margin">
        <t-row :gutter="[16, 16]">
          <t-col
            v-for="product in productList"
            :key="product.index"
            :lg="3"
            :xs="5"
            :xl="3"
          >
            <product-card
              class="list-card-item"
              :product="product"
              @click="handleClickProduct(product)"
            />
          </t-col>
        </t-row>
      </div>
</template>

<script lang="ts">
export default {
  name: 'ListCard',
};
</script>

<script setup lang="ts">
import { ref } from 'vue';

import type { CardProductType } from './components/product-card.vue';
import ProductCard from './components/product-card.vue';
import router from '@/routers';

const productList = ref([
  {
    index: 1,
    name: '用户管理',
    description: '管理用户的相关信息',
    type: 1,},
  // {
  //   index: 2,
  //   name: '活动管理',
  //   description: '管理活动的相关信息',
  //   type: 2,
  // },
  {
    index: 3,
    name: '活动审核',
    description: '审核活动的相关信息',
    type: 3,},
  {
    index: 4,
    name: '动态审核',
    description: '审核动态内容',
    type: 4,},
  {
    index: 5,
    name: '支付流水',
    description: '查看最近支付流水信息',
    type: 5,},
  {
    index: 6,
    name: '性能监控',
    description: '监控系统性能、资源使用',
    type: 6,},
]);

const handleClickProduct = (product: CardProductType) => {
  if (product.type === 1) {
    router.push('/admin/userManage');
  }
  // else if (product.type === 2) {
  //   router.push('/admin/eventManage');
  // }
  else if (product.type === 3) {
    router.push('/approval');
  } else if (product.type === 4) {
    router.push('/momentAudit');
  } else if (product.type === 5) {
    router.push('/payment');
  } else if (product.type === 6) {
    router.push('/monitor');

  }
};
</script>

<style lang="less" scoped>
.list-card {
  height: 100%;

  &-operation {
    display: flex;
    justify-content: space-between;
    margin-bottom: var(--td-comp-margin-xxl);

    .search-input {
      width: 360px;
    }
  }

  &-item {
    margin: 10px;
    padding: var(--td-comp-paddingTB-xl) var(--td-comp-paddingTB-xl);

    :deep(.t-card__header) {
      padding: 0;
    }

    :deep(.t-card__body) {
      padding: 0;
      margin-top: var(--td-comp-margin-xxl);
      margin-bottom: var(--td-comp-margin-xxl);
    }

    :deep(.t-card__footer) {
      padding: 0;
    }
  }

  &-pagination {
    padding: var(--td-comp-paddingTB-xl) var(--td-comp-paddingTB-xl);
  }

  &-loading {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.card-with-margin {
  margin: 20px;
}
</style>
