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
            <a-button type="primary" @click="handleQuery()">
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
          :data-source="level1"
          :pagination="false"
          :loading="loading"
          @change="handleTableChange">

        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar">
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
      title="分类表单"
      v-model:open="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk">
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent"/>
        <a-select
            ref="select"
            v-model:value="category.parent"
            @focus="focus"
            @change="handleChange"
        >
          <a-select-option value="0">无</a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id == c.id"  >{{c.name}}</a-select-option>
          <a-select-option value="disabled" disabled>Disabled</a-select-option>
        </a-select>
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
  name: 'AdminCategory',
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref([]);
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    console.log("onsetup...", categorys)
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '顺序',
        dataIndex: 'sort',
      },
      {
        title: '父分类',
        dataIndex: 'parent',
      },
      {
        title: 'action',
        dataIndex: 'action',
        slots: {customRender: 'action'}
      },
    ];

    const level1 = ref();

    /**
     * 数据查询
     */
    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.status == 0) {
          categorys.value = data.data;

          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value, 0);
          console.log('树形',level1.value)
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
      handleQuery();
    };

    /**
     * 表单
     */
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    console.log('category----', category);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/category/save", category.value).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          //重新加载
          handleQuery();
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
      console.log("on___edit-----", record);
      modalVisible.value = true;
      category.value = Tool.copy(record);
    };

    /**
     * 新增
     */
    const add = () => {
      console.log("on___add-----");
      modalVisible.value = true;
      category.value = {};
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          //成功
          handleQuery();
        }

      });
    }

    onMounted(() => {
      handleQuery();
    });

    return {
      // categorys,
      level1,
      pagination,
      columns,
      loading,
      handleTableChange,
      param,

      modalVisible,
      modalLoading,
      category,
      handleModalOk,

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