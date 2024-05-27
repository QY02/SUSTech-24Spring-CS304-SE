import os

class Config:
    RABBITMQ_URL = 'amqp://user:56481367@47.107.113.54:25569/test'
    CHAT_PRODUCE_QUEUE_NAME = 'ChatProduceQueue'
    CHAT_CONSUME_QUEUE_NAME = 'ChatConsumeQueue'

CONFIG = Config()