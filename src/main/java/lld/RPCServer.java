package lld;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

interface RPCFib extends Remote {
    int fib(int n) throws RemoteException;
}

class RPCFibImpl extends UnicastRemoteObject implements RPCFib {
    protected RPCFibImpl() throws RemoteException {
        super();
    }

    @Override
    public int fib(int n) throws RemoteException {
        int a=0, b=1;
        for (int i=0; i<n; i++) {
            b = b+a;
            a = b-a;
        }
        System.out.println("Fibonacci of " + n + " is : " + a);
        return a;
    }
}

public class RPCServer {
    public static void main(String[] args) {
        try {
            RPCFib obj = new RPCFibImpl();
            // Bind the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("RPCFib", obj);
            System.out.println("Server ready, listening @ http://localhost:1099");

            obj.fib(9);
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}

class Client {
    public static void main(String[] args) {
        try {
            // Get the registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            // Lookup the remote object
            RPCFib remoteProcedure = (RPCFib) registry.lookup("RPCFib");

            Random rdm = new Random();
            for (int i=0; i<10; i++) {
                int next = rdm.nextInt(20);
                int fib = remoteProcedure.fib(next);
                System.out.println("Response for " + next + ": " + fib);
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
