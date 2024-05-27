FROM python:3.9

WORKDIR /app

RUN apt-get update \
   && apt-get install -y --no-install-recommends cron \
   && apt-get install -y tzdata \
   && rm -rf /var/lib/apt/lists/* \
   && apt-get clean

COPY AI_rec.py /app/

RUN pip install recommenders
RUN pip install pandas
RUN pip install mysql-connector-python
RUN pip install coloredlogs
RUN pip3 install torch torchvision torchaudio

ENV LC_ALL C.UTF-8 
ENV TZ=Asia/Shanghai
RUN ln -fs /usr/share/zoneinfo/${TZ} /etc/localtime && \
dpkg-reconfigure -f noninteractive tzdata 

RUN echo "* * * * * root python /app/AI_rec.py" >> /etc/crontab
CMD PYTHONIOENCODING=utf-8 /etc/init.d/cron start && touch /root/ai_rec.log && tail -f /root/ai_rec.log
