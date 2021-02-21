package com.example.asistencia;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.abades.alberto.tfg_and_2.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class NuevoUsuario extends ActionBarActivity{

    private static final int PORT = 9090;
    private static final String HOST = "192.168.1.103";

    private Socket fd;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Comandos comando;

    private String dni;
    private String nombre;
    private String apellido;
    private String password;

    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;

    private boolean correcto = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        et1 = (EditText) findViewById(R.id.nuevo_usuario_dni);
        et2 = (EditText) findViewById(R.id.nuevo_usuario_nombre);
        et3 = (EditText) findViewById(R.id.nuevo_usuario_apellido);
        et4 = (EditText) findViewById(R.id.nuevo_usuario_password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public class NuevoUsuarioTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                fd = new Socket(HOST, PORT);
                dis = new DataInputStream(new BufferedInputStream(fd.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(fd.getOutputStream()));
                dni = et1.getText().toString();
                nombre = et2.getText().toString();
                apellido = et3.getText().toString();
                password = et4.getText().toString();
                comando = new ComandoAgregarPersona(dni, nombre, apellido, password);
                comando.sendTo(dos);
                Respuestas response = Respuestas.receiveFrom(dis);
                correcto = response.toString().equalsIgnoreCase("[rok]message: Added");
                System.out.println(response);
            } catch (Exception e) {
                throw new RuntimeException(this + ": " + e);
            } finally {
                try {
                    if (fd != null) {
                        fd.close();
                    }
                    if (dis != null) {
                        dis.close();
                    }
                    if (dos != null) {
                        dos.close();
                    }
                } catch (Exception e) {
                    /////OOOOOOOK
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

    public void volverLogin(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void mandarDatos(View view){
        NuevoUsuarioTask nuevoUsuarioTask = new NuevoUsuarioTask();
        nuevoUsuarioTask.execute();
        //if(correcto){
        Intent intent = new Intent(this, Acciones.class);
        startActivity(intent);
        //}else{
        //    Toast toast1 = Toast.makeText(getApplicationContext(), "El usuario ya existe", Toast.LENGTH_LONG);
        //    toast1.setGravity(Gravity.CENTER,20,20);
        //    toast1.show();
        //et1.getText().clear();
        //et2.getText().clear();
        //et3.getText().clear();
        //et4.getText().clear();
        //}
    }

}

