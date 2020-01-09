package lab.bandm.puzzletalk;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;

public class GetToken extends FirebaseInstanceId {

  public   GetToken(){
      super();

  }
    @NonNull
    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public long getCreationTime() {
        return super.getCreationTime();
    }

    @NonNull
    @Override
    public Task<InstanceIdResult> getInstanceId() {
        return super.getInstanceId();
    }

    @Override
    public void deleteInstanceId() throws IOException {
        super.deleteInstanceId();
    }

    @Nullable
    @Override
    public String getToken(@NonNull String s, @NonNull String s1) throws IOException {
        return super.getToken(s, s1);
    }

    /*static FirebaseInstanceId firebaseInstanceId;
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
    }*/
}
