package top.atmb.autumnbox.acp

import java.util.*

/**
 * Created by zsh24 on 01/22/2018.
 */
data class ACPDataBuilder(var fCode:Byte = 1,var data:ByteArray = ByteArray(0)){
    val totalSize:Int get() { return data.size + 1 }
    val dataSize:Int get() {return data.size}

    fun toBytes():ByteArray{
        var result:Array<Byte> = ByteArray(totalSize).toTypedArray()
        result[0] = fCode
        if(data.isNotEmpty()){
            System.arraycopy(data.toTypedArray(), 0,result,1,dataSize)
        }
        return result.toByteArray()
    }
}