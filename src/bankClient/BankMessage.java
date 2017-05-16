/*COMP90020 project assessment
 * 2017
 * Group member :
 * 732355 
 * 732329
 * 776991
 * 756344
 * */
package bankClient;

public class BankMessage {
	private String account;
	private String operate;
	private double amount;
	private double balance;
	public BankMessage(String account, String operate, double amount) {
		super();
		this.account = account;
		this.operate = operate;
		this.amount = amount;
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

	

}
