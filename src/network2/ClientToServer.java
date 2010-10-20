package network2;


public interface ClientToServer extends java.rmi.Remote {
	public void sayHello ();
	public void RegisterClient (ServerToClient client);
}

