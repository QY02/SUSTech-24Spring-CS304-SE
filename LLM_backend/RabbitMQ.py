import os
import signal

import coloredlogs
import pika
import logging
from typing import Dict, Any
from config import CONFIG
from LLM_API import initialize
from langchain.memory import ChatMessageHistory

# 初始化日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 创建一个文件处理器
file_handler = logging.FileHandler('app.log')
file_handler.setLevel(logging.INFO)

# 创建一个日志格式，包含时间戳
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

# 添加文件处理器到 logger
logger.addHandler(file_handler)

# 安装彩色日志处理器
coloredlogs.install(level='INFO', logger=logger)


# 创建一个超时异常
class TimeoutException(Exception):
    pass


# 当定时器到期时引发超时异常
def handle_timeout(signum, frame):
    raise TimeoutException()


# 设置定时器
signal.signal(signal.SIGALRM, handle_timeout)

# 连接到 RabbitMQ
connection = pika.BlockingConnection(pika.URLParameters(CONFIG.RABBITMQ_URL))
channel = connection.channel()
channel.queue_declare(queue=CONFIG.CHAT_CONSUME_QUEUE_NAME)
channel.queue_declare(queue=CONFIG.CHAT_PRODUCE_QUEUE_NAME)


# RabbitMQ 消费者回调函数
def on_message(ch, method, properties, body):
    try:
        msg_data: Dict[str, Any] = eval(body)
        session_ID = msg_data.get("session_ID")
        history = msg_data.get("history", [])
        query = msg_data.get("query")
        LLM_type = msg_data.get("LLM_type")
        api_key = msg_data.get("api_key")
        tmp_event = msg_data.get("event")
        logger.info(f"Received message: Session_ID={session_ID}, QUERY={query}")

        if LLM_type == "TEST":
            send_to_rabbitmq("这是一个测试", session_ID)
            return

        chat_history = ChatMessageHistory()
        chat = initialize(LLM_type, api_key)
        initial_prompt = ("你是“校园活动与娱乐中心”系统的助手。"
                          "在这个系统中，用户可以查看南方科技大学的表演（例如音乐会）、讲座）、比赛以及其他活动的信息。"
                          "用户还可以预订门票、座位或撰写评论。注意！你只能帮助用户解答有关活动等方面的问题。")
        chat_history.add_user_message(initial_prompt)
        # chat_history.add_user_message("当前的活动是：" + tmp_event)
        chat_history.add_ai_message("你好，我是校园活动与娱乐中心的助手。你有什么问题需要帮助吗？")
        for msg in history:
            if msg.get("role") == "ai":
                chat_history.add_ai_message(msg.get("content"))
            else:
                chat_history.add_user_message(msg.get("content"))
        chat_history.add_user_message(query)
        signal.alarm(30)
        response = chat.invoke(chat_history.messages)
        send_to_rabbitmq(response.content, session_ID)
    except Exception as e:
        logger.error(f"Error processing message: {e} , Session_ID={session_ID}, QUERY={query}")
        send_to_rabbitmq("对不起，请求失败，请稍后重试", session_ID)
    finally:
        ch.basic_ack(delivery_tag=method.delivery_tag)
        signal.alarm(0)


# 发送消息到 RabbitMQ
def send_to_rabbitmq(msg, session_ID):
    reply = {
        'session_ID': session_ID,
        'response': msg
    }
    channel.basic_publish(exchange='',
                          routing_key=CONFIG.CHAT_CONSUME_QUEUE_NAME,
                          body=str(reply))
    logger.info(f"Sent reply: Session_ID={session_ID}, RESPONSE={msg}")


# 启动消费者
def consumer_process():
    channel.basic_consume(
        queue=CONFIG.CHAT_PRODUCE_QUEUE_NAME,
        on_message_callback=on_message,
        auto_ack=False
    )
    logger.info(f"[{os.getpid()}] Consumer started, waiting for messages...")
    channel.start_consuming()


if __name__ == "__main__":
    try:
        consumer_process()
    except KeyboardInterrupt:
        print('Exiting program...')
        connection.close()
