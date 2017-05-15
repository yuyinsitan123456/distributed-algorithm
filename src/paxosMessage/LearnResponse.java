package paxosMessage;

public class LearnResponse {
	private int id;
	private int instanceId;
	private String value;
	public LearnResponse(int id, int instanceId, String value) {
		this.id = id;
		this.instanceId = instanceId;
		this.value=value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	
}
