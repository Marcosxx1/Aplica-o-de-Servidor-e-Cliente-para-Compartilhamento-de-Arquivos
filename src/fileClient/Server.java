package fileClient;

import java.io.*;
import java.net.*;


/*	TSI 3713 Sistemas Distribuídos
    Professor: 
	Rodrigo Andrade Cardoso
	
	Alunos:
	Luiz Ferreira Neto
  	Pablo Ruan Dias
 	Marcos Alexandre da Silva
 
 */


/**
 * Esta classe representa um servidor que atende a solicitações de clientes para listar arquivos (INDEX)
 * ou baixar arquivos (GET) de uma pasta compartilhada por meio de uma conexão de soquete.
 */
public class Server {
    /**
     * O método principal da classe que inicia o servidor e lida com as solicitações dos clientes.
     *
     * @param args Os argumentos da linha de comando (não são usados neste caso).
     * @throws IOException Se ocorrer um erro de E/S durante a comunicação com o cliente.
     */
    public static void main(String[] args) throws IOException {
        // Configuração do servidor
        int serverPort = 8080; // Porta em que o servidor está escutando
        String directoryPath = "C:\\Users\\marco\\eclipse-workspace\\TrabalhoDois\\src\\arquivos_compartilhados"; // Caminho para a pasta de arquivos compartilhados

        // Criação do soquete do servidor
        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println("Servidor iniciado com a pasta no caminho: " + directoryPath + "\n Servidor disponível na porta: " + serverPort);

        while (true) {
            // Aceita uma conexão de cliente
            Socket clientSocket = serverSocket.accept();
            System.out.println("Novo cliente conectado");

            // Inicia uma nova thread para lidar com a comunicação com o cliente
            Thread clientThread = new Thread(() -> {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String command;
                    while ((command = in.readLine()) != null) {
                        if (command.equals("INDEX")) {
                            // Comando INDEX: Envia a lista de arquivos da pasta compartilhada para o cliente
                            File directory = new File(directoryPath);
                            String[] fileList = directory.list();
                            String fileListString = String.join(", ", fileList);
                            out.println(fileListString);
                        } else if (command.startsWith("GET ")) {
                            // Comando GET: Envia o conteúdo de um arquivo solicitado pelo cliente
                            String fileName = command.substring(4);
                            File file = new File(directoryPath + "/" + fileName);
                            if (file.exists() && file.isFile()) {
                                out.println("OK"); // Confirma que o arquivo existe
                                try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                                    String line;
                                    while ((line = fileReader.readLine()) != null) {
                                        out.println(line); // Envia cada linha do arquivo ao cliente
                                    }
                                }
                            } else {
                                out.println("Arquivo não encontrado"); // Informa que o arquivo não foi encontrado
                            }
                        } else {
                            out.println("Comando inválido"); // Responde se o comando não for reconhecido
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                        System.out.println("Cliente desconectado");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Inicia a thread do cliente
            clientThread.start();
        }
    }
}
