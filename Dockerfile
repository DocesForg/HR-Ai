# Build Spring Boot API
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# Build React UI
FROM node:22-alpine AS frontend-build
WORKDIR /workspace/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Serve UI and proxy API to Spring Boot
FROM nginx:1.27-alpine
COPY deploy/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=frontend-build /workspace/frontend/dist /usr/share/nginx/html
COPY --from=backend-build /workspace/target/hr-ai-0.1.0.jar /opt/hr-ai/app.jar
RUN apk add --no-cache openjdk21-jre-headless
EXPOSE 80 8080
CMD ["sh", "-c", "java -jar /opt/hr-ai/app.jar & nginx -g 'daemon off;'"]
