package com.albertoabades.asistencia;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.abades.alberto.tfg_and_2.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ElegirAsignatura extends ActionBarActivity {

    private static final int PORT = 9090;
    private static final String HOST= "192.168.1.103";

    private Socket fd;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Comandos comando;

    private Spinner spinnerCarreras;
    private Spinner spinnerAsignaturas;

    private int carrera;
    private int asignatura;
    private String idcarrera;
    private String idasignatura;
    private String[] partsCarrera;
    private String[] partsAsignatura;
    private String toSplitCarrera;
    private String toSplitAsignatura;

    private boolean correcto = false;
    public String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_asignatura);

        usuario = getIntent().getExtras().getString("usuario");
        System.out.println("El dni introducido es: " + usuario);

        CarrerasTask carrerasTask = new CarrerasTask();
        carrerasTask.execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_elegir_asignatura, menu);
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

    public class CarrerasTask extends AsyncTask<String, Void, String>{
        ArrayList<String> listaCarreras = new ArrayList<String>();
        @Override
        protected String doInBackground(String... params){
            try{
                fd = new Socket(HOST, PORT);
                dis = new DataInputStream(new BufferedInputStream(fd.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(fd.getOutputStream()));
                comando = new ComandoListarCarreras();
                comando.sendTo(dos);
                Respuestas response = Respuestas.receiveFrom(dis);
                System.out.println(response);
                listaCarreras = response.toArray();
                System.out.println(listaCarreras);
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
                    /////OOOOOOOK
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            Carreras(listaCarreras);
            spinnerCarreras.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener(){
                        public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                            toSplitCarrera = parent.getItemAtPosition(position).toString();
                            System.out.println("Has escogido: " + toSplitCarrera);
                            partsCarrera = toSplitCarrera.split("\\|");
                            idcarrera = partsCarrera[0];
                            System.out.println("El codigo es: " + idcarrera);
                            carrera = Integer.parseInt(idcarrera);
                            //carrera = 2040;
                        }

                        public void onNothingSelected(AdapterView<?> parent){
                            System.out.println("");
                        }
                    }
            );
        }
    }

    public class AsignaturasTask extends AsyncTask<String, Void, String>{
        ArrayList<String> listaAsignaturas = new ArrayList<String>();
        @Override
        protected String doInBackground(String... params){
            try{
                fd = new Socket(HOST, PORT);
                dis = new DataInputStream(new BufferedInputStream(fd.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(fd.getOutputStream()));
                //comando = new ComandoListarAsignaturas(2039);
                comando = new ComandoListarAsignaturas(carrera);
                comando.sendTo(dos);
                Respuestas response = Respuestas.receiveFrom(dis);
                System.out.println(response);
                listaAsignaturas = response.toArray();
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
                    /////OOOOOOOK
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            Asignaturas(listaAsignaturas);
            CarreraYAsignatura();
            System.out.println("La carrera y la asignatura son: " + carrera + "|" + asignatura);
        }
    }

    public class AgregarAsignaturaTask extends AsyncTask<String, Void, String>{

        protected String doInBackground(String... params){
            try{
                fd = new Socket(HOST, PORT);
                dis = new DataInputStream(new BufferedInputStream(fd.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(fd.getOutputStream()));
                comando = new ComandoAgregarAsignatura(usuario, carrera, asignatura);
                comando.sendTo(dos);
                Respuestas response = Respuestas.receiveFrom(dis);
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
                    //OOOOOOK
                }
            }
            return null;
        }

        protected void onPostExecute(String result){
            if(correcto){
                Agregado();
            }else{
                Error();
            }
        }
    }

    private void Carreras(ArrayList<String> arrayCarreras) {
        spinnerCarreras = (Spinner) findViewById(R.id.spinner_agregar_carreras);
        spinnerCarreras = (Spinner) this.findViewById(R.id.spinner_agregar_carreras);
        ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayCarreras);
        adaptador1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarreras.setAdapter(adaptador1);
    }

    private void Asignaturas(ArrayList<String> arrayAsignaturas){
        spinnerAsignaturas = (Spinner) findViewById(R.id.spinner_agregar_asignaturas);
        spinnerAsignaturas = (Spinner) this.findViewById(R.id.spinner_agregar_asignaturas);
        ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayAsignaturas);
        adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAsignaturas.setAdapter(adaptador2);
    }

    private void CarreraYAsignatura(){
        spinnerCarreras.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        toSplitCarrera = parent.getItemAtPosition(position).toString();
                        System.out.println("Has escogido: " + toSplitCarrera);
                        partsCarrera = toSplitCarrera.split("\\|");
                        idcarrera = partsCarrera[0];
                        System.out.println("El codigo es: " + idcarrera);
                        carrera = Integer.parseInt(idcarrera);
                        //carrera = 2040;
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
        spinnerAsignaturas.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        toSplitAsignatura = parent.getItemAtPosition(position).toString();
                        System.out.println("Has escogido: " + toSplitAsignatura);
                        partsAsignatura = toSplitAsignatura.split("\\|");
                        idasignatura = partsAsignatura[0];
                        System.out.println("El codigo es: " + idasignatura);
                        asignatura = Integer.parseInt(idasignatura);
                        //carrera = 2040;
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
    }

    public void ElegirAsignaturas(View view){
        AsignaturasTask asignaturasTask = new AsignaturasTask();
        asignaturasTask.execute();
    }

    public void goAcciones(View view){
        Intent intent = new Intent(this, Acciones.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void Agregar(View view){
        AgregarAsignaturaTask agregarAsignaturaTask = new AgregarAsignaturaTask();
        agregarAsignaturaTask.execute();
    }

    public void Agregado(){
        Toast toast1 = Toast.makeText(getApplicationContext(), "Agregado", Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER,20,20);
        toast1.show();
    }

    public void Error(){
        Toast toast1 = Toast.makeText(getApplicationContext(), "Ya existe", Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER,20,20);
        toast1.show();
    }

}

