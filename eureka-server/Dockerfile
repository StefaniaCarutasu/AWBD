FROM openjdk:17
COPY ./target/*.jar eureka-server.jar
EXPOSE 8761
CMD ["java","-jar","eureka-server.jar"]