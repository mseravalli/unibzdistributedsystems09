package test;

import static org.junit.Assert.*;

import org.junit.Test;

import server.Server;

import client.Client;

public class ClientTest {
	
	String serverIP = "127.0.0.1";
	int serverPort = 8080;
	String message = "Test message";

	
	Client aClient = new Client();
	Runnable runnableServer = new Server();
	Thread theServer = new Thread (runnableServer);	
	
	@Test
	public void testConnect() {		
		
		assertEquals(false, aClient.connect(serverIP, serverPort, "nickname1"));
		
		theServer.start();
		
		/*
		 * this part is needed in order to wait the server to be up
		 */
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(true, aClient.connect(serverIP, serverPort, "nickname1"));
		
		aClient.disconnect();
		
	}

	@Test
	public void testSendMessage() {		
		
		
		assertEquals(false, aClient.sendMessage(message));
		
		aClient.connect(serverIP, serverPort, "nickname2");
		
		assertEquals(true, aClient.sendMessage(message));
		
		theServer.interrupt();
	}

}
