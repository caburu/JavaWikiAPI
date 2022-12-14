import java.util.Scanner;

import javax.swing.JOptionPane;

import br.ufla.gac106.JavaWikiAPI.Wiki;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner entrada = new Scanner(System.in);

        // Cria objeto para consultar dados da Wikipedia
        Wiki wiki = new Wiki();
        // Liga o modo de debug (verboso)
        wiki.setDebug(true);

        String titulo;
        do {
            System.out.print("\nDigite o título da página a ser buscada.\n\t- ENTER para sair.\n\t- No Windows, escreva @ para palavras acentuadas\n> ");
            titulo = entrada.nextLine();
            
            if (titulo.equals("@")) {
                System.out.println("Warning: No terminal no Windows está dando problema com String acentuadas.");
                // Se uso a palavra "São Paulo" diretamente no código, URLEncoder transforma em "S%C3%A3o+Paulo" e funciona.
                // Mas se o usuário digita isso no terminal, URLEncoder transforma em "S%EF%BF%BDo+Paulo" e dá errado.
                titulo = JOptionPane.showInputDialog(null, "Digite o título da página a ser buscada (vazio para sair): ");
            }
                        
            if (titulo != null && titulo.length() > 0) {

                System.out.println("\n=== Primeiro trecho da página: ===");
                System.out.println(wiki.buscarResumoDePagina(titulo));

                // System.out.println("\n=== Lista de imagens da página: ===");
                // ArrayList<String> infoImagens = wiki.getImagesOnPage(termo);
                // for (String infoImagem : infoImagens) {
                //     System.out.println("> " + infoImagem);
                // }

                // var infoImg = infoImagens.get(0);
                // String[] params = {"titles", infoImg, "prop", "imageinfo", "iiprop", "url"};
                // okhttp3.Response httpResponse = wiki.basicGET("query", params);                        
                // if (httpResponse.code() == 200) {
                //     // Gson gson = new GsonBuilder().setPrettyPrinting().create();;
                //     // String jsonString = httpResponse.body().string();
                //     // impressaoAmigavelJSON(jsonString);

                //     // Obtendo os dados do JSON como um Map
                //     System.out.println("\nObtendo URL da primeira imagem:");
                //     try {
                //         String jsonString = httpResponse.body().string();
                //         JsonObject obj = JsonParser.parseString(jsonString).getAsJsonObject();
                //         String url = obj.get("query").getAsJsonObject()
                //                         .get("pages").getAsJsonObject()
                //                         .get("-1").getAsJsonObject()
                //                         .get("imageinfo").getAsJsonArray()
                //                         .get(0).getAsJsonObject()
                //                         .get("url").getAsString();
                //         System.out.println(url);
                        
                //         BufferedImage image = ImageIO.read(new URL(url));
                //         JLabel lbl = new JLabel(new ImageIcon(image));
                //         JOptionPane.showMessageDialog(null, lbl, "ImageDialog",  JOptionPane.PLAIN_MESSAGE, null);
                //     } catch (JsonSyntaxException | IOException e) {
                //         e.printStackTrace();
                //     }
                    
                // }
            }
        } while (titulo != null && titulo.length() > 0);

        wiki.close();

        entrada.close();
    }
}
