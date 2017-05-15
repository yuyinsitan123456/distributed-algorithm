package paxosMessage;

public class Promise {
	private int acceptorid;
	private int instanceId;
	private boolean promiseType;
	private int acceptedBallot;
	private Object acceptedValue;
	
	public Promise(int acceptorid, int instanceId, boolean promiseType, int acceptedBallot, Object acceptedValue) {
		this.acceptorid = acceptorid;
		this.instanceId = instanceId;
		this.promiseType = promiseType;
		this.acceptedBallot = acceptedBallot;
		this.acceptedValue = acceptedValue;
	}
	
	public int getAcceptorid() {
		return acceptorid;
	}
	public void setAcceptorid(int acceptorid) {
		this.acceptorid = acceptorid;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public boolean isPromiseType() {
		return promiseType;
	}
	public void setPromiseType(boolean promiseType) {
		this.promiseType = promiseType;
	}
	public int getAcceptedBallot() {
		return acceptedBallot;
	}
	public void setAcceptedBallot(int acceptedBallot) {
		this.acceptedBallot = acceptedBallot;
	}
	public Object getAcceptedValue() {
		return acceptedValue;
	}
	public void setAcceptedValue(Object acceptedValue) {
		this.acceptedValue = acceptedValue;
	}
}
