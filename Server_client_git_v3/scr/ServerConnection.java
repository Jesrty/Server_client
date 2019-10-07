import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Thread{

    Socket socket;
    Server server;
    DataInputStream dis;
    DataOutputStream dos;
    boolean shouldRun = true;

    public ServerConnection(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    public void sendToClient(String text){
        try {
            dos.writeUTF(text);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToAllClient(String text){
        for (ServerConnection sc: server.connectionList) {
            sc.sendToClient(text);
        }
    }

    @Override
    public void run() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            while (shouldRun){
                while (dis.available() == 0){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String textIn = dis.readUTF();
                if(textIn.contains(": JOIN")){
                    String[] splitToName = textIn.split(":");
                    boolean namecheck = true;
                    for (String names : server.getNameList()) {
                        if(names.equals(splitToName[0])) {
                            sendToClient("J_ER: Name taken try again");
                            namecheck = false;
                            server.removeConnectionList(this);
                        }
                    }
                    if (namecheck) {
                        sendToClient("J_OK");
                        server.addNameList(splitToName[0]);
                    }
                }
                else if (textIn.contains(": QUIT")){
                    String[] splitToName = textIn.split(":");
                    server.removeNameList(splitToName[0]);
                    server.removeConnectionList(this);
                }
                else if(textIn.contains(": LIST")){
                    for(String s : server.nameList){
                        sendToClient(s);
                    }
                }

                else {
                    sendToAllClient(textIn);
                }
            }

            dis.close();
            dos.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
