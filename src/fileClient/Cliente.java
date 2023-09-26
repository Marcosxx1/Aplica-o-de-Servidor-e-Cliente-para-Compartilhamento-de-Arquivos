package fileClient;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/*	TSI 3713 Sistemas Distribuídos
    Professor: 
	Rodrigo Andrade Cardoso
	
	Alunos:
	Luiz Ferreira Neto
  	Pablo Ruan Dias
 	Marcos Alexandre da Silva
 
 */


/**
 * Esta classe representa um cliente que se conecta a um servidor para realizar operações
 * como listar arquivos (INDEX) ou baixar arquivos (GET) por meio de uma conexão de soquete.
 */
public class Cliente {
    /**
     * O método principal da classe que inicia a execução do cliente.
     *
     * @param args Os argumentos da linha de comando (não são usados neste caso).
     * @throws IOException Se ocorrer um erro de E/S durante a comunicação com o servidor.
     */
    public static void main(String[] args) throws IOException {
        // Configuração do servidor de destino
        String serverHostname = "localhost"; // Endereço do servidor
        int serverPort = 8080; // Porta de conexão

        try (Socket socket = new Socket(serverHostname, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Conexão bem-sucedida com o servidor
            System.out.println("Conectado ao servidor\n");

            Scanner scanner = new Scanner(System.in);

            // Loop principal para interação com o usuário
            while (true) {
                System.out.println("Digite o comando \n\n(INDEX, GET <file_name>, or EXIT):");

                // Ler o comando do usuário
                String userInput = scanner.nextLine();

                // Converter o comando para maiúsculas para fazer correspondência sem distinção entre maiúsculas e minúsculas
                userInput = userInput.toUpperCase();

                if (userInput.equals("INDEX")) {
                    // Enviar o comando INDEX para o servidor
                    out.println(userInput);
                    System.out.println("Comando INDEX enviado");

                    // Receber e imprimir a lista de arquivos do servidor
                    String fileList = in.readLine();
                    System.out.println("\nLista de arquivos recebida do servidor: ");
                    System.out.println(fileList);
                } else if (userInput.startsWith("GET ")) {
                    // Extrair o nome do arquivo do comando GET
                    String fileName = userInput.substring(4);

                    // Enviar o comando GET para o servidor com o nome do arquivo
                    out.println(userInput);
                    System.out.println("Comando GET para o arquivo: " + fileName);

                    // Receber a resposta do servidor
                    String response = in.readLine();
                    if (response.equals("OK")) {
                        // Se a resposta for "OK", receber e imprimir o conteúdo do arquivo
                        String fileContents = in.readLine();
                        System.out.println("Conteúdos do arquivo recebidos: ");
                        System.out.println(fileContents);
                    } else {
                        // Se a resposta não for "OK", imprimir uma mensagem de erro
                        System.out.println("Erro: " + response);
                    }
                } else if (userInput.equals("EXIT")) {
                    // Enviar o comando EXIT para o servidor e encerrar o loop
                    out.println(userInput);
                    System.out.println("Comando EXIT enviado");
                    break; // Sair do loop
                } else {
                    // Tratar comando inválido
                    System.out.println("Erro: Comando inválido");
                }
            }
        }
    }
}
