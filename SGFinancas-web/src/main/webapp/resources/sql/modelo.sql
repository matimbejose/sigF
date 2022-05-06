/*

    ATENÇÃO: AO CRIAR ALGO NESSE ARQUIVO, NÃO ESQUECER DE ATUALIZAR TAMBÉM NO 
    --->  Script criacao de tabelas.sql   <--- 

    APÓS ATUALIZAÇÃO DO MODELO DE DADOS E APLICAÇÃO DO SCRIPT EM PRODUÇÃO, 
    CHRISTOPHER IRÁ REMOVER O CONTEUDO DESSE ARQUIVO

*/

-- Sr Sarmento, Christopher
-- certificado digital
alter table empresa ADD TIPO_CERTIFICADO_DIGITAL VARCHAR(2)
alter table empresa_AUD ADD TIPO_CERTIFICADO_DIGITAL VARCHAR(2)
alter table empresa ADD NOME_CERTIFICADO_DIGITAL VARCHAR(300)
alter table empresa_AUD ADD NOME_CERTIFICADO_DIGITAL VARCHAR(300)

-------------------------------------
------PEDRO COTTA--------------------
------09/01/2017---------------------

CREATE TABLE CONTRATO ( 
	ID int identity(1,1)  NOT NULL,
	ID_CLIENTE int,
	ID_FORNECEDOR int,
	ID_DOCUMENTO int,
	TIPO_CONTRATO varchar(1),
	DATA_INICIO datetime NOT NULL,
	DATA_FIM datetime NOT NULL,
	OBSERVACAO varchar(2000),
	VALOR_CONTRATO numeric(10,2),
	TAXA_INSTALACAO numeric(10,2),
	TAXA_ADESAO numeric(10,2),
	TENAT_ID varchar(20) NOT NULL
)
;

ALTER TABLE CONTRATO ADD CONSTRAINT PK_CONTRATO 
	PRIMARY KEY CLUSTERED (ID)
;

ALTER TABLE CONTRATO ADD CONSTRAINT FK_CONTRATO_CLIENTE 
	FOREIGN KEY (ID_CLIENTE) REFERENCES CLIENTE (ID)
;

ALTER TABLE CONTRATO ADD CONSTRAINT FK_CONTRATO_DOCUMENTO 
	FOREIGN KEY (ID_DOCUMENTO) REFERENCES DOCUMENTO (ID)
;

ALTER TABLE CONTRATO ADD CONSTRAINT FK_CONTRATO_FORNECEDOR 
	FOREIGN KEY (ID_FORNECEDOR) REFERENCES FORNECEDOR (ID)
;

CREATE TABLE CONTRATO_AUD (
	ID int NOT NULL,REV int NOT NULL,REVTYPE smallint,
	ID_CLIENTE int,
	ID_FORNECEDOR int,
	ID_DOCUMENTO int,
	TIPO_CONTRATO varchar(1),
	DATA_INICIO datetime,
	DATA_FIM datetime,
	OBSERVACAO varchar(2000),
	VALOR_CONTRATO numeric(10,2),
	TAXA_INSTALACAO numeric(10,2),
	TAXA_ADESAO numeric(10,2),
        TENAT_ID INT,
    CONSTRAINT PK_CONTRATO_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_CONTRATO_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE) 
)

ALTER TABLE CONTA_PAGAR ADD
ID_CONTRATO INT 

ALTER TABLE CONTA_PAGAR_AUD ADD
ID_CONTRATO INT 

ALTER TABLE CONTA_RECEBER ADD
ID_CONTRATO INT 

ALTER TABLE CONTA_RECEBER_AUD ADD
ID_CONTRATO INT 

INSERT INTO FUNCAO_AJUDA 
VALUES('CAD_CONTRATO_ENTRADA', 'Informe os parametros')

----------------------------------------------------------------------------------------------------------------------------------------------------
------GUILHERME--------------------
------10/01/2017---------------------
----------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CSOSN 
ALTER COLUMN CODIGO varchar(3) NOT NULL

ALTER TABLE CSOSN 
ALTER COLUMN CODIGO varchar(3)

ALTER TABLE CST 
ALTER COLUMN CODIGO varchar(3) NOT NULL

ALTER TABLE CST 
ALTER COLUMN CODIGO varchar(3)

ALTER TABLE CFOP 
DROP COLUMN NUM_CFOP 

ALTER TABLE CFOP_AUD  
DROP COLUMN NUM_CFOP 

----------------------------------------------------------------------------------------------------------------------------------------------------
------PEDRO COTTA--------------------
------10/01/2017---------------------
----------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE PARAMETRO_SISTEMA ADD
ID_PLANO_CONTA_CONTA_BANCARIA INT NOT NULL

ALTER TABLE PARAMETRO_SISTEMA ADD
ID_PLANO_CONTA_CLIENTE INT NOT NULL

ALTER TABLE PARAMETRO_SISTEMA ADD
ID_PLANO_CONTA_FORNECEDOR INT NOT NULL

ALTER TABLE PARAMETRO_SISTEMA ADD
ID_PLANO_CONTA_PRODUTO INT NOT NULL

ALTER TABLE PARAMETRO_SISTEMA ADD
ID_PLANO_CONTA_SERVICO INT NOT NULL

ALTER TABLE PARAMETRO_SISTEMA ADD
ID_PLANO_CONTA_TRANSPORTADORA INT NOT NULL

ALTER TABLE PARAMETRO_SISTEMA_AUD ADD
ID_PLANO_CONTA_CONTA_BANCARIA INT

ALTER TABLE PARAMETRO_SISTEMA_AUD ADD
ID_PLANO_CONTA_CLIENTE INT 

ALTER TABLE PARAMETRO_SISTEMA_AUD ADD
ID_PLANO_CONTA_FORNECEDOR INT 

ALTER TABLE PARAMETRO_SISTEMA_AUD ADD
ID_PLANO_CONTA_PRODUTO INT 

ALTER TABLE PARAMETRO_SISTEMA_AUD ADD
ID_PLANO_CONTA_SERVICO INT 

ALTER TABLE PARAMETRO_SISTEMA_AUD ADD
ID_PLANO_CONTA_TRANSPORTADORA INT

----------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTRATO ADD
ID_CONTA_BANCARIA INT NOT NULL

ALTER TABLE CONTRATO_AUD ADD
ID_CONTA_BANCARIA INT 

ALTER TABLE CONTRATO ADD CONSTRAINT FK_CONTRATO_CONTA_BANCARIA 
	FOREIGN KEY (ID_CONTA_BANCARIA) REFERENCES CONTA_BANCARIA (ID)

----------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTRATO 
ADD ID_CONTA int NOT NULL

ALTER TABLE CONTRATO_AUD  
ADD ID_CONTA int

ALTER TABLE CONTRATO 
ADD CONSTRAINT FK_CONTRATO_CONTA FOREIGN KEY (ID_CONTA) REFERENCES CONTA (ID)

----------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTRATO 
ADD ID_PLANO_CONTA int NOT NULL

ALTER TABLE CONTRATO_AUD  
ADD ID_PLANO_CONTA int

ALTER TABLE CONTRATO 
ADD CONSTRAINT FK_CONTRATO_PLANO_CONTA FOREIGN KEY (ID_PLANO_CONTA) REFERENCES PLANO_CONTA (ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: HUGO
-- Data: 10/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE SERVICO 
ADD ID_PRODUTO_CATEGORIA INT NOT NULL

ALTER TABLE SERVICO 
ADD VALOR_VENDA NUMERIC(10,2) NOT NULL

ALTER TABLE SERVICO 
ADD CUSTO_SERVICO NUMERIC(10,2) NOT NULL

ALTER TABLE SERVICO
ADD TENAT_ID INT NOT NULL

ALTER TABLE SERVICO_AUD 
ADD ID_PRODUTO_CATEGORIA INT NULL

ALTER TABLE SERVICO_AUD 
ADD VALOR_VENDA NUMERIC(10,2) NULL

ALTER TABLE SERVICO_AUD 
ADD CUSTO_SERVICO NUMERIC(10,2) NULL

ALTER TABLE SERVICO_AUD
ADD TENAT_ID INT NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: PEDRO COTTA
-- Data: 11/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE FORNECEDOR ADD ID_PLANO_CONTA INT,
CONSTRAINT FK_FORNECEDOR_PLANO_CONTA FOREIGN KEY(ID_PLANO_CONTA) REFERENCES PLANO_CONTA (ID)

ALTER TABLE FORNECEDOR_AUD ADD ID_PLANO_CONTA INT


ALTER TABLE CLIENTE ADD ID_PLANO_CONTA INT,
CONSTRAINT FK_CLIENTE_PLANO_CONTA FOREIGN KEY(ID_PLANO_CONTA) REFERENCES PLANO_CONTA (ID)

ALTER TABLE CLIENTE_AUD ADD ID_PLANO_CONTA INT

ALTER TABLE PRODUTO ADD ID_PLANO_CONTA INT,
CONSTRAINT FK_PRODUTO_PLANO_CONTA FOREIGN KEY(ID_PLANO_CONTA) REFERENCES PLANO_CONTA (ID)

ALTER TABLE PRODUTO_AUD ADD ID_PLANO_CONTA INT

ALTER TABLE TRANSPORTADORA ADD ID_PLANO_CONTA INT,
CONSTRAINT FK_TRANSPORTADORA_PLANO_CONTA FOREIGN KEY(ID_PLANO_CONTA) REFERENCES PLANO_CONTA (ID)

ALTER TABLE TRANSPORTADORA_AUD ADD ID_PLANO_CONTA INT

ALTER TABLE SERVICO ADD ID_PLANO_CONTA INT,
CONSTRAINT FK_SERVICO_PLANO_CONTA FOREIGN KEY(ID_PLANO_CONTA) REFERENCES PLANO_CONTA (ID)

ALTER TABLE SERVICO_AUD ADD ID_PLANO_CONTA INT

ALTER TABLE CONTA_BANCARIA ADD ID_PLANO_CONTA INT,
CONSTRAINT FK_CONTA_BANCARIA_PLANO_CONTA FOREIGN KEY(ID_PLANO_CONTA) REFERENCES PLANO_CONTA (ID)

ALTER TABLE CONTA_BANCARIA_AUD ADD ID_PLANO_CONTA INT

ALTER TABLE SERVICO ALTER COLUMN
VALOR_VENDA NUMERIC(10,2)

ALTER TABLE SERVICO ALTER COLUMN
CUSTO_SERVICO NUMERIC(10,2)

ALTER TABLE CONTRATO ALTER COLUMN
VALOR_CONTRATO NUMERIC(10,2) NOT NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: GUILHERME 
-- Data: 11/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE CONTA (
	ID int IDENTITY(1,1) NOT NULL,
	ID_CLIENTE int null,
	ID_FORNECEDOR int null,
	ID_CONTA_BANCARIA int NOT NULL,
	ID_CENTRO_CUSTO int NULL,
	ID_DOCUMENTO int NULL,
	ID_PLANO_CONTA INT NOT NULL,
        ID_UNIDADE_OCUPACAO int,
	DESCRICAO varchar (200) NOT NULL,
	SITUACAO varchar (2) NOT NULL,
        NUMERO_PARCELAS INT not null,
	DATA_VENCIMENTO datetime NOT NULL,
	VALOR numeric (10, 2) NOT NULL,
        VALOR_TOTAL numeric (10, 2) NOT NULL,
	VALOR_PAGO numeric (10, 2) NULL,
	DATA_PAGAMENTO datetime NULL,
	DESCONTO numeric (10, 2) NULL,
	JUROS numeric (10, 2) NULL,
        MULTA numeric (10, 2) NULL,
	OUTROS_CUSTOS numeric (10, 2) NULL,
	ENCARGOS numeric (10, 2) NULL,
	OBSERVACAO varchar (500) NULL,
	REPETIR_CONTA varchar (1) NOT NULL,
	TIPO_REPETICAO varchar (1) NULL,
	QTD_REPETICAO int NULL,
	INFORMAR_PAGAMENTO varchar (1) NOT NULL,
        MOTIVO_CANCELAMENTO VARCHAR(500),
        DATA_CANCELAMENTO DATETIME,
        TIPO_CONTA varchar(1) NOT NULL,
	TIPO_TRANSACAO varchar(1) NOT NULL, 
        TENAT_ID INT NULL,
    CONSTRAINT PK_CONTA PRIMARY KEY (ID)
)

CREATE TABLE CONTA_AUD (
		ID int NOT NULL,REV int NOT NULL,REVTYPE smallint,
		ID_CLIENTE int,
		ID_FORNECEDOR int,
		ID_CONTA_BANCARIA int,
		ID_CENTRO_CUSTO int NULL,
		ID_DOCUMENTO int NULL,
		DESCRICAO varchar(200),
                ID_UNIDADE_OCUPACAO int,
                ID_PLANO_CONTA INT NULL,
                NUMERO_PARCELAS INT null,
                MOTIVO_CANCELAMENTO VARCHAR(500),
		SITUACAO varchar(2),
		DATA_VENCIMENTO datetime,
                DATA_PAGAMENTO datetime NULL,
		VALOR numeric(10, 2),
		VALOR_TOTAL numeric (10, 2), 
		VALOR_PAGO numeric(10, 2) NULL,
                DESCONTO numeric(10, 2) NULL,
		JUROS numeric(10, 2) NULL,
                MULTA numeric(10, 2) NULL,
		OUTROS_CUSTOS numeric(10, 2) NULL,
		ENCARGOS numeric (10, 2) NULL,
		OBSERVACAO varchar(500) NULL,
		REPETIR_CONTA varchar(1),
		TIPO_REPETICAO varchar(1) NULL,
		QTD_REPETICAO int NULL,
		INFORMAR_PAGAMENTO varchar(1),
                DATA_CANCELAMENTO DATETIME,
		TIPO_CONTA varchar(1),
		TIPO_TRANSACAO varchar(1), 
                TENAT_ID INT NULL,
        CONSTRAINT PK_CONTA_AUD PRIMARY KEY CLUSTERED (ID, REV),
        CONSTRAINT FK_CONTA_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE) 
)

ALTER TABLE CONTA ADD
    CONSTRAINT FK_CONTA_CENTRO_CUSTO FOREIGN KEY(ID_CENTRO_CUSTO) REFERENCES CENTRO_CUSTO (ID),
    CONSTRAINT FK_CONTA_CLIENTE FOREIGN KEY(ID_CLIENTE) REFERENCES CLIENTE (ID),
    CONSTRAINT FK_CONTA_FORNECEDOR FOREIGN KEY(ID_FORNECEDOR) REFERENCES FORNECEDOR (ID),
    CONSTRAINT FK_CONTA_CONTA_BANCARIA FOREIGN KEY(ID_CONTA_BANCARIA) REFERENCES CONTA_BANCARIA (ID),
    CONSTRAINT FK_CONTA_DOCUMENTO FOREIGN KEY(ID_DOCUMENTO) REFERENCES DOCUMENTO (ID),
    CONSTRAINT FK_CONTA_PLANO_CONTA FOREIGN KEY (ID_PLANO_CONTA) REFERENCES PLANO_CONTA(ID) ,
    CONSTRAINT FK_CONTA_UNIDADE_OCUPACAO FOREIGN KEY (ID_UNIDADE_OCUPACAO) REFERENCES UNIDADE_OCUPACAO(ID) 

-------------------------------------

CREATE TABLE CONTA_PARCELA (
	ID int IDENTITY(1,1) NOT NULL,
	ID_CONTA int NOT NULL,
	ID_DOCUMENTO int NULL,
	ID_CONTA_BANCARIA int NULL,
	SITUACAO varchar (2) NULL,
	DATA_VENCIMENTO datetime NOT NULL,
	VALOR numeric (10, 2) NOT NULL,
	VALOR_TOTAL numeric (10, 2) NOT NULL,
	OBSERVACAO varchar (50) NULL,
	DATA_PAGAMENTO datetime NULL,
	VALOR_PAGO numeric (10, 2) NULL,
	NUM_PARCELA int NULL,
        PAGAMENTO_PARCIAL VARCHAR(1),
        MOTIVO_CANCELAMENTO VARCHAR(500),
        RESPONSAVEL_CANCELAMENTO VARCHAR(300),
        DATA_CANCELAMENTO DATETIME,
	DESCONTO numeric (10, 2) NULL,
	JUROS numeric (10, 2) NULL,
	OUTROS_CUSTOS numeric (10, 2) NULL,
	MULTA numeric (10, 2) NULL,
	ENCARGOS numeric (10, 2) NULL,
        TENAT_ID INT NOT NULL,
    CONSTRAINT PK_CONTA_PARCELA PRIMARY KEY (ID)
)

CREATE TABLE CONTA_PARCELA_PARCIAL (
	ID int IDENTITY(1,1) NOT NULL,
	ID_CONTA_PARCELA int NULL,
	ID_DOCUMENTO int NULL,
	ID_CONTA_BANCARIA int NULL,
        ID_TIPO_PAGAMENTO int,
	DATA_PAGAMENTO datetime NULL,
        DATA_VENCIMENTO DATETIME, 
	VALOR_PAGO numeric (10, 2) NULL,
        TENAT_ID INT NOT NULL,
    CONSTRAINT PK_CONTA_PARCELA_PARCIAL PRIMARY KEY (ID)
)

CREATE TABLE CONTA_PARCELA_AUD (
    ID int NOT NULL,REV int NOT NULL,REVTYPE smallint,
    ID_CONTA int NULL,
    ID_DOCUMENTO int NULL,
    ID_CONTA_BANCARIA int NULL,
    SITUACAO varchar (2) NULL,
    DATA_VENCIMENTO datetime ,
    VALOR numeric (10, 2) ,
	VALOR_TOTAL numeric (10, 2) ,
    OBSERVACAO varchar (50) NULL,
    DATA_PAGAMENTO datetime NULL,
    VALOR_PAGO numeric (10, 2) NULL,
    NUM_PARCELA int NULL,
    PAGAMENTO_PARCIAL VARCHAR(1),
    MOTIVO_CANCELAMENTO VARCHAR(500),
    RESPONSAVEL_CANCELAMENTO VARCHAR(300),
    DATA_CANCELAMENTO DATETIME,
    DESCONTO numeric (10, 2) NULL,
    JUROS numeric (10, 2) NULL,
    OUTROS_CUSTOS numeric (10, 2) NULL,
    MULTA numeric (10, 2) NULL,
	ENCARGOS numeric (10, 2) NULL,
    TENAT_ID INT ,
    CONSTRAINT PK_CONTA_PARCELA_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_CONTA_PARCELA_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE) 
)

CREATE TABLE CONTA_PARCELA_PARCIAL_AUD (
	ID int NOT NULL,REV int NOT NULL,REVTYPE smallint,
	ID_CONTA_PARCELA int NULL,
	ID_DOCUMENTO int NULL,
	ID_CONTA_BANCARIA int NULL,
        ID_TIPO_PAGAMENTO int,
	DATA_PAGAMENTO datetime NULL,
        DATA_VENCIMENTO DATETIME, 
	VALOR_PAGO numeric (10, 2) NULL,
        TENAT_ID INT ,
    CONSTRAINT PK_CONT_PARCELA_PARCIAL_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_CONTA_PARCELA_PARCIAL_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE) 
)

ALTER TABLE CONTA_PARCELA ADD
CONSTRAINT FK_CONTA_PARCELA_CONTA_BANCARIA FOREIGN KEY(ID_CONTA_BANCARIA) REFERENCES CONTA_BANCARIA (ID),
CONSTRAINT FK_CONTA_PARCELA_CONTA_ FOREIGN KEY(ID_CONTA) REFERENCES CONTA (ID),
CONSTRAINT FK_CONTA_PARCELA_DOCUMENTO FOREIGN KEY(ID_DOCUMENTO) REFERENCES DOCUMENTO (ID)

ALTER TABLE CONTA_PARCELA_PARCIAL ADD
CONSTRAINT FK_CONTA_PARCELA_PARCIAL_CONTA_BANCARIA FOREIGN KEY(ID_CONTA_BANCARIA) REFERENCES CONTA_BANCARIA (ID),
CONSTRAINT FK_CONTA_PARCELA_PARCIAL_CONTA_PARCELA FOREIGN KEY(ID_CONTA_PARCELA) REFERENCES CONTA_PARCELA (ID),
CONSTRAINT FK_CONTA_PARCELA_PARCIAL_DOCUMENTO FOREIGN KEY(ID_DOCUMENTO) REFERENCES DOCUMENTO (ID),
CONSTRAINT FK_CONTA_PARCELA_PARCIAL_TIPO_PAGAMENTO FOREIGN KEY (ID_TIPO_PAGAMENTO) REFERENCES TIPO_PAGAMENTO(ID)

---------------------------------------


ALTER TABLE COMPRA 
ADD ID_CONTA int --NOT NULL 

ALTER TABLE COMPRA_AUD 
ADD ID_CONTA int 

ALTER TABLE COMPRA 
ADD CONSTRAINT FK_COMPRA_CONTA FOREIGN KEY (ID_CONTA) REFERENCES CONTA(ID)

--------------------------------------------------------------------------------

ALTER TABLE VENDA 
ADD ID_CONTA int --NOT NULL 

ALTER TABLE VENDA_AUD 
ADD ID_CONTA int 

ALTER TABLE VENDA 
ADD CONSTRAINT FK_VENDA_CONTA FOREIGN KEY (ID_CONTA) REFERENCES CONTA(ID)

---------------------------------------

ALTER TABLE EXTRATO_CONTA_CORRENTE 
ADD ID_CONTA_PARCELA int

ALTER TABLE EXTRATO_CONTA_CORRENTE_AUD
ADD ID_CONTA_PARCELA int

ALTER TABLE EXTRATO_CONTA_CORRENTE
ADD CONSTRAINT FK_EXTRATO_CONTA_PARCELA FOREIGN KEY (ID_CONTA_PARCELA) REFERENCES CONTA_PARCELA(ID) 

--

ALTER TABLE EXTRATO_CONTA_CORRENTE 
ADD ID_CONTA_PARCELA_PARCIAL int

ALTER TABLE EXTRATO_CONTA_CORRENTE_AUD
ADD ID_CONTA_PARCELA_PARCIAL int

ALTER TABLE EXTRATO_CONTA_CORRENTE
ADD CONSTRAINT FK_EXTRATO_CONTA_PARCELA_PARCIAL FOREIGN KEY (ID_CONTA_PARCELA_PARCIAL) REFERENCES CONTA_PARCELA_PARCIAL(ID) 

--------------------------------------------------------------------------------

ALTER TABLE CONTRATO 
ADD ID_CONTA int NOT NULL

ALTER TABLE CONTRATO_AUD  
ADD ID_CONTA int

ALTER TABLE CONTRATO 
ADD CONSTRAINT FK_CONTRATO_CONTA FOREIGN KEY (ID_CONTA) REFERENCES CONTA (ID)

--------------------------------------------------------------------------------
-- ATUALIZACAO REGISTROS CONTA
--------------------------------------------------------------------------------

-- CONTA --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA 
ADD ID_CONTA_PAGAR int 

SELECT * FROM CONTA;

update a set a.valor = VALOR_TOTAL from CONTA_PAGAR a where VALOR is null 

INSERT INTO 
	CONTA(ID_FORNECEDOR, ID_DOCUMENTO, ID_CENTRO_CUSTO, ID_CONTA_BANCARIA, DESCRICAO, DATA_VENCIMENTO, VALOR_TOTAL, SITUACAO, REPETIR_CONTA, INFORMAR_PAGAMENTO, DATA_PAGAMENTO, NUMERO_PARCELAS, TENAT_ID, ID_PLANO_CONTA, VALOR_PAGO, VALOR, TIPO_TRANSACAO, TIPO_CONTA, ID_CONTA_PAGAR)
SELECT 
	ID_FORNECEDOR, ID_DOCUMENTO, ID_CENTRO_CUSTO, ID_CONTA_BANCARIA, DESCRICAO, DATA_VENCIMENTO, VALOR_TOTAL, SITUACAO, 'N', 'N', DATA_PAGAMENTO, NUMERO_PARCELAS, TENAT_ID, ID_PLANO_CONTA, VALOR_PAGO, VALOR, 'P', 'N', ID
FROM 
	CONTA_PAGAR  

----------------------------------------------------------

update a set a.VALOR_TOTAL = valor from CONTA_RECEBER a where VALOR_TOTAL is null 

update a set a.TIPO_CONTA = 'N' from CONTA_RECEBER a where TIPO_CONTA is null 

ALTER TABLE CONTA 
ADD ID_CONTA_RECEBER int 

INSERT INTO 
	CONTA(ID_CLIENTE, ID_DOCUMENTO, ID_CENTRO_CUSTO, ID_CONTA_BANCARIA, DESCRICAO, DATA_VENCIMENTO, VALOR_TOTAL, SITUACAO, REPETIR_CONTA, INFORMAR_PAGAMENTO, DATA_PAGAMENTO, NUMERO_PARCELAS, TENAT_ID, ID_PLANO_CONTA, VALOR_PAGO, VALOR, TIPO_TRANSACAO, TIPO_CONTA, ID_CONTA_RECEBER)
SELECT 
	ID_CLIENTE, ID_DOCUMENTO, ID_CENTRO_CUSTO, ID_CONTA_BANCARIA, DESCRICAO, DATA_VENCIMENTO, VALOR_TOTAL, SITUACAO, 'N', 'N', DATA_RECEBIMENTO, NUMERO_PARCELAS, TENAT_ID, ID_PLANO_CONTA, VALOR_RECEBIDO, VALOR, 'R', TIPO_CONTA, ID
FROM 
	CONTA_RECEBER   

-- PARCELA ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA_PARCELA 
ADD ID_CONTA_PAGAR_PARCELA int 

INSERT INTO CONTA_PARCELA(ID_CONTA_BANCARIA, ID_DOCUMENTO, NUM_PARCELA, VALOR, DATA_VENCIMENTO, DATA_PAGAMENTO, SITUACAO, VALOR_PAGO, PAGAMENTO_PARCIAL, TENAT_ID, ID_CONTA_PAGAR_PARCELA, VALOR_TOTAL, ID_CONTA)
SELECT a.ID_CONTA_BANCARIA, a.ID_DOCUMENTO, a.NUM_PARCELA, a.VALOR, a.DATA_VENCIMENTO, a.DATA_PAGAMENTO, a.SITUACAO, a.VALOR_PAGO, a.PAGAMENTO_PARCIAL, a.TENAT_ID, a.id, a.VALOR, c.id
FROM CONTA_PAGAR_PARCELA a JOIN CONTA_PAGAR b ON a.ID_CONTA_PAGAR = b.id JOIN CONTA c ON c.ID_CONTA_PAGAR = b.id  

----------------------------------------------------------

ALTER TABLE CONTA_PARCELA 
ADD ID_CONTA_RECEBER_PARCELA int 

INSERT INTO CONTA_PARCELA(ID_CONTA_BANCARIA, ID_DOCUMENTO, NUM_PARCELA, VALOR, DATA_VENCIMENTO, DATA_PAGAMENTO, SITUACAO, VALOR_PAGO, PAGAMENTO_PARCIAL, TENAT_ID, ID_CONTA_RECEBER_PARCELA, VALOR_TOTAL, ID_CONTA)
SELECT a.ID_CONTA_BANCARIA, a.ID_DOCUMENTO, a.NUM_PARCELA, a.VALOR_PARCELA, a.DATA_VENCIMENTO, a.DATA_RECEBIMENTO, a.SITUACAO, a.VALOR_RECEBIDO, a.RECEBIMENTO_PARCIAL, a.TENAT_ID, a.id, a.VALOR_PARCELA, c.id
FROM CONTA_RECEBER_PARCELA a JOIN CONTA_RECEBER b ON a.ID_CONTA_RECEBER = b.id JOIN CONTA c ON c.ID_CONTA_RECEBER = b.id  

-- PARCIAL --------------------------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA_PARCELA_PARCIAL 
ADD ID_CONTA_PAGAR_PARCELA_PARCIAL int 

INSERT INTO CONTA_PARCELA_PARCIAL(ID_CONTA_BANCARIA, ID_DOCUMENTO, VALOR_PAGO, DATA_PAGAMENTO, DATA_VENCIMENTO, TENAT_ID, ID_CONTA_PAGAR_PARCELA_PARCIAL, ID_CONTA_PARCELA)
SELECT a.ID_CONTA_BANCARIA, a.ID_DOCUMENTO, a.VALOR, a.DATA_PAGAMENTO, a.DATA_VENCIMENTO, a.TENAT_ID, a.id, c.id 
FROM CONTA_PAGAR_PARCELA_PARCIAL a JOIN CONTA_PAGAR_PARCELA b ON a.ID_CONTA_PAGAR_PARCELA = b.id JOIN CONTA_PARCELA c ON c.ID_CONTA_PAGAR_PARCELA = b.id  

----------------------------------------------------------

ALTER TABLE CONTA_PARCELA_PARCIAL 
ADD ID_CONTA_RECEBER_PARCELA_PARCIAL int 

INSERT INTO CONTA_PARCELA_PARCIAL(ID_CONTA_BANCARIA, ID_DOCUMENTO, VALOR_PAGO, DATA_PAGAMENTO, DATA_VENCIMENTO, TENAT_ID, ID_CONTA_RECEBER_PARCELA_PARCIAL, ID_CONTA_PARCELA)
SELECT a.ID_CONTA_BANCARIA, a.ID_DOCUMENTO, a.VALOR_RECEBIDO, a.DATA_RECEBIMENTO, a.DATA_VENCIMENTO, a.TENAT_ID, a.id, c.id 
FROM CONTA_RECEBER_PARCELA_PARCIAL a JOIN CONTA_RECEBER_PARCELA b ON a.ID_CONTA_RECEBER_PARCELA = b.id JOIN CONTA_PARCELA c ON c.ID_CONTA_RECEBER_PARCELA = b.id  

-- COMPRA / VENDA ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

UPDATE a SET ID_CONTA = c.id FROM COMPRA a JOIN CONTA_PAGAR b ON a.ID_CONTA_PAGAR = b.id JOIN CONTA c ON b.id = c.ID_CONTA_PAGAR

----------------------------------------------------------

UPDATE a SET ID_CONTA = c.id  FROM VENDA a JOIN CONTA_RECEBER b ON a.ID_CONTA_RECEBER = b.id JOIN CONTA c ON b.id = c.ID_CONTA_RECEBER

-- EXTRATO ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

UPDATE a SET a.ID_CONTA_PARCELA = c.id FROM EXTRATO_CONTA_CORRENTE a JOIN CONTA_PAGAR_PARCELA b ON a.ID_CONTA_PAGAR_PARCELA = b.ID JOIN CONTA_PARCELA c ON c.ID_CONTA_PAGAR_PARCELA = b.id 

----------------------------------------------------------

UPDATE a SET a.ID_CONTA_PARCELA = c.id FROM EXTRATO_CONTA_CORRENTE a JOIN CONTA_RECEBER_PARCELA b ON a.ID_CONTA_RECEBER_PARCELA = b.ID JOIN CONTA_PARCELA c ON c.ID_CONTA_RECEBER_PARCELA = b.id 

----------------------------------------------------------

UPDATE a SET a.ID_CONTA_PARCELA_PARCIAL = c.id FROM EXTRATO_CONTA_CORRENTE a JOIN CONTA_PAGAR_PARCELA_PARCIAL b ON a.ID_CONTA_PAGAR_PARCELA_PARCIAL = b.ID JOIN CONTA_PARCELA_PARCIAL c ON c.ID_CONTA_PAGAR_PARCELA_PARCIAL = b.id 

----------------------------------------------------------

UPDATE a SET a.ID_CONTA_PARCELA_PARCIAL = c.id FROM EXTRATO_CONTA_CORRENTE a JOIN CONTA_RECEBER_PARCELA_PARCIAL b ON a.ID_CONTA_RECEBER_PARCELA_PARCIAL = b.ID JOIN CONTA_PARCELA_PARCIAL c ON c.ID_CONTA_RECEBER_PARCELA_PARCIAL = b.id 

----------------------------------------------------------

UPDATE CONTA SET SITUACAO = 'NP' WHERE SITUACAO = 'NR'

----------------------------------------------------------

UPDATE CONTA SET SITUACAO = 'PP' WHERE SITUACAO = 'RP'

----------------------------------------------------------

UPDATE CONTA SET SITUACAO = 'PG' WHERE SITUACAO = 'RC'

----------------------------------------------------------

UPDATE CONTA_PARCELA SET SITUACAO = 'NP' WHERE SITUACAO = 'NR'

----------------------------------------------------------

UPDATE CONTA_PARCELA SET SITUACAO = 'PP' WHERE SITUACAO = 'RP'

----------------------------------------------------------

UPDATE CONTA_PARCELA SET SITUACAO = 'PG' WHERE SITUACAO = 'RC'

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- DROPS
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE EXTRATO_CONTA_CORRENTE 
DROP CONSTRAINT FK_EXTRATO_CONTA_CORRENTE_CONTA_PAGAR_PARCELA

ALTER TABLE EXTRATO_CONTA_CORRENTE 
DROP CONSTRAINT FK_EXTRATO_CONTA_CORRENTE_CONTA_PAGAR_PARCELA_PARCIAL

ALTER TABLE EXTRATO_CONTA_CORRENTE 
DROP CONSTRAINT FK_EXTRATO_CONTA_CORRENTE_CONTA_RECEBER_PARCELA

ALTER TABLE EXTRATO_CONTA_CORRENTE 
DROP CONSTRAINT FK_EXTRATO_CONTA_CORRENTE_CONTA_RECEBER_PARCELA_PARCIAL

---------------------------------------

ALTER TABLE EXTRATO_CONTA_CORRENTE 
DROP COLUMN ID_CONTA_PAGAR_PARCELA 

ALTER TABLE EXTRATO_CONTA_CORRENTE 
DROP COLUMN ID_CONTA_PAGAR_PARCELA_PARCIAL 

ALTER TABLE EXTRATO_CONTA_CORRENTE 
DROP COLUMN ID_CONTA_RECEBER_PARCELA 

ALTER TABLE EXTRATO_CONTA_CORRENTE 
DROP COLUMN ID_CONTA_RECEBER_PARCELA_PARCIAL 


---------------------------------------

ALTER TABLE EXTRATO_CONTA_CORRENTE_AUD  
DROP COLUMN ID_CONTA_PAGAR_PARCELA 

ALTER TABLE EXTRATO_CONTA_CORRENTE_AUD  
DROP COLUMN ID_CONTA_PAGAR_PARCELA_PARCIAL 

ALTER TABLE EXTRATO_CONTA_CORRENTE_AUD  
DROP COLUMN ID_CONTA_RECEBER_PARCELA 

ALTER TABLE EXTRATO_CONTA_CORRENTE_AUD  
DROP COLUMN ID_CONTA_RECEBER_PARCELA_PARCIAL

----------------------------------------------------------

ALTER TABLE COMPRA 
DROP CONSTRAINT FK_COMPRA_CONTA_PAGAR

ALTER TABLE VENDA 
DROP CONSTRAINT FK_VENDA_CONTA_RECEBER

--

ALTER TABLE COMPRA 
DROP COLUMN ID_CONTA_PAGAR 

ALTER TABLE VENDA 
DROP COLUMN ID_CONTA_RECEBER 

--

ALTER TABLE COMPRA_AUD  
DROP COLUMN ID_CONTA_PAGAR 

ALTER TABLE VENDA_AUD  
DROP COLUMN ID_CONTA_RECEBER 


-- 

ALTER TABLE COMPRA 
ALTER COLUMN ID_CONTA int NOT NULL 

ALTER TABLE VENDA 
ALTER COLUMN ID_CONTA int NOT NULL 

----------------------------------------------------------

DROP TABLE CONTA_PAGAR_PARCELA_PARCIAL

DROP TABLE CONTA_RECEBER_PARCELA_PARCIAL 

DROP TABLE CONTA_PAGAR_PARCELA_PARCIAL_AUD 

DROP TABLE CONTA_RECEBER_PARCELA_PARCIAL_AUD  

--

DROP TABLE CONTA_PAGAR_PARCELA

DROP TABLE CONTA_RECEBER_PARCELA

DROP TABLE CONTA_PAGAR_PARCELA_AUD

DROP TABLE CONTA_RECEBER_PARCELA_AUD

--

DROP TABLE CONTA_PAGAR_PAGAMENTO

DROP TABLE CONTA_RECEBER_PAGAMENTO

DROP TABLE CONTA_PAGAR_PAGAMENTO_AUD 

DROP TABLE CONTA_RECEBER_PAGAMENTO_AUD

--

DROP TABLE CONTA_PAGAR

DROP TABLE CONTA_RECEBER


--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CONTA_PARCELA_PARCIAL 
ADD OUTROS_CUSTOS numeric(10 , 2)

ALTER TABLE CONTA_PARCELA_PARCIAL 
ADD JUROS numeric(10 , 2)

ALTER TABLE CONTA_PARCELA_PARCIAL 
ADD MULTA numeric(10 , 2)

ALTER TABLE CONTA_PARCELA_PARCIAL 
ADD DESCONTO numeric(10 , 2)

ALTER TABLE CONTA_PARCELA_PARCIAL 
ADD ENCARGOS numeric(10 , 2)

-----------------------------------------

ALTER TABLE CONTA_PARCELA_PARCIAL_AUD 
ADD OUTROS_CUSTOS numeric(10 , 2)

ALTER TABLE CONTA_PARCELA_PARCIAL_AUD 
ADD JUROS numeric(10 , 2)

