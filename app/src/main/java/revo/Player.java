package revo;

import java.net.InetAddress;

/**
 * Created by ressay on 09/06/17.
 */

public class Player
{
    String nickName = "";
    InetAddress address = null;
    Player(InetAddress a, String n)
    {
        nickName = n;
        address = a;
    }


    @Override
    public String toString() {
        return nickName;
    }
}
