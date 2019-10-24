# 测试环境Dockerfile文件配置
FROM 127.0.0.1:9004/openjdk-jre
ENV PROFILE test
WORKDIR /depot-web-cz
ADD depot-web-cz/docker/depot-web-cz-0.0.1.jar /depot-web-cz/
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENTRYPOINT ["java", "-jar","./depot-web-cz-0.0.1.jar","--spring.profiles.active=${PROFILE}"]
EXPOSE 8083

# 张鹏本地开发环境Dockerfile文件配置
#FROM openjdk:8
#ENV PROFILE dev
#WORKDIR /depot-web-cz
#ADD depot-web-cz-0.0.1.jar /depot-web-cz/
#ENTRYPOINT ["java", "-jar","./depot-web-cz-0.0.1.jar","--spring.profiles.active=${PROFILE}"]
