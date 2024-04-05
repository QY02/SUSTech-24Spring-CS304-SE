import os

class Config:
    RABBITMQ_URL = 'amqp://user:123456@localhost:5672/test'
    CHAT_PRODUCE_QUEUE_NAME = 'ChatProduceQueue'
    CHAT_CONSUME_QUEUE_NAME = 'ChatConsumeQueue'

CONFIG = Config()