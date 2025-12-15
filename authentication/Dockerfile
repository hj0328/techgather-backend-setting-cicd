FROM amazoncorretto:21-al2023 AS build

WORKDIR /authentication

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM amazoncorretto:21-al2023 AS runtime
EXPOSE 5080

WORKDIR /authentication

COPY --from=build /authentication/dependencies/ ./
COPY --from=build /authentication/snapshot-dependencies/ ./
COPY --from=build /authentication/spring-boot-loader/ ./
COPY --from=build /authentication/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
#