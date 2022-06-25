package org.smile.framework.util;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smile.framework.callback.RequestCallBack;
import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkResponse;

import java.net.InetSocketAddress;

public class ServerUtil {

	private static Logger logger = LoggerFactory.getLogger(ServerUtil.class);

	@SuppressWarnings("resource")
	public static void accept(int port, final RequestCallBack requestCallBack) {
		try {
			IoAcceptor acceptor = new NioSocketAcceptor();
			// 增加日志过滤器
			acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			// 这里不添加字符编码过滤器
			// 指定业务逻辑处理器
			acceptor.setHandler(new ServerHandler(requestCallBack));
			// 设置buffer的长度
			acceptor.getSessionConfig().setReadBufferSize(2048);
			// 设置连接超时时间
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30000);
			// 绑定端口
			acceptor.bind(new InetSocketAddress(port));
		} catch (Exception e) {
			logger.error("Server Start Failure!", e);
		}
	}

	private static class ServerHandler implements IoHandler {

		private RequestCallBack requestCallBack;

		public ServerHandler(RequestCallBack requestCallBack) {
			this.requestCallBack = requestCallBack;
		}

		@Override
		public void sessionCreated(IoSession ioSession) throws Exception {
			logger.info("Session Created[sessionId:{}]", ioSession.getId());
		}

		@Override
		public void sessionOpened(IoSession ioSession) throws Exception {
			logger.info("Session Opened[sessionId:{}]", ioSession.getId());
		}

		@Override
		public void sessionClosed(IoSession ioSession) throws Exception {
			logger.info("Session Closed[sessionId:{}]", ioSession.getId());
		}

		@Override
		public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
			logger.info("Session Idle[sessionId:{}]", ioSession.getId());
		}

		@Override
		public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
			String errorMessage = String.format("Session Raise Exception![sessionId:%d]", ioSession.getId());
			logger.error(errorMessage, throwable);
		}

		@Override
		public void messageReceived(IoSession ioSession, Object message) throws Exception {
			logger.info("Session MessageReceived[sessionId:{}]", ioSession.getId());
			IoBuffer ioBuffer = (IoBuffer)message;
			byte[] data = new byte[ioBuffer.limit()];
			ioBuffer.get(data);

			NetworkRequest networkRequest = (NetworkRequest)ProtocolUtil.transToObject(data);
			NetworkResponse networkResponse = requestCallBack.onService(networkRequest);
			networkResponse.setSequence(networkRequest.getSequence());

			byte[] reponseByte = ProtocolUtil.transToByte(networkResponse);
			ioSession.write(IoBuffer.wrap(reponseByte));
		}

		@Override
		public void messageSent(IoSession ioSession, Object o) throws Exception {
			logger.info("Session MessageSent[sessionId:{}]", ioSession.getId());
		}
	}
}