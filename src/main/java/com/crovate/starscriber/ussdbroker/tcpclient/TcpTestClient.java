/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.tcpclient;

import com.crovate.message.ProtoBuffRequest;
import com.crovate.message.ProtoBuffResponse;
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
public class TcpTestClient {
      
    private final static Logger logger = LoggerFactory.getLogger(TcpTestClient.class.getName());
    
    private static int PORT = 6789;
    private static String HOST_NAME = "localhost";
    private static boolean isClosed = false;

    public void startClient(String phoneNumber) {
        
         try {
               int packetCount = 0;
               Socket clientSocket = openConnection(HOST_NAME, PORT);
               OutputStream output = clientSocket.getOutputStream();
               InputStream input = clientSocket.getInputStream();
               ProtoBuffRequest.Request request = null;
               HeartBeat heartBeat = new HeartBeat();
                   
               ProtoBuffResponse.Response response = null;
               
               TcpTestClientHandler handler = new TcpTestClientHandler(phoneNumber);
                   
               for(int i=0; i< 100; i++){
                 
               //  if(TcpClientHandler.isStart()){
                    request = handler.getRequest(response);
                    request.writeDelimitedTo(output);
                    packetCount++; 
                    logger.info("Sending packet count {}",packetCount);
                 
                    System.out.println("Request Type:"+ request.getType() +"\nRequest Send:\n" + request.getMessage());
               //  }
                 
                 response = ProtoBuffResponse.Response.parseDelimitedFrom(input);
                          
                 if(heartBeat.isHeartbeatResponse(response.getResponse())){
                              //System.out.println("Heartbeat request: "+response.getResponse());
                         request = heartBeat.getHeartbeatRequest();

                         request.writeDelimitedTo(output);
                  }else if(response.getTimeout()){
                      isClosed = true;
                      System.out.println("Request is timeout");
                  }else if(response.getError()){
                      isClosed = true;
                      System.out.println(response.getResponse());
                  }else{
                     
                     System.out.println("-----Response recieved: "+response.getResponse() + "------\n");
                     request = handler.getRequest(response);
                     if(request.getType().equals(ProtoBuffRequest.Request.RequestType.CLOSE)){
                        isClosed = true;  /* uncomment this line if each socket is closed after sending menu once. Otherwise socket will restart sending menu after the last menu.
                        //  TcpClientHandler.setStart(true);   /* Comment this line when uncomment above line.*/
                      }
                     request.writeDelimitedTo(output);
                     System.out.println("-----Request Send: " + request.getMessage() + "------\n");
                    
                     
                 }

               }
                input.close();
                output.close();
                closeConnection(clientSocket);

           } catch (IOException ex) {
               logger.error(null, ex);
              
           
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
		
      System.out.println("Will send message to number:" + phoneNumber);  
      
      TcpTestClient  client = new TcpTestClient();
        
      client.startClient(phoneNumber);
    
  }

}
