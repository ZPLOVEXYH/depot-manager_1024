FROM 127.0.0.1:9002/openjdk-jre
ENV PROFILE test
WORKDIR /depot-web
ADD depot-web-0.0.1.jar /depot-web/
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENTRYPOINT ["java", "-jar","./depot-web-0.0.1.jar","--spring.profiles.active=${PROFILE}"]
EXPOSE 8082