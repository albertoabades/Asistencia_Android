package com.albertoabades.asistencia;

/**
 * Created by alberto on 28/3/18.
 */
public class RespuestasResultado extends Respuestas {

    protected String message;

    public RespuestasResultado(byte kind){
        super(kind);
    }

    public String toString(){
        return super.toString() + String.format("message: %s", message);
    }
}

