import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Server {
	private static Cipher aes = null;
	private static final String PRIVAT_B_KEY_PATH = "key_b.privat";
	private static SecretKey secretKey = null;

	private static ServerSocket server = null;

	public static void main(String[] args) throws IOException, ClassNotFoundException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		server = new ServerSocket(9999);
		System.out.println("Server listening on port -> 9999 ...");

		receiveAESKey();
		getAESKey();
		receiveVideo("video.mp4");

		server.close();
	}

	private static void receiveVideo(String fileType) {
		Socket client;
		try {
			client = server.accept();

			InputStream in = client.getInputStream();
			OutputStream out = new FileOutputStream(fileType);

			aes = Cipher.getInstance("AES");
			aes.init(Cipher.DECRYPT_MODE, secretKey);
			CipherInputStream isDe = new CipherInputStream(in, aes);

			copy(isDe, out);
			System.out.println("Video received over a secure channel.");
			out.close();
			in.close();
			client.close();

		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	private static void receiveAESKey() throws IOException, ClassNotFoundException {
		Socket client = server.accept();
		InputStream in = client.getInputStream();
		OutputStream out = new FileOutputStream("AESKey");
		copy(in, out);
		out.close();
		in.close();
		client.close();
	}

	public static String decrypt(byte[] text, PrivateKey key) {
		byte[] dectyptedText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA");

			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new String(dectyptedText);
	}

	private static void getAESKey() throws FileNotFoundException, IOException, ClassNotFoundException {
		Path path = Paths.get("AESKey");
		byte[] cipherText = Files.readAllBytes(path);

		// Decrypt the cipher text using the private key.
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PRIVAT_B_KEY_PATH));
		final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		final String plainText = decrypt(cipherText, privateKey);

		// System.out.println(plainText);
		String encodedKey = plainText;
		// decode the base64 encoded string
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		// rebuild key using SecretKeySpec
		secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		inputStream.close();
	}

	private static void copy(InputStream is, OutputStream os) throws IOException {
		int i;
		byte[] b = new byte[16 * 1024];
		while ((i = is.read(b)) != -1) {
			os.write(b, 0, i);
		}
	}
}
