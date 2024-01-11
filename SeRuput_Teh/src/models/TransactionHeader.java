package models;

public class TransactionHeader {
	private String transactionID;
	private String userID;
	
	public TransactionHeader(String transactionID, String userID) {
		this.transactionID = transactionID;
		this.userID = userID;
	}
	
	public String getTransactionID() {
		return transactionID;
	}
	
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
}
