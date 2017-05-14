package util;

public interface CommSever {
	public byte[] recvFrom() throws InterruptedException;
}
