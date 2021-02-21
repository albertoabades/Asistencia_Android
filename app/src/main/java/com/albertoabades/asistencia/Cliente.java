package com.albertoabades.asistencia;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by alberto on 28/3/18.
 */
public class Cliente {

    private String host;
    private static final int PORT = 9090;
    private Comandos command;
    private Socket fd;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Cliente(String host, Comandos command){
        this.host = host;
        this.command = command;
    }

    public void start(){
        try{
            this.fd = new Socket(this.host, PORT);
            this.dis = new DataInputStream(new BufferedInputStream(this.fd.getInputStream()));
            this.dos = new DataOutputStream(new BufferedOutputStream(this.fd.getOutputStream()));
            this.command.sendTo(this.dos);
            Respuestas response = Respuestas.receiveFrom(this.dis);
            System.out.println(response);
        }catch(Exception e){
            throw new RuntimeException(this + ": " + e);
        }finally{
            close();
        }
    }

    private void close(){
        try{
            if(this.fd != null){
                this.fd.close();
            }
            if(this.dis != null){
                this.dis.close();
            }
            if(this.dos != null){
                this.dos.close();
            }
        }catch(Exception e){
            //OOOOOK!!!
        }
    }


}
