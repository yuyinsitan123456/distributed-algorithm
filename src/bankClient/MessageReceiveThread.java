package bankClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;




public class MessageReceiveThread implements Runnable{
	private Socket socket;
	private BufferedReader in;
	private ArrayList messageList;
	private boolean run = true;
	public MessageReceiveThread(Socket socket, BufferedReader in) {
		super();
		this.socket = socket;
		this.in = in;
		this.messageList = new ArrayList<String>();

	}
	
	@Override
	public void run() {

		try {
			//this.in = new BufferedReader(new InputStreamReader(
			//		socket.getInputStream(), "UTF-8"));
			while (run) {
				String message = in.readLine();
				this.messageList.add(message);
				ClientGUI.ShowMessage(this.messageList);
			}
			this.messageList.add("123");
			this.messageList.add("456");
			ClientGUI.ShowMessage(this.messageList);
			System.exit(0);
			in.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Communication Error: " + e.getMessage());
			System.exit(1);
		}

	}



}
