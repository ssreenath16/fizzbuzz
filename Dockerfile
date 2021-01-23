FROM openjdk:8
EXPOSE 8080
ADD /build/libs/fizzbuzz-0.0.1.jar fizzbuzz-0.0.1.jar
ENTRYPOINT ["java", "-jar", "fizzbuzz-0.0.1.jar"]