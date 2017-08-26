package pl.blali733.counters.events;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import pl.blali733.counters.events.interfaces.AuthorityChange;

/**
 * Extended Fragment class, implements custom events.
 * @author blali733
 * @version 1.0
 * @since 0.3
 */
public class AuthFragment extends Fragment implements AuthorityChange {
    /**
     * Default implementation of method executed after user authority has changed.
     * @since 1.0
     */
    @Override
    public void onAuthChange(){
        Toast.makeText(getContext(),"Authority change detected and not implemented.",Toast.LENGTH_LONG).show();
    }
}
