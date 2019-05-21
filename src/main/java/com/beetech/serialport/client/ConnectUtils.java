package com.beetech.serialport.client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectUtils {

    public static final int REPEAT_TIME = 5;//表示重连次数
    public static final String HOST = "gtw1.wendu114.com";//IP地址
//    public static final String HOST = "192.168.31.173";//本地调试IP地址
//    public static final String HOST = "192.168.1.102";//本地调试IP地址,家里
    public static final int PORT = 8089;//端口号
    public static final int IDLE_TIME = 60*10;//客户端10分钟内没有向服务端发送数据

    public static final int TIMEOUT = 5;//设置连接超时时间,超过5s还没连接上便抛出异常

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String stringNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

}
