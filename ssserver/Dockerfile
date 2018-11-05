FROM java:openjdk-8-jre-alpine

MAINTAINER hellozjf 908686171@qq.com

ARG JAR_FILE
ADD target/${JAR_FILE} ssserver.jar

EXPOSE 8888 8388

VOLUME /shadowsocks

ENTRYPOINT ["java", "-jar", "ssserver.jar"]