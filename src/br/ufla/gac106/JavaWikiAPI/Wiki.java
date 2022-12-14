package br.ufla.gac106.JavaWikiAPI;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Classe para obter dados de uma plataforma Wiki.
 * 
 * Pode ser a Wikipedia em qualquer idioma, ou qualquer outro site que utilize a plataforma Wiki
 */
public class Wiki implements Closeable {
    // domínio da Wiki a ser consultada
    private String dominio;
    // endereço completo da API a ser utilizada
    private String endpoint;
    // parâmetros padrões a serem utilizados em todas as consultas
    private Map<String, String> parametrosPadroes;
    // indica se está em modo de debug (nesse caso, são exibidas mensagens detalhadas do que é feito)
    private boolean debug;

    /**
     * Constrói o objeto capaz de obter dados da Wikipedia em Português (domínio "pt.wikipedia.org").
     * Lembre-se de usar o método terminar quando não for mais usar o objeto
     * 
     * Para obter dados de outras Wikis, utilize outro construtor.
     */
    public Wiki() {
        this("pt.wikipedia.org");
    }

    /**
     * Constrói o objeto capaz de obter dados de uma Wiki
     * 
     * @param dominio Domínio da Wikie a ser consultada (ex: "en.wikipedia.org"). Se não informado, usa "pt.wikipedia.org"
     */
    public Wiki(String dominio) {
        this.dominio = dominio;
        endpoint = "https://" + dominio + "/w/api.php";
        debug = false;

        definirParametrosPadroes();
    }

    /**
     * Define parâmetros padrões a serem utilizados em todas as consultas
     */
    private void definirParametrosPadroes() {
        parametrosPadroes = new HashMap<>();
        
        // Usaremos o módulo de consulta
        parametrosPadroes.put("action","query");

        // Aplica os redirecionamentos necessários até chegar na página correta
        parametrosPadroes.put("redirects","true");
        
        // Vamos obter resultados no formato JSON v2 (sem isso é retornado resultado HTML)
        parametrosPadroes.put("format","json");
        parametrosPadroes.put("formatversion","2");
    }

    /**
     * Domínio que está sendo utilizado nas consultas
     * 
     * @return Domínio da Wiki
     */
    public String getDominio() {
        return dominio;
    }

    /**
     * Permite ligar/desligar modo de debug
     * 
     * @param debug Estado do modo de debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;

        if (debug) System.out.println("=> Wiki em modo de debug (endpoint: " + endpoint + ")");
    }

    /**
     * Termina o uso das ferramentas internas
     */
    @Override
    public void close() throws IOException {
        // É necessário terminar o loop do Unirest para conseguir fechar o programa
        Unirest.shutdown();
    }

    /*
     * Retorna o resumo (texto antes da primeira seção) de uma página
     * 
     * @param titulo Título da página a ser buscada
     * 
     * @returns Objeto da página Wiki buscada (ou null se ela não for encontrada)
     */
    public PaginaWiki buscarResumoDePagina(String titulo) throws Exception {
        if (debug) System.out.println("=> Wiki: Montando parâmetros da busca por resumo de página");

        Map<String, String> parametros = new HashMap<>();
        
        // Parâmetro que indica a busca pelo resumo da página
        parametros.put("prop", "extracts");
        
        // Vamos buscar apenas o conteúdo antes da primeira seção
        parametros.put("exintro", "true");

        // Vamos buscar como texto puro em vez de HTML limitado
        parametros.put("explaintext", "true");

        // Vamos buscar o texto sem nenhuma formatação
        parametros.put("exsectionformat", "plain");

        // Vamos buscar a página cujo título foi passado
        parametros.put("titles", titulo);

        if (titulo.equals("São Paulo")) {
            System.out.println("É igual");
        }
        else {
            System.out.println("É diferente " + titulo + " x " + "São Paulo");
        }

        return consultarWiki(parametros);
    }

    /*
     * Junta dois MAPs em um novo MAP
     */
    private <K,V> Map<K, V> concatenaParametros(Map<K, V> parametros1, Map<K, V> parametros2) {
        // Junta os parâmetros passados com os parâmetros padrões
        Map<K, V> parametros = new HashMap<>(parametros1);
        parametros.putAll(parametros2);                
        return parametros;
    }

    private PaginaWiki consultarWiki(Map<String, String> parametros) throws Exception {
        try {
            // Começa a montar a string de consulta            
            String requisicao =  URLUtils.constroiURLRequisicao(endpoint, concatenaParametros(parametrosPadroes, parametros));
            if (debug) System.out.println("=> Wiki: URL da requisição: " + requisicao);
               
            // Faz a requisição na API
            HttpResponse<JsonNode> response = Unirest.get(requisicao).asJson();

            // Se a requisição NÃO deu certo
            if (response.getStatus() != 200) { 
                // TODO: criar exceção verificada específica
                throw new Exception("A requisição à API da Wikipedia não retornou resultado (status " + response.getStatus() + ")");
            }
            else {
                if (debug) System.out.println("=> Wiki: JSON de resposta\n" + JSONUtils.stringAmigavel(response.getBody().toString()));
                return processarConsulta(response.getBody().toString());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Processa uma consulta a uma página Wiki
     * 
     * @param jsonString String JSON retornada pela consulta
     * 
     * @return Objeto PaginaWiki com os dados da página encontrada
     */
    private PaginaWiki processarConsulta(String jsonString) {
        
        JsonObject obj = JsonParser.parseString(jsonString).getAsJsonObject();
        
        if (obj.get("batchcomplete").getAsBoolean()) {
            JsonObject pagina = obj.get("query").getAsJsonObject().get("pages").getAsJsonArray().get(0).getAsJsonObject();

            if (pagina.get("invalid") != null) {
                if (debug) System.out.println("=> Wiki: página não encontrada, motivo: " + pagina.get("invalidreason").getAsString());
            }
            else if (pagina.get("missing") != null) {
                if (debug) System.out.println("=> Wiki: página de título '" + pagina.get("title").getAsString() + "' não existe.");
            }
            else {
                return new PaginaWiki(pagina.get("title").getAsString(), 
                                    pagina.get("pageid").getAsInt(),
                                    pagina.get("extract").getAsString());
            }
            return null;
        }
        else {
            throw new UnsupportedOperationException("Wiki: Ainda não há trtamento para consultas em mlote");
        }
        
    }

}