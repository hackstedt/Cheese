package network2;

import java.rmi.*;
import java.rmi.registry.*;

class Client {
	ClientToServer com;

	
	/*public static void main(String[] args) throws Throwable {
		Registry registry=LocateRegistry.getRegistry("localhost", 12345);
		com = (ClientToServer)registry.lookup("cheese");
		com.sayHello();
		com.RegisterClient(new ClientCommunicationsImpl ());
	}*/
	
	public void connect() throws Throwable{
		Registry registry=LocateRegistry.getRegistry("localhost", 12345);
		com = (ClientToServer)registry.lookup("cheese");
	}

}
