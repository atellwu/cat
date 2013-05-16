package com.dianping.cat.abtest.netty;

import com.dianping.cat.abtest.netty.client.MessageClient;

public class TestClient {

	private static String multicastHost = "224.0.0.1";

	private static int port = 8081;

	/**
	 * 创建client<br>
	 * 收到消息则处理
	 */
	public static void main(String[] args) {
		MessageClient client = new MessageClient(multicastHost, port);
		client.sendHello();
	}

}
