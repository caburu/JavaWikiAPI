import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import br.ufla.gac106.JavaWikiAPI.PaginaWiki;
import br.ufla.gac106.JavaWikiAPI.Wiki;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner entrada = new Scanner(System.in);

        // Cria objeto para consultar dados da Wikipedia
        Wiki wiki = new Wiki();
        
        String termo;
        int opcao = 0;

        while (opcao != 4) {
            System.out.println("\n=== JAVA WIKI API ===");
            System.out.println(" 1 - Consultar página por título");
            System.out.println(" 2 - Pesquisar títulos de página");            
            System.out.println(" 3 - Ligar/desligar modo de debug");
            System.out.println(" 4 - Sair");
            System.out.print("\nQual opção deseja acessar? ");
            opcao = Integer.parseInt(entrada.nextLine());
                       
            if (opcao == 4) {
                // se vai sair, não faz nada
            }
            else if (opcao == 3) {
                // liga/desliga o modo de debug
                wiki.setDebug(!wiki.getDebug());
                System.out.println("Modo de DEBUG agora: " + wiki.getDebug());
            }
            else {
                if (opcao > 4) {
                    System.out.println("=> Opção inválida!");
                } 
                else {
                    System.out.print("\nDigite o termo de busca\n\t- No Windows, escreva @ para palavras acentuadas\n> ");
                    termo = entrada.nextLine();
                
                    if (termo.equals("@")) {
                        System.out.println("Warning: No terminal no Windows está dando problema com String acentuadas.");
                        // Se uso a palavra "São Paulo" diretamente no código, URLEncoder transforma em "S%C3%A3o+Paulo" e funciona.
                        // Mas se o usuário digita isso no terminal, URLEncoder transforma em "S%EF%BF%BDo+Paulo" e dá errado.
                        termo = JOptionPane.showInputDialog(null, "Digite o termo de busca: ");
                    }
                            
                    if (termo == null || termo.length() == 0) {
                        System.out.println("=> Nenhum termo digitado.");
                    }
                    else {
                        if (opcao == 1) {
                            System.out.println("Consultando " + wiki.getDominio() + " (aguarde)...");
                            PaginaWiki pagina = wiki.buscarPagina(termo);

                            if (pagina == null) {
                                System.out.println("\nPágina " + termo + " não encontrada!");
                            }
                            else {
                                System.out.println("\n" + pagina);
                                
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
                        }
                        else if (opcao == 2) {
                            System.out.println("\nTítulos de páginas encontradas: ");
                            System.out.println("> " + wiki.pesquisarTitulosDePaginas(termo));
                        }
                    }
                }
                System.out.println("\nDigite ENTER para continuar");
                entrada.nextLine();
            }
        }

        wiki.close();

        entrada.close();
    }
}
