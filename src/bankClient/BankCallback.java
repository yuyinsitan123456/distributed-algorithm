package bankClient;

import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;



public class BankCallback {

	private Map<String, Double> bank = new HashMap<>();
	private Gson gson = new Gson();

	public void callback(String msg) {
		
		BankMessage message = gson.fromJson(msg, BankMessage.class);
		switch (message.getOperate()) {
		case "creatAccount":
			//System.out.println(bank.get(message.getAccount()));
			///send response to client
			break;
		case "withdraw":
			bank.put(message.getAccount(), message.getAmount());
			//System.out.println("ok");
			///send response to client
			break;
		case "deposit":
			bank.remove(message.getAccount(), message.getAmount());
			//System.out.println("ok");
			///send response to client
			break;
		case "deleteAccount":
			bank.remove(message.getAccount());
			//System.out.println("ok");
			///send response to client
			break;
		default:
			break;
		}
	}
	
}
