package com.zhao.lazy.server;

import org.eclipse.jetty.server.Server;

public class ServerJetty extends Thread{

	private int port;
	public ServerJetty() {}
	public ServerJetty(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			Server server = new Server(port);
	        server.setHandler(new ServerHandler());
	        server.start();
	        server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
