/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crovate.starscriber.ussdbroker.tcpclient;

import com.crovate.message.ProtoBuffRequest.Request;
import com.crovate.message.ProtoBuffRequest.Request.RequestType;

/**
 *
 * @author jawad
 */
public class RequestHandler {
    
    public static Request buildRequest(String id,String cp,String op,Integer timeout,String message,RequestType type){
       
       Request.Builder request =  Request.newBuilder();
       
       request.setId(id);
       request.setCp(cp);
       request.setOp(op);
       request.setTimeout(timeout);
       request.setMessage(message);
       request.setType(type);
       
       return request.build();
       
    }
}
