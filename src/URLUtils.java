import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Classe com métodos utilitários para tratar URLs
 */
public class URLUtils {
    // codificação de caracteres utilizada para montar as URLs
    private static String charset = "UTF-8";

    /**
     * Monta o trecho da URL com os parâmetros de uma requisição no formato "&param=valor" para cada par chave-valor passado.
     * 
     * @param parametros Pares que representam cada parâmetro e seu valor
     * 
     * @return Trecho da URL no formato descrito
     * @throws UnsupportedEncodingException
     */
    public static String montaStringParametros(Map<String, String> parametros) throws UnsupportedEncodingException {
        String stringParametros = "";
        // Acrescenta os parâmetros na string de consulta
        for (String param : parametros.keySet()) {                
            stringParametros += "&" + param + String.format("=%s", URLEncoder.encode(parametros.get(param), charset));
        }
        return stringParametros;
    }
}
