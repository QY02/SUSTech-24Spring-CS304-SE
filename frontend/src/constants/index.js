// 合同状态枚举
import {ref} from "vue";

export const CONTRACT_STATUS = {
    FAIL: 0,
    AUDIT_PENDING: 1,
    EXEC_PENDING: 2,
    EXECUTING: 3,
    FINISH: 4,
};
export const EVENT_TYPE_MAP = {
    0: '讲座',
    1: '工作坊',
    2: '比赛',
    3: '表演',
    4: '展览',
    5: '论坛',
    6: '体育',
    7: '志愿',
    8: '学院',
    9: '沙龙',
    10: '培训',
    11: '社团',
    12: '其他',
};

export const EVENT_TYPE_value = ref([
    {label: '讲座', value: 1,},
    {label: '工作坊', value: 2,},
    {label: '比赛', value: 3},
    {label: '表演', value: 4},
    {label: '展览', value: 5},
    {label: '论坛', value: 6,},
    {label: '体育', value: 7},
    {label: '志愿', value: 8},
    {label: '学院', value: 9},
    {label: '沙龙', value: 10,},
    {label: '培训', value: 11},
    {label: '社团', value: 12},
    {label: '其他', value: 13},
]);
export const EVENT_TYPE_value_1 = ref([ //从0开始的
    {label: '讲座', value: 0,},
    {label: '工作坊', value: 1,},
    {label: '比赛', value: 2},
    {label: '表演', value: 3},
    {label: '展览', value: 4},
    {label: '论坛', value: 5,},
    {label: '体育', value: 6},
    {label: '志愿', value: 7},
    {label: '学院', value: 8},
    {label: '沙龙', value: 9,},
    {label: '培训', value: 10},
    {label: '社团', value: 11},
    {label: '其他', value: 12},
]);
// 合同类型枚举
export const CONTRACT_TYPES = {
    MAIN: 0,
    SUB: 1,
    SUPPLEMENT: 2,
};

// 合同收付类型枚举
export const CONTRACT_PAYMENT_TYPES = {
    PAYMENT: 0,
    RECEIPT: 1,
};

// 通知主题映射
export const NOTIFICATION_THEMES = {
    0: 'default',
    1: 'success',
    2: 'danger',
    3: 'primary',
    4: 'warning',
    5: 'danger'
};
//通知类型映射
export const NOTIFICATION_TYPES = {
    0: '其他',
    1: '审核通过',
    2: '审核未通过',
    3: '成功参加活动',
    4: '活动修改',
    5: '活动取消'
};


