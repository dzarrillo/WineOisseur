package com.dzartek.wineoisseur.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dzartek.wineoisseur.R;
import com.dzartek.wineoisseur.utils.Constants;

public class FavoriteWineReceiver extends BroadcastReceiver {
    private final String TAG = FavoriteWineReceiver.class.getName();
    public static final String PROCESS_RESPONSE = "com.dzartek.wineoisseur.intent.action.PROCESS_RESPONSE";

    public FavoriteWineReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String responseString = intent.getStringExtra(Constants.RESPONSE_STRING);
        String reponseMessage = intent.getStringExtra(Constants.RESPONSE_MESSAGE);

        switch (responseString) {
            case Constants.ACTION_ISFAVORITE:

                if (reponseMessage.equals("true")) {
                  //  Log.d(TAG, "ISFAVORITE: " + reponseMessage);
                    Toast.makeText(context, context.getString(R.string.FavoriteWineReceiver)
                            + responseString + context.getString(R.string.message_Receiver) + reponseMessage, Toast.LENGTH_SHORT).show();

                }

                break;
            case Constants.ACTION_DELETE:
//                    showMyToast("FavoriteWine deleted! " + responseString + " - Message: " + reponseMessage);
                Toast.makeText(context, context.getString(R.string.favoriteWine_deleted)
                        + responseString
                        + context.getString(R.string.message_Receiver) + reponseMessage, Toast.LENGTH_SHORT).show();
                break;
            case Constants.ACTION_SAVE:
                Toast.makeText(context, context.getString(R.string.favoriteWine_saved)
                        + responseString
                        + context.getString(R.string.message_Receiver) + reponseMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
