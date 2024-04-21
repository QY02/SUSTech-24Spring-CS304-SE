<template>

<div class="card">
  <div class="card-1">
    <t-image
      src="https://tdesign.gtimg.com/demo/demo-image-1.png"
      fit="fill"
      :style="{ width: '100%', height: '100%' }"
      shape="round"
    />
  </div>
  <div class="right">
    <div class="card-2">
      <h2>标题</h2>
    </div>
    <div class="card-3">
      it's me hi
      {{ infoMessage }}
    </div>
    <div class="bottom">
    <div class="card-4">
    <t-space size="24px">
      <t-button shape="circle" theme="primary" @click="navigateToTab('review')">
        <template #icon> <HeartIcon /></template>
      </t-button>
      <t-button shape="circle" theme="primary">
        <template #icon> <HeartFilledIcon /></template>
      </t-button>
    </t-space>
      </div>
      <div class="card-4">
        <t-button  theme="primary">
            <template #icon><StarFilledIcon /></template>
            4.5
          </t-button>
      </div>
    </div>
  </div>
</div>




  <el-affix :offset="55">
    <t-tabs :value="value_lable" size="large" @change="onTabChange" :affix-props="{ offsetTop: 150 }">
            <t-tab-panel value="events" label="EVENTS"></t-tab-panel>
            <t-tab-panel value="about" label="EVENT DETAILS"></t-tab-panel>
            <t-tab-panel value="price" label="TICKET PRICING"></t-tab-panel>
            <t-tab-panel value="policy" label="ADMISSION POLICY"></t-tab-panel>
            <t-tab-panel value="gallery" label="GALLERY"></t-tab-panel>
            <t-tab-panel value="reviews" label="REVIEWS"></t-tab-panel>
    </t-tabs>
  </el-affix>
  <div :id="`${path}#events`" style="height: 60px;"></div>

  <t-space style="display: flex; width: 100%; margin-left: 24px;">
      <div>
        <div class="title">EVENTS</div>
        <div class="line"></div>
      </div>
      <div style="margin-top: 30px; display: flex; justify-content: flex-end; margin-right:79px ;">
          <div v-if="show_event_type">
            <t-space>
              <t-button theme="primary" shape="square" variant="base" @click="onClickEventType(true)">
                <list-icon slot="icon" />
              </t-button>
              <t-button shape="square" variant="outline" @click="onClickEventType(false)">
                <table-icon slot="icon" />
              </t-button>
            </t-space>
          </div>
          <div v-else>
            <t-space>
              <t-button shape="square" variant="outline" @click="onClickEventType(true)">
                <list-icon slot="icon" />
              </t-button>
              <t-button  theme="primary" shape="square" variant="base" @click="onClickEventType(false)">
                <table-icon slot="icon" />
              </t-button>
            </t-space>
          </div>
      </div>
  </t-space>
  <el-card style="height: auto; padding: 5px;  max-width: 100% ; margin-right: 30px; margin-left: 30px; margin-bottom: 40px;">
    <div v-if="show_event_type" style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
      <t-space direction="vertical" style="margin-top: 20px; ">
        <t-collapse style="width: 50vw">
        <t-collapse-panel v-for="(session, index) in sessionInformation"
                          :header="`${dateToString(session.startTime)} - ${dateToString(session.endTime)} ${session.venue}`">
          <div class="choose-session-detail-div">
            <p v-if="session.registrationRequired" class="choose-session-detail-text">
              {{
                `报名时间: ${dateToString(session.registrationStartTime)} - ${dateToString(session.registrationEndTime)}`
              }}</p>
            <p v-else class="choose-session-detail-text">无需报名</p>
            <p class="choose-session-detail-text">{{ `人数限制: ${session.minSize} - ${session.maxSize}` }}</p>
          </div>
        </t-collapse-panel>
      </t-collapse>
      </t-space>
      <br>
      <t-button @click="pushRouter('book')">前往报名</t-button>

      <!-- <EventTicketTable></EventTicketTable> -->
    </div>
    <div v-else>
      <EventTicketCalender></EventTicketCalender>
    </div>
  </el-card>
  <div style="height: 20px;"></div>
  <div :id="`${path}#about`" style="height: 36px;"></div>
  <div class="card" style="flex-direction: column;">
    <t-space style="display: flex; width: 100%;">
      <div>
          <div class="title">EVENT DETAILS</div>
          <div class="line"></div>
        </div>
    </t-space>
    <div style="margin-left: 25px;margin-right: 25px; margin-bottom: 40px;">
      介绍部分
      Taylor Swift in Concert
        
        She is, quite simply, a global superstar.
          
        Taylor Swift is a seven-time GRAMMY winner, and the youngest recipient in history of the music industry’s highest honor, the GRAMMY Award for Album of the Year. She is the only female artist in music history (and just the fourth artist ever) to twice have an album hit the 1 million first-week sales figure (2010’s Speak Now and 2012’s RED). She’s a household name whose insanely catchy yet deeply personal self-penned songs transcend music genres, and a savvy businesswoman who has built a childhood dream into an empire.
          
        But the numbers don’t tell Taylor's story half as well as she could. After all, it’s the intangibles that elevate Swift into the stratosphere of our pop culture planet, allowing the 24-year old singer-songwriter to orbit in a more rarified air. Her large-scale charitable contributions are one thing, but it’s in the small gestures – the notes of compassion she posts on the Instagram photos of lovelorn fans, the genuine hugs she distributes without discretion – where Swift proves time and time again that platinum-selling, record-setting success has not changed her inherent nature. She is awkwardly honest and powerfully empathetic; a brazen superfan, loyal friend, fierce protector of hearts; and one of the world’s greatest ambassadors for the power of just being yourself.
          
        Granted, for Taylor, “being herself” tends towards shimmering, gossamer perfection – but that’s an image regularly blown whenever she dons fake braces and a tri-pony to clown around on late night TV. She’s the first artist since the Beatles (and the only female artist in history) to log six or more weeks at #1 with three consecutive studio albums, and while she’s been named one of Time magazine’s 100 Most Influential People in the World, she’s probably the only person on that list who uses social media to post notes to her best friends and videos of her cats. 
          
        As Billboard’s only artist to receive this nod twice and youngest-ever Woman of the Year prepares to release her fifth album, 1989, she finds herself, as always, in the glare of a blinding spotlight of expectation – but if you think that scares her, you haven’t been paying attention. She calls 1989 her most sonically cohesive collection, and armed with a multiple-week international No. 1 debut single, “Shake It Off,” she’s ready to blaze into the next phase of her still-young career, where she’ll continue to dance like no one’s watching, write like she stole our collective diary, and inevitably soar to ever-greater heights. All that’s left to wonder is how many more lives she’ll lift in the process.
        
    </div>
  </div>


  <div :id="`${path}#price`" style="height: 60px;"></div>
  <t-space style="display: flex; width: 100%; margin-left: 24px;">
      <div>
        <div class="title">TICKET PRICING</div>
        <div class="line"></div>
      </div>
  </t-space>
  <div class="ticket_card" style="margin-left: 55px; margin-top: -10px;">
      <p style="  color: rgba(7, 63, 216, 1); font-size: 18px; font-weight: 700; letter-spacing: 1px;">
        GENERAL SALE
      </p>
      <p style="  margin-top: 0.4rem; color: rgb(70, 73, 79);font-weight: 600;">
        开始日期
      </p>
      <p style=" margin-top: -5px; line-height: 1.625; color: rgb(70, 73, 79);">
        2024年3月14号<br>13:00
      </p>
      <el-divider />
      <p style="  color: rgba(7, 63, 216, 1); font-size: 18px; font-weight: 700; letter-spacing: 1px;">
        STANDARD
      </p>
      <p style="  margin-top: 0.4rem; line-height: 1.625; color: rgb(70, 73, 79);;">
        CAT1: ¥888
        <br>
        CAT2: ¥688
        <br>
        CAT3: ¥488
        <br>
        CAT4: ¥388
        <br>
        CAT5: ¥288
        <br>
      </p>
  </div>
  <div style="height: 20px;"></div>
  <div style="height: 20px;"></div>

  <div :id="`${path}#policy`" style="height: 36px;"></div>
  <div class="card" style="flex-direction: column;">
    <t-space style="display: flex; width: 100%;">
        <div>
          <div class="title">ADMISSION POLICY</div>
          <div class="line"></div>
        </div>
    </t-space>
    <div class="ticket_card" style="margin-left: 55px; margin-top: -10px;">
        <p style="  margin-top: 0.4rem; color: rgb(70, 73, 79);font-weight: 600;">
          Admission Rules:
        </p>
        <p style="  margin-top: 0.4rem; line-height: 1.625;color: rgb(70, 73, 79);;">
          1. 不允许携带食物或饮料进场
          <br>
          2. 不允许开闪光灯拍照
          <br>
          3. 演出48h前不予退款
          <br>
        </p>
    </div>
    <div style="height: 40px;"></div>
  </div>
  
  <div :id="`${path}#gallery`" style="height: 60px;"></div>
  <t-space style="display: flex; width: 100%; margin-left: 24px;">
      <div>
        <div class="title">GALLERY</div>
        <div class="line"></div>
      </div>
  </t-space>
  <el-card style="padding: 5px; height: 390px ; max-width: 100% ; margin-right: 30px; margin-left: 30px; margin-bottom: 40px;">
    <el-carousel :interval="4000" type="card" height="300px" width="100%" indicator-position="outside">
        <el-carousel-item v-for="item in 6" :key="item">
          <h3 text="2xl" justify="center"><t-image
          src="https://tdesign.gtimg.com/demo/demo-image-1.png"
          fit="fill"
          :style="{ width: '100%', height: '100%' }"
          shape="round"
        /></h3>
        </el-carousel-item>
      </el-carousel>
    <div style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
      <t-button @click="pushRouter('gallery')">前往画廊</t-button>
    </div>
  </el-card>
  <div style="height: 40px;"></div>


  <!-- review part -->
  <div :id="`${path}#reviews`" style="height: 36px;"></div>
  <div class="card" style="flex-direction: column;">
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
              <div class="stars"><t-rate :default-value="4.5" allow-half disabled size="16" /></div>
                <div class="comment_infos">
                  <p class="date-time">
                     {{item.datetime}}
                  </p>
                  <p class="description">
                    {{ item.content }}
                  </p>
              </div>
              <div class="author">
                — {{ item.author }}
              </div>
            </div>
          </template>
        </t-list-item>
      </t-list>
    </div>
    <div style="height: 40px;"></div>
  </div>

</template>



<script setup>
import {sessionInformation, bookingInformation, toNextStep} from '@/components/book/Steps.vue';
import { HeartIcon, HeartFilledIcon, ListIcon, TableIcon, StarFilledIcon, DiscountIcon } from 'tdesign-icons-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { NotifyPlugin, Link } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref,onMounted,onUnmounted } from 'vue';
import axios from "axios";
import get from 'lodash/get';
import { defineExpose,watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import EventTicketTable from './EventTicketTable.vue';
import EventTicketCalender from './EventTicketCalender.vue';
import ChooseSession from '../book/ChooseSession.vue';
import router from '@/routers';
const value_lable = ref('events');

const show_event_type = ref(true);

const onClickEventType = (value) => {
  show_event_type.value = value;
};

const onTabChange = (newValue) => {
  value_lable.value = newValue;
  navigateToTab(newValue);
}

const navigateToTab = (tabName) => {
  window.location.href = `##${tabName}`; // 在链接中添加锚点路径并跳转
  window.scrollTo(0, window.scrollY + 55);
};


const dateToString = (date) => {
  const dayNameArray = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
  const dayName = dayNameArray[date.getDay()];
  const localeDateStringArray = date.toLocaleString().split(' ');
  const result = `${localeDateStringArray[0]} ${dayName} ${localeDateStringArray[1]}`;
  return result;
}


const pushRouter = (value)=>{
  switch (value) {
    case 'gallery':
      router.push('/gallery');
      break;
    case 'book':
      router.push('/book');
      break;
  }
}


const token = sessionStorage.getItem('token')
const uid = sessionStorage.getItem('uid')
const eventId = 1

axios.get(`/event/getEventDetail/${eventId}`, {
  headers: {
    token: sessionStorage.getItem('token')
  }
}).then((response) => {
  console.log(response)
}).catch(()=>{})



defineExpose({ navigateToTab });



const { appContext } = getCurrentInstance();
const path = computed(() => get(appContext, '$route.path', ''));


const url =
  'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
const title = '标题';
const description = '描述';
const infoMessage = `卡片内容，以描述性为主，可以是文字、图片或图文组合的形式。按业务需求进行自定义组合。`;

const commentsData = [
  {
    id: 'A',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名A',
    datetime: '今天16:38',
    content: '评论作者名A写的评论内容。',
  },
  {
    id: 'B',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名B',
    datetime: '今天16:38',
    content: '评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。评论作者名B写的评论内容。',
  },
  {
    id: 'C',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名C',
    datetime: '今天16:38',
    content: '评论作者名C写的评论内容。',
  },
  {
    id: 'A',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名A',
    datetime: '今天16:38',
    content: '评论作者名A写的评论内容。',
  },
  {
    id: 'B',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名B',
    datetime: '今天16:38',
    content: '评论作者名B写的评论内容。',
  },
  {
    id: 'C',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名C',
    datetime: '今天16:38',
    content: '评论作者名C写的评论内容。',
  },
  {
    id: 'A',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名A',
    datetime: '今天16:38',
    content: '评论作者名A写的评论内容。',
  },
  {
    id: 'B',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名B',
    datetime: '今天16:38',
    content: '评论作者名B写的评论内容。',
  },
  {
    id: 'C',
    avatar: 'https://tdesign.gtimg.com/site/avatar.jpg',
    author: '评论作者名C',
    datetime: '今天16:38',
    content: '评论作者名C写的评论内容。',
  },
];



</script>



<style lang="css">
.anchor-demo {
  border: 1px solid transparent;
  padding: 20px;
  margin: -20px;
}
.anchor-demo:target {
  border-color: #1890ff;
}

.event{
  margin-left: 25px;
  margin-right: 25px;
  display: flex;
  flex-direction: row;
  gap: 10px;
}

.card {
  --gray: rgba(229, 231, 235, 1);
  display: flex;
  grid-gap: 1.25rem;
  gap: 1.25rem;
  background-color: rgba(255, 255, 255, 1);
  padding: 1.5rem;
}

.card-1 {
  width: 40%;
  margin-right: 15px;
  border-radius: 0.75rem;
  background-color: var(--gray);
}

.right {
  display: flex;
  flex: 1 1 0%;
  flex-direction: column;
  grid-gap: 1.25rem;
  gap: 1.25rem;
}

.card-2 {
  height: 3.5rem;
  width: 100%;
  border-radius: 1rem;
}

.card-3 {
  width: 100%;
  border-radius: 1rem;
}

.bottom {
  margin-top: auto;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 5%;
}

.card-4 {
  height: 2rem;
  border-radius: 9999px;
}

.card-5 {
  height: 2rem;
  width: 100%;
  border-radius: 9999px;
  background-color: var(--gray);
}


@keyframes pulse {
  to {
    opacity: .2;
  }
}


.container {
  position: relative;
}

.content {
  height: 2000px; /* 用于模拟页面滚动 */
}

button#scrollButton {
  position: absolute;
  bottom: 20px;
  right: 20px;
  transform: translateY(0);
  transition: transform 0.3s ease;
}

button#scrollButton.fixed {
  position: fixed;
  bottom: auto;
  top: 20px;
}


.title {
  color: #5e6066;
  font-size: 1.6em;
  font-weight: 700;
  margin: 30px;
  letter-spacing: 3.5px;
}

.line {
    width: 70px;
    height: 4px;
    margin-top: -15px;
    margin-bottom: 20px;
    margin-left: 30px;
    background-color: black;
  }


  .comment_card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: rgba(255, 255, 255, 1);
  padding: 20px;
}

.price_card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
}


.stars {
  display: flex;
  grid-gap: 0.125rem;
  gap: 0.125rem;
  margin-bottom: -10px;
  color: rgb(238, 203, 8);
}

.comment_infos {
  margin-top: 1rem;
}

.date-time {
  color: rgba(7, 63, 216, 1);
  font-size: 12px;
  font-weight: 600;
}

.description {
  margin-top: 0.4rem;
  line-height: 1.625;
  color: rgba(107, 114, 128, 1);
}

.author {
  margin-top: 1.3rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  color: rgba(107, 114, 128, 1);
}

</style>
<style scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>