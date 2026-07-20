import models.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DNSParser {

    private RData resolveData(ByteBuffer buffer, int rdLength) throws UnknownHostException {
        if (rdLength == 0) {
            return new ARecord("192.32.10.0");
        }
        System.out.println("resolveData" + rdLength);
        byte[] rdata = new byte[rdLength];
        buffer.get(rdata);
        InetAddress ip = InetAddress.getByAddress(rdata);
        return new ARecord(ip.getHostAddress());
    }

    private List<NameField> readName(ByteBuffer buffer) {
        List<NameField> nameFields = new ArrayList<>();
        int value = buffer.get() & 0xFF;
        while (value != 0) {
            System.out.println("readName value" + value);
            NameField nameField = new NameField();
            nameField.setLength(value);
            byte[] data = new byte[value];
            buffer.get(data);
            value = buffer.get() & 0xFF;
            String label = new String(data, StandardCharsets.UTF_8);
            if (!label.isEmpty()) {
                nameField.setName(label);
                System.out.println("readName=" + label + ":" + value);
                nameFields.add(nameField);
            }
        }
        return nameFields;
    }


    DNSPacket parseUDP(ByteBuffer buffer) {
        DNSPacket dnsPacket = new DNSPacket();

        Header header = new Header();
        header.setId(buffer.getShort());
        header.setFlags(buffer.getShort());
        header.setQuestion_count(buffer.getShort());
        header.setAnswer_count(buffer.getShort());
        header.setAuth_count(buffer.getShort());
        header.setAdditional_count(buffer.getShort());

        dnsPacket.setHeader(header);

        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < header.getQuestion_count(); i++) {
            Question question = new Question();
            question.setNames(readName(buffer));
            question.setType(buffer.getShort());
            question.setClass_bit(buffer.getShort());

            System.out.println("header" + header.getId() + "questionType:" + question.getType() + "names" +
                    question.getNames());
            questions.add(question);
        }

        header.setQuestion_count(questions.size());
        dnsPacket.setQuestions(questions);

        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < questions.size();  i++) {
            Answer answer = new Answer();
            answer.setNames(readName(buffer));
            answer.setType(buffer.getShort() | 1);
            answer.setClass_bit(buffer.getShort());
            answer.setTtl(buffer.getInt());
            answer.setLength(buffer.getShort() & 0xFFFF);
            try {
                answer.setData(resolveData(buffer, answer.getLength()));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            answers.add(answer);
        }

        header.setAnswer_count(answers.size());
        dnsPacket.setAnswers(answers);
        return dnsPacket;
    }
}
