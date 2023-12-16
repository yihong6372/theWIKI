<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:selectedKeys="state.selectedKeys"
          v-model:openKeys="state.openKeys"
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          :items="items"
      >
<!--        <a-sub-menu key="sub1">-->
<!--          <template #title>-->
<!--              <span>-->
<!--                <user-outlined/>-->
<!--                subnav 1-->
<!--              </span>-->
<!--          </template>-->
<!--          <a-menu-item key="1">option1</a-menu-item>-->
<!--          <a-menu-item key="2">option2</a-menu-item>-->
<!--          <a-menu-item key="3">option3</a-menu-item>-->
<!--          <a-menu-item key="4">option4</a-menu-item>-->
<!--        </a-sub-menu>-->

      </a-menu>
    </a-layout-sider>
    <a-layout-contents
        :style="{ background: '#fff', padding: '20px', margin: 0, minHeight: '280px' }"
    >
      <a-list item-layout="vertical" size="large" :grid="{ gutter:20, column: 3}" :pagination="pagination" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ icon, text } in actions" :key="icon">
                <component :is="icon" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-contents>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef, VueElement, h} from 'vue';
import axios from 'axios'
import {
  AppstoreOutlined,
  CalendarOutlined,
  LikeOutlined,
  MailOutlined,
  MessageOutlined, SettingOutlined,
  StarOutlined
} from "@ant-design/icons-vue";
import {Tool} from "@/util/tool";
import {ItemType, message} from "ant-design-vue";


const pagination = {
  onChange: (page: number) => {
    console.log(page);
  },
  pageSize: 3,
};
const actions: any= [
  { icon: StarOutlined, text: '156' },
  { icon: LikeOutlined, text: '156' },
  { icon: MessageOutlined, text: '2' },
];

export default defineComponent({
  name: 'HomeView2',
  setup() {
    console.log("setup......");
    const ebooks = ref();
    // const ebooks1 = reactive({books: []});
    function getItem(
        label: string,
        key: string,
        children?: ItemType[],
        icon?: any,
        type?: 'group',
    ): ItemType {
      return {
        key,
        icon,
        children,
        label,
        type,
      } as ItemType;
    }

    const level1 = ref();

    let categorys: any;

    const state = ref({
      selectedKeys: ['1'],
      openKeys: ['sub1'],
    });

    const items: any = ref([
      getItem('Navigation Three', 'sub2',  [
        getItem('Option 7', '7',),
        getItem('Option 8', '8',),
        getItem('Option 9', '9',),
        getItem('Option 10', '10',),
      ],h(SettingOutlined)),
      // categorys.forEach((item: any) => {
      //   if (item.id == 0) {
      //     getItem(item.name, item.id,null, getItem())
      //   }
      // })
    ]);

    const toItems = (array: any, parentId: number) => {
      const target: any = [];
      array.forEach((i: any) => {
        if (i.parent === 0) {
          const child: any = [];
          if (i.children != null) {
            i.children.forEach((j: any) => {
                child.push(getItem(j.name, j.id));
            })
          }
          const item = getItem(i.name, i.id, child);
          target.push(item);
        }
      });
      items.value = target;
    }
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

             toItems(level1.value, 0);
        } else {
          message.error(data.msg);
        }

      });
    };


    onMounted(() => {

      handleQueryCategory();
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.data.list;
      });
    });

    return {
      ebooks,
      // books: toRef(ebooks1, "books"),
      actions,
      // level1,
      items,
      state
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
