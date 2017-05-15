package bank;

import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;

import core.PaxosCallback;

public class BankCallback implements PaxosCallback {

	private Map<String, Double> bank = new HashMap<>();
	private Gson gson = new Gson();

	@Override
	public void callback(String msg) {
		
		BankMessage message = gson.fromJson(msg, BankMessage.class);
		switch (message.getOperate()) {
		case "creatAccount":
			//System.out.println(bank.get(message.getAccount()));
			break;
		case "withdraw":
			bank.put(message.getAccount(), message.getAmount());
			//System.out.println("ok");
			break;
		case "deposit":
			bank.remove(message.getAccount(), message.getAmount());
			//System.out.println("ok");
			break;
		case "deleteAccount":
			bank.remove(message.getAccount());
			//System.out.println("ok");
			break;
		default:
			break;
		}
	}
	
}
