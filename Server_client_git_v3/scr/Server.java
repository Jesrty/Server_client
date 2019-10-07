import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Server {

    ServerSocket serverSocket;
    List<ServerConnection> connectionList = new ArrayList<>();
    Set<String> nameList = new HashSet<>();
    boolean shouldRun = true;

    public Set<String> getNameList() {
        return nameList;
    }

    public void addNameList(String name) {
        nameList.add(name);
    }

    public void removeNameList(String name){
        nameList.remove(name);
    }

    public void removeConnectionList(ServerConnection connection){
        connectionList.remove(connection);
    }

    public static void main(String[] args){
        new Server();
    }

    public Server(){
        try {
            serverSocket = new ServerSocket(3333);
            while (shouldRun) {
                Socket s = serverSocket.accept();
                ServerConnection serCon = new ServerConnection(s, this);

                serCon.start();
                connectionList.add(serCon);

                /**
                 *  Et check der kommer, når der tilsluttes en ny client,
                 *  så man kan se hvor mange der er på,
                 *
                 *  for (ServerConnection sc : connectionList) {
                 *      System.out.println(sc.toString());
                 *  }
                 */

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
