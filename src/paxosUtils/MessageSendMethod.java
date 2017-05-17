package paxosUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MessageSendMethod {

	public void sendTo(String ip, int port, String msg)  {
		Socket socket;
		try {
			socket = new Socket(ip, port);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(msg+"\n");
			writer.flush();
			writer.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(ip+":"+port+"--error!");
		}
		
	}

}
