package pl.blali733.counters.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import pl.blali733.counters.storage.Settings;
import pl.blali733.counters.R;

/**
 * Class responsible for settings dialog creation and servicing.
 * @author blali733
 * @version 1.0
 * @since 0.5 app / 1.1 pkg
 */
public class SettingsDialog extends Activity {

    /**
     * Method populating counter creation screen.
     * @param savedInstanceState State of application.
     * @since 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);

        Switch swDirection = findViewById(R.id.settings_scrolling_direction);
        swDirection.setChecked(Settings.getInstance().isDirectionUp());
        swDirection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.getInstance().setDirectionUp(buttonView.isChecked());
            }
        });
    }
}