ALTER TABLE CONTA_PARCELA_PARCIAL_AUD 
ADD MULTA numeric(10 , 2)

ALTER TABLE CONTA_PARCELA_PARCIAL_AUD 
ADD DESCONTO numeric(10 , 2)

ALTER TABLE CONTA_PARCELA_PARCIAL_AUD 
ADD ENCARGOS numeric(10 , 2)

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: HUGO
-- Data: 12/01/2017
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE FUNCIONARIO ( 
        ID int identity(1,1) NOT NULL,
        ID_CIDADE int,
        NOME varchar(200) NOT NULL,
        TIPO_CONTRATACAO varchar(1) NOT NULL, 
        DATA_NASCIMENTO datetime NOT NULL,
        NOME_MAE varchar(200),
        SALARIO_BASE numeric(10,2),
        TELEFONE_CELULAR varchar(50),
        TELEFONE_RESIDENCIAL varchar(50),
        EMAIL varchar(50),
        ENDERECO varchar(200),
        NUMERO varchar(10),
        COMPLEMENTO varchar(50),
        FOTO varbinary(max),
        DATA_CONTRATACAO datetime NOT NULL,
        DATA_DEMISSAO datetime,
        TENAT_ID varchar(20) NOT NULL,
        CONSTRAINT PK_ID_FUNCIONARIO PRIMARY KEY CLUSTERED (ID)

)

ALTER TABLE FUNCIONARIO 
ADD CONSTRAINT FK_ID_CIDADE FOREIGN KEY (ID_CIDADE) REFERENCES CIDADE(ID)

CREATE TABLE FUNCIONARIO_AUD ( 
        ID int identity(1,1) NOT NULL,
        REV int NOT NULL,REVTYPE smallint,
        ID_CIDADE int,
        NOME varchar(200) NULL,
        TIPO_CONTRATACAO varchar(1) NULL, 
        DATA_NASCIMENTO datetime NULL,
        NOME_MAE varchar(200),
        SALARIO_BASE numeric(10,2),
        TELEFONE_CELULAR varchar(50),
        TELEFONE_RESIDENCIAL varchar(50),
        EMAIL varchar(50),
        ENDERECO varchar(200),
        NUMERO varchar(10),
        COMPLEMENTO varchar(50),
        FOTO varbinary(max),
        DATA_CONTRATACAO datetime NULL,
        DATA_DEMISSAO datetime,
        TENAT_ID varchar(20) NULL,
        CONSTRAINT PK_FUNCIONARIO_AUD PRIMARY KEY CLUSTERED (ID, REV),
        CONSTRAINT FK_FUNCIONARIO_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE)
)

ALTER TABLE FUNCIONARIO_AUD 
ADD CPF varchar(20) NULL

ALTER TABLE FUNCIONARIO_AUD 
ADD SENHA varchar(100)

ALTER TABLE FUNCIONARIO_AUD 
ADD MATRICULA varchar(50)

ALTER TABLE FUNCIONARIO 
ADD CPF varchar(20) NOT NULL

ALTER TABLE FUNCIONARIO 
ADD SENHA varchar(100)

ALTER TABLE FUNCIONARIO 
ADD SENHA varchar(100)

ALTER TABLE FUNCIONARIO
ADD MATRICULA varchar(50)

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: HUGO
-- Data: 12/01/2017
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE PONTO_AUD 
ADD CONSTRAINT FK_ID_FUNCIONARIO_AUD FOREIGN KEY (ID_FUNCIONARIO) REFERENCES FUNCIONARIO(ID) 

ALTER TABLE PONTO
ADD CONSTRAINT FK_ID_FUNCIONARIO FOREIGN KEY (ID_FUNCIONARIO) REFERENCES FUNCIONARIO(ID) 
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: PEDRO COTTA 
-- Data: 13/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

-------------------------------CONTRATO----------------------------------
ALTER TABLE CONTRATO ADD
NUMERO_PARCELAS INT NOT NULL

ALTER TABLE CONTRATO_AUD ADD
NUMERO_PARCELAS INT 

ALTER TABLE CONTRATO ADD
DATA_VENCIMENTO_PAGAMENTO DATETIME NOT NULL


ALTER TABLE CONTRATO_AUD ADD
DATA_VENCIMENTO_PAGAMENTO DATETIME
--------------------------------------------------------------------------
-------------------------------CONTA--------------------------------------
ALTER TABLE PLANO_CONTA_LANCAMENTO_CONTABIL ADD
ID_CONTA INT 

ALTER TABLE PLANO_CONTA_LANCAMENTO_CONTABIL_AUD ADD
ID_CONTA INT 

ALTER TABLE PLANO_CONTA_LANCAMENTO_CONTABIL ADD
CONSTRAINT FK_PLANO_CONTA_LANCAMENTO_CONTABIL_CONTA FOREIGN KEY(ID_CONTA) REFERENCES CONTA (ID)

ALTER TABLE CONTRATO ADD
CONSTRAINT FK_CONTRATO_CONTA FOREIGN KEY(ID_CONTA) REFERENCES CONTA (ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: HUGO
-- Data: 16/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------
ALTER TABLE PONTO 
ALTER COLUMN DATA_PONTO datetime NULL


------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: GUILHERME ALMEIDA  
-- Data: 13/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------


ALTER TABLE COMPRA 
DROP COLUMN DESCRICAO 

ALTER TABLE COMPRA_AUD  
DROP COLUMN DESCRICAO 

ALTER TABLE VENDA 
DROP COLUMN DESCRICAO 

ALTER TABLE VENDA_AUD  
DROP COLUMN DESCRICAO 


------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: PEDRO COTTA 
-- Data: 17/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

---------------------------------------CONTA BANCARIA-------------------------------------------------------------

ALTER TABLE CONTA_BANCARIA ADD
SITUACAO VARCHAR(1)

ALTER TABLE CONTA_BANCARIA_AUD ADD
SITUACAO VARCHAR(1)

UPDATE CONTA_BANCARIA SET SITUACAO = 'A' WHERE SITUACAO IS NULL

ALTER TABLE CONTA_BANCARIA ALTER COLUMN
SITUACAO VARCHAR(1) NOT NULL

ALTER TABLE CONTA_BANCARIA ADD
DATA_CANCELAMENTO DATETIME 

ALTER TABLE CONTA_BANCARIA_AUD ADD
DATA_CANCELAMENTO DATETIME 

INSERT INTO LAYOUT 
VALUES('Layout funcionário', 'FR', 1, 0 , ';', null, null)

INSERT INTO LAYOUT_CAMPO
VALUES(6, 'Nome', 'T', null, null, 1, 'S', null, null, null, null)

INSERT INTO LAYOUT_CAMPO
VALUES(6, 'Tipo de contratação', 'T', null, null, 2, 'S', null, null, null, null)

INSERT INTO LAYOUT_CAMPO
VALUES(6, 'Data de nascimento', 'T', null, null, 3, 'S', null, 'dd/MM/aaaa', null, null)

INSERT INTO LAYOUT_CAMPO
VALUES(6, 'Data de contratação', 'T', null, null, 4, 'S', null, 'dd/MM/aaaa', null, null)

INSERT INTO LAYOUT_CAMPO
VALUES(6, 'CPF', 'T', null, null, 5, 'S', null, null, null, null)

INSERT INTO LAYOUT_CAMPO
VALUES(6, 'Matrícula', 'T', null, null, 6, 'S', null, null, null, null)


INSERT INTO LAYOUT_CAMPO
VALUES(6, 'Senha', 'T', null, null, 7, 'S', null, null, null, null)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: HUGO
-- Data: 18/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE FUNCIONARIO
ADD ID_PLANO_CONTA INT NULL

ALTER TABLE FUNCIONARIO 
ADD CONSTRAINT FK_FUNCIONARIO_PLANO_CONTA FOREIGN KEY (ID_PLANO_CONTA) REFERENCES PLANO_CONTA(ID) 

ALTER TABLE FUNCIONARIO_AUD
ADD ID_PLANO_CONTA INT NULL

ALTER TABLE PARAMETRO_SISTEMA
ADD ID_PLANO_CONTA_FUNCIONARIO INT NULL

ALTER TABLE PARAMETRO_SISTEMA_AUD
ADD ID_PLANO_CONTA_FUNCIONARIO INT NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: GUILHERME
-- Data: 18/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE PONTO 
ADD TIPO_HORA varchar(2) NOT NULL 

ALTER TABLE PONTO_AUD 
ADD TIPO_HORA varchar(2) 

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: HUGO
-- Data: 19/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

UPDATE FORNECEDOR SET RAZAO_SOCIAL = DESCRICAO

ALTER TABLE FORNECEDOR 
ALTER COLUMN RAZAO_SOCIAL varchar(200) NOT NULL

ALTER TABLE FORNECEDOR_AUD 
ALTER COLUMN RAZAO_SOCIAL varchar(200) 

------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE FORNECEDOR 
DROP COLUMN DESCRICAO

ALTER TABLE FORNECEDOR_AUD 
DROP COLUMN DESCRICAO


------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: PEDRO COTTA 
-- Data: 18/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

---------------------------------------CONTRATO-------------------------------------------------------------
ALTER TABLE CONTRATO ADD
DESCONTO NUMERIC(10,2)

ALTER TABLE CONTRATO_AUD ADD
DESCONTO NUMERIC(10,2)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: HUGO
-- Data: 20/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE 
ADD VALOR_IR NUMERIC(10,2) NULL

ALTER TABLE CLIENTE 
ADD VALOR_PIS NUMERIC(10,2) NULL

ALTER TABLE CLIENTE 
ADD VALOR_CSLL NUMERIC(10,2) NULL

ALTER TABLE CLIENTE 
ADD VALOR_INSS NUMERIC(10,2) NULL

ALTER TABLE CLIENTE 
ADD VALOR_COFINS NUMERIC(10,2) NULL

------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE CLIENTE_AUD 
ADD VALOR_IR NUMERIC(10,2) NULL

ALTER TABLE CLIENTE_AUD 
ADD VALOR_PIS NUMERIC(10,2) NULL

ALTER TABLE CLIENTE_AUD 
ADD VALOR_CSLL NUMERIC(10,2) NULL

ALTER TABLE CLIENTE_AUD 
ADD VALOR_INSS NUMERIC(10,2) NULL

ALTER TABLE CLIENTE_AUD  
ADD VALOR_COFINS NUMERIC(10,2) NULL

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: GUILHERME
-- Data: 20/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

DELETE a FROM LAYOUT_CAMPO a JOIN LAYOUT b ON a.id_LAYOUT = b.id WHERE b.descricao = 'Layout funcionário'

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Nome', 'T',  1, 'S', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Matrícula', 'I',  2, 'S', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'CPF', 'T',  3, 'S', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Data de nascimento', 'D',  4, 'S', null, 'dd/MM/yyyy')

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Data de contratação', 'D',  5, 'S', null, 'dd/MM/yyyy')

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Data de demissão', 'D',  6, 'N', null, 'dd/MM/yyyy')

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Salário', 'I',  7, 'N', ',', null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Email', 'T',  8, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Telefone residencial', 'T',  9, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Telefone celular', 'T',  10, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'CEP', 'T',  11, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Endereco', 'T',  12, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Número', 'T',  13, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Complemento', 'T',  14, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Bairro', 'T',  15, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Cidade', 'T',  16, 'N', null, null)

INSERT INTO LAYOUT_CAMPO(ID_LAYOUT, DESCRICAO, TIPO_DADO, POSICAO_ARQUIVO, OBRIGATORIO, SEPARADOR_CASAS_DECIMAIS, MASCARA_DATA)
VALUES((SELECT ID FROM LAYOUT WHERE DESCRICAO = 'Layout funcionário'), 'Nome da mãe', 'T',  17, 'N', null, null)

------------------------------------------------------------------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: PEDRO COTTA
-- Data: 23/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO PERFIL (DESCRICAO, TIPO)
VALUES('Funcionário', 'FR')

UPDATE FUNCIONARIO SET EMAIL = 'TESTE@TESTE.COM' WHERE EMAIL IS NULL

ALTER TABLE FUNCIONARIO ALTER COLUMN
EMAIL VARCHAR(50) NOT NULL

INSERT INTO GRUPO ( DESCRICAO, TIPO, GESTAO_INTERNA)
VALUES ('Grupo funcionário', (SELECT TIPO FROM PERFIL WHERE DESCRICAO = 'Funcionário'), 'S')

INSERT INTO GRUPO_EMPRESA(ID_GRUPO, ID_EMPRESA)
VALUES((SELECT ID FROM GRUPO WHERE DESCRICAO = 'Grupo funcionário'), (SELECT ID FROM EMPRESA WHERE DESCRICAO = 'ACME S/A'))

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: GUILHERME ALMEIDA
-- Data: 24/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------


UPDATE EMPRESA SET DATA_CREDENCIAMENTO = getDATE() WHERE DATA_CREDENCIAMENTO is NULL;

ALTER TABLE EMPRESA 
ALTER COLUMN DATA_CREDENCIAMENTO DATETIME NOT NULL

------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE ITEM_CTISS (
	ID int IDENTITY(1,1) NOT NULL, 
	DESCRICAO varchar(500) NOT NULL,
	ID_CTISS int NOT NULL,
	CONSTRAINT PK_ITEM_CTISS PRIMARY KEY (ID)
)

CREATE TABLE ITEM_CTISS_AUD ( 
	ID int NOT NULL,REV int NOT NULL,REVTYPE smallint,
	DESCRICAO varchar(500),
	ID_CTISS int,
	CONSTRAINT PK_ITEM_CTISS_AUD PRIMARY KEY CLUSTERED (ID, REV),
    CONSTRAINT FK_ITEM_CTISS_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE) 
)

ALTER TABLE ITEM_CTISS 
ADD CONSTRAINT FK_ITEM_CTISS_CTISS FOREIGN KEY (ID_CTISS) REFERENCES CTISS(ID)

------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE NFS ( 
	 ID int IDENTITY(1,1) NOT NULL,
 
	 -- CLIENTE
	 ID_CLIENTE int NOT NULL,
	 ID_UF_CLIENTE int NOT NULL,
	 ID_CIDADE_CLIENTE int NOT NULL,
	 CIDADE_CLIENTE varchar (200) NOT NULL,
	 UF_CLIENTE varchar (2) NOT NULL,
	 NOME_CLIENTE varchar (200) NOT NULL,
	 TIPO_PESSOA_CLIENTE varchar(2) NOT NULL,
	 CPF_CNPJ_CLIENTE varchar (20) NOT NULL,
	 CEP_CLIENTE varchar (15) NOT NULL,
	 ENDERECO_CLIENTE varchar (2000) NOT NULL,
	 NUMERO_CLIENTE varchar (50) NOT NULL,
	 BAIRRO_CLIENTE varchar (200) NOT NULL,
	 COMPLEMENTO_CLIENTE varchar (200) NULL,
	 RG_CLIENTE varchar (20) NULL,
	 EMAIL_CLIENTE varchar (50) NULL,
	 TELEFONE_CLIENTE varchar (50) NULL,
	 INSCRICAO_MUNICIPAL_CLIENTE varchar(50) NULL,

	 -- IDENTIFICACAO
	 DESCRICAO_SERVICO varchar (5000) NOT NULL,
	 ID_CTISS int NOT NULL,
	 ID_ITEM_CTISS int NOT NULL,
	 ID_MUNICIO_ISSQN int NOT NULL,
	 NATUREZA_OPERACAO varchar(1),
	 REGIME_TRIVUTACAO varchar(1),
 
	 -- VALORES
	 VALOR_TOTAL numeric (10, 2) NULL,
	 VALOR_DEDUCOES numeric (10, 2) NULL,
	 DESCONTO_CONDICIONADO numeric (10, 2) NULL,
	 DESCONTO_INCONDICIONADO numeric (10, 2) NULL,
	 VALOR_INSS numeric (10, 2) NULL,
	 VALOR_IR numeric (10, 2) NULL,
	 VALOR_PIS numeric (10, 2) NULL,
	 VALOR_COFINS numeric (10, 2) NULL,
	 VALOR_CSLL numeric (10, 2) NULL,
	 OUTRAS_RETENCOES numeric (10, 2) NULL,
	 ISS_RETIDO_CLIENTE varchar (1) NULL,

	 -- INTERMEDIARIO
	 TIPO_PESSOA_INTERMEDIARIO varchar(2),
	 CPF_CNPJ_INTERMEDIARIO varchar (20),
	 INSCRICAO_MUNICIPAL_INTERMEDIARIO varchar(50) NULL,
	 NOME_INTERMEDIARIO varchar (200),

	 -- CONSTRUCAO_CIVIL 
	 NUMERO_CEI varchar(200),
	 NUMERO_ART varchar(200),

	 -- NF
	 SITUACAO varchar (1) NOT NULL,
	 DATA_GERACAO datetime NOT NULL,
	 DATA_EMISSAO datetime NOT NULL,
         CODIGO_VERIFICACAO int NOT NULL,
	 SERIE int NOT NULL,
         NUMERO_NOTA_FISCAL int NOT NULL,
         TENAT_ID INT NOT NULL,
         DATA_RETORNO_PROCESSAMENTO DATE,
	 DATA_CANCELAMENTO DATE,
	 XML_ENVIO varchar (max) NULL,
	 XML_RETORNO varchar (max) NULL,
         XML_ARMAZENAMENTO varchar (max) NULL,
	 MENSAGEM_ERRO_ENVIO varchar (max) NULL,

    CONSTRAINT PK_NFS PRIMARY KEY (ID)
)

CREATE TABLE NFS_AUD ( 
	ID int NOT NULL,REV int NOT NULL,REVTYPE smallint,
	 -- CLIENTE
	 ID_CLIENTE int ,
	 ID_UF_CLIENTE int ,
	 ID_CIDADE_CLIENTE int ,
	 CIDADE_CLIENTE varchar (200) ,
	 UF_CLIENTE varchar (2) ,
	 NOME_CLIENTE varchar (200) ,
	 TIPO_PESSOA_CLIENTE varchar(2) ,
	 CPF_CNPJ_CLIENTE varchar (20) ,
	 CEP_CLIENTE varchar (15) ,
	 ENDERECO_CLIENTE varchar (2000) ,
	 NUMERO_CLIENTE varchar (50) ,
	 BAIRRO_CLIENTE varchar (200) ,
	 COMPLEMENTO_CLIENTE varchar (200) NULL,
	 RG_CLIENTE varchar (20) NULL,
	 EMAIL_CLIENTE varchar (50) NULL,
	 TELEFONE_CLIENTE varchar (50) NULL,
	 INSCRICAO_MUNICIPAL_CLIENTE varchar(50) NULL,

	 -- IDENTIFICACAO
	 DESCRICAO_SERVICO varchar (5000) ,
	 ID_CTISS int ,
	 ID_ITEM_CTISS int ,
	 ID_MUNICIO_ISSQN int ,
	 NATUREZA_OPERACAO varchar(1),
	 REGIME_TRIVUTACAO varchar(1),
 
	 -- VALORES
	 VALOR_TOTAL numeric (10, 2) NULL,
	 VALOR_DEDUCOES numeric (10, 2) NULL,
	 DESCONTO_CONDICIONADO numeric (10, 2) NULL,
	 DESCONTO_INCONDICIONADO numeric (10, 2) NULL,
	 VALOR_INSS numeric (10, 2) NULL,
	 VALOR_IR numeric (10, 2) NULL,
	 VALOR_PIS numeric (10, 2) NULL,
	 VALOR_COFINS numeric (10, 2) NULL,
	 VALOR_CSLL numeric (10, 2) NULL,
	 OUTRAS_RETENCOES numeric (10, 2) NULL,
	 ISS_RETIDO_CLIENTE varchar (1) NULL,

	 -- INTERMEDIARIO
	 TIPO_PESSOA_INTERMEDIARIO varchar(2),
	 CPF_CNPJ_INTERMEDIARIO varchar (20),
	 INSCRICAO_MUNICIPAL_INTERMEDIARIO varchar(50) NULL,
	 NOME_INTERMEDIARIO varchar (200),

	 -- CONSTRUCAO_CIVIL 
	 NUMERO_CEI varchar(200),
	 NUMERO_ART varchar(200),

	 -- NF
	 SITUACAO varchar (1) ,
	 DATA_GERACAO datetime ,
	 DATA_EMISSAO datetime ,
         CODIGO_VERIFICACAO int ,
	 SERIE int ,
         NUMERO_NOTA_FISCAL int ,
         TENAT_ID INT ,
         DATA_RETORNO_PROCESSAMENTO DATE,
	 DATA_CANCELAMENTO DATE,
	 XML_ENVIO varchar (max) NULL,
	 XML_RETORNO varchar (max) NULL,
         XML_ARMAZENAMENTO varchar (max) NULL,
	 MENSAGEM_ERRO_ENVIO varchar (max) NULL,
	CONSTRAINT PK_NFS_AUD PRIMARY KEY CLUSTERED (ID, REV),
        CONSTRAINT FK_NFS_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REVTYPE) 
)

ALTER TABLE NFS ADD
 CONSTRAINT FK_NFS_CLIENTE FOREIGN KEY (ID_CLIENTE) REFERENCES CLIENTE(ID),
 CONSTRAINT FK_NFS_UF_CLIENTE FOREIGN KEY (ID_UF_CLIENTE) REFERENCES UF(ID),
 CONSTRAINT FK_NFS_CIDADE_CLIENTE FOREIGN KEY (ID_CIDADE_CLIENTE) REFERENCES CIDADE(ID),
 CONSTRAINT FK_NFS_CTISS FOREIGN KEY (ID_CTISS) REFERENCES CTISS(ID),
 CONSTRAINT FK_NFS_ITEM_CTISS FOREIGN KEY (ID_ITEM_CTISS) REFERENCES ITEM_CTISS(ID),
 CONSTRAINT FK_NFS_MUNICIO_ISSQN FOREIGN KEY (ID_MUNICIO_ISSQN) REFERENCES CIDADE(ID)

------------------------------------------------------------------------------------------------------------------------------------------------------
-- Nome: GUILHERME ALMEIDA
-- Data: 25/01/2017
------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER TABLE EMPRESA 
DROP COLUMN TEM_INSCRICAO_ESTADUAL 

ALTER TABLE EMPRESA_AUD 
DROP COLUMN TEM_INSCRICAO_ESTADUAL 

------------------------------------------------------------------------------------------------------------------------------------------------------

UPDATE CLASSIFICACAO SET DIVISAO = REPLACE(DIVISAO, ' ', '');

------------------------------------------------------------------------------------------------------------------------------------------------------