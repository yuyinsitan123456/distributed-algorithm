package paxosBank;

import java.io.IOException;

import paxosServer.PaxosServer;

public class test {
	public static void main(String[] args) throws IOException, InterruptedException {
		new PaxosServer("conf.json");
	}
}
