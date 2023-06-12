# fast-magic-api

```shell
mvn clean package -DskipTests=true
```

```shell
scp -r /tmp/local_dir username@servername:remote_dir
```

```shell
#!/bin/bash
IMAGE_NAME_PREFIX="fast-magic-api"
CONTAINER_NAME="fast-magic-api"

# 获取匹配的容器名称
CONTAINER_NAME=$(docker ps --format "{{.Names}}" --filter "name=^${IMAGE_NAME_PREFIX}" -q)
# 获取匹配的镜像名称
IMAGE_NAME=$(docker ps --format "{{.Image}}" --filter "name=^${IMAGE_NAME_PREFIX}" -q)

# 检查容器是否存在
if [[ -n "$CONTAINER_NAME" ]]; then
  echo "容器 $CONTAINER_NAME 存在，停止..."
  # 停止容器
  docker stop $CONTAINER_NAME
fi
## 检查镜像是否存在
#if [[ -n "$IMAGE_NAME" ]]; then
#  echo "镜像 $IMAGE_NAME 存在，删除镜像..."
#  # 删除镜像
#  docker rmi $IMAGE_NAME
#fi

CURRENT_DATETIME=$(date +"%Y%m%d%H%M%S")

# 生成随机的主版本号、次版本号和修订版本号
MAJOR_VERSION=$(( RANDOM % 10 ))
MINOR_VERSION=$(( RANDOM % 10 ))
PATCH_VERSION=$(( RANDOM % 10 ))

# 组合版本号
APP_VERSION="$CURRENT_DATETIME.$MAJOR_VERSION.$MINOR_VERSION.$PATCH_VERSION"

echo "随机生成的应用版本号：$APP_VERSION"

IMAGE_NAME="$IMAGE_NAME_PREFIX:$APP_VERSION"

echo "构建镜像 $IMAGE_NAME ..."
# 构建镜像
docker build -f $IMAGE_NAME_PREFIX.Dockerfile -t $IMAGE_NAME .

echo "启动镜像 $IMAGE_NAME ..."
# 启动镜像
docker run -it --rm --name $IMAGE_NAME_PREFIX -p 9090:8080 -d \
  -e JAVA_OPTS="-Xms256m -Xmx256m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -Dspring.profiles.active=dev -D=server.port=8080" \
  -e LOG_DIR=/logs/fast-magic-api \
  -v /data/fast-magic-api/logs:/logs/fast-magic-api \
  $IMAGE_NAME

docker logs --tail 100 -f $IMAGE_NAME_PREFIX
```