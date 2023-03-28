FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/book-management.jar att-project.jar
ENTRYPOINT ["java","-jar","/att-project.jar"]