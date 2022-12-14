package br.ufla.gac106.JavaWikiAPI;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Classe com métodos utilitários para tratar URLs
 */
public class URLUtils {
    /**
     * Monta o trecho da URL com os parâmetros de uma requisição no formato "&param=valor" para cada par chave-valor passado.
     * 
     * @param parametros Pares que representam cada parâmetro e seu valor
     * 
     * @return Trecho da URL no formato descrito
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    public static String constroiURLRequisicao(String endpoint, Map<String, String> parametros) throws URISyntaxException, UnsupportedEncodingException, MalformedURLException {
        String stringParametros = endpoint + "?";
        for (String param : parametros.keySet()) {
            stringParametros += "&" + String.format("%s=%s", param, URLEncoder.encode(parametros.get(param), StandardCharsets.UTF_8));
        }
        return stringParametros; 
    }
}
