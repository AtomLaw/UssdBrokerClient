package com.crovate.starscriber.ussdbroker.tcpclient;

import com.crovate.message.RequestResponse.Request;
import com.crovate.message.RequestResponse.Response;
import java.util.UUID;

public class TcpClientHandler{
	

    private static boolean start = true;
    
    private int lastCompId;
    
    private boolean firstResp = true;
    
    private boolean optionTwoSelected = false;
  
    private String phoneNumber;

    public static boolean isStart() {
        return start;
    }

    public static void setStart(boolean start) {
        TcpClientHandler.start = start;
    }

    
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public TcpClientHandler(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    
    public Request getRequest(Response response) {
        
        Request request = null;   
        
        try{
                String resp = null;
                
                if(start){

                    start = false;
                    resp = "Enviando CallMe o Seleccione:\n\n" +
                           "1. Presta Facil\n"+
                           "2. DaMe (GiftMe)\n"+
                           "3. Saldo (Balance)";
                    
                    String uniqueId = getUUID();
                    
                    System.out.println("Client Session Id: " + uniqueId);
                    
                    request = RequestHandler.buildRequest(uniqueId,phoneNumber,"Its op",5000,
                                                         resp , Request.RequestType.BEGIN);

                }else{
           			
                    request = processContinue(response);
                }

        }catch(Exception e){
                e.printStackTrace();
        }
        
        return request;
	}
	
	
	private Request processContinue(Response response){		
		
          Request request = null;
          
          String uniqueId = response.getId();
           
          try{
			
                String resp = "";
		
                
		if(response.getResponse().startsWith("1") && firstResp){
		
                           resp = "Presta Facil\n\n"+
                        	  "Deseas un prestamo de Claro de $1.000?\n Se te cobraran $50 si el prestamo no es pagado en 5 dias.\n" +
				  "El prestamo se descontara de tu proxima recarga.\n"+
				  "1. Si\n"+
				  "2. No";
				
				firstResp = false;
				 request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                     resp , Request.RequestType.CONTINUE);
				return request;
		}
			
		
                if(response.getResponse().startsWith("1") && !firstResp && !optionTwoSelected){
				
                    resp = "Presta Facil\n"+
                           "Has recibido $1.000 de prestamo, puedes seguir llamando!\n"+
                            "Saludos de Claro.";
                
                    request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                     resp , Request.RequestType.END);
			
                    return request;
		}
                
                if(response.getResponse().startsWith("2") && !firstResp && !optionTwoSelected){
				resp = "Si otro dia necesitas prestamo, " + 
						"cuenta con Claro";
				 request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                     resp , Request.RequestType.END);
		
			
                                return request;
		}
			
		if(response.getResponse().startsWith("2") && firstResp){
				
                    resp = "DaMe\n"+
                            "Seleccione el Monto:\n\n"+
                            "1. 1.500\n"+
                            "2. 2.000\n"+
                            "3. 2.500\n"+
                            "4. 3.000";
			
                    firstResp = false;
                    optionTwoSelected = true;
                    
                    request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                     resp , Request.RequestType.CONTINUE);
                    return request;
                }
			
		if(response.getResponse().startsWith("1") && !firstResp && optionTwoSelected){
		
                    resp = "DaMe\n\n"+
                        	"Se envio una solicitud de DaMe por $1.500 a 92011933.";
				
                    request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                     resp , Request.RequestType.END);
		
                    return request;
		}
			
		if(response.getResponse().startsWith("2") && !firstResp && optionTwoSelected){
			
                    resp = "DaMe\n\n"+
                            "Se envio una solicitud de DaMe por $2000 a 92011933.";
			
                    request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                     resp , Request.RequestType.END);
		                    
                    return request;
		}
			
			
                if(response.getResponse().startsWith("3") && !firstResp && optionTwoSelected){
		
                    
                    resp = "DaMe\n\n"+
                           "Se envio una solicitud de DaMe por $2.500 a 92011933.";
				
                        
                    
                    request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                     resp , Request.RequestType.END);
		    
                    return request;
		}

		
                if(response.getResponse().startsWith("4") && !firstResp && optionTwoSelected){
				
		
                    resp = "DaMe\n\n"+
			"Se envio una solicitud de DaMe por $3.000 a 92011933.";
		
                    request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                   resp , Request.RequestType.END);
		    
                    
                    return request;
		}

                
                if(response.getResponse().startsWith("3") && firstResp){
				
                    resp = "Saldo\n\n"+ 
                            "Su saldo es $3.435.";
				
                    request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                   resp , Request.RequestType.END);
		
                    return request;
		}
                    
                request = RequestHandler.buildRequest(uniqueId,"Its cp","Its op",5000,
                                                   resp , Request.RequestType.CLOSE);
		    
                return request; 
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
            return request;
	}
        
        
        public String getUUID(){
            
            String uniqueID = UUID.randomUUID().toString();
            
            return uniqueID;
            
        }

}
