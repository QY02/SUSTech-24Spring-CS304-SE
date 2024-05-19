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
                path: '/apply',
                component: () => import('@/components/event/ApplyForEvent.vue')
            },
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
            {
                name: 'adminHomePage',
                path: '/admin/homepage',
                component: () => import('@/components/admin/homePage.vue')
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
                name: 'momentAudit',
                path: '/momentAudit',
                component: () => import('@/components/admin/adminMoments.vue')
            }
        ]
    },


    // //userLayout
    // {
    //     path: '/layout',
    //     // component: () => import('@/components/layouts/LayOutOfCourse.vue'), // 导入公告组件
    //     children: [
    //         {//默认路径
    //             path: '',
    //             redirect: '/courseHome'
    //         },
    //         {//进入的路径
    //             path: '/courseHome', // 使用动态路由参数来传递课程ID
    //             // name: '/CourseDetail',
    //             // component: () => import('@/components/courseHome/courseMain.vue'), // 课程详情组件的路径
    //         },
    //     ],
    // },
    //
    // // teacher-layout-course
    // {
    //     path: '/layoutEventTeacher',
    //     // component: () => import('@/components/layouts/LayOutOfEventTeacher.vue'),
    //     children: [
    //         {//默认路径
    //             path: '',
    //             redirect: '/EventHomeTeacher',
    //         },
    //     ],
    // },
    //
    // //profile
    //
    // {
    //     path: '/layoutProfile',
    //     // component: () => import('@/components/layouts/LayOutOfProfile.vue'),
    //     children: [
    //         {
    //             path: '',
    //             // redirect: '/personalInformation',
    //         },
    //         {
    //             path: '/personalInformation',
    //             // component: () => import('@/components/profile/PersonalInformation.vue'),
    //         },
    //         {
    //             path: '/skills',
    //             // component: () => import('@/components/profile/TechnologyStack.vue'),
    //         },
    //         {
    //             path: '/theme',
    //             // component: () => import('@/components/profile/ThemeSetting.vue'),
    //         },
    //         {
    //             path: '/programming',
    //             // component: () => import('@/components/profile/ProgrammingSkills.vue'),
    //         },
    //     ]
    // },
    //
    // {
    //     path: '/layoutProfileTeacher',
    //     // component: () => import('@/components/layouts/LayoutOfProfileTeacher.vue'),
    //     children: [
    //         {
    //             path: '',
    //             // redirect: '/teacher/personalInformation',
    //         },
    //         {
    //             path: '/teacher/personalInformation',
    //             // component: () => import('@/components/profile/PersonalInformation.vue'),
    //         },
    //         {
    //             path: '/teacher/skills',
    //             // component: () => import('@/components/profile/TechnologyStackTeacher.vue'),
    //         },
    //         {
    //             path: '/teacher/theme',
    //             // component: () => import('@/components/profile/ThemeSetting.vue'),
    //         },
    //     ]
    // },
    //
    // {
    //     path: '/layoutProfileAdmin',
    //     // component: () => import('@/components/layouts/LayoutOfProfileAdmin.vue'),
    //     children: [
    //         {
    //             path: '',
    //             // redirect: '/admin/personalInformation',
    //         },
    //         {
    //             path: '/admin/personalInformation',
    //             // component: () => import('@/components/profile/PersonalInformation.vue'),
    //         },
    //         {
    //             path: '/admin/theme',
    //             // component: () => import('@/components/profile/ThemeSetting.vue'),
    //         },
    //     ]
    // },
    //
    // {
    //     path: '/teacher/homepage',
    //     // component: () => import('@/components/home/HomePageTeacher.vue'),
    // },
    //
    //
    // {
    //     path: '/teacher/welcomePage',
    //     // component: () => import('@/components/home/WelcomeHomeTeacher.vue'),
    // },
    //
    // {
    //     path: '/homepage',
    //     // component: () => import('@/components/home/HomePage.vue')
    // },
    // {
    //     path: '/welcomePage',
    //     // component: () => import('@/components/home/WelcomeHome.vue')
    // },
    // {
    //     path: '/admin/homepage',
    //     // component: () => import('@/components/home/HomePageAdmin.vue'),
    //     children: [
    //         {
    //             path: '',
    //             redirect: '/admin/welcomePage',
    //         },
    //         {
    //             path: '/admin/welcomePage',
    //             // component: () => import('@/components/home/WelcomeHomeAdmin.vue'),
    //         },
    //         {
    //             path: '/admin/users',
    //             // component: () => import('@/components/admin/UserAdmin.vue'),
    //         },
    //         {
    //             path: '/admin/courses',
    //             // component: () => import('@/components/admin/CoursesAdmin.vue'),
    //         },
    //         {
    //             path: '/admin/announcement',
    //             // component: () => import('@/components/admin/announcementAdmin.vue'),
    //         },
    //
    //     ]
    // }


    // 添加其他路由
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 在全局前置守卫中过滤路径
// router.beforeEach((to, from, next) => {
//     if (to.path.includes('/login')) {
//         // 如果是登录路径，则不使用 MainLayout.vue 组件
//         next();
//     } else {
//         // 如果不是登录路径，使用 MainLayout.vue 组件
//         to.matched[0].components.default = () => import('@/layouts/MainLayout.vue');
//         next();
//     }
// });
export default router;
