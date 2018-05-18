package revo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

/**
 * Created by ressay on 10/06/17.
 */

public class packetGenerator
{
    static DatagramPacket generatePacket(Object o)
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final byte[] data = baos.toByteArray();

        return new DatagramPacket(data, data.length);
    }
}
