<template>

  <!--  Form 整体 ----------------------------------------------------------------------------->
  <div style="display: flex;flex-direction: column; margin-left: 10px">

    <!--    表头 ---------------------------------------------------------------------------------->
    <div style="display: flex;justify-content: space-between;margin: 10px;align-items: center">
      <t-button @click="showAdd">
        <template #icon>
          <add-icon/>
        </template>
        添加新用户
      </t-button>
      <va-input
          v-model.number="perPage"
          type="number"
          placeholder="Items..."
          label="每页展示个数"
      />

      <va-input
          v-model.number="currentPage"
          type="number"
          placeholder="Page..."
          label="当前页面"
      />
      <va-input
          v-model="filter"
          class="sm:col-span-2 md:col-span-3"
          placeholder="搜索..."
      />

    </div>

    <!--    表格  ------------------------------------------------------------------>
    <va-data-table
        class="table-crud"
        :items="items"
        :columns="columns"
        :per-page="perPage"
        :current-page="currentPage"
        striped
        selectable
        :row-bind="getRowBind"
        v-model="selectedList"
        :filter="filter"
        @filtered="filtered = $event.items"
    >
      <template #cell(type)="{ value }">
        <div v-if="value==='0'||value==='管理员'">
          {{ '管理员' }}
        </div>
        <div v-else-if="value==='1'||value==='普通用户'">
          {{ '普通用户' }}
        </div>
      </template>
      <template #cell(actions)="{ rowIndex }">
        <va-button
            preset="plain"
            icon="edit"
            @click="openModalToEditItemById(rowIndex)"
        />
        <va-button
            v-if="items[rowIndex].id===userId"
            preset="plain"
            icon="delete"
            class="ml-3"
            color="danger"
            disabled
        />
        <va-button
            v-else
            preset="plain"
            icon="delete"
            class="ml-3"
            color="danger"
            @click="deleteItemById(rowIndex)"
        />
      </template>

      <template #bodyAppend>
        <tr>
          <td colspan="7">
            <div style="display: flex;
                             justify-content: center;
                              margin-top: 7px">
              <va-pagination v-model="currentPage" :pages="pages"/>
            </div>
          </td>
        </tr>
      </template>
    </va-data-table>
    <!--    修改数据的modal 不用修改-->

    <el-dialog
        class="modal-crud"
        :model-value="!!isEditing"
        title="编辑用户"

        size="small"
        hide-default-actions
        :before-close="resetEditedItem"
        destroy-on-close
    >
      <va-form
          ref="formRef2"
      >
        <va-input
            v-model="editedItem['name']"
            class="my-6"
            label="姓名"
        />
        <va-input
            v-model="editedItem['password']"
            required-mark
            :rules="[(v) => /^[a-zA-Z0-9/]{8,}$/.test(v) || v === '********' || 'Only numbers, characters and slash are allowed, minimum length is 8']"
            class="my-6"
            label="密码"
        />
        <va-input
            v-model="editedItem['email']"
            class="my-6"
            label="邮箱"
        />
        <va-input
            v-model="editedItem['phoneNumber']"
            :rules="[(v) => v===null||/^(\d{11})?$/.test(v) || 'Phone number must be 11 digits']"
            class="my-6"
            label="电话"
        />
        <va-input
            v-model="editedItem['department']"
            class="my-6"
            label="学院"
        />
      </va-form>

      <template #footer>
        <va-button
            @click="resetEditedItem()"
            style="margin-right: 10px"
            color="BackgroundPrimary"
        >取消
        </va-button>
        <va-button
            :disabled="!isValidedit"
            @click="editItem"
        >
          确认
        </va-button>
      </template>
    </el-dialog>

    <el-dialog
        class="modal-crud"
        :model-value="!!isAdding"
        title="添加用户"
        size="small"

        hide-default-actions
        :before-close="resetCreatedItem"
        destroy-on-close
    >
      <va-form ref="formRef1">
        <va-input
            v-model="createdItem['id']"
            required-mark
            :rules="[(v) => /^[0-9]{8}$/.test(v) || 'ID must be an .,lp-digit number']"
            class="my-6"
            label="ID"
        />
        <va-input
            v-model="createdItem['name']"
            class="my-6"
            label="姓名"
        />
        <va-input
            v-model="createdItem['password']"
            required-mark
            :rules="[(v) => /^[a-zA-Z0-9/]{8,}$/.test(v)  || 'Only numbers, characters and slash are allowed, minimum length is 8']"
            class="my-6"
            label="密码"
        />
        <va-input
            v-model="createdItem['email']"
            class="my-6"
            label="邮箱"
        />
        <va-input
            v-model="createdItem['phoneNumber']"
            :rules="[(v) => v===null||/^(\d{11})?$/.test(v) || 'Phone number must be 11 digits']"
            class="my-6"
            label="电话"
        />
        <va-select
            v-model="createdItem['type']"
            label="用户类型"
            :options="optionsState"

        />
        <va-input
            v-model="createdItem['department']"
            class="my-6"
            label="学院"
        />
      </va-form>
      <template #footer>
        <va-button
            @click="resetCreatedItem()"
            style="margin-right: 10px"
            color="BackgroundPrimary"
        >
          取消
        </va-button>
        <va-button
            :disabled="!isValidadd"
            @click="addNewItem"
        >
          确认
        </va-button>
      </template>

    </el-dialog>

    <div style="height: 100px;"></div>
  </div>

  <!--最下面的alert ---------------------------------------------------------------->

  <t-button theme="danger" @click="onButtonClickDelete" :disabled="selectedList.length===0"
            style="position: fixed;right: 60px;transform: translate(45%, -25%);">删除所选用户
  </t-button>


</template>

<script setup>
import {ref, onMounted, computed, watch} from 'vue';
import axios from "axios";
import {useToast, useModal, useForm, useColors} from "vuestic-ui";
import {AddIcon} from "tdesign-icons-vue-next";

const token = sessionStorage.getItem('token')

const {isValid: isValidadd, validate: validateadd} = useForm('formRef1')
const {isValid: isValidedit, validate: validateedit} = useForm('formRef2')

const {confirm} = useModal()
const {init} = useToast();

const items = ref([]);
const columns = [
  // {key: 'try'},
  {key: 'id', sortable: true},
  {key: 'name', label: "名字", sortable: true},
  {key: 'email', label: "邮箱", sortable: true},
  {key: 'phoneNumber', label: "电话", sortable: true},
  {key: 'type', label: "用户类型", sortable: true},
  {key: 'department', label: "学院", width: 80},
  {key: 'actions', label: "操作", width: 80},
];
const defaultItem = {
  id: '',
  name: '',
  password: '12345678',
  email: '',
  phoneNumber: '',
  type: '普通用户',
  department: '',
};

const isEditing = ref(null);
const nowEditType = ref(null);
const nowEditName = ref(null);
const nowEditEmail = ref(null);
const nowEditPhone = ref(null);
const nowEditDep = ref(null);
const isAdding = ref(null);
const editedItem = ref(null);
const createdItem = ref({...defaultItem});
const selectedList = ref([]);
const showModalEdit = ref(false);
const showModelChangeRole = ref(false);
const filter = ref("");
const filtered = ref("");
const perPage = ref(10);
const currentPage = ref(1);
const optionsState = ref(["Student", "Teacher", "Admin"]);
const selectState = ref('Student');
const userId = ref(null);

const resetEditedItem = () => {//恢复初始值，设置弹窗不显示
  editedItem.value.type = nowEditType.value
  editedItem.value.name = nowEditName.value
  editedItem.value.email = nowEditEmail.value
  editedItem.value.phoneNumber = nowEditPhone.value
  editedItem.value.department = nowEditDep.value
  isEditing.value = null;
  // alert(nowEditType.value)

  // editedItem.value=null;
};
const resetChangeRole = () => {//恢复初始值，设置弹窗不显示
  showModelChangeRole.value = false
};
const resetCreatedItem = () => {//恢复初始值，设置弹窗不显示
  createdItem.value = {...defaultItem};
  isAdding.value = null
};
const deleteItemById = async (id) => {//单删
  const result = await confirm({
    title: "Are you sure to delete?",
    okText: "Yes",
    cancelText: "No",
  });
  if (result) {//暂时还不能用
    // alert(items.value[id].id)
    axios.post(`/user/delete/${items.value[id].id}`, {}, {
      headers: {
        token: token,
      },
    }).then(() => {
      init({message: "Successfully deleted!!!", color: "success"});
      location.reload()
    }).catch();
  }


};
const getRowBind = (row) => {
  if (row.type !== "1") {
    return {
      class: ["custom-class"]
    };
  }
};
const addNewItem = () => {//add 确认弹窗点击确定后执行的操作
  // alert(JSON.stringify(createdItem.value))
  if (validateadd()) {
    if (createdItem.value.type === 'Student') {
      createdItem.value.type = 2
    } else if (createdItem.value.type === 'Teacher') {
      createdItem.value.type = 1
    } else if (createdItem.value.type === 'Admin') {
      createdItem.value.type = 0
    } else {
      createdItem.value.type = -1
    }
    axios.post("/user/add", createdItem.value, {
      headers: {
        token: token,
      },
    }).then(() => {
      init({message: "Successfully created!!!", color: "success"});
      location.reload()
    }).catch();
  }
};

