FROM openjdk:11
EXPOSE  8080
COPY /*.jar  course_evaluation.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/course_evaluation.jar"]