package org.spongycastle.crypto.tls;

import java.io.IOException;
import org.spongycastle.crypto.modes.AEADBlockCipher;
import org.spongycastle.crypto.params.AEADParameters;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.util.Arrays;












public class TlsAEADCipher
  implements TlsCipher
{
  public static final int NONCE_RFC5288 = 1;
  static final int NONCE_DRAFT_CHACHA20_POLY1305 = 2;
  protected TlsContext context;
  protected int macSize;
  protected int record_iv_length;
  protected AEADBlockCipher encryptCipher;
  protected AEADBlockCipher decryptCipher;
  protected byte[] encryptImplicitNonce;
  protected byte[] decryptImplicitNonce;
  protected int nonceMode;
  
  public TlsAEADCipher(TlsContext context, AEADBlockCipher clientWriteCipher, AEADBlockCipher serverWriteCipher, int cipherKeySize, int macSize)
    throws IOException
  {
    this(context, clientWriteCipher, serverWriteCipher, cipherKeySize, macSize, 1);
  }
  
  TlsAEADCipher(TlsContext context, AEADBlockCipher clientWriteCipher, AEADBlockCipher serverWriteCipher, int cipherKeySize, int macSize, int nonceMode)
    throws IOException
  {
    if (!TlsUtils.isTLSv12(context))
    {
      throw new TlsFatalAlert((short)80);
    }
    
    this.nonceMode = nonceMode;
    



    switch (nonceMode)
    {
    case 1: 
      int fixed_iv_length = 4;
      record_iv_length = 8;
      break;
    case 2: 
      int fixed_iv_length = 12;
      record_iv_length = 0;
      break;
    default: 
      throw new TlsFatalAlert((short)80);
    }
    int fixed_iv_length;
    this.context = context;
    this.macSize = macSize;
    
    int key_block_size = 2 * cipherKeySize + 2 * fixed_iv_length;
    
    byte[] key_block = TlsUtils.calculateKeyBlock(context, key_block_size);
    
    int offset = 0;
    
    KeyParameter client_write_key = new KeyParameter(key_block, offset, cipherKeySize);
    offset += cipherKeySize;
    KeyParameter server_write_key = new KeyParameter(key_block, offset, cipherKeySize);
    offset += cipherKeySize;
    byte[] client_write_IV = Arrays.copyOfRange(key_block, offset, offset + fixed_iv_length);
    offset += fixed_iv_length;
    byte[] server_write_IV = Arrays.copyOfRange(key_block, offset, offset + fixed_iv_length);
    offset += fixed_iv_length;
    
    if (offset != key_block_size)
    {
      throw new TlsFatalAlert((short)80); }
    KeyParameter decryptKey;
    KeyParameter encryptKey;
    KeyParameter decryptKey;
    if (context.isServer())
    {
      encryptCipher = serverWriteCipher;
      decryptCipher = clientWriteCipher;
      encryptImplicitNonce = server_write_IV;
      decryptImplicitNonce = client_write_IV;
      KeyParameter encryptKey = server_write_key;
      decryptKey = client_write_key;
    }
    else
    {
      encryptCipher = clientWriteCipher;
      decryptCipher = serverWriteCipher;
      encryptImplicitNonce = client_write_IV;
      decryptImplicitNonce = server_write_IV;
      encryptKey = client_write_key;
      decryptKey = server_write_key;
    }
    
    byte[] dummyNonce = new byte[fixed_iv_length + record_iv_length];
    
    encryptCipher.init(true, new AEADParameters(encryptKey, 8 * macSize, dummyNonce));
    decryptCipher.init(false, new AEADParameters(decryptKey, 8 * macSize, dummyNonce));
  }
  

  public int getPlaintextLimit(int ciphertextLimit)
  {
    return ciphertextLimit - macSize - record_iv_length;
  }
  
  public byte[] encodePlaintext(long seqNo, short type, byte[] plaintext, int offset, int len)
    throws IOException
  {
    byte[] nonce = new byte[encryptImplicitNonce.length + record_iv_length];
    
    switch (nonceMode)
    {
    case 1: 
      System.arraycopy(encryptImplicitNonce, 0, nonce, 0, encryptImplicitNonce.length);
      
      TlsUtils.writeUint64(seqNo, nonce, encryptImplicitNonce.length);
      break;
    case 2: 
      TlsUtils.writeUint64(seqNo, nonce, nonce.length - 8);
      for (int i = 0; i < encryptImplicitNonce.length; i++)
      {
        int tmp103_101 = i; byte[] tmp103_99 = nonce;tmp103_99[tmp103_101] = ((byte)(tmp103_99[tmp103_101] ^ encryptImplicitNonce[i]));
      }
      break;
    default: 
      throw new TlsFatalAlert((short)80);
    }
    
    int plaintextOffset = offset;
    int plaintextLength = len;
    int ciphertextLength = encryptCipher.getOutputSize(plaintextLength);
    
    byte[] output = new byte[record_iv_length + ciphertextLength];
    if (record_iv_length != 0)
    {
      System.arraycopy(nonce, nonce.length - record_iv_length, output, 0, record_iv_length);
    }
    int outputPos = record_iv_length;
    
    byte[] additionalData = getAdditionalData(seqNo, type, plaintextLength);
    AEADParameters parameters = new AEADParameters(null, 8 * macSize, nonce, additionalData);
    
    try
    {
      encryptCipher.init(true, parameters);
      outputPos += encryptCipher.processBytes(plaintext, plaintextOffset, plaintextLength, output, outputPos);
      outputPos += encryptCipher.doFinal(output, outputPos);
    }
    catch (Exception e)
    {
      throw new TlsFatalAlert((short)80, e);
    }
    
    if (outputPos != output.length)
    {

      throw new TlsFatalAlert((short)80);
    }
    
    return output;
  }
  
  public byte[] decodeCiphertext(long seqNo, short type, byte[] ciphertext, int offset, int len)
    throws IOException
  {
    if (getPlaintextLimit(len) < 0)
    {
      throw new TlsFatalAlert((short)50);
    }
    
    byte[] nonce = new byte[decryptImplicitNonce.length + record_iv_length];
    
    switch (nonceMode)
    {
    case 1: 
      System.arraycopy(decryptImplicitNonce, 0, nonce, 0, decryptImplicitNonce.length);
      System.arraycopy(ciphertext, offset, nonce, nonce.length - record_iv_length, record_iv_length);
      break;
    case 2: 
      TlsUtils.writeUint64(seqNo, nonce, nonce.length - 8);
      for (int i = 0; i < decryptImplicitNonce.length; i++)
      {
        int tmp133_131 = i; byte[] tmp133_129 = nonce;tmp133_129[tmp133_131] = ((byte)(tmp133_129[tmp133_131] ^ decryptImplicitNonce[i]));
      }
      break;
    default: 
      throw new TlsFatalAlert((short)80);
    }
    
    int ciphertextOffset = offset + record_iv_length;
    int ciphertextLength = len - record_iv_length;
    int plaintextLength = decryptCipher.getOutputSize(ciphertextLength);
    
    byte[] output = new byte[plaintextLength];
    int outputPos = 0;
    
    byte[] additionalData = getAdditionalData(seqNo, type, plaintextLength);
    AEADParameters parameters = new AEADParameters(null, 8 * macSize, nonce, additionalData);
    
    try
    {
      decryptCipher.init(false, parameters);
      outputPos += decryptCipher.processBytes(ciphertext, ciphertextOffset, ciphertextLength, output, outputPos);
      outputPos += decryptCipher.doFinal(output, outputPos);
    }
    catch (Exception e)
    {
      throw new TlsFatalAlert((short)20, e);
    }
    
    if (outputPos != output.length)
    {

      throw new TlsFatalAlert((short)80);
    }
    
    return output;
  }
  





  protected byte[] getAdditionalData(long seqNo, short type, int len)
    throws IOException
  {
    byte[] additional_data = new byte[13];
    TlsUtils.writeUint64(seqNo, additional_data, 0);
    TlsUtils.writeUint8(type, additional_data, 8);
    TlsUtils.writeVersion(context.getServerVersion(), additional_data, 9);
    TlsUtils.writeUint16(len, additional_data, 11);
    
    return additional_data;
  }
}
