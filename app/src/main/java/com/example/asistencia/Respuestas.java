package com.example.asistencia;

import java.io.DataInputStream;
import java.util.ArrayList;

/**
 * Created by alberto on 28/3/18.
 */
public abstract class Respuestas extends Mensajes{

    public Respuestas(byte kind){
        super(kind);
    }

    public static Respuestas receiveFrom(DataInputStream dis){
        byte kind = 0;
        try{
            kind = dis.readByte();
            switch(kind){
                case ROK:
                    //SOMETHING
                    return new RespuestaOK(dis);
                case RERROR:
                    //return new ErrorResponse(dis);
                    return new RespuestaError(dis);
                case RLISTARCARRERAS:
                    return new RespuestaListarCarreras(dis);
                case RLISTARASIGNATURAS:
                    return new RespuestaListarAsignaturas(dis);
                case RLISTARMISCARRERAS:
                    //return new
                    return new RespuestaListarCarreras(dis);
                case RLISTARMISASIGNATURAS:
                    //return new
                    return new RespuestaListarAsignaturas(dis);
                default:
                    throw new RuntimeException("receivefrom: unknown response receive");
            }
        }catch(Exception e){
            throw new RuntimeException("receivefrom: " + e);
        }
    }

    public ArrayList<String> toArray(){
        return null;
    }
}

