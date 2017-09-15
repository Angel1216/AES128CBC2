package mx.com.beo.api;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.beo.util.AESCBC128bits;
import mx.com.beo.util.Sanitizacion;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
public class AppControlador {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppControlador.class);

	/**
     * Servicio para encriptar la cadena con el estándar avanzanzado de encripción (AES).
     * @param fechaOperacion se envia por JSON en el body, ejemplo: 20170912.
     * @param criterioConsulta se envia por JSON en el body, ejemplo: "R".
     * @param valorConsulta se envia por JSON en el body, ejemplo: "4310163".
     * @param claveSpeiBancoEmisorPago se envia por JSON en el body, ejemplo: 40132.
     * @param claveSpeiBancoReceptorPago se envia por JSON en el body, ejemplo: 40002.
     * @param numeroCuentaBeneficiario se envia por JSON en el body, ejemplo: "002528095301162365".
     * @param montoOperacion se envia por JSON en el body, ejemplo: "1.00".
     * 
     * @return la cadena encriptada con AES y codificada en base64.
     * @throws Exception puede devolver cualquier tipo de excepción que se presente.
     */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/obtenerURL", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> detalleFondos(RequestEntity<Object> request) {
		
		LOGGER.info("EndPoint obtenerURL");
		
		// Variables
		Map<String, Object> responseError = new HashMap<String, Object>();
		Map<String, Object> mapBody = (Map<String, Object>) request.getBody();
		Map<String, Object> responseEncryption = new HashMap<String, Object>();
		
		String cadenaCifrada = "";
		String cadenaSanitizada = "";
		StringBuilder url= new StringBuilder();
		StringBuilder cadenaOriginal = new StringBuilder();
		Sanitizacion sanitizacion = new Sanitizacion(); 

		try {
			
			// Arma cadena
			cadenaOriginal.append(mapBody.get("fechaOperacion")).append("|").
			append(mapBody.get("criterioConsulta")).append("|").
			append(mapBody.get("valorConsulta")).append("|").
			append(mapBody.get("claveSpeiBancoEmisorPago")).append("|").
			append(mapBody.get("claveSpeiBancoReceptorPago")).append("|").
			append(mapBody.get("numeroCuentaBeneficiario")).append("|").
			append(mapBody.get("montoOperacion"));
						
			// Encriptar AES CBC 128 bits
			String key = "1M4lYza2hlrEEhoQv2xGMQ5v+wyeGUhCfiQsIqqGSdc=";
			cadenaCifrada = AESCBC128bits.encrypt(key, cadenaOriginal.toString());
			cadenaSanitizada = sanitizacion.sanitizacion(cadenaCifrada);
			
			url.append("https://www.banxico.org.mx/cep/go?i=").
				append(mapBody.get("claveSpeiBancoEmisorPago").toString()).
				append("&s=20150817&d=").
				append(cadenaSanitizada);
			
			LOGGER.debug("url sanitizada: " + url);
			
			responseEncryption.put("url", url);
			responseEncryption.put("responseStatus", 200);
			responseEncryption.put("responseError", "");
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			responseError = new HashMap<String, Object>();
			responseError.put("responseStatus", 500);
			responseError.put("responseError", exception.getMessage());
			return new ResponseEntity<>(responseError,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(responseEncryption,HttpStatus.OK);
		
	}
}