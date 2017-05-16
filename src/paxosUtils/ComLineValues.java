package paxosUtils;

import org.kohsuke.args4j.Option;

public class ComLineValues {
	@Option(
			required = false,
			name = "-l",
			aliases = {"--serversConf"},
			usage = "Server config path"
			)
	private String serversConf= "F:\\essay\\da\\config.json";
	
	public ComLineValues() {
	}

	public String getServersConf() {
		return this.serversConf;
	}
	
}

