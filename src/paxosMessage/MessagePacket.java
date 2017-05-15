package paxosMessage;

public class MessagePacket {
	private Message message;
	private String roleType;
	public MessagePacket(Message message, String roleType) {
		this.setMessage(message);
		this.setRoleType(roleType);
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
}
