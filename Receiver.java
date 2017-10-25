import java.io.IOException;
import java.net.*;
import java.util.Base64;

public class Receiver {
	
	private int key;
	private String keyString;

    public static void main(String[] args) throws Exception {
        int port = args.length == 0 ? 8888 : Integer.parseInt(args[0]);
        new Receiver().run(port);
    }

    public void run(int port) throws Exception {    
      try {
        DatagramSocket serverSocket = new DatagramSocket(port);
        byte[] receiveData = new byte[16];

        System.out.printf("Listening on udp:%s:%d%n",
                InetAddress.getLocalHost().getHostAddress(), port);     
        DatagramPacket receivePacket = new DatagramPacket(receiveData,
                           receiveData.length);

      while(true) 
      {  
	      serverSocket.receive(receivePacket);
	      String sentence = new String( receivePacket.getData(), 0,
	                         receivePacket.getLength() );
	      
	      // now send acknowledgement packet back to sender     
	      InetAddress IPAddress = receivePacket.getAddress();
	      
	      KeyGenerator keyGenerator = new KeyGenerator();
	      try {
	    	  key = keyGenerator.getKeyG(Integer.parseInt(sentence));
	    	  System.out.println("Chave: " + key);
	      
	    	  String sendString = keyGenerator.getKey() + "";
	    	  byte[] sendData = sendString.getBytes("UTF-8");
	    	  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
	    	  serverSocket.send(sendPacket);
	      } catch (Exception e) {
	    	  /* Neste caso não é troca de chaves, mas apenas dados criptografados. */
	    	  System.out.println("Texto recebido em HEXA: " + convertByteToHex(receivePacket.getData()));
	    	  byte[] array = new byte[receivePacket.getData().length];
	    	  array = receivePacket.getData();
	    	  ModoCBC modoCBC = new ModoCBC();
	    	  String keyHex = Integer.toHexString(key);
	    	  String keyString = "";
	    	  
	    	  for (int i = 0; i < 16; i++) { keyString += keyHex; }
	    	  String str = convertByteToHex(receivePacket.getData());
	    	  System.out.println("Texto decifrado: " + modoCBC.decipher(keyString, str));
	      }
      }
      /*****************************************************/
      } catch (IOException e) {
              System.out.println(e);
      }
      // should close serverSocket in finally block
    }
    
    private String convertByteToHex(byte[] data) {
    	StringBuilder sb = new StringBuilder();
	    for (byte b : data) {
	        sb.append(String.format("%02x", b));
	    }
	    
	    return sb.toString();

    }
}