package mx.com.beo.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.google.common.io.BaseEncoding;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
 
/**
* Copyright (c)  2017 Nova Solution Systems S.A. de C.V.
* Mexico D.F.
* Todos los derechos reservados.
*
* @author Angel Martínez León
*
* ESTE SOFTWARE ES INFORMACIÓN CONFIDENCIAL. PROPIEDAD DE NOVA SOLUTION SYSTEMS.
* ESTA INFORMACIÓN NO DEBE SER DIVULGADA Y PUEDE SOLAMENTE SER UTILIZADA DE ACUERDO CON LOS TÉRMINOS DETERMINADOS POR LA EMPRESA SÍ MISMA.
*/
public class AESCBC128bits {

    // Definición del tipo de algoritmo a utilizar
    private final static String alg = "AES";
    // Definición del modo de cifrado a utilizar
    private final static String cI = "AES/CBC/PKCS5Padding";
 
    /**
     * Función que retorna un tipo String y que recibe una llave (key) y el texto que se desea cifrar.
     * @param key la llave en tipo String a utilizar.
     * @param iv el vector de inicialización a utilizar.
     * @param cleartext el texto sin cifrar a encriptar.
     * @return el texto cifrado en modo String.
     * @throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException.
     */
    public static String encrypt(String key, String cleartext) throws Exception {
  
    	// Se obtiene un array de bytes de la Key
    	byte[] p1_Key = new byte[16];
        byte[] p2_IV = new byte[16];
        
    	byte[] numArray =  decodeBase64(key);
    	
        int index1 = 0;
        int index2 = 0;
        
        // Con el array de bytes se geenra una nueva Key y el Vector a utilizar en el algoritmo de encriptacion
        for (byte num : numArray)
        {
          if (index1 < 16)
          {
            p1_Key[index1] = num;
          }
          else
          {
            p2_IV[index2] = num;
            ++index2;
          }
          ++index1;
        }
    
        // Se obtiene la instancia dle algoritmo AES CBC 128 bits por medio del Cipher
        Cipher cipher = Cipher.getInstance(cI);
        SecretKeySpec skeySpec = new SecretKeySpec(p1_Key, alg);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(p2_IV);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        
        return BaseEncoding.base64().encode(encrypted);
    }
 
    /**
     * Función que retorna un tipo String y que recibe una llave (key), un vector de inicialización (iv) y el texto que se desea descifrar.
     * @param key la llave en tipo String a utilizar.
     * @param iv el vector de inicialización a utilizar.
     * @param encrypted el texto cifrado en modo String.
     * @return el texto desencriptado en modo String.
     * @throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException.
     */
    
    public static String decrypt(String key, String iv, String desencrypted) throws Exception {
    	
            Cipher cipher = Cipher.getInstance(cI);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), alg);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            byte[] enc = decodeBase64(desencrypted);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(enc);
            return new String(decrypted);
            
    }

}