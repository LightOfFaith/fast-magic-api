# 1.指定基础镜像openjdk:8-alpine对应的Alpine版本为3.9.4（进入容器，执行命令cat /etc/alpine-release）
FROM openjdk:8-alpine

# 2.指明该镜像的作者和其电子邮件
MAINTAINER naeleven "naeleven@gmail.com"

# 3.在构建镜像时，指定镜像的工作目录，之后的命令都是基于此工作目录，如果不存在，则会创建目录
WORKDIR /app

# 4.将一些安装包复制到镜像中，语法：ADD/COPY <src>... <dest>
## ADD与COPY的区别：ADD复制并解压，COPY仅复制
COPY . /app

# 5.运行指定的命令
## 更新Alpine的软件源为阿里云
RUN cp /etc/apk/repositories /etc/apk/repositories.bak
RUN echo "https://mirrors.aliyun.com/alpine/v3.9/main" > /etc/apk/repositories && \
    echo "https://mirrors.aliyun.com/alpine/v3.9/community" >> /etc/apk/repositories
## 安装tzdata软件包，并将时区设置为Asia/Shanghai
RUN	apk update && apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata
## 安装fontconfig和ttf-dejavu软件包，用于支持中文显示
RUN apk add --no-cache fontconfig ttf-dejavu && \
    fc-cache -f

# 6.设置环境变量
## 设置时区
ENV TZ=Asia/Shanghai
## 设置JVM参数
## ENV JAVA_OPTS="-Xms256m -Xmx256m -Xss256k -Xmn128m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
## LANG、LANGUAGE、LC_ALL，用于指定中文UTF-8编码
ENV LANG zh_CN.UTF-8
ENV LANGUAGE zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8

# 7.启动命令
CMD ["sh", "-c", "java -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:$LOG_DIR/gc-%t.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heap.hprof -XX:ErrorFile=$LOG_DIR/hs_err_pid%p.log $JAVA_OPTS -jar app.jar"]