------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 22/02/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE PAGAMENTO_MENSALIDADE ADD TIPO VARCHAR(2) NOT NULL;
ALTER TABLE PAGAMENTO_MENSALIDADE_AUD ADD TIPO VARCHAR(2) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 02/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE MODULO_PACOTE ADD DATA_VENCIMENTO DATE NULL
ALTER TABLE MODULO_PACOTE_AUD ADD DATA_VENCIMENTO DATE NULL
ALTER TABLE MODULO_PACOTE ADD DESCRICAO VARCHAR(5000) NULL
ALTER TABLE MODULO_PACOTE_AUD ADD DESCRICAO VARCHAR(5000) NULL;

CREATE TABLE MODULO_PACOTE_CATEGORIA_EMPRESA (
    ID INT IDENTITY(1,1) NOT NULL,
    ID_MODULO_PACOTE INTEGER NOT NULL,
    ID_CATEGORIA_EMPRESA INTEGER NOT NULL,
    CONSTRAINT PK_MODULO_PACOTE_CATEGORIA_EMPRESA PRIMARY KEY (ID),
    CONSTRAINT FK_MODULO_PACOTE_CATEGORIA_EMPRESA_MODULO_PACOTE FOREIGN KEY (ID_MODULO_PACOTE) REFERENCES MODULO_PACOTE (ID),
    CONSTRAINT FK_MODULO_PACOTE_CATEGORIA_EMPRESA_CATEGORIA_EMPRESA FOREIGN KEY (ID_CATEGORIA_EMPRESA) REFERENCES CATEGORIA_EMPRESA (ID)
)

CREATE TABLE MODULO_PACOTE_CATEGORIA_EMPRESA_AUD (
    ID INT NOT NULL,REV int NOT NULL,REVTYPE smallint,
    ID_MODULO_PACOTE INTEGER NULL,
    ID_CATEGORIA_EMPRESA INTEGER NULL,
    CONSTRAINT PK_MODULO_PACOTE_CATEGORIA_EMPRESA_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_MODULO_PACOTE_CATEGORIA_EMPRESA_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE)
)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 04/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE PAGAMENTO_MENSALIDADE ADD ID_USUARIO_GERACAO INTEGER NOT NULL DEFAULT 1
ALTER TABLE PAGAMENTO_MENSALIDADE ADD ID_USUARIO_GERACAO_PAGAMENTO INTEGER NULL DEFAULT 1
ALTER TABLE PAGAMENTO_MENSALIDADE_AUD ADD ID_USUARIO_GERACAO INTEGER NULL
ALTER TABLE PAGAMENTO_MENSALIDADE_AUD ADD ID_USUARIO_GERACAO_PAGAMENTO INTEGER NULL;
ALTER TABLE PAGAMENTO_MENSALIDADE ADD CONSTRAINT FK_PAGAMENTO_MENSALIDADE_USUARIO_GERACAO FOREIGN KEY (ID_USUARIO_GERACAO) REFERENCES USUARIO (ID)
ALTER TABLE PAGAMENTO_MENSALIDADE ADD CONSTRAINT FK_PAGAMENTO_MENSALIDADE_USUARIO_GERACAO_PAGAMENTO FOREIGN KEY (ID_USUARIO_GERACAO_PAGAMENTO) REFERENCES USUARIO (ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 08/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE PAGAMENTO_MENSALIDADE ADD ID_MODULO_PACOTE INTEGER NULL
ALTER TABLE PAGAMENTO_MENSALIDADE_AUD ADD ID_MODULO_PACOTE INTEGER NULL;
ALTER TABLE PAGAMENTO_MENSALIDADE ADD CONSTRAINT FK_PAGAMENTO_MENSALIDADE_MODULO_PACOTE FOREIGN KEY (ID_MODULO_PACOTE) REFERENCES MODULO_PACOTE (ID);

DELETE FROM USUARIO_ACESSO_RAPIDO
DELETE FROM ACESSO_RAPIDO;
INSERT INTO "ACESSO_RAPIDO" ("DESCRICAO", "ICONE", "LINK") VALUES
	('Contas a pagar', 'fa-download', 'contaPagar'),
	('Contas a receber', 'fa-upload', 'contaReceber'),
	('Venda', 'fa-shopping-basket', 'venda'),
	('Orçamento', 'fa-line-chart', 'orcamento'),
	('Ordem de serviço', 'fa-pencil-square', 'os/listaOrdemDeServico.xhtml'),
	('Produtos', 'fa-product-hunt', 'produto'),
	('Serviços', 'fa-wrench', 'servico'),
	('NFe', 'fa-file-text-o', 'notaFiscalProduto'),
	('NFSe', 'fa-file-text', 'notaFiscalServico'),
	('Cliente', 'fa-male', 'cliente'),
	('Contrato de cliente', 'fa-wpforms', 'contratoCliente'),
	('Contrato de fornecedor', 'fa-wpforms', 'contratoFornecedor');

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 11/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE INTROJS_CONFIG (
    ID INT IDENTITY(1,1) NOT NULL,
    PAGE_ID VARCHAR(100) NOT NULL,
    FORCA_EXIBICAO VARCHAR(1) NOT NULL DEFAULT 'N',
    REVISAO NUMERIC NOT NULL,
    JS TEXT,
    CONSTRAINT PK_INTROJS_CONFIG PRIMARY KEY (ID),
)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 15/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA
ADD DATA_EMISSAO DATE NULL

ALTER TABLE CONTA_AUD
ADD DATA_EMISSAO DATE NULL

ALTER TABLE CENTRO_CUSTO
ADD CNPJ VARCHAR(20) NULL

ALTER TABLE CENTRO_CUSTO_AUD
ADD CNPJ VARCHAR(20) NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 18/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE ADD SALDO_ATUAL NUMERIC(10,2)
ALTER TABLE CLIENTE ADD SALDO_INICIAL NUMERIC(10,2)
ALTER TABLE CLIENTE_AUD ADD SALDO_ATUAL NUMERIC(10,2)
ALTER TABLE CLIENTE_AUD ADD SALDO_INICIAL NUMERIC(10,2);

CREATE TABLE CLIENTE_MOVIMENTACAO (
    ID INT IDENTITY(1,1) NOT NULL,
    TENAT_ID INT NOT NULL,
    ID_CLIENTE INT NOT NULL,
    ORIGEM VARCHAR(10) NOT NULL,
    DATA_MOVIMENTACAO DATE NOT NULL,
    DATA_VENCIMENTO DATE NOT NULL,
    DATA_PAGAMENTO DATE,
    VALOR_PREVISTO NUMERIC(10,2) NOT NULL,
    VALOR_JUROS NUMERIC(10,2),
    VALOR_MULTAS NUMERIC(10,2),
    VALOR_PAGO NUMERIC(10,2),
    CONSTRAINT PK_CLIENTE_MOVIMENTACAO PRIMARY KEY (ID),
    CONSTRAINT FK_CLIENTE_MOVIMENTACAO_ID_CLIENTE FOREIGN KEY (ID_CLIENTE) REFERENCES CLIENTE (ID)
)

CREATE TABLE CLIENTE_MOVIMENTACAO_AUD (
    ID INT NOT NULL,REV int NOT NULL,REVTYPE smallint,
    TENAT_ID INT NULL,
    ID_CLIENTE INT NULL,
    ORIGEM VARCHAR(10) NULL,
    DATA_MOVIMENTACAO DATE NULL,
    DATA_VENCIMENTO DATE NULL,
    DATA_PAGAMENTO DATE NULL,
    VALOR_PREVISTO NUMERIC(10,2) NULL,
    VALOR_JUROS NUMERIC(10,2) NULL,
    VALOR_MULTAS NUMERIC(10,2) NULL,
    VALOR_PAGO NUMERIC(10,2) NULL,
    CONSTRAINT PK_CLIENTE_MOVIMENTACAO_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_CLIENTE_MOVIMENTACAO_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE)
)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 22/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE VENDA_PRODUTO ADD FORNECIDO_TERCEIRO VARCHAR(1) NULL
ALTER TABLE VENDA_SERVICO ADD FORNECIDO_TERCEIRO VARCHAR(1) NULL
ALTER TABLE VENDA_PRODUTO_AUD ADD FORNECIDO_TERCEIRO VARCHAR(1) NULL
ALTER TABLE VENDA_SERVICO_AUD ADD FORNECIDO_TERCEIRO VARCHAR(1) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 23/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CENTRO_CUSTO ADD TOKEN VARCHAR(40) NULL
ALTER TABLE CENTRO_CUSTO_AUD ADD TOKEN VARCHAR(40) NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 24/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_MOVIMENTACAO ADD ID_INTEGRACAO VARCHAR(50) NULL
ALTER TABLE CLIENTE_MOVIMENTACAO ADD DATA_CANCELAMENTO DATE NULL
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD ID_INTEGRACAO VARCHAR(50) NULL
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD DATA_CANCELAMENTO DATE NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 24/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE ADD ID_INTEGRACAO INT NULL
ALTER TABLE CLIENTE_AUD ADD ID_INTEGRACAO INT NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 30/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_MOVIMENTACAO ADD VALOR_SALDO numeric(10,2) NOT NULL
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD VALOR_SALDO numeric(10,2) NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 31/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CENTRO_CUSTO ADD ID_CONTA_BANCARIA int null
ALTER TABLE CENTRO_CUSTO_AUD ADD ID_CONTA_BANCARIA int null;
ALTER TABLE CENTRO_CUSTO ADD CONSTRAINT FK_CENTRO_CUSTO_CONTA_BANCARIA FOREIGN KEY (ID_CONTA_BANCARIA) REFERENCES CONTA_BANCARIA (ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 01/04/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA_PARCELA ALTER COLUMN NUM_NF VARCHAR(70) NULL
ALTER TABLE CONTA_PARCELA_AUD ALTER COLUMN NUM_NF VARCHAR(70) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 30/03/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_MOVIMENTACAO ADD STATUS VARCHAR(20)  NULL
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD STATUS VARCHAR(20) NULL
ALTER TABLE CLIENTE_MOVIMENTACAO ADD ID_CENTRO_CUSTO INT NULL
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD ID_CENTRO_CUSTO INT NULL
ALTER TABLE CONTA_PARCELA ADD TARIFA NUMERIC(10,2)  NULL
ALTER TABLE CONTA_PARCELA_AUD ADD TARIFA NUMERIC(10,2) NULL
ALTER TABLE CONTA ADD TARIFA NUMERIC(10,2) NULL
ALTER TABLE CONTA_AUD ADD TARIFA NUMERIC(10,2) NULL;

ALTER TABLE CLIENTE_MOVIMENTACAO ADD CONSTRAINT FK_CLIENTE_MOVIMENTACAO_CENTRO_CUSTO FOREIGN KEY (ID_CENTRO_CUSTO) REFERENCES CENTRO_CUSTO (ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 06/04/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA ADD ID_CLIENTE_MOVIMENTACAO INT NULL
ALTER TABLE CONTA_AUD ADD ID_CLIENTE_MOVIMENTACAO INT NULL

ALTER TABLE CONTA ADD CONSTRAINT FK_CONTA_CLIENTE_MOVIMENTACAO FOREIGN KEY (ID_CLIENTE_MOVIMENTACAO) REFERENCES CLIENTE_MOVIMENTACAO (ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 09/04/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE VENDA ADD KM NUMERIC(10,2) NULL
ALTER TABLE VENDA_AUD ADD KM NUMERIC(10,2) NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 15/04/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE USUARIO ADD PODE_MUDAR_PRECO_UNITARIO_VENDA VARCHAR(1) NOT NULL DEFAULT 'N'
ALTER TABLE USUARIO_AUD ADD PODE_MUDAR_PRECO_UNITARIO_VENDA VARCHAR(1) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 15/04/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_MOVIMENTACAO ADD VALOR_SALDO_ANTERIOR NUMERIC(10,2) NULL
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD VALOR_SALDO_ANTERIOR NUMERIC(10,2) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 15/04/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_MOVIMENTACAO ADD VALOR_TAXA NUMERIC(10,2) NULL
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD VALOR_TAXA NUMERIC(10,2) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 27/04/21
------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE INDEX INDEX_CONTA_PAI ON PLANO_CONTA (ID_CONTA_PAI)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel
-- Data: 05/05/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_MOVIMENTACAO_AUD  ADD  VALOR_TAXA NUMERIC(10,2) NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 10/05/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE PARAMETRO_GERAL ADD MEIO_PAGAMENTO_PADRAO VARCHAR(10)
ALTER TABLE PARAMETRO_GERAL_AUD ADD MEIO_PAGAMENTO_PADRAO VARCHAR(10) NULL
ALTER TABLE PAGAMENTO_MENSALIDADE ADD REFERENCIA_FITPAG VARCHAR(20) NULL
ALTER TABLE PAGAMENTO_MENSALIDADE_AUD ADD REFERENCIA_FITPAG VARCHAR(20) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 18/05/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CENTRO_CUSTO ADD ID_PLANO_CONTA_IUGU INT NULL
ALTER TABLE CENTRO_CUSTO_AUD ADD ID_PLANO_CONTA_IUGU INT NULL
ALTER TABLE CENTRO_CUSTO ADD ID_PLANO_CONTA_REDE INT NULL
ALTER TABLE CENTRO_CUSTO_AUD ADD ID_PLANO_CONTA_REDE INT NULL
ALTER TABLE CENTRO_CUSTO ADD ID_PLANO_CONTA_DINHEEIRO INT NULL
ALTER TABLE CENTRO_CUSTO_AUD ADD ID_PLANO_CONTA_DINHEEIRO INT NULL
ALTER TABLE CENTRO_CUSTO ADD ID_PLANO_CONTA_OUTROS INT NULL
ALTER TABLE CENTRO_CUSTO_AUD ADD ID_PLANO_CONTA_OUTROS INT NULL;

ALTER TABLE CENTRO_CUSTO ADD CONSTRAINT FK_CENTRO_CUSTO_PLANO_CONTA_IUGU FOREIGN KEY (ID_PLANO_CONTA_IUGU) REFERENCES PLANO_CONTA (ID)
ALTER TABLE CENTRO_CUSTO ADD CONSTRAINT FK_CENTRO_CUSTO_PLANO_CONTA_REDE FOREIGN KEY (ID_PLANO_CONTA_REDE) REFERENCES PLANO_CONTA (ID)
ALTER TABLE CENTRO_CUSTO ADD CONSTRAINT FK_CENTRO_CUSTO_PLANO_CONTA_DINHEEIRO FOREIGN KEY (ID_PLANO_CONTA_DINHEEIRO) REFERENCES PLANO_CONTA (ID)
ALTER TABLE CENTRO_CUSTO ADD CONSTRAINT FK_CENTRO_CUSTO_PLANO_CONTA_OUTROS FOREIGN KEY (ID_PLANO_CONTA_OUTROS) REFERENCES PLANO_CONTA (ID);

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 27/05/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE INTEGRACAO_BANCARIA ADD BANCO VARCHAR(50) NULL
ALTER TABLE INTEGRACAO_BANCARIA_AUD ADD BANCO VARCHAR(50) NULL


------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 31/05/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE USUARIO ADD RECEBE_EMAIL_IUGU VARCHAR(1) NOT NULL DEFAULT 'N'
ALTER TABLE USUARIO_AUD ADD RECEBE_EMAIL_IUGU VARCHAR(1) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 01/06/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_MOVIMENTACAO ADD TIPO_MOVIMENTACAO VARCHAR(30) NULL;
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD TIPO_MOVIMENTACAO VARCHAR(30) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 08/06/21
------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE VERSAO_SISTEMA (
    ID INT IDENTITY(1,1) NOT NULL,
    VERSAO VARCHAR(10) NOT NULL,
    DATA_INCLUSAO DATE NOT NULL,
    DESCRICAO TEXT NOT NULL,
    CONSTRAINT PK_VERSAO_SISTEMA PRIMARY KEY (ID),
);

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 08/06/21
------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO "PERMISSAO" ("DESCRICAO", "DESCRICAO_DETALHADA") VALUES ('CLIENTE_MOVIMENTACAO_EXCLUIR', 'Excluir uma movimentação de cliente'),
    ('CLIENTE_MOVIMENTACAO_SOLICITAR_EXCLUIR', 'Solicitar a exclusão de uma movimentação de cliente');
ALTER TABLE CLIENTE_MOVIMENTACAO ADD ATIVO VARCHAR(1) NOT NULL DEFAULT 'S'
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD ATIVO VARCHAR(1) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 14/06/21
------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE CLIENTE_MOVIMENTACAO_ALTERACAO (
    ID INT IDENTITY(1,1) NOT NULL,
    TENAT_ID INT NOT NULL,
    ID_CLIENTE_MOVIMENTACAO INT NOT NULL,
    ID_USUARIO_SOLICITACAO INT NOT NULL,
    ID_USUARIO_FINALIZACAO INT NULL,
    DATA_ALTERACAO DATE NOT NULL,
    DATA_FINALIZACAO DATE,
    TIPO VARCHAR(5) NOT NULL,
    STATUS VARCHAR(5) NOT NULL,
    CONSTRAINT PK_CLIENTE_MOVIMENTACAO_ALTERACAO PRIMARY KEY (ID),
    CONSTRAINT FK_VERSAO_SISTEMA_USUARIO_SOLICITACAO FOREIGN KEY (ID_USUARIO_SOLICITACAO) REFERENCES USUARIO (ID),
    CONSTRAINT FK_VERSAO_SISTEMA_USUARIO_FINALIZACAO FOREIGN KEY (ID_USUARIO_FINALIZACAO) REFERENCES USUARIO (ID),
    CONSTRAINT FK_VERSAO_SISTEMA_CLIENTE_MOVIMENTACAO FOREIGN KEY (ID_CLIENTE_MOVIMENTACAO) REFERENCES CLIENTE_MOVIMENTACAO (ID)
)

CREATE TABLE CLIENTE_MOVIMENTACAO_ALTERACAO_AUD (
    ID INT NOT NULL,REV int NOT NULL,REVTYPE smallint,
    TENAT_ID INT NULL,
    ID_USUARIO_SOLICITACAO INT NULL,
    ID_USUARIO_FINALIZACAO INT NULL,
    ID_CLIENTE_MOVIMENTACAO INT NULL,
    DATA_ALTERACAO DATE NOT NULL,
    DATA_FINALIZACAO DATE NULL,
    TIPO VARCHAR(5) NULL,
    STATUS VARCHAR(5) NULL,
    CONSTRAINT PK_CLIENTE_MOVIMENTACAO_ALTERACAO_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_CLIENTE_MOVIMENTACAO_ALTERACAO_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE)
);

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 22/06/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_MOVIMENTACAO ADD QUANTIDADE_PARCELAS INT NULL;
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD QUANTIDADE_PARCELAS INT NULL;
ALTER TABLE CLIENTE_MOVIMENTACAO ADD DATA_LIQUIDACAO DATE NULL;
ALTER TABLE CLIENTE_MOVIMENTACAO_AUD ADD DATA_LIQUIDACAO DATE NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 30/06/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA_PARCELA ADD DATA_EMISSAO DATE NULL;
ALTER TABLE CONTA_PARCELA_AUD ADD DATA_EMISSAO DATE NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 14/06/21
------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE USUARIO_LEITURA_TERMO (
    ID INT IDENTITY(1,1) NOT NULL,
    ID_USUARIO INT NOT NULL,
    VERSAO_TERMO INT NOT NULL,
    DATA_ACEITE DATETIME NOT NULL,
    IP VARCHAR(50) NOT NULL,
    CONSTRAINT PK_USUARIO_LEITURA_TERMO PRIMARY KEY (ID),
    CONSTRAINT FK_USUARIO_LEITURA_TERMO_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID)
)

CREATE TABLE USUARIO_LEITURA_TERMO_AUD (
    ID INT NOT NULL,REV int NOT NULL,REVTYPE smallint,
    ID_USUARIO INT NULL,
    VERSAO_TERMO INT NULL,
    DATA_ACEITE DATETIME NULL,
    IP VARCHAR(50) NULL,
    CONSTRAINT PK_USUARIO_LEITURA_TERMO_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_USUARIO_LEITURA_TERMO_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE)
);

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 26/07/21
------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO "PERMISSAO" ("DESCRICAO", "DESCRICAO_DETALHADA") VALUES ('SOLICITACAO_GERENCIAR', 'Gerenciar solicitações de cadastro de cliente'),
                                                                    ('SOLICITACAO_VISUALIZAR', 'Visualizar solicitações de cadsatro de usuário');

