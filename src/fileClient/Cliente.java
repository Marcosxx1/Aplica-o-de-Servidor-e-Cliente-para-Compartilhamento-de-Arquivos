package fileClient;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/*	TSI 3713 Sistemas Distribuídos
Professor: 
Rodrigo Andrade Cardoso

Alunos:
Luiz Ferreira Neto 
	Pablo Ruan Dias
	Marcos Alexandre da Silva

*/

/*
 * Esta é a classe principal que representa o cliente do sistema de gerenciamento de arquivos.
 */

public class Cliente {
	public static void main(String[] args) throws IOException {
		// Configurações do servidor
		String serverHostname = "localhost";
		int serverPort = 8080;

		try (Socket socket = new Socket(serverHostname, serverPort);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				Scanner scanner = new Scanner(System.in)) {

			System.out.println("Connected to the server\n");

			while (true) {
				try {
					System.out.println("Escolha a ação:");
					System.out.println("1. Listar todos arquivos (INDEX)");
					System.out.println("2. Baixar arquivo (GET)");
					System.out.println("3. Sair");

					// Lê a escolha do usuário
					int choice = scanner.nextInt();
					scanner.nextLine();

					if (choice == 1) {
						// Comando para listar arquivos
						out.println("INDEX");
						System.out.println("Comando INDEX enviado");

						// Recebe a lista de arquivos do servidor
						String fileList = in.readLine();
						if (fileList != null) {
							System.out.println("\nLista de arquivos recebidos do servidor: ");
							System.out.println(fileList);
						} else {
							System.out.println("Resposta vazia do servidor.");
						}
					} else if (choice == 2) {
						// Comando para baixar um arquivo
						System.out.println("Digite o nome do arquivo para download: ");
						String fileName = scanner.nextLine();

						System.out.println(
								"Digite o diretório onde o arquivo será baixado (ex: C:\\caminho\\para\\salvar):");
						String destinationDirectory = scanner.nextLine();

						// Verifica se o diretório de destino é válido
						Path destinationPath = Paths.get(destinationDirectory, fileName);
						if (!Files.isDirectory(destinationPath.getParent())) {
							System.out.println("Diretório inválido.");
							continue;
						}

						// Envia o comando GET com o nome do arquivo
						out.println("GET " + fileName);
						System.out.println("Comando GET enviado para o arquivo: " + fileName);

						// Recebe a resposta do servidor
						String response = in.readLine();

						if (response != null) {
							if (response.equals("OK")) {
								// Se o arquivo existe, baixa e salva no diretório especificado
								try (FileWriter fileWriter = new FileWriter(destinationPath.toFile());
										BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
									String line = in.readLine();

									while (!line.equals("END_OF_FILE")) {
										bufferedWriter.write(line);
										bufferedWriter.newLine();

										line = in.readLine();
									}
									System.out.println("Arquivo baixado com sucesso: " + fileName);
								}
							} else if (response.equals("ARQUIVO_INVALIDO")) {
								System.out.println("Nome do arquivo inválido. Arquivo não encontrado no servidor.");
							} else if (response.startsWith("ERROR")) {
								String errorMessage = response.substring(6);
								if (errorMessage.equals("Arquivo não encontrado")) {
									System.out.println(" Arquivo não encontrado no servidor.");
								} else {
									System.out.println("Erro do servidor: " + errorMessage);
								}
							} else {
								System.out.println("Resposta inesperada do servidor: " + response);
							}
						} else {
							System.out.println("Resposta inválida do servidor.");
						}
					} else if (choice == 3) {
						// Encerra a conexão e sai do loop
						System.out.println("Fechando a conexão com o servidor.");
						break;
					} else {
						System.out.println("\nOpção inválida\nDigite: 1, 2, ou 3.\n");
					}
				} catch (InputMismatchException e) {
					// Tratamento de entrada inválida
					System.out.println("Entrada inválida. Digite: 1, 2, ou 3.\n");
					scanner.nextLine();
				}
			}
		}
	}
}
