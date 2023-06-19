# fast-magic-api

```shell
mvn clean package -DskipTests=true
```

```shell
scp -r /tmp/local_dir username@servername:remote_dir
```

```shell
#!/bin/bash
APP_NAME="$1"
SPRING_PROFILE="$2"

if [ -z "$APP_NAME" ]; then
  # 若变量为空，则提示用户输入，并将输入的值赋给变量
  read -p "请输入应用名称：" INPUT
  APP_NAME="$INPUT"
fi

JAVA_OPTS="$JAVA_OPTS -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -XX:ErrorFile=/logs/$APP_NAME/hs_err_pid%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/logs/$APP_NAME/heapdump_pid%p.hprof"
JAVA_OPTS="$JAVA_OPTS -Xms256m -Xmx256m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m  -Dserver.port=8080"

if [ -n "$SPRING_PROFILE" ]; then
  JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILE"
else
  JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"
fi

echo "JAVA_OPTS: $JAVA_OPTS"

IMAGE_NAME_PREFIX=$APP_NAME
CONTAINER_NAME=$APP_NAME

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
MAJOR_VERSION=$((RANDOM % 10))
MINOR_VERSION=$((RANDOM % 10))
PATCH_VERSION=$((RANDOM % 10))

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
  -e JAVA_OPTS="$JAVA_OPTS" \
  -e LOG_DIR=/logs/$APP_NAME \
  -v /data/$APP_NAME/logs:/logs/$APP_NAME \
  $IMAGE_NAME

docker logs --tail 100 -f $IMAGE_NAME_PREFIX

```

```shell
sh run.sh fast-magic-api dev
```