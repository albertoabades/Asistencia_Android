package com.example.asistencia;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * Created by alberto on 17/4/18.
 */
public class RespuestaListarAsignaturas extends Respuestas{

    private ListarAsignaturas[] asignaturas;

    public RespuestaListarAsignaturas(ListarAsignaturas[] asignaturas){
        super(RLISTARASIGNATURAS);
        this.asignaturas = asignaturas;
    }

    public RespuestaListarAsignaturas(DataInputStream dis){
        super(RLISTARASIGNATURAS);
        try{
            int size = Packer.unpackInt(readBytes(dis));
            this.asignaturas = new ListarAsignaturas[size];
            for(int index = 0; index  < size; index++){
                this.asignaturas[index] = ListarAsignaturas.fromRaw(readBytes(dis));
            }
        }catch(Exception e){
            throw new RuntimeException("RLISTARASIGNATURAS receive: " + e);
        }
    }

    public String toString(){
        String result = super.toString() + "\n";
        for(ListarAsignaturas asignatura: asignaturas){
            result += asignatura + "\n";
        }
        return result;
    }

    public ArrayList<String> toArray(){
        ArrayList<String> arrayAsignaturas = new ArrayList<String>();
        String asignaturaString;
        for(ListarAsignaturas asignatura: asignaturas){
            asignaturaString = asignatura.toString();
            arrayAsignaturas.add(asignaturaString);
        }
        return arrayAsignaturas;
    }

    public void sendTo(DataOutputStream dos){
        synchronized(dos){
            super.sendTo(dos);
            try{
                byte[] sizeBytes = Packer.packInt(this.asignaturas.length);
                writeBytes(dos, sizeBytes);
                if(asignaturas.length > 0){
                    for(ListarAsignaturas asignatura: asignaturas){
                        writeBytes(dos, asignatura.toRaw());
                    }
                }
                dos.flush();
            }catch(Exception e){
                throw new RuntimeException("RLISTARASIGNATURAS send: " + e);
            }
        }
    }
}
