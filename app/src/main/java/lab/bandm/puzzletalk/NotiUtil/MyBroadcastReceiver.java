package lab.bandm.puzzletalk.NotiUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Objects;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Bundle bundle=intent.getExtras();
        StringBuilder str= new StringBuilder();

        if(bundle!=null){
            Object[] pdus=(Object[])bundle.get("pdus");

            SmsMessage[] message=new SmsMessage[Objects.requireNonNull(pdus).length];

            for(int i=0;i<message.length;i++){
                message[i]=SmsMessage.createFromPdu((byte[]) pdus[i]);

                str.append(message[i].getOriginatingAddress()).append(" / 메시지 : ").append(message[i].getMessageBody().toString()).append("\n");

            }
            Toast.makeText(context, str.toString(),Toast.LENGTH_LONG).show();
        }

    }
}