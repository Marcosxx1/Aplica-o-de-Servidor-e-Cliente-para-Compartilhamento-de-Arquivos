# Sistema de Gerenciamento de Arquivos - README

Este é um sistema simples de gerenciamento de arquivos que permite listar arquivos no servidor e baixar arquivos específicos do servidor. Ele consiste em dois componentes: o servidor e o cliente.

## Executando o Servidor

Para executar o servidor, siga estas etapas:

1. Navegue até o diretório `src` onde estão localizados os arquivos do servidor:

### em um prompt
```shell
cd caminho\ate\a\pasta\src
```

## 2. Compile os arquivos do servidor e cliente usando o `javac`:

```shell
javac fileClient/Server.java
javac fileClient/Cliente.java

 
java fileClient.Server arquivos_compartilhados
 ```

## 3. Execute o servidor com o comando a seguir, fornecendo o caminho do diretório de arquivos compartilhados como argumento:

 
```shell
java fileClient.Server <caminho_do_diretorio_de_arquivos_compartilhados>
 ```
Substitua `<caminho_do_diretorio_de_arquivos_compartilhados>` pelo caminho absoluto do diretório onde os arquivos compartilhados estão localizados.

 Nesta aplicação estamos utilizando a pasta arquivos_compartilhados:

 ```shell
 java fileClient.Server arquivos_compartilhados
 ```
 

O servidor será iniciado e estará disponível na porta padrão 8080.

## Executando o Cliente
### em outro prompt

Para executar o cliente, siga estas etapas:

1. Abra um novo prompt de comando.

2. Navegue até o diretório `src` onde estão localizados os arquivos do cliente:

```shell
cd caminho\ate\a\pasta\src
```

3. Execute o cliente com o comando a seguir, fornecendo o endereço do servidor e a porta como argumentos (localhost e 8080, respectivamente, se estiverem em execução no mesmo computador):

```shell
java fileClient.Cliente localhost 8080
```


O cliente será iniciado e você poderá interagir com o servidor para listar arquivos e baixar arquivos específicos.

### Comandos Disponíveis no Cliente:

- **1. Listar todos arquivos (INDEX):** Use esta opção para listar todos os arquivos disponíveis no servidor.

- **2. Baixar arquivo (GET):** Use esta opção para baixar um arquivo específico do servidor. Você será solicitado a fornecer o nome do arquivo e o diretório de destino.

- **3. Sair:** Use esta opção para encerrar a conexão com o servidor e sair do cliente.

O sistema também inclui validações para garantir que o diretório de destino seja válido e fornece mensagens de erro adequadas quando um arquivo não é encontrado ou um comando inválido é inserido.

 
