package emailservice;

/**
 *
 * @author fabio
 */
public interface Email {
    public int getID();
    public boolean getPrefered();
    public String getMittente();
    public String[] getDestinatario();
    public String getArgomento();
    public String getTesto();
    public String getDataSpedizione();
    public boolean isLetta();    
    public void setLetta(boolean flag);
    public void setPrefered(boolean prefered);
    @Override
    public String toString();
    @Override
    public boolean equals(Object obj);

    public void setMittente(String string);

    public void setDestinatario(String[] dests);

}
