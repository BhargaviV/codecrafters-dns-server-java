package models;

import java.nio.ByteBuffer;
import java.util.List;

public class Question {

    List<NameField> names;

    // The record type.
    int type;

    // The class, in practice always set to 1.
    int class_bit;

    public List<NameField> getNames() {
        return names;
    }

    public void setNames(List<NameField> name) {
        this.names = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getClass_bit() {
        return class_bit;
    }

    public void setClass_bit(int class_bit) {
        this.class_bit = class_bit;
    }

    public void writeToBuffer(ByteBuffer buffer) {
        for (NameField name : names) {
            name.writeToBuffer(buffer);
        }
        // null terminator
        buffer.put((byte) 0);
        buffer.putShort((short) type);
        System.out.println(buffer.order() + ":" + type);
        buffer.putShort((short) class_bit);
    }

}
