package paxosUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageSendMethod {

	public void sendTo(String ip, int port, String msg) throws UnknownHostException, IOException {
		Socket socket = new Socket(ip, port);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		writer.write(msg+"\n");
		writer.flush();
		writer.close();
		socket.close();
	}

}
