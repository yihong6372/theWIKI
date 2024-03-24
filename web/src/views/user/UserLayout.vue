<template>
  <div id="userLayout" :class="'user-layout-wrapper'">
    <div class="container">


      <div class="user-layout-content">
        <div class="top">
          <div class="header">
            <a href="/web/public">
<!--              <img src="#" class="logo" alt="logo">-->
              <span class="title">Ant Design</span>
            </a>
          </div>
          <div class="desc">
            {{ ('layouts.userLayout.title') }}
          </div>
        </div>

        <router-view />

        <div class="footer">
          <div class="links">
            <a href="_self">帮助</a>
            <a href="_self">隐私</a>
            <a href="_self">条款</a>
          </div>
          <div class="copyright">
            Copyright &copy; 2018 vueComponent
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: 'UserView',
  setup() {
    console.log('UserView setup')
  }
})
</script>

<style lang="less" scoped>

.user-layout-lang {
  background-color: rgba(61, 58, 133, 0.71);
  backdrop-filter: blur(4px);
  height: 40px;
  border: 3px;
  border-radius: 4px;
  padding: 3px;
}
#userLayout.user-layout-wrapper {
  height: 100%;
  background-image:
      url("../../assets/login-background.jpg");
  background-size: cover;

  &.mobile {
    .container {
      .main {
        max-width: 368px;
        width: 98%;
      }
    }
  }

  .container {
    width: 100%;
    min-height: 100%;
    //background: #f0f2f5 url(~@/assets/background.svg) no-repeat 50%;
    background-size: 100%;
    //padding: 50px 0 84px;
    position: relative;
    padding: 40px;

    .user-layout-lang {
      width: 100%;
      height: 40px;
      line-height: 44px;
      text-align: right;

      .select-lang-trigger {
        cursor: pointer;
        padding: 12px;
        margin-right: 24px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        vertical-align: middle;
      }
    }

    .user-layout-content {
      padding: 32px 0 24px;

      .top {
        text-align: center;

        .header {
          height: 44px;
          line-height: 44px;

          .badge {
            position: absolute;
            display: inline-block;
            line-height: 1;
            vertical-align: middle;
            margin-left: -12px;
            margin-top: -10px;
            opacity: 0.8;
          }

          .logo {
            height: 44px;
            vertical-align: top;
            margin-right: 16px;
            border-style: none;
          }

          .title {
            font-size: 33px;
            color: rgba(0, 0, 0, .85);
            font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
            font-weight: 600;
            position: relative;
            top: 2px;
          }
        }
        .desc {
          font-size: 14px;
          color: rgba(0, 0, 0, 0.45);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }

      .main {
        min-width: 200px;
        width: 400px;
        margin: 30px auto;
      }

      .footer {
        // position: absolute;
        width: 100%;
        bottom: 0;
        padding: 0 16px;
        margin: 48px 0 24px;
        text-align: center;

        .links {
          margin-bottom: 8px;
          font-size: 14px;
          a {
            color: rgba(0, 0, 0, 0.45);
            transition: all 0.3s;
            &:not(:last-child) {
              margin-right: 40px;
            }
          }
        }
        .copyright {
          color: rgba(0, 0, 0, 0.45);
          font-size: 14px;
        }
      }
    }

    a {
      text-decoration: none;
    }

  }
}
</style>
