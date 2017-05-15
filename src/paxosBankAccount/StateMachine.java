package paxosBankAccount;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import paxosUtils.Instance;
import paxosUtils.NodeInfo;

public class StateMachine {
	private static CopyOnWriteArraySet<NodeInfo> machines=new CopyOnWriteArraySet<>();
	private static ConcurrentMap<Integer, Instance> instanceState = new ConcurrentHashMap<>();
	private static ConcurrentMap<Integer, Object> finishedInstances = new ConcurrentHashMap<>();
	private static String state;
	public StateMachine(List<NodeInfo> machines){
		for(NodeInfo machine:machines){
			StateMachine.machines.add(machine);
		}
	}
	public static NodeInfo getNodeInfo(int id) {
		for (NodeInfo each : machines) {
			if (id == each.getId()) {
				return each;
			}
		}
		return null;
	}
	public static CopyOnWriteArraySet<NodeInfo> getNodeInfo() {
		return machines;
	}
	public static void setNodeInfo(CopyOnWriteArraySet<NodeInfo> m) {
		StateMachine.machines=m;
	}
	public static String getState() {
		return state;
	}
	public static void setState(String s) {
		StateMachine.state = s;
	}
	public static void changeState(Integer instanceId) {
		StateMachine.state = StateMachine.state+finishedInstances.get(instanceId);
	}
	public static ConcurrentMap<Integer, Instance> getInstanceState() {
		return instanceState;
	}
	public static void setInstanceState(ConcurrentMap<Integer, Instance> is) {
		StateMachine.instanceState = is;
	}
	public static void addInstanceState(Integer instanceId, Instance instance) {
		StateMachine.instanceState.put(instanceId, instance);
	}
	public static void changeInstanceState(Integer instanceId, String value) {
		StateMachine.instanceState.get(instanceId).setValue(value);
	}
	public static ConcurrentMap<Integer, Object> getFinishedInstances() {
		return finishedInstances;
	}
	public static void setFinishedInstances(ConcurrentMap<Integer, Object> finishedInstances) {
		StateMachine.finishedInstances = finishedInstances;
	}
	public static void addFinishedInstances(Integer id, Object value) {
		StateMachine.finishedInstances.put(id, value);
	}
}
