import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import AdminView from '@/views/AdminView.vue'
import MemberDetailsView from '@/views/MemberDetailsView.vue'
import FacultyManagement from '@/components/FacultyManagement.vue'
import DepartmentManagement from '@/components/DepartmentManagement.vue'
import ServiceManagement from '@/components/ServiceManagement.vue'
import MemberManagement from '@/components/MemberManagement.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
    },
    {
      path: '/members/:id',
      name: 'member-details',
      component: MemberDetailsView,
    },
    {
      path: '/admin',
      component: AdminView,
      children: [
        {
          path: '',
          redirect: '/admin/faculties',
        },
        {
          path: 'faculties',
          component: FacultyManagement,
        },
        {
          path: 'departments',
          component: DepartmentManagement,
        },
        {
          path: 'services',
          component: ServiceManagement,
        },
        {
          path: 'members',
          component: MemberManagement,
        },
      ],
    },
  ],
})

export default router
