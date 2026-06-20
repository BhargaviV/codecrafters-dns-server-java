import models.DNSPacket;
import models.Header;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");
    DNSParser parser = new DNSParser();

    // TODO: Uncomment the code below to pass the first stage
    //
     try(DatagramSocket serverSocket = new DatagramSocket(2053)) {
       while(true) {
         final byte[] buf = new byte[512];
         // request
         final DatagramPacket packet = new DatagramPacket(buf, buf.length);
         serverSocket.receive(packet);
         // parse
         ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
         DNSPacket dnsPacket = parser.parseUDP(buffer);

         System.out.println("Received data");

         // response
         final byte[] bufResponse = new byte[512];
         DNSPacket response = buildResponse(dnsPacket);
         response.writeToBuffer(ByteBuffer.wrap(bufResponse));
         final DatagramPacket packetResponse = new DatagramPacket(bufResponse, bufResponse.length, packet.getSocketAddress());
         serverSocket.send(packetResponse);
       }
     } catch (IOException e) {
         System.out.println("IOException: " + e.getMessage());
     }

  }

  private static DNSPacket buildResponse(DNSPacket dnsPacket) {
      Header header = dnsPacket.getHeader();
      header.setQuery_or_response(1);
      dnsPacket.setHeader(header);
      return dnsPacket;
  }
}
