package com.example.asistencia;


import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by alberto on 28/3/18.
 */
public class Mensajes {

    public static final byte RERROR = 0;
    public static final byte ROK = 1;
    public static final byte TAGREGARPERSONA = 2;
    public static final byte TLISTARCARRERAS = 4;
    public static final byte RLISTARCARRERAS = 5;
    public static final byte TLISTARASIGNATURAS = 8;
    public static final byte RLISTARASIGNATURAS = 9;
    public static final byte TAGREGARASIGNATURA = 10;
    public static final byte TLISTARMISCARRERAS = 12;
    public static final byte RLISTARMISCARRERAS = 13;
    public static final byte TLISTARMISASIGNATURAS = 14;
    public static final byte RLISTARMISASIGNATURAS = 15;
    public static final byte TLOGIN =16;
    public static final byte RLOGIN = 17;
    public static final byte TCREARCLASE = 18;
    public static final byte RCREARCLASE = 19;
    public static final byte TAGREGARCLASE = 20;
    public static final byte RAGREGARCLASE = 21;
    public static final byte TPING = 99;

    private byte kind;

    protected static final int LENGTH_SIZE = 4;

    public Mensajes(byte kind){
        this.kind = kind;
    }

    public static String kindName(byte kind){
        switch(kind){
            case RERROR:
                return "rerror";
            case ROK:
                return "rok";
            case TLISTARCARRERAS:
                return "tlistarcarreras";
            case RLISTARCARRERAS:
                return "rlistarcarreras";
            case TLISTARASIGNATURAS:
                return "tlistarasignaturas";
            case RLISTARASIGNATURAS:
                return "rlistarasignaturas";
            case TLOGIN:
                return "tlogin";
            case RLOGIN:
                return "rlogin";
            case TAGREGARASIGNATURA:
                return "tlistarasignatura";
            case TLISTARMISCARRERAS:
                return "tlistarmiscarreras";
            case RLISTARMISCARRERAS:
                return "rlistarmiscarreras";
            case TLISTARMISASIGNATURAS:
                return "tlistarmisasignaturas";
            case TCREARCLASE:
                return "tcrearclase";
            case RCREARCLASE:
                return "rcrearclase";
            case TAGREGARCLASE:
                return "tagregarclase";
            case RAGREGARCLASE:
                return "ragregarclase";
            case TPING:
                return "tping";
            default:
                throw new RuntimeException("kindname: bad message kind");
        }
    }

    public String toString(){
        return String.format("[%s]", kindName(kind));
    }

    protected void writeBytes(DataOutputStream dos, byte[] bytes){
        try{
            int n = bytes.length;
            byte[] nBytes = Packer.packInt(n);
            dos.write(nBytes);
            if(n > 0){
                dos.write(bytes);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    protected byte[] readBytes(DataInputStream dis){
        try{
            byte[] nBytes = new byte[LENGTH_SIZE];
            int bytesRead = dis.read(nBytes);
            if(bytesRead == LENGTH_SIZE){
                int n = Packer.unpackInt(nBytes);
                if(n > 8 * 1024 * 1024){
                    throw new RuntimeException("readbytes: message is > 8MB");
                }
                byte[] bytes = new byte[n];
                if(n > 0){
                    dis.readFully(bytes);
                }
                return bytes;
            }else{
                throw new RuntimeException("readbytes: invalid bytes length");
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    protected void writeString(DataOutputStream dos, String string){
        try{
            byte[] bytes = string.getBytes("UTF-8");
            writeBytes(dos, bytes);
        }catch(Exception e){
            throw new RuntimeException("writestring: UTF-8 not supported");
        }
    }

    protected String readString(DataInputStream dis){
        try{
            byte[] bytes = readBytes(dis);
            return new String(bytes, "UTF-8");
        }catch(Exception e){
            throw new RuntimeException("readstring: UTF-8 not supported");
        }
    }

    public void sendTo(DataOutputStream out){
        try{
            out.writeByte(kind);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}

