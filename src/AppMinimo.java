import java.util.List;

import br.ufla.dac.javaWikiAPI.Wiki;
import br.ufla.dac.javaWikiAPI.PaginaWiki;

public class AppMinimo {
    public static void main(String[] args) throws Exception {
        // Cria o objeto que consulta a API da Wikipedia
        Wiki wiki = new Wiki();

        // Demonstrando busca de uma página por título

        System.out.println("Exibindo descrição da página de título 'Lavras'");
        PaginaWiki pagina = wiki.consultarPagina("Lavras");
        System.out.println(pagina.getResumo());

        // Demonstrando pesquisa por títulos de página por um termo de busca

        System.out.println("\nFazendo uma busca por páginas que tenham o termo 'gato' no título");
        List<String> titulosDePagina = wiki.pesquisarTitulosDePaginas("gato");

        System.out.println("\nExibindo títulos de página encontrados");
        for (String tituloDePagina : titulosDePagina) {
            System.out.print(tituloDePagina + " - ");
        }

        System.out.println("\nExibindo descrições das páginas encontradas:");
        for (String tituloDePagina : titulosDePagina) {
            pagina = wiki.consultarPagina(tituloDePagina);
            System.out.println(tituloDePagina + "\n" + pagina.getResumo() + "\n");
        }

        // libera os recursos do objeto usado para fazer a consulta à API
        wiki.close();
    }
}
