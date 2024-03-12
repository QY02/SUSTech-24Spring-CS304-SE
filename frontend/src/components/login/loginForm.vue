<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="login">
      <label for="username">Username:</label>
      <input type="text" id="username" v-model="username" required>
      <br>
      <label for="password">Password:</label>
      <input type="password" id="password" v-model="password" required>
      <br>
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script>
import {inject, ref} from 'vue';
// import router from "@/routers";

export default {
  setup() {
    const username = ref('');
    const password = ref('');

    // 使用 inject 获取全局变量 $userRole
    const userRole = inject('$userRole');

    const login = async () => {
      // 在这里执行登录逻辑
      // 登录成功后，从后端获取用户角色，然后将其存储到全局变量
      // 这个示例假设从后端获取到的用户角色是 'teacher'
      const response = await fetch('/api/login', {
        method: 'POST',
        body: JSON.stringify({username: username.value, password: password.value}),
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        const data = await response.json();
        // 将从后端获取的用户角色存储到全局变量 $userRole
        userRole.value = data.userRole;

        // 导航到其他页面或执行其他操作
        // await router.push('/homepage')
      } else {
        // 处理登录失败的情况
        console.error('Login failed');
      }
    };

    return {
      username,
      password,
      login,
      userRole, // 返回用户角色，以便在模板中使用
    };
  },
};
</script>


<style>
/* 样式可以根据需要自行定义 */
</style>
