package life.tannineo.cs7ns6.node.entity.reserved;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * info of one peer
 */
@Getter
@Setter
@ToString
public class Peer implements Serializable {

    /**
     * ip:port
     * <p>
     * TODO: ip is hardcoded localhost
     */
    private final String addr;


    public Peer(String addr) {
        this.addr = addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Peer peer = (Peer) o;
        return Objects.equals(addr, peer.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addr);
    }
}
