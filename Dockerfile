FROM openjdk:11 as builder
COPY . .
RUN ./gradlew build -x test

FROM openjdk:11 as release
COPY --from=builder ./build/libs/articlereader.jar app.jar
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]