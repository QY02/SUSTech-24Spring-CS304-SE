<script setup>
import { ref, reactive } from 'vue';
import axios from "axios";

const commentsData = ref([]);
const eventId = sessionStorage.getItem('eventId')
const token = sessionStorage.getItem('token')
const uid = sessionStorage.getItem('uid')
const commentForm  = reactive({
    title: '',
    content: '',
    rate : 0,
})




const getComment = () => {
    axios.get(`/comment/getById`, {
        "eventId": eventId,
        "type": 0,
    }, {
        headers: {
            token: token
        }
    }).then((response) => {
        commentsData.value = response.data.data
    }).catch(() => { })
}
getComment();

// 没完全实现。。好难啊。。。
const addComment = () => {
    axios.get(`/comment/add`, {
        "eventId": eventId,
        "publisherId": uid,
        "score": commentForm.score,
        "title": commentForm.title,
        "content": commentForm.content,
    }, {
        headers: {
            token: token
        }
    }).then((response) => {
    }).catch(() => { })
} 

const deleteComment = (commentId) => {
    axios.get(`/comment/delete/${commentId}`, {
        "eventId": eventId,
        "publisherId": uid
    }, {
        headers: {
            token: token
        }
    }).then((response) => {
    }).catch(() => { })
}

</script>

<template>
    <t-space style="display: flex; width: 100%;">
        <div>
            <div class="title">REVIEWS</div>
            <div class="line"></div>
        </div>
        <div style="margin-top: 30px; display: flex; justify-content: flex-end; margin-right:30px ;">
            <t-button>评论</t-button>
        </div>
    </t-space>
    <div>
        <t-list :split="true">
            <t-list-item v-for="(item, index) in commentsData" :key="index">
                <template #content>
                    <div class="comment_card">
                        <div class="stars"><t-rate :default-value="item.score" allow-half disabled size="16" /></div>
                        <div class="comment_infos">
                            <p class="date-time">
                                {{ item.publishDate }}
                            </p>
                            <p class="description">
                                {{ item.content }}
                            </p>
                        </div>
                        <div class="author">
                            — {{ item.publisherId }}
                        </div>
                    </div>
                </template>
            </t-list-item>
        </t-list>
    </div>
    <div style="height: 40px;"></div>
</template>