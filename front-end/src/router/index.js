import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import("../views/SpeedTableView.vue")
    },
    {
      path: '/average',
      name: 'average',
      component: () => import("../views/AverageSpeedView.vue")
    },
    {
      path: '/pathMatch(.*)*',
      name: "notFound",
      component: () => import("../views/PageNotFoundView.vue")
    }
  ]
})

export default router;