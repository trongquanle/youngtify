import Vue from 'vue'
import VueRouter from 'vue-router'
import { tokenExpried } from '../util/tokenUtil';

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("@/components/messenger/Index.vue"),
    redirect: {name: "youngtify"},
    beforeEnter: (to, from, next) => {
      if (tokenExpried()) {
        next();
      } else if (from.name != 'login') {
        localStorage.clear();
        next({ name: 'login' });
      }
    },
    children: [
      {
        path: "",
        name: "youngtify",
        component: () => import("@/components/messenger/chat/Index.vue")
      },
      {
        path: "friends",
        name: "friends",
        redirect: {name: "requests"},
        component: () => import("@/components/messenger/friend/Index.vue"),
        children: [
          {
            path: "search",
            name: "search",
            component: () => import("@/components/messenger/friend/content/FriendSearch.vue")
          },
          {
            path: "",
            name: "requests",
            component: () => import("@/components/messenger/friend/content/FriendInvite.vue")
          },
          {
            path: "y/:id",
            name: "y",
            component: () => import("@/components/messenger/friend/content/FriendChat.vue")
          }
        ]
      }
    ]
  },
  {
    path: "/auth",
    name: "auth",
    component: () => import("@/components/auth/Index.vue"),
    children: [
      {
        path: "login",
        name: 'login',
        component: () => import("@/components/auth/login/Index.vue")
      },
      {
        path: "register",
        name: 'register',
        component: () => import("@/components/auth/register/Index.vue")
      }
    ]
  },
  {
    path: "/videocall",
    name: "videocall",
    component: () => import("@/components/videocall/Index.vue")
  }
]

const router = new VueRouter({
  mode: "history",
  routes
})

export default router
