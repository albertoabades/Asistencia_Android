package com.albertoabades.asistencia;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by alberto on 18/6/18.
 */
public class ComandoListarMisAsignaturas extends Comandos{

    private static final String STR_COMMAND = "LS_MIS_ASIGNATURAS";
    private static final int LENGTH = 2;

    private String dni;
    private long idCarrera;

    public ComandoListarMisAsignaturas(String dni, long idCarrera){
        super(TLISTARMISASIGNATURAS);
        this.dni = dni;
        this.idCarrera = idCarrera;
    }

    public ComandoListarMisAsignaturas(DataInputStream dis){
        super(TLISTARMISASIGNATURAS);
        this.dni = readString(dis);
        byte[] idCarreraBytes = readBytes(dis);
        this.idCarrera = Packer.unpackLong(idCarreraBytes);
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public long getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(long idCarrera) {
        this.idCarrera = idCarrera;
    }

    @Override
    public void sendTo(DataOutputStream dos){
        synchronized(dos){
            super.sendTo(dos);
            try{
                byte[] dniBytes = this.dni.getBytes("UTF-8");
                byte[] idCarreraBytes = Packer.packLong(this.idCarrera);
                writeBytes(dos, dniBytes);
                writeBytes(dos, idCarreraBytes);
                dos.flush();
            }catch(Exception e){
                throw new RuntimeException("TLISTARMISASIGNATURAS send: " + e);
            }
        }
    }
}

