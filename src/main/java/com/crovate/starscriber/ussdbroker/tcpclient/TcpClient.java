package com.crovate.starscriber.ussdbroker.tcpclient;

import com.crovate.message.ProtoBuffRequest.Request;
import com.crovate.message.ProtoBuffRequest.Request.RequestType;
import com.crovate.message.ProtoBuffResponse.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author jawad
 */
public class TcpClient {
     
    private final static Logger logger = LoggerFactory.getLogger(TcpClient.class.getName());
    
    private static int PORT = 6789;
    private static String HOST_NAME = "localhost";
    private static boolean isClosed = false;

    public void startClient(String phoneNumber) {
        
         try {
               Socket clientSocket = openConnection(HOST_NAME, PORT);
               OutputStream output = clientSocket.getOutputStream();
               InputStream input = clientSocket.getInputStream();
               Request request = null;
               HeartBeat heartBeat = new HeartBeat();
                   
               Response response = null;
               
               TcpClientHandler handler = new TcpClientHandler(phoneNumber);
              
               // count the number of packets send
               int packetSent = 0;
             
               // this loop is for sending menus multiples times.
               //while(packetSent < 20){
                   
                    while(!isClosed){

                      if(TcpClientHandler.isStart()){
                         request = handler.getRequest(response);
                         request.writeDelimitedTo(output);
                         packetSent++;
                         logger.debug("Request Type:"+ request.getType() +"\nRequest Send:\n" + request.getMessage());
                      }

                      response = Response.parseDelimitedFrom(input);

                      if(heartBeat.isHeartbeatResponse(response.getResponse())){
                                   //System.out.println("Heartbeat request: "+response.getResponse());
                              request = heartBeat.getHeartbeatRequest();

                              request.writeDelimitedTo(output);
                       }else if(response.getTimeout()){
                           isClosed = true;
                           logger.debug("Request is timeout");
                       }else if(response.getError()){
                           isClosed = true;
                           logger.debug(response.getResponse());
                       }else{

                          logger.debug("-----Response recieved: "+response.getResponse() + "------\n");
                          request = handler.getRequest(response);
                          if(request.getType().equals(RequestType.CLOSE)){
                             isClosed = true;  
                           }
                          request.writeDelimitedTo(output);
                          packetSent++;
                          logger.debug("-----Request Send: " + request.getMessage() + "------\n");


                      }

                    }
                    
                 System.out.println("******** Number of packets sent to ussd-broker ***********" + packetSent);           
        
                 // below are menu sending multiple times changes      
//                    isClosed = false; 
//                    TcpClientHandler.setStart(true);
//               }
               
                 // above are menu sending multiple times changes            

                input.close();
                output.close();
                closeConnection(clientSocket);

           } catch (IOException ex) {
                logger.error( null, ex);
              
           
           }catch (Exception e) {
                logger.error("Exception occured ", e);
                System.exit(1);
         }
    }
  
    
    public Socket openConnection(final String hostName,final int port){
        
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(HOST_NAME, PORT);

        } catch (IOException e) {
            logger.error("Exception occured while connecting to server", e);
        }
        return clientSocket;
    }
    
    public void closeConnection(Socket clientSocket){
        try {
            logger.info("Closing the connection");
            clientSocket.close();
           
        } catch (IOException e) {
            logger.error("Exception occured while closing the connection", e);
        }
    }
    
   
   public static void main(String args[]) throws Exception
    
  {
      String phoneNumber = "";
     
      if(args.length != 0){
           phoneNumber = args[0];
      }else{
           phoneNumber = "56979409249";
      }
		
      logger.debug("Will send message to number:" + phoneNumber);  
      
      TcpClient  client = new TcpClient();
        
      client.startClient(phoneNumber);
    
  }

  
}

  
