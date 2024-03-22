import os

from langchain_community.chat_message_histories.in_memory import ChatMessageHistory
from langchain_openai import ChatOpenAI
from langchain_community.chat_models import ChatZhipuAI
from langchain_community.chat_models.tongyi import ChatTongyi
from langchain_google_genai import ChatGoogleGenerativeAI


def initialize(LLM_type: str, api_key: str):
    if LLM_type == "GPT":  # 可用
        chat = ChatOpenAI(openai_api_key=api_key)
    elif LLM_type == "ZHIPU":  # 可用 要修改zhipuai.py
        chat = ChatZhipuAI(temperature=0.5, api_key=api_key, model="chatglm_turbo")
    elif LLM_type == "GOOGLE":  # 连不上
        chat = ChatGoogleGenerativeAI(google_api_key=api_key, model="gemini-pro",transport="rest")
    elif LLM_type == "TONGYI":  # 可用
        chat = ChatTongyi(dashscope_api_key=api_key)
    elif LLM_type == "TEST":
        chat = None
    else:
        raise ValueError("Invalid LLM type")
    return chat


if __name__ == "__main__":
    history = [{"role": "ai", "content": "你好，我是校园活动与娱乐中心的助手。你有什么问题需要帮助吗？"}
               , {"role": "user", "content": "你好，我想了解一下南方科技大学的音乐会信息。"}]
    # chat = initialize("GPT", os.getenv("OPENAI_API_KEY"))
    chat = initialize("GOOGLE", os.getenv("GOOGLE_API_KEY"))
    # chat = initialize("ZHIPU", os.getenv("ZHIPU_API_KEY"))
    # chat = initialize("TONGYI", os.getenv("TONGYI_API_KEY"))
    chat_history = ChatMessageHistory()
    initial_prompt= ("你是“校园活动与娱乐中心”系统的一位有用的助手。"
                     "在这个系统中，用户可以查看南方科技大学的表演（例如音乐会）、讲座）、比赛以及其他活动的信息。"
                     "用户还可以预订门票、座位或撰写评论。你只能帮助用户解答有关活动等方面的问题。")
    chat_history.add_user_message(initial_prompt)
    for msg in history:
        if msg.get("role") == "ai":
            chat_history.add_ai_message(msg.get("content"))
        else:
            chat_history.add_user_message(msg.get("content"))
    response = chat.invoke(chat_history.messages)
    print(response.content)
