FROM openjdk:21
MAINTAINER suvrat
COPY target/todo.jar todo.jar
EXPOSE 8888:8888
ENTRYPOINT ["java","-jar","/todo.jar"]