package com.hjc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Command {

    String command;

    public Command(String command){
        this.command = command;
    }

    public String getCommand(){
        return command;
    }

    public Process exec() throws IOException, InterruptedException {

        return Runtime.getRuntime().exec(command);
    }

    public void exec(String tag) throws IOException, InterruptedException {
        Process p;
        try {
            p = exec();
        } catch (IOException e1) {
            e1.printStackTrace();

            return;
        }
        assert p != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String[] toUiaArray(){
        int length = command.split("\\s").length;
        String[] com = new String[length];
        String[] newStr = new String[2];
        int classPosition = 0;
        String frontStr = "";
        String backStr = "";
        for(int i=0; i<length; i++){
            com[i] = command.split("\\s")[i];
        }
        for(int i=0; i<length; i++){
            if(command.split("\\s")[i].startsWith("-c")){
                classPosition = i+1;
            }
        }
        for(int i=0; i<classPosition+1; i++){
            if(i == classPosition){
                frontStr += com[i] + "#";
            }
            else {
                frontStr += com[i] + " ";
            }
        }
        for(int i=classPosition+1; i<length; i++){
            backStr += com[i] + " ";
        }
        newStr[0] = frontStr;
        newStr[1] = backStr;
        return newStr;
    }

    public BufferedReader getExecuteBuffer() throws IOException, InterruptedException {
        Process p = exec();
        assert p != null;
        p.waitFor();
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }
}
