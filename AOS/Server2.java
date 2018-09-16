import bin.Node;
import bin.Parseconfig;
import bin.RoutingTable;
import bin.Message;
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 
// Server class 
public class Server2
{ 
    private static int nodeId;
    private static int portNum;
    private static int neiborhoodNum;
    private static RoutingTable table;
    // private 

    public static void main(String[] args) throws IOException  
    { 

    
        // server is listening on port 5056 
        

        // portNum = Integer.parseInt(args[0]);
        // nodeId = Integer.parseInt(args[1]);
        // System.out.println("Port#= "+portNum);
        // System.out.println("nodeID= "+nodeId);
        
        // // Parse the config.txt
        // parseConfig config = new parseConfig(nodeId, args[2]);
        
        // // Print the configuration file
        // config.printConfig(nodeId);
        portNum = Integer.parseInt(args[0]);
        nodeId = Integer.parseInt(args[1]);
        System.out.println("Port#= "+portNum);
        System.out.println("nodeID= "+nodeId);
        Parseconfig config = new Parseconfig(nodeId, args[2]);
        config.printConfig(nodeId);


        neiborhoodNum = config.getNeighborsCnt();
        // ***
        // Todo: the port has to be assigned as argument when launcher.sh calls this Server.class
        // ***
        ServerSocket ss = new ServerSocket(portNum);                                       
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


        Socket s = null;
        Thread serverT = new ServerHandler(s, ss, neiborhoodNum);
        serverT.start();
        // while (true)  
        // { 
        //     Socket s = null; 
              
        //     try 
        //     { 
        //         // socket object to receive incoming client requests 
        //         s = ss.accept(); 
                  
        //         System.out.println("A new message is coming : " + s); 
                  
        //         // obtaining input and out streams 
        //         DataInputStream incomingMsg = new DataInputStream(s.getInputStream()); 
        //         DataOutputStream returnMsg = new DataOutputStream(s.getOutputStream()); 
                  
        //         System.out.println(""); 
  
        //         // create a new thread object 
        //         Thread t = new ClientHandler(s, incomingMsg, returnMsg); 
  
        //         // Invoking the start() method 
        //         t.start(); 
                  
        //     } 
        //     catch (Exception e){ 
        //         s.close(); 
        //         e.printStackTrace(); 
        //     } 
        // }
        RoutingTable table = new RoutingTable(config);
        // System.out.println("Outgoing node:");
        // System.out.println((table.getMsg().getOrigin().configToSring()));
        // System.out.println (Arrays.toString(table.getMsg().getDistance()));

        // System.out.println(Arrays.toString(table.getMsg().getOrigin().get))
        // table.getnodeId();
        System.out.print("neiborhoodNum:");
        System.out.println(neiborhoodNum);

        // this.table = table
        long start = System.currentTimeMillis();
        long end = start + 3*1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end)
        {
            // run
        }

        for(int i = 0;i < neiborhoodNum;i++){
            Node targetNode = table.getConfig().getNeighbors().get(i);
            // RoutingTable table = table.clone();
            // table.getMsg().setDestination(targetNode);
            // System.out.println (Arrays.toString(table.getMsg().getDistance()));

            Thread clientT = new ClientHandler(table, targetNode);
            clientT.start();

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
class ServerHandler extends Thread{
    private ObjectInputStream incomingMsg; 
    private DataOutputStream returnMsg;
    private static int neiborhoodNum;
    private static int inComingMsgCnt;
    Socket s; 
    ServerSocket ss;
    Message newComingObj;
    public ServerHandler(Socket s, ServerSocket ss, int neiborhoodNum)  
    { 
        this.s = s;
        this.ss = ss;
        this.neiborhoodNum = neiborhoodNum;
        this.inComingMsgCnt = 0;
        // this.incomingMsg = incomingMsg; 
        // this.returnMsg = returnMsg; 
    } 
    @Override
    public void run()  
    {
        String received; 
        String toreturn; 
        while (true)  
        { 
            Socket s = null; 
              
            try { 
                s = ss.accept(); 
                System.out.println("A new message is coming : " + s); 
                incomingMsg = new ObjectInputStream(s.getInputStream()); 
                DataOutputStream returnMsg = new DataOutputStream(s.getOutputStream());

                newComingObj = (Message) incomingMsg.readObject();
                System.out.println("Object received = " + Arrays.toString(newComingObj.getDistance()));

                inComingMsgCnt++;
                if(inComingMsgCnt == neiborhoodNum){
                    System.out.println("Merge table");
                    inComingMsgCnt = 0;


                }
                // returnMsg.writeUTF("Received, and close!"); 
                
                this.s.close(); 

                // receive the answer from client 
                // received = incomingMsg.readUTF(); 
                  
                // if(received.equals("Exit")) 
                // {  
                //     System.out.println("Client " + this.s + " sends exit..."); 
                //     System.out.println("Closing this connection."); 
                //     this.s.close(); 
                //     System.out.println("Connection closed"); 
                //     break; 
                // }


                // returnMsg.writeUTF("Suppose received your table."); 

                  
            } catch (SocketException se) {
            System.exit(0);
            } catch (IOException e) {
            e.printStackTrace();
            } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
            } catch (NullPointerException ne){

            }
        }          
                  
        // String received; 
        // String toreturn; 
        // while (true)  
        // { 
        //     try { 
  
        //         // Ask user what he wants 
        //         returnMsg.writeUTF("What do you want?[Date | Time]..\n"+ 
        //                     "Type Exit to terminate connection."); 
                  
        //         // receive the answer from client 
        //         received = incomingMsg.readUTF(); 
                  
        //         if(received.equals("Exit")) 
        //         {  
        //             System.out.println("Client " + this.s + " sends exit..."); 
        //             System.out.println("Closing this connection."); 
        //             this.s.close(); 
        //             System.out.println("Connection closed"); 
        //             break; 
        //         } 
                  
        //         // creating Date object 
        //         Date date = new Date(); 
                  
        //         // write on output stream based on the 
        //         // answer from the client 
        //         switch (received) { 
                  
        //             case "establish" : 
        //                 // toreturn = fordate.format(date); 
        //                 // returnMsg.writeUTF(toreturn); 
        //                 break; 
                          
        //             case "update" : 
        //                 toreturn = fortime.format(date); 
        //                 returnMsg.writeUTF(toreturn); 
        //                 break; 
                          
        //             default: 
        //                 returnMsg.writeUTF("Invalid input"); 
        //                 break; 
        //         } 
        //     } catch (IOException e) { 
        //         e.printStackTrace(); 
        //     } 
        // } 
          
        // try
        // { 
        //     // closing resources 
        //     this.incomingMsg.close(); 
        //     this.returnMsg.close(); 
              
        // }catch(IOException e){ 
        //     e.printStackTrace(); 
        // } 
    } 
}
class ClientHandler extends Thread  
{ 
    // DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    // DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
    // final DataInputStream incomingMsg; 
    // final DataOutputStream returnMsg; 
    // final Socket s;
    private RoutingTable currentTable;
    private Node targetNode;
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    // private ArrayList<Node> hosts;
    // Node host = new Node(Integer.parseInt(4), "dc06.utdallas.edu", "1233");

  
    // Constructor 
    public ClientHandler(RoutingTable table, Node targetNode)  
    { 
        this.currentTable = table;
        this.targetNode = targetNode;
    } 
    public void sendMsg(Message msg, Node targetNode){
        // System.out.println("My target: "+targetNode.getHostName());
        try{
            currentTable.getMsg().setDestination(targetNode);

            System.out.println("Ready to send msg to:"+ (currentTable.getMsg().getDestination().getNodeId()) + "Content: "+Arrays.toString(currentTable.getMsg().getDistance()));


            try {
                socket = new Socket(targetNode.getHostName(), targetNode.getPort());
                System.out.println("Connected!");
                isConnected = true;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                // Student student = new Student(1, "Bijoy");
                // System.out.println("Object to be written = " + student);
                outputStream.writeObject(msg);

            } catch (SocketException se) {
                se.printStackTrace();
                // System.exit(0);
                System.out.println("Connection fail, not able connect to : " + targetNode.getHostName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (NullPointerException e){
            System.out.println("table is null");
        }

        // System.out.println("Ready to send msg:");
        // // currentTable.getMsg().getMsgAsString();
        // System.out.print(currentTable.getNodeId()+" to ");
        // System.out.print(currentTable.getMsg().getDestination().getNodeId());

        return;
    }
    //     private int nodeId;
    // private String host;
    // private String port;
    
    @Override
    public void run()  
    { 

        sendMsg(currentTable.getMsg(), targetNode);

        // String received; 
        // String toreturn; 
        // while (true)  
        // { 
        //     try { 
  
        //         // Ask user what he wants 
        //         returnMsg.writeUTF("What do you want?[Date | Time]..\n"+ 
        //                     "Type Exit to terminate connection."); 
                  
        //         // receive the answer from client 
        //         received = incomingMsg.readUTF(); 
                  
        //         if(received.equals("Exit")) 
        //         {  
        //             System.out.println("Client " + this.s + " sends exit..."); 
        //             System.out.println("Closing this connection."); 
        //             this.s.close(); 
        //             System.out.println("Connection closed"); 
        //             break; 
        //         } 
                  
        //         // creating Date object 
        //         Date date = new Date(); 
                  
        //         // write on output stream based on the 
        //         // answer from the client 
        //         switch (received) { 
                  
        //             case "establish" : 
        //                 // toreturn = fordate.format(date); 
        //                 // returnMsg.writeUTF(toreturn); 
        //                 break; 
                          
        //             case "update" : 
        //                 toreturn = fortime.format(date); 
        //                 returnMsg.writeUTF(toreturn); 
        //                 break; 
                          
        //             default: 
        //                 returnMsg.writeUTF("Invalid input"); 
        //                 break; 
        //         } 
        //     } catch (IOException e) { 
        //         e.printStackTrace(); 
        //     } 
        // } 
          
        // try
        // { 
        //     // closing resources 
        //     this.incomingMsg.close(); 
        //     this.returnMsg.close(); 
              
        // }catch(IOException e){ 
        //     e.printStackTrace(); 
        // } 
    }

} 