package encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptionUtil {

	private static byte[] int2bytearray(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (i >> 24);
		bytes[1] = (byte) (i >> 16);
		bytes[2] = (byte) (i >> 8);
		bytes[3] = (byte) i;
		return bytes;
	}

	private static int bytearray2int(byte[] b) {
		int i = ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16) | ((b[2] & 0xFF) << 8) | (b[3] & 0xFF);
		return i;
	}

	private static Cipher encrypter = null;
	private static Cipher decrypter = null;
	private static ENCRYPTION_MODE eMode = null;

	public static byte[] encrypt(PublicKey publicKey, int value) throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, NoSuchProviderException {
		if (publicKey == null) {
			return int2bytearray(value);
		} else {
			if (encrypter == null) {
				if (eMode == ENCRYPTION_MODE.ECIES) {
					encrypter = Cipher.getInstance("ECIES", "BC");
				} else if (eMode == ENCRYPTION_MODE.RSA) {
					encrypter = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
				}
				encrypter.init(Cipher.ENCRYPT_MODE, publicKey);
			}

			byte[] buffer = int2bytearray(value);
			byte[] encrypted = encrypter.doFinal(buffer);
			return encrypted;
		}
	}

	public static int decrypt(PrivateKey privateKey, byte encrypted[])
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchProviderException {
		if (privateKey == null) {
			return bytearray2int(encrypted);
		} else {
			if (decrypter == null) {
				if (eMode == ENCRYPTION_MODE.ECIES) {
					decrypter = Cipher.getInstance("ECIES", "BC");
				} else if (eMode == ENCRYPTION_MODE.RSA) {
					decrypter = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
				}
				decrypter.init(Cipher.DECRYPT_MODE, privateKey);
			}
			byte[] decrypted = decrypter.doFinal(encrypted);
			int value = bytearray2int(decrypted);
			return value;
		}
	}

	public static KeyPair getKeyPair(ENCRYPTION_MODE eMode)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
		EncryptionUtil.eMode = eMode;
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(eMode.toString(), "BC");
		if (eMode == ENCRYPTION_MODE.RSA) {
			keyGen.initialize(2048);
		} else if (eMode == ENCRYPTION_MODE.ECIES) {
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
			keyGen.initialize(ecSpec, new SecureRandom());
		}

		KeyPair keyPair = keyGen.generateKeyPair();

		return keyPair;
	}
}
