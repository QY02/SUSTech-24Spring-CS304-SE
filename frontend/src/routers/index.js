import {createRouter, createWebHistory} from 'vue-router';

const routes = [
    {
        // 此处的‘@’表示src目录
        name:'login',
        path: '/login',
        component: () => import('@/components/login/loginIn.vue')
    },
    {
        path: '/register',
        component: () => import('@/components/login/registerForm.vue')
    },
    {
        path: '/',
        component: () => import('@/layouts/MainLayout.vue'),
        children: [
            {
                name: 'OuterHome',
                path: '/',
                component: () => import('@/components/OuterHome.vue')
            },

            {
                name: 'home',
                path: '/HomePage',
                component: () => import('@/components/home/HomePage.vue')
            },
            // {
            //     path: '/homeHot',
            //     component: () => import('@/components/home/HomeHot.vue')
            // },
            // {
            //     path: '/homeNew',
            //     component: () => import('@/components/home/HomeNew.vue')
            // },
            // {
            //     path: '/homeRecommend',
            //     component: () => import('@/components/home/HomeRecommend.vue')
            // },

            {
                path: '/historyEvents',
                component: () => import('@/components/home/UserHistory.vue')
            },
            {
                path: '/myPublishes',
                component: () => import('@/components/home/UserPublishedPage.vue')
            },
            {
                path: '/myFavorites',
                component: () => import('@/components/home/MyFavoritesPage.vue')
            },
            {
                path: '/myOrderRecords',
                component: () => import('@/components/home/MyOrderRecords.vue')
            },
            {
                path: '/OrderRecordDetails',
                component: () => import('@/components/home/OrderRecordDetail.vue')
            },
            {
                path: '/approval',
                component: () => import('@/components/admin/EventApproval.vue')
            },
            {
                name: 'book',
                path: '/book',
                component: () => import('@/components/book/Steps.vue')
            },
            {
                name: 'applyEvent',
                path: '/applyEvent',
                component: () => import('@/components/event/ApplyForEvent.vue')
            },
            {
                name: 'event',
                path: '/event',
                component: () => import('@/components/event/EventPage.vue')
            },
            {
                name: 'moments',
                path: '/moments',
                component: () => import('@/components/moment/Moments.vue')
            },
            {
                name: 'notification',
                path: '/notification',
                component: () => import('@/components/notification/NoticeAll.vue')
            },
            {
                name: 'user',
                path: '/user',
                component: () => import('@/components/profile/userPage.vue')
            },
            {
                name: 'chatWithEvent',
                path: '/chatWithEvent',
                component: () => import('@/components/chat/chatWithEvent.vue')
            },
            {
                name: 'chat',
                path: '/chat',
                component: () => import('@/components/chat/singleChat.vue')
            },
            {   name: 'adminUserManage',
                path: '/admin/userManage',
                component: () => import('@/components/admin/userManagement.vue')
            },
            {
                path: '/admin/eventManage',
                component: () => import('@/components/admin/homePage.vue')
            },
            {
                name: 'newMoment',
                path: '/newMoment',
                component: () => import('@/components/moment/createMoment/index.vue')
            },
            {
                name: '403forbidden',
                path: '/forbidden',
                component: () => import('@/components/admin/403page.vue')
            }
        ]
    },
    {
        path: '/admin',
        component: () => import('@/layouts/AdminLayout.vue'),
        children: [
            {
                path: '/',
                redirect: '/adminHomePage',
                component: () => import('@/components/admin/homePage.vue')
            },
            {
                name: 'momentAudit',
                path: '/admin/momentAudit',
                component: () => import('@/components/admin/adminMoments.vue')
            },
            {
                name: 'adminHomePage',
                path: '/admin/homepage',
                component: () => import('@/components/admin/homePage.vue')
            },
            {   name: 'adminUserManage',
                path: '/admin/userManage',
                component: () => import('@/components/admin/userManagement.vue')
            },
            {
                name: 'approval',
                path: '/admin/approval',
                component: () => import('@/components/admin/EventApproval.vue')
            },
        ],
        beforeEnter: (to, from, next) => {
            if (!isAdmin()) {
                next({ name: '403forbidden' });
            } else {
                next();
            }
        }
    },
];

function isAdmin() {
    return sessionStorage.getItem('role') === 'admin';
}

const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;
