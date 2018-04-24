package group33.seg.controller.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

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

}
