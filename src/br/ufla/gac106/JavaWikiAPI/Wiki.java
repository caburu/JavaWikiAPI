package br.ufla.gac106.JavaWikiAPI;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    // endereço completo da API a ser utilizada
    private String endpoint;
    // parâmetros padrões a serem utilizados em todas as consultas
    private Map<String, String> parametrosPadroes;
    // indica se está em modo de debug (nesse caso, são exibidas mensagens detalhadas do que é feito)
    private boolean debug;

    /**
     * Constrói o objeto capaz de obter dados da Wikipedia em Português (domínio "pt.wikipedia.org").
     * Lembre-se de usar o método terminar quando não for mais usar o objeto.
     * 
     * Para obter dados de outras Wikis, utilize outro construtor (ou o método setEndpoint).
     */
    public Wiki() {
        this("https://pt.wikipedia.org/w/api.php");
    }

    /**
     * Constrói o objeto capaz de obter dados de uma Wiki
     * Lembre-se de usar o método terminar quando não for mais usar o objeto.
     * 
     * @param endpoint Endpoint da Wiki a ser consultada (ex: "https://en.wikipedia.org/w/api.php").
     */
    public Wiki(String endpoint) {
        this.endpoint = endpoint;
        debug = false;

        definirParametrosPadroes();
    }

    /**
     * Define parâmetros padrões a serem utilizados em todas as consultas
     */
    private void definirParametrosPadroes() {
        parametrosPadroes = new HashMap<>();        

        // Aplica os redirecionamentos necessários até chegar na página correta
        parametrosPadroes.put("redirects","resolve");
        
        // Vamos obter resultados no formato JSON v2 (sem isso é retornado resultado HTML)
        parametrosPadroes.put("format","json");
        parametrosPadroes.put("formatversion","2");
    }

    /**
     * Domínio que está sendo utilizado nas consultas
     * 
     * @return Domínio da Wiki
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Muda o domínio Wiki utilizado nas consultas (ex: "https://en.wikipedia.org/w/api.php")
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Retorna se está em modo de debug (nesse caso, são exibidas mensagens detalhadas do que é feito)
     * 
     * @return Indica se está em modo de debug
     */
    public Boolean getDebug() {
        return debug;
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
     * Retorna um objeto que representa a página Wiki cujo título foi passado (ou null se a página não for encontrada).
     * Observações:
     * - O resumo é dado pelo texto que vem antes da primeira seção da página (nem toda página da Wiki tem).
     * - A imagem é o 'thumbnail' da página (nem toda página da Wiki tem).
     * 
     * @param titulo Título da página a ser buscada
     * 
     * @returns Objeto da página Wiki buscada (ou null se ela não for encontrada)
     */
    public PaginaWiki buscarPagina(String titulo) throws Exception {
        if (debug) System.out.println("=> Wiki: Montando parâmetros da busca por uma página pelo título");

        Map<String, String> parametros = new HashMap<>();

        // Vamos fazer uma consulta
        parametros.put("action", "query");
        
        // Parâmetro que indica a busca pelo resumo da página e pela imagem (thumbnail) da página
        parametros.put("prop", "extracts|pageimages");
        
        // Vamos buscar apenas o conteúdo antes da primeira seção
        parametros.put("exintro", "true");

        // Vamos buscar como texto puro em vez de HTML limitado
        parametros.put("explaintext", "true");

        // Vamos buscar o texto sem nenhuma formatação
        parametros.put("exsectionformat", "plain");

        // Largura máxima da imagem em pixels
        parametros.put("pithumbsize", "300");

        // Vamos buscar a página cujo título foi passado
        parametros.put("titles", titulo);

        return consultarWiki(parametros);
    }

    
    /**
     * Faz uma busca pelo termo passado e retorna títulos de páginas relacionados ao termo de busca (no máximo 10 títulos).
     * É útil quando se pretende buscar uma página mas não se tem certeza do título dela na Wiki
     * 
     * @param termoDeBusca String utilizada para a busca
     * 
     * @return Uma lista de páginas retornada pela busca
     */
    public List<String> pesquisarTitulosDePaginas(String termoDeBusca) throws Exception {
        if (debug) System.out.println("=> Wiki: Montando parâmetros da pesquisa por títulos de páginas");

        Map<String, String> parametros = new HashMap<>();

        // Vamos fazer uma pesquisa
        parametros.put("action", "opensearch");
        
        // Parâmetro para passar o termo de busca
        parametros.put("search", termoDeBusca);
        
        return pesquisarNaWiki(parametros);
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

    private JsonElement fazerRequisicao(Map<String, String> parametros) throws Exception {
        try {
            // Concatena os parâmetros passados com os padrões
            Map<String, String> param = concatenaParametros(parametrosPadroes, parametros);

            // Começa a montar a string de consulta            
            String requisicao =  URLUtils.constroiURLRequisicao(endpoint, param);
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
                return JsonParser.parseString(response.getBody().toString());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PaginaWiki consultarWiki(Map<String, String> parametros) throws Exception {
        JsonObject obj = fazerRequisicao(parametros).getAsJsonObject();

        if (debug) System.out.println("=> Wiki: Processando retorno da requisição");
        
        if (obj.get("batchcomplete").getAsBoolean()) {
            JsonObject pagina = obj.get("query").getAsJsonObject().get("pages").getAsJsonArray().get(0).getAsJsonObject();

            if (pagina.get("invalid") != null) {
                if (debug) System.out.println("=> Wiki: página não encontrada, motivo: " + pagina.get("invalidreason").getAsString());
            }
            else if (pagina.get("missing") != null) {
                if (debug) System.out.println("=> Wiki: página de título '" + pagina.get("title").getAsString() + "' não existe.");
            }
            else {
                BufferedImage imagem = null;
                if (pagina.get("thumbnail") != null) {
                    String endereçoDaImagem = pagina.get("thumbnail").getAsJsonObject().get("source").getAsString();
                    imagem = ImageIO.read(new URL(endereçoDaImagem));
                }
                String resumo = "";
                if (pagina.get("extract") != null) {
                    resumo = pagina.get("extract").getAsString();
                }
                return new PaginaWiki(pagina.get("title").getAsString(), 
                                      pagina.get("pageid").getAsInt(),
                                      resumo, imagem);
            }
            return null;
        }
        else {
            throw new UnsupportedOperationException("Wiki: Ainda não há trtamento para consultas em lote");
        }
    }

    private List<String> pesquisarNaWiki(Map<String, String> parametros) throws Exception {
        JsonArray jsonArray = fazerRequisicao(parametros).getAsJsonArray();

        if (debug) System.out.println("=> Wiki: Processando retorno da requisição");
        
        List<String> titulos = new ArrayList<>();

        for (JsonElement element : jsonArray.get(1).getAsJsonArray()) {
            titulos.add(element.getAsString());
        }

        return titulos;
    }
}
