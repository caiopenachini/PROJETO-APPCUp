package app.cheerup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by NOVO on 15/04/2015.
 */
public class ReadComments extends ActionBarActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_comments);
    }

    public void goto_ajouter_patient(View view) {
        Intent intention = new Intent(this,ajouter_patient.class);
        startActivity(intention);
    }

    public void goto_rechercher_patient(View view) {
        Intent intention = new Intent(this,rechercher_patient.class);
        startActivity(intention);
    }

    public void goto_poster_compterendu(View view) {
        Intent intention = new Intent(this,poster_compterendu.class);
        startActivity(intention);
    }

}
