package paxosRole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import paxosBankAccount.StateMachine;
import paxosMessage.AcceptRequest;
import paxosMessage.Accepted;
import paxosMessage.Message;
import paxosMessage.MessagePacket;
import paxosMessage.PrepareMessage;
import paxosMessage.Promise;
import paxosUtils.Config;
import paxosUtils.FileUtils;
import paxosUtils.Instance;
import paxosUtils.MessageSendMethod;
import paxosUtils.NodeInfo;
import paxosUtils.RoleType;

public class Accepter {

	private int id;
	private int lastInstanceId = 0;
	private Config config;
	private MessageSendMethod send;

	private Gson gson = new Gson();

	private BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<>();

	public Accepter(int id, Config config, MessageSendMethod send) {
		this.id = id;
		this.config = config;
		this.setSend(send);
		instanceRecover();
		new Thread(() -> {
			while (true) {
				try {
					Message msg = msgQueue.take();
					System.out.println(msg);
					receivedMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void setMessage(Message message) throws InterruptedException {
		this.msgQueue.put(message);
	}

	public void receivedMessage(Message message) throws UnknownHostException, IOException {
		switch (message.getType()) {
		case "PrepareMessage":
			PrepareMessage prepareMessage = gson.fromJson(message.getInfo(), PrepareMessage.class);
			receivedPrepare(prepareMessage.getProposerId(), prepareMessage.getInstanceId(), prepareMessage.getBallot());
			break;
		case "AcceptRequest":
			AcceptRequest acceptRequest = gson.fromJson(message.getInfo(), AcceptRequest.class);
			reseivedAccept(acceptRequest.getProposerId(), acceptRequest.getInstanceId(), acceptRequest.getBallot(),
					acceptRequest.getValue());
			break;
		default:
			System.out.println("error type!");
			break;
		}
	}

	public void receivedPrepare(int proposerId, int instanceId, int ballot) throws UnknownHostException, IOException {
		Map<Integer, Instance> instanceState=StateMachine.getInstanceState();
		System.out.println(instanceState);
		if (!instanceState.containsKey(instanceId)) {
			instanceState.put(instanceId, new Instance(ballot, null, 0));
			instancePersistence();
			promise(proposerId, id, instanceId, true, 0, null);
		} else {
			Instance current = instanceState.get(instanceId);
			if (ballot > current.getBallot()) {
				current.setBallot(ballot);
				instancePersistence();
				promise(proposerId, id, instanceId, true, current.getAcceptedBallot(), current.getValue());
			} else {
				promise(proposerId, id, instanceId, false, current.getBallot(), null);
			}
		}
	}

	private void promise(int proposerId, int acceptorId, int instanceId, boolean promiseType, int acceptedBallot, Object acceptedValue)
			throws UnknownHostException, IOException {
		Message message = new Message("Promise",
				gson.toJson(new Promise(acceptorId, instanceId, promiseType, acceptedBallot, acceptedValue)));
		NodeInfo proposer = StateMachine.getNodeInfo(proposerId);
		this.send.sendTo(proposer.getHost(), proposer.getPort(),
				gson.toJson(new MessagePacket(message, RoleType.PROPOSER)));
	}

	public void reseivedAccept(int proposerId, int instanceId, int ballot, Object value) throws UnknownHostException, IOException {
		Map<Integer, Instance> instanceState=StateMachine.getInstanceState();
		System.out.println(instanceState);
		if (!instanceState.containsKey(instanceId)) {
			accepted(proposerId, id, instanceId, false);
		} else {
			Instance currentInstance = instanceState.get(instanceId);
			if (ballot == currentInstance.getBallot()) {
				currentInstance.setAcceptedBallot(ballot);
				currentInstance.setValue(value);
				StateMachine.setCurrentInstanceId(instanceId+1);
				StateMachine.addFinishedInstances(instanceId, value);
				if (!instanceState.containsKey(instanceId + 1)) {
					instanceState.put(instanceId + 1, new Instance(1, null, 0));
				}
				this.lastInstanceId = instanceId;
				System.out.println(StateMachine.getFinishedInstances().toString());
				instancePersistence();
				accepted(proposerId, id, instanceId, true);
			} else {
				accepted(proposerId, id, instanceId, false);
			}
		}
	}

	private void accepted(int proposerId, int acceptorid, int instanceId, boolean acceptorType) throws UnknownHostException, IOException {
		Message message = new Message("Accepted",
				gson.toJson(new Accepted(acceptorid, instanceId, acceptorType)));
		NodeInfo proposer = StateMachine.getNodeInfo(proposerId);
		this.send.sendTo(proposer.getHost(), proposer.getPort(),
				gson.toJson(new MessagePacket(message, RoleType.PROPOSER)));
	}

	public int getLastInstanceId() {
		return lastInstanceId;
	}

	private void instancePersistence() {
		if (!this.config.isEnableDataPersistence())
			return;
		try {
			FileWriter fileWriter = new FileWriter(getInstanceFileAddr());
			Map<Integer, Instance> instanceState=StateMachine.getInstanceState();
			fileWriter.write(gson.toJson(instanceState));
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void instanceRecover() {
		if (!this.config.isEnableDataPersistence())
			return;
		String data = FileUtils.readFromFile(getInstanceFileAddr());
		if (data == null || data.length() == 0) {
			File file = new File(getInstanceFileAddr());
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return;
		}
		Map<Integer, Instance> instanceState=StateMachine.getInstanceState();
		instanceState.putAll(gson.fromJson(data, new TypeToken<Map<Integer, Instance>>() {
		}.getType()));
		instanceState.forEach((key, value) -> {
			if (value.getValue() != null)
				StateMachine.setCurrentInstanceId();
				StateMachine.addFinishedInstances(key, value.getValue());
		});
	}
	
	private String getInstanceFileAddr() {
		return this.config.getDataDir() + "accepter-" + this.id + ".json";
	}

	public int getId() {
		return id;
	}

	public Config getConfig() {
		return config;
	}

	public MessageSendMethod getSend() {
		return send;
	}

	public void setSend(MessageSendMethod send) {
		this.send = send;
	}

}
