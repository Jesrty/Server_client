import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    ClientConnection cliCon;
    private String clientName;
    boolean shouldRun = true;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public static void main(String[] args){
        new Client();
    }

    public Client() {
        try {
            Socket s = new Socket("Localhost", 3333);
            cliCon = new ClientConnection(s, this);

            cliCon.start();

            listenForInput();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void listenForInput(){
        Scanner s = new Scanner(System.in);

        System.out.println("Type your user name:");
        String name = s.nextLine();
        clientName = name;
        cliCon.sendToServer("JOIN");

        while (true){
            while(!s.hasNextLine()){
                try {
                    //er der for at give cpu'en mindre arbejde
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String input = s.nextLine();

            if(input.equals("QUIT")){

                cliCon.sendToServer(input);

                System.exit(0);
            }

            cliCon.sendToServer(input);
        }


    }
}
