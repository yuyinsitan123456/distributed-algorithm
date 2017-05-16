package paxosUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;

import paxosBankAccount.StateMachine;
import paxosMessage.Message;
import paxosMessage.MessagePacket;

public class Broadcast {
	private int id;

	private Config config;

	private MessageSendMethod send;

	private Gson gson = new Gson();

	private BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<>();

	public Broadcast(int id , Config config, MessageSendMethod send){
		this.setId(id);
		this.setConfig(config);
		this.setSend(send);

		NodeInfo nodeInfo=StateMachine.getNodeInfo(id);
		Message message = new Message("NodeInfo", gson.toJson(new NodeInfo(id, nodeInfo.getHost(), nodeInfo.getPort())));
		String msgp = gson.toJson(new MessagePacket(message, RoleType.SERVER));
		StateMachine.getNodeInfo().forEach((info) -> {
			if(info.getId()!=id){
				try {
					this.send.sendTo(info.getHost(), info.getPort(), msgp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		new Thread(() -> {
			while (true) {
				try {
					Message msg = msgQueue.take();
					receivedMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void setServerMessage(Message message) throws InterruptedException {
		this.msgQueue.put(message);
	}

	public void receivedMessage(Message message) throws InterruptedException {
		switch (message.getType()) {
		case "NodeInfo":
			NodeInfo nodeInfo = gson.fromJson(message.getInfo(), NodeInfo.class);
			StateMachine.addNodeInfo(nodeInfo);
			break;
		default:
			System.err.println("error type!");
			break;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public MessageSendMethod getSend() {
		return send;
	}

	public void setSend(MessageSendMethod send) {
		this.send = send;
	}
}
