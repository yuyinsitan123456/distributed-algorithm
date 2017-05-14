package bank;

public class BankMessage {
	private String account;
	private String operate;
	private double amount;
	private double balance;
	public BankMessage(String account, String operate, double amount, double balance) {
		super();
		this.account = account;
		this.operate = operate;
		this.amount = amount;
		this.balance = balance;
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