const editItem = () => {
  if (validateedit()) {
    if (editedItem.value.password === '********') {
      axios.put("/user/update/admin", {
        id: editedItem.value.id,
        name: editedItem.value.name,
        type: editedItem.value.type,
        email: editedItem.value.email,
        phoneNumber: editedItem.value.phoneNumber,
        department: editedItem.value.department,

      }, {
        headers: {
          token: token,
        },
      }).then(() => {
        init({message: "Successfully updated!", color: "success"});
        location.reload()
      }).catch();
    } else {
      // alert(JSON.stringify(editedItem.value.name))
      // alert(JSON.stringify(editedItemId.value))
      axios.put("/user/update/admin", {
        id: editedItem.value.id,
        name: editedItem.value.name,
        type: editedItem.value.type,
        password: editedItem.value.password,
        email: editedItem.value.email,
        phoneNumber: editedItem.value.phoneNumber,
        department: editedItem.value.department,

      }, {
        headers: {
          token: token,
        },
      }).then(() => {
        init({message: "Successfully updated!", color: "success"});
        location.reload()
      }).catch();
    }
  }
};

const openModalToEditItemById = index => {
  // alert(JSON.stringify(items.value[index]))
  editedItem.value = items.value[index];
  isEditing.value = 222;
  editedItem.value.password = '********'
  nowEditType.value = editedItem.value.type;
  nowEditName.value = editedItem.value.name;
  nowEditEmail.value = editedItem.value.email;
  nowEditPhone.value = editedItem.value.phoneNumber;
  nowEditDep.value = editedItem.value.department;
};


const getAllUsers = () => {//拿到初始数据进行展示
  userId.value = sessionStorage.getItem('uid')
  // alert(userId.value)
  axios.post("/user/getAll", {}, {
    headers: {
      token: token,
    },
  })
      .then(response => {
        items.value = response.data.data
        // alert(JSON.stringify(response.data.data))
      }).catch();
};

const onButtonClickDelete = async () => {//批量删除所选items
  const result = await confirm({
    message: "After submitting, the selected items will be deleted.",
    title: "Are you sure to submit?",
    okText: "Yes",
    cancelText: "No",
  });

  if (result) {//暂时不能用
    init({message: "Submitting...", color: "success"});

    axios.post("/user/delete/batch",
        selectedList.value
            .filter(item => item.id !== userId.value)
            .map(item => item.id),
        {
          headers: {
            token: token,
          },
        }
    ).then(() => {
      init({message: "Successfully updated!!!", color: "success"});
      location.reload()
    }).catch();
  }
};
const showAdd = () => {
  isAdding.value = 111;

}
const showChangeRole = () => {
  showModelChangeRole.value = !showModelChangeRole.value
}
const showEdit = () => {//没用到的一个方法，不用管
  showModalEdit.value = !showModalEdit.value

}

const pages = computed(() => {
  return perPage.value && perPage.value !== 0
      ? Math.ceil(filtered.value.length / perPage.value)
      : filtered.value.length;
});

watch([perPage, filtered], () => {
      pages.value = perPage.value && perPage.value !== 0
          ? Math.ceil(filtered.value.length / perPage.value)
          : filtered.value.length;
    }
)
;
onMounted(() => {
  getAllUsers();
});
</script>

<style lang="scss" scoped>
.table-crud {
  --va-form-element-default-width: 0;

  .va-input {
    display: block;
  }

  &__slot {
    th {
      vertical-align: middle;
    }
  }
}

.modal-crud {
  .va-input {
    display: block;
  }
}

//::v-deep(.custom-class) .va-data-table {
//  pointer-events: none;
//  background-color: var(--va-background-element);
//}
</style>