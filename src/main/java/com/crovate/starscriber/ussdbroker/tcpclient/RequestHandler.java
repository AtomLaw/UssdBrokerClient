/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.tcpclient;

import com.crovate.message.RequestResponse;
import com.crovate.message.RequestResponse.Request.RequestType;

/**
 *
 * @author jawad
 */
public class RequestHandler {
    
    public static RequestResponse.Request buildRequest(String id,String cp,String op,Integer timeout,String message,RequestType type){
       
       RequestResponse.Request.Builder request =  RequestResponse.Request.newBuilder();
       
       request.setId(id);
       request.setCp(cp);
       request.setOp(op);
       request.setTimeout(timeout);
       request.setMessage(message);
       request.setType(type);
       
       return request.build();
       
    }
}
