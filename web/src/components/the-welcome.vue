<template>
  <div>
    <div class="tip">
      <div><b>示例网站说明：</b></div>
      <div>1. 统计数据是真实的，一分钟左右延时。</div>
      <div>2. 有文档被点赞会收到实时通知哦！</div>
      <div>3. 左侧菜单是动态加载的，登录后在分类管理可配置</div>
      <div>4. 文档树可无限级扩展，支持文字、图片、<b>视频</b></div>
      <div>你也想有个WIKI知识库吗？，<b>配套视频课程</b>：
        <a href="#" target="_blank">
        知识库系统》
        </a>
      </div>
    </div>

    <a-row>
      <a-col :span="24">
        <a-card>
          <a-row>
            <a-col :span="8">
              <a-statistic title="总阅读量" :value="statistic.viewCount">
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="总点赞量" :value="statistic.voteCount">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="点赞率" :value="statistic.voteCount / statistic.viewCount * 100"
                           :precision="2"
                           suffix="%"
                           :value-style="{ color: '#cf1322' }">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic title="今日阅读" :value="statistic.todayViewCount" style="margin-right: 50px">
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic title="今日点赞" :value="statistic.todayVoteCount">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic
                title="预计今日阅读"
                :value="statistic.todayViewIncrease"
                :value-style="{ color: '#0000ff' }"
              >
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic
                title="预计今日阅读增长"
                :value="statistic.todayViewIncreaseRateAbs"
                :precision="2"
                suffix="%"
                class="demo-class"
                :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
              >
                <template #prefix>
                  <arrow-down-outlined v-if="statistic.todayViewIncreaseRate < 0"/>
                  <arrow-up-outlined v-if="statistic.todayViewIncreaseRate >= 0"/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>

    <br>
    <a-row>
      <a-col :span="24" id="main-col">
        <div id="main" style="width: 100%;height:300px;"></div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, onMounted } from 'vue'
  import axios from 'axios';

  // declare let echarts: any;
  declare let echarts: any;

  export default defineComponent({
    name: 'the-welcome',
    setup () {
      const statistic = ref();
      statistic.value = {};
      const getStatistic = () => {
        axios.get('/ebook-snapshot/get-statistic').then((response) => {
          const data = response.data;
          console.log(data)
          if (data.data.length > 1) {
            const statisticResp = data.data;
            console.log("昨天有数据：",statisticResp)
            statistic.value.viewCount = statisticResp[1].viewCount;
            statistic.value.voteCount = statisticResp[1].voteCount;
            statistic.value.todayViewCount = statisticResp[1].viewIncrease;
            statistic.value.todayVoteCount = statisticResp[1].voteIncrease;
            console.log('snapshot')

            // 按分钟计算当前时间点，占一天的百分比
            const now = new Date();
            const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
            // console.log(nowRate)
            statistic.value.todayViewIncrease = parseInt(String(statisticResp[1].viewIncrease / nowRate));
            // todayViewIncreaseRate：今日预计增长率
            statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - statisticResp[0].viewIncrease) / statisticResp[0].viewIncrease * 100;
            statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
          } else {
            const statisticResp = data.data;
            console.log("昨天没有数据：",statisticResp)
            statistic.value.viewCount = statisticResp[0].viewCount;
            statistic.value.voteCount = statisticResp[0].voteCount;
            statistic.value.todayViewCount = statisticResp[0].viewIncrease;
            statistic.value.todayVoteCount = statisticResp[0].voteIncrease;
            console.log('snapshot')

            // 按分钟计算当前时间点，占一天的百分比
            const now = new Date();
            const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
            // console.log(nowRate)
            statistic.value.todayViewIncrease = parseInt(String(statisticResp[0].viewIncrease / nowRate));
            // todayViewIncreaseRate：今日预计增长率
            statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - 0) ;
            statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
          }
        });
      };

      const init30DayEcharts = (list: any) => {
        console.log('init30')
        // 发布生产后出现问题：切到别的页面，再切回首页，报表显示不出来
        // 解决方法：把原来的id=main的区域清空，重新初始化
        const mainDom = document.getElementById('main-col');
        if (mainDom) {
          mainDom.innerHTML = '<div id="main" style="width: 100%;height:300px;"></div>';
        }
        // 基于准备好的dom，初始化echarts实例
        const myChart = echarts.init(document.getElementById('main'));

        const xAxis = [];
        const seriesView = [];
        const seriesVote = [];
        for (let i = 0; i < list.length; i++) {
          const record = list[i];
          xAxis.push(record.date);
          seriesView.push(record.viewIncrease);
          seriesVote.push(record.voteIncrease);
        }

        // 指定图表的配置项和数据
        const option = {
          title: {
            text: '30天趋势图'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['总阅读量', '总点赞量']
          },
          grid: {
            left: '1%',
            right: '3%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxis
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '总阅读量',
              type: 'line',
              // stack: '总量', 不堆叠
              data: seriesView,
              smooth: true
            },
            {
              name: '总点赞量',
              type: 'line',
              // stack: '总量', 不堆叠
              data: seriesVote,
              smooth: true
            }
          ]
        };



        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
      };

      const get30DayStatistic = () => {
        axios.get('/ebook-snapshot/get-30-statistic').then((response) => {
          const data = response.data;
          if (data) {
            const statisticList = data.data;

            init30DayEcharts(statisticList)
          }
        });
      };

      const testEcharts = () => {
        // 基于准备好的dom，初始化echarts实例
        const myChart = echarts.init(document.getElementById('main'));

        // 指定图表的配置项和数据
        const option = {
          title: {
            text: 'ECharts 入门示例'
          },
          tooltip: {},
          legend: {
            data:['销量']
          },
          xAxis: {
            data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
          },
          yAxis: {},
          series: [{
            name: '销量',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20]
          }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
      };

      onMounted(() => {
        getStatistic();
        // testEcharts();
        get30DayStatistic();
      });

      return {
        statistic
      }
    }
  });
</script>
<style scoped>
.tip {
  padding: 10px 5px;
  margin-bottom: 20px;
  color: rgba(216, 0, 12, 0.99); /* 使用具体的色号来代替红色，增强可控性 */
  border: 1px solid #ffbaba; /* 给予边框一种淡红色，以突出警告效果 */
  background: linear-gradient(to bottom, #ffffff 0%, #ffffff 100%),
  repeating-linear-gradient(-45deg, #ffbdbd 0, #ffbdbd 25%, #ffffff 25%, #ffffff 50%);
  background-size: 100% 100%, 10px 10px; /* 调整斜条纹背景的大小 */
  animation: ants 12s linear infinite;
  border-radius: 4px; /* 添加圆角以获得更现代的视觉效果 */
  box-shadow: 0 2px 4px rgba(0,0,0,0.1); /* 添加阴影提升层次感 */
}

/* 定义动画 */
@keyframes ants {
  to {
    background-position: 100% 100%, 0 0;
  }
}

</style>
