package network2;

import java.io.Serializable;

import cheese.common.ServerToClient;
import cheese.common.ClientToServer;

public class CommunicationsImpl implements ClientToServer, Serializable {

	private static final long serialVersionUID = -9206364229514299617L;

	@Override
	public void sayHello() {
		System.out.println("Hello world!");
	}

	@Override
	public void RegisterClient(ServerToClient client) {
		System.out.println (client.getName());
	}
}
