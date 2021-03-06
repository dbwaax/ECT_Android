## 交通时空大数据分析挖掘系统(Android 客户端)


## 1.什么是交通时空大数据分析挖掘系统？
交通时空大数据分析挖掘系统：设计并实现了一种交通时空大数据分析挖掘系统。系统的主要组成部分为:云服务器、移动端应用(安卓)、windows端应用。  
为了实现高效的分布式处理过程,系统的分布式环境采用spark+hadoop,在利用hdfs分布式存储的情况下充分发挥出spark快速处理分布式数据的优势,并支持用户采用pyspark + jupyter notebook对任务程序进行编写并提交任务运行。在分布式系统中对手机信令数据的处理主要分为:数据上传与数据抓取、数据清洗、数据处理、数据分析、数据可视化。


## 2.基于什么开发？
移动端APP的设计与开发主要基于Android Studio与对应SDK 开发环境，主要编程语言为Java。


## 3.功能介绍
主要功能包括：用户登录、二维码扫描、注册、密码重置、数据上传、数据可视化处理、数据结论可视化、消息推送接收等。  
<div align=center><img src="https://github.com/dbwaax/ECT_Android/blob/main/image/data_process.png"/></div> 

**①用户登陆、注册、密码重置**  
>在用户登录上采用文本式登陆，将用户输入的登陆信息MD5加密后发送HTTP请求到登陆检测接口。数据库查询用户输入信息的准确性并给予反馈。同时在用户登陆后，APP会在本地记录用户的登陆状态，下次用户再次打开APP时便不再需要输入账号与密码。同时在用户的注册于密码重置上，我们沿用了web前端的页面使用WebView在手机APP页面直接对web页面进行展示，并保留了注册界面的安全验证功能。  
><div align=center><img src="https://github.com/dbwaax/ECT_Android/blob/main/image/login.png"/></div>    

**②数据上传、数据可视化处理**  
>数据上传直接调用服务器的文件上传接口对本地的用户文件进行上传，需要注意的是为了打开Android手机的文件管理器与完成文件上传，需要在代码中对文件读写、网络连接权限的注册。
>  ```xml
>    <uses-permission android:name="android.permission.INTERNET" />
>    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
>    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
>  ```
>数据可视化处理的实现方法为调用服务器的数据处理接口，并建立线程持续接受接口回执数据，显示在限定的TextView中。  

**③二维码扫描**  
>为了方便用户在PC上的登陆，APP上自带有二维码扫描，用来配合部署在服务器上的二维码登录功能。为了使用手机摄像头来对二维码进行扫描需要在权限中注册摄像头权限，并在后续程序中对摄像头权限进行动态申请。
>二维码的识别主要是利用了第三方库，在APP扫描到当前Web前端产生的10位随机码后，会自动弹出是否确认登陆的页面，用户若点击确认则手机APP将会向服务器的二维码登录确认接口发送当前用户的账户名、10位随机码与令牌相关信息，服务器接口接受到信息后会将当前10位随机码对应令牌信息的激活状态改为1，这样Web前端页面就可以正确跳转了。  
>静态注册权限（动态注册权限略）：
>  ```xml
> 	<uses-permission android:name="android.permission.FLASHLIGHT" />
>	  <uses-permission android:name="android.permission.CAMERA" />
>	  <uses-feature android:name="android.hardware.camera" />
> 	<uses-feature android:name="android.hardware.camera. Autofocus" />
>  ```
><div align=center><img src="https://github.com/dbwaax/ECT_Android/blob/main/image/QR_verify.png"/></div>  

**④数据可视化**  
>在手机APP的数据可视化上，主要使用了Echart + HelloChart +WebView组合的方式对数据进行可视化展示，本身手机APP不负责任何计算工作，只需要通过HTTP请求获取到对应的数据并重绘数据图表即可实现简单的数据可视化。在这个基础上通过定时器还增加了定时自动刷新数据的功能，可使得数据实时的刷新。

**⑤推送接受服务**  
>在服务器有配置MQTT服务，并提供了定时的消息推送。为了在APP打开的时候正常的获取到推送的内容，需要将APP客户端看作一个订阅者，在APP初始化时就进行MQTT消息的订阅，这样就可以成功的获取订阅数据，也就是定时推送消息。
><div align=center><img src="https://github.com/dbwaax/ECT_Android/blob/main/image/MQTT.png"/></div>
><div align=center><img width="500" height="200" src="https://github.com/dbwaax/ECT_Android/blob/main/image/notify.jpg"/></div>

## 4.效果展示
<div align=center><img width="500" height="1000" src="https://github.com/dbwaax/ECT_Android/blob/main/image/app1.jpg"/></div>
<div align=center><img width="500" height="1000" src="https://github.com/dbwaax/ECT_Android/blob/main/image/app2.jpg"/></div>
<div align=center><img width="500" height="1000" src="https://github.com/dbwaax/ECT_Android/blob/main/image/app3.jpg"/></div>
<div align=center><img width="500" height="1000" src="https://github.com/dbwaax/ECT_Android/blob/main/image/app4.jpg"/></div>.
<div align=center><img width="500" height="1000" src="https://github.com/dbwaax/ECT_Android/blob/main/image/app5.jpg"/></div>

## License

[CC0 1.0 (Public Domain)](LICENSE.md)
