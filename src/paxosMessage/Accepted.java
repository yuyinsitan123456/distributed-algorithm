package paxosMessage;

public class Accepted {
	private int acceptorId;
	private int instanceId;
	private boolean acceptedType;
	public Accepted(int acceptorId, int instanceId, boolean acceptedType) {
		this.setAcceptorId(acceptorId);
		this.setInstanceId(instanceId);
		this.setAcceptedType(acceptedType);
	}
	public int getAcceptorId() {
		return acceptorId;
	}
	public void setAcceptorId(int acceptorId) {
		this.acceptorId = acceptorId;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public boolean isAcceptedType() {
		return acceptedType;
	}
	public void setAcceptedType(boolean acceptedType) {
		this.acceptedType = acceptedType;
	}
}
