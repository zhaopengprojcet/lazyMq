# lazyMq  
###### 自测使用MQ , 未完，不断更新  
  
>##### 2019-07-02  
###### 重构了项目结构，将common工程包、db包与核心项目分离，方便后期对于db的扩展和迁移
  
>#### 近期功能需求  
###### 一.服务端  
###### 1.核心功能  
###### ~~1.  服务端数据存储和结构设置~~   
###### ~~2.  服务端存储内容读取和更新删除通用工具~~   
###### ~~3.  服务端同步队列底层实现~~   
###### ~~4.  服务端异步队列底层实现~~   
###### ~~5.  ~~待发送队列的结构和逻辑处理~~    
###### ~~6.  重试队列的结构和逻辑处理~~    
###### ~~7.  死信队列的结构和逻辑处理~~  
###### 8.  服务端可视化服务工具包  
 ...
###### 2.接收客户端注册请求  
###### ~~1.  客户端发送注册请求所需要的账户和密码在服务端的存储和调用~~    
###### ~~2.  客户端发送注册消息体结构~~    
###### ~~3.  客户端发送请求解密方式~~   
###### ~~4.  客户端发送请求内容校验~~   
###### ~~5.  注册内容在服务端的存储方式和调用方式~~   
  ...  
###### 3.接收客户端发送的消息体  
###### ~~1.  ~~客户端发送的MQ消息体结构~~   
###### ~~2.  ~~客户端发送的MQ消息解密方式~~   
###### ~~3.  ~~客户端发送的MQ消息内容效验方式~~   
 ...  
##### 二.客户端  
###### ~~1.  读取配置文件方式~~   
###### ~~2.  设置消息监听注解和发送消息通用接口~~   
###### ~~3.  消息体结构~~   
###### ~~4.  消息内容加密方式~~   
###### ~~5.  收到监听消息后通用回调~~   
 ...  
  
