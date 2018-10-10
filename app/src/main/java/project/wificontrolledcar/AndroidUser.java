package project.wificontrolledcar;

/**
 * AndroidUser.java
 *
 * This class represents an AndroidUser for the app. It contains various methods for setting the
 * user's choice of IP-address and port. It also sets user name.
 *
 * @author Jonas Eiselt
 * @since 2016-05-21
 */
public class AndroidUser
{
    private int id;

    private String userName;
    private String password;

    private String clientName;
    private String clientPass;

    private String ipAddress;
    private int tcpPort;

    public AndroidUser()
    {
        // Empty constructor
    }

    /**
     * This is a public constructor.
     * @param id used by database
     * @param userName used for authentication with app
     * @param password used for authentication with app
     * @param clientName used for authentication with server
     * @param clientPass used for authentication with server
     * @param ipAddress server address
     * @param tcpPort server port
     */
    public AndroidUser(int id, String userName, String password, String clientName, String clientPass, String ipAddress, int tcpPort)
    {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.clientName = clientName;
        this.clientPass = clientPass;
        this.ipAddress = ipAddress;
        this.tcpPort = tcpPort;
    }

    /**
     * @return id
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * @param id to be set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return user name
     */
    public String getUserName()
    {
        return this.userName;
    }

    /**
     * @param userName to be set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * @return password
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * @param password to be set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return client name
     */
    public String getClientName()
    {
        return this.clientName;
    }

    /**
     * @param clientName to be set
     */
    public void setClientName(String clientName)
    {
        this.clientName = clientName;
    }

    /**
     * @return client password
     */
    public String getClientPass()
    {
        return this.clientPass;
    }

    /**
     * @param clientPass to be set
     */
    public void setClientPass(String clientPass)
    {
        this.clientPass = clientPass;
    }

    /**
     * @return IP-address
     */
    public String getIpAddress()
    {
        return this.ipAddress;
    }

    /**
     * @param ipAddress to be set
     */
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    /**
     * @return tcp port
     */
    public int getTcpPort()
    {
        return this.tcpPort;
    }

    /**
     * @param tcpPort to be set
     */
    public void setTcpPort(int tcpPort)
    {
        this.tcpPort = tcpPort;
    }
}