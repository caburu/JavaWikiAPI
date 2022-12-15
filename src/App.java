import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import br.ufla.gac106.JavaWikiAPI.PaginaWiki;
import br.ufla.gac106.JavaWikiAPI.Wiki;

public class App {
    // Objeto usado para consultar a API de uma Wiki.
    private Wiki wiki;
    // Para obter dados do usuário
    private Scanner entrada;

    public static void main(String[] args) throws Exception {
        new App().executar();
    }
    
    /*
     * Executa o programa
     */
    public void executar() throws Exception {
        entrada = new Scanner(System.in);

        // Cria objeto para consultar dados da Wikipedia
        wiki = new Wiki();
        
        // Trata o loop do menu
        int opcao = 0;
        while (opcao != 5) {
            // exibe menu e opção opção do usuário
            opcao = exibirMenu();

            // trata a opção de menu escolhida
            switch (opcao) {
                // se for para consultar página ou pesquisar títulos de página
                case 1:
                    String titulo = obterTermoDeBusca("o título da página");
                    consultarPagina(titulo);
                    break;

                case 2:
                    // obtém o termo de busca (título da página ou chave de busca)
                    String termo = obterTermoDeBusca("o termo a ser pesquisado");
                    buscarTitulosDePaginas(termo);
                    break;
                
                case 3:
                    ligarDesligarModoDebug();
                    break;
                    
                case 4:
                    mudarWikiConsultada();
                    break;
                    
                case 5:
                    System.out.println("Obrigado por usar a Java Wiki API!");
                    break;           

                default:
                    System.out.println("=> Opção inválida!");
                    break;
            }
        }

        // libera os recursos do objeto usado para fazer a consulta à API
        System.out.println("Liberando recursos (aguarde)...");
        wiki.close();
        entrada.close();
    }
    /*
     * Exibe o menu e obtém a opção escolhida pelo usuário
     */
    private int exibirMenu() {
        int opcao;
        System.out.println("\n=== JAVA WIKI API ===");
        System.out.println(" 1 - Consultar página por título");
        System.out.println(" 2 - Pesquisar títulos de página");
        System.out.println(" 3 - Ligar/desligar modo de debug");
        System.out.println(" 4 - Mudar Wiki acessada (domínio)");
        System.out.println(" 5 - Sair");
        System.out.print("\nDigite a opção desejada: ");
        opcao = Integer.parseInt(entrada.nextLine());
        return opcao;
    }

    /**
     * Obtém uma String de busca do usuário
     * @return A String digitada ou null (se não fornecida)
     */
    private String obterTermoDeBusca(String descricaoDoTermo) {
        System.out.print("\nDigite " + descricaoDoTermo + "\n\t- No Windows, escreva @ para palavras acentuadas\n> ");
        String termo = entrada.nextLine();
    
        if (termo.equals("@")) {
            System.out.println("Warning: No terminal no Windows está dando problema com String acentuadas.");
            // Se uso a palavra "São Paulo" diretamente no código, URLEncoder transforma em "S%C3%A3o+Paulo" e funciona.
            // Mas se o usuário digita isso no terminal, URLEncoder transforma em "S%EF%BF%BDo+Paulo" e dá errado.
            termo = JOptionPane.showInputDialog(null, "Digite " + descricaoDoTermo + ":");
        }

        if (termo == null || termo.length() == 0) {
            System.out.println("=> Nenhum termo digitado.");
            return null;
        }
        else {
            return termo;
        }
    }

    /*
     * Consulta a página de título passado na API da Wiki
     */
    private void consultarPagina(String titulo) throws Exception {
        if (titulo != null) {    
            System.out.println("Consultando " + wiki.getEndpoint() + " (aguarde)...");
            PaginaWiki pagina = wiki.buscarPagina(titulo);

            if (pagina == null) {
                System.out.println("\nPágina " + titulo + " não encontrada!");
            }
            else {
                System.out.println("\n" + pagina);
                
                exibirImagemDaPagina(pagina);
            }            
        }
        aguardarEnter();
    }

    /*
     * Exibe uma janela com a imagem (thumbnail) da página Wiki passada
     */
    private void exibirImagemDaPagina(PaginaWiki pagina) {
        if (pagina.getImagem() != null) {
            System.out.println("\n=> Imagem encontrada e exibida em seguida.");
            JFrame janela = new JFrame(pagina.getTitulo());
            janela.add(new JLabel(new ImageIcon(pagina.getImagem()), JLabel.LEFT));
            janela.setAlwaysOnTop(true);
            janela.pack();
            janela.setVisible(true);
        }
        else {
            System.out.println("=> Página não tem imagem (thumbnail)!");
        }
    }

    /*
     * Faz uma pesquisa por títulos de página que tenham o termo passado
     */
    private void buscarTitulosDePaginas(String termo) throws Exception {
        if (termo != null) {
            System.out.println("\nTítulos de páginas encontradas: ");
            System.out.println("> " + wiki.pesquisarTitulosDePaginas(termo));
        }
        aguardarEnter();
    }

    /**
     * Exibe mensagem e aguarda o usuário digitar ENTER
     */
    private void aguardarEnter() {
        System.out.println("\nDigite ENTER para continuar");
        entrada.nextLine();
    }

    /**
     * Muda o domínio da Wiki utilizado
     */
    private void mudarWikiConsultada() {
        System.out.print("Digite o endpoint da Wiki a ser consultada (exemplos: https://en.wikipedia.org/w/api.php ou https://awoiaf.westeros.org/api.php):\n> ");
        String dominio = entrada.nextLine();
        if (dominio.length() == 0) { 
            System.out.println("Domínimo inválido!");
        }
        else {
            wiki.setEndpoint(dominio);
            System.out.print("> Domínio alterado para: " + dominio);
        }
        aguardarEnter();
    }


    /**
     * Liga/desliga o modo de debug do objeto que consulta a API da Wiki
     */
    private void ligarDesligarModoDebug() {
        // liga/desliga o modo de debug
        wiki.setDebug(!wiki.getDebug());
        System.out.println("Modo de DEBUG agora: " + wiki.getDebug());
    }
}
