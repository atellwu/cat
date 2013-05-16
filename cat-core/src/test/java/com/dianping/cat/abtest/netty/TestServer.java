package com.dianping.cat.abtest.netty;

import java.util.concurrent.TimeUnit;

import com.dianping.cat.abtest.netty.server.MessageServer;

public class TestServer {

    private static String multicastHost = "224.0.0.1";

    private static int port = 8081;

    /**
     * 创建client<br>
     * 收到消息则处理
     * 
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        MessageServer server = new MessageServer(multicastHost, port);
        while (true) {
            server.sendInterval();
            TimeUnit.SECONDS.sleep(5);
        }
    }

}
