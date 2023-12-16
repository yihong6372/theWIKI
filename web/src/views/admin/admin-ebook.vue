<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '20px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>


          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
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

        <template v-slot:category="{ text, record }">
          <span>{{ getCategoryName(record.category1Id)}} / {{ getCategoryName(record.category2Id)}}</span>
        </template>

        <template v-slot:action="{ text, record }">
          <a-space warp>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>

            <a-popconfirm
                title="是否删除"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)">
              <a-button type="primary" danger>
                删除
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="电子书表单"
      v-model:open="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk">
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name"/>
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader v-model:value="categoryIds" :options="level1" :field-names="{ label: 'name', value: 'id', children: 'children'}" placeholder="Please select" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea"/>
      </a-form-item>
    </a-form>

  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";

import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool"

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const param = ref();
    param.value = {};
    const ebooks = ref([]);
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    console.log("onsetup...", ebooks)
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: {customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '分类',
        slots: {customRender: 'category'}
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
        slots: {customRender: 'action'}
      },
    ];


    const level1 = ref();
    let categorys: any;

    /**
     * 分类数据查询
     */
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        categorys = data.data;
        if (data.status == 0) {
          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.msg);
        }

      });
    };

    const getCategoryName = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          result = item.name;
        }
      });
      return result;
    }

    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.status == 0) {
          ebooks.value = data.data.list;
          //重置分页
          pagination.value.current = params.page;
          pagination.value.total = data.data.total;
        } else {
          message.error(data.msg);
        }

      });
    };

    /**param
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("分页参数有：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    /**
     * 表单
     */
    const categoryIds = ref();
    const ebook = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      console.log('categoryIds=>', categoryIds.value);
      console.log('categoryIds=>', categoryIds);
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          //重新加载
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          alert(data.msg);
        }
        modalVisible.value = false;
        modalLoading.value = false;
      })
    }


    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
      // ebook.value = record;
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          //成功
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }

      });
    }

    onMounted(() => {
      handleQueryCategory();
    });

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      param,
      level1,
      categoryIds,

      modalVisible,
      modalLoading,
      ebook,
      handleModalOk,
      getCategoryName,

      handleQuery,
      edit,
      add,
      handleDelete,
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>