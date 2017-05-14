package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import util.CommClient;








public class Proposer {
	enum proposerState {
		READY, PREPARE, ACCEPT, FINISH
	}

	private Map<Integer, Proposal> proposalState = new HashMap<>();

	// current instance
	private int currentInstance = 0;

	// proposer's id
	private int id;

	// accepter's number
	private int accepterNum;

	// accepters
	private List<NodeInfo> accepters;

	// my info
	private NodeInfo my;

	// timeout for each phase(ms)
	private int timeout;
	

	// ready to submit queue
	private BlockingQueue<Object> readyToSubmitQueue = new ArrayBlockingQueue<>(1);

	// success submitted queue
	private BlockingQueue<Object> hasSummitQueue = new ArrayBlockingQueue<>(1);
	
	// whether last submit is success
	private boolean isLastSumbitSuccess = false;
	
	//accepter
	private Accepter accepter;
	
	// message list
	private BlockingQueue<PacketBean> msgQueue = new LinkedBlockingQueue<>();
		
	private BlockingQueue<PacketBean> submitMsgQueue = new LinkedBlockingQueue<>();
		
	private Gson gson = new Gson();
		
	// client
	private CommClient client;

	public Proposer(int id, List<NodeInfo> accepters, NodeInfo my, int timeout, Accepter accepter,
				CommClient client) {
			super();
			this.id = id;
			this.accepters = accepters;
			this.my = my;
			this.timeout = timeout;
			this.accepter = accepter;
			this.client = client;
			
			new Thread(() -> {
				while (true) {
					try {
						PacketBean msg = this.msgQueue.take();
						recvPacket(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			new Thread(() -> {
				while (true) {
					try {
						PacketBean msg = this.submitMsgQueue.take();
						submit(msg.getData());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		
		/**
		 * insert to list
		 * @param bean
		 * @throws InterruptedException
		 */
		public void sendPacket(PacketBean bean) throws InterruptedException {
			this.msgQueue.put(bean);
		}
		
		/**
		 * 
		 * @param bean
		 * @throws InterruptedException 
		 */
		public void recvPacket(PacketBean bean) throws InterruptedException {
			switch (bean.getType()) {
			case "PrepareResponsePacket":
				PrepareResponsePacket prepareResponsePacket = gson.fromJson(bean.getData(),
						PrepareResponsePacket.class);
				onPrepareResponse(prepareResponsePacket.getId(), prepareResponsePacket.getInstance(),
						prepareResponsePacket.isOk(), prepareResponsePacket.getAb(),
						prepareResponsePacket.getAv());
				break;
			case "AcceptResponsePacket":
				AcceptResponsePacket acceptResponsePacket = gson.fromJson(bean.getData(),
						AcceptResponsePacket.class);
				onAcceptResponce(acceptResponsePacket.getId(), acceptResponsePacket.getInstance(),
						acceptResponsePacket.isOk());
				break;
			case "SubmitPacket":
				this.submitMsgQueue.add(bean);
				break;
			default:
				System.out.println("unknown type!!!");
				break;
			}
		}

		/**
		 * 
		 * @param object
		 * @return
		 * @throws InterruptedException
		 */
		public Object submit(Object object) throws InterruptedException {
			this.readyToSubmitQueue.put(object);
			beforPrepare();
			Object value = this.hasSummitQueue.take();
			return value;
		}

		/**
		 * 
		 * beforePrepare
		 */
		public void beforPrepare() {
			// get the last proposal's id
			this.currentInstance = accepter.getLastInstanceId();
			this.currentInstance++;
			Proposal proposal = new Proposal(1, new HashSet<>(), null, 0, new HashSet<>(), false,
					proposerState.READY);
			this.proposalState.put(this.currentInstance, proposal);
			if (this.isLastSumbitSuccess == false) {
				// run full processes
				prepare(this.id, this.currentInstance, 1);
			} else {
				// just do accept
				proposal.isSuccess = true;
				accept(this.id, this.currentInstance, 1, this.readyToSubmitQueue.peek());
			}
		}

		/**
		 * send prepare to all acceptors
		 * @param instance
		 *            current instance
		 * @param ballot
		 *            prepare's ballot
		 */
		private void prepare(int id, int instance, int ballot) {
			this.proposalState.get(instance).state = proposerState.PREPARE;
			this.accepters.forEach((info) -> {
				PacketBean bean = new PacketBean("PreparePacket", gson.toJson(new PreparePacket(id, instance, ballot)));
				String msg = gson.toJson(new Packet(bean, groupId, WorkerType.ACCEPTER));
				try {
					this.client.sendTo(info.getHost(), info.getPort(), msg.getBytes());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			setTimeout(new TimerTask() {

				@Override
				public void run() {
					// retry phase 1 again!
					Proposal current = proposalState.get(instance);
					if (current.state == proposerState.PREPARE) {
						current.ballot++;
						prepare(id, instance, current.ballot);
					}
				}
			});
		}

		/**
		 * collect response to the prepare from acceptors
		 * 
		 * @param id
		 * @param instance
		 * @param ok
		 * @param ab
		 * @param av
		 * @throws InterruptedException
		 */
		public void onPrepareResponse(int peerId, int instance, boolean ok, int ab, Object av) {
			Proposal current = this.proposalState.get(instance);
			if (current.state != proposerState.PREPARE)
				return;
			if (ok) {
				current.pSet.add(peerId);
				if (ab > current.valueBallot && av != null) {
					current.valueBallot = ab;
					current.value = av;
					current.isSucc = false;
				}
				if (current.pSet.size() >= this.accepterNum / 2 + 1) {
					if (current.value == null) {
						Object object = this.readyToSubmitQueue.peek();
						current.value = object;
						current.isSucc = true;
					}
					accept(id, instance, current.ballot, current.value);
				}
			}
		}

		/**
		 * 鍚戞墍鏈夌殑accepter鍙戦�乤ccept锛屽苟璁剧疆鐘舵�併��
		 * 
		 * @param id
		 * @param instance
		 * @param ballot
		 * @param value
		 */
		private void accept(int id, int instance, int ballot, Object value) {
			this.instanceState.get(instance).state = Proposer_State.ACCEPT;
			Gson gson = new Gson();
			this.accepters.forEach((info) -> {
				PacketBean bean = new PacketBean("AcceptPacket",
						gson.toJson(new AcceptPacket(id, instance, ballot, value)));
				String msg = gson.toJson(new Packet(bean, groupId, WorkerType.ACCEPTER));
				try {
					this.client.sendTo(info.getHost(), info.getPort(), msg.getBytes());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			setTimeout(new TimerTask() {
				@Override
				public void run() {
					// retry phase 2 again!
					Instance current = instanceState.get(instance);
					if (current.state == Proposer_State.ACCEPT) {
						current.ballot++;
						prepare(id, instance, current.ballot);
					}
				}
			});
		}

		/**
		 * 鎺ユ敹鍒癮ccepter杩斿洖鐨刟ccept鍝嶅簲
		 * 
		 * @param peerId
		 * @param instance
		 * @param ok
		 * @throws InterruptedException
		 */
		public void onAcceptResponce(int peerId, int instance, boolean ok) throws InterruptedException {
			Instance current = this.instanceState.get(instance);
			if (current.state != Proposer_State.ACCEPT)
				return;
			if (ok) {
				current.acceptSet.add(peerId);
				if (current.acceptSet.size() >= this.accepterNum / 2 + 1) {
					// 娴佺▼缁撴潫
					done(instance);
					if (current.isSucc) {
						this.isLastSumbitSucc = true;
						this.hasSummitQueue.put(this.readyToSubmitQueue.take());
					} else {
						// 璇存槑杩欎釜instance鐨刬d宸茬粡琚崰鏈�
						this.isLastSumbitSucc = false;
						beforPrepare();
					}
				}
			}
		}

		/**
		 * 鏈paxos閫変妇缁撴潫
		 */
		public void done(int instance) {
			this.instanceState.get(instance).state = Proposer_State.FINISH;
		}

		/**
		 * set timeout task
		 * 
		 * @param task
		 */
		private void setTimeout(TimerTask task) {
			new Timer().schedule(task, this.timeout);
		}
}
