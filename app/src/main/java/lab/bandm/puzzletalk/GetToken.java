package lab.bandm.puzzletalk;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class GetToken  {

    static FirebaseInstanceId firebaseInstanceId;
    static TokenRD tokenRD;

    public static TokenRD CallToken() {
        firebaseInstanceId = FirebaseInstanceId.getInstance();
        firebaseInstanceId.getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("FIREBASE", "getInstanceId failed", task.getException());
                    return;
                }
                tokenRD = new TokenRD();
                tokenRD.setmToken(task.getResult().getToken());
            }
        });
        return tokenRD;
    }
}
