package network2;


import java.rmi.server.*;
import java.rmi.registry.*;
import java.rmi.*;




class Server extends java.rmi.server.UnicastRemoteObject {

	private static final long serialVersionUID = -3450907762115438702L;

	protected Server() throws RemoteException {
		super();
		CommunicationsImpl comimpl = new CommunicationsImpl ();
		Registry r = LocateRegistry.createRegistry(12345);
		r.rebind("cheese", comimpl);
		System.out.println ("Server läuft");
		// TODO Drücke beliebige Taste zum beenden
	}

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		new Server ();
	}

}
