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
        <t-tab-panel value="about"  label="ABOUT"></t-tab-panel>
        <t-tab-panel value="review" label="REVIEW"></t-tab-panel>
      </t-tabs>
  </el-affix>
  <div :id="`${path}#events`" style="height: 60px;"></div>
  <h2>EVENTS</h2>
  
  <div :id="`${path}#about`" style="height: 60px;"></div>
  <div>
      Taylor Swift in Concert
  
  She is, quite simply, a global superstar.
    
  Taylor Swift is a seven-time GRAMMY winner, and the youngest recipient in history of the music industry’s highest honor, the GRAMMY Award for Album of the Year. She is the only female artist in music history (and just the fourth artist ever) to twice have an album hit the 1 million first-week sales figure (2010’s Speak Now and 2012’s RED). She’s a household name whose insanely catchy yet deeply personal self-penned songs transcend music genres, and a savvy businesswoman who has built a childhood dream into an empire.
    
  But the numbers don’t tell Taylor's story half as well as she could. After all, it’s the intangibles that elevate Swift into the stratosphere of our pop culture planet, allowing the 24-year old singer-songwriter to orbit in a more rarified air. Her large-scale charitable contributions are one thing, but it’s in the small gestures – the notes of compassion she posts on the Instagram photos of lovelorn fans, the genuine hugs she distributes without discretion – where Swift proves time and time again that platinum-selling, record-setting success has not changed her inherent nature. She is awkwardly honest and powerfully empathetic; a brazen superfan, loyal friend, fierce protector of hearts; and one of the world’s greatest ambassadors for the power of just being yourself.
    
  Granted, for Taylor, “being herself” tends towards shimmering, gossamer perfection – but that’s an image regularly blown whenever she dons fake braces and a tri-pony to clown around on late night TV. She’s the first artist since the Beatles (and the only female artist in history) to log six or more weeks at #1 with three consecutive studio albums, and while she’s been named one of Time magazine’s 100 Most Influential People in the World, she’s probably the only person on that list who uses social media to post notes to her best friends and videos of her cats. 
    
  As Billboard’s only artist to receive this nod twice and youngest-ever Woman of the Year prepares to release her fifth album, 1989, she finds herself, as always, in the glare of a blinding spotlight of expectation – but if you think that scares her, you haven’t been paying attention. She calls 1989 her most sonically cohesive collection, and armed with a multiple-week international No. 1 debut single, “Shake It Off,” she’s ready to blaze into the next phase of her still-young career, where she’ll continue to dance like no one’s watching, write like she stole our collective diary, and inevitably soar to ever-greater heights. All that’s left to wonder is how many more lives she’ll lift in the process.
  

  </div>

  <div :id="`${path}#review`" style="height: 60px;"></div>
  <div>
    <t-list :split="true">
      <t-list-item v-for="(item, index) in commentsData" :key="index">
        <template #content>
          <t-comment :avatar="item.avatar" :author="item.author" :datetime="item.datetime" :content="item.content">
            <template #actions>
              <t-space key="thumbUp" :size="6">
                <t-icon name="thumb-up" />
                <span>6</span>
              </t-space>
              <t-space key="chat" :size="6">
                <t-icon name="chat" />
                <span>回复</span>
              </t-space>
            </template>
          </t-comment>
          <div>
            <h3>评分</h3>
            <t-rate :default-value="4.5" allow-half disabled size="16" />
          </div>
        </template>
      </t-list-item>
    </t-list>
  </div>
</template>

<script setup>
import { HeartIcon, HeartFilledIcon, CloudUploadIcon, StarIcon, StarFilledIcon, DiscountIcon } from 'tdesign-icons-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { NotifyPlugin, Link } from 'tdesign-vue-next';
import { computed, getCurrentInstance, ref,onMounted,onUnmounted } from 'vue';
import get from 'lodash/get';
import { defineExpose,watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const value_lable = ref('events');

const onTabChange = (newValue) => {
  value_lable.value = newValue;
  navigateToTab(newValue);
}

const navigateToTab = (tabName) => {
  window.location.href = `##${tabName}`; // 在链接中添加锚点路径并跳转
  window.scrollTo(0, window.scrollY + 55);
};



const fix = ref('false')
// 监听页面滚动事件
const handleScroll = () => {
  if (stickyElement.value) {
    const distanceFromTop = stickyElement.value.getBoundingClientRect().top;
    if (distanceFromTop <= 0) {
      fix.value = true
      alert("in")
    } else {
      fix.value= false
    }
  }
};
const scrolled = ref(window.scrollY);
onMounted(() => {
  window.addEventListener('scroll', () => {
    handleScroll
    console.log('触发滚动事件'); // 检查是否触发了滚动事件
    scrolled.value = window.scrollY;
  });
});
onUnmounted(() => {
  window.removeEventListener('touchmove', handleTouchMove);
});
window.addEventListener('scroll', () => {
  scrolled.value = window.scrollY;
  init('页面已滚动距离：', scrolled);
  NotifyPlugin({ title: scrolled, content: '用户表示普通操作信息提示'});
});
watch(scrolled, (newValue, oldValue) => {
  NotifyPlugin({ title: newValue, content: '用户表示普通操作信息提示' });
});



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

</style>
