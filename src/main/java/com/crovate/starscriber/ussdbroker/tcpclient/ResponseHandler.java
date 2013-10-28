/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.tcpclient;

import com.crovate.message.RequestResponse;

/**
 *
 * @author jawad
 */
public class ResponseHandler {
    
    public static RequestResponse.Response buildResponse(String id,String responseString ,boolean error){
      
        RequestResponse.Response.Builder response =  RequestResponse.Response.newBuilder();
       
        response.setId(id);
        response.setResponse(responseString);
        response.setError(error);
        
        return response.build();
    
    }
}
