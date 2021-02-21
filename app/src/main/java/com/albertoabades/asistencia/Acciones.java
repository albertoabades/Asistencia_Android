package com.example.asistencia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Acciones extends ActionBarActivity  {

    public String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);

        usuario = getIntent().getExtras().getString("usuario");
        System.out.println("El dni del layout de Acciones es: " + usuario);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acciones, menu);
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

    public void goLogin(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void goElegirAsignatura(View view){
        Intent intent = new Intent(this, ElegirAsignatura.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void goComprobarClase(View view){
        Intent intent = new Intent(this, ComprobarClase.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void goAgregarClase(View view){
        Intent intent = new Intent(this, AgregarClase.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void goCrearClase(View view){
        Intent intent = new Intent(this, CrearClase.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }
}
