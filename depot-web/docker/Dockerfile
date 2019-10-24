# 测试环境Dockerfile文件配置
FROM 127.0.0.1:9004/openjdk-jre
ENV PROFILE test
WORKDIR /depot-web
ADD depot-web/docker/depot-web-0.0.1.jar /depot-web/
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENTRYPOINT ["java", "-jar","./depot-web-0.0.1.jar","--spring.profiles.active=${PROFILE}"]
EXPOSE 8082

# 张鹏本地开发环境Dockerfile文件配置
#FROM openjdk:8
#ENV PROFILE dev
#WORKDIR /depot-web
#ADD depot-web-0.0.1.jar /depot-web/
#ENTRYPOINT ["java", "-jar","./depot-web-0.0.1.jar","--spring.profiles.active=${PROFILE}"]
