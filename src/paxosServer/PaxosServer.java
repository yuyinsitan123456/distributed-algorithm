package paxosServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;

import paxosBankAccount.StateMachine;
import paxosMessage.MessagePacket;
import paxosRole.Accepter;
import paxosRole.Learner;
import paxosRole.Proposer;
import paxosUtils.Config;
import paxosUtils.FileUtils;
import paxosUtils.MessageSendMethod;
import paxosUtils.NodeInfo;
import paxosUtils.RoleType;

public class PaxosServer {
	private Config config;

	private NodeInfo nodeInfo;

	private ServerSocket serverListening;

	private BlockingQueue<String> queue = new LinkedBlockingQueue<>();

	private ExecutorService pool = Executors.newCachedThreadPool();

	private Gson gson = new Gson();

	public PaxosServer(String fileName) throws IOException, InterruptedException{
		this.config=gson.fromJson(FileUtils.readFromFile(fileName), Config.class);
		this.nodeInfo=getNodeInfo(this.config.getId(),this.config.getNodes());
		new StateMachine(this.config.getNodes());
		this.serverListening = new ServerSocket(this.nodeInfo.getPort());
		new Thread(() -> {
			while (true) {
				try {
					Socket server = serverListening.accept();
					pool.execute(new ReadThread(server, queue));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		MessageSendMethod send=new  MessageSendMethod();
		Accepter accepter = new Accepter(this.nodeInfo.getId(), this.config,send);
		Proposer proposer = new Proposer(this.nodeInfo.getId(), this.config.getTimeout(),send);
		Learner learner = new Learner(this.nodeInfo.getId(), this.config,send);
		while (true) {
			if(!queue.isEmpty()){
				MessagePacket messagePacket = gson.fromJson(new String(queue.take()), MessagePacket.class);
				switch (messagePacket.getRoleType()) {
				case RoleType.ACCEPTER:
					accepter.setMessage(messagePacket.getMessage());
					break;
				case RoleType.PROPOSER:
					proposer.setMessage(messagePacket.getMessage());
					break;
				case RoleType.LEARNER:
					learner.setMessage(messagePacket.getMessage());
					break;
				case RoleType.CLIENT:
					proposer.setClientMessage(messagePacket.getMessage());
					break;
				default:
					break;
				}
			}
		}
	}

	private NodeInfo getNodeInfo(int id,List<NodeInfo> nodes) {
		for (NodeInfo node : nodes) {
			if (node.getId() == this.config.getId()) {
				return node;
			}
		}
		return null;
	}

	class ReadThread implements Runnable {

		private Socket server;
		private BlockingQueue<String> queue;

		public ReadThread(Socket server, BlockingQueue<String> queue) {
			this.server = server;
			this.queue = queue;
		}

		@Override
		public void run() {
			BufferedReader reader = null ;
			BufferedWriter writer = null ;
			try {
				reader = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(this.server.getOutputStream()));
				String message;
				while ((message = reader.readLine())!=null) {
					this.queue.put(message);
					MessagePacket messagePacket = gson.fromJson(message, MessagePacket.class);
					System.out.println(message);
					if(RoleType.CLIENT.equals(messagePacket.getRoleType())){
						while(true){
							Object feedback=StateMachine.getClientOutput();
							if(feedback!=null){
								System.out.println("feedback"+feedback.toString());
								writer.write(feedback.toString());
								writer.flush();
								break;
							}
						}
					}
					break;
				}
				reader.close();
				writer.close();
				this.server.close();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} finally{
				try {
					if(reader!=null)
						reader.close();
					if(writer!=null)
						writer.close();
					if(this.server!=null)
						this.server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
