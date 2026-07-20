package models;

import java.nio.ByteBuffer;
import java.util.List;

public class Answer {

    List<NameField> names;

    // The record type.
    int type;

    // The class, in practice always set to 1.
    int class_bit;

    // Time-To-Live, i.e. how long a record can be cached before it should be requeried. - 4 byte
    int ttl;

    // Length of the record type specific data. - 2 byte
    int length;

    RData data;


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getClass_bit() {
        return class_bit;
    }

    public void setClass_bit(int class_bit) {
        this.class_bit = class_bit;
    }

    public void setNames(List<NameField> names) {
        this.names = names;
    }

    public List<NameField> getNames() {
        return names;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getTtl() {
        return ttl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setData(RData data) {
        this.data = data;
    }

    public RData getData() {
        return data;
    }

    public void writeToBuffer(ByteBuffer buffer) {
        for (NameField name : names) {
            name.writeToBuffer(buffer);
        }
        // null terminator
        buffer.put((byte) 0);
        buffer.putShort((short) type);
        buffer.putShort((short) class_bit);
        buffer.putInt(60);
        buffer.putShort((short) 4);

        if (type == 1) {
            for (String part : ((String) data.getData()).split("\\.")) {
                buffer.put((byte) Integer.parseInt(part));
            }
        }
    }

    @Override
    public String toString() {
        return "answers:" + names.toString();
    }
}
