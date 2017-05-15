package paxosMessage;

public class AcceptRequest {
	private int proposerId;
	private int instanceId;
	private int ballot;
	private Object value;
	public AcceptRequest(int proposerId,int instanceId,int ballot,Object value){
		this.setProposerId(proposerId);
		this.setInstanceId(instanceId);
		this.setBallot(ballot);
		this.setValue(value);
	}
	public int getProposerId() {
		return proposerId;
	}
	public void setProposerId(int proposerId) {
		this.proposerId = proposerId;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public int getBallot() {
		return ballot;
	}
	public void setBallot(int ballot) {
		this.ballot = ballot;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
