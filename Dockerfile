FROM openjdk:11
EXPOSE 8080
ADD /target/cloud-0.0.1-SNAPSHOT.jar mycloud.war
VOLUME /c:/temp /var/tmp
ENTRYPOINT ["java", "-jar", "mycloud.war"]
