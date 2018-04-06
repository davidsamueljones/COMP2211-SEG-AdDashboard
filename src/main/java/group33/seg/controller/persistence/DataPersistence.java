package group33.seg.controller.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Data persistence layer for storage and retrieval of objects in files. Defines default location and file
 * extension of stored objects.
 */
public class DataPersistence {
	private static final String DIRECTORY = "storage";
	private static final String FILE_EXTENSION = "adw";

	/**
	 * Do not allow this class to be instantiated.
	 */
	public DataPersistence() {}

	/**
	 * Store the given object using the file determined from the identifier.
	 *
	 * @param identifier Identifier to use for storage
	 * @param object Object to store
	 * @throws FileNotFoundException Unable to open file location found using identifier
	 */
	public static void store(String identifier, Object object) throws FileNotFoundException {
		final File file = getFile(identifier);
		// Make directories if required
		file.getParentFile().mkdirs();
		// Create output stream
		final FileOutputStream fos = new FileOutputStream(file);
		SerializationUtils.serialize(object, fos);
		try {
			fos.close();
		} catch(IOException e) {
			// close failed, ignore
		}
	}

	/**
	 * Retrieve an object from the persistence layer with the given identifier.
	 *
	 * @param identifier Identifier to try and find stored object for
	 * @return Object loaded from file
	 * @throws FileNotFoundException Unable to open file location found using identifier
	 */
	public static Object get(String identifier) throws FileNotFoundException {
		// Open file input stream using identifiers respective file
		final FileInputStream fis = new FileInputStream(getFile(identifier));
		// Get object from file input stream
		final Object object = SerializationUtils.deserialize(fis);
		try {
			fis.close();
		} catch(IOException e) {
			// close failed, ignore
		}
		return object;
	}

	/**
	 * Determine the persistence path for an identifier.
	 *
	 * @param identifier Identifier to get path (File) for
	 * @return File that identifier should represent
	 */
	private static File getFile(String identifier) {
		return new File(String.format("%s/%s.%s", DIRECTORY, identifier, FILE_EXTENSION));
	}

}
