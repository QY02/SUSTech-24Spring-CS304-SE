FROM python:3.11-slim

WORKDIR /usr/src/app

COPY requirements.txt ./

RUN pip install --no-cache-dir -r requirements.txt


COPY zhipuai.py /usr/local/lib/python3.11/site-packages/langchain_community/chat_models/zhipuai.py

COPY . .

CMD ["python", "./RabbitMQ.py"]