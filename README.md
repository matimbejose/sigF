# SG Finanças


#### Pré requisitos
- JDK 8
- Maven
- Git
- Variáveis de ambiente configuradas

## Configuração

####DataSource
Para rodar o projeto é necessário configurar o `standalone-full.xml` localizado
   dentro do seu diretório do wildfly ex: `c:\wildfly\standalone\configuration`
     
   - Verifique se o `<xa-datasource >` está  está com a `jndi` configurada corretamente.
   - Remover a tag `deployments` com referencia de outro projeto (caso exista).

####Lombok
Se caso estiver usando `IntelliJ` será preciso baixar e ativar este recurso.
- Baixe o plugin no marketplace.
- File > Settings > Builder, Execution, Deployment > Compiler > `Annotation Processor`
   - Marque (checked) `Enable Annotation Processing`. 

####Maven
É necessário certificar da configuração do maven do repositório (.m2), se está OK.



1. Configuração do maven (Apenas para IntelliJ):
- Clique em File > Settings > Builder, Execution, Deployment > Build Tools > Maven > Runner 
- O primeiro campo: `Delegate IDE build/run actions to Maven` deixe `marcado (checked)` 


####IntelliJ IDEA
Para executar nesta ide é preciso configurar o lombok e o servidor.

1. Na configuração do servidor, selecione:
- Jboss > Local.
- No Application Server, configure a pasta do servidor ex: `c:\wildfly\`
- Na aba `Deployment` adicione um `Artifact ( + )` que será o projeto `nome_do_projeto-war:exploded` (algo parecido com esse nome)
- Na aba `Startup/Connection` sobrescreva para >> TODOS <<  os `"starts"` da aplicação o `startup script`
e adicione isso: `c:\wildfly\bin\standalone.bat  -c standalone-full.xml` (não esqueça que é necessario seguir o caminho do seu servidor no seu computador)

##Comandos básicos Maven
- Para gerar os arquivos do build do projeto: `mvn clean install -DSkipTests`


