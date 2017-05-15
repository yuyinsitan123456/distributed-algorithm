package paxosMessage;

public class LearnRequest {
	private int id;
	private int instanceId;
	public LearnRequest(int id, int instanceId) {
		this.id = id;
		this.instanceId = instanceId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	
}
