<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
          :open-keys="openKeys"
      >

        <a-menu-item key="welcome">
            <MailOUtlined/>
            <span>欢迎</span>
        </a-menu-item>

        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
              <span>
                <user-outlined/>
                {{item.name}}
              </span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOUtlined/><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>

      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '20px', margin: 0, minHeight: '280px'}"
    >

      <div class="welcome" v-show="isShowWelcome">
        <h1>welcome</h1>
<!--        <the-welcome/>-->
        <div class="tip">
          <div><b>网站说明：</b></div>
          <div>===</div>
          <div>2. 有文档被点赞会收到实时通知哦！</div>
          <div>3.===</div>
          <div>4. 文档树可无限级扩展，支持文字、图片、<b>视频</b>
            <a href="#" target="_blank">
              知识库系统》
            </a>
          </div>
        </div>
      </div>

      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large"
              :grid="{ gutter:20, column: 3}" :pagination="pagination" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
<!--              <span v-for="{ icon, text } in actions" :key="icon">-->
<!--                <component :is="icon" style="margin-right: 8px" />-->
<!--                {{ text }}-->
<!--              </span>-->
              <span>
                <component :is="FileOutlined" style="margin-right: 8px" />
                {{item.docCount}}
              </span><span>
                <component :is="UserOutlined" style="margin-right: 8px" />
                {{item.viewCount}}
              </span><span>
                <component :is="LikeFilled" style="margin-right: 8px" />
                {{item.voteCount}}
              </span>

            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">{{ item.name }}</router-link>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef, h} from 'vue';
import axios from 'axios'
import {
  FileOutlined, LikeFilled,
  LikeOutlined,
  MessageOutlined,
  StarOutlined, UserOutlined
} from "@ant-design/icons-vue";
import {Tool} from "@/util/tool";
import { message} from "ant-design-vue";
import TheWelcome from "@/components/the-welcome.vue";


const pagination = {
  onChange: (page: number) => {
    console.log(page);
  },
  pageSize: 6,
};
const actions: any= [
  { icon: StarOutlined, text: '156' },
  { icon: LikeOutlined, text: '156' },
  { icon: MessageOutlined, text: '2' },
];

export default defineComponent({
  name: 'HomeView',
  components: {
    TheWelcome
  },
  methods: {LikeFilled, UserOutlined, FileOutlined},
  setup() {
    console.log("homeSetup......");
    const ebooks = ref();
    // const ebooks1 = reactive({books: []});
    const openKeys =  ref();
    const level1 = ref();

    let categorys: any;

    /**
     * 分类数据查询
     */
    const handleQueryCategory = () => {
      // loading.value = true;
      axios.get("/category/all").then((response) => {
        // loading.value = false;
        const data = response.data;
        if (data.status == 0) {
          categorys = data.data;
          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);

          openKeys.value = [];
          for (let i = 0; i < categorys.length; i++) {
            openKeys.value.push(categorys[i].id)
          }

        } else {
          message.error(data.msg);
        }

      });
    };


    const isShowWelcome = ref(true);
    let category2Id = 0;


    const handleQueryEbook = () => {
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000,
          category2Id: category2Id
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.data.list;
        // ebooks1.books = data.content;
      });
    };

    const handleClick = (value: any) => {
      console.log("menu click", value)
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        console.log("menu click--------", value)
        category2Id = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
      // isShowWelcome.value = value.key === 'welcome';
    };

    onMounted(() => {
      handleQueryCategory();
    });

    return {
      ebooks,
      // books: toRef(ebooks1, "books"),
      actions,
      level1,
      handleClick,
      isShowWelcome,
      openKeys,

      pagination
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
<style scoped>
.tip {
  padding: 10px 5px;
  margin-bottom: 20px;
  color: red;
  border: 1px solid transparent;
  background: linear-gradient(white,white) padding-box,repeating-linear-gradient(-45deg, black 0, black 25%, white 0, white 50%) 0/.6em .6em;
  animation:ants 12s linear infinite;
}
</style>