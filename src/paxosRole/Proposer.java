package paxosRole;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;

import paxosBankAccount.StateMachine;
import paxosMessage.AcceptRequest;
import paxosMessage.Accepted;
import paxosMessage.Message;
import paxosMessage.MessagePacket;
import paxosMessage.PrepareMessage;
import paxosMessage.Promise;
import paxosUtils.MessageSendMethod;
import paxosUtils.ProceserState;
import paxosUtils.ProcessingInstance;
import paxosUtils.RoleType;

public class Proposer {

	private int id;

	private int timeout;

	private boolean isLastSumbitSucc=false;
	
	private BlockingQueue<Object> tempQueue = new ArrayBlockingQueue<>(1);
	
	private BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<>();

	private BlockingQueue<Message> recieveClientQueue = new LinkedBlockingQueue<>();

	private Gson gson = new Gson();

	private MessageSendMethod send;

	public Proposer(int id , int timeout, MessageSendMethod send) {
		this.id = id;
		this.timeout = timeout;
		this.send = send;
		new Thread(() -> {
			while (true) {
				try {
					Message msg = this.msgQueue.take();
					receivedMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(() -> {
			while (true) {
				try {
					Message msg = this.recieveClientQueue.take();
					this.tempQueue.put(msg);
					beforePerpare();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void setMessage(Message message) throws InterruptedException {
		this.msgQueue.put(message);
	}

	public void setClientMessage(Message message) throws InterruptedException {
		this.recieveClientQueue.put(message);
	}

	public void beforePerpare() throws InterruptedException {
		new ProcessingInstance(1, new HashSet<>(), null,0, new HashSet<>(), false, ProceserState.PREPARE);
		if (this.isLastSumbitSucc == false) {
			prepare(this.id, StateMachine.getCurrentInstanceId(), 1);
		}else{
			ProcessingInstance.setSucc(true);
			accept(this.id, StateMachine.getCurrentInstanceId(), 1, this.tempQueue.peek());
		}
	}


	public void receivedMessage(Message message) throws InterruptedException {
		switch (message.getType()) {
		case "Promise":
			Promise promise = gson.fromJson(message.getInfo(),
					Promise.class);
			reseivedPromise(promise.getAcceptorid(), promise.getInstanceId(),
					promise.isPromiseType(), promise.getAcceptedBallot(),
					promise.getAcceptedValue());
			break;
		case "Accepted":
			Accepted accepted = gson.fromJson(message.getInfo(),
					Accepted.class);
			reseivedAccepted(accepted.getAcceptorId(), accepted.getInstanceId(),
					accepted.isAcceptedType());
			break;
		default:
			System.out.println("error type!");
			break;
		}
	}

	private void prepare(int id, int instanceId, int ballot) {
		ProcessingInstance.setState(ProceserState.PREPARE);
		Message message = new Message("PrepareMessage", gson.toJson(new PrepareMessage(id, instanceId, ballot)));
		String msg = gson.toJson(new MessagePacket(message, RoleType.ACCEPTER));
		StateMachine.getNodeInfo().forEach((info) -> {
			try {
				this.send.sendTo(info.getHost(), info.getPort(), msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		setTimeout(new TimerTask() {
			@Override
			public void run() {
				if (ProcessingInstance.getState() == ProceserState.PREPARE&&instanceId==StateMachine.getCurrentInstanceId()) {
					ProcessingInstance.addBallot();
					prepare(id, instanceId, ProcessingInstance.getBallot());
				}
			}
		});
	}

	public void reseivedPromise(int accpetorId, int instanceId, boolean ok, int acceptBallot, Object acceptValue) {
		if (ProcessingInstance.getState() != ProceserState.PREPARE)
			return;
		if (ok) {
			ProcessingInstance.addPromiseSet(accpetorId);
			if (acceptBallot > ProcessingInstance.getBallot() && acceptValue != null) {
				ProcessingInstance.setBallot(acceptBallot);
				ProcessingInstance.setValue(acceptValue);
				ProcessingInstance.setSucc(false);
			}
			if (ProcessingInstance.getPromiseSet().size() >= StateMachine.getNodeInfo().size() / 2 + 1) {
				if (ProcessingInstance.getValue() == null) {
					ProcessingInstance.setValue(this.tempQueue.peek());
					ProcessingInstance.setSucc(true);
				}
				accept(id, instanceId, ProcessingInstance.getBallot(), ProcessingInstance.getValue());
			}
		}
	}

	private void accept(int id, int instanceId, int ballot, Object value) {
		ProcessingInstance.setState(ProceserState.ACCEPT);
		Gson gson = new Gson();
		Message message = new Message("AcceptRequest",
				gson.toJson(new AcceptRequest(id, instanceId, ballot, value)));
		String msg = gson.toJson(new MessagePacket(message, RoleType.ACCEPTER));
		StateMachine.getNodeInfo().forEach((info) -> {
			try {
				this.send.sendTo(info.getHost(), info.getPort(), msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		setTimeout(new TimerTask() {
			@Override
			public void run() {
				if (ProcessingInstance.getState() == ProceserState.ACCEPT&&instanceId==StateMachine.getCurrentInstanceId()) {
					ProcessingInstance.addBallot();
					prepare(id, instanceId, ProcessingInstance.getBallot());
				}
			}
		});
	}

	public void reseivedAccepted(int acceptorId, int instanceId, boolean ok) throws InterruptedException {
		if (ProcessingInstance.getState() != ProceserState.ACCEPT)
			return;
		if (ok) {
			ProcessingInstance.addAcceptSet(acceptorId);
			if (ProcessingInstance.getAcceptSet().size() >= (StateMachine.getNodeInfo().size() / 2) + 1) {
				done();
				if (ProcessingInstance.isSucc()) {
					this.isLastSumbitSucc = true;
					this.tempQueue.poll();
					StateMachine.setClientOutput(StateMachine.getFinishedInstances());
				} else {
					this.isLastSumbitSucc = false;
					beforePerpare();
				}
			}
		}
	}

	public void done() {
		ProcessingInstance.setState(ProceserState.FINISH);
	}

	private void setTimeout(TimerTask task) {
		new Timer().schedule(task, this.timeout);
	}

}
