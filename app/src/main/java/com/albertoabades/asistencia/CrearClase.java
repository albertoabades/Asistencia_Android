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


public class CrearClase extends ActionBarActivity {

    private static final int PORT = 9090;
    private static final String HOST = "192.168.1.103";

    private Socket fd;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Comandos comando;

    private Spinner spinnerMisCarreras;
    private Spinner spinnerMisAsignaturas;
    private Spinner spinnerHorarioDia;
    private Spinner spinnerHorarioMes;
    private Spinner spinnerHorarioAño;
    private Spinner spinnerHorarioHora;
    private Spinner spinnerHorarioMinutos;

    private String dias[] = {"1","2"};
    private String meses[] = {"3","4"};
    private String años[] = {"2019","2020","2021","2022","2023"};
    private String horas[] = {"00","01","13","15","22"};
    private String minutos[] = {"00","01","02","03"};

    private String dia;
    private String mes;
    private String año;
    private String hora;
    private String minuto;

    private String toSplitMiCarrera;
    private String[] partsMiCarrera;
    private String idMiCarrera;
    private int miCarrera;

    private String toSplitMiAsignatura;
    private String[] partsMiAsignatura;
    private String idMiAsignatura;
    private int miAsignatura;

    public String usuario;
    public String horario;

    private boolean correcto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_clase);

        usuario = getIntent().getExtras().getString("usuario");
        System.out.println("El dni del layout de Acciones es: " + usuario);

        HorarioDias();
        HorarioMeses();
        HorarioAños();
        HorarioHoras();
        HorarioMinutos();

        MisCarrerasTask misCarrerasTask = new MisCarrerasTask();
        misCarrerasTask.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_clase, menu);
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

    public class MisCarrerasTask extends AsyncTask<String, Void, String> {
        ArrayList<String> listaMisCarreras = new ArrayList<String>();
        @Override
        protected String doInBackground(String... params){
            try{
                fd = new Socket(HOST, PORT);
                dis = new DataInputStream(new BufferedInputStream(fd.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(fd.getOutputStream()));
                comando = new ComandoListarMisCarreras(usuario);
                comando.sendTo(dos);
                Respuestas response = Respuestas.receiveFrom(dis);
                listaMisCarreras = response.toArray();
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
                    ///OOOOOOOOOK
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            MisCarreras(listaMisCarreras);
            spinnerMisCarreras.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener(){
                        public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                            toSplitMiCarrera = parent.getItemAtPosition(position).toString();
                            partsMiCarrera = toSplitMiCarrera.split("\\|");
                            idMiCarrera = partsMiCarrera[0];
                            miCarrera = Integer.parseInt(idMiCarrera);
                        }

                        public void onNothingSelected(AdapterView<?> parent){
                            System.out.println("");
                        }
                    }
            );
        }
    }

    public class MisAsignaturasTask extends AsyncTask<String, Void, String>{
        ArrayList<String> listaMisAsignaturas = new ArrayList<String>();
        @Override
        protected String doInBackground(String... params){
            try{
                fd = new Socket(HOST, PORT);
                dis = new DataInputStream(new BufferedInputStream(fd.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(fd.getOutputStream()));
                comando = new ComandoListarMisAsignaturas(usuario, miCarrera);
                comando.sendTo(dos);
                Respuestas response = Respuestas.receiveFrom(dis);
                listaMisAsignaturas = response.toArray();
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
                    //OOOOOOOOK
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            MisAsignaturas(listaMisAsignaturas);
            MiCarreraYMiAsignatura();
            StringBuilder sbHorario = new StringBuilder();
            sbHorario.append(dia);
            sbHorario.append("-");
            sbHorario.append(mes);
            sbHorario.append("-");
            sbHorario.append(año);
            sbHorario.append(" ");
            sbHorario.append(hora);
            sbHorario.append(":");
            sbHorario.append(minuto);
            horario = sbHorario.toString();
        }
    }

    public class CrearClaseTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params){
            try{
                fd = new Socket(HOST, PORT);
                dis = new DataInputStream(new BufferedInputStream(fd.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(fd.getOutputStream()));
                comando = new ComandoCrearClase(usuario, horario, miCarrera, miAsignatura);
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
                    //OOOOOOOK
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if(correcto){
                Creado();
            }else{
                Error();
            }
        }
    }

    private void MisCarreras(ArrayList<String> arrayMisCarreras){
        spinnerMisCarreras = (Spinner) findViewById(R.id.spinner_clase_carreras);
        spinnerMisCarreras = (Spinner) this.findViewById(R.id.spinner_clase_carreras);
        ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayMisCarreras);
        adaptador1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMisCarreras.setAdapter(adaptador1);
    }

    private void MisAsignaturas(ArrayList<String> arrayMisAsignaturas){
        spinnerMisAsignaturas = (Spinner) findViewById(R.id.spinner_clase_asignaturas);
        spinnerMisAsignaturas = (Spinner) this.findViewById(R.id.spinner_clase_asignaturas);
        ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayMisAsignaturas);
        adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMisAsignaturas.setAdapter(adaptador2);
    }

    private void HorarioDias(){
        spinnerHorarioDia = (Spinner) findViewById(R.id.spinner_horario_dias);
        spinnerHorarioDia = (Spinner) this.findViewById(R.id.spinner_clase_asignaturas);
        ArrayAdapter<String> adaptador3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dias);
        adaptador3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioDia.setAdapter(adaptador3);
    }

    private void HorarioMeses(){
        spinnerHorarioMes = (Spinner) findViewById(R.id.spinner_horario_dias);
        spinnerHorarioMes = (Spinner) this.findViewById(R.id.spinner_clase_asignaturas);
        ArrayAdapter<String> adaptador4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meses);
        adaptador4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioMes.setAdapter(adaptador4);
    }

    private void HorarioAños(){
        spinnerHorarioAño = (Spinner) findViewById(R.id.spinner_horario_dias);
        spinnerHorarioAño = (Spinner) this.findViewById(R.id.spinner_clase_asignaturas);
        ArrayAdapter<String> adaptador5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, años);
        adaptador5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioAño.setAdapter(adaptador5);
    }

    private void HorarioHoras(){
        spinnerHorarioHora = (Spinner) findViewById(R.id.spinner_horario_dias);
        spinnerHorarioHora = (Spinner) this.findViewById(R.id.spinner_clase_asignaturas);
        ArrayAdapter<String> adaptador6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, horas);
        adaptador6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioHora.setAdapter(adaptador6);
    }

    private void HorarioMinutos(){
        spinnerHorarioMinutos = (Spinner) findViewById(R.id.spinner_horario_dias);
        spinnerHorarioMinutos = (Spinner) this.findViewById(R.id.spinner_clase_asignaturas);
        ArrayAdapter<String> adaptador7 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutos);
        adaptador7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarioMinutos.setAdapter(adaptador7);
    }

    private void MiCarreraYMiAsignatura(){
        spinnerMisCarreras.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        toSplitMiCarrera = parent.getItemAtPosition(position).toString();
                        partsMiCarrera = toSplitMiCarrera.split("\\|");
                        idMiCarrera = partsMiCarrera[0];
                        miCarrera = Integer.parseInt(idMiCarrera);
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
        spinnerMisAsignaturas.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        toSplitMiAsignatura = parent.getItemAtPosition(position).toString();
                        partsMiAsignatura = toSplitMiAsignatura.split("\\|");
                        idMiAsignatura = partsMiAsignatura[0];
                        miAsignatura = Integer.parseInt(idMiAsignatura);
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
        spinnerHorarioDia.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        dia = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
        spinnerHorarioMes.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        mes = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
        spinnerHorarioAño.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        año = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
        spinnerHorarioHora.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        hora = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
        spinnerHorarioMinutos.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
                        minuto = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent){
                        System.out.println("");
                    }
                }
        );
    }

    public void ElegirMiAsignatura(View view){
        MisAsignaturasTask misAsignaturasTask = new MisAsignaturasTask();
        misAsignaturasTask.execute();
    }

    public void goAcciones(View view){
        Intent intent = new Intent(this, Acciones.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void Crear(View view){
        CrearClaseTask crearClaseTask = new CrearClaseTask();
        crearClaseTask.execute();
    }

    public void Creado(){
        Toast toast1 = Toast.makeText(getApplicationContext(), "Creado", Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER,20,20);
        toast1.show();
    }

    public void Error(){
        Toast toast1 = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER,20,20);
        toast1.show();
    }
}

