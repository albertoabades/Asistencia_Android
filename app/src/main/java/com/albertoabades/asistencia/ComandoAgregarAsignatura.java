package com.albertoabades.asistencia;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ComandoAgregarAsignatura extends Comandos{

    private String dni;
    private int idCarrera;
    private long idAsignatura;

    public ComandoAgregarAsignatura(String dni, int idCarrera, long idAsignatura){
        super(TAGREGARASIGNATURA);
        this.dni = dni;
        this.idCarrera = idCarrera;
        this.idAsignatura = idAsignatura;
    }

    public ComandoAgregarAsignatura(DataInputStream dis){
        super(TAGREGARASIGNATURA);
        this.dni = readString(dis);
        byte[] idCarreraBytes = readBytes(dis);
        this.idCarrera = Packer.unpackInt(idCarreraBytes);
        byte[] idAsignaturaBytes = readBytes(dis);
        this.idAsignatura = Packer.unpackLong(idAsignaturaBytes);
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public long getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(long idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public void sendTo(DataOutputStream dos){
        synchronized(dos){
            super.sendTo(dos);
            try{
                byte[] dniBytes = this.dni.getBytes("UTF-8");
                byte[] idCarreraBytes = Packer.packInt(this.idCarrera);
                byte[] idAsignaturaBytes = Packer.packLong(this.idAsignatura);
                writeBytes(dos, dniBytes);
                writeBytes(dos, idCarreraBytes);
                writeBytes(dos, idAsignaturaBytes);
                dos.flush();
            }catch(Exception e){
                throw new RuntimeException("TAGREGARASIGNATURA send: " + e);
            }
        }
    }
}

