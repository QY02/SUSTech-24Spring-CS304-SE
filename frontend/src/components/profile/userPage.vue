<template>
    <UserInformation></UserInformation>
</template>



<script setup>
import UserInformation from "@/components/profile/userInformation.vue";
</script>



<style>
.inform {
    padding: 20px;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    /* 控制元素之间的间距 */
}

.px-2 {
    font-size: large;
    font-weight: bolder;
}

.github-login-button {
    border-radius: 10px;
    box-sizing: border-box;
    padding: 5px 10px;
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 20px;
    gap: 5px;
    border: 2px solid #747474;
}

.personalInformationForm {
    display: flex;
    flex-direction: column;
    gap: 6px;
}
</style>