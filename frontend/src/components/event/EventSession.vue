<template>
  <div class="t-table-demo__editable-row" style="width: 100%">
    <div>
      <t-button @click="()=>visibleBody=true">
        <template #icon>
          <add-icon/>
        </template>
        添加新场次
      </t-button>
    </div>
    <br/>
    <t-table
        ref="tableRef"
        row-key="key"
        :columns="columns"
        :rules="FORM_RULES"
        :data="data"
        stripe:true
        :editable-row-keys="editableRowKeys"
        bordered:true
        :pagination="pagination"
        lazy-load:true
        @row-edit="onRowEdit"
        @row-validate="onRowValidate"
        @validate="onValidate"
    >
      <template #operation="{ row }">
        <div class="table-operations">
          <div v-if="!editableRowKeys.find(k => k === row.key)" class="operations">
            <t-link theme="primary" hover="color" :data-id="row.key" @click="onEdit(row.key)">编辑</t-link>
            <t-link theme="danger" hover="color" :data-id="row.key" @click="onDelete(row.key)">删除</t-link>
          </div>
          <div v-else class="operations">
            <t-link theme="primary" hover="color" :data-id="row.key" @click="onSave(row.key)">保存</t-link>
            <t-link theme="primary" hover="color" :data-id="row.key" @click="onCancel(row.key)">取消</t-link>
          </div>
        </div>
      </template>

    </t-table>
  </div>
  <t-dialog
      v-model:visible="visibleBody"
      header="请填写场次信息"
      destroy-on-close:true
      placement="center"
      width="45%"
      :cancel-btn=null
      :confirm-btn=null
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
            label-width="200px"
            label-align="right"
        >
          <t-form-item label="是否需要报名" name="registration_required">
            <t-switch v-model="newData.registration_required" :label="['是', '否']"></t-switch>
          </t-form-item>
          <t-form-item label="报名开始时间-报名结束时间" name="registration_time_range">
            <t-date-range-picker enable-time-picker=true allow-input=true clearable=true
                                 v-model="newData.registration_time_range"
                                 :disabled="!newData.registration_required"
                                 :presets="presets"
            />
          </t-form-item>
          <t-form-item label="开始时间-结束时间" name="event_time_range">
            <t-date-range-picker enable-time-picker=true allow-input=true clearable=true
                                 v-model="newData.event_time_range"
                                 :presets="presets"
            />
          </t-form-item>

          <t-form-item label="人数" name="count_range_of_people">
            <t-range-input v-model="newData.count_range_of_people" :placeholder="['最小值','最大值']"/>
          </t-form-item>
          <t-form-item label="地址" name="venue">
            <t-input v-model="newData.venue">地址</t-input>
          </t-form-item>
          <t-form-item label="地图" name="location">
            <t-button @click="showChooseLocationDialog">
              <template #icon>
                <MapInformation2Icon/>
              </template>
              {{ newData.location === null ? "选择位置" : `${newData.location[0]}, ${newData.location[1]}` }}
            </t-button>
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
  <t-dialog v-model:visible="chooseLocationDialogVisible" placement="center" width="50vw" header="选择位置"
            :onConfirm="handleChooseLocationConfirm" :onClose="handleChooseLocationCancel">
    <div id="mapContainer" class="choose-location-map-div">
      <div class="search-place-div">
        <input class="search-place-input" v-model="searchPlaceInputValue" placeholder="搜索地点" id="searchPlaceInput"></input>
        <div class="search-place-result-div" id="searchPlaceResultDiv"></div>
      </div>
    </div>
  </t-dialog>
</template>

<script setup>
import {computed, onUnmounted, ref, watch} from 'vue';
import {DateRangePicker, Input, MessagePlugin, NotifyPlugin, RangeInput, Switch} from 'tdesign-vue-next';
import {AddIcon, MapInformation2Icon} from "tdesign-icons-vue-next";
import {useVModel} from "@vueuse/core";
import dayjs from "dayjs";
import {AMap} from "@/main";

const chooseLocationDialogVisible = ref(false);
let map = null;
let mapScale = null;
let mapToolBar = null;
let mapControlBar = null;
let mapType = null;
let mapMarker = null;
const searchPlaceInputValue = ref("");
let mapAutoComplete = null;
let mapPlaceSearch = null;

const showChooseLocationDialog = () => {
  if (AMap.value !== null) {
    chooseLocationDialogVisible.value = true;
    if (map === null) {
      map = new AMap.value.Map("mapContainer", {
        viewMode: "3D",
        zoom: 17,
        center: newData.value.location === null ? [113.997, 22.596] : newData.value.location,
      });
      mapScale = new AMap.value.Scale();
      mapToolBar = new AMap.value.ToolBar({
        position: {
          top: '110px',
          right: '40px'
        }
      });
      mapControlBar = new AMap.value.ControlBar({
        position: {
          top: '10px',
          right: '10px',
        }
      });
      mapType = new AMap.value.MapType({
        defaultType: 0,
        position: {
          bottom: '110px',
          right: '10px',
        }
      });
      map.addControl(mapScale);
      map.addControl(mapToolBar);
      map.addControl(mapControlBar);
      map.addControl(mapType);
      mapMarker = new AMap.value.Marker({
        icon: "https://a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
        position: map.getCenter(),
        offset: new AMap.value.Pixel(-11, -35),
        clickable: false,
        draggable: true
      });
      mapMarker.on("dragend", function () {
        setMapMarkerPosition();
      });
      mapAutoComplete = new AMap.value.AutoComplete({
        input: "searchPlaceInput",
        output: "searchPlaceResultDiv"
      });
      mapPlaceSearch = new AMap.value.PlaceSearch({
        map: map
      });
      mapPlaceSearch.on("markerClick", function (e) {
        setMapMarkerPosition(e.marker.getPosition());
      });
      mapAutoComplete.on("select", function (e) {
        mapPlaceSearch.setCity(e.poi.adcode);
        mapPlaceSearch.search(e.poi.name);
      });
      map.on('complete', function () {
        map.add(mapMarker);
        mapMarker.setLabel({
          content: `${mapMarker.getPosition().getLng()}, ${mapMarker.getPosition().getLat()}`,
          direction: "top",
          offset: new AMap.value.Pixel(-13, -8)
        });
        map.on('click', function (e) {
          setMapMarkerPosition(e.lnglat);
        });
        map.on('moveend', () => {
          mapMarker.setLabel({
            direction: "top",
            offset: new AMap.value.Pixel(-13, -8)
          });
        });
      });
    } else {
      map.setZoomAndCenter(17, newData.value.location === null ? [113.997, 22.596] : newData.value.location);
      mapMarker.setPosition(newData.value.location === null ? [113.997, 22.596] : newData.value.location);
      mapMarker.setLabel({
        content: `${mapMarker.getPosition().getLng()}, ${mapMarker.getPosition().getLat()}`
      });
    }
  } else {
    NotifyPlugin.info({title: "地图模块加载中，请稍后"})
  }
}

const setMapMarkerPosition = (position) => {
  if (position) {
    mapMarker.setPosition(position);
  }
  mapMarker.setLabel({
    content: `${mapMarker.getPosition().getLng()}, ${mapMarker.getPosition().getLat()}`,
  });
  setTimeout(() => {
    mapMarker.setLabel({
      direction: "top",
      offset: new AMap.value.Pixel(-13, -8)
    });
  }, 0);
}

onUnmounted(() => {
  map?.destroy();
});

const handleChooseLocationConfirm = () => {
  const currentMarkerPosition = mapMarker.getPosition();
  if (currentMarkerPosition !== null) {
    newData.value.location = currentMarkerPosition.toArray();
  }
  chooseLocationDialogVisible.value = false;
  mapPlaceSearch.clear();
  searchPlaceInputValue.value = "";
}

const handleChooseLocationCancel = () => {
  mapPlaceSearch.clear();
  searchPlaceInputValue.value = "";
}

const props = defineProps({
  sessionData: Array
})
const emit = defineEmits(['update:sessionData'])

const data = useVModel(props, 'sessionData', emit)
watch(data, () => {
  data.value.forEach((item, index) => {
    // 将每个元素的 key 属性设置为当前索引加一
    item.key = index + 1;
  });
})
const presets = ref({
  最近7天: [dayjs().subtract(6, 'day').toDate(), dayjs().toDate()],
  最近3天: [dayjs().subtract(2, 'day').toDate(), dayjs().toDate()],
  今天: [dayjs().toDate(), dayjs().toDate()],
});
const newData = ref({
  key: String(data.value.length),
  registration_required: false,
  registration_time_range: [],
  event_time_range: [],
  count_range_of_people: [0,5],
  seat_map_id: '',
  venue: '',
  location: null,
  visible: false,
});

const tableRef = ref();
const editableRowKeys = ref([]);
const currentSaveId = ref('');
// 保存变化过的行信息
const editMap = {};
const visibleBody = ref(false);
const pagination = computed(() => ({
  defaultCurrent: 1,
  defaultPageSize: 5,
  total: data.value.length
}));

const onEdit = (id) => {
  if (!editableRowKeys.value.find(k => k === id)) {
    editableRowKeys.value.push(id);
  }
};

// 更新 editableRowKeys
const updateEditState = (id) => {
  const index = editableRowKeys.value.findIndex((t) => t === id);
  editableRowKeys.value.splice(index, 1);
};

const onCancel = (id) => {
  updateEditState(id);
  tableRef.value.clearValidateData();
};

const onSave = (id) => {
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
const onRowValidate = (params) => {
  console.log('Event Table Row Validate:', params);
};

const onDelete = (id) => {
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
      key: String(data.value.length),
      registration_required: false,
      registration_time_range: [],
      event_time_range: [],
      count_range_of_people: [0,5],
      seat_map_id: '',
      venue: '',
      location: null,
      visible: false,
    }
    visibleBody.value = false;
    MessagePlugin.success('提交成功');
  } else {
    console.log('Validate Errors: ', firstError, validateResult);
    MessagePlugin.warning(firstError);
  }
}

function onValidate(params) {
  console.log('Event Table Data Validate:', params);
}

const onRowEdit = (params) => {
  const {row, col, value} = params;
  const oldRowData = editMap[row.key]?.editedRow || row;
  const editedRow = {...oldRowData, [col.colKey]: value};
  editMap[row.key] = {
    ...params,
    editedRow,
  };
};


const columns = computed(() => [
  {
    title: '序号', colKey: 'key',
    width: 60, align: 'center',
    fixed: 'left',
    cell: (h, {rowIndex}) => {
      return rowIndex + 1;
    },
  },
  {
    title: '是否需要报名', colKey: 'registration_required',
    width: 120, align: 'center',
    cell: (h, {row}) => row.registration_required === true ? '是' : '否',
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
    title: '报名时间', colKey: 'registration_time_range',
    width: 330, align: 'center',
    cell: (h, {row}) => {
      let re = "无需预约"
      if (row.registration_required) {
        const displayValue = row.registration_time_range;
        re = `${displayValue[0]} ~ ${displayValue[1]}`;
      }
      return re
    },
    edit: {
      component: DateRangePicker, showEditIcon: false,
      props: {
        enableTimePicker: true
      },
    },
    rules: FORM_RULES.value.registration_time_range
  },
  {
    title: '活动时间', colKey: 'event_time_range',
    width: 330, align: 'center',
    edit: {
      component: DateRangePicker, showEditIcon: false,
      props: {
        allowInput: true, enableTimePicker: true
      },
    },
    rules: FORM_RULES.value.event_time_range,
    cell: (h, {row}) => {
      const displayValue = row.event_time_range;
      return `${displayValue[0]} ~ ${displayValue[1]}`
    },
  },
  {
    title: '人数范围', colKey: 'count_range_of_people',
    width: 130, align: 'center',
    cell: (h, {row}) => {
      const displayValue = row.count_range_of_people;
      return `${displayValue[0]} ~ ${displayValue[1]}`
    },
    edit: {
      component: RangeInput, showEditIcon: false,
    },
    rules: FORM_RULES.value.count_range_of_people,
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
    ellipsis: true,
    edit: {
      component: Input, showEditIcon: false,
    },
    rules: FORM_RULES.value.venue,
  },
  {
    title: '地图', colKey: 'location',
    width: 100, align: 'center',
    edit: {
      component: Input, showEditIcon: false,
    },
    rules: FORM_RULES.value.location,
  },
  {
    title: '是否可见', colKey: 'visible',
    align: 'center',
    cell: (h, {row}) => row.visible === true ? '是' : '否',
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
    colKey: 'operation',
    width: 150, fixed: "right"
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
  location: [{required: true, message: '地址必填'}],

});
const onReset = () => {
  newData.value.location = null;
  MessagePlugin.success('重置成功');
};

</script>
<style lang="css">
.operations {
  display: flex;
  //justify-content: space-around;
  gap: 10px;
}

.choose-location-map-div {
  display: flex;
  height: 50vh;
}

.search-place-div {
  position: relative;
  top: 10px;
  left: 10px;
  z-index: 99999;
}

.search-place-input {
  width: 150px;
  height: 32px;
  border-width: 1px;
  border-style: solid;
  border-radius: 3px;
  border-color: rgba(255, 255, 255, 0);
  box-shadow: 0 0 4px 1px rgba(0, 0, 0, .2);
  padding: 0 8px;
  background-color: #fff;
  outline: none;
  color: rgba(0, 0, 0, 0.9);
  font: 14px / 22px PingFang SC, Microsoft YaHei, Arial Regula;
  box-sizing: border-box;
  transition: border cubic-bezier(0.38, 0, 0.24, 1) 0.2s, box-shadow cubic-bezier(0.38, 0, 0.24, 1) 0.2s;
}

.search-place-input:hover {
  border-color: #0052d9;
}

.search-place-input:focus {
  z-index: 1;
  border-color: #0052d9;
  box-shadow: 0 0 0 2px #d9e1ff;
}

.search-place-result-div {
  position: relative;
  top: 3px;
  background-color: #fff;
  border-radius: 3px;
  box-shadow: 0 0 4px 1px rgba(0, 0, 0, .2);
}
</style>
