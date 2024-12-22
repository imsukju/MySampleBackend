FROM maven:3.8.5-eclipse-temurin-17 AS builder
# 사용할 Maven 이미지와 멀티 스테이지 빌드를 위한 별명 설정

WORKDIR /practice
# 컨테이너의 workdir 설정

LABEL authors="tjrwn"
LABEL version="1"
LABEL description="siuuuuuuuuuuuuuuuuuuuuuuuuuuu"
# 라벨설정

COPY pom.xml .
# 호스트의 pom.xml 파일을 컨테이너의 작업 디렉토리(현재 디렉토리)에 복사한다

COPY src ./src
# src 디렉토리를 컨테이너 workdir 인 /practice의 /src에 설정한다
# 만약 하위 디렉토리를 배제하고싶으면
# .dockerignore:
# target/dependency/BOOT-INF/lib/subdir/*


RUN mvn dependency:go-offline
# 이미지를 빌드하는 과정에서 Maven 의존성을 로컬에 캐시한다




# 실행 단계
FROM eclipse-temurin:17-jre-slim AS Run

ARG DEPENDENCY=/target
COPY --from=builder ${DEPENDENCY}/practiceBackend-0.0.1-SNAPSHOT.jar .




##############################
# 예시들과 그 설명 #############
##############################
#ARG DEPENDENCY=/target/dependency

#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#외부 라이브러리가 위치한 BOOT-INF/lib을 /app/lib에 복사합니다.
#이전의 빌드스테이지 Workdir 밑에다 마운트

#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#JAR 파일의 메타데이터가 포함된 META-INF를 /app/META-INF에 복사합니다.

#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
##애플리케이션 코드가 컴파일된 BOOT-INF/classes를 /app 디렉토리에 복사합니다.
