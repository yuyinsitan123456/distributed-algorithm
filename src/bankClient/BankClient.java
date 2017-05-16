/*COMP90020 project assessment
 * 2017
 * Group member :
 * 732355 
 * 732329
 * 776991
 * 756344
 * */
package bankClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;


public class BankClient {
	private String clientID;
	private  Socket clientSocket;
	private BufferedReader in;
	private BufferedWriter out;
	public BankClient(String host, int port) throws UnsupportedEncodingException, IOException {
		super();
		this.clientID = "JZ";
		this.clientSocket = new Socket(host, port);
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
		this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
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
	public BufferedReader getIn() {
		return in;
	}
	public BufferedWriter getOut() {
		return out;
	}
	public void StartListen(){
		try{
	
			//create a thread to receive the server's message
			Thread receiveThread = new Thread(new MessageReceiveThread(clientSocket,in));
			receiveThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
