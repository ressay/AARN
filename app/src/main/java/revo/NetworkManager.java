package revo;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by ressay on 06/06/17.
 */

public class NetworkManager
{
    static int servers_port = 50008;
    static int servers_Dport = 50007;
    static int players_port = 50006;
    private static NetworkManager _instance = null;

    public static NetworkManager getInstance()
    {
        if(_instance == null) _instance = new NetworkManager();
        return _instance;
    }

    private NetworkManager()
    {

    }

    public interface ListenerCallBack
    {
        void listeningAction(String receivedMessage, InetAddress sender);
    }

    public interface DListenerCallBack
    {
        void listeningAction(double[] receivedMessage, InetAddress sender);
    }

    HashMap<Integer,LinkedList<ListenerCallBack>> actions = new HashMap<>();
    HashMap<Integer,LinkedList<DListenerCallBack>> dActions = new HashMap<>();

    private boolean addActionToPort(int port,ListenerCallBack action)
    {
        if(!actions.containsKey(port))
        {
            actions.put(port,new LinkedList<ListenerCallBack>());
            actions.get(port).add(action);
            return true;
        }
        else {
            actions.get(port).add(action);
            return false;
        }
    }

    private boolean addDActionToPort(int port,DListenerCallBack action)
    {
        if(!dActions.containsKey(port))
        {
            dActions.put(port,new LinkedList<DListenerCallBack>());
            dActions.get(port).add(action);
            return true;
        }
        else {
            dActions.get(port).add(action);
            return false;
        }
    }

    public ListenerCallBack listen(int p,ListenerCallBack action) {

        final int port = p;
        final NetworkManager net = this;
        if(addActionToPort(port,action))
        new Thread() {
            public void run() {
                while(net.actions.containsKey(port)) {
                    if(!net.actions.containsKey(port))
                        return;
                    String text;
                    byte[] message = new byte[1500];
                    try {
                        DatagramPacket p = new DatagramPacket(message, message.length);
                        DatagramSocket s = new DatagramSocket(port);
                        s.receive(p);
                        text = new String(message, 0, p.getLength());
                        Log.d("ClientAction", "message:" + text);

                        LinkedList<ListenerCallBack> actions = net.actions.get(port);
                        for(ListenerCallBack ac : actions)
                        ac.listeningAction(text,p.getAddress());

                        s.close();
                    } catch (Exception e) {

                        Log.d("ClientAction", "error hosts " + e.toString());
                    }
                }
            }
        }.start();
        return action;
    }

    public DListenerCallBack listenD(int p,DListenerCallBack action) {

        final int port = p;
        final NetworkManager net = this;
        if(addDActionToPort(port,action))
            new Thread() {
                public void run() {
                    while(net.dActions.containsKey(port)) {
                        if(!net.dActions.containsKey(port))
                            return;
                        double[] text;
                        byte[] message = new byte[1500];
                        try {
                            DatagramPacket p = new DatagramPacket(message, message.length);
                            DatagramSocket s = new DatagramSocket(port);
                            Log.e("ClientAction", "gonna receive!!");
                            s.receive(p);
                            text = ByteDouble.toDoubleArray(ByteDouble.getBytes(message,p.getLength()));
                            Log.e("ClientAction", "message:" + text);

                            LinkedList<DListenerCallBack> actions = net.dActions.get(port);
                            for(DListenerCallBack ac : actions)
                                ac.listeningAction(text,p.getAddress());

                            s.close();
                        } catch (Exception e) {

                            Log.d("ClientAction", "error hosts " + e.toString());
                        }
                    }
                }
            }.start();
        return action;
    }

    public void cancelActionInPort(int port,ListenerCallBack lcb)
    {
        if(actions.get(port).contains(lcb))
        actions.get(port).remove(lcb);
    }

    public void cancelAction(ListenerCallBack lcb)
    {
        Iterator<Integer> ports = actions.keySet().iterator();
        while(ports.hasNext())
            cancelActionInPort(ports.next(),lcb);
    }

    public void sendMessageToHost(String message, NetworkScanHost.Host host) {
        sendMessage(message,players_port,host.getAddress());
    }

    public void sendMessage(String message, int prt, InetAddress address) {
        final int port = prt;
        final String messageStr = message;
        final InetAddress local = address;
        new Thread() {
            public void run() {

                try {
                    DatagramSocket s = new DatagramSocket();

                    int msg_length = messageStr.length();
                    byte[] msg = messageStr.getBytes();
                    DatagramPacket p = new DatagramPacket(msg, msg_length, local, port);
                    s.send(p);
                    Log.d("ClientAction", "message sent to " + local.getHostAddress());
                } catch (Exception e) {
                    Log.d("ClientAction", "error  " + e.toString());
                }
            }
        }.start();

    }

    public void sendDMessage(double[] message, int prt, InetAddress address) {
        final int port = prt;
        final double[] messageStr = message;
        final InetAddress local = address;
        new Thread() {
            public void run() {

                try {
                    DatagramSocket s = new DatagramSocket();

                    byte[] msg = ByteDouble.toByteArray(messageStr);
                    DatagramPacket p = new DatagramPacket(msg, msg.length, local, port);
                    s.send(p);
                    Log.d("ClientAction", "message sent to " + local.getHostAddress());
                } catch (Exception e) {
                    Log.d("ClientAction", "error  " + e.toString());
                }
            }
        }.start();

    }

    public void broadCastMessage(String message, int port) {
        sendMessage(message,port,MainActivity.broadCastAd);
    }


    static public InetAddress getBroadcastAddress(Activity act) throws IOException {
        WifiManager wifi = (WifiManager) act.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));
        return InetAddress.getByAddress(quads);
    }

    static public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }



}
