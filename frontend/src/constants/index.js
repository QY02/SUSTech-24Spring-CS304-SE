// 合同状态枚举
export const CONTRACT_STATUS = {
    FAIL: 0,
    AUDIT_PENDING: 1,
    EXEC_PENDING: 2,
    EXECUTING: 3,
    FINISH: 4,
};

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


