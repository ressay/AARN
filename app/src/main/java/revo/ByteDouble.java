package revo;

import java.nio.ByteBuffer;

/**
 * Created by ressay on 17/05/18.
 */

public class ByteDouble
{
    public static byte[] getBytes(byte[] b, int length)
    {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = b[i];
        }
        return bytes;
    }

    public static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public static byte[] toByteArray(double[] array)
    {
        byte[] bytes = new byte[array.length*8];
        int k =0;
        for(double d : array)
        {
            byte[] bs = toByteArray(d);
            for(byte b : bs)
                bytes[k++] = b;
        }
        return bytes;
    }

    public static double[] toDoubleArray(byte[] array)
    {
        double[] doubles = new double[array.length/8];
        int k =0;
        for (int i = 0; i < array.length; i+=8)
        {
            byte[] bs = new byte[8];
            for (int j = 0; j < 8; j++) {
                bs[j] = array[i+j];
            }
            doubles[k++] = toDouble(bs);
        }
        return doubles;
    }
}
