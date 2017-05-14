package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import message.Packet;
import message.Packetbox;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import util.CommClient;


public class Accepter {
	static class Instance{
		//current ballot number
		private int ballot;
		//accepted value
		private Object value;
		//accepted value's ballot
		private int acceptedballot;
		
		public Instance(int ballot, Object value, int acceptedBallot){
			super();
			this.ballot = ballot;
			this.value = value;
			this.acceptedballot = acceptedballot;
		}
		
		public void setValue(Object value){
			this.value = value;
		}
	}
	//
// accept's state, contain each instances 
	private Map<Integer, Instance> instanceState = new HashMap<>();
	//accepted value
	private Map<Integer, Object> acceptedValue = new HashMap<>();
	//accepter's id
	private transient int id;
	//proposals
	private transient List<InfoObject> proposals;
	//my conf
	private transient InfoObject my;
	//store the last success sumbmitted instance
	private int lastInstanceId = 0;
	//config
	private ConfObject confObject = 0;
	//initial gson
	private Gson gson = new Gson();
	//client
	private CommClient client;
	
	//msgQueue, storing on packetbox
	private BlockingQueue<packetbox> msgQueue = new LinkedBlockingQueue<>();
	
	public Accepter(int id, List<InfoObject> proposals, InfoObject my, ConfObject confObject, CommClient client){
		this.id = id;
		this.proposals = proposals;
		this.my = my;
		this.confObject = confObject;
		this.client = client;
		
		instanceRecover();
		new Thread(()->{
			while (true){
				msgbox msg = msgQueue.take();
			}
		})
	}
	
}
