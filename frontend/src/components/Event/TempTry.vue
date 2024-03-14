<template>
  <div class="t-table-demo__editable-row" style="width: 100%">
    <div style="display: flex;justify-content: space-between;margin: 10px;align-items: center">
      <va-button
          @click="()=>visibleBody=true"
          icon="add"
          style="width: fit-content;height: fit-content"
      >
        Add New project
      </va-button>
      <va-input
          v-model.number="perPage"
          type="number"
          placeholder="Items..."
          label="Items per page"
      />
      <va-input
          v-model.number="currentPage"
          type="number"
          placeholder="Page..."
          label="Current page"
      />
      <va-input
          v-model="filter"
          class="sm:col-span-2 md:col-span-3"
          placeholder="Filter..."
      />

    </div>
    <va-data-table
        class="table-crud"
        :items="data"
        :columns="columns"
        :per-page="perPage"
        :current-page="currentPage"
        striped
        selectable
        v-model="selectedList"
        :filter="filter"
        @filtered="filtered = $event.items"
    >
      <template #cell(introduction)="{ value }">
        <div style="white-space: pre-wrap; word-break: break-all;"> {{ value }}</div>
      </template>
      <template #cell(actions)="{ rowIndex }">
        <va-button
            preset="plain"
            icon="edit"
        />
        <va-button
            preset="plain"
            icon="delete"
            class="ml-3"
        />
      </template>

      <template #bodyAppend>
        <tr>
          <td colspan="8">
            <div style="display: flex;
                             justify-content: center;
                              margin-top: 7px;width: 100%;">
              <va-pagination v-model="currentPage" :pages="pages"/>
            </div>
          </td>
        </tr>
      </template>
    </va-data-table>
    <div>
      <t-button @click="()=>visibleBody=true">
        <template #icon>
          <add-icon/>
        </template>
        添加新场次
      </t-button>
    </div>
    <br/>
  </div>
    <t-dialog
        :visible="visibleBody"
        attach="body"
        header="请填写场次信息"
        destroy-on-close="true"
        width="45%"
        :cancel-btn="null"
        :confirm-btn="null"
    >
      <template #body>
        <t-space direction="vertical">
          <t-form
              ref="form"
              id="form"
              :data="newData"
              reset-type="initial"
              @reset="onReset"
              @submit="addData"
              :rules="FORM_RULES"
          >
            <t-form-item label="是否需要报名" name="registration_required">
              <t-switch v-model="newData.registration_required" :label="['是', '否']"></t-switch>
            </t-form-item>
            <t-form-item label="报名开始时间-报名结束时间" name="registration_time_range">
              <t-date-range-picker enable-time-picker="true" allow-input="true" clearable="true"
                                   v-model="newData.registration_time_range"
                                   :disabled="!newData.registration_required"/>
            </t-form-item>
            <t-form-item label="开始时间-结束时间" name="event_time_range">
              <t-date-range-picker enable-time-picker="true" allow-input="true" clearable="true"
                                   v-model="newData.event_time_range"/>
            </t-form-item>

            <t-form-item label="人数" name="count_range_of_people">
              <t-range-input v-model="newData.count_range_of_people" :placeholder="['最小值','最大值']"/>
            </t-form-item>
            <t-form-item label="地址" name="venue">
              <t-input v-model="newData.venue">地址</t-input>
            </t-form-item>
            <t-form-item label="地图" name="location">
              <t-input v-model="newData.location">地图</t-input>
            </t-form-item>
            <t-form-item label="是否可见" name="visible">
              <t-switch v-model="newData.visible" :label="['是', '否']"></t-switch>
            </t-form-item>

            <t-form-item>
              <t-space size="small">
                <t-button theme="primary" type="submit">提交</t-button>
                <t-button theme="default" variant="base" type="reset">重置</t-button>
              </t-space>
            </t-form-item>
          </t-form>
        </t-space>

      </template>
    </t-dialog>


</template>

<script setup lang="jsx">
import {computed, ref} from 'vue';
import {DateRangePicker, Input, MessagePlugin, RangeInput, Switch} from 'tdesign-vue-next';
import {AddIcon} from "tdesign-icons-vue-next";
import React from 'react';

