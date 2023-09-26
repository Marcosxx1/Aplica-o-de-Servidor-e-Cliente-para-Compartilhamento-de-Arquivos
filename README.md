# Aplicação de Servidor e Cliente para Compartilhamento de Arquivos

Esta é uma aplicação de servidor e cliente que permite listar arquivos (INDEX) e baixar arquivos (GET) de uma pasta compartilhada.

## Configuração do Servidor

1. **Navegar até o diretório `src` do projeto:**

```shell
   cd caminho/para/o/diretorio/src
```


 
Compilar os arquivos do servidor:

 


```shell
javac fileClient/Server.java
 ```
Execute o servidor, especificando o caminho para a pasta compartilhada (substitua <caminho> pelo caminho real da pasta compartilhada):

 
```shell
java fileClient.Server <caminho>
 ```

O servidor estará disponível na porta padrão 8080.

Configuração do Cliente
Abra um novo prompt de comando em uma janela separada.

Navegue até o diretório src do projeto:

 ```shell
 ```


```shell
cd caminho/para/o/diretorio/src
 ```

Compile o arquivo do cliente:

 ```shell
 javac fileClient/Cliente.java
 ```



Execute o cliente, especificando o endereço do servidor (localhost) e a porta (8080):

  ```shell
  java fileClient.Cliente localhost 8080
 ```

Agora você pode interagir com o cliente para listar arquivos disponíveis (INDEX) ou baixar arquivos específicos (GET) da pasta compartilhada.

Lembre-se de substituir <caminho> pelo caminho real da pasta compartilhada que deseja usar.

Nota: Certifique-se de que o servidor esteja em execução antes de iniciar o cliente.

Exemplo de Uso
Para listar os arquivos disponíveis no servidor (comando INDEX):

No cliente, digite INDEX.

```vbnet
Digite o comando

(INDEX, GET <nome_do_arquivo>, ou EXIT):
```

Digite o comando

(INDEX, GET <nome_do_arquivo>, ou EXIT):
O servidor enviará a lista de arquivos disponíveis.

```arduino
Lista de arquivos recebida do servidor:
arquivo1.txt, arquivo2.txt, ...
Para baixar um arquivo específico (comando GET):
```

Lista de arquivos recebida do servidor:
arquivo1.txt, arquivo2.txt, ...
Para baixar um arquivo específico (comando GET):

No cliente, digite GET seguido do nome do arquivo desejado.

```vbnet
Digite o comando

(INDEX, GET <nome_do_arquivo>, ou EXIT):
```
O servidor verificará se o arquivo existe e o enviará para o cliente, se disponível.

```arduino

Conteúdos do arquivo recebidos:
Conteúdo do arquivo...
Comandos Disponíveis
INDEX: Lista os arquivos disponíveis no servidor.
GET <nome_do_arquivo>: Baixa o arquivo especificado do servidor.
EXIT: Encerra a aplicação do cliente.
```