package com.crovate.starscriber.ussdbroker.tcpclient;

import com.crovate.message.RequestResponse;
import com.crovate.message.RequestResponse.Request;
import com.crovate.message.RequestResponse.Request.RequestType;
import com.crovate.message.RequestResponse.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jawad
 */
public class TcpClient {
     
    private final static Logger logger = Logger.getLogger(TcpClient.class.getName());
    
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
                   
               while(!isClosed){
                 
                 if(TcpClientHandler.isStart()){
                    request = handler.getRequest(response);
                    request.writeDelimitedTo(output);
                 
                    System.out.println("Request Type:"+ request.getType() +"\nRequest Send:\n" + request.getMessage());
                 }
                 
                 response = RequestResponse.Response.parseDelimitedFrom(input);
                          
                 if(heartBeat.isHeartbeatResponse(response.getResponse())){
                              //System.out.println("Heartbeat request: "+response.getResponse());
                         request = heartBeat.getHeartbeatRequest();

                         request.writeDelimitedTo(output);
                  }else if(response.getTimeout()){
                      isClosed = true;
                      System.out.println("Request is timeout");
                  }else{
                     
                     System.out.println("-----Response recieved: "+response.getResponse() + "------\n");
                     request = handler.getRequest(response);
                     if(request.getType().equals(RequestType.CLOSE)){
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
               logger.log(Level.SEVERE, null, ex);
              
           
           }catch (Exception e) {
                logger.log(Level.SEVERE, "Exception occured ", e);
                System.exit(1);
         }
    }
  
    
    public Socket openConnection(final String hostName,final int port){
        
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(HOST_NAME, PORT);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception occured while connecting to server", e);
        }
        return clientSocket;
    }
    
    public void closeConnection(Socket clientSocket){
        try {
            logger.log(Level.INFO, "Closing the connection");
            clientSocket.close();
           
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception occured while closing the connection", e);
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
      
      TcpClient  client = new TcpClient();
        
      client.startClient(phoneNumber);
    
  }

  
}

  
