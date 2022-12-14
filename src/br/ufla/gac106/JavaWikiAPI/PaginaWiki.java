package br.ufla.gac106.JavaWikiAPI;

/**
 * Classe que representa uma Página Wiki consultada
 */
public class PaginaWiki {
    
    // Título da Página
    private String titulo;
    // Identificador da página na Wiki
    private int id;
    // Resumo da página
    private String resumo;
    
    /*
     * Constrói um objeto de uma página Wiki a partir de seu título, identificador e texto de resumo
     * 
     * @param titulo Título da página na Wiki
     * @param id Identificador da página na Wiki
     * @param resumo Texto de resumo da página na Wiki
     */
    public PaginaWiki(String titulo, int id, String resumo) {
        this.titulo = titulo;
        this.id = id;
        this.resumo = resumo;
    }

    /**
     * Título da página na Wiki
     * @return O título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Identificador da página na Wiki
     * @return O identificador
     */
    public int getId() {
        return id;
    }

    /**
     * Resumo da página na Wiki
     * @return O resumo
     */
    public String getResumo() {
        return resumo;
    }

    @Override
    public String toString() {
        return "PaginaWiki: " + titulo + " (id=" + id + ")\n" + resumo;
    }

    
}
