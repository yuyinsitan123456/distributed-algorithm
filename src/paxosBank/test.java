package paxosBank;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import paxosServer.PaxosServer;
import paxosUtils.ComLineValues;

public class test {
	public static void main(String[] args) throws IOException, InterruptedException, CmdLineException {
		ComLineValues comLineValues = new ComLineValues();
		CmdLineParser cparser = new CmdLineParser(comLineValues);
		cparser.parseArgument(args);
		String keyFilepath = comLineValues.getServersConf();
		new PaxosServer(keyFilepath);
	}
}
