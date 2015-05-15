package app.cheerup;


//public class resultat extends ActionBarActivity {


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class resultat extends ActionBarActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> patientList;


    // url to get all products list
    private static String url_all_patients = "http://cheerup-backup.esy.es/server/resultatRecherchePatient.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PATIENTS = "patients";
    private static final String TAG_NOM = "nom";
    private static final String TAG_PRENOM = "prenom";
    private static final String TAG_ANNEE = "annee";
    private static final String TAG_HOPITAL= "hopital";


    // products JSONArray
    JSONArray patients = null;

    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        // Hashmap para el ListView
        patientList = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllPatients().execute();
        lista = (ListView) findViewById(R.id.listAllPatients);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }//fin onCreate


    class LoadAllPatients extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(resultat.this);
            pDialog.setMessage("Cargando pacientes. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_patients, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All Patients: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    patients = json.getJSONArray(TAG_PATIENTS);

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < patients.length(); i++) {
                        JSONObject c = patients.getJSONObject(i);

                        // Storing each json item in variable
                        String nom = c.getString(TAG_NOM);
                        String prenom = c.getString(TAG_PRENOM);
                        String annee = c.getString(TAG_ANNEE);
                        String hopital = c.getString(TAG_HOPITAL);


                        // creating new HashMap
                        HashMap map = new HashMap();

                        // adding each child node to HashMap key => value
                        map.put(TAG_NOM, nom);
                        map.put(TAG_PRENOM, prenom);
                        map.put(TAG_ANNEE, annee);
                        map.put(TAG_HOPITAL, hopital);

                        patientList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            resultat.this,
                            patientList,
                            R.layout.single_post,
                            new String[] {
                                    TAG_NOM,
                                    TAG_PRENOM,
                                    TAG_ANNEE,
                                    TAG_HOPITAL
                            },
                            new int[] {
                                    R.id.single_post_tv_nom,
                                    R.id.single_post_tv_prenom,
                                    R.id.single_post_tv_annee,
                                    R.id.single_post_tv_hopital
                            });
                    // updating listview
                    //setListAdapter(adapter);
                    lista.setAdapter(adapter);
                }
            });
        }
    }
}

