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
    private SendFameDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        listener = (SendFameDialogListener) getTargetFragment();
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
                        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

                        try {
                            String sentfame = editTextsentfame.getText().toString();
                            Integer int_sentfame = Integer.parseInt(sentfame);

                            // send data back to fragment
                            ((SendFameDialogListener) getTargetFragment()).getinputedfame(int_sentfame);
                        }catch(Exception e){

                        }
                    }
                });

        editTextsentfame = view.findViewById(R.id.edit_username);

        return builder.create();
    }
   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SendFameDialogListener) context;
        } catch (ClassCastException e) {
            Log.d("e",e.toString());
        }
    }*/

    public interface SendFameDialogListener {
        void getinputedfame(Integer data);
    }
}
