import java.util.Scanner;

import javax.swing.JOptionPane;

import br.ufla.gac106.JavaWikiAPI.Wiki;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner entrada = new Scanner(System.in);

        // Cria objeto para consultar dados da Wikipedia
        Wiki wiki = new Wiki();
        // Liga o modo de debug (verboso)
        // wiki.setDebug(true);
        
        String termo;
        int opcao = 0;

        while (opcao != 3) {
            System.out.println("\n=== JAVA WIKI API ===");
            System.out.println(" 1 - Consultar página por título");
            System.out.println(" 2 - Pesquisar títulos de página");
            System.out.println(" 3 - Sair");
            System.out.print("\nQual opção deseja acessar? ");
            opcao = Integer.parseInt(entrada.nextLine());

            if (opcao == 3) {
                // se vai sair, não faz nada
            }
            else {
                System.out.print("\nDigite o termo de busca\n\t- No Windows, escreva @ para palavras acentuadas\n> ");
                termo = entrada.nextLine();
            
                if (termo.equals("@")) {
                    System.out.println("Warning: No terminal no Windows está dando problema com String acentuadas.");
                    // Se uso a palavra "São Paulo" diretamente no código, URLEncoder transforma em "S%C3%A3o+Paulo" e funciona.
                    // Mas se o usuário digita isso no terminal, URLEncoder transforma em "S%EF%BF%BDo+Paulo" e dá errado.
                    termo = JOptionPane.showInputDialog(null, "Digite o título da página a ser buscada (vazio para sair): ");
                }
                        
                if (termo != null && termo.length() > 0) {

                    if (opcao == 1) {
                        System.out.println("\n=== Primeiro trecho da página: ===");
                        System.out.println(wiki.buscarPagina(termo));
                    }
                    else if (opcao == 2) {
                        System.out.println("\n=== Primeiro trecho da página: ===");
                        System.out.println(wiki.pesquisarTitulosDePaginas(termo));
                    }
                }
            }
        }

        wiki.close();

        entrada.close();
    }
}