const selectedList = ref([]);
const filter = ref("");
const filtered = ref("");
const pages = computed(() => {
  return perPage.value && perPage.value !== 0
      ? Math.ceil(filtered.value.length / perPage.value)
      : filtered.value.length;
});
let cnt = 1;
const initData = new Array(7).fill(null).map(() => ({
  key: String(cnt++),
  registration_required: false,
  registration_time_range: [],
  event_time_range: [],
  count_range_of_people: [],
  seat_map_id: '',
  venue: '',
  location: '',
  visible: false,
}));
const data = ref([...initData]);

const newData = ref({
  key: String(cnt++),
  registration_required: false,
  registration_time_range: [],
  event_time_range: [],
  count_range_of_people: [],
  seat_map_id: '',
  venue: '',
  location: '',
  visible: false,
});

const tableRef = ref();
const editableRowKeys = ref([]);
const currentSaveId = ref('');
// 保存变化过的行信息
const editMap = {};
const visibleBody = ref(false);

const onEdit = (e) => {
  const {id} = e.currentTarget.dataset;
  if (!editableRowKeys.value.includes(id)) {
    editableRowKeys.value.push(id);
  }
};

// 更新 editableRowKeys
const updateEditState = (id) => {
  const index = editableRowKeys.value.findIndex((t) => t === id);
  editableRowKeys.value.splice(index, 1);
};

const onCancel = (e) => {
  const {id} = e.currentTarget.dataset;
  updateEditState(id);
  tableRef.value.clearValidateData();
};

const onSave = (e) => {
  const {id} = e.currentTarget.dataset;
  currentSaveId.value = id;
  // 触发内部校验，而后也可在 onRowValidate 中接收异步校验结果
  tableRef.value.validateRowData(id).then((params) => {
    console.log('Event Table Promise Validate:', params);
    if (params.result.length) {
      const r = params.result[0];
      MessagePlugin.error(`${r.col.title} ${r.errorList[0].message}`);
      return;
    }
    // 如果是 table 的父组件主动触发校验
    if (params.trigger === 'parent' && !params.result.length) {
      const current = editMap[currentSaveId.value];
      if (current) {
        alert(JSON.stringify(current.editedRow))
        data.value.splice(current.rowIndex, 1, current.editedRow);
        MessagePlugin.success('保存成功');
      }
      updateEditState(currentSaveId.value);
    }
  });
};

// 行校验反馈事件，tableRef.value.validateRowData 执行结束后触发

const onDelete = (e) => {
  const {id} = e.currentTarget.dataset;
  // 删除操作
  // 例如：根据 id 找到对应的行索引，然后使用 splice 方法将其从数据中移除
  const index = data.value.findIndex((item) => item.key === id);
  if (index !== -1) {
    const temp = [...data.value];
    temp.splice(index, 1);
    data.value = temp;
    MessagePlugin.success('删除成功');
  } else {
    MessagePlugin.error('未找到要删除的行');
  }
};
const addData = ({validateResult, firstError}) => {
  if (validateResult === true) {
    data.value = [...data.value, newData.value]
    newData.value = {
      key: String(cnt++),
      registration_required: false,
      registration_time_range: [],
      event_time_range: [],
      count_range_of_people: [],
      seat_map_id: '',
      venue: '',
      location: '',
      visible: false,
    }
    visibleBody.value = false;
    MessagePlugin.success('提交成功');
  } else {
    console.log('Validate Errors: ', firstError, validateResult);
    MessagePlugin.warning(firstError);
  }
}


const perPage = ref(10);
const currentPage = ref(1);

