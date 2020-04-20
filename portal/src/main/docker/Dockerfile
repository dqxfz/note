FROM tomcat
VOLUME /tmp
ADD portal-1.0-SNAPSHOT.war webapps/ROOT.war
EXPOSE 8080
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]