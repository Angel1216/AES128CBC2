package mx.com.beo.util;

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
public class Sanitizacion {
	
	/**
     * Función que retorna un tipo String y que recibe una cadena de texto para sanitizarla (reemplazar los caracteres =, / y + por %3D, %2F y %2B respectivamente).
     * @param cadena de texto a sanitizar.
     * @return texto sanitizado.
     * @throws Exception puede devolver cualquier tipo de excepción que se presente.
     */
	public String sanitizacion(String url) throws Exception{
		return url.replace("=", "%3D").replace("/", "%2F").replace("+", "%2B");
	}

}
