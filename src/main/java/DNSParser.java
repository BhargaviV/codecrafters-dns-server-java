import models.DNSPacket;
import models.Header;
import models.NameField;
import models.Question;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DNSParser {

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

        Question question = new Question();
        question.setNames(readName(buffer));
        question.setType(buffer.getShort());
        question.setClass_bit(buffer.getShort());
        header.setQuestion_count(1);
        header.setId(1234);
        System.out.println("header" + header.getId() + "questionType:" + question.getType());

        dnsPacket.setQuestion(question);
        return dnsPacket;
    }
}