const columns = computed(() => [
  {
    title: '序号', colKey: 'key',
    width: 60, align: 'center',
    fixed: 'left',
    // cell: (h, {rowIndex}) => {
    //   return <span>{rowIndex + 1}</span>;
    // },
  },
  {
    title: '是否需要报名', colKey: 'registration_required',
    width: 120, align: 'center',
    cell: (h, {row}) => {
      // Convert registration_required value to display as '是' or '否'
      const displayValue = row.registration_required === true ? '是' : '否';
      return <span>{displayValue}</span>;
    },
    edit: {
      component: Switch,
      props: {
        label: ['是', '否']
      },
      showEditIcon: false,
      on: ({updateEditedCellValue}) => ({
        onChange: (value) => {
          // window.alert(value.value)
          updateEditedCellValue({
            isUpdateCurrentRow: true,
            registration_required: value.value
          });
        },
      }),
    },
  },
  {
    title: '申请时间', colKey: 'registration_time_range',
    width: 400, align: 'center',
    cell: (h, {row}) => {
      let re = "无需预约"
      if (row.registration_required) {
        const displayValue = row.registration_time_range;
        re = <span>{`${displayValue[0]} 到 ${displayValue[1]}`}</span>;
      }
      return re
    },
    edit: {
      component: DateRangePicker, showEditIcon: false,
      props: {
        enableTimePicker: true
      },
    },
  },
  {
    title: '活动日期', colKey: 'event_time_range',
    width: 400, align: 'center',
    edit: {
      component: DateRangePicker, showEditIcon: false,
      props: {
        allowInput: true, enableTimePicker: true
      },
    },
    cell: (h, {row}) => {
      const displayValue = row.event_time_range;
      return <span>{`${displayValue[0]} 到 ${displayValue[1]}`}</span>
    },
  },
  {
    title: '人数范围', colKey: 'count_range_of_people',
    width: 200, align: 'center',
    cell: (h, {row}) => {
      const displayValue = row.count_range_of_people;
      return <span>{`${displayValue[0]} ~ ${displayValue[1]}`}</span>
    },
    edit: {
      component: RangeInput, showEditIcon: false,
    },
  },
  {
    title: '座位', colKey: 'seat_map_id',
    width: 100, align: 'center',
    edit: {
      component: Input, showEditIcon: false,
    },
  },
  {
    title: '地点', colKey: 'venue',
    width: 100, align: 'center',
    edit: {
      component: Input, showEditIcon: false,
    },
  },
  {
    title: '地图', colKey: 'location',
    width: 100, align: 'center',
    edit: {
      component: Input, showEditIcon: false,
    },
  },
  {
    title: '是否可见', colKey: 'visible',
    align: 'center',
    cell: (h, {row}) => {
      // Convert visible required value to display as '是' or '否'
      const displayValue = row.visible === true ? '是' : '否';
      return <span>{displayValue}</span>;
    },
    edit: {
      component: Switch, showEditIcon: false,
      props: {
        label: ['是', '否']
      },
      on: ({updateEditedCellValue}) => ({
        onChange: (value) => {
          // window.alert(value.value)
          updateEditedCellValue({
            isUpdateCurrentRow: true,
            visible: value.value
          });
        },
      }),
    },
  },
  {
    title: '操作栏',
    colKey: 'operate',
    width: 150, align: 'center',
    fixed: 'right',
    cell: (h, {row}) => {
      const editable = editableRowKeys.value.includes(row.key);
      return (
          <div class="table-operations">
            {!editable && (
                <div class="operations">
                  <t-link theme="primary" hover="color" data-id={row.key} onClick={onEdit}>编辑</t-link>
                  <t-link theme="danger" hover="color" data-id={row.key} onClick={onDelete}>删除</t-link>
                </div>
            )}
            {editable && (
                <div class="operations">
                  <t-link theme="primary" hover="color" data-id={row.key} onClick={onSave}>保存</t-link>
                  <t-link theme="primary" hover="color" data-id={row.key} onClick={onCancel}>取消</t-link>
                </div>
            )}
          </div>
      );
    },
  },
]);

const count_range_of_peopleValidator = (val) => {
  // 将输入的字符串转化为数字
  const [first, second] = val.map(Number);

  if (isNaN(first) || first <= 0) {
    return {result: false, message: '最小值应该为正数', type: 'error'};
  }
  if (isNaN(second) || second <= 0) {
    return {result: false, message: '最大值应该为正数', type: 'error'};
  }

  // 检查第二个数字是否大于等于第一个数字
  if (second < first) {
    return {result: false, message: '最大值必须大于等于最小值', type: 'error'};
  }

  return {result: true};
};

const FORM_RULES = ref({
  registration_time_range: [{required: computed(() => newData.value.registration_required), message: '报名时间必填'}],
  event_time_range: [{required: true, message: '活动时间必填'}],
  count_range_of_people: [
    {required: true, message: '人数必填'},
    {validator: count_range_of_peopleValidator},
  ],
  venue: [{required: true, message: '地址必填'}],
  position: [{required: true, message: '地址必填'}],

});
const onReset = () => {
  MessagePlugin.success('重置成功');
};

</script>

<style lang="css">
.operations {
  display: flex;
  justify-content: space-around;
  gap: 10px;
}
</style>
