import bin.Node;
import bin.Parseconfig;
import bin.RoutingTable;
import bin.Message;
import java.io.*; 
import java.text.*; 
import java.util.*;
import java.util.concurrent.*;

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

        ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();

        portNum = Integer.parseInt(args[0]);
        nodeId = Integer.parseInt(args[1]);
        System.out.println("Port#= "+portNum);
        System.out.println("nodeID= "+nodeId);
        Parseconfig config = new Parseconfig(nodeId, args[2]);
        config.printConfig(nodeId);


        neiborhoodNum = config.getNeighborsCnt();


        Thread serverM = new ServerMother(portNum, neiborhoodNum, queue);
        serverM.start();

        RoutingTable table = new RoutingTable(config);

        System.out.println(neiborhoodNum);



        long start = System.currentTimeMillis();
        long end = start + 3*1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end)
        {
            // run
        }

        Thread clientT = new ClientHandler(table, queue);

        clientT.start();

    }


} 
  
// ClientHandler class 
class ServerMother extends Thread{
    // private ObjectInputStream incomingMsg; 
    // private DataOutputStream returnMsg;
    // private static int neiborhoodNum;
    // private static int inComingMsgCnt;
    // Socket s; 
    // ServerSocket ss;
    // Message newComingObj;
    private static int portNum;
    private static int neiborhoodNum;
    ConcurrentLinkedQueue<Message> queue;
    public ServerMother(int portNum, int neiborhoodNum, ConcurrentLinkedQueue<Message> queue)  
    { 
        this.portNum = portNum;
        this.neiborhoodNum = neiborhoodNum;
        this.queue = queue;
    } 
    @Override
    public void run()
    {
        try{
            ServerSocket ss = new ServerSocket(this.portNum);
        while (true)  
        { 
            Socket s = null; 
              
            try 
            { 
                // socket object to receive incoming client requests 
                s = ss.accept(); 
                  
                System.out.println("A new client is connected : " + s); 

                System.out.println("Assigning new thread for this client"); 
  
                Thread serverT = new ServerHandler(s, ss, neiborhoodNum, queue);
                serverT.start();

                  
            } 
            catch (Exception e){ 
                s.close(); 
                e.printStackTrace(); 
            }
    
        }

        }
        catch(IOException e){
        }      
                
    } 
}
class ServerHandler extends Thread{
    private ObjectInputStream incomingMsg; 
    private DataOutputStream returnMsg;
    private static int neiborhoodNum;
    private static int inComingMsgCnt;
    ConcurrentLinkedQueue<Message> queue;

    Socket s; 
    ServerSocket ss;
    Message newComingObj;
    public ServerHandler(Socket s, ServerSocket ss, int neiborhoodNum, ConcurrentLinkedQueue<Message> queue)  
    { 
        this.s = s;
        this.ss = ss;
        this.neiborhoodNum = neiborhoodNum;
        this.inComingMsgCnt = 0;
        this.queue = queue;
        // this.incomingMsg = incomingMsg; 
        // this.returnMsg = returnMsg; 
    } 
    @Override
    public void run()  
    {
        while(true){
                try { 
            //         s = ss.accept(); 
            //         System.out.println("A new message is coming : " + s); 
                    incomingMsg = new ObjectInputStream(s.getInputStream()); 
                    DataOutputStream returnMsg = new DataOutputStream(s.getOutputStream());

                    newComingObj = (Message) incomingMsg.readObject();
                    queue.add(newComingObj);
                    // System.out.println("Object push = " + Arrays.toString(newComingObj.getDistance()));

            //         inComingMsgCnt++;
            //         if(inComingMsgCnt == neiborhoodNum){
            //             System.out.println("Update finish, send to clients");
            //             inComingMsgCnt = 0;


            //         }
            //         // returnMsg.writeUTF("Received, and close!"); 
                    
            //         this.s.close(); 

            //         // receive the answer from client 
            //         // received = incomingMsg.readUTF(); 
                      
            //         // if(received.equals("Exit")) 
            //         // {  
            //         //     System.out.println("Client " + this.s + " sends exit..."); 
            //         //     System.out.println("Closing this connection."); 
            //         //     this.s.close(); 
            //         //     System.out.println("Connection closed"); 
            //         //     break; 
            //         // }


            //         // returnMsg.writeUTF("Suppose received your table."); 

                      
                }
                catch (SocketException se) {
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    } catch (ClassNotFoundException cn) {
                    cn.printStackTrace();
                    } catch (NullPointerException ne){

                }
        }
            
            
                      
         
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
    private static int neiborhoodNum;
    ConcurrentLinkedQueue<Message> queue;
    // private ArrayList<Node> hosts;
    // Node host = new Node(Integer.parseInt(4), "dc06.utdallas.edu", "1233");

  
    // Constructor 
    public ClientHandler(RoutingTable table, ConcurrentLinkedQueue<Message> queue)  
    { 
        this.currentTable = table;
        this.neiborhoodNum = table.getConfig().getNeighborsCnt();
        this.queue = queue;
        // this.targetNode = targetNode;
    } 
    public void sendMsg(Message msg, Node targetNode){
        // System.out.println("My target: "+targetNode.getHostName());
        try{
            // currentTable.getMsg().setDestination(targetNode);

            // System.out.println("Ready to send msg to:"+ (currentTable.getMsg().getDestination().getNodeId()) + "Content: "+Arrays.toString(currentTable.getMsg().getDistance()));


            try {
                socket = new Socket(targetNode.getHostName(), targetNode.getPort());
                // System.out.println("Connected!");
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
        Message fetchMsg;
        int numOfMergeTimes;
        for(int i = 0;i < this.neiborhoodNum;i++){
            Node targetNode = this.currentTable.getConfig().getNeighbors().get(i);

            sendMsg(this.currentTable.getMsg(), targetNode);
        }

        // while (true){
            // keeps fetch on blockingqueue
            // until blocking queue have element to fetch
                // sendMsg()
        numOfMergeTimes = 0;

        while(true){
            fetchMsg = queue.poll();
            if(fetchMsg == null){

            }else{
                System.out.println("Original table:" + Arrays.toString(this.currentTable.getMsg().getDistance()));
                System.out.println("Going to merge this msg:" + Arrays.toString(fetchMsg.getDistance()));
                this.currentTable.updateTable(fetchMsg);
                numOfMergeTimes++;
                if(numOfMergeTimes == this.neiborhoodNum){
                     System.out.println("Merge successful! Send out!:" + Arrays.toString(this.currentTable.getMsg().getDistance()));

                     numOfMergeTimes = 0;
                }
            }
        }
        // while ((fetchMsg = queue.poll()) != null) {
        //    System.out.println("Going to merge this msg: " + Arrays.toString(fetchMsg.getDistance()));
        //    numOfMergeTimes++;
        //    if(numOfMergeTimes == this.neiborhoodNum){
        //         System.out.println("Merge successful! Send out!");
        //         numOfMergeTimes = 0;
        //    }
        // }
            // try {
            //    Thread.currentThread().sleep(500);
            // } catch (Exception ex) {
            //    ex.printStackTrace();
            // }
        // }
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