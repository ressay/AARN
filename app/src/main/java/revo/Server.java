package revo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Server {
	MainActivity activity;
	ServerSocket serverSocket;
	String message = "";
	static final int socketServerPORT = 8081;
	BufferedReader input;
	PrintWriter output;
 
	public Server(MainActivity activity) {
		this.activity = activity;
		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}
 
	static public int getPort() {
		return socketServerPORT;
	}
 
	public void onDestroy() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
 
	private class SocketServerThread extends Thread {
 
		int count = 0;
 
		@Override
		public void run() {
			try {
				// create ServerSocket using specified port
				serverSocket = new ServerSocket(socketServerPORT);
				String msg = "";
				while (true) {
					// block the call until connection is created and return
					// Socket object
					Socket socket = serverSocket.accept();
					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					output = new PrintWriter(socket.getOutputStream(),true);
					msg = input.readLine();

					if(msg.equals("connect"))
						message = "connected";

					count++;

 

					SocketServerReplyThread socketServerReplyThread = 
							new SocketServerReplyThread(socket, count);
					socketServerReplyThread.run();
 
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
 
	private class SocketServerReplyThread extends Thread {
 
		private Socket hostThreadSocket;
		int cnt;
 
		SocketServerReplyThread(Socket socket, int c) {
			hostThreadSocket = socket;
			cnt = c;
		}
 
		@Override
		public void run() {
			OutputStream outputStream;
			String msgReply = "Hello from Server, you are #" + cnt;
 
			try {
				outputStream = hostThreadSocket.getOutputStream();
				PrintStream printStream = new PrintStream(outputStream);
				printStream.print(message);
				printStream.close();
 
				message += "replayed: " + msgReply + "\n";
 

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message += "Something wrong! " + e.toString() + "\n";
			}
 
		}
 
	}
 
	public String getIpAddress() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
						.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
						.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress
							.nextElement();
 
					if (inetAddress.isSiteLocalAddress()) {
						ip += "Server running at : "
								+ inetAddress.getHostAddress();
					}
				}
			}
 
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}
		return ip;
	}
}
