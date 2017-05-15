package paxosMessage;

public class PrepareMessage {
	private int proposerId;
	private int instanceId;
	private int ballot;
	public PrepareMessage(int proposerId, int instanceId, int ballot) {
		this.setProposerId(proposerId);
		this.setInstanceId(instanceId);
		this.setBallot(ballot);
	}
	public int getProposerId() {
		return proposerId;
	}
	public void setProposerId(int proposerId) {
		this.proposerId = proposerId;
	}
	public int getBallot() {
		return ballot;
	}
	public void setBallot(int ballot) {
		this.ballot = ballot;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	
}
