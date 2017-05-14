package core;

public class Accepter {
	static class Instance{
		//current ballot number
		private int ballot;
		//accepted value
		private Object value;
		//accepted value's ballot
		private int acceptedballot;
		
		public Instance(int ballot, Object value, int acceptedBallot){
			super();
			this.ballot = ballot;
			this.value = value;
			this.acceptedballot = acceptedballot;
		}
		
		public void setValue(Object value){
			this.value = value;
		}
	}
	//
// accept's state, contain each instances 
	private Map<Integer, Instance> instanceState=
}