CREATE TABLE SOLICITACAO_CADASTRO_CLIENTE (
    ID INT IDENTITY(1,1) NOT NULL,
    TENAT_ID INT NOT NULL,
    ID_CLIENTE INT NULL,
    NOME VARCHAR(60) NOT NULL,
    CPFCNPJ VARCHAR(20) NOT NULL,
    CNH VARCHAR(20) NOT NULL,
    CATEGORIA_CNH VARCHAR(5) NOT NULL,
    CELULAR VARCHAR(20) NOT NULL,
    EMAIL VARCHAR(30) NOT NULL,
    ID_CIDADE INT NOT NULL,
    CEP VARCHAR(15) NOT NULL,
    ENDERECO VARCHAR(200) NOT NULL,
    NUMERO VARCHAR(10) NOT NULL,
    BAIRRO VARCHAR(50) NOT NULL,
    COMPLEMENTO VARCHAR(100) NULL,
    CONSTRAINT PK_SOLICITACAO_CADASTRO_CLIENTE PRIMARY KEY (ID),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_CLIENTE FOREIGN KEY (ID_CLIENTE) REFERENCES CLIENTE (ID),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_CIDADE FOREIGN KEY (ID_CIDADE) REFERENCES CIDADE (ID)
)

CREATE TABLE SOLICITACAO_CADASTRO_CLIENTE_VEICULO (
    ID INT IDENTITY(1,1) NOT NULL,
    TENAT_ID INT NOT NULL,
    ID_SOLICITACAO_CADASTRO_CLIENTE INT NOT NULL,
    ID_MODELO_INFORMACAO INT NOT NULL,
    ID_DOCUMENTO INT NOT NULL,
    ID_COR_VEICULO INT NOT NULL,
    ID_COMBUSTIVEL INT NOT NULL,
    ANO_FABRICACAO INT NOT NULL,
    PLACA VARCHAR(10) NOT NULL,
    CAMBIO VARCHAR(15) NOT NULL,
    NRO_PORTAS INT NOT NULL,
    NRO_PASSAGEIROS INT NOT NULL,
    RENAVAM VARCHAR(30) NOT NULL,
    CHASSI VARCHAR(30) NOT NULL,
    VALOR_FIPE NUMERIC NOT NULL,
    STAUS VARCHAR(1) NOT NULL,
    DATA_MODIFICACAO DATETIME NOT NULL,
    OBSERVACAO VARCHAR(100) NULL,
    CONSTRAINT PK_SOLICITACAO_CADASTRO_CLIENTE_VEICULO PRIMARY KEY (ID),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_VEICULO_SOLICITACAO_CADASTRO_CLIENTE FOREIGN KEY (ID_SOLICITACAO_CADASTRO_CLIENTE) REFERENCES SOLICITACAO_CADASTRO_CLIENTE (ID),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_VEICULO_MODELO_INFORMACAO FOREIGN KEY (ID_MODELO_INFORMACAO) REFERENCES MODELO_INFORMACAO (ID),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_VEICULO_DOCUMENTO FOREIGN KEY (ID_DOCUMENTO) REFERENCES DOCUMENTO (ID),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_VEICULO_COR_VEICULO FOREIGN KEY (ID_COR_VEICULO) REFERENCES COR_VEICULO (ID),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_VEICULO_COMBUSTIVEL FOREIGN KEY (ID_COMBUSTIVEL) REFERENCES COMBUSTIVEL (ID)
)

CREATE TABLE SOLICITACAO_CADASTRO_CLIENTE_AUD (
    ID INT NOT NULL,REV int NOT NULL,REVTYPE smallint,
    TENAT_ID INT NULL,
    ID_CLIENTE INT NULL,
    NOME VARCHAR(60) NULL,
    CPFCNPJ VARCHAR(20) NULL,
    CNH VARCHAR(20) NULL,
    CATEGORIA_CNH VARCHAR(5) NULL,
    CELULAR VARCHAR(20) NULL,
    EMAIL VARCHAR(30) NULL,
    ID_CIDADE INT NULL,
    CEP VARCHAR(15) NULL,
    ENDERECO VARCHAR(200) NULL,
    NUMERO VARCHAR(10) NULL,
    BAIRRO VARCHAR(50) NULL,
    COMPLEMENTO VARCHAR(100) NULL,
    CONSTRAINT PK_SOLICITACAO_CADASTRO_CLIENTE_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE)
)

CREATE TABLE SOLICITACAO_CADASTRO_CLIENTE_VEICULO_AUD (
    ID INT NOT NULL,REV int NOT NULL,REVTYPE smallint,
    TENAT_ID INT NULL,
    ID_SOLICITACAO_CADASTRO_CLIENTE INT NULL,
    ID_MODELO_INFORMACAO INT NULL,
    ID_DOCUMENTO INT NULL,
    ID_COR_VEICULO INT NULL,
    ID_COMBUSTIVEL INT NULL,
    ANO_FABRICACAO INT NULL,
    PLACA VARCHAR(10) NULL,
    CAMBIO VARCHAR(15) NULL,
    NRO_PORTAS INT NULL,
    NRO_PASSAGEIROS INT NULL,
    RENAVAM VARCHAR(30) NULL,
    CHASSI VARCHAR(30) NULL,
    VALOR_FIPE NUMERIC NULL,
    STAUS VARCHAR(1) NULL,
    DATA_MODIFICACAO DATETIME NULL,
    OBSERVACAO VARCHAR(100) NULL,
    CONSTRAINT PK_SOLICITACAO_CADASTRO_CLIENTE_VEICULO_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_SOLICITACAO_CADASTRO_CLIENTE_VEICULO_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE)
);

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 06/08/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE USUARIO ADD RECEBE_EMAIL_ACESSO_EMPRESA VARCHAR(1) NOT NULL DEFAULT 'N'
ALTER TABLE USUARIO_AUD ADD RECEBE_EMAIL_ACESSO_EMPRESA VARCHAR(1) NULL;

ALTER TABLE EMPRESA_USUARIO_ACESSO ADD ORIGEM VARCHAR(10) NOT NULL DEFAULT '-'
ALTER TABLE EMPRESA_USUARIO_ACESSO_AUD ADD ORIGEM VARCHAR(10) NULL;

ALTER TABLE MODELO ADD TIPO VARCHAR(10) NOT NULL DEFAULT 'carros'
ALTER TABLE MODELO_INFORMACAO ADD TIPO VARCHAR(10) NOT NULL DEFAULT 'carros'
ALTER TABLE MODELO_AUD ADD TIPO VARCHAR(10) NULL
ALTER TABLE MODELO_INFORMACAO_AUD ADD TIPO VARCHAR(10) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 06/08/21
------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA (
    ID INT IDENTITY(1,1) NOT NULL,
    TENAT_ID INT NOT NULL,
    ID_TRANSACAO_INTEGRACAO_BANCARIA INT NOT NULL,
    ID_CONTA_PARCELA INT NOT NULL,
    CONSTRAINT PK_TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA PRIMARY KEY (ID),
    CONSTRAINT FK_TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA_TRANSACAO_INTEGRACAO_BANCARIA FOREIGN KEY (ID_TRANSACAO_INTEGRACAO_BANCARIA) REFERENCES TRANSACAO_INTEGRACAO_BANCARIA (ID),
    CONSTRAINT FK_TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA_CONTA_PARCELA FOREIGN KEY (ID_CONTA_PARCELA) REFERENCES CONTA_PARCELA (ID)
)
CREATE TABLE TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA_AUD (
    ID INT NOT NULL,REV int NOT NULL,REVTYPE smallint,
    TENAT_ID INT NULL,
    ID_TRANSACAO_INTEGRACAO_BANCARIA INT NULL,
    ID_CONTA_PARCELA INT NULL,
    CONSTRAINT PK_TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE)
);

INSERT INTO TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA (TENAT_ID, ID_TRANSACAO_INTEGRACAO_BANCARIA, ID_CONTA_PARCELA)
SELECT TENAT_ID, ID, ID_CONTA_PARCELA
FROM TRANSACAO_INTEGRACAO_BANCARIA
WHERE ID_CONTA_PARCELA IS NOT NULL;

ALTER TABLE TRANSACAO_INTEGRACAO_BANCARIA DROP CONSTRAINT IF EXISTS FK_TRANSACAO_CONTA_PARCELA;
ALTER TABLE TRANSACAO_INTEGRACAO_BANCARIA DROP COLUMN ID_CONTA_PARCELA
ALTER TABLE TRANSACAO_INTEGRACAO_BANCARIA_AUD DROP COLUMN ID_CONTA_PARCELA;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 31/08/21
------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO "PERMISSAO" ("DESCRICAO", "DESCRICAO_DETALHADA") VALUES ('DASHBOARD_VIEW', 'Acessar a dashboard')


------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 01/09/21
------------------------------------------------------------------------------------------------------------------------------------------------------
ALTER TABLE TRANSACAO_INTEGRACAO_BANCARIA ADD  VALOR_CONCILIADO NUMERIC (10,2);
ALTER TABLE TRANSACAO_INTEGRACAO_BANCARIA_AUD ADD  VALOR_CONCILIADO NUMERIC (10,2);

ALTER TABLE TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA ADD  PROCESSADO VARCHAR (1);
ALTER TABLE TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA_AUD ADD  PROCESSADO VARCHAR (1);

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel Ribeiro
-- Data: 01/09/21
------------------------------------------------------------------------------------------------------------------------------------------------------
ALTER TABLE PREVISAO_ORCAMENTARIA ADD  ID_CENTRO_CUSTO INT NULL;
ALTER TABLE PREVISAO_ORCAMENTARIA_AUD ADD  ID_CENTRO_CUSTO INT NULL;

ALTER TABLE PREVISAO_ORCAMENTARIA ADD CONSTRAINT FK_PREVISAO_ORCAMENTARIA_CENTRO_CUSTO FOREIGN KEY (ID_CENTRO_CUSTO) REFERENCES CENTRO_CUSTO (ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 17/11/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE SOLICITACAO_CADASTRO_CLIENTE_VEICULO ADD MOTIVO_CANCELAMENTO VARCHAR(500) NULL;
ALTER TABLE SOLICITACAO_CADASTRO_CLIENTE_VEICULO_AUD ADD MOTIVO_CANCELAMENTO VARCHAR(500) NULL;

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 22/11/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE ALTER COLUMN CATEGORIA_CNH VARCHAR(3)
ALTER TABLE CLIENTE_AUD ALTER COLUMN CATEGORIA_CNH VARCHAR(3);

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel
-- Data: 23/11/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE MARCA ALTER COLUMN FIPE_ORDER VARCHAR(50)
ALTER TABLE MARCA_AUD ALTER COLUMN FIPE_ORDER VARCHAR(50)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Luiz Otávio
-- Data: 245/11/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE SOLICITACAO_CADASTRO_CLIENTE ALTER COLUMN EMAIL VARCHAR(70)
ALTER TABLE SOLICITACAO_CADASTRO_CLIENTE_AUD ALTER COLUMN EMAIL VARCHAR(70)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 25/11/21
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE SOLICITACAO_CADASTRO_CLIENTE ALTER COLUMN EMAIL VARCHAR(100)
ALTER TABLE SOLICITACAO_CADASTRO_CLIENTE_AUD ALTER COLUMN EMAIL VARCHAR(100);

ALTER TABLE SOLICITACAO_CADASTRO_CLIENTE_VEICULO ALTER COLUMN OBSERVACAO VARCHAR(200)
ALTER TABLE SOLICITACAO_CADASTRO_CLIENTE_VEICULO_AUD ALTER COLUMN OBSERVACAO VARCHAR(200);

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 13/12/21
------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO NCM ("codigo", "descricao", "tipo") VALUES
    ('00', 'Mercadoria ou outra operação que não possa ser classificada segundo a tabela da NCM', '')

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 10/01/22
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CFOP ADD INDICADOR_SERVICO VARCHAR(1) NOT NULL DEFAULT 'N';
ALTER TABLE CFOP_AUD ADD INDICADOR_SERVICO VARCHAR(1);
UPDATE CFOP SET INDICADOR_SERVICO = 'S' WHERE CODIGO IN ('6933', '5933');
UPDATE CFOP SET INDICADOR_NFE = 'N' WHERE CODIGO IN ('6933', '5933');

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Emanuel
-- Data: 13/01/22
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA_BANCARIA ADD ID_CENTRO_CUSTO int null
ALTER TABLE CONTA_BANCARIA_AUD ADD ID_CENTRO_CUSTO int null;
ALTER TABLE CONTA_BANCARIA ADD CONSTRAINT FK_CONTA_BANCARIA_CENTRO_CUSTO FOREIGN KEY (ID_CENTRO_CUSTO) REFERENCES CENTRO_CUSTO (ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 11/01/22
------------------------------------------------------------------------------------------------------------------------------------------------------

UPDATE PLANO_CONTA_PADRAO SET codigo_pai = '3.2.1.06' WHERE codigo = '3.2.1.06.002';
UPDATE PLANO_CONTA_PADRAO SET codigo_pai = '3.1.1.01' WHERE codigo = '3.2.2.01.010';

UPDATE PC1
SET PC1.ID_CONTA_PAI = PC2.ID
FROM PLANO_CONTA PC1
JOIN PLANO_CONTA PC2 ON PC2.TENAT_ID = PC1.TENAT_ID
WHERE PC1.CODIGO = '3.2.1.06.002' AND PC2.CODIGO = '3.2.1.06';

UPDATE PC1
SET PC1.ID_CONTA_PAI = PC2.ID
FROM PLANO_CONTA PC1
JOIN PLANO_CONTA PC2 ON PC2.TENAT_ID = PC1.TENAT_ID
WHERE PC1.CODIGO = '3.2.2.01.010' AND PC2.CODIGO = '3.2.2.01';

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: Humberto
-- Data: 17/01/22
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE PARAMETRO_SISTEMA ADD NOTIFICACAO_SMS_ENVIAR VARCHAR(1) NOT NULL DEFAULT 'N';
ALTER TABLE PARAMETRO_SISTEMA ADD NOTIFICACAO_SMS_NUMEROS VARCHAR(100) NULL;
ALTER TABLE PARAMETRO_SISTEMA ADD PRAZO_NOTIFICACAO INT NULL;
ALTER TABLE PARAMETRO_SISTEMA_AUD ADD NOTIFICACAO_SMS_ENVIAR VARCHAR(1) NOT NULL DEFAULT 'N';
ALTER TABLE PARAMETRO_SISTEMA_AUD ADD NOTIFICACAO_SMS_NUMEROS VARCHAR(100) NULL;
ALTER TABLE PARAMETRO_SISTEMA_AUD ADD PRAZO_NOTIFICACAO INT NULL;

UPDATE PARAMETRO_SISTEMA SET PRAZO_NOTIFICACAO = 1 WHERE PRAZO_NOTIFICACAO IS NULL;
UPDATE PARAMETRO_SISTEMA SET NOTIFICACAO_SMS_NUMEROS = '' WHERE NOTIFICACAO_SMS_NUMEROS IS NULL;
