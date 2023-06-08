# fast-magic-api

```shell
## 构建Docker镜像
docker build -t fast-magic-api:1.0.0 .
## 运行Docker容器
docker run -it --rm --name fast-magic-api -p 9090:8080 -d \
  -e JAVA_OPTS="-Xms256m -Xmx256m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -Dspring.profiles.active=local -D=server.port=8080" \
  -e LOG_DIR=/app/logs
  -v /data/exec/fast-magic-api:/app \
  -v /data/logs/fast-magic-api:/app/logs/fast-magic-api \
  fast-magic-api:1.0.0
## 查看运行日志
docker logs --tail 100 -f fast-magic-api
```