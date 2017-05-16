package paxosUtils;

public class NodeInfo {
	private int id;
	private String host;
	private int port;
	public NodeInfo(int id, String host, int port) {
		this.id = id;
		this.host = host;
		this.port = port;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
