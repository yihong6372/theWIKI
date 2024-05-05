<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="level1.length === 0">对不起，找不到相关文档！</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length > 0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              :defaultExpandAll="true"
              :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{ doc.name }}</h2>
            <div>
              <span>阅读数：{{ doc.viewCount }}</span> &nbsp; &nbsp;
              <span>点赞数：{{ doc.voteCount }}</span>
            </div>
            <div class="ai-content">
              <a-collapse v-model:activeKey="activeKey">
                <a-collapse-panel key="1" header="AI摘要：">
<!--                  <p style="line-height: 1.6; color: #666">-->
<!--                  <p style="line-height: 1.6; color: #666">{{ eventHTML }}</p>-->
<!--                  </p>-->
                  <div class="eventHTML" v-html="eventHTML"></div>
                </a-collapse-panel>
              </a-collapse>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc"/>
          </div>
          <div class="wangeditor" :innerHTML="html"></div>
          <div class="vote-div">
            <a-button type="primary" shape="round" :size="'large'" @click="vote" v-show="doc">
              <template #icon>
                <LikeOutlined/> &nbsp;点赞：{{ doc.voteCount }}
              </template>
            </a-button>
          </div>
        </a-col>
      </a-row>

    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {computed, defineComponent, onBeforeUnmount, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import {EventSourcePolyfill} from "event-source-polyfill";
import {marked} from "marked";

export default defineComponent({
  name: 'DocView',
  setup() {
    const route = useRoute();
    const docs = ref();
    const html = ref();
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];
    // 当前选中的文档
    const doc = ref();
    doc.value = {};


    const clientId = ref(null);
    const msg = ref<string>('总结以下文章');


    /**
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档
    level1.value = [];

    /**
     * 内容查询
     **/
    const handleQueryContent = (id: number) => {
      axios.get("/doc/find-content/" + id).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          html.value = data.data.content;
          sendMsg();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        const data = response.data;
        if (data.status == 0) {
          docs.value = data.data;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          if (Tool.isNotEmpty(level1.value)) {
            defaultSelectedKeys.value = [level1.value[0].id];
            handleQueryContent(level1.value[0].id);
            // 初始显示文档信息
            doc.value = level1.value[0];
            clientId.value = doc.value.id
            console.log('开始sse：clientId:', clientId.value);
            createSSE();
          } else {
            message.info("文档为空")
          }
        } else {
          message.error(data.msg);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected', selectedKeys, info);
      if (Tool.isNotEmpty(selectedKeys)) {
        // 选中某一节点时，加载该节点的文档信息
        doc.value = info.selectedNodes[0];
        console.log('doc', doc.value)
        // 加载内容
        handleQueryContent(selectedKeys[0]);
      }
    };

    // 点赞
    const vote = () => {
      axios.get('/doc/vote/' + doc.value.id).then((response) => {
        const data = response.data;
        if (data.status === 0) {
          doc.value.voteCount++;
          message.success(data.msg);
        } else {
          message.error(data.msg);
        }
      });
    };


    type EventSourcePolyfillType = any;
    const url = 'http://localhost:8080';
    const sseEvent = ref<EventSourcePolyfillType | null>(null);
    const eventData = ref<string>('');
    const activeKey = ref(['1']);

    const eventHTML = computed(() => marked.parse(eventData.value))

    const createSSE = async () => {
      if (window.EventSource) {
        sseEvent.value = new EventSourcePolyfill(`${url}/sse/stream/${clientId.value}`, {
          heartbeatTimeout: 60 * 60 * 1000
        });

        sseEvent.value.onopen = function (event: MessageEvent) {
          console.log("连接成功", event);
        };

        sseEvent.value.onmessage = function (event: MessageEvent) {
          // let popData = eventData.value[eventData.value.length - 1];
          eventData.value += event.data;
          console.log("接收信息", event);
        };

        sseEvent.value.onerror = function (error: MessageEvent | string) {
          console.log("错误", error);
        };
      } else {
        console.log("不支持sse");
      }
    };

    const sendMsg = () => {
      eventData.value = '';
      console.log('9999', msg.value)
      axios.post(`${url}/sse/send`, {
        clientId: clientId.value,
        content: msg.value + html.value
      });
    };

    onBeforeUnmount(() => {
      if (sseEvent.value) {
        sseEvent.value.close();
        axios.get(`${url}/sse/close`, {
          params: {
            clientId: clientId.value
          }
        }).then(res => console.log(res));
      }
    });

    onMounted(() => {
      handleQuery();
    });

    return {
      level1,
      html,
      onSelect,
      defaultSelectedKeys,
      doc,
      vote,
      activeKey,
      eventData,
      eventHTML
    }
  }
});
</script>

<style>
.ai-content {
  border: 1px solid #dcdcdc;       /* 边框颜色 */
  border-radius: 8px;              /* 圆角边框 */
  padding: 0;                   /* 内边距 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 盒子阴影 */
  margin: 20px;                    /* 外边距 */
  background-color: #cbeef3;          /* 背景颜色 */
  max-width: 900px;                /* 最大宽度 */
}

.wangeditor {
  margin: 10px;
  padding: 30px;
}

/* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 样式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}

.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}

.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}

.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul, ol {
  margin: 10px 0 10px 20px;
}

/* 和antdv p冲突，覆盖掉 */
.wangeditor blockquote p {
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}

/* 点赞 */
.vote-div {
  padding: 15px;
  text-align: center;
}

/* 图片自适应 */
.wangeditor img {
  max-width: 100%;
  height: auto;
}

/* 视频自适应 */
.wangeditor iframe {
  width: 100%;
  height: 400px;
}
</style>
