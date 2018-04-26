package group33.seg.controller.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class providing static methods for serialising and deserialising of objects.
 */
public class SerializationUtils {

  /**
   * Do not allow this class to be instantiated.
   */
  private SerializationUtils() {}

  /**
   * Serialise and then deserialise an object to create a deep clone. This method is slow but is
   * provided as an alternative to external libraries.
   *
   * @param obj Object to clone
   * @return Clone of object
   */
  public static Object deepClone(Object obj) {
    // Serialize into bytes
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    serialize(obj, baos);
    // Deserialize bytes into object
    return deserialize(new ByteArrayInputStream(baos.toByteArray()));
  }

  /**
   * Serialse an object to an output stream.
   *
   * @param obj Object to serialise
   * @param os Output stream to target
   */
  public static void serialize(Object obj, OutputStream os) {
    // Verify arguments
    if (os == null) {
      throw new IllegalArgumentException("The OutputStream cannot be null");
    }
    // Handle output
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(os);
      oos.writeObject(obj);
    } catch (IOException e) {
      System.err.println("Serialization failed");
    } finally {
      try {
        // Close the stream
        if (oos != null) {
          oos.flush();
          oos.close();
        }
      } catch (IOException ex) {
        // close failed, ignore
      }
    }
  }

  /**
   * Deserialise an object from an input stream.
   *
   * @param is Input stream to get object from
   * @return Object from input stream, returns null on error
   */
  public static Object deserialize(InputStream is) {
    // Verify arguments
    if (is == null) {
      throw new IllegalArgumentException("The InputStream cannot be null");
    }
    // Handle input
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(is);
      return ois.readObject();
    } catch (ClassNotFoundException | IOException e) {
      System.err.println("Deserialization failed");
      return null;
    } finally {
      try {
        // Close the stream
        if (ois != null) {
          ois.close();
        }
      } catch (IOException e) {
        // close failed, ignore
      }
    }
  }

  private static Cipher makeCipher(char[] password, int opmode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    SecretKey key64 = new SecretKeySpec(new String(password).getBytes(), "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(opmode, key64);
    return cipher;
  }

  private static Cipher makeEncryptCipher(char[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    return makeCipher(password, Cipher.ENCRYPT_MODE);
  }

  private static Cipher makeDecryptCipher(char[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    return makeCipher(password, Cipher.DECRYPT_MODE);
  }

  public static void serializeEncrypted(Serializable obj, OutputStream os, char[] password){
    serializeEncrypted(obj, os, password, true);
  }

  public static void serializeEncrypted(Serializable obj, OutputStream os, char[] password, boolean clearPasswordAfter) {
    // Verify arguments
    if (os == null) {
      throw new IllegalArgumentException("The OutputStream cannot be null");
    }
    // Handle output
    ObjectOutputStream oos = null;
    try {
      Cipher cipher = makeEncryptCipher(password);

      //clear password
      if(clearPasswordAfter){
        for(int i=0; i<password.length; i++){
          password[i] = '0';
        }
      }

      SealedObject sealedObject = new SealedObject(obj, cipher);
      CipherOutputStream cipherOutputStream = new CipherOutputStream(new BufferedOutputStream(os), cipher);

      oos = new ObjectOutputStream(cipherOutputStream);
      oos.writeObject(sealedObject);
    } catch (InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
      System.err.println("Serialization failed");
    } finally {
      try {
        // Close the stream
        if (oos != null) {
          oos.flush();
          oos.close();
        }
      } catch (IOException ex) {
        // close failed, ignore
      }
    }
  }

  public static Object deserializeEncrypted(InputStream is, char[] password){
    return deserializeEncrypted(is, password, true);
  }

  public static Object deserializeEncrypted(InputStream is, char[] password,  boolean clearPasswordAfter){
    try {
      Cipher cipher = makeDecryptCipher(password);

      //clear password
      if(clearPasswordAfter){
        for(int i=0; i<password.length; i++){
          password[i] = '0';
        }
      }
      
      CipherInputStream cipherInputStream = new CipherInputStream( new BufferedInputStream(is), cipher );
      ObjectInputStream inputStream = new ObjectInputStream( cipherInputStream );
      
      SealedObject sealedObject = (SealedObject) inputStream.readObject();
      Object obj = sealedObject.getObject( cipher );

      inputStream.close();
      return obj;

    } catch(NoSuchPaddingException | BadPaddingException |
            NoSuchAlgorithmException | InvalidKeyException |
            IOException | ClassNotFoundException | IllegalBlockSizeException e) {
      e.printStackTrace();
      return null;
    }
  }
}
