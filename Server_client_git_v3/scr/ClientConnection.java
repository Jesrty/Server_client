import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection extends Thread{

    Socket s;
    Client client;
    DataInputStream dis;
    DataOutputStream dos;
    boolean shouldRun = true;

    public ClientConnection(Socket socket, Client client){
        s = socket;
        this.client = client;
    }

    public void sendToServer(String text){
        try {
            dos.writeUTF((client.getClientName() + ": " + text));
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        try {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            while (shouldRun) {
                try {
                    while (dis.available() == 0) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String reply = dis.readUTF();
                    System.out.println(reply);

                    if (reply.contains("J_ER: Name taken try again")){
                        System.exit(0);
                    }


                }
                catch (IOException e){
                    e.printStackTrace();
                    close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void close(){
        try {
            dis.close();
            dos.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
