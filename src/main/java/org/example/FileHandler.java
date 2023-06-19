package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileHandler {
    private File file;
    private byte[] fileDataFirstHalf;
    private byte[] fileDataSecondHalf;
    public FileHandler(File file){
        this.file = file;
        splitFile();
    }

    public void splitFile(){
        try(FileInputStream sourceInputStream = new FileInputStream(file)) {

            fileDataFirstHalf = sourceInputStream.readNBytes((int)file.length()/2);
            fileDataSecondHalf = sourceInputStream.readNBytes((int)Math.ceilDiv(file.length(),2));

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }


    public File getFile() {
        return file;
    }

    public byte[] getFileDataFirstHalf() {
        return fileDataFirstHalf;
    }

    public byte[] getFileDataSecondHalf() {
        return fileDataSecondHalf;
    }
}
