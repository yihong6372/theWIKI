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
      title="文档表单"
      v-model:open="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk">
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-tree-select
            v-model:value="doc.parent"
            show-search
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="Please select"
            allow-clear
            tree-default-expand-all
            :tree-data="treeSelectData"
            :fieldNames="{label: 'name', key: 'id', value: 'id', children: 'children'}"
        >
        </a-tree-select>
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="doc.parent"/>
        <a-select
            ref="select"
            v-model:value="doc.parent"
        >
          <a-select-option value="0">无</a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id == c.id">{{ c.name }}
          </a-select-option>
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
  name: 'AdminDoc',
  setup() {
    const param = ref();
    param.value = {};
    const docs = ref([]);
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    console.log("setup...", docs)
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
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        console.log('response', response);
        if (data.status == 0) {
          docs.value = data.data;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log('树形', level1.value)
        } else {
          message.error(data.msg);
        }

      }).catch((error) => {
        console.error('请求错误', error);
        // 在这里处理错误，比如显示错误信息
        message.error('请求失败: ' + error.message);
      }).finally(() => {
        // 无论请求成功还是失败，都会执行
        loading.value = false;
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
    const treeSelectData = ref();
    treeSelectData.value = [];
    const doc = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    console.log('doc----', doc);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          //重新加载
          handleQuery();
        } else {
          alert(data.msg);
        }
        modalVisible.value = false;
        modalLoading.value = false;
      });
    };

    const setDisable = (treeSelectData: any, id: any) => {
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          console.log('disabled', node);
          node.disabled = true;

          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id);
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children) ) {
            setDisable(children, id);
          }
        }
      }
    };


    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 新增
     */
    const add = () => {
      console.log("on___add-----");
      modalVisible.value = true;
      doc.value = {};

      treeSelectData.value = Tool.copy(level1.value);

      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete("/doc/delete/" + id).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          //成功
          handleQuery();
        }

      });
    };


    onMounted(() => {
      handleQuery();
    });

    return {
      // docs,
      level1,
      pagination,
      columns,
      loading,
      handleTableChange,
      param,

      modalVisible,
      modalLoading,
      doc,
      handleModalOk,

      handleQuery,
      edit,
      add,
      handleDelete,
      treeSelectData
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