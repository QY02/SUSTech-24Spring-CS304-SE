import type { FormRule, UploadFile } from 'tdesign-vue-next';

export const FORM_RULES: Record<string, FormRule[]> = {
  name: [{ required: true, message: '请输入标题', type: 'error' }],
  comment: [{ required: true, message: '请输入内容', type: 'error' }],
    event: [{ required: true, message: '请选择活动', type: 'error' }],
};

export const INITIAL_DATA = {
  name: '',
  type: '',
  comment: '',
  event: {
    label: '',
    value: '',
  },
  files: [] as Array<UploadFile>,
};
