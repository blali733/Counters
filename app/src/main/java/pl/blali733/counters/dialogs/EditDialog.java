package pl.blali733.counters.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/**
 * Class responsible for creation and servicing edit window dialog.
 * @author blali733
 * @version 1.0
 * @since 0.3
 */
public class EditDialog extends DialogFragment {
    /**
     * Method used to set up dialog window.
     * @param savedInstanceState Application state.
     * @return Created dialog.
     * @since 1.0
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        return builder.create();
    }
}
