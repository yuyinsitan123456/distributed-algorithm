package paxosUtils;

import java.util.ArrayList;
import java.util.List;

public class Config {
	private List<NodeInfo> nodes = new ArrayList<>();
	private int id;
	private int timeout;
	private int learningTime;
	private String dataDir;
	private boolean enableDataPersistence;

	public Config() {
	}

	public List<NodeInfo> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeInfo> nodes) {
		this.nodes = nodes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	public boolean isEnableDataPersistence() {
		return enableDataPersistence;
	}

	public void setEnableDataPersistence(boolean enableDataPersistence) {
		this.enableDataPersistence = enableDataPersistence;
	}

	public int getLearningTime() {
		return learningTime;
	}

	public void setLearningTime(int learningTime) {
		this.learningTime = learningTime;
	}
	
}
