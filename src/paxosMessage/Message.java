package paxosMessage;

public class Message {
	private String type;
	private String info;
	public Message(String type, String info){
		this.setType(type);
		this.setInfo(info);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString(){
		return type+info;
	}
	
}
