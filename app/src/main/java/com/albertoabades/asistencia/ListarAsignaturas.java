package com.example.asistencia;

/**
 * Created by alberto on 17/4/18.
 */
public class ListarAsignaturas {

    private static final int RAW_INT_LENGTH = 4;
    private static final int RAW_LONG_LENGTH = 8;

    private long idAsignatura;
    private String nombreAsignatura;
    private long idCarrera;

    public ListarAsignaturas(long idAsignatura, String nombreAsignatura, long idCarrera){
        this.idAsignatura = idAsignatura;
        this.nombreAsignatura = nombreAsignatura;
        this.idCarrera = idCarrera;
    }

    public long getIdAsignatura(){
        return this.idAsignatura;
    }

    public void setIdAsignatura(long idAsignatura){
        this.idAsignatura = idAsignatura;
    }

    public String getNombreAsignatura(){
        return this.nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura){
        this.nombreAsignatura = nombreAsignatura;
    }

    public long getIdCarrera(){
        return this.idCarrera;
    }

    public void setIdCarrera(long idCarrera){
        this.idCarrera = idCarrera;
    }

    public String toString(){
        return Long.toString(this.idAsignatura) + "|" + this.nombreAsignatura + "|" + Long.toString(this.idCarrera);
    }

    public byte[] toRaw(){
        try{
            int length = 0;
            byte[] idAsignaturaToRaw = Packer.packLong(this.idAsignatura);
            length += idAsignaturaToRaw.length;
            byte[] nombreAsignaturaToRaw = this.nombreAsignatura.getBytes("UTF-8");
            byte[] nombreAsignaturaToRawLength = Packer.packInt(nombreAsignaturaToRaw.length);
            length += nombreAsignaturaToRaw.length + nombreAsignaturaToRawLength.length;
            byte[] idCarreratoRaw = Packer.packLong(this.idCarrera);
            length += idCarreratoRaw.length;
            byte[] totalInfoRaw = new byte[length];
            int index = 0;
            System.arraycopy(idAsignaturaToRaw, 0, totalInfoRaw, index, idAsignaturaToRaw.length);
            index += idAsignaturaToRaw.length;
            System.arraycopy(nombreAsignaturaToRawLength, 0, totalInfoRaw, index, nombreAsignaturaToRawLength.length);
            index += nombreAsignaturaToRawLength.length;
            System.arraycopy(nombreAsignaturaToRaw, 0, totalInfoRaw, index, nombreAsignaturaToRaw.length);
            index += nombreAsignaturaToRaw.length;
            System.arraycopy(idCarreratoRaw, 0, totalInfoRaw, index, idCarreratoRaw.length);
            return totalInfoRaw;
        }catch(Exception e){
            throw new RuntimeException("getrawdata: UTF-8 not supported");
        }
    }

    public static ListarAsignaturas fromRaw(byte[] rawData){
        try{
            int index = 0;
            byte[] idAsignaturaRaw = new byte[RAW_LONG_LENGTH];
            System.arraycopy(rawData, index, idAsignaturaRaw, 0, idAsignaturaRaw.length);
            index += idAsignaturaRaw.length;
            long idAsignatura = Packer.unpackLong(idAsignaturaRaw);
            byte[] nombreAsignaturaRawLength = new byte[RAW_INT_LENGTH];
            System.arraycopy(rawData, index, nombreAsignaturaRawLength, 0, nombreAsignaturaRawLength.length);
            index += nombreAsignaturaRawLength.length;
            int nombreAsignaturaLength = Packer.unpackInt(nombreAsignaturaRawLength);
            byte[] nombreAsignaturaRaw = new byte[nombreAsignaturaLength];
            System.arraycopy(rawData, index, nombreAsignaturaRaw, 0, nombreAsignaturaRaw.length);
            index += nombreAsignaturaRaw.length;
            String nombreAsignatura = new String(nombreAsignaturaRaw, "UTF-8");
            byte[] idCarreraRaw =  new byte[RAW_LONG_LENGTH];
            System.arraycopy(rawData, index, idCarreraRaw, 0, idCarreraRaw.length);
            index += idCarreraRaw.length;
            long idCarrera = Packer.unpackLong(idCarreraRaw);
            return new ListarAsignaturas(idAsignatura, nombreAsignatura, idCarrera);
        }catch(Exception e){
            throw new RuntimeException("setrawdata: UTF-8 not supported");
        }
    }
}

