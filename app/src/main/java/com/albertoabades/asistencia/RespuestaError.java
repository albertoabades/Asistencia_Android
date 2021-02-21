package com.example.asistencia;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by alberto on 16/5/18.
 */
public class RespuestaError extends RespuestasResultado {

    public RespuestaError(String message){
        super(RERROR);
        this.message = message;
    }

    public RespuestaError(DataInputStream dis){
        super(RERROR);
        try{
            this.message = readString(dis);
        }catch(Exception e){
            throw new RuntimeException("RERROR receive: " + e);
        }
    }

    public void sendTo(DataOutputStream dos){
        synchronized(dos){
            super.sendTo(dos);
            try{
                writeString(dos, message);
                dos.flush();
            }catch(Exception e){
                throw new RuntimeException("RERROR send: " + e);
            }
        }
    }
}
