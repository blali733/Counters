package pl.blali733.counters.dialogs;

import android.app.Activity;
import android.os.Bundle;

import pl.blali733.counters.R;

/**
 * Class responsible for creation and servicing edit window dialog.
 * @author blali733
 * @version 1.0
 * @since 0.3
 */
public class EditDialog extends Activity {
    /**
     * Method used to set up dialog window.
     * @param savedInstanceState Application state.
     * @since 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        this.setFinishOnTouchOutside(false);


    }

    /**
     * Method overriding action of back key into finishing activity.
     * @since 1.0
     */
    @Override
    public void onBackPressed(){
        finish();
    }
}
