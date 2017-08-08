package com.example.deken.myapplication;


import com.epaylinks.appupdate.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class JConnection11 extends Thread {
	public static final int CONNECTION_CLIENT_TYPE = 2;
	private static final String TAG ="JConnection";
	private int m_type;
	private Socket m_socket;
	private ServerSocket m_serversocket;
	private String m_host;
	private int m_port;
	private byte m_buffer[];
	private InputStream m_inputstream;
	private BufferedInputStream m_bufferedinputstream;
	private ByteArrayOutputStream m_bufferedoutputstream;
	private boolean connected;
	private int soTimeout = 50000;
	private int connectTimeout = 60000;
	private ReadDataTimeoutListener mReadDataTimeoutListener;

	public int connect(String host, int port) {
		if (connected) {
			return 0;
		}
		m_host = host;
		m_port = port;

		if (m_type == 1) {
			try {
				m_serversocket = new ServerSocket(m_port);
				m_port = m_serversocket.getLocalPort();
				m_socket = m_serversocket.accept();
			} catch (IOException e) {
				return -1;
			}
		} else {//客户端连接
			try {
				SocketAddress socketAddress = new InetSocketAddress(m_host, m_port);
				m_socket = new Socket();
				m_socket.connect(socketAddress, connectTimeout);
				m_socket.setSoTimeout(soTimeout);
				m_inputstream = m_socket.getInputStream();
				m_bufferedoutputstream = new ByteArrayOutputStream();
				m_bufferedinputstream = new BufferedInputStream(m_inputstream);
			} catch (IOException e) {
				return -1;
			}
		}
		connected = true;
		return 0;
	}

	public synchronized int disconnect() {
		if (!connected) {
			return 0;
		}
		try {
			if (m_bufferedinputstream != null) {
				m_bufferedinputstream.close();
				LogUtils.d(TAG,"m_bufferedinputstream close");
			}
			if (m_bufferedoutputstream != null) {
				m_bufferedoutputstream.flush();
				m_bufferedoutputstream.close();
				LogUtils.d(TAG,"m_bufferedoutputstream close");
			}
		} catch (IOException e) {
			LogUtils.d(TAG,"m_bufferedinputstream close err");
			LogUtils.d(TAG,"m_bufferedoutputstream close");
		}
		try {
			m_socket.close();
			LogUtils.d(TAG,"m_socket close");
		} catch (IOException e) {
			return -1;
		}
		connected = false;
		return 0;
	}

	public String read() {
		if (!connected) {
			return null;
		}
//		byte[] datalen=new byte[6];
		int len = -1;
//		try {
//			m_inputstream.read(datalen);
//			int length = Integer.parseInt(new String(datalen));
//			LogUtils.i("deken","the head"+length);
//			byte[] bytes=new byte[length];
//			len = m_bufferedinputstream.read(bytes, length-datalen.length, bytes.length);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		m_buffer=new byte[1024*1024*2];//最大的缓冲区是237M Failed to allocate a 1073741836 byte allocation with 13255514 free bytes and 237MB until OOM
		try {
			byte[] datalen=new byte[6];
			m_inputstream.read(datalen);
			LogUtils.i("deken","the head befaul"+new String(datalen));
			int length = Integer.parseInt(new String(datalen));
			LogUtils.i("deken","the head"+length);
			byte[] realData=new byte[length];
			while((len = m_bufferedinputstream.read(realData, 0, realData.length))!=-1){
				LogUtils.i("deken","while  len : " +len);
//				m_bufferedoutputstream.write(realData,0,realData.length);
//				System.arraycopy();
			}


		}catch (Exception e) {
			LogUtils.i("deken","e exception " +e.toString());
		}
		if (len == -1) {
			return null;
		}
		LogUtils.d(TAG,"connect read len: "+len);
//		byte ret[] = m_bufferedoutputstream.toString().getBytes();
/*		for (int i = 0; i < len; i++) {
			ret[i] = m_buffer[i];
		}*/
		return m_bufferedoutputstream.toString();
	}

	public int write(byte toWrite[]) {//一次性发送
		if (toWrite == null) {
			return 0;
		} else {
			return write(toWrite, 0, toWrite.length);
		}
	}

	private int write(byte toWrite[], int start, int end) {
		if (toWrite == null) {
			return 0;
		}
		if (toWrite.length == 0) {
			return 0;
		}
		if (!connected) {
			return 0;
		}
		try {
			m_bufferedoutputstream.write(toWrite, start, end);
			m_bufferedoutputstream.flush();
		} catch (IOException e) {
			return -1;
		}
		return 0;
	}

	public boolean isConnected() {
		return connected;
	}

	public int getLocalPort() {
		return m_port;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	/**
	 * the read data timeout
	 * @param soTimeout milliseconds
	 */
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * the read connect timeout
	 * @param connectTimeout milliseconds
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 设置读取数据超时监听
	 * @param l
	 */
	public void setReadDataTimeoutListener(ReadDataTimeoutListener l){
		this.mReadDataTimeoutListener=l;
	}
	public interface ReadDataTimeoutListener{
		void  isReadTimeout(boolean isTimeout);
	}
}
