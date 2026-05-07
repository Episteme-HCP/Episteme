# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy all poms and dependencies
COPY pom.xml .
COPY episteme-core/pom.xml episteme-core/
COPY episteme-natural/pom.xml episteme-natural/
COPY episteme-social/pom.xml episteme-social/
COPY episteme-server/pom.xml episteme-server/

# Pre-fetch dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY episteme-core episteme-core
COPY episteme-natural episteme-natural
COPY episteme-social episteme-social
COPY episteme-server episteme-server

# Build only the server and its dependencies
# Note: We skip tests and use -pl/-am to avoid building episteme-native which requires OpenCL/CUDA
# We add --enable-preview for Vector API / Panama compatibility in Java 21/25
RUN mvn clean install -pl episteme-server -am -DskipTests -Dmaven.compiler.release=21 -Dmaven.compiler.compilerArgs="--enable-preview"

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Set non-root user for security (industrial standard)
RUN addgroup -S episteme && adduser -S episteme -G episteme
USER episteme

# Copy the executable jar from the build stage
COPY --from=build /app/episteme-server/target/episteme-server-*.jar app.jar

# Expose port for MCP (HTTP/SSE)
EXPOSE 8080

# Environment variables for industrial tuning
ENV JAVA_OPTS="-Xms512m -Xmx2g -Djava.security.egd=file:/dev/./urandom --add-modules jdk.incubator.vector"

# Run the server
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
