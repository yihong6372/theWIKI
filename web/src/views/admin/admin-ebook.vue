<template>

  <a-layout>
    <a-layout-contents
        :style="{ background: '#fff', padding: '20px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange">

        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar">
        </template>

        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary">
              编辑
            </a-button>
            <a-button type="primary" danger>
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-contents>
  </a-layout>
</template>

<script  lang="ts">
  import { defineComponent, onMounted, ref } from "vue";
  import axios from 'axios';

  export default defineComponent({
    name: 'AdminEbook',
    setup() {
      const ebooks = ref();
      const pagination = ref({
        current: 1,
        pageSize: 4,
        total: 0
      });
      const loading = ref(false);

      const columns = [
        {
          title: '封面',
          dataIndex: 'cover',
          slots: { customRender: 'cover'}
        },
        {
          title: '名称',
          dataIndex: 'name',
        },
        {
          title: '分类一',
          dataIndex: 'category1Id',
        },
        {
          title: '分类二',
          dataIndex: 'category2Id',
        },
        {
          title: '文档数',
          dataIndex: 'docCount',
        },
        {
          title: '阅读数',
          dataIndex: 'viewCount',
        },
        {
          title: '点赞数',
          dataIndex: 'voteCount',
        },
        {
          title: 'action',
          dataIndex: 'action',
          slots: { customRender: 'action'}
        },
      ];

      /**
       * 数据查询
       */
      const handleQuery = (params: any) => {
        loading.value = true;
        axios.get("/ebook/list", {
          params: {
            page: params.page,
            size: params.size
          }
        }).then((response) => {
          loading.value = false;
          const data = response.data;
          ebooks.value = data.data.list;

          //重置分页
          pagination.value.current = params.page;
          pagination.value.total = data.data.total;
        });
      };

      /**
       * 表格点击页码时触发
       */
      const  handleTableChange = (pagination: any) => {
        console.log("分页参数有：" + pagination);
        handleQuery({
          page: pagination.current,
          size: pagination.pageSize
        });
      };

      onMounted(() => {
        handleQuery({
          page: 1,
          size: pagination.value.pageSize
        });
      });

      return {
        ebooks,
        pagination,
        columns,
        loading,
        handleTableChange,
      }
    }
  });
</script>