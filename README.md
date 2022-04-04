## 交通时空大数据分析挖掘系统(Android 客户端)


## 1.什么是交通时空大数据分析挖掘系统？
交通时空大数据分析挖掘系统：设计并实现了一种交通时空大数据分析挖掘系统。系统的主要组成部分为:云服务器、移动端应用(安卓)、windows端应用。  
为了实现高效的分布式处理过程,系统的分布式环境采用spark+hadoop,在利用hdfs分布式存储的情况下充分发挥出spark快速处理分布式数据的优势,并支持用户采用pyspark + jupyter notebook对任务程序进行编写并提交任务运行。在分布式系统中对手机信令数据的处理主要分为:数据上传与数据抓取、数据清洗、数据处理、数据分析、数据可视化。


## 2.基于什么开发？
移动端APP的设计与开发主要基于Android Studio与对应SDK 开发环境，主要编程语言为Java。


## 3.功能介绍
主要功能包括：用户登录、二维码扫描、注册、密码重置、数据上传、数据可视化处理、数据结论可视化、消息推送接收等。  
**①用户登陆、注册、密码重置**  
>在用户登录上采用文本式登陆，将用户输入的登陆信息MD5加密后发送HTTP请求到登陆检测接口。数据库查询用户输入信息的准确性并给予反馈。同时在用户登陆后，APP会在本地记录用户的登陆状态，下次用户再次打开APP时便不再需要输入账号与密码。同时在用户的注册于密码重置上，我们沿用了web前端的页面使用WebView在手机APP页面直接对web页面进行展示，并保留了注册界面的安全验证功能。  

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
>静态注册权限：
>  ```xml
>	<uses-permission android:name="android.permission.FLASHLIGHT" />
>	<uses-permission android:name="android.permission.CAMERA" />
>	<uses-feature android:name="android.hardware.camera" />
>	<uses-feature android:name="android.hardware.camera. Autofocus" />
>  ```
> 动态注册权限略

**③二维码扫描**  
## 4.效果展示
![image](https://github.com/dbwaax/ECT_windows/blob/master/image/windows1.png)  
![image](https://github.com/dbwaax/ECT_windows/blob/master/image/windows2.png)  
![image](https://github.com/dbwaax/ECT_windows/blob/master/image/windows3.png)  
![image](https://github.com/dbwaax/ECT_windows/blob/master/image/windows4.png)  
![image](https://github.com/dbwaax/ECT_windows/blob/master/image/windows5.png)  
![image](https://github.com/dbwaax/ECT_windows/blob/master/image/windows6.png)  
![image](https://github.com/dbwaax/ECT_windows/blob/master/image/windows7.png)  
![image](https://github.com/dbwaax/ECT_windows/blob/master/image/wwwww1w1w1w1.png)  



## License

[CC0 1.0 (Public Domain)](LICENSE.md)
