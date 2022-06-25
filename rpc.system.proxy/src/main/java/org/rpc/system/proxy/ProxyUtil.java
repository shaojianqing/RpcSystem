package org.rpc.system.proxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.rpc.system.proxy.callback.ProxyCallBack;
import org.smile.framework.rpc.data.RpcRequest;

public class ProxyUtil {
	
	public static void proxy(int proxyPort, ProxyCallBack callBack) {
		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(proxyPort);			
			while(true) {
				
				Socket socket = serverSocket.accept();				
				InputStream is = socket.getInputStream();
				byte[] result = IOUtils.toByteArray(is);
				
				RpcRequest rpcContext = callBack.onProxy(result);
				
				Socket proxySocket = new Socket(rpcContext.getServerUrl(), 6666);
				OutputStream proxyOs = proxySocket.getOutputStream();
				proxyOs.write(result);
				proxyOs.flush();
				proxySocket.shutdownOutput();
				
				InputStream proxyIs = proxySocket.getInputStream();
				InputStreamReader proxyIsr = new InputStreamReader(proxyIs);
				BufferedReader proxyBr = new BufferedReader(proxyIsr);
				StringBuilder responseStringBuilder = new StringBuilder();
				String responseInfo;
				while((responseInfo = proxyBr.readLine())!=null) {
					responseStringBuilder.append(responseInfo);
				}
				
				proxySocket.close();
				
				callBack.onResult(responseStringBuilder.toString());
				
				OutputStream os = socket.getOutputStream();
				PrintWriter pw =new PrintWriter(os);
				pw.write(responseStringBuilder.toString());
				pw.flush();
				socket.shutdownOutput();			
				
				socket.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
