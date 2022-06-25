package org.smile.framework.util;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkResponse;
import org.smile.framework.rpc.NetworkSession;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class NetworkUtil {

	private static Logger logger = LoggerFactory.getLogger(NetworkUtil.class);

	private static AtomicLong requestSequence = new AtomicLong(0);

	private static Map<String, IoSession> ioSessionMap = new ConcurrentHashMap<String, IoSession>();

	private static Map<Long, String> ioSessionIdMap = new ConcurrentHashMap<Long, String>();

	private static Map<Long, NetworkSession> rpcSessionMap = new ConcurrentHashMap<Long, NetworkSession>();

	private static ClientHandler clientHandler = new ClientHandler();

	public static void sendRequest(String address, int port, NetworkSession networkSession) throws Exception {

		IoSession session = prepareConnection(address, port);
		NetworkRequest networkRequest = networkSession.getNetworkRequest();
		networkRequest.setSequence(requestSequence.getAndIncrement());
		byte[] data = ProtocolUtil.transToByte(networkRequest);
		session.write(IoBuffer.wrap(data));
		synchronized (networkSession) {
			rpcSessionMap.put(networkRequest.getSequence(), networkSession);
			while (!networkSession.isFinish()) {
				networkSession.wait();
			}
		}
	}

	private static IoSession prepareConnection(String address, int port) {

		String key = String.format("session-%s-%d", address, port);
		if (ioSessionMap.containsKey(key)) {
			return ioSessionMap.get(key);
		}

		NioSocketConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8"))));
		connector.setConnectTimeout(300);
		connector.setHandler(clientHandler);// 设置事件处理器
		ConnectFuture connectFuture = connector.connect(new InetSocketAddress(address, port));
		connectFuture.awaitUninterruptibly();
		ioSessionMap.put(key, connectFuture.getSession());
		ioSessionIdMap.put(connectFuture.getSession().getId(), key);
		return connectFuture.getSession();
	}

	private static class ClientHandler implements IoHandler {

		public void sessionCreated(IoSession ioSession) throws Exception {
			logger.info("Session Created[sessionId:{}]", ioSession.getId());
		}

		public void sessionOpened(IoSession ioSession) throws Exception {
			logger.info("Session Opened[sessionId:{}]", ioSession.getId());
		}

		public void sessionClosed(IoSession ioSession) throws Exception {
			logger.info("Session Closed[sessionId:{}]", ioSession.getId());
			String key = ioSessionIdMap.get(ioSession.getId());
			ioSessionIdMap.remove(ioSession.getId());
			ioSessionMap.remove(key);
		}

		public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
			logger.info("Session Idle[sessionId:{}]", ioSession.getId());
		}

		public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
			String errorMessage = String.format("Session Raise Exception![sessionId:%d]", ioSession.getId());
			String key = ioSessionIdMap.get(ioSession.getId());
			ioSessionIdMap.remove(ioSession.getId());
			ioSessionMap.remove(key);
			logger.error(errorMessage, throwable);
		}

		public void messageReceived(IoSession ioSession, Object message) throws Exception {
			logger.info("Session MessageReceived[sessionId:{}]", ioSession.getId());
			IoBuffer ioBuffer = (IoBuffer)message;
			byte[] data = new byte[ioBuffer.limit()];
			ioBuffer.get(data);

			NetworkResponse networkResponse = (NetworkResponse)ProtocolUtil.transToObject(data);
			if (rpcSessionMap.containsKey(networkResponse.getSequence())) {
				NetworkSession rpcSession = rpcSessionMap.get(networkResponse.getSequence());
				rpcSessionMap.remove(networkResponse.getSequence());
				synchronized (rpcSession) {
					rpcSession.setFinish(true);
					rpcSession.notifyAll();
				}
			}
		}

		public void messageSent(IoSession ioSession, Object message) throws Exception {
			logger.info("Session MessageSent[sessionId:{}]", ioSession.getId());
		}
	}
}
