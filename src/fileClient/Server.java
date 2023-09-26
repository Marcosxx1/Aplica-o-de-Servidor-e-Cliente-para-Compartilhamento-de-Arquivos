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
 * Esta classe representa o servidor para o sistema de gerenciamento de arquivos.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        int serverPort = 8080; // Porta do servidor
        String directoryPath = "C:\\Users\\marco\\eclipse-workspace\\TrabalhoDois\\src\\arquivos_compartilhados"; // Diretório de arquivos compartilhados

        // Cria um socket de servidor na porta especificada
        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println(
            "Server iniciado com o diretório: " + directoryPath + "\nServidor disponível na porta: " + serverPort);

        while (true) {
            // Aceita a conexão de um novo cliente
            Socket clientSocket = serverSocket.accept();
            System.out.println("Novo cliente conectado");

            // Cria uma nova thread para lidar com o cliente
            Thread clientThread = new Thread(() -> {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String command;
                    while ((command = in.readLine()) != null) {
                        if (command.equals("INDEX")) {
                            // Comando para listar arquivos no diretório
                            File directory = new File(directoryPath);
                            String[] fileList = directory.list();
                            String fileListString = String.join(", ", fileList);
                            out.println(fileListString);
                        } else if (command.startsWith("GET ")) {
                            // Comando para enviar um arquivo ao cliente
                            String fileName = command.substring(4);
                            File file = new File(directoryPath + File.separator + fileName);

                            if (file.exists() && file.isFile()) {
                                // Se o arquivo existe, lê e envia seu conteúdo para o cliente
                                try (BufferedInputStream fileInputStream = new BufferedInputStream(
                                        new FileInputStream(file))) {
                                    byte[] buffer = new byte[1024];
                                    int bytesRead;

                                    out.println("OK");

                                    try (BufferedOutputStream outputStream = new BufferedOutputStream(
                                            clientSocket.getOutputStream())) {
                                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                            outputStream.write(buffer, 0, bytesRead);
                                        }
                                    }
                                }
                            } else {
                                // Se o arquivo não existe, envia um erro para o cliente
                                out.println("ERROR Arquivo não encontrado");
                            }
                            out.println("END_OF_FILE");
                        } else {
                            // Comando inválido
                            out.println("Comando inválido");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // Fecha a conexão com o cliente
                        clientSocket.close();
                        System.out.println("Client disconnected");
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
