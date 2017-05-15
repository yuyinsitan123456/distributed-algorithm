package bank;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class BankClient {
	private String clientID;
	private Socket clientSocket;
	public BankClient(String clientID, Socket clientSocket) {
		super();
		this.clientID = clientID;
		this.clientSocket = clientSocket;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	

}
