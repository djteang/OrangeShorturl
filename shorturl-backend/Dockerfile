# 使用官方 OpenJDK 8 作为基础镜像
FROM openjdk:8

# 设置维护者信息
LABEL maintainer="orange_tang"
LABEL description="orange shorturl"
LABEL version="1.0.0"

#将maven构建好的jar添加到镜像中
ADD ./target/shorturl-backend-1.0.0.jar  app.jar

# 暴露应用端口（改为 9003）
EXPOSE 9003

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://127.0.0.1:9003/api/url/list || exit 1

# 启动应用
ENTRYPOINT ["java","-jar","app.jar"]

# 指定时区
ENV TZ=Asia/Shanghai

