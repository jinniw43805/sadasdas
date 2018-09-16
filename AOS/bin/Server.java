import bin.Node;
import bin.Parseconfig;
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 

// Server class 
public class Server  
{ 
    public static void main(String[] args) throws IOException  
    { 
        // server is listening on port 5056 
        

        // ***
        // Todo: the port has to be assigned as argument when launcher.sh calls this Server.class
        // ***
        ServerSocket ss = new ServerSocket(5056);   
        // ***
        // End Todo
        // ***  

        // ***
        // Todo: Get all neiborhood information
        // ---
        // 1. call getNeighbors(){}
        // ---
        // End Todo
        // ***

        // ***
        // Todo: Establish socket to neiborhood, and send first message to neiborhood.
        // ---
        // 1. call establishWithNeighbor(){}
        // 2. call establishWithNeighbor(currentTable, ArrayList<Node>){}
        // ---
        // End Todo
        // ***  
 


        while (true)  
        { 
            Socket s = null; 
              
            try 
            { 
                // socket object to receive incoming client requests 
                s = ss.accept(); 
                  
                System.out.println("A new message is coming : " + s); 
                  
                // obtaining input and out streams 
                DataInputStream incomingMsg = new DataInputStream(s.getInputStream()); 
                DataOutputStream returnMsg = new DataOutputStream(s.getOutputStream()); 
                  
                System.out.println(""); 
  
                // create a new thread object 
                Thread t = new ClientHandler(s, incomingMsg, returnMsg); 
  
                // Invoking the start() method 
                t.start(); 
                  
            } 
            catch (Exception e){ 
                s.close(); 
                e.printStackTrace(); 
            } 
        } 
    }

    // Todo: Function needs to be implemented
    // public ArrayList<Node> getNeighbors() {
    //     return neighbors;
    // }

    // Todo: Function needs to be implemented
    // public void establishWithNeighbor() {
    //     establish neighbors socket;
    // }

    // public void sendMsg(neiborhood, currentTable){
            // send        
    // }

} 
  
// ClientHandler class 
class ClientHandler extends Thread  
{ 
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
    final DataInputStream incomingMsg; 
    final DataOutputStream returnMsg; 
    final Socket s; 
    // private ArrayList<Node> hosts;
    // Node host = new Node(Integer.parseInt(4), "dc06.utdallas.edu", "1233");

  
    // Constructor 
    public ClientHandler(Socket s, DataInputStream incomingMsg, DataOutputStream returnMsg)  
    { 
        this.s = s; 
        this.incomingMsg = incomingMsg; 
        this.returnMsg = returnMsg; 
    } 
  
    @Override
    public void run()  
    { 
        String received; 
        String toreturn; 
        while (true)  
        { 
            try { 
  
                // Ask user what he wants 
                returnMsg.writeUTF("What do you want?[Date | Time]..\n"+ 
                            "Type Exit to terminate connection."); 
                  
                // receive the answer from client 
                received = incomingMsg.readUTF(); 
                  
                if(received.equals("Exit")) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    this.s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // creating Date object 
                Date date = new Date(); 
                  
                // write on output stream based on the 
                // answer from the client 
                switch (received) { 
                  
                    case "establish" : 
                        // toreturn = fordate.format(date); 
                        // returnMsg.writeUTF(toreturn); 
                        break; 
                          
                    case "update" : 
                        toreturn = fortime.format(date); 
                        returnMsg.writeUTF(toreturn); 
                        break; 
                          
                    default: 
                        returnMsg.writeUTF("Invalid input"); 
                        break; 
                } 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
          
        try
        { 
            // closing resources 
            this.incomingMsg.close(); 
            this.returnMsg.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 