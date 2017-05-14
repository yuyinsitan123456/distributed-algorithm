package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;






public class Proposer {
	enum proposerState {
		READY, PREPARE, ACCEPT, FINISH
	}
	class proposal{
		private int ballot;
		// a set for promise receive
		private Set<Integer> promiseSet;
		// value found after phase 1
		private Object value;
		// value's ballot
		private int valueBallot;
		// accept set
		private Set<Integer> acceptSet;
		// is  doneValue
		private boolean isSucc;
		// proposer's state
		private proposerState state;
		
		public proposal(int ballot, Set<Integer> promiseSet, Object value, int valueBallot, Set<Integer> acceptSet,
				boolean isSucc, proposerState state) {
			super();
			this.ballot = ballot;
			this.promiseSet = promiseSet;
			this.value = value;
			this.valueBallot = valueBallot;
			this.acceptSet = acceptSet;
			this.isSucc = isSucc;
			this.state = state;
		}	
	}
	private Map<Integer, proposal> proposerState = new HashMap<>();

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
		
		

}
