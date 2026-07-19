package models;

import java.nio.ByteBuffer;

public class DNSPacket {

    Header header;
    Question question;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void writeToBuffer(ByteBuffer buffer) {
        header.writeToBuffer(buffer);
        question.writeToBuffer(buffer);
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }
}
