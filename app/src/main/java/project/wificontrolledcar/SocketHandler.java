package project.wificontrolledcar;

import java.net.Socket;

/**
 * SocketHandler.java
 *
 * This class is used for getting and setting socket. The class is used when a user connects (sets
 * socket) to server in one activity and uses that socket in another activity for keeping the same
 * communication-line alive.
 *
 * Synchronized methods are used since the methods are being modified from within a thread.
 *
 * @author Jonas Eiselt
 * @since 2016-05-21
 */
public class SocketHandler
{
    private static Socket socket;

    /**
     * This is a public class method that sets socket.
     * @param socket to be set
     */
    public static synchronized void setSocket(Socket socket)
    {
        SocketHandler.socket = socket;
    }

    /**
     * This is a public class method that returns a socket.
     * @return socket to be returned
     */
    public static synchronized Socket getSocket()
    {
        return socket;
    }
}
