注：”双人对战“ 有兴趣的童鞋可以自己完善。:laughing: 作者现在没有时间整合:cry:

只需要将exer_in_fight3中的界面整合到ersblocks（单人对战界面）中，用个右下角显示就行了，原本的难度调整功能按钮可以删除（设个默认值），或是另外开一个地方放对方的实时信息面板就行。

exer_in_fight3中已经实现了通信，不过是代码中写死了IP和Port，此时只需加上菜单按钮设置即可（exer_in_fight4）。

# ersblock

俄罗斯方块游戏--JAVA实现（含双人联机对战）  
> 注:本项目实现全部来源于图书馆的一本Java书籍，书名忘记了@。@ 我在学习的时候动手写了一遍，有较为详细的代码注释，用于大家学习交流。侵删。  
> 本游戏项目涉及：JavaSE swing、awt图形编程 、socket通信  
----

## ersblocks
单人对战版本

### exer_in_fight3

含服务端和客户端。用于测试两端是否通信成功。

1. 先运行 ShowServer.java,显示服务端
2. 运行ShowClient.java，显示客户端

### exer_in_fight4

用于界面菜单设置 通信双方IP地址和端口号

ShowConnection.java 


## ersblocks_fight（待整合）
双人联机对战版本