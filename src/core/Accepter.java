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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


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
	
}
