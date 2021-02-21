package com.albertoabades.asistencia;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alberto on 16/3/19.
 */
public class Tabla {

    private TableLayout tabla;
    private ArrayList<TableRow> filas;
    private Activity actividad;
    private Resources rs;
    private int FILAS;
    private int COLUMNAS;

    public Tabla(Activity actividad, TableLayout tabla){
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
    }

    public void agregarCabecera(int recursoCabecera){
        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(actividad);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(layoutFila);

        String[] arrayCabecera = rs.getStringArray(recursoCabecera);
        COLUMNAS = arrayCabecera.length;

        for(int i = 0; i<arrayCabecera.length; i++){
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(arrayCabecera[i]), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setText(arrayCabecera[i]);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            //texto.setTextAppearance(actividad, R.style.estilo_celda);
            //texto.setBackgroundResource(R.drawable.tabla_celda_cabecera);
            texto.setLayoutParams(layoutCelda);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }

    public void agregarFilaTabla(ArrayList<String> elementos){
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        for(int i = 0; i < elementos.size(); i++){
            TextView texto = new TextView(actividad);
            texto.setText(String.valueOf(elementos.get(i)));
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            //texto.setTextAppearance(actividad, R.style.estilo_celda);
            //texto.setBackgroundResource(R.drawable.tabla_celda);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(texto.getText().toString()), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setLayoutParams(layoutCelda);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }

    public void eliminarFila(int indiceFilaeliminar){
        if(indiceFilaeliminar > 0 && indiceFilaeliminar < FILAS){
            tabla.removeViewAt(indiceFilaeliminar);
            FILAS--;
        }
    }

    public int getFILAS(){
        return FILAS;
    }

    public int getCOLUMNAS(){
        return COLUMNAS;
    }

    public int getCeldasTotales(){
        return FILAS*COLUMNAS;
    }

    private int obtenerAnchoPixelesTexto(String texto){
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }
}

