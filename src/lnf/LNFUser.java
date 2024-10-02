package lnf;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import encryption.EncryptionUtil;

public class LNFUser {

	private int value;
	// If pk is null, the value will not be encrypted.
	private PublicKey pk;

	public LNFUser(int value, PublicKey pk) {
		this.value = value;
		this.pk = pk;
	}

	// For evaluation only
	public int getOriginalValue() {
		return value;
	}

	public byte[] getValue() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
		return EncryptionUtil.encrypt(pk, value);
	}

	public void setPoisonedValue(Set<Integer> targets) {
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(targets);
		int rand = (int) (Math.random() * targets.size());
		value = list.get(rand);
	}
}
