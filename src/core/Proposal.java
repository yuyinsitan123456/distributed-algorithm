/**
 * 
 */
package core;

import java.util.Set;

import core.Proposer.proposerState;

/**
 * @author Hogan
 *
 */
public class Proposal{
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
	
	public Proposal(int ballot, Set<Integer> promiseSet, Object value, int valueBallot, Set<Integer> acceptSet,
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
