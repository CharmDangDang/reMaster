package lab.bandm.puzzletalk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class OnlongClick implements View.OnLongClickListener {

    @Override
    public boolean onLongClick(View v) {

            ClipboardManager clipboardManager = (ClipboardManager) MainActivity.context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("text", ((TextView) v).getText()));
            Toast.makeText(MainActivity.context, "번호가 복사되었습니다.", Toast.LENGTH_SHORT).show();

        return false;
    }
}
