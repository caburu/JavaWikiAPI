import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        // Precisei passar o charSet por problemas no terminal do VS Code no Windows em reconhecer acentos
        Scanner entrada = new Scanner(System.in, "utf-8");

        // Cria objeto para consultar dados da Wikipedia
        Wiki wiki = new Wiki();
        // Liga o modo de debug (verboso)
        wiki.setDebug(true);

        String titulo;
        do {
            System.out.print("Digite o título da página a ser buscada (ENTER para sair): ");
            titulo = entrada.nextLine();
                        
            if (titulo.length() > 0) {

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
        } while (titulo.length() > 0);

        entrada.close();
    }
}
