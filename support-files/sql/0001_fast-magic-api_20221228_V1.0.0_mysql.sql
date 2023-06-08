create database if not exists `fast-magic-api` default character set = utf8mb4 collate = utf8mb4_general_ci;

create table if not exists `magic_api_file`
(
    `id`           int          not null auto_increment comment 'ID',
    `file_path`    varchar(512) not null,
    `file_content` mediumtext,
    primary key (`id`, `file_path`)
) engine = InnoDB
  auto_increment = 1
  character SET = utf8mb4
  collate = utf8mb4_general_ci;

create table if not exists `magic_api_backup`
(
    `id`          varchar(32) not null comment 'ID',
    `create_date` bigint(13)  not null comment '备份时间',
    `tag`         varchar(32)  default null comment '标签',
    `type`        varchar(32)  default null comment '类型',
    `name`        varchar(64)  default null comment '名称',
    `content`     blob comment '备份内容',
    `create_by`   varchar(255) default null comment '操作人',
    primary key (`id`, `create_date`)
) engine = InnoDB
  character SET = utf8mb4
  collate = utf8mb4_general_ci;
