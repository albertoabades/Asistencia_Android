package com.example.asistencia;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abades.alberto.tfg_and_2.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class Login extends ActionBarActivity {


    private static final int PORT = 9090;
    private static final String HOST = "192.168.1.103";

    private Socket fd;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Comandos comando;

    private String dni;
    private String password;
    private String responseString;

    EditText et1;
    EditText et2;

    private boolean correcto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et1 = (EditText)findViewById(R.id.login_user);
        et2 = (EditText)findViewById(R.id.login_password);
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

    public class LoginTask extends AsyncTask<String, Void, String>{

        protected String doInBackground(String... params){
            try{
                fd = new Socket(HOST, PORT);
                dis = new DataInputStream(new BufferedInputStream(fd.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(fd.getOutputStream()));
                dni = et1.getText().toString();
                password = et2.getText().toString();
                comando = new ComandoLogin(dni, password);
                comando.sendTo(dos);
                Respuestas response = Respuestas.receiveFrom(dis);
                System.out.println(response);
                responseString = response.toString();
                correcto = response.toString().equalsIgnoreCase("[rok]message: Added");
            }catch(Exception e){
                throw new RuntimeException(this + ": " + e);
            }finally{
                try{
                    if(fd != null){
                        fd.close();
                    }
                    if(dis != null){
                        dis.close();
                    }
                    if(dos != null){
                        dos.close();
                    }
                }catch(Exception e){
                    /////OOOOOOK
                }
            }
            return null;
        }

        protected void onPostExecute(String result){
            System.out.println("El resultado del login ha sido: " + correcto);
            if(correcto){
                goAcciones();
            }else{
                limpiarCampos();
            }

        }

    }

    public void goSignIn(View view){
        Intent intent = new Intent(this, NuevoUsuario.class);
        startActivity(intent);
    }

    public void ejecutarLogin(View view){
        LoginTask loginTask = new LoginTask();
        loginTask.execute();
    }

    public void goAcciones(){
        Intent intent = new Intent(this, Acciones.class);
        System.out.println("El dni que se ha introducido en el login es: " + dni);
        intent.putExtra("usuario",dni);
        startActivity(intent);
    }

    public void limpiarCampos(){
        Toast toast1 = Toast.makeText(getApplicationContext(), "Usuario o password incorrecto", Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER,20,20);
        toast1.show();
        et1.getText().clear();
        et2.getText().clear();
    }
}

