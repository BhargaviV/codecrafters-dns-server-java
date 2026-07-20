import models.DNSPacket;
import models.Header;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class Main {

    static DNSParser parser = new DNSParser();

    static DNSPacket forwardToResolver(String ip, DNSPacket original) throws IOException {

        if (original.getQuestions().isEmpty()) {
            System.out.println("forwardToResolver empty questions" + ip);
            return new DNSPacket();
        }
        System.out.println("forwardToResolver" + ip);
        String[] ipaddr = ip.split(":");
        InetAddress resolver = InetAddress.getByName(ipaddr[0]);

        DNSPacket forward = new DNSPacket();
        Header h = new Header();
        h.setId(original.getHeader().getId());
        h.setQuestion_count(1);
        h.setRecursion_desired(original.getHeader().getRecursion_desired());
        forward.setHeader(h);
        forward.setQuestions(List.of(original.getQuestions().getFirst()));

        ByteBuffer out = ByteBuffer.allocate(512);
        forward.writeToBuffer(out);
        System.out.println("forwardToResolver sending request" + ip);

        DatagramSocket upstreamSocket = new DatagramSocket();
        DatagramPacket request = new DatagramPacket(
                out.array(),
                out.position(),
                resolver,
                Integer.parseInt(ipaddr[1])
        );
        upstreamSocket.send(request);

        byte[] responseBuffer = new byte[512];
        DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
        upstreamSocket.receive(responsePacket);
        ByteBuffer buffer = ByteBuffer.wrap(responsePacket.getData());
        System.out.println("forwardToResolver received response" + ip);
        return parser.parseUDP(buffer);
    }


  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // TODO: Uncomment the code below to pass the first stage
    //
     try(DatagramSocket serverSocket = new DatagramSocket(2053)) {
       while(true) {

            final byte[] buf = new byte[512];
            // request
            final DatagramPacket packet = new DatagramPacket(buf, buf.length);
            serverSocket.receive(packet);
            ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
            DNSPacket dnsPacket = parser.parseUDP(buffer);
            if (args.length > 0 && args[0].equals("--resolver")) {
                System.out.println("args" + Arrays.toString(args));
                DNSPacket resolvedDNSPacket = forwardToResolver(args[1], dnsPacket);
                if (resolvedDNSPacket.getAnswers() != null && !resolvedDNSPacket.getAnswers().isEmpty()) {
                    System.out.println("Resolved answer" + resolvedDNSPacket.getAnswers());
                    dnsPacket.getAnswers().set(0, resolvedDNSPacket.getAnswers().getFirst());
                }
            }

         System.out.println("Received data");

         // response
         final byte[] bufResponse = new byte[512];
         DNSPacket response = buildResponse(dnsPacket);
         response.writeToBuffer(ByteBuffer.wrap(bufResponse));
           for (int i = 0; i < 512; i++) {
               System.out.printf("%02X ", bufResponse[i] & 0xFF);
           }
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
