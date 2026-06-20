package models;

import java.nio.ByteBuffer;

public class DNSPacket {

    Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void writeToBuffer(ByteBuffer buffer) {
        header.writeToBuffer(buffer);
    }

}
