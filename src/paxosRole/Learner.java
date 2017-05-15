package paxosRole;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import paxosBankAccount.StateMachine;
import paxosMessage.LearnRequest;
import paxosMessage.LearnResponse;
import paxosMessage.Message;
import paxosMessage.MessagePacket;
import paxosUtils.Config;
import paxosUtils.Instance;
import paxosUtils.MessageSendMethod;
import paxosUtils.NodeInfo;
import paxosUtils.RoleType;

public class Learner {
	private int id;

	private Map<Integer, Map<Integer, String>> tmpState = new HashMap<>();

	private int currentInstanceId = 1;

	private Config config;

	private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

	private Gson gson = new Gson();

	private MessageSendMethod send;

	private BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<>();

	public Learner(int id, Config config, MessageSendMethod send) {
		this.id = id;
		this.setConfig(config);
		this.send = send;
		service.scheduleAtFixedRate(() -> {sendRequest(this.id, this.currentInstanceId);} , config.getLearningTime(), config.getLearningTime(), TimeUnit.MILLISECONDS);
		new Thread(() -> {
			while (true) {
				try {
					Message message = msgQueue.take();
					receivedMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void setMessage(Message message) throws InterruptedException {
		this.msgQueue.put(message);
	}

	public void receivedMessage(Message message) {
		switch (message.getType()) {
		case "LearnRequest":
			LearnRequest request = gson.fromJson(message.getInfo(), LearnRequest.class);
			if (StateMachine.getFinishedInstances().containsKey(request.getInstanceId())){
				String value = StateMachine.getFinishedInstances().get(request.getInstanceId()).toString();
				sendResponse(request.getId(), request.getInstanceId(), value);
			}
			break;
		case "LearnResponse":
			LearnResponse response = gson.fromJson(message.getInfo(), LearnResponse.class);
			receivedResponse(response.getId(), response.getInstanceId(), response.getValue());
			break;
		default:
			System.err.println("error type!");
			break;
		}
	}

	private void sendRequest(int id, int instanceId) {
		this.tmpState.remove(instanceId);
		Message message = new Message("LearnRequest", gson.toJson(new LearnRequest(id, instanceId)));
		String data = gson.toJson(new MessagePacket(message, RoleType.LEARNER));
		StateMachine.getNodeInfo().forEach((info) -> {
			try {
				this.send.sendTo(info.getHost(), info.getPort(), data.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void sendResponse(int learnerId, int instanceId, String value) {
		NodeInfo learner = StateMachine.getNodeInfo(learnerId);
		try {
			Message message = new Message("LearnResponse",
					gson.toJson(new LearnResponse(id, instanceId, value)));
			this.send.sendTo(learner.getHost(), learner.getPort(),
					gson.toJson(new MessagePacket(message, RoleType.LEARNER)).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receivedResponse(int learnerId, int instanceId, String value) {
		if (!this.tmpState.containsKey(instanceId)) {
			this.tmpState.put(instanceId, new HashMap<>());
		}
		if (value == null || value.length() == 0)
			return;
		Map<Integer, String> learnerMap = this.tmpState.get(instanceId);
		learnerMap.put(learnerId, value);
		Map<String, Integer> count = new HashMap<>();
		learnerMap.forEach((tempLearnerId, tempValue) -> {
			if (!count.containsKey(tempValue)) {
				count.put(tempValue, 1);
			} else {
				count.put(tempValue, count.get(tempValue) + 1);
			}
		});
		count.forEach((tempValue, number) -> {
			if (number >= StateMachine.getNodeInfo().size() / 2 + 1) {
				StateMachine.addFinishedInstances(instanceId, tempValue);
				boolean ok=StateMachine.getInstanceState().containsKey(instanceId);
				if (ok) {
					StateMachine.addInstanceState(instanceId,new Instance(1, tempValue, 1));
				} else {
					StateMachine.changeInstanceState(instanceId,tempValue);
				}
				if (instanceId == currentInstanceId) {
					StateMachine.changeState(instanceId);
					currentInstanceId++;
				}
			}
		});
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
