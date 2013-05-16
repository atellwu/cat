package com.dianping.cat.abtest.netty.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.dianping.cat.abtest.netty.MessageInvoker;

public class MessageServer {

    private MessageInvoker messageInvoker;

    public MessageServer(String multicastHost, int port) {
        messageInvoker = new MessageInvoker(multicastHost, port);
        messageInvoker.setHandler(new MyHandler());
        messageInvoker.init();
    }

    // @cron("0 0/1 * * * *")
    public void sendInterval() {
        // TODO 定时发出abtest列表
        String msg = "server:update abtest list";
        messageInvoker.sendMessage(msg);
    }

    // @destory
    public void close() {
        messageInvoker.close();
    }

    static class MyHandler extends SimpleChannelUpstreamHandler {

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            // TODO 如果是来自client的消息，则更新client list，并根据其domain，回复abtest 列表
            String msg = e.getMessage().toString();
            if (msg.startsWith("client:")) {
                System.out.println("Receive msg:" + msg.substring(7));
                e.getChannel().write("server: init abtest list", e.getRemoteAddress());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            e.getCause().printStackTrace();
        }
    }

}
