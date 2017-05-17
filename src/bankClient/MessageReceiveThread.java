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
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;





public class MessageReceiveThread implements Runnable{
	private Socket socket;
	private BufferedReader in;
	private ArrayList<String> messageList;
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
				messageList.add(message);
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
