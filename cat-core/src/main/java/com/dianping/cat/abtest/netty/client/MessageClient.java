package com.dianping.cat.abtest.netty.client;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.junit.Before;

import com.dianping.cat.abtest.netty.MessageInvoker;

public class MessageClient {

    private MessageInvoker messageInvoker;

    public MessageClient(String multicastHost, int port) {
        messageInvoker = new MessageInvoker(multicastHost, port);
        messageInvoker.setHandler(new MyHandler());
        messageInvoker.init();
    }

    @Before
    public void sendHello() {
        // TODO 定时发出abtest列表
        String msg = "client:hello, i am <domain>";
        messageInvoker.sendMessage(msg);
    }

    // @destory
    public void close() {
        messageInvoker.close();
    }

    static class MyHandler extends SimpleChannelUpstreamHandler {

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            // TODO 如果是来自server的消息，则更新abtest 列表
            String msg = e.getMessage().toString();
            if (msg.startsWith("server:")) {
                System.out.println("Receive msg:" + msg.substring(7));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            e.getCause().printStackTrace();
        }
    }

}
