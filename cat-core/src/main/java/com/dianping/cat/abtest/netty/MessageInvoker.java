package com.dianping.cat.abtest.netty;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.oio.OioDatagramChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

public class MessageInvoker {
	private ConnectionlessBootstrap bootstrap;

	private DatagramChannel channel;

	private InetAddress m_multicastAddress;

	private String m_multicastHost;

	private int m_port;

	private ChannelHandler handler;

	public MessageInvoker(String multicastHost, int port) {
		m_multicastHost = multicastHost;
		m_port = port;
		try {
			m_multicastAddress = InetAddress.getByName(m_multicastHost);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("Invalid host: " + m_multicastHost);
		}
		if (!m_multicastAddress.isMulticastAddress()) {
			throw new IllegalArgumentException("Not a Multicast Address: " + m_multicastAddress);
		}
	}

	public String getMulticastHost() {
		return m_multicastHost;
	}

	public int getPort() {
		return m_port;
	}

	public ChannelHandler getHandler() {
		return handler;
	}

	public void setHandler(ChannelHandler handler) {
		this.handler = handler;
	}

	// 监听单播端口，加入组播，注册收发到消息的处理者
	public void init() {
		DatagramChannelFactory factory = new OioDatagramChannelFactory(Executors.newCachedThreadPool());

		ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(factory);

		// 注册收发到消息的处理者
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new StringEncoder(CharsetUtil.ISO_8859_1), new StringDecoder(
				      CharsetUtil.ISO_8859_1), handler);
			}
		});

		// 绑定监听的单播/组播的端口
		channel = (DatagramChannel) bootstrap.bind(new InetSocketAddress(m_port));

		// 加入组播地址
		channel.joinGroup(m_multicastAddress);

	}

	public void sendMessage(String msg) {
		channel.write(msg, new InetSocketAddress(m_multicastHost, m_port));
	}

	public void close() {
		channel.unbind();
		bootstrap.releaseExternalResources();
	}

}
