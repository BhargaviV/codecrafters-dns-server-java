package models;

import java.nio.ByteBuffer;

public class DNSPacket {

    Header header;
    Question question;
    Answer answer;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void writeToBuffer(ByteBuffer buffer) {
        header.writeToBuffer(buffer);
        question.writeToBuffer(buffer);
        answer.writeToBuffer(buffer);
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }
}
