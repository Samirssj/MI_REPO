FROM maven:3.9.16-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM tomcat:10.1-jdk25-temurin
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /app/target/repositorio-academico.war /usr/local/tomcat/webapps/ROOT.war
COPY docker/start.sh /start.sh
RUN chmod +x /start.sh
EXPOSE 10000
CMD ["/start.sh"]
