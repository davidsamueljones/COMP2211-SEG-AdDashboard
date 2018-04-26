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
        char[] encryptPassword = "testPassword".toCharArray();
        char[] decryptPassword = "testPassword".toCharArray();

        assertTrue("Encryption not working", readWriteTestEncrypted("correct.test", encryptPassword, decryptPassword));
    }

    @Test
    public void wrongPasswordTest() {
        char[] encryptPassword = "testPassword".toCharArray();
        char[] decryptPassword = "passwordTest".toCharArray();

        assertTrue("Wrong password accepted", !readWriteTestEncrypted("wrong.test", encryptPassword, decryptPassword));
    }

    private boolean readWriteTestEncrypted(String testLocation, char[] encryptPassword, char[] decryptPassword) {
        String testString = "testString";
        File testFile = new File(testLocation);

        try {
            //serialise object
            OutputStream os = new FileOutputStream(testFile);
            SerializationUtils.serializeEncrypted(testString, os, encryptPassword, false);
            os.close();

            //deserialise object
            InputStream is = new FileInputStream(testFile);
            Object readObject = SerializationUtils.deserializeEncrypted(is, decryptPassword);
            is.close();

            testFile.delete();

            //check if objects are equal
            if (readObject instanceof String) {
                String readString = (String) readObject;
                return readString.equals(testString);
            } else {
                return false;
            }
        } catch (IOException e) {
            testFile.delete();
            return false;
        }
    }
}