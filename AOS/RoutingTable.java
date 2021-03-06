package bin;
import java.io.*;
import java.text.*; 
import java.util.*;
import java.util.concurrent.*;

public class RoutingTable{
    private int myNodeId;
    // private String myHost;
    // private String myPort;
    private Parseconfig config;
    // private
    private Message msg;
    private int[] hop;

    final int INT_MAX = 10000;

    public RoutingTable(Parseconfig config){
        this.config = config;
        this.myNodeId = config.getnodeId();

        initMsg(config);


    }
    public Parseconfig getConfig(){
        return this.config;
    }
    public Message getMsg(){
        return this.msg;
    }
    public int[] getHop(){
        return this.hop;
    }
    private int[] initHop(){
        int[] tmpInt = new int[this.config.getNumOfNode()];

        System.out.println("Number of Node:"+this.config.getNumOfNode());
        // fills 1 hop neighbor info in the table

        for (int i = 0; i < this.config.getNumOfNode(); i++){
            // System.out.println("i=: "+ i );

            try{
                tmpInt[this.config.getNeighbors().get(i).getNodeId()] = 1;
                // System.out.println(this.config.getNeighbors().get(i).getNodeId());
            }catch(NullPointerException e2){
                // handleException(e2);
            }catch(IndexOutOfBoundsException e2){
                // handleException(e2);
            }
        }
        // fills its own node and unreachable node info in the table
        for (int i = 0; i < this.config.getNumOfNode(); i++) {
            if (tmpInt[i] == 0)
                if (i == myNodeId)
                    tmpInt[i] = 0;
                else
                    tmpInt[i] = INT_MAX;
        }
        
        return tmpInt;
    }
    public void handleException(Exception ex) {
     // handle exception here
        System.out.println("fail at index=: ");

    }
    private void initMsg(Parseconfig config){
        // return true;

        Node origin = config.getmyNode();
        Node destination = origin;
        int[] testArray = {1,2,3};
        this.hop = initHop();



        Message msg = new Message(origin, destination, this.hop);
        this.msg = msg;
        return;
    }

    public void updateTable(Message outerMessage){
            for (int i = 0; i < this.hop.length; i++) {
            // if (this.hop[i] > (this.hop[outerMessage.getOrigin().getNodeId()] + outerMessage.getDistance()[i]));
            //     this.hop[i] = this.hop[outerMessage.getOrigin().getNodeId()] + outerMessage.getDistance()[i];
            
                if(i == myNodeId || i == outerMessage.getOrigin().getNodeId() || this.hop[i] == 1){
                    // do nothing
                    // Not update yourself and your neiborhood;
                }
                else{
                    if (this.hop[i] == outerMessage.getDistance()[i]){
                        //do nothing
                    }else{
                        if (this.hop[i] > (this.hop[outerMessage.getOrigin().getNodeId()] + outerMessage.getDistance()[i]) || this.hop[i] == 0) ;
                            this.hop[i] = this.hop[outerMessage.getOrigin().getNodeId()] + outerMessage.getDistance()[i];
                        }
                }
                
            }
            
    

        this.msg.setDistance(this.hop);

        System.out.println("update result" + Arrays.toString(this.hop));
        return;
    }
    public int getNodeId(){
        return this.myNodeId;
    }

    // public String configToSring(){
    //     return String.format("%d %s %s", nodeId, host, port);
    // }

    // public int getNodeId(){
    //     return nodeId;
    // }
    
    // public String getHostName(){
    //     return host;
    // }
    
    // public int getPort(){
    //     return Integer.parseInt(port);
    // }
}