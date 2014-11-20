package mydietplan.cnunezba.com.mydietplan;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class MainActivity extends Activity {
    private static final String PREFS_KEY_USER_COOKIE = "session-cookie";
    private static final String PREFS_KEY_USER_LOGGED_COOKIE = "session-logged-cookie";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    private User usuario_logado;

    public User getUsuario_logado() {
        return usuario_logado;
    }

    public void setUsuario_logado(User usuario_logado) {
        this.usuario_logado = usuario_logado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        setUsuario_logado((User)getIntent().getExtras().getSerializable("user"));

        new getDietasUsuario().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments

        //OBTENGO LAS COMIDAS DE LA DIETA SELECCIONADA
        new getComidasDieta().execute();



        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());

            getActivity().setTitle(planet);
            return rootView;
        }
    }




    //OBTENER DIETAS DE USUARIO
    private class getDietasUsuario extends AsyncTask<Void, Void, Dieta[]> {
        private MultiValueMap<String, Object> formData;
        ProgressDialog pDialog;

        protected void onPreExecute(){
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Obteniendo tus dietas...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Dieta[] doInBackground(Void... params) {
            try {
                final String url = "http://midietasana.esy.es/Slim/getdietas";
                // populate the data to post
                formData = new LinkedMultiValueMap<String, Object>();
                formData.add("id_usuario", getUsuario_logado().getId_usuario());

                RestTemplate restTemplate = HttpClientFactory.getRestTemplate();

                //AÑADIMOS LA COOKIE DE AUTENTICADO
                SharedPreferences mPrefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                String cookie = mPrefs.getString(PREFS_KEY_USER_COOKIE,"no data");
                String cookie_logged = mPrefs.getString(PREFS_KEY_USER_LOGGED_COOKIE,"no data");

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                requestHeaders.set("Cookie", cookie);
                requestHeaders.set("Cookie-Logged", cookie_logged);

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                        formData, requestHeaders);


                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                ResponseEntity<Dieta[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                        Dieta[].class);

                Dieta[] dietas_response = response.getBody();




                return dietas_response;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Dieta[] dieta) {
            pDialog.dismiss();
            if(dieta != null) {

                List<Dieta> listaDieta = Arrays.asList(dieta);
                DietaAdapter adaptador_dieta;
                adaptador_dieta = new DietaAdapter(MainActivity.this, listaDieta);

                mDrawerList.setAdapter(adaptador_dieta);







            }else {
                Toast.makeText(MainActivity.this, "No se ha podido recuperar las dietas.", Toast.LENGTH_SHORT).show();
            }
        }
    }




    //OBTENER COMIDAS DE DIETA SELECCIONADA
    private class getComidasDieta extends AsyncTask<Void, Void, Comida[]> {
        private MultiValueMap<String, Object> formData;
        ProgressDialog pDialog;

        protected void onPreExecute(){
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Obteniendo las comidas...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Comida[] doInBackground(Void... params) {
            try {
                final String url = "http://midietasana.esy.es/Slim/getdietaseleccionada";
                // populate the data to post
                formData = new LinkedMultiValueMap<String, Object>();
                formData.add("id_dieta", "55");
                formData.add("id_usuario", getUsuario_logado().getId_usuario());

                RestTemplate restTemplate = HttpClientFactory.getRestTemplate();

                //AÑADIMOS LA COOKIE DE AUTENTICADO
                SharedPreferences mPrefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                String cookie = mPrefs.getString(PREFS_KEY_USER_COOKIE,"no data");
                String cookie_logged = mPrefs.getString(PREFS_KEY_USER_LOGGED_COOKIE,"no data");

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                requestHeaders.set("Cookie", cookie);
                requestHeaders.set("Cookie-Logged", cookie_logged);

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                        formData, requestHeaders);


                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                ResponseEntity<Comida[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                        Comida[].class);

                Comida[] comidas_response = response.getBody();




                return comidas_response;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Comida[] comidas) {
            pDialog.dismiss();
            if(comidas != null) {

                ListView lstComidaDieta = (ListView) findViewById(R.id.lstComidaDieta);
                ListView lstAlimentosComida = (ListView) findViewById(R.id.lstAlimentosComida);

                String hora_aux;



                for(int i = 0; i < comidas.length; i++){
                    List<Comida> listadoAlimentosComida = null;

                    do{
                        hora_aux = comidas[i].getHora();
                        listadoAlimentosComida.add(comidas[i]);
                        i++;
                    }while (comidas[i].getHora().equals(hora_aux));
                    AlimentosComidaAdapter alimentosComidaAdapter;
                    alimentosComidaAdapter = new AlimentosComidaAdapter(MainActivity.this, listadoAlimentosComida);
                }





                ComidaAdapter adaptador_comida;
                adaptador_comida = new ComidaAdapter(MainActivity.this, listadoComida);

                lstComidaDieta.setAdapter(adaptador_comida);








            }else {
                Toast.makeText(MainActivity.this, "No se ha podido recuperar las dietas.", Toast.LENGTH_SHORT).show();
            }
        }
    }







}