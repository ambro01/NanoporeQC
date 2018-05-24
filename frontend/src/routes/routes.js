import DashboardLayout from '../components/Dashboard/Layout/DashboardLayout.vue'
// GeneralViews
import NotFound from '../components/GeneralViews/NotFoundPage.vue'

// Admin pages
import Overview from 'src/components/Dashboard/Views/Overview.vue'
import UserProfile from 'src/components/Dashboard/Views/UserProfile.vue'
import Notifications from 'src/components/Dashboard/Views/Notifications.vue'
import NewAnalysis from 'src/components/Dashboard/Views/NewAnalysis.vue'
import Typography from 'src/components/Dashboard/Views/Typography.vue'
import Icons from 'src/components/Dashboard/Views/Icons.vue'
import MyAnalyses from 'src/components/Dashboard/Views/MyAnalyses.vue'
import Login from 'src/components/Login/login.vue'

const routes = [
  {
    path: '/',
    name: 'login',
    component: Login
  },
  {
    path: '/view',
    component: DashboardLayout,
    redirect: '/view/stats',
    children: [
      {
        path: 'overview',
        name: 'overview',
        component: Overview
      },
      {
        path: 'stats',
        name: 'stats',
        component: UserProfile
      },
      {
        path: 'notifications',
        name: 'notifications',
        component: Notifications
      },
      {
        path: 'new-analysis',
        name: 'new-analysis',
        component: NewAnalysis
      },
      {
        path: 'my-analyses',
        name: 'my-analyses',
        component: MyAnalyses
      },
      {
        path: 'typography',
        name: 'typography',
        component: Typography
      },
      {
        path: 'icons',
        name: 'icons',
        component: Icons
      }
    ]
  },
  {path: '*', component: NotFound}
]

/**
 * Asynchronously load view (Webpack Lazy loading compatible)
 * The specified component must be inside the Views folder
 * @param  {string} name  the filename (basename) of the view to load.
 function view(name) {
   var res= require('../components/Dashboard/Views/' + name + '.vue');
   return res;
};**/

export default routes
