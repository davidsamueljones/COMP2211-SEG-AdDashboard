import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import group33.seg.controller.utilities.SerializationUtils;

public class SerializationTest {
    @Test
    public void plaintextSerialisationTest() {
        String testString = "testString";
        File testFile = new File("test.file");

        try {
            //serialise object
            OutputStream os = new FileOutputStream(testFile);
            SerializationUtils.serialize(testString, os);
            os.close();

            //deserialise object
            InputStream is = new FileInputStream(testFile);
            Object readObject = SerializationUtils.deserialize(is);
            is.close();

            testFile.delete();

            //check if objects are equal
            if (readObject instanceof String) {
                String readString = (String) readObject;
                assertTrue("Read object was not the same as the written object", readString.equals(testString));
            } else {
                fail("Read object was not the correct type");
            }
        } catch (IOException e) {
            testFile.delete();
            fail(e.getMessage());
        }
    }

    @Test
    public void encryptedSerialisationTest() {
        String testString = "testString";
        File testFile = new File("test.file");

        char[] examplePassword = "example".toCharArray();
        try {
            //serialise object
            OutputStream os = new FileOutputStream(testFile);
            SerializationUtils.serializeEncrypted(testString, os, examplePassword, false);
            os.close();

            //deserialise object
            InputStream is = new FileInputStream(testFile);
            Object readObject = SerializationUtils.deserializeEncrypted(is, examplePassword);
            is.close();

            testFile.delete();

            //check if objects are equal
            if (readObject instanceof String) {
                String readString = (String) readObject;
                assertTrue("Read object was not the same as the written object", readString.equals(testString));
            } else {
                fail("Read object was not the correct type");
            }
        } catch (IOException e) {
            testFile.delete();
            fail(e.getMessage());
        }
    }
}