package mx.com.beo.local;

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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import mx.com.beo.App;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class BanxicoTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(BanxicoTest.class);

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@SuppressWarnings("unchecked")
	@Test
	public void detalleFondos() throws Exception {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("fechaOperacion", 20170912);
		body.put("criterioConsulta", "R");
		body.put("valorConsulta", "4310163");
		body.put("claveSpeiBancoEmisorPago", 40132);
		body.put("claveSpeiBancoReceptorPago", 40002);
		body.put("numeroCuentaBeneficiario", "002528095301162365");
		body.put("montoOperacion", "1.00");
		
		HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);
		LOGGER.info("Cuerpo que se arma " + entity.getBody());
		System.out.println("Cabeceras que se arman" + entity.getHeaders());
		
		ResponseEntity<Object> response = restTemplate.exchange(createURLWithPort("/obtenerURL"), HttpMethod.POST, entity, Object.class);
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("responseStatus", 200);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		LOGGER.info("Cabacera del servicio cosumido" + response.getHeaders());
		LOGGER.info("Cuerpo del servicio cosumido" + response.getBody());
		mapBody = (Map<String, Object>) response.getBody();
		assertEquals(respuesta.get("responseStatus"), mapBody.get("responseStatus"));
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
