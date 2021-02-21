package com.example.asistencia;

import java.io.DataInputStream;

/**
 * Created by alberto on 28/3/18.
 */
public abstract class Comandos extends Mensajes {

    protected static final int COMMAND_INDEX = 0;

    public Comandos(byte kind){
        super(kind);
    }

    public static Comandos receiveFrom(DataInputStream dis){
        byte kind = 0;
        try{
            kind = dis.readByte();
            switch(kind){
                case TLISTARCARRERAS:
                    //SOMETHING
                    return new ComandoListarCarreras(dis);
                case TLISTARASIGNATURAS:
                    return new ComandoListarAsignaturas(dis);
                case TAGREGARASIGNATURA:
                    return new ComandoAgregarAsignatura(dis);
                case TCREARCLASE:
                    return new ComandoCrearClase(dis);
                //case TPING:
                //SOMETHING
                //    return new ComandoPing(dis);
                default:
                    throw new RuntimeException("receivefrom: unknown command receive");
            }
        }catch(Exception e){
            throw new RuntimeException("receivefrom: " + e);
        }
    }
}

