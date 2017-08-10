package emailservice;

import java.io.Serializable;

/**
 *
 * @author fabio
 */
public class ConcreteClientName implements ClientName, Serializable {
    private String name;

    public ConcreteClientName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
