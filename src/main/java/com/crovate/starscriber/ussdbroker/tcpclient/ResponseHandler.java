/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.tcpclient;

import com.crovate.message.ProtoBuffResponse.Response;

/**
 *
 * @author jawad
 */
public class ResponseHandler {
    
    public static Response buildResponse(String id,String responseString ,boolean error, boolean timeout){
      
        Response.Builder response =  Response.newBuilder();
       
        response.setId(id);
        response.setResponse(responseString);
        response.setError(error);
        response.setTimeout(timeout);
        return response.build();
    
    }
}
