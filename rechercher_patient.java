package app.cheerup;

/**
 * Created by NOVO on 15/04/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class rechercher_patient extends ActionBarActivity implements OnClickListener{
    //private EditText user, pass;
    //private Button  mRegister;


    private EditText nome, prenome, hospital, ano;
    private Button  mRechercher;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //si lo trabajan de manera local en xxx.xxx.x.x va su ip local
    // private static final String REGISTER_URL = "http://xxx.xxx.x.x:1234/cas/register.php";

    //testing on Emulator:
    private static final String AJOUTER_PATIENT_URL =  "http://cheerup-backup.esy.es/server/recherchePatient.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //R.layout.NOME DO FRQGMENTO QUE SERA VINCULADO
        //EX: R.layout.register
        setContentView(R.layout.activity_rechercher_patient);

        // esquerda: nome no php
        //direita: nome no fragmento

        nome = (EditText)findViewById(R.id.nom);
        prenome = (EditText)findViewById(R.id.prenom);
        hospital = (EditText)findViewById(R.id.hopital);
        ano = (EditText)findViewById(R.id.annee);

        //direita: nome que aparece no botao
        mRechercher = (Button)findViewById(R.id.recherchePatient);
        mRechercher.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        new CreateUser().execute();

    }



    class CreateUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = new ProgressDialog(Register.this);
            pDialog = new ProgressDialog(rechercher_patient.this);
            pDialog.setMessage("Recherchant Patient...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            //String username = user.getText().toString();
            //String password = pass.getText().toString();
            String nom = nome.getText().toString();
            String prenom = prenome.getText().toString();
            String hopital = hospital.getText().toString();
            String annee = ano.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                //params.add(new BasicNameValuePair("username", username));
                //params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("nom", nom));
                params.add(new BasicNameValuePair("prenom", prenom));
                params.add(new BasicNameValuePair("hopital", hopital));
                params.add(new BasicNameValuePair("annee", annee));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        AJOUTER_PATIENT_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Patient Ajoute!", json.toString());


                    Intent i = new Intent(rechercher_patient.this, resultat.class);
                    startActivity(i);
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(rechercher_patient.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}

