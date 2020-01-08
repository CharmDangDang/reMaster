package lab.bandm.puzzletalk.NotiUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Bundle bundle=intent.getExtras();
        String str="";

        if(bundle!=null){
            Object[] pdus=(Object[])bundle.get("pdus");

            SmsMessage[] message=new SmsMessage[pdus.length];

            for(int i=0;i<message.length;i++){
                message[i]=SmsMessage.createFromPdu((byte[]) pdus[i]);

                str+=message[i].getOriginatingAddress()+" / 메시지 : "+message[i].getMessageBody().toString()+"\n";

            }
            Toast.makeText(context,str,Toast.LENGTH_LONG).show();
        }

    }
}