import {createRouter, createWebHistory} from 'vue-router';

const routes = [
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
                // 此处的‘@’表示src目录
                name: 'login',
                path: '/login',
                component: () => import('@/components/Event/ApplyForEvent.vue')
                // component: () => import('@/components/login/loginIn.vue')
            },
            {
                path: '/register',
                component: () => import('@/components/login/registerForm.vue')
            },
            {
                path: '/apply',
                component: () => import('@/components/Event/ApplyForEvent.vue')
            },
            {
                path: '/approval',
                component: () => import('@/components/Admin/EventApproval.vue')
            },
        ]
    },

    //userLayout
    {
        path: '/layout',
        // component: () => import('@/components/layouts/LayOutOfCourse.vue'), // 导入公告组件
        children: [
            {//默认路径
                path: '',
                redirect: '/courseHome'
            },
            {//进入的路径
                path: '/courseHome', // 使用动态路由参数来传递课程ID
                // name: '/CourseDetail',
                // component: () => import('@/components/courseHome/courseMain.vue'), // 课程详情组件的路径
            },
        ],
    },

    // teacher-layout-course
    {
        path: '/layoutEventTeacher',
        // component: () => import('@/components/layouts/LayOutOfEventTeacher.vue'),
        children: [
            {//默认路径
                path: '',
                redirect: '/EventHomeTeacher',
            },
        ],
    },

    //profile

    {
        path: '/layoutProfile',
        // component: () => import('@/components/layouts/LayOutOfProfile.vue'),
        children: [
            {
                path: '',
                // redirect: '/personalInformation',
            },
            {
                path: '/personalInformation',
                // component: () => import('@/components/profile/PersonalInformation.vue'),
            },
            {
                path: '/skills',
                // component: () => import('@/components/profile/TechnologyStack.vue'),
            },
            {
                path: '/theme',
                // component: () => import('@/components/profile/ThemeSetting.vue'),
            },
            {
                path: '/programming',
                // component: () => import('@/components/profile/ProgrammingSkills.vue'),
            },
        ]
    },

    {
        path: '/layoutProfileTeacher',
        // component: () => import('@/components/layouts/LayoutOfProfileTeacher.vue'),
        children: [
            {
                path: '',
                // redirect: '/teacher/personalInformation',
            },
            {
                path: '/teacher/personalInformation',
                // component: () => import('@/components/profile/PersonalInformation.vue'),
            },
            {
                path: '/teacher/skills',
                // component: () => import('@/components/profile/TechnologyStackTeacher.vue'),
            },
            {
                path: '/teacher/theme',
                // component: () => import('@/components/profile/ThemeSetting.vue'),
            },
        ]
    },

    {
        path: '/layoutProfileAdmin',
        // component: () => import('@/components/layouts/LayoutOfProfileAdmin.vue'),
        children: [
            {
                path: '',
                // redirect: '/admin/personalInformation',
            },
            {
                path: '/admin/personalInformation',
                // component: () => import('@/components/profile/PersonalInformation.vue'),
            },
            {
                path: '/admin/theme',
                // component: () => import('@/components/profile/ThemeSetting.vue'),
            },
        ]
    },

    {
        path: '/teacher/homepage',
        // component: () => import('@/components/home/HomePageTeacher.vue'),
    },


    {
        path: '/teacher/welcomePage',
        // component: () => import('@/components/home/WelcomeHomeTeacher.vue'),
    },

    {
        path: '/homepage',
        // component: () => import('@/components/home/HomePage.vue')
    },
    {
        path: '/welcomePage',
        // component: () => import('@/components/home/WelcomeHome.vue')
    },
    {
        path: '/admin/homepage',
        // component: () => import('@/components/home/HomePageAdmin.vue'),
        children: [
            {
                path: '',
                redirect: '/admin/welcomePage',
            },
            {
                path: '/admin/welcomePage',
                // component: () => import('@/components/home/WelcomeHomeAdmin.vue'),
            },
            {
                path: '/admin/users',
                // component: () => import('@/components/admin/UserAdmin.vue'),
            },
            {
                path: '/admin/courses',
                // component: () => import('@/components/admin/CoursesAdmin.vue'),
            },
            {
                path: '/admin/announcement',
                // component: () => import('@/components/admin/announcementAdmin.vue'),
            },

        ]
    }


    // 添加其他路由
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
