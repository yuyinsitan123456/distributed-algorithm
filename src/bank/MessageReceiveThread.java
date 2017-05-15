package bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;




public class MessageReceiveThread implements Runnable{
	private Socket socket;
	private BufferedReader in;
	private boolean run = true;
	public MessageReceiveThread(Socket socket, BufferedReader in) {
		super();
		this.socket = socket;
		this.in = in;
	}
	
	@Override
	public void run() {

		try {
			this.in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			while (run) {
				String message = in.readLine();
				ClientGUI.ShowMessage(message);
			}
			System.exit(0);
			in.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Communication Error: " + e.getMessage());
			System.exit(1);
		}

	}



}
