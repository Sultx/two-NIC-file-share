package org.example;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TcpClient {

    ServerSocket serverSocket;
    Socket firstSocket;
    Socket secondSocket;
    ArrayList<InetSocketAddress> addresses;
    DataOutputStream dataOutputStream;



    public TcpClient(byte[] firstHalf, byte[] secondHalf, NetworkInterface firstNic, NetworkInterface secondNic) throws IOException {

        serverSocket = new ServerSocket(30692);
        firstSocket = new Socket();
        firstSocket.bind(addresses.get(0));
        secondSocket.bind(addresses.get(1));
        firstSocket = serverSocket.accept();
        secondSocket = serverSocket.accept();

        addresses = new ArrayList<>();
        getProperInetAddress(firstNic,secondNic);

        dataOutputStream = new DataOutputStream(firstSocket.getOutputStream());
        dataOutputStream.writeInt(firstHalf.length);
        dataOutputStream = new DataOutputStream(secondSocket.getOutputStream());
        dataOutputStream.writeInt(secondHalf.length);
        new Thread(send(firstSocket,firstHalf)).start();
        new Thread(send(secondSocket,secondHalf)).start();
    }



    private void getProperInetAddress(NetworkInterface firstNic, NetworkInterface secondNic) {

        firstNic.getInetAddresses().asIterator().forEachRemaining(inetAddress -> {
            if (!inetAddress.isLoopbackAddress()  && inetAddress instanceof Inet4Address){
                addresses.add(new InetSocketAddress(inetAddress,0));
            }
        });

        secondNic.getInetAddresses().asIterator().forEachRemaining(inetAddress -> {
            if (!inetAddress.isLoopbackAddress()  && inetAddress instanceof Inet4Address){
                addresses.add(new InetSocketAddress(inetAddress,0));
            }
        });
    }

    private Runnable send(Socket socket,byte[] data){
        return () -> {
            try {
                socket.getOutputStream().write(data);
                socket.getOutputStream().close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }




}
