package twfq5.mail.umkc.edu.miworkoutpartner;

/**
 * Created by Tanner on 11/9/2015.
 */
public class SetCarrier
{
    public String getSetString() {
        return this.setString;
    }

    public void setSetString(String setString) {
        this.setString = setString;
    }

    public Set getSet()
    {
        return this.set;
    }

    public void setSet(Set set)
    {
        this.set = set;
    }

    private String setString;
    private Set set;

    public void add(String s, Set set)
    {
        this.setString = s;
        this.set = set;
    }
}
