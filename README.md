# depot-manager
## 技术选型

### 后端技术

技术 | 名称 
----|----
Spring Boot | 容器+MVC框架 | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
Spring Security | 认证和授权框架 | [https://spring.io/projects/spring-security](https://spring.io/projects/spring-security)
MyBatis Plus | ORM框架  | [https://mp.baomidou.com/)
MyBatisGenerator | 数据层代码生成 | [http://www.mybatis.org/generator/index.html](http://www.mybatis.org/generator/index.html)
PageHelper | MyBatis物理分页插件 | [http://git.oschina.net/free/Mybatis_PageHelper](http://git.oschina.net/free/Mybatis_PageHelper)
Swagger-UI | 文档生产工具 | [https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui)
Hibernator-Validator | 验证框架 | [http://hibernate.org/validator/](http://hibernate.org/validator/)
RabbitMq | 消息队列 | [https://www.rabbitmq.com/](https://www.rabbitmq.com/)
MSMQ | 微软消息队列 | [https://docs.microsoft.com/zh-cn/dotnet/framework/wcf/diagnostics/tracing/msmq)
Redis | 分布式缓存 | [https://redis.io/](https://redis.io/)
Docker | 应用容器引擎 | [https://www.docker.com/](https://www.docker.com/)
Druid | 数据库连接池 | [https://github.com/alibaba/druid](https://github.com/alibaba/druid)
JWT | JWT登录支持 | [https://github.com/jwtk/jjwt](https://github.com/jwtk/jjwt)
LogStash | 日志收集 | [https://github.com/logstash/logstash-logback-encoder](https://github.com/logstash/logstash-logback-encoder)
Lombok | 简化对象封装工具 | [https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok)

### 前端技术

技术 | 名称 
----|----
Vue | 前端框架
Vue-router | 路由框架
Vuex | 全局状态管理框架
Element | 前端UI框架
Axios | 前端HTTP框架
Js-cookie | cookie管理工具

### 框架搭建

功能 | 完成 
----|----
集成MyBatis PLUS | ✔
集成MyBatisGenerator | ✔
集成SpringSecurity | ✔
集成Swagger-UI | ✔
集成Hibernator-Validator | ✔
集成日志功能 | ✔
集成监控功能 | ✔
crud操作demo | ✔
合理规划包结构 | ✔
SpringAOP通用日志处理 | ✔
SpringAOP通用验证失败结果返回 | ✔
CommonResult对通用返回结果进行封装 | ✔
SpringSecurity登录改为Restful形式 | ✔
JWT登录、注册、获取token | ✔

JTA事务处理 | ✔
集成单元测试 | ✔
OSS上传功能 | ✔
Elasticsearch搜索功能 | ✔
HTTPS支持 | ✔
Redis数字型ID生成 | ✔
SpringTask定时任务支持 | ✔
docker容器化部署 | ✔
配置区分生产和测试环境 | ✔
ELK日志收集功能 | ✔
RabbitMq异步通信 | ✔
RestTemplate服务间调用 | ✔
SpringSecurity权限管理功能 | ✔

### 使用工具

工具 | 下载地址 
----|----
开发工具idea | https://www.jetbrains.com/idea/download
redis客户端连接工具 | https://redisdesktop.com/download
本地host管理 | https://oldj.github.io/SwitchHosts/
Linux远程连接工具 | http://www.netsarang.com/download/software.html
数据库连接工具 | http://www.formysql.com/xiazai.html
数据库设计工具 | http://powerdesigner.de/
原型设计工具 | https://www.axure.com/
思维导图设计工具 | http://www.edrawsoft.cn/mindmaster
gif录制工具 | https://www.screentogif.com/

# 设置IDEA快捷键为Eclipse风格

- 点击File->Settings->Keymap，选择快捷键风格为Eclipse
- 按如下表格中的英文描述进行搜索，并改为相应快捷键

Eclipse | IDEA | 英文描述 | 中文描述
----|----|----|----
ctrl+shift+r	|ctrl+shift+r	|Navigate->File	|找工作空间的文件
ctrl+shift+t	|ctrl+shift+t	|Navigate->Class	|找类定义
ctrl+shift+g	|ctrl+shift+g	|Edit->Find->Find Usages	|查找方法在哪里调用.变量在哪里被使用
ctrl+t	|ctrl+t	|Other->Hierarchy Class	|看类继承结构
ctrl+o	|ctrl+o	|Navigate->File Structure	|搜索一个类里面的方法
shift+alt+z	|shift+alt+z	|Code->Surround With	|生成常见的代码块
shift+alt+l	|shift+alt+l	|Refactor->Extract->Variable	|抽取变量
shift+alt+m	|shift+alt+m	|Refactor->Extract->Method	|抽取方法
alt+left	|alt+left	|Navigate->Back	|回退上一个操作位置
alt+right	|alt+right	|Navigate->Forward	|前进上一个操作位置
ctrl+home	|ctrl+home	|Move Caret to Text Start	|回到类最前面
ctrl+end	|ctrl+end	|Move Caret to Text End	|回到类最后面
ctrl+2 L	|shift+alt+l	|Refactor->Extract->Variable	|抽取变量
ctrl+e	|alt+r	|View->Recent Files	|最近打开的文件
ctrl+w	|ctrl+w	|Close	|关闭当前窗口
alt+/	|alt+/	|Code->Completion->Basic	|提示变量生成
ctrl+1	|ctrl+1	|Other->Show Intention Actions	|提示可能的操作
ctrl+h	|ctrl+h	|Find in Path	|全局搜索
alt+上/下箭头	|alt+上/下箭头	|Code->Move Line Up/Down	|移动一行代码
ctrl+alt+上/下箭头	|ctrl+alt+上/下箭头	|Editor Actions->Duplicate Lines	|复制一行
ctrl+shift+j	|ctrl+shift+j	|Other->Fix doc comment	|方法注释
暂无|alt+enter	|Other->Show Intention Actions	|提示常见操作
Ctrl+F	|Ctrl+F/Ctrl+R	|Find/Replace	|查找替换
Shift+Enter	|Shift+Enter	|Start New Line	|开启新的一行
Ctrl+Alt+S	|Ctrl+Alt+S	|Generate	|生成getter,setter,tostring等

### 数据库规范
表名 | 表描述
----|----
p_开头的表 | 基础类表

### 数据库表以及javabean实体类、表描述规范
表名 | javabean名称 | 描述
----|----|----
p_business_type	|PBusinessType	|业务类型表
p_enterprise_type	|PEnterpriseType 	|企业类型表
p_goods_arr_status	|PGoodsArrStatus	|到货状态表
p_goods_status	|PGoodsStatus	|货物状态表
p_goods_type	|PGoodsType	|货物类型表
p_pack_type	|PPackType	|包装类型表
p_work_sheet_status	|PWorkSheetStatus	|作业单状态表
p_area_code	|PAreaCode	|监管区域表
p_conta_model	|PContaModel	|箱型表
p_conta_op_type	|PContaOptype	|集装箱操作类型表
p_customs_code	|PCustomsCode	|海关代码表
p_transport_type	|PTransportType	|运输方式类型表
c_discharges	|CDischarges	|装卸货地表
c_enterprises	|CEnterprises	|企业信息表
c_station_area_positions	|CStationAreaPositions	|堆位表
c_station_areas	|CStationAreas	|堆区表
c_stations	|CStations	|场站表
c_vehicle	|CVehicle	|车辆备案表


### 全局错误码以及错误描述（后续不断完善中）
code | message
----|----
0000 | succcess
1001 | 空指针异常
1002 | 类型强制转换异常
1003 | 链接失败异常
1004 | 传递非法参数异常
1005 | 数字格式异常
1006 | 下标越界异常
1007 | 安全异常
1008 | 数据库异常
1009 | 算术运算异常
1010 | 数据库主键冲突
1011 | 缺少所需的请求主体
1012 | 不支持请求方法（例如：不支持请求方法Request method 'PATCH' not supported）
1013 | 运行时异常
9998 | 业务异常（显示自定义的业务异常描述）
9999 | 未知异常