package top.atmb.autumnbox.acp;

import java.net.*;
import java.io.*;

import top.atmb.autumnbox.acp.processor.ACPDataBuilder;

public class ACPServer extends Thread{
    private static final String TAG ="ACPServer";
    private ACPResponser responser;
    public void setResponser(ACPResponser responser){
        this.responser = responser;
    }
    @Override
    public void run(){
        try{
            ServerSocket server = new ServerSocket(ACP.STD_PORT);
            ACP.printLog(TAG,"server started...");
            ACP.printLog(TAG,"server listenning...");
            while(true){
                Socket client =  server.accept();
                new Thread(()->{startAcpStdFlow(client);}).start();
            }
        }catch(Exception ex){
            ACP.printLog(TAG,"server error");
            ex.printStackTrace();
        }
    }
    private void startAcpStdFlow(Socket client){
        ACP.printLog(TAG,"a client connected...");
        try{
            DataInputStream reader = new DataInputStream(client.getInputStream());
            DataOutputStream writer = new DataOutputStream(client.getOutputStream());

            byte[] requestBuffer = new byte[1024];
            int lenght =  reader.read(requestBuffer);
            ACPDataBuilder builder = responser.onRequestReceived(new String(requestBuffer,0,lenght));
            ACP.printLog(TAG,"response data lenght->" + builder.toBytes().length);
            writer.write(builder.toBytes());
            writer.flush();
            writer.close();
            reader.close();
            client.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        ACP.printLog(TAG, "a client disconnected");
    }
}