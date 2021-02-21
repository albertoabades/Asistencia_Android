package com.example.asistencia;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by alberto on 19/5/18.
 */
public class ComandoLogin extends Comandos{

    private static final int LENGTH = 3;
    private static final String STR_COMMAND = "LOGIN";

    public String dni;
    public String password;

    public ComandoLogin(String dni, String password){
        super(TLOGIN);
        this.dni = dni;
        this.password = password;
    }

    public ComandoLogin(DataInputStream dis){
        super(TLOGIN);
        this.dni = readString(dis);
        this.password = readString(dis);

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDni(){
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void sendTo(DataOutputStream dos){
        synchronized(dos){
            super.sendTo(dos);
            try{
                byte[] dniBytes = this.dni.getBytes("UTF-8");
                byte[] passwordBytes = this.password.getBytes("UTF-8");
                writeBytes(dos, dniBytes);
                writeBytes(dos, passwordBytes);
                dos.flush();
            }catch(Exception e){
                throw new RuntimeException("TLOGIN send: " + e);
            }
        }
    }
}
