package sg.edu.np.mad.moviespaceapp.SendFameDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Scanner;

import sg.edu.np.mad.moviespaceapp.R;

public class SendFameDialog extends AppCompatDialogFragment {
    private EditText editTextsentfame;
    public interface OnInputSelected{
        void sendInput(int input);
    }
    public OnInputSelected monInputSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sendfame_dialog_item, null);

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String sentfame = editTextsentfame.getText().toString();
                            int int_sentfame = Integer.parseInt(sentfame);

                            monInputSelected.sendInput(int_sentfame);
                        }catch(Exception e){

                        }
                    }
                });

        editTextsentfame = view.findViewById(R.id.edit_username);

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            monInputSelected = (OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.d("e",e.toString());
        }
    }
}
