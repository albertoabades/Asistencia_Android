package com.example.asistencia;

/**
 * Created by alberto on 5/4/18.
 */
public class ListarCarreras {

    private static final int RAW_INT_LENGTH = 4;
    private static final int RAW_LONG_LENGTH = 8;

    private long idCarrera;
    private String nombreCarrera;

    public ListarCarreras(long idCarrera, String nombreCarrera){
        this.idCarrera = idCarrera;
        this.nombreCarrera = nombreCarrera;
    }

    public long getIdCarrera(){
        return idCarrera;
    }

    public void setIdCarrera(long idCarrera){
        this.idCarrera = idCarrera;
    }

    public String getNombreCarrera(){
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera){
        this.nombreCarrera = nombreCarrera;
    }

    public String toString(){
        return Long.toString(this.idCarrera) + "|" + this.nombreCarrera;
    }

    public byte[] toRaw(){
        try{
            int length = 0;
            byte[] idCarreraToRaw = Packer.packLong(this.idCarrera);
            length += idCarreraToRaw.length;
            byte[] nombreCarreraToRaw = this.nombreCarrera.getBytes("UTF-8");
            byte[] nombreCarreraToRawLength = Packer.packInt(nombreCarreraToRaw.length);
            length += nombreCarreraToRaw.length + nombreCarreraToRawLength.length;
            byte[] totalInfoRaw = new byte[length];
            int index= 0;
            System.arraycopy(idCarreraToRaw, 0, totalInfoRaw, index, idCarreraToRaw.length);
            index += idCarreraToRaw.length;
            System.arraycopy(nombreCarreraToRawLength, 0, totalInfoRaw, index, nombreCarreraToRawLength.length);
            index += nombreCarreraToRawLength.length;
            System.arraycopy(nombreCarreraToRaw, 0, totalInfoRaw, index, nombreCarreraToRaw.length);
            index += nombreCarreraToRaw.length;
            return totalInfoRaw;
        }catch(Exception e){
            throw new RuntimeException("getrawdata: UTF-8 not supported");
        }
    }

    public static ListarCarreras fromRaw(byte[] rawData){
        try{
            int index = 0;
            byte[] idCarreraRaw = new byte[RAW_LONG_LENGTH];
            System.arraycopy(rawData, index, idCarreraRaw, 0, idCarreraRaw.length);
            index += idCarreraRaw.length;
            long idCarrera = Packer.unpackLong(idCarreraRaw);
            byte[] nombreCarreraRawLength = new byte[RAW_INT_LENGTH];
            System.arraycopy(rawData, index, nombreCarreraRawLength, 0, nombreCarreraRawLength.length);
            index += nombreCarreraRawLength.length;
            int nombreCarreraLength = Packer.unpackInt(nombreCarreraRawLength);
            byte[] nombreCarreraRaw = new byte[nombreCarreraLength];
            System.arraycopy(rawData, index, nombreCarreraRaw, 0, nombreCarreraRaw.length);
            index += nombreCarreraRaw.length;
            String nombreCarrera = new String(nombreCarreraRaw, "UTF-8");
            return new ListarCarreras(idCarrera, nombreCarrera);
        }catch(Exception e){
            throw new RuntimeException("setdataraw: UTF-8 not supported");
        }
    }
}
