package bin;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Message implements java.io.Serializable{

    Node origin;
    Node destination;
    int [] distance;
    public int[] getDistance(){
        return this.distance;
    }

    public Node getOrigin(){
        return this.origin;
    }
    public Node getDestination(){
        return this.destination;
    }
    public void getMsgAsString(){
        System.out.println("origin:"+origin.getNodeId()+" to "+destination.getNodeId()+",Msg:");
        return;

    }
    public void setDestination(Node destination){
        this.destination = destination;
        // System.out.println("after setting new dest:" + this.destination.getNodeId());
        return;
    }
    public void setDistance(int[] newDistance){
        this.distance = newDistance;
        return;
    }
    Message(Node newOrg, Node newDst, int [] array)
    {   
        this.origin = newOrg;
        this.destination = newDst;
        try{
            this.distance = array.clone();
        }
        catch(NullPointerException e){

        }
    }

}

    