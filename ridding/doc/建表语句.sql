//骑行主表TB_Ridding
CREATE TABLE TB_Ridding (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '骑行活动id，自增长',
LeaderUserId bigint(20) NOT NULL DEFAULT '0' COMMENT '队长id',
MapId bigint(20) NOT NULL DEFAULT 0 COMMENT '地图id',
Name varchar(50) NOT NULL DEFAULT '' COMMENT '骑行的名称',
RiddingType smallint(6) NOT NULL DEFAULT '0' COMMENT '骑行类型,10为短途，20为中途，30为长途',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '纪录创建时间',
LastUpdateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '上次更新时间',
UserCount  int(11) NOT NULL DEFAULT '0' COMMENT '队员数量',
riddingStatus tinyint(4) NOT NULL DEFAULT '0' COMMENT '骑行状态',
isRecom tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是推荐',
isPublic tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是公开',
likeCount int(11) NOT NULL DEFAULT 0 COMMENT '喜欢数量',
commentCount int(11) NOT NULL DEFAULT 0 COMMENT '评论数量',
useCount  int(11) NOT NULL DEFAULT 0 COMMENT '使用数量',
careCount  int(11) NOT NULL DEFAULT 0 COMMENT '关注数量',
isSyncSina tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否同步新浪微博,0表示不同步,1表示同步',
PRIMARY KEY  (`Id`)
)   DEFAULT CHARSET=UTF8 COMMENT '骑行主表';


//骑行评论表
CREATE TABLE TB_Ridding_Comment (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id，自增长',
riddingId bigint(20) NOT NULL DEFAULT '0' COMMENT '活动id',
UserId bigint(20) NOT NULL DEFAULT '0' COMMENT '评论人的userid',
toUserId bigint(20) NOT NULL DEFAULT 0 COMMENT '评论给某人的id',
text varchar(512)  NOT NULL DEFAULT '' COMMENT '内容',
usePicUrl varchar(256)  NOT NULL DEFAULT '' COMMENT '评论引用的照片id',
commentType  tinyint(4) NOT NULL DEFAULT '0' COMMENT '评论类型,0表示评论,1表示评论回复',
replyId bigint(20) NOT NULL DEFAULT '0' COMMENT '回复的评论的id',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '纪录创建时间',
LastUpdateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '上次更新时间',
PRIMARY KEY  (`Id`)
)   DEFAULT CHARSET=UTF8 COMMENT '骑行评论表';


//骑行喜欢表TB_Ridding_Action
CREATE TABLE TB_Ridding_Action (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id，自增长',
userId bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
RiddingId bigint(20) NOT NULL DEFAULT 0 COMMENT '活动id',
type  tinyint(4) NOT NULL DEFAULT '0' COMMENT '操作类型',
objectId bigint(20) NOT NULL DEFAULT '0' COMMENT '操作对象id',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '纪录创建时间',
LastUpdateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '上次更新时间',
PRIMARY KEY  (`Id`)
)   DEFAULT CHARSET=UTF8 COMMENT '';


//骑行地图表TB_Map
CREATE TABLE TB_Map (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '骑行地图id，自增长',
MapUrl varchar(5000) NOT NULL  DEFAULT '' COMMENT '地图原始url',
MapTaps varchar(5000) NOT NULL DEFAULT '' COMMENT '地图标记坐标，Json格式',
CreateTime bigint(20) NOT NULL default '0' COMMENT '纪录创建时间',
objectId bigint(20) NOT NULL DEFAULT '0' COMMENT '获取纪录id',
objectType tinyint(4) NOT NULL DEFAULT '0' COMMENT '记录的来源,0新浪微博',
userId bigint(20) NOT NULL DEFAULT '0' COMMENT '传地图的用户Id',
Status tinyint(4) NOT NULL DEFAULT '0' COMMENT '地图当前状态0为不可使用,1为可以使用',
MapPoint text NOT NULL DEFAULT '' COMMENT '经过编译后的地址',
Distance int(11) NOT NULL DEFAULT '0' COMMENT '行程距离',
BeginLocation varchar(63) NOT NULL DEFAULT '' COMMENT '起始位置名称',
EndLocation varchar(63) NOT NULL DEFAULT '' COMMENT '终点位置名称',
MidLocation varchar(1023) NOT NULL DEFAULT '' COMMENT '路过位置名称',
cityId bigint(20) NOT NULL DEFAULT '0' COMMENT '起点的城市id',
avatorPic bigint(20) NOT NULL DEFAULT '0' COMMENT '地图的封面',
avatorPicUrl varchar(255) NOT NULL DEFAULT '0' COMMENT '地图的封面url',
PRIMARY KEY  (`Id`)
)   DEFAULT CHARSET=UTF8 COMMENT '骑行地图表';


//骑行用户表TB_Ridding_User
CREATE TABLE TB_Ridding_User (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id，自增长',
RiddingId bigint(20) NOT NULL DEFAULT '0' COMMENT '骑行主表id',
UserId bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
UserRole tinyint(4) NOT NULL default '0' COMMENT '用户权限,0非队员，10普通队员,20队长',
CreateTime bigint(20) NOT NULL default '0' COMMENT '纪录创建时间',
LastUpdateTime bigint(20) NOT NULL default '0' COMMENT '上次更新时间',
riddingStatus tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户的单个骑行状态',
selfName  varchar(64) NOT NULL DEFAULT '' COMMENT '用户的单个骑行名称',
PRIMARY KEY  (`Id`)
)   DEFAULT CHARSET=UTF8 COMMENT '骑行用户表';

//用户信息表TB_Profile
CREATE TABLE TB_Profile (
UserId bigint(20) NOT NULL  AUTO_INCREMENT  COMMENT '用户id',
UserName varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
NickName varchar(64) NOT NULL DEFAULT '' COMMENT '昵称',
TotalDistance int(11) NOT NULL DEFAULT '0' COMMENT '行程总距离',
CreateTime bigint(20) NOT NULL default '0' COMMENT '创建时间',
LastUpdateTime bigint(20) NOT NULL default '0' COMMENT '上次更新时间',
SAvatorUrl varchar(128) NOT NULL DEFAULT ' ' COMMENT '用户小头像',
BAvatorUrl varchar(128) NOT NULL DEFAULT ' ' COMMENT '用户大头像',
level int(11) NOT NULL DEFAULT '0' COMMENT '用户登陆',
backgroundUrl varchar(127) NOT NULL DEFAULT ' ' COMMENT '用户背景',
graysAvatorUrl varchar(128) NOT NULL DEFAULT ' ' COMMENT '用户大头像',
taobaoCode varchar(127) NOT NULL DEFAULT ' ' COMMENT '淘宝Code',
PRIMARY KEY  (`UserId`)
)   DEFAULT CHARSET=UTF8 COMMENT '用户信息表';

//外部资源账户表TB_SourceAccount
CREATE TABLE TB_Source_Account (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
UserId bigint(20) NOT NULL  DEFAULT '0'  COMMENT  '用户id',
AccessUserId bigint(20) NOT NULL DEFAULT  '0' COMMENT '资源用户id',
AccessUserName varchar(64) NOT NULL DEFAULT '' COMMENT '资源用户名',
AccessToken varchar(128) NOT NULL DEFAULT '' COMMENT '用户对应token',
SourceType tinyint(4) NOT NULL COMMENT '外部资源类新,1为新浪微博',
CreateTime bigint(20) NOT NULL default '0' COMMENT '创建时间',
PRIMARY KEY  (`Id`)
)   DEFAULT CHARSET=UTF8 COMMENT '外部资源账户表';


//用户帐户表TB_Account
CREATE TABLE TB_Account (
UserId bigint(20) NOT NULL  DEFAULT '0' COMMENT '用户id,自增长',
UserName varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
PassWord varchar(64) NOT NULL DEFAULT '' COMMENT '密码',
CreateTime bigint(20) NOT NULL default '0' COMMENT '创建时间',
PRIMARY KEY  (`UserId`)
)   DEFAULT CHARSET=UTF8 COMMENT '用户帐户表';


CREATE TABLE TB_Source (
id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
SourceId bigint(20) NOT NULL DEFAULT '0' COMMENT '资源id',
AccessUserId bigint(20) NOT NULL DEFAULT '0' COMMENT '发布的资源用户的id',
Text varchar(1000) NOT NULL DEFAULT '' COMMENT '内容',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '纪录创建时间',
Status tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示未处理，1表示已处理,2url是错的,10链接无效',
sourceType tinyint(4) NOT NULL DEFAULT '0' COMMENT '资源的来源,1新浪微博',
PRIMARY KEY  (`id`)
)   DEFAULT CHARSET=UTF8 COMMENT  '外部资源保存纪录表';


CREATE TABLE IF NOT EXISTS `TB_City` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `pid` int(10) NOT NULL,
  `tree` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3191 ;

//推荐表
CREATE TABLE TB_Recom (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
Userid bigint(20) NOT NULL DEFAULT '0' COMMENT '推荐人userid',
ToUserid bigint(20) NOT NULL DEFAULT '0' COMMENT '推荐给某人的userid',
SourceId bigint(20) NOT NULL DEFAULT '0' COMMENT '对应资源的id',
Type tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示推荐地图',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '纪录创建时间',
PRIMARY KEY  (`id`)
)DEFAULT CHARSET=UTF8 COMMENT  '个人推荐表';



CREATE TABLE TB_Recom (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
Userid bigint(20) NOT NULL DEFAULT '0' COMMENT '推荐人userid',
ToUserid bigint(20) NOT NULL DEFAULT '0' COMMENT '推荐给某人的userid',
SourceId bigint(20) NOT NULL DEFAULT '0' COMMENT '对应资源的id',
Type tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示推荐地图',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '纪录创建时间',
PRIMARY KEY  (`id`)
)DEFAULT CHARSET=UTF8 COMMENT  '个人推荐表';


//'广场广告表'
CREATE TABLE TB_Public (
  Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
  type int(11) NOT NULL DEFAULT '0' COMMENT '广告类型',
  Json varchar(512) NOT NULL DEFAULT '0' COMMENT 'json内容',
  CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '纪录创建时间',
  LastUpdateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  weight int(11) NOT NULL DEFAULT '0' COMMENT '权重值',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广场广告表';

//地图修复表
CREATE TABLE TB_MapFix (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
latitude double(11,2) NOT NULL DEFAULT '0' COMMENT '经度',
longtitude double(11,2) NOT NULL DEFAULT '0' COMMENT '纬度',
offsetx int(11) NOT NULL DEFAULT '0' COMMENT 'x像素偏移',
offsety int(11) NOT NULL DEFAULT '0' COMMENT 'y像素偏移',
toLatitude double(11,2) NOT NULL DEFAULT '0' COMMENT '偏移经度',
toLongtitude double(11,2) NOT NULL DEFAULT '0' COMMENT '偏移纬度',
PRIMARY KEY  (`id`)
)DEFAULT CHARSET=UTF8 COMMENT  '地图修复表';

CREATE TABLE TB_User_Nearby (
UserId bigint(20) NOT NULL COMMENT '用户id',
latitude double(11,2) NOT NULL DEFAULT '0' COMMENT '经度',
longtitude double(11,2) NOT NULL DEFAULT '0' COMMENT '纬度',
geohash varchar(63) NOT NULL DEFAULT '' COMMENT 'geo hash',
LastUpdateTime bigint(20) NOT NULL default '0' COMMENT '上次更新时间',
PRIMARY KEY  (`UserId`)
)DEFAULT CHARSET=UTF8 COMMENT  '用户附近表';


//apns记录表
CREATE TABLE TB_IosApns (
userId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
token varchar(127) NOT NULL DEFAULT '' COMMENT '内容',
CreateTime bigint(20) NOT NULL default '0' COMMENT '创建时间',
LastUpdateTime bigint(20) NOT NULL default '0' COMMENT '上次更新时间',
Status tinyint(4) NOT NULL DEFAULT '0' COMMENT '0不在使用中,1使用中',
Version varchar(63) NOT NULL DEFAULT '' COMMENT '版本号',
PRIMARY KEY  (`userId`)
)DEFAULT CHARSET=UTF8 COMMENT  'Ios Apns记录表';

CREATE TABLE TB_PHOTO (
id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
originalPath varchar(127) NOT NULL DEFAULT '' COMMENT '原图的路径',
smallPath varchar(127) NOT NULL DEFAULT '' COMMENT '75图的路径',
midPath varchar(127) NOT NULL DEFAULT '' COMMENT '130图的路径',
bigPath varchar(127) NOT NULL DEFAULT '' COMMENT '260图的路径',
squarePath varchar(127) NOT NULL DEFAULT '' COMMENT '75方图',
PRIMARY KEY  (`id`)
)DEFAULT CHARSET=UTF8 COMMENT  '图片表';


//骑行照片表TB_Ridding_Picture
alter table TB_Ridding_Picture convert to character set utf8mb4 collate utf8mb4_bin; 
CREATE TABLE TB_Ridding_Picture (
Id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id，自增长',
RiddingId bigint(20) NOT NULL DEFAULT '0' COMMENT '骑行主表id',
photoUrl varchar(255) NOT NULL DEFAULT '' COMMENT '照片url',
localName varchar(255) NOT NULL DEFAULT '' COMMENT '照片在本地的名称',
UserId bigint(20) NOT NULL DEFAULT '0' COMMENT '拍照的用户id',
latitude double(11,5) NOT NULL DEFAULT '0' COMMENT '经度',
longtitude double(11,5) NOT NULL DEFAULT '0' COMMENT '纬度',
CreateTime bigint(20) NOT NULL default '0' COMMENT '纪录创建时间',
LastUpdateTime bigint(20) NOT NULL default '0' COMMENT '上次更新时间',
takePicDate bigint(20) NOT NULL default '0' COMMENT '拍摄时间',
takePicLocation varchar(63) DEFAULT '' COMMENT '拍摄位置',
Status tinyint(4) NOT NULL DEFAULT '0' COMMENT '照片状态,0公开,1私人,20已删除',
description varchar(512) DEFAULT '' COMMENT '描述',
width int(11) DEFAULT 0  COMMENT '宽度',
height int(11) DEFAULT 0 COMMENT '高度',
likeCount int(11) DEFAULT '0' COMMENT '喜欢这张照片的数量',
breadId bigint(20) NOT NULL DEFAULT '0' COMMENT '面包id',
PRIMARY KEY  (`Id`)
)   DEFAULT CHARSET=UTF8 COMMENT '骑行照片表';

//发微博功能表
CREATE TABLE TB_WeiBo (
id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
Text varchar(255) NOT NULL DEFAULT '' COMMENT '内容',
PhotoUrl varchar(127) NOT NULL DEFAULT '' COMMENT '图片url',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '记录创建时间',
SendTime bigint(20) NOT NULL DEFAULT '0' COMMENT '发送记录的时间',
Status tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示未处理，1表示已处理',
sourceType tinyint(4) NOT NULL DEFAULT '0' COMMENT '资源的来源,1新浪微博',
weiboType tinyint(4) NOT NULL DEFAULT '0' COMMENT '微博内容的类型，0为常规微博，1为地图微博',
weiboId bigint(20) NOT NULL DEFAULT '0' COMMENT '微博id',
riddingId bigint(20) NOT NULL DEFAULT '0' COMMENT '骑行id',
PRIMARY KEY  (`id`)
)   DEFAULT CHARSET=UTF8 COMMENT  '发微博功能表';

CREATE TABLE TB_RepostMap (
id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
weiboId bigint(20) NOT NULL DEFAULT '0' COMMENT '微博id',
respostWeiBoId bigint(20) NOT NULL DEFAULT '0' COMMENT '转发的微博id',
respostSourceId bigint(20) NOT NULL DEFAULT '0' COMMENT '转发的人的微博id',
respostUserId bigint(20) NOT NULL DEFAULT '0' COMMENT '转发的人的id',
sourceType tinyint(4) NOT NULL DEFAULT '0' COMMENT '资源的来源,1新浪微博',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '记录创建时间',
PRIMARY KEY  (`id`)
)   DEFAULT CHARSET=UTF8 COMMENT  '获得转发地图微博生成地图表';

CREATE TABLE TB_User_Relation (
id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
userId bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
toUserId bigint(20) NOT NULL DEFAULT '0' COMMENT '关注用户id',
CreateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '记录创建时间',
Status tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示删除，1表示存在',
PRIMARY KEY  (`id`)
) DEFAULT CHARSET=UTF8 COMMENT  '用户关系表';

CREATE TABLE TB_WeiBo_ToBeSend (
accountId bigint(20) NOT NULL DEFAULT '0' COMMENT '微博id',
sourceType tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示微博，1表示其他',
lastUpdateTime bigint(20) NOT NULL DEFAULT '0' COMMENT '记录创建时间',
sendStatus tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示未发送，1表示已发送',
text varchar(512) NOT NULL DEFAULT '' COMMENT '微博内容',
PRIMARY KEY  (`accountId`)
) DEFAULT CHARSET=UTF8 COMMENT  '微博发送表';


CREATE TABLE TB_User_Pay (
id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id,自增长',
userId bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
typeId int(11) DEFAULT 0  COMMENT '购买产品类型id',
createTime bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
beginTime bigint(20) NOT NULL DEFAULT '0' COMMENT '开始时间',
status tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示无效状态，1表示试用期,2有效',
dayLong int(11) DEFAULT 0  COMMENT '时间长度按天算',
PRIMARY KEY  (`id`)
) DEFAULT CHARSET=UTF8 COMMENT  '用户付费记录表';