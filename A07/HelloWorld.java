import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class HelloWorld {

	public static void main(String[] args) throws FileNotFoundException {

		File videoFile = new File("video.mp4");
		File videoFileEn = new File("encrypted_video.mp4");
		File videoFileDe = new File("dec_video.mp4");
		InputStream inOrig = new FileInputStream(videoFile);
		OutputStream outEn = new FileOutputStream(videoFileEn);
		InputStream inEn = new FileInputStream(videoFileEn);
		OutputStream outde = new FileOutputStream(videoFileDe);

		try {
//			final String PUBLIC_KEY_PATH = "key_a.public";
//			PublicKey key = null;
//			FileInputStream fis;
//			fis = new FileInputStream(PUBLIC_KEY_PATH);
//			ObjectInputStream in = new ObjectInputStream(fis);
//			key = (PublicKey) in.readObject();
//			in.close();
//			final String PRIVATE_KEY_PATH = "key_a.privat";
//			PrivateKey pri_key = null;
//			FileInputStream fis_pri;
//			fis = new FileInputStream(PRIVATE_KEY_PATH);
//			ObjectInputStream in_pri = new ObjectInputStream(fis);
//			pri_key = (PrivateKey) in_pri.readObject();
//			in.close();
//			Cipher rsa;
//			rsa = Cipher.getInstance("RSA");
//			rsa.init(Cipher.ENCRYPT_MODE, key);
//			CipherOutputStream os = new CipherOutputStream(outEn, rsa);
//			copy(inOrig, os);
//			os.close();
//			outEn.close();
//			rsa.init(Cipher.DECRYPT_MODE, pri_key);
//			CipherInputStream isDe = new CipherInputStream(inEn, rsa);
//			copy(isDe, outde);
//			
			
			
			
			
			
			
			
			
			
			
			
			
			KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
			SecretKey myAesKey = keygenerator.generateKey();
			Cipher aesCipher;
			aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.ENCRYPT_MODE, myAesKey);
			CipherOutputStream os = new CipherOutputStream(outEn, aesCipher);
			copy(inOrig, os);
			os.close();
			outEn.close();
			aesCipher.init(Cipher.DECRYPT_MODE, myAesKey);
			CipherInputStream isDe = new CipherInputStream(inEn, aesCipher);
			copy(isDe, outde);

		} catch (Exception e) {
			System.out.println("Exception");
		}
	}

	/**
	 * Copies a stream.
	 */
	private static void copy(InputStream is, OutputStream os) throws IOException {
		int i;
		byte[] b = new byte[1024];
		while ((i = is.read(b)) != -1) {
			os.write(b, 0, i);
		}
	}
}
