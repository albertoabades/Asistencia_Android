package com.example.asistencia;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by alberto on 28/3/18.
 */
public class RespuestaOK extends RespuestasResultado{

    public RespuestaOK(String message){
        super(ROK);
        this.message = message;
    }

    public RespuestaOK(DataInputStream dis){
        super(ROK);
        try{
            message = readString(dis);
        }catch(Exception e){
            throw new RuntimeException("ROK receive: " + e);
        }
    }

    public void sendTo(DataOutputStream dos){
        synchronized(dos){
            super.sendTo(dos);
            try{
                writeString(dos, message);
                dos.flush();
            }catch(Exception e){
                throw new RuntimeException("ROK send: " + e);
            }
        }
    }
}

