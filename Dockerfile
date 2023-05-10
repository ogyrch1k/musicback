FROM openjdk:11
ARG JAR_FILE=target/music-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
#--network for_music  -p 8080:8080 -v for_music:/opt/app/music