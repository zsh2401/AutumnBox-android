package top.atmb.autumnbox.acp;

import top.atmb.autumnbox.acpcommand.execute
import top.atmb.autumnbox.acpcommand.string2Command
import top.atmb.autumnbox.acpcommand.toCommand
import java.net.*;
import java.io.*;

class ACPServer: Thread() {
    companion object {
        var TAG = "ACPServer"
    }
    lateinit var receivedEventHandler:(String)->ACPDataBuilder
    override fun run() {
        try{
            var serverSocket = ServerSocket(ACP.STD_PORT)
            ACP.printLog(TAG,"server listening")
            while(true){
                var client = serverSocket.accept()
                Thread({acpFlow(client)}).start()
            }
        }catch (ex:Exception){}
    }
    private fun acpFlow(client:Socket){
        ACP.printLog(TAG,"a client connected")

        try{
            var reader = DataInputStream(client.getInputStream())
            var writer = DataOutputStream(client.getOutputStream())

            var requestBuffer = ByteArray(1024)
            var size = reader.read(requestBuffer)
            //终极抄袭C#?????
            val result =  receivedEventHandler?.invoke(String(requestBuffer,0,size))
            writer.write(result.toBytes())

            writer.flush()
            writer.close()
            reader.close()
            client.close()
        }catch (ex:Exception){
            ex.printStackTrace()
            try{
                client.close()
            }catch (ex:Exception){}
        }
        ACP.printLog(TAG,"a client disconnected")
    }
}

//public class ACPServer extends Thread{
//    private static final String TAG ="ACPServer";
//    private ACPResponser responser;
//    public void setResponser(ACPResponser responser){
//        this.responser = responser;
//    }
//    @Override
//    public void run(){
//        try{
//            ServerSocket server = new ServerSocket(ACP.STD_PORT);
//            ACP.Compani(TAG,"server started...");
//            ACP.printLog(TAG,"server listenning...");
//            while(true){
//                Socket client =  server.accept();
//                new Thread(()-> startAcpStdFlow(client)).start();
//            }
//        }catch(Exception ex){
//            ACP.printLog(TAG,"server error");
//            ex.printStackTrace();
//        }
//    }
//    private void startAcpStdFlow(Socket client){
//        ACP.printLog(TAG,"a client connected...");
//        try{
//            DataInputStream reader = new DataInputStream(client.getInputStream());
//            DataOutputStream writer = new DataOutputStream(client.getOutputStream());
//
//            byte[] requestBuffer = new byte[1024];
//            int lenght =  reader.read(requestBuffer);
//            ACPDataBuilder builder = responser.onRequestReceived(new String(requestBuffer,0,lenght));
//            ACP.printLog(TAG,"response data lenght->" + builder.toBytes().length);
//            writer.write(builder.toBytes());
//            writer.flush();
//            writer.close();
//            reader.close();
//            client.close();
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        ACP.printLog(TAG, "a client disconnected");
//    }
//}