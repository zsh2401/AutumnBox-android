package top.atmb.autumnbox.acp.processor;

import top.atmb.autumnbox.acp.ACP;

/**
 * Created by zsh24 on 01/21/2018.
 */

public final class ACPDataBuilder {
    private static final String TAG = "ACPDataBuilder";
    private byte fCode = ACP.FCODE_UNKNOW_ERR;
    private byte[] data = new byte[0];
    public int getTotalLenght(){
        return getDataLenght() + 1;
    }
    public int getDataLenght(){
        return data.length;
    }
    public byte fCode(){
        return fCode;
    }
    public void setfCode(byte fCode){
        this.fCode = fCode;
    }
    public void setData(byte[] bytes){
        ACP.printLog(TAG,"set data" + bytes.length);
        this.data = bytes;
    }
    public byte[] toBytes(){
        byte[] result = new byte[getTotalLenght()];
        result[0] = fCode;
        if(getDataLenght() > 0){
            System.arraycopy(data,0,result, 1,data.length);
        }
//        ACP.printLog(TAG,"fCode->"+ result[0]);
//        ACP.printLog(TAG,"lenght ->" + getTotalLenght());
        return result;
    }
}
