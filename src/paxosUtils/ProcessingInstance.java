package paxosUtils;

import java.util.Set;

public class ProcessingInstance {
	private static int ballot=0;
	private static Set<Integer> promiseSet;
	private int valueBallot;
	private static Object value;
	private static Set<Integer> acceptSet;
	private static boolean isSucc;
	private static String state;

	public ProcessingInstance(int b, Set<Integer> ps, Object v,int vb,
			Set<Integer> as, boolean i, String s) {
		setBallot(b);
		setPromiseSet(ps);
		setValue(v);
		setAcceptSet(as);
		setSucc(i);
		setState(s);
		setValueBallot(vb);
	}

	public static int getBallot() {
		return ballot;
	}

	public static void setBallot(int ballot) {
		ProcessingInstance.ballot = ballot;
	}
	
	public static void addBallot() {
		ProcessingInstance.ballot++;
	}


	public static Set<Integer> getPromiseSet() {
		return promiseSet;
	}

	public static void setPromiseSet(Set<Integer> promiseSet) {
		ProcessingInstance.promiseSet = promiseSet;
	}
	
	public static void addPromiseSet(Integer accpetorId) {
		ProcessingInstance.promiseSet.add(accpetorId);
	}

	public static Object getValue() {
		return value;
	}

	public static void setValue(Object value) {
		ProcessingInstance.value = value;
	}

	public static Set<Integer> getAcceptSet() {
		return acceptSet;
	}

	public static void setAcceptSet(Set<Integer> acceptSet) {
		ProcessingInstance.acceptSet = acceptSet;
	}
	
	public static void addAcceptSet(Integer acceptorId) {
		ProcessingInstance.acceptSet.add(acceptorId);
	}

	public static boolean isSucc() {
		return isSucc;
	}

	public static void setSucc(boolean isSucc) {
		ProcessingInstance.isSucc = isSucc;
	}

	public static String getState() {
		return state;
	}

	public static void setState(String state) {
		ProcessingInstance.state = state;
	}

	public int getValueBallot() {
		return valueBallot;
	}

	public void setValueBallot(int valueBallot) {
		this.valueBallot = valueBallot;
	}
}
