package message;

import core.roleInfo;

public class Packet {

	private Packetbox packetBean;
	private int groupId;
	private roleInfo workerType;
	public Packet(Packetbox packetBean, int groupId, roleInfo workerType) {
		super();
		this.packetBean = packetBean;
		this.groupId = groupId;
		this.workerType = workerType;
	}
	public Packetbox getPacketBean() {
		return packetBean;
	}
	public void setPacketBean(Packetbox packetBean) {
		this.packetBean = packetBean;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public roleInfo getWorkerType() {
		return workerType;
	}
	public void setWorkerType(roleInfo workerType) {
		this.workerType = workerType;
	}
}
