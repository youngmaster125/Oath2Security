package org.sid.Oath2Security;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class GenerateKey {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator =KeyPairGenerator.getInstance("RSA");
        var keyPair =keyPairGenerator.generateKeyPair();
        
      ///  KeyPair keyPair=keyPairGenerator.generateKeyPair();

        byte [] pub=keyPair.getPublic().getEncoded();
        byte [] pri=keyPair.getPublic().getEncoded();
        PemWriter pemWriter=new PemWriter(new OutputStreamWriter(new FileOutputStream("pub.pem")));
        PemObject pemObject =new PemObject("PUBLIC KEY",pub);
        pemWriter.writeObject(pemObject);
        pemWriter.close();

        PemWriter pemWriter2=new PemWriter(new OutputStreamWriter(new FileOutputStream("pri.pem")));
        PemObject pemObject2 =new PemObject("PRIVATE KEY",pri);
        pemWriter2.writeObject(pemObject2);
        System.out.print(true);
        pemWriter2.close();


    }
}
