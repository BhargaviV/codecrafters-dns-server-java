import models.DNSPacket;
import models.Header;

import java.nio.ByteBuffer;

public class DNSParser {


    DNSPacket parseUDP(ByteBuffer buffer) {
        DNSPacket dnsPacket = new DNSPacket();

        Header header = new Header();

        header.setId(buffer.getShort());
        header.setFlags(buffer.getShort());
        header.setQuestion_count(buffer.getShort());
        header.setAnswer_count(buffer.getShort());
        header.setAuth_count(buffer.get());
        header.setAdditional_count(buffer.getShort());

        dnsPacket.setHeader(header);
        return dnsPacket;
    }
}
