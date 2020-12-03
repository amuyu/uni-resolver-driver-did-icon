# Dockerfile for amuyu/driver-did-icon

FROM openjdk:8-jdk-alpine AS builder
MAINTAINER amuyu <hjsong@iconloop.com>

# build driver-did-stack

ADD . /opt/driver-did-icon
WORKDIR /opt/driver-did-icon
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar


FROM openjdk:8-jdk-alpine
COPY --from=builder /opt/driver-did-icon/build/libs/*.jar app.jar

# variables

ENV uniresolver_driver_did_icon_node_url=https://test-ctz.solidwallet.io/api/v3
ENV uniresolver_driver_did_icon_score_addr=cx4b4543847d552e6cfbbfa55fbd4a9378534839f9
ENV uniresolver_driver_did_icon_network_id=2

# done

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
