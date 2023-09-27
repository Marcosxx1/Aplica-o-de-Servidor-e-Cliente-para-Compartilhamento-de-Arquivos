package fileClient;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * Esta classe representa o servidor para o sistema de gerenciamento de arquivos.
 */
public class Server {
    public static void main(String[] args) throws IOException {
    	Scanner entrada = new Scanner(System.in);
        int serverPort = 8080; // Porta do servidor

        String  directoryPath = "";
        boolean validPath = false;

        while (!validPath) {
            System.out.println("Digite o diretório onde os arquivos estão localizados:");
            directoryPath = entrada.nextLine();

            // Check if the directory exists
            Path path = Paths.get(directoryPath);
            if (Files.isDirectory(path)) {
                validPath = true;
            } else {
                System.out.println("Diretório inválido. Tente novamente.");
            }
        }

        
        // Cria um socket de servidor na porta especificada
        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println(
            "Server iniciado com o diretório: " + directoryPath + "\nServidor disponível na porta: " + serverPort);

        while (true) {
            // Aceita a conexão de um novo cliente
            Socket clientSocket = serverSocket.accept();
            System.out.println("Novo cliente conectado");
            final String finalDirectoryPath = directoryPath; 

            // Cria uma nova thread para lidar com o cliente
            Thread clientThread = new Thread(() -> {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String command;
                    while (true) {
                        try {
                            command = in.readLine();
                            if (command == null) {
                                // O cliente fechou a conexão, paramos o loop
                                break;
                            }

                            if (command.equals("INDEX")) {
                                // Comando para listar arquivos no diretório
                                File directory = new File(finalDirectoryPath);
                                String[] fileList = directory.list();
                                String fileListString = String.join(", ", fileList);
                                out.println(fileListString);
                            } else if (command.startsWith("GET ")) {
                                // Comando para enviar um arquivo ao cliente
                                String fileName = command.substring(4);
                                File file = new File(finalDirectoryPath + File.separator + fileName);

                                if (file.exists() && file.isFile()) {
                                    // Se o arquivo existe, lê e envia seu conteúdo para o cliente
                                    FileInputStream fileInputStream = new FileInputStream(file);
                                    try (BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
                                        byte[] buffer = new byte[1024];
                                        int bytesRead;

                                        out.println("OK");

                                        try (BufferedOutputStream outputStream = new BufferedOutputStream(
                                                clientSocket.getOutputStream())) {
                                            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                                                outputStream.write(buffer, 0, bytesRead);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        // Fecha o arquivo após a transferência
                                        fileInputStream.close();
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
                        } catch (SocketException se) {
                            // Lida com SocketException com a desconexão do cliente
                            System.out.println("Client disconnected");
                            break; // Para o loop para sair da thread
                        } catch (IOException e) {
                            e.printStackTrace();
                            break; // Para o loop para sair da thread em caso de outras exceções
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
