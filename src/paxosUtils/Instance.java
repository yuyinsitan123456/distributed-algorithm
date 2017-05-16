package paxosUtils;

public class Instance {
	private int ballot;
	private Object value;
	private int acceptedBallot;

	public Instance(int ballot, Object value, int acceptedBallot) {
		this.setBallot(ballot);
		this.setValue(value);
		this.setAcceptedBallot(acceptedBallot);
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


	public int getAcceptedBallot() {
		return acceptedBallot;
	}


	public void setAcceptedBallot(int acceptedBallot) {
		this.acceptedBallot = acceptedBallot;
	}
	@Override
	public String toString(){
		return ballot+acceptedBallot+"";
	}
}
