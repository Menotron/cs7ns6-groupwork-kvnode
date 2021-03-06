package life.tannineo.cs7ns6.node.entity.network;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * client request
 */
@Getter
@Setter
@ToString
public class ClientKVReq implements Serializable {

    public static int SET = 0;

    public static int GET = 1;

    // TODO: now only SUPPORT SET
    public static int DEL = 2;

    int type;

    String key;

    String value;

    private ClientKVReq(Builder builder) {
        setType(builder.type);
        setKey(builder.key);
        setValue(builder.value);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public enum Type {
        SET(0), GET(1), DEL(2);
        int code;

        Type(int code) {
            this.code = code;
        }

        public static Type value(int code) {
            for (Type type : values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return null;
        }
    }


    public static final class Builder {

        private int type;
        private String key;
        private String value;

        private Builder() {
        }


        public Builder type(int val) {
            type = val;
            return this;
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public Builder value(String val) {
            value = val;
            return this;
        }

        public ClientKVReq build() {
            return new ClientKVReq(this);
        }
    }
}

