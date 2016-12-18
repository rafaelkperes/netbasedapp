import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Client {
	private static Cipher aes = null;

	private static final String PUBLIC_B_KEY_PATH = "key_b.public";

	private static SecretKey secretKey = null;

	public static void main(String[] args)
			throws UnknownHostException, IOException, ClassNotFoundException, GeneralSecurityException {

		secretKey = KeyGenerator.getInstance("AES").generateKey();

		System.out.print("Client started...");

		sendAESKey();
		sendVideo("video.mp4");

	}

	private static void sendAESKey() throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
		// create new key
		Socket server = new Socket("localhost", 9999);
		String originalText = "Text to be encrypted ";
		ObjectInputStream inputStream = null;

		// get base64 encoded version of the key
		String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		originalText = encodedKey;
		// System.out.println(originalText);
		// Encrypt the string using the public key
		inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_B_KEY_PATH));
		final PublicKey publicKey = (PublicKey) inputStream.readObject();
		final byte[] cipherText = encrypt(originalText, publicKey);
		FileOutputStream fos = new FileOutputStream("AESKey");
		fos.write(cipherText);
		fos.close();

		File file = new File("AESKey");
		InputStream in = new FileInputStream(file);
		OutputStream outt = server.getOutputStream();

		copy(in, outt);
		outt.close();
		in.close();
		server.close();
		inputStream.close();
	}

	private static void sendVideo(String fileType) {
		try {

			Socket server = new Socket("localhost", 9999);
			File videoFile = new File(fileType);
			InputStream in = new FileInputStream(videoFile);
			OutputStream out = server.getOutputStream();

			aes = Cipher.getInstance("AES");

			aes.init(Cipher.ENCRYPT_MODE, secretKey);
			CipherOutputStream os = new CipherOutputStream(out, aes);

			copy(in, os);
			out.close();
			in.close();
			server.close();

		} catch (NoSuchPaddingException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	public static byte[] encrypt(String text, PublicKey key) {
		byte[] cipherText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA");
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	private static void copy(InputStream is, OutputStream os) throws IOException {
		int i;
		byte[] b = new byte[16 * 1024];
		while ((i = is.read(b)) != -1) {
			os.write(b, 0, i);
		}
	}
}
