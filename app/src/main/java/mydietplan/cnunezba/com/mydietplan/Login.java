package mydietplan.cnunezba.com.mydietplan;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.Collection;
import java.util.List;

public class Login extends Activity {

    private static final String PREFS_KEY_USER_COOKIE = "session-cookie";
    private static final String PREFS_KEY_USER_LOGGED_COOKIE = "session-logged-cookie";
    private static final String PREFS_KEY_RECORDAR_DATOS = "recordar-datos-sesion";
    private static final String PREFS_KEY_INICIO_AUTOMATICO = "inicio-sesion-automatico";
    private static final String PREFS_KEY_USER_NAME = "user-name";
    private static final String PREFS_KEY_USER_PASSWORD = "user-password";



    /**
     * Whether or not we're showing the back of the card (otherwise showing the front).
     */
    private boolean mShowingBack = false;

    public User usuario_logado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //View decorView = getWindow().getDecorView();
        // Hide the status bar.
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        if(prefs.getBoolean(PREFS_KEY_RECORDAR_DATOS, false)){
            CheckBox chkRecordarme = (CheckBox) findViewById(R.id.chkRecordarDatos);

            TextView txtUser = (TextView) findViewById(R.id.txtUser);
            TextView txtPass = (TextView) findViewById(R.id.txtPass);

            chkRecordarme.setChecked(true);

            txtUser.setText(prefs.getString(PREFS_KEY_USER_NAME, ""));
            txtPass.setText(prefs.getString(PREFS_KEY_USER_PASSWORD, ""));

            if(prefs.getBoolean(PREFS_KEY_INICIO_AUTOMATICO, false)){
                CheckBox chkLoginAutomatico = (CheckBox) findViewById(R.id.chkIniSesAutomatico);

                chkLoginAutomatico.setChecked(true);

                new getLoginUser().execute();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }









































    //LOGIN
    private class getLoginUser extends AsyncTask<Void, Void, User> {
        ProgressDialog pDialog;
        private MultiValueMap<String, Object> formData;
        protected void onPreExecute(){
            pDialog = new ProgressDialog(Login.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Comprobando usuario...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected User doInBackground(Void... params) {
            try {
                EditText user = (EditText)findViewById(R.id.txtUser);
                EditText pass = (EditText)findViewById(R.id.txtPass);
                String valUser = user.getText().toString();
                String valPass = pass.getText().toString();

                /*String valUser = "carlosf";
                String valPass = "123456";*/


                // populate the data to post
                formData = new LinkedMultiValueMap<String, Object>();
                formData.add("user", valUser);
                formData.add("pass", valPass);





                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                // Populate the MultiValueMap being serialized and headers in an HttpEntity object to use for the request
                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                        formData, requestHeaders);


                //RestTemplate restTemplate = new RestTemplate(true);
                RestTemplate restTemplate = HttpClientFactory.getRestTemplate();


                final String url = "http://midietasana.esy.es/Slim/login";

                /*==============================*/
                /*============PROXY=============*/
                /*==============================*/
                /*HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                DefaultHttpClient defaultHttpClient = (DefaultHttpClient)factory.getHttpClient();
                defaultHttpClient.getCredentialsProvider().setCredentials(new AuthScope(PROXY_HOST, PROXY_PORT),
                        new UsernamePasswordCredentials(PROXY_USER,
                                PROXY_PASSWORD));
                factory.setHttpClient(defaultHttpClient);
                restTemplate.setRequestFactory(factory);*/
                /*==============================*/
                /*============PROXY=============*/
                /*==============================*/


                // Make the network request, posting the message, expecting a String in response from the server
                /*ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                        String.class);

                // Return the response body to display to the user
                return response.getBody();*/

                // Add the String message converter
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());


                ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                        User.class);

                usuario_logado = response.getBody();

                HttpHeaders rHeaders = response.getHeaders();
                List<String> val = rHeaders.get("Set-Cookie");

                if(null != val) {
                    SharedPreferences prefs =
                            getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    String cookie = val.get(0);
                    String cookie_logged = getCookieUser(val, usuario_logado.getNombre());
                    editor.putString(PREFS_KEY_USER_COOKIE, cookie);
                    editor.putString(PREFS_KEY_USER_LOGGED_COOKIE, cookie_logged);

                    editor.commit();

                    Log.e("Cookie devuelta al logarse: ", cookie);
                }

                return usuario_logado;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
            }

            return null;


        }

        private String getCookieUser (List<String> cookies, String user){
            String cookie_user = cookies.get(0);

            for(int i = 0; i < cookies.size(); i++){
                cookie_user = cookies.get(i);
                if(cookie_user.contains(user)){
                    return cookie_user;
                }
            }

            return cookie_user;

        }

        @Override
        protected void onPostExecute(User user) {
            pDialog.dismiss();
            if(user != null) {

                //Recordamos datos
                CheckBox chkRecordarme = (CheckBox) findViewById(R.id.chkRecordarDatos);
                CheckBox chkLoginAutomatico = (CheckBox) findViewById(R.id.chkIniSesAutomatico);

                TextView txtPass = (TextView) findViewById(R.id.txtPass);


                if(chkRecordarme.isChecked()){

                    SharedPreferences prefs =
                            getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(PREFS_KEY_USER_NAME, user.getNombre());
                    editor.putString(PREFS_KEY_USER_PASSWORD, txtPass.getText().toString());

                    editor.putBoolean(PREFS_KEY_RECORDAR_DATOS, true);
                    if(chkLoginAutomatico.isChecked()){
                        editor.putBoolean(PREFS_KEY_INICIO_AUTOMATICO, true);
                    }

                    editor.commit();
                }



                Toast.makeText(Login.this, "Datos de usuario correcto.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, MainActivity.class);

                intent.putExtra("user", user);

                startActivity(intent);
                Login.this.finish();


            }else {
                Toast.makeText(Login.this, "Datos de usuario incorrectos.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onInicioAutomaticoClick (View view){
        CheckBox chkLoginAutomatico = (CheckBox) findViewById(R.id.chkIniSesAutomatico);
        CheckBox chkRecordarme = (CheckBox) findViewById(R.id.chkRecordarDatos);

        if(chkLoginAutomatico.isChecked()){
            chkRecordarme.setChecked(true);
        }
    }

    public void onClickLogin (View v){
        new getLoginUser().execute();

    }
}
