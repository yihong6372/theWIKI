<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '20px', margin: 0, minHeight: '280px' }"
    >

      <a-row :gutter="24">
        <a-col :span="8">
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
              v-if="level1.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{record.sort}} {{text}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :fieldNames="{label: 'name', value: 'id', children: 'children'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content">
                <Toolbar
                    style="border-bottom: 1px solid #ccc"
                    :editor="editorRef"
                    :defaultConfig="toolbarConfig"
                    :mode="mode"
                />
                <Editor
                    style="height: 500px; overflow-y: hidden;"
                    v-model="valueHtml"
                    :defaultConfig="editorConfig"
                    :mode="mode"
                    @onCreated="handleCreated"
                />
              </div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
<!--  <a-modal-->
<!--      title="文档表单"-->
<!--      v-model:open="modalVisible"-->
<!--      :confirm-loading="modalLoading"-->
<!--      @ok="handleModalOk">-->
<!--    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">-->
<!--      <a-form-item label="名称">-->
<!--        <a-input v-model:value="doc.name"/>-->
<!--      </a-form-item>-->
<!--      <a-form-item label="顺序">-->
<!--        <a-input v-model:value="doc.sort"/>-->
<!--      </a-form-item>-->
<!--      <a-form-item label="父分类">-->
<!--        <a-tree-select-->
<!--            v-model:value="doc.parent"-->
<!--            show-search-->
<!--            style="width: 100%"-->
<!--            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"-->
<!--            placeholder="Please select"-->
<!--            allow-clear-->
<!--            tree-default-expand-all-->
<!--            :tree-data="treeSelectData"-->
<!--            :fieldNames="{label: 'name', value: 'id', children: 'children'}"-->
<!--        >-->
<!--        </a-tree-select>-->

<!--      </a-form-item>-->
<!--      <a-form-item label="内容">-->
<!--        <div id="content">-->
<!--          <Editor-->

<!--              style="height: 400px; overflow-y: hidden"-->
<!--              @onCreated="handleCreated"-->
<!--          />-->
<!--        </div>-->
<!--      </a-form-item>-->
<!--    </a-form>-->

<!--  </a-modal>-->
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, watch, shallowRef} from "vue";
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool"
import {useRoute} from "vue-router";
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

export default defineComponent({
  name: 'AdminDoc',
  components: { Editor, Toolbar },
  setup() {
    const route = useRoute();
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
        slots: { customRender: 'name'}
      },
      {
        title: 'action',
        key: 'action',
        slots: {customRender: 'action'}
      },
    ];

    const level1 = ref();
    level1.value = [];
    const treeSelectData = ref();
    treeSelectData.value = [];

    /**
     * 数据查询
     */
    const handleQuery = () => {
      loading.value = true;
      level1.value = [];
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;

        if (data.status == 0) {
          docs.value = data.data;
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log('树形', level1.value);
          treeSelectData.value = Tool.copy(level1.value);
          treeSelectData.value.unshift({id: 0, name: '无'});

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
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    }
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const editorRef = shallowRef()
    // editorRef.value.config.zIndex = 0;

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = valueHtml.value;
      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          message.success("保存成功");
          //重新加载
          handleQuery();
        } else {
          message.success("保存成功");
        }
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


    let ids: Array<string>  = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
          for (let i = 0; i < treeSelectData.length; i++) {
            const node = treeSelectData[i];
            if (node.id === id) {
              console.log('disabled', node);
              // node.disabled = true;
              ids.push(id);

              const children = node.children;
              if (Tool.isNotEmpty(children)) {
                for (let j = 0; j < children.length; j++) {
                  getDeleteIds(children, children[j].id);
                }
              }
            } else {
              const children = node.children;
              if (Tool.isNotEmpty(children) ) {
                getDeleteIds(children, id);
              }
            }
          }
        };


    const valueHtml = ref('<p>hello</p>');

    /**
     * 富文本
     * @param editor
     */

    const handleCreated = (editor: any) => {
      console.log('created', editor);
      editorRef.value = editor // 记录 editor 实例，重要！
    }


    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id).then((response) => {
        const data = response.data;
        const editor = editorRef.value;
        console.log('cont==>endit', editor);
        console.log('cont==>data', data);
        if (data.status == 0) {
          console.log('datacontant',data.data.content)
          valueHtml.value = data.data.content;
          console.log('cont==>endit22', editor);
        } else {
          console.log('error')
          message.error(data.message);
        }
      });
    };


    /**
     * 编辑
     */

    const edit = (record: any) => {
      // modalVisible.value = true;

      doc.value = Tool.copy(record);

      handleQueryContent();
      console.log('edit====', doc.value);

      treeSelectData.value = Tool.copy(level1.value);

      console.log('treeSelectDate===',treeSelectData.value)
      setDisable(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id: 0, name: '无'});

    };

    /**
     * 新增
     */
    const add = () => {
      console.log("on___add-----");
      modalVisible.value = true;
      doc.value = { ebookId: route.query.ebookId };
      valueHtml.value = '';
      doc.value.content = valueHtml.value;

      treeSelectData.value = Tool.copy(level1.value);

      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      getDeleteIds(level1.value, id);
      axios.delete("/doc/delete/" + ids.join(',')).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          //成功
          ids = [];
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
      editorRef,
      valueHtml,

      modalVisible,
      modalLoading,
      doc,
      handleSave,
      getDeleteIds,
      handleCreated,

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