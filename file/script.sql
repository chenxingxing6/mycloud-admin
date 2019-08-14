create table schedule_job
(
  job_id          bigint auto_increment
  comment '任务id'
    primary key,
  bean_name       varchar(200)  null
  comment 'spring bean名称',
  method_name     varchar(100)  null
  comment '方法名',
  params          varchar(2000) null
  comment '参数',
  cron_expression varchar(100)  null
  comment 'cron表达式',
  status          tinyint       null
  comment '任务状态  0：正常  1：暂停',
  remark          varchar(255)  null
  comment '备注',
  create_time     datetime      null
  comment '创建时间'
)
  comment '定时任务'
  charset = utf8;

create table schedule_job_log
(
  log_id      bigint auto_increment
  comment '任务日志id'
    primary key,
  job_id      bigint        not null
  comment '任务id',
  bean_name   varchar(200)  null
  comment 'spring bean名称',
  method_name varchar(100)  null
  comment '方法名',
  params      varchar(2000) null
  comment '参数',
  status      tinyint       not null
  comment '任务状态    0：成功    1：失败',
  error       varchar(2000) null
  comment '失败信息',
  times       int           not null
  comment '耗时(单位：毫秒)',
  create_time datetime      null
  comment '创建时间'
)
  comment '定时任务日志'
  charset = utf8;

create index job_id
  on schedule_job_log (job_id);

create table sys_config
(
  id          bigint auto_increment
    primary key,
  param_key   varchar(50)         null
  comment 'key',
  param_value varchar(2000)       null
  comment 'value',
  status      tinyint default '1' null
  comment '状态   0：隐藏   1：显示',
  remark      varchar(500)        null
  comment '备注',
  constraint param_key
  unique (param_key)
)
  comment '系统配置信息表'
  charset = utf8;

create table sys_dept
(
  dept_id   bigint auto_increment
    primary key,
  parent_id bigint              null
  comment '上级部门ID，一级部门为0',
  name      varchar(50)         null
  comment '部门名称',
  order_num int                 null
  comment '排序',
  del_flag  tinyint default '0' null
  comment '是否删除  -1：已删除  0：正常'
)
  comment '部门管理'
  charset = utf8;

create table sys_dict
(
  id        bigint auto_increment
    primary key,
  name      varchar(100)        not null
  comment '字典名称',
  type      varchar(100)        not null
  comment '字典类型',
  code      varchar(100)        not null
  comment '字典码',
  value     varchar(1000)       not null
  comment '字典值',
  order_num int default '0'     null
  comment '排序',
  remark    varchar(255)        null
  comment '备注',
  del_flag  tinyint default '0' null
  comment '删除标记  -1：已删除  0：正常',
  constraint type
  unique (type, code)
)
  comment '数据字典表'
  charset = utf8;

create table sys_disk
(
  id           bigint auto_increment
    primary key,
  company_name varchar(200)        null
  comment '企业名字',
  company_id   bigint              not null
  comment '企业ID,dept_id',
  name         varchar(100)        null
  comment '文件名',
  is_valid     tinyint default '1' not null
  comment '是否有效',
  create_user  varchar(32)         null
  comment '登录者',
  create_time  bigint              null
  comment '登录时间'
)
  comment '企业网盘目录'
  charset = utf8;

create table sys_log
(
  id          bigint auto_increment
    primary key,
  username    varchar(50)   null
  comment '用户名',
  operation   varchar(50)   null
  comment '用户操作',
  method      varchar(200)  null
  comment '请求方法',
  params      varchar(5000) null
  comment '请求参数',
  time        bigint        not null
  comment '执行时长(毫秒)',
  ip          varchar(64)   null
  comment 'IP地址',
  create_date datetime      null
  comment '创建时间'
)
  comment '系统日志'
  charset = utf8;

create table sys_menu
(
  menu_id   bigint auto_increment
    primary key,
  parent_id bigint       null
  comment '父菜单ID，一级菜单为0',
  name      varchar(50)  null
  comment '菜单名称',
  url       varchar(200) null
  comment '菜单URL',
  perms     varchar(500) null
  comment '授权(多个用逗号分隔，如：user:list,user:create)',
  type      int          null
  comment '类型   0：目录   1：菜单   2：按钮',
  icon      varchar(50)  null
  comment '菜单图标',
  order_num int          null
  comment '排序'
)
  comment '菜单管理'
  charset = utf8;

create table sys_notice
(
  id             bigint auto_increment
  comment '公告ID'
    primary key,
  notice_title   varchar(100)            not null
  comment '公告标题',
  notice_type    tinyint                 not null
  comment '公告类型（1通知 2公告）',
  notice_content varchar(500)            not null
  comment '公告内容',
  status         tinyint(1) default '0'  null
  comment '公告状态（0正常 1关闭）',
  create_user    varchar(32)             not null
  comment '登录者',
  create_time    bigint                  null
  comment '登录时间',
  op_user        varchar(32)             null
  comment '更新者',
  op_time        bigint                  null
  comment '更新时间',
  last_ver       smallint(6) default '0' not null
  comment '版本号',
  remark         varchar(255) default '' null
  comment '备注'
)
  comment '通知公告表'
  charset = utf8;

create table sys_oss
(
  id          bigint auto_increment
    primary key,
  url         varchar(200) null
  comment 'URL地址',
  create_date datetime     null
  comment '创建时间'
)
  comment '文件上传'
  charset = utf8;

create table sys_role
(
  role_id     bigint auto_increment
    primary key,
  role_name   varchar(100) null
  comment '角色名称',
  remark      varchar(100) null
  comment '备注',
  dept_id     bigint       null
  comment '部门ID',
  create_time datetime     null
  comment '创建时间'
)
  comment '角色'
  charset = utf8;

create table sys_role_dept
(
  id      bigint auto_increment
    primary key,
  role_id bigint null
  comment '角色ID',
  dept_id bigint null
  comment '部门ID'
)
  comment '角色与部门对应关系'
  charset = utf8;

create table sys_role_menu
(
  id      bigint auto_increment
    primary key,
  role_id bigint null
  comment '角色ID',
  menu_id bigint null
  comment '菜单ID'
)
  comment '角色与菜单对应关系'
  charset = utf8;

create table sys_schedule
(
  job_id          bigint auto_increment
  comment '任务id'
    primary key,
  bean_name       varchar(200)       null
  comment 'spring bean名称',
  method_name     varchar(100)       null
  comment '方法名',
  params          varchar(2000)      null
  comment '参数',
  cron_expression varchar(100)       null
  comment 'cron表达式',
  status          tinyint            null
  comment '任务状态  0：正常  1：暂停',
  remark          varchar(255)       null
  comment '备注',
  is_valid        tinyint(10)        not null,
  last_ver        tinyint(10)        not null,
  create_time     bigint default '0' not null
  comment '创建时间',
  op_time         bigint default '0' not null
  comment '修改时间'
)
  comment '定时任务'
  charset = utf8;

create index idx_status
  on sys_schedule (status);

create table sys_user
(
  user_id     bigint auto_increment
    primary key,
  username    varchar(50)                                                                                                   not null
  comment '用户名',
  img_path    varchar(100) default 'http://ppkn5nh6t.bkt.clouddn.com/upload/20190414/d7bb19a0f02b4d65b28959a896ad8111.jpeg' not null,
  password    varchar(100)                                                                                                  null
  comment '密码',
  salt        varchar(20)                                                                                                   null
  comment '盐',
  email       varchar(100)                                                                                                  null
  comment '邮箱',
  mobile      varchar(100)                                                                                                  null
  comment '手机号',
  type        tinyint                                                                                                       null
  comment '0：管理员 1：前台用户',
  status      tinyint                                                                                                       null
  comment '状态  0：禁用   1：正常',
  dept_id     bigint                                                                                                        null
  comment '部门ID',
  create_time datetime                                                                                                      null
  comment '创建时间',
  open_id     varchar(50)                                                                                                   null
  comment 'qqopenId',
  constraint username
  unique (username)
)
  comment '系统用户'
  charset = utf8;

create table sys_user_role
(
  id      bigint auto_increment
    primary key,
  user_id bigint null
  comment '用户ID',
  role_id bigint null
  comment '角色ID'
)
  comment '用户与角色对应关系'
  charset = utf8;

create table tb_disk_file
(
  id          bigint auto_increment
    primary key,
  disk_id     bigint              null
  comment '用户ID',
  file_id     bigint              null
  comment '文件ID',
  is_valid    tinyint default '1' not null
  comment '是否有效',
  create_user varchar(32)         null
  comment '登录者',
  create_time bigint              null
  comment '登录时间'
)
  comment '企业共享网盘和文件对应关系'
  charset = utf8;

create table tb_file
(
  id            bigint                  not null
  comment '文件ID'
    primary key,
  parent_id     bigint default '0'      null
  comment '文件父ID',
  original_name varchar(200)            null
  comment '原版文件名 x9Bx98.pptx',
  name          varchar(100)            null
  comment '文件名 42759866854752.pptx',
  original_path varchar(200)            null
  comment '原版文件路径 /file/',
  path          varchar(100)            null
  comment '文件路径 /39166745223986/42759866854752.pptx',
  length        varchar(100)            null
  comment '文件长度 73.1',
  type          tinyint                 null
  comment '文件类型  0：目录  1：文件',
  view_flag     tinyint                 null
  comment '是否可用看',
  is_valid      tinyint default '1'     not null
  comment '是否有效',
  create_user   varchar(32)             null
  comment '登录者',
  create_time   bigint                  null
  comment '登录时间',
  op_user       varchar(32)             null
  comment '更新者',
  op_time       bigint                  null
  comment '更新时间',
  last_ver      smallint(6) default '0' not null
  comment '版本号',
  download_num  int default '0'         not null
  comment '下载数量'
)
  comment '用户与文件对应关系'
  charset = utf8;

create table tb_follow
(
  id           bigint                  not null
  comment 'ID'
    primary key,
  from_user_id bigint                  null
  comment '用户ID',
  to_user_id   bigint                  null
  comment '被关注用户ID',
  is_valid     tinyint default '1'     not null
  comment '是否有效',
  create_user  varchar(32)             null
  comment '登录者',
  create_time  bigint                  null
  comment '登录时间',
  op_user      varchar(32)             null
  comment '更新者',
  op_time      bigint                  null
  comment '更新时间',
  last_ver     smallint(6) default '0' not null
  comment '版本号'
)
  comment '关注用户表'
  charset = utf8;

create table tb_share
(
  id             bigint                  not null
  comment 'ID'
    primary key,
  from_user_id   bigint                  null
  comment '用户ID',
  from_user_name varchar(100)            null
  comment '用户名',
  to_user_id     bigint                  null
  comment '被关注用户ID',
  to_user_name   varchar(100)            null
  comment '用户名',
  file_id        bigint                  null
  comment '被关注用户ID',
  file_name      varchar(100)            null
  comment '文件名',
  expired_time   bigint                  null
  comment '过期时间',
  is_valid       tinyint default '1'     not null
  comment '是否有效',
  create_user    varchar(32)             null
  comment '登录者',
  create_time    bigint                  null
  comment '登录时间',
  op_user        varchar(32)             null
  comment '更新者',
  op_time        bigint                  null
  comment '更新时间',
  last_ver       smallint(6) default '0' not null
  comment '版本号'
)
  comment '分享表'
  charset = utf8;

create table tb_user_file
(
  id      bigint not null
    primary key,
  user_id bigint null
  comment '用户ID',
  file_id bigint null
  comment '文件ID'
)
  comment '用户与文件对应关系'
  charset = utf8;


