----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES 
----------------------------------------------------------------------------------------------------------------------------------------------------------

DECLARE @descricao varchar(500),
        @descricaoDetalhada varchar(500)


-- TABELAS DE APOIO
SET @descricao = 'BANCO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar bancos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CEST_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar CEST'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CFOP_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar CFOP'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CNAE_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar CNAE'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CSOSN_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar CSOSN'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CST_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar CST'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CTISS_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar CST'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'NCM_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar NCM'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ORIGEM_MERCADORIA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar origens de mercadoria'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'SITUACAO_TRIBUTARIA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar situações tributárias'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
--SET @descricao = 'NATUREZA_OPERACAO_VISUALIZAR'
--SET @descricaoDetalhada = 'Visualizar naturezas de operação'
--IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--

-- CONDOMINIO
SET @descricao = 'CONDOMINIO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar condomínio'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONDOMINIO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar condomínios'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'BLOCO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar bloco'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'BLOCO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar blocos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'UNIDADE_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar unidade'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'UNIDADE_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar unidades'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'VAGA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar vaga'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'VAGA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar vagas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'TIPO_PATRIMONIO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar tipo de patrimonio'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'TIPO_PATRIMONIO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar tipos de patrimonio'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PATRIMONIO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar patrimonio'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PATRIMONIO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar patrimonios'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'SINDICO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar síndico'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'SINDICO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar síndicos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PARAMETRO_CONDOMINIO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar parametro do condomínio'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PARAMETRO_CONDOMINIO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar parametros do condomínio'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'MENSALIDADE_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar mensalidade'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'MENSALIDADE_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar mensalidades'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'RESERVA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar reserva'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'RESERVA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar reservas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'MANUTENCAO_PRORAMADA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar manutenção programada'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'MANUTENCAO_PROGRAMADA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar manutenção programada'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--

-- CADASTRO
SET @descricao = 'EMPRESA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar empresa'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'EMPRESA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar empresas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTA_BANCARIA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar contas bancaria'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTA_BANCARIA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar contas bancarias'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CLIENTE_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar cliente'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CLIENTE_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar clientes'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PLANO_CONTA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar plano de contas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PLANO_CONTA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar planos de contas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CATEGORIA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar categoria de produto/serviço'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CATEGORIA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar categorias de produto/serviço'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PRODUTO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar produto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PRODUTO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar produtos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'SERVICO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar serviço'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'SERVICO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar serviços'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CENTRO_CUSTO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar centro de custo'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CENTRO_CUSTO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar centros de custo'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'UNIDADE_MEDIDA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar unidade de medida'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'UNIDADE_MEDIDA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar unidades medida'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FORMA_PAGAMENTO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar forma de pagamento'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FORMA_PAGAMENTO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar forma de pagamento'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FORNECEDOR_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar fornecedor'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FORNECEDOR_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar fornecedores'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'TRANSPORTADORA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar transportadora'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'TRANSPORTADORA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar transportadoras'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'MOTIVO_CANCELAMENTO_CONTA_GERENCIAR'
SET @descricaoDetalhada = 'Gerenciar motivo cancelamento de conta'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'MOTIVO_CANCELAMENTO_CONTA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar motivo cancelamento de conta'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTABILIDADE_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar a contabilidade'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTABILIDADE_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lista de contabilidade'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ESTOQUE_PRODUTO'
SET @descricaoDetalhada = 'Visualizar estoque de produto e realizar inventário'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'EXTRATO_ESTOQUE_PRODUTO'
SET @descricaoDetalhada = 'Visualizar movimentação do produto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

-- ESCOLA
SET @descricao = 'AREA_ATUACAO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar a área de atuação'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'AREA_ATUACAO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lista de área de atuação'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PROFESSOR_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar professores'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PROFESSOR_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lista de professores'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CURSO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar cursos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CURSO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lista de cursos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'SOLICITANTE_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar solicitantes'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'SOLICITANTE_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lista de solicitantes'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'TURMA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar turmas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'TURMA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lista de turmas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ALUNO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar alunos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ALUNO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lista de alunos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PARCEIRO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar parceiro'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PARCEIRO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lista de parceiros'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--

-- ENTRADA
SET @descricao = 'VENDA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar venda'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'VENDA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar vendas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ORCAMENTO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar orçamento'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ORCAMENTO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar orçamento'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTA_RECEBER_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar conta a receber'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTA_RECEBER_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar contas a receber'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ORDEM_SERVICO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar ordem de serviço'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ORDEM_SERVICO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar ordens de serviço'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTRATO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar contrato'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTRATO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar contratos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'AJUSTE_CONTRATO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar ajustes de contratos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'AJUSTE_CONTRATO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar ajustes de contratos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--

-- SAIDA
SET @descricao = 'COMPRA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar compra'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'COMPRA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar compras'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTA_PAGAR_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar conta a pagar'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONTA_PAGAR_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar contas a pagar'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'NOTA_FISCAL_PRODUTO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar nota fiscal de produto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'NOTA_FISCAL_PRODUTO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar notas fiscais de produto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'NOTA_FISCAL_SERVICO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar nota fiscal de serviço'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'NOTA_FISCAL_SERVICO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar notas fiscais de serviço'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--

-- FINANCEIRO
SET @descricao = 'EXTRATO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar extratos de conta corrente'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'BOLETO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar boletos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'EMITIR_BOLETO'
SET @descricaoDetalhada = 'Emitir boleto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'REMESSA_GERENCIAR'
SET @descricaoDetalhada = 'Gerenciar remessa'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'REMESSA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar remessa'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'RETORNO_GERENCIAR'
SET @descricaoDetalhada = 'Gerenciar retorno'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'RETORNO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar retorno'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FLUXO_CAIXA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar fluxos de caixa'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'IMPORTACAO_CONTA'
SET @descricaoDetalhada = 'Importação de contas contáveis'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'DRE_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar DRE'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ANALISE_ORCAMENTARIA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar análises orçamentárias'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'TRANSFERENCIA_GERENCIAR';
SET @descricaoDetalhada = 'Gerenciar transferências entre contas';
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'TRANSFERENCIA_VISUALIZAR';
SET @descricaoDetalhada = 'Gerenciar transferências entre contas';
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'INTEGRACAO_BANCARIA_GERENCIAR';
SET @descricaoDetalhada = 'Cadastrar e editar integração bancária';
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'INTEGRACAO_BANCARIA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar integração bancária'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'LANCAMENTO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lançamentos contábil'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FECHAMENTO_CONTABIL_GERENCIAR'
SET @descricaoDetalhada = 'Visualizar lançamentos contábil'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FECHAMENTO_CONTABIL_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar lançamentos contábil'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'INCLUIR_BALANCETE_VERIFICACAO'
SET @descricaoDetalhada = 'Incluir o balancete de verificação ao menu contábil'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

-- SEGURANÇA
SET @descricao = 'PERFIL_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar perfil'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PERFIL_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar perfis'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PERMISSAO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar permissão'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PERMISSAO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar permissões'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'GRUPO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar grupo'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'GRUPO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar grupos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'USUARIO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar usuário'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'USUARIO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar usuários'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FUNCAO_AJUDA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar função de ajuda'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--

-- ADMINISTRACAO
SET @descricao = 'PARAMETRO_SISTEMA_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar parametro do sistema'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PARAMETRO_SISTEMA_ADMINISTRACAO'
SET @descricaoDetalhada = 'Cadastrar e editar parametro de administracao do sistema'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PARAMETRO_GERAL_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar um parametro geral'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PARAMETRO_GERAL_VISUALIZAR'
SET @descricaoDetalhada = 'Cadastrar e editar um parametro geral'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'EMPRESA_CREDENCIADA_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar empresas credenciadas'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'CONCEDER_ACESSO_EMPRESA'
SET @descricaoDetalhada = 'Conceder acesso ao usuário a mais de uma empresa'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ATUALIZACAO_CACHE'
SET @descricaoDetalhada = 'Atualização do cache'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'EXPORTAR_ARQUIVO'
SET @descricaoDetalhada = 'Exportar arquivos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'LAYOUT_GERENCIAR'
SET @descricaoDetalhada = 'Gerenciar layout'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'LAYOUT_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar layout'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--

-- PESSOA
SET @descricao = 'FUNCIONARIO_GERENCIAR'
SET @descricaoDetalhada = 'Cadastrar e editar funcionário'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'FUNCIONARIO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar funcionários'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'REGISTRO_PONTO_GERENCIAR'
SET @descricaoDetalhada = 'Registrar ponto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'PONTO_GERENCIAR'
SET @descricaoDetalhada = 'Consultar registro de ponto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ABONO_PONTO_GERENCIAR'
SET @descricaoDetalhada = 'Gerenciar abono de ponto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'ABONO_PONTO_VISUALIZAR'
SET @descricaoDetalhada = 'Visualizar abono de ponto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--
SET @descricao = 'MANUTENCAO_PONTO_GERENCIAR'
SET @descricaoDetalhada = 'Manutenção do ponto'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao
--

-- RELATORIO
SET @descricao = 'RELATORIO_ANALISE_PAGAMENTO'
SET @descricaoDetalhada = 'Relatório de análise de pagamento'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'RELATORIO_ANALISE_RECEBIMENTO'
SET @descricaoDetalhada = 'Relatório de análise de recebimento'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'RELATORIO_LANCAMENTO_CAIXA'
SET @descricaoDetalhada = 'Relatório de lançamentos no caixa'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'RELATORIO_MOVIMENTO_ESTOQUE'
SET @descricaoDetalhada = 'Relatório de movimento de estoque'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'RELATORIO_GIRO_ESTOQUE'
SET @descricaoDetalhada = 'Relatório de giro de estoque'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'RELATORIO_VENDA_CLIENTE'
SET @descricaoDetalhada = 'Relatório de venda por cliente'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'RELATORIO_VENDA_VENDEDOR'
SET @descricaoDetalhada = 'Relatório de venda por vendedor'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'RELATORIO_SERVICO_CLIENTE'
SET @descricaoDetalhada = 'Relatório de serviço por cliente'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

SET @descricao = 'RELATORIO_MAIS_VENDIDOS'
SET @descricaoDetalhada = 'Relatório de produtos mais vendidos'
IF NOT EXISTS( SELECT ID FROM PERMISSAO WHERE DESCRICAO = @descricao) BEGIN INSERT INTO PERMISSAO (DESCRICAO, DESCRICAO_DETALHADA) values (@descricao, @descricaoDetalhada) END ELSE UPDATE PERMISSAO SET DESCRICAO_DETALHADA = @descricaoDetalhada WHERE DESCRICAO = @descricao

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- DELETANDO PERMISSOES PADROES DOS PERFIS E DOS GRUPOS DE GESTÃO INTERNA
----------------------------------------------------------------------------------------------------------------------------------------------------------

-- DELETAR PERMISSOES PADRAO POR PERFIL
DELETE PERMISSAO_PERFIL

-- DELETAR TODAS PERMISSOES DOS GRUPOS DE GESTAO INTERNA
DELETE FROM GRUPO_PERMISSAO WHERE ID_GRUPO IN 
(
    SELECT DISTINCT(G.ID) FROM GRUPO_PERMISSAO GP JOIN GRUPO G ON GP.ID_GRUPO = G.ID WHERE G.GESTAO_INTERNA = 'S'
)

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES DO PERFIL E E GRUPO DE GESTÃO INTERNA PARA USUÁRIOS SUPORTE
----------------------------------------------------------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- TODAS AS PERMISSOES PARA O PERFIL SUPORTE

DECLARE @perfilSuporte int = (SELECT ID FROM PERFIL WHERE TIPO = 'AD')

INSERT INTO PERMISSAO_PERFIL (ID_PERFIL, ID_PERMISSAO)
SELECT 
    @perfilSuporte,
    A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN PERMISSAO_PERFIL B ON A.ID = B.ID_PERMISSAO AND B.ID_PERFIL = @perfilSuporte 
WHERE 
    B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- TODAS AS PERMISSOES PARA O GRUPO SUPORTE

DECLARE @grupoSuporte int = (SELECT ID FROM GRUPO WHERE TIPO = 'AD' AND GESTAO_INTERNA = 'S')

INSERT INTO GRUPO_PERMISSAO (ID_GRUPO, ID_PERMISSAO)
SELECT 
    @grupoSuporte,
    A.ID 
FROM 
    PERMISSAO A 
    LEFT JOIN GRUPO_PERMISSAO B ON A.ID = B.ID_PERMISSAO AND B.ID_GRUPO = @grupoSuporte
WHERE 
    B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES DO PERFIL E E GRUPO DE GESTÃO INTERNA PARA USUÁRIOS MASTER
----------------------------------------------------------------------------------------------------------------------------------------------------------

DECLARE @permissoesMasterUsuario TABLE (permissao varchar(200))
-- apoio
INSERT @permissoesMasterUsuario VALUES('BANCO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CEST_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CFOP_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CNAE_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CSOSN_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CST_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CTISS_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('NCM_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('ORIGEM_MERCADORIA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('SITUACAO_TRIBUTARIA_VISUALIZAR')
--INSERT @permissoesMasterUsuario VALUES('NATUREZA_OPERACAO_VISUALIZAR')
-- cadastro
INSERT @permissoesMasterUsuario VALUES('EMPRESA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('EMPRESA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CONTA_BANCARIA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CONTA_BANCARIA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CLIENTE_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CLIENTE_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('PLANO_CONTA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('PLANO_CONTA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CATEGORIA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CATEGORIA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('PRODUTO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('PRODUTO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('SERVICO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('SERVICO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CENTRO_CUSTO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CENTRO_CUSTO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('UNIDADE_MEDIDA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('UNIDADE_MEDIDA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('FORMA_PAGAMENTO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('FORMA_PAGAMENTO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('FORNECEDOR_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('FORNECEDOR_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('TRANSPORTADORA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('TRANSPORTADORA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('MOTIVO_CANCELAMENTO_CONTA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('MOTIVO_CANCELAMENTO_CONTA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CONTABILIDADE_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CONTABILIDADE_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('ESTOQUE_PRODUTO')
INSERT @permissoesMasterUsuario VALUES('EXTRATO_ESTOQUE_PRODUTO')
-- escola
INSERT @permissoesMasterUsuario VALUES('AREA_ATUACAO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('AREA_ATUACAO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('PROFESSOR_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('PROFESSOR_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CURSO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CURSO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('SOLICITANTE_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('SOLICITANTE_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('TURMA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('TURMA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('ALUNO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('ALUNO_VISUALIZAR')
-- transacao
INSERT @permissoesMasterUsuario VALUES('VENDA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('VENDA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CONTA_RECEBER_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CONTA_RECEBER_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('ORDEM_SERVICO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('ORDEM_SERVICO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CONTRATO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CONTRATO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('COMPRA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('COMPRA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('ORCAMENTO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('ORCAMENTO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('CONTA_PAGAR_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('CONTA_PAGAR_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('NOTA_FISCAL_PRODUTO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('NOTA_FISCAL_PRODUTO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('NOTA_FISCAL_SERVICO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('NOTA_FISCAL_SERVICO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('AJUSTE_CONTRATO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('AJUSTE_CONTRATO_VISUALIZAR')
-- financeiro
INSERT @permissoesMasterUsuario VALUES('EXTRATO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('BOLETO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('REMESSA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('REMESSA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('RETORNO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('RETORNO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('EMITIR_BOLETO')
INSERT @permissoesMasterUsuario VALUES('FLUXO_CAIXA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('DRE_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('IMPORTACAO_CONTA')
INSERT @permissoesMasterUsuario VALUES('ANALISE_ORCAMENTARIA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('TRANSFERENCIA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('TRANSFERENCIA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('INTEGRACAO_BANCARIA_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('INTEGRACAO_BANCARIA_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('LANCAMENTO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('FECHAMENTO_CONTABIL_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('FECHAMENTO_CONTABIL_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('INCLUIR_BALANCETE_VERIFICACAO')
-- seguranca
INSERT @permissoesMasterUsuario VALUES('PERFIL_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('PERFIL_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('PERMISSAO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('GRUPO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('GRUPO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('USUARIO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('USUARIO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('FUNCAO_AJUDA_VISUALIZAR')
-- pessoa
INSERT @permissoesMasterUsuario VALUES('FUNCIONARIO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('FUNCIONARIO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('REGISTRO_PONTO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('PONTO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('ABONO_PONTO_GERENCIAR')
INSERT @permissoesMasterUsuario VALUES('ABONO_PONTO_VISUALIZAR')
INSERT @permissoesMasterUsuario VALUES('MANUTENCAO_PONTO_GERENCIAR')
-- relatorio
INSERT @permissoesMasterUsuario VALUES('RELATORIO_ANALISE_PAGAMENTO')
INSERT @permissoesMasterUsuario VALUES('RELATORIO_ANALISE_RECEBIMENTO')
INSERT @permissoesMasterUsuario VALUES('RELATORIO_LANCAMENTO_CAIXA')
INSERT @permissoesMasterUsuario VALUES('RELATORIO_GIRO_ESTOQUE')
INSERT @permissoesMasterUsuario VALUES('RELATORIO_MOVIMENTO_ESTOQUE')
INSERT @permissoesMasterUsuario VALUES('RELATORIO_VENDA_CLIENTE')
INSERT @permissoesMasterUsuario VALUES('RELATORIO_VENDA_VENDEDOR')
INSERT @permissoesMasterUsuario VALUES('RELATORIO_SERVICO_CLIENTE')
INSERT @permissoesMasterUsuario VALUES('RELATORIO_MAIS_VENDIDOS')

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O PERFIL USUARIO MASTER 

DECLARE @perfilMasterUsuario int  = (SELECT ID FROM PERFIL WHERE TIPO = 'MU')

INSERT INTO PERMISSAO_PERFIL (ID_PERFIL, ID_PERMISSAO)
SELECT 
    @perfilMasterUsuario, A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN PERMISSAO_PERFIL B ON A.ID = B.ID_PERMISSAO AND B.ID_PERFIL = @perfilMasterUsuario 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesMasterUsuario) 
    AND B.ID IS NULL 

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O GRUPO USUARIO MASTER

DECLARE @grupoMasterUsuario int = (SELECT ID FROM GRUPO WHERE TIPO = 'MU' AND GESTAO_INTERNA = 'S')

INSERT INTO GRUPO_PERMISSAO (ID_GRUPO, ID_PERMISSAO)
SELECT
    @grupoMasterUsuario,
    A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN GRUPO_PERMISSAO B ON A.ID = B.ID_PERMISSAO AND B.ID_GRUPO = @grupoMasterUsuario 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesMasterUsuario) 
    AND B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES DO PERFIL E E GRUPO DE GESTÃO INTERNA PARA USUÁRIOS COMUNS
----------------------------------------------------------------------------------------------------------------------------------------------------------

DECLARE @permissoesUsuarioComum TABLE (permissao varchar(200))
-- apoio
INSERT @permissoesUsuarioComum VALUES('BANCO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CEST_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CFOP_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CNAE_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CSOSN_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CST_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CTISS_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('NCM_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('ORIGEM_MERCADORIA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('SITUACAO_TRIBUTARIA_VISUALIZAR')
--INSERT @permissoesUsuarioComum VALUES('NATUREZA_OPERACAO_VISUALIZAR')
-- cadastro
INSERT @permissoesUsuarioComum VALUES('EMPRESA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CONTA_BANCARIA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CLIENTE_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('PLANO_CONTA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CATEGORIA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('PRODUTO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('SERVICO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CENTRO_CUSTO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('UNIDADE_MEDIDA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('FORMA_PAGAMENTO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('FORNECEDOR_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('TRANSPORTADORA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('MOTIVO_CANCELAMENTO_CONTA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CONTABILIDADE_VISUALIZAR')
-- escola
INSERT @permissoesUsuarioComum VALUES('AREA_ATUACAO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('PROFESSOR_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CURSO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('SOLICITANTE_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('TURMA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('ALUNO_VISUALIZAR')
-- transacao
INSERT @permissoesUsuarioComum VALUES('VENDA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CONTA_RECEBER_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('ORDEM_SERVICO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CONTRATO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('COMPRA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('CONTA_PAGAR_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('NOTA_FISCAL_PRODUTO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('NOTA_FISCAL_SERVICO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('AJUSTE_CONTRATO_VISUALIZAR')
-- financeiro
INSERT @permissoesUsuarioComum VALUES('EXTRATO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('BOLETO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('EMITIR_BOLETO')
INSERT @permissoesUsuarioComum VALUES('REMESSA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('RETORNO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('FLUXO_CAIXA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('DRE_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('ANALISE_ORCAMENTARIA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('TRANSFERENCIA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('INTEGRACAO_BANCARIA_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('LANCAMENTO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('FECHAMENTO_CONTABIL_VISUALIZAR')
-- seguranca
INSERT @permissoesUsuarioComum VALUES('PERFIL_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('GRUPO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('USUARIO_GERENCIAR')
INSERT @permissoesUsuarioComum VALUES('USUARIO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('FUNCAO_AJUDA_VISUALIZAR')
-- pessoa
INSERT @permissoesUsuarioComum VALUES('FUNCIONARIO_VISUALIZAR')
INSERT @permissoesUsuarioComum VALUES('REGISTRO_PONTO_GERENCIAR')
INSERT @permissoesUsuarioComum VALUES('PONTO_GERENCIAR')
INSERT @permissoesUsuarioComum VALUES('ABONO_PONTO_VISUALIZAR')
-- relatorio
INSERT @permissoesUsuarioComum VALUES('RELATORIO_ANALISE_PAGAMENTO')
INSERT @permissoesUsuarioComum VALUES('RELATORIO_ANALISE_RECEBIMENTO')
INSERT @permissoesUsuarioComum VALUES('RELATORIO_LANCAMENTO_CAIXA')
INSERT @permissoesUsuarioComum VALUES('RELATORIO_GIRO_ESTOQUE')
INSERT @permissoesUsuarioComum VALUES('RELATORIO_MOVIMENTO_ESTOQUE')
INSERT @permissoesUsuarioComum VALUES('RELATORIO_VENDA_CLIENTE')
INSERT @permissoesUsuarioComum VALUES('RELATORIO_VENDA_VENDEDOR')
INSERT @permissoesUsuarioComum VALUES('RELATORIO_SERVICO_CLIENTE')
INSERT @permissoesUsuarioComum VALUES('RELATORIO_MAIS_VENDIDOS')

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O PERFIL USUARIO COMUM

DECLARE @perfilUsuarioComum int  = (SELECT ID FROM PERFIL WHERE TIPO = 'UC')

INSERT INTO PERMISSAO_PERFIL (ID_PERFIL, ID_PERMISSAO)
SELECT 
    @perfilUsuarioComum, A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN PERMISSAO_PERFIL B ON A.ID = B.ID_PERMISSAO AND B.ID_PERFIL = @perfilUsuarioComum 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesUsuarioComum) 
    AND B.ID IS NULL 

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O GRUPO USUARIO COMUM

DECLARE @grupoUsuarioComum int = (SELECT ID FROM GRUPO WHERE TIPO = 'UC' AND GESTAO_INTERNA = 'S')

INSERT INTO GRUPO_PERMISSAO (ID_GRUPO, ID_PERMISSAO)
SELECT
    @grupoUsuarioComum,
    A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN GRUPO_PERMISSAO B ON A.ID = B.ID_PERMISSAO AND B.ID_GRUPO = @grupoUsuarioComum 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesUsuarioComum) 
    AND B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES DO PERFIL E E GRUPO DE GESTÃO INTERNA PARA VENDEDORES MASTER
----------------------------------------------------------------------------------------------------------------------------------------------------------

DECLARE @permissoesMasterVendedor TABLE (permissao varchar(200))
-- apoio
INSERT @permissoesMasterVendedor VALUES('BANCO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CEST_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CFOP_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CNAE_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CSOSN_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CST_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CTISS_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('NCM_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('ORIGEM_MERCADORIA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('SITUACAO_TRIBUTARIA_VISUALIZAR')
--INSERT @permissoesMasterVendedor VALUES('NATUREZA_OPERACAO_VISUALIZAR')
-- cadastro
INSERT @permissoesMasterVendedor VALUES('EMPRESA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CONTA_BANCARIA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('CONTA_BANCARIA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CLIENTE_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('CLIENTE_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('PLANO_CONTA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('PLANO_CONTA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CATEGORIA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('CATEGORIA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('PRODUTO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('PRODUTO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('SERVICO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('SERVICO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CENTRO_CUSTO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('CENTRO_CUSTO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('UNIDADE_MEDIDA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('UNIDADE_MEDIDA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('FORMA_PAGAMENTO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('FORMA_PAGAMENTO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('FORNECEDOR_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('FORNECEDOR_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('TRANSPORTADORA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('TRANSPORTADORA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('MOTIVO_CANCELAMENTO_CONTA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('MOTIVO_CANCELAMENTO_CONTA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CONTABILIDADE_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('CONTABILIDADE_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('ESTOQUE_PRODUTO')
INSERT @permissoesMasterVendedor VALUES('EXTRATO_ESTOQUE_PRODUTO')
-- transacao
INSERT @permissoesMasterVendedor VALUES('VENDA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('VENDA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CONTA_RECEBER_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('CONTA_RECEBER_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('ORDEM_SERVICO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('ORDEM_SERVICO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CONTRATO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('CONTRATO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('COMPRA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('COMPRA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('CONTA_PAGAR_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('CONTA_PAGAR_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('NOTA_FISCAL_PRODUTO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('NOTA_FISCAL_PRODUTO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('NOTA_FISCAL_SERVICO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('NOTA_FISCAL_SERVICO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('AJUSTE_CONTRATO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('AJUSTE_CONTRATO_VISUALIZAR')
-- financeiro
INSERT @permissoesMasterVendedor VALUES('EXTRATO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('BOLETO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('EMITIR_BOLETO')
INSERT @permissoesMasterVendedor VALUES('REMESSA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('RETORNO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('FLUXO_CAIXA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('DRE_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('IMPORTACAO_CONTA')
INSERT @permissoesMasterVendedor VALUES('ANALISE_ORCAMENTARIA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('TRANSFERENCIA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('TRANSFERENCIA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('INTEGRACAO_BANCARIA_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('INTEGRACAO_BANCARIA_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('LANCAMENTO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('INCLUIR_BALANCETE_VERIFICACAO')
-- seguranca
INSERT @permissoesMasterVendedor VALUES('PERFIL_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('PERFIL_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('PERMISSAO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('GRUPO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('GRUPO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('USUARIO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('USUARIO_VISUALIZAR')
INSERT @permissoesMasterVendedor VALUES('FUNCAO_AJUDA_VISUALIZAR')
-- pessoa
INSERT @permissoesMasterVendedor VALUES('REGISTRO_PONTO_GERENCIAR')
INSERT @permissoesMasterVendedor VALUES('PONTO_GERENCIAR')
-- relatorio
INSERT @permissoesMasterVendedor VALUES('RELATORIO_ANALISE_PAGAMENTO')
INSERT @permissoesMasterVendedor VALUES('RELATORIO_ANALISE_RECEBIMENTO')
INSERT @permissoesMasterVendedor VALUES('RELATORIO_LANCAMENTO_CAIXA')
INSERT @permissoesMasterVendedor VALUES('RELATORIO_GIRO_ESTOQUE')
INSERT @permissoesMasterVendedor VALUES('RELATORIO_MOVIMENTO_ESTOQUE')
INSERT @permissoesMasterVendedor VALUES('RELATORIO_VENDA_CLIENTE')
INSERT @permissoesMasterVendedor VALUES('RELATORIO_VENDA_VENDEDOR')
INSERT @permissoesMasterVendedor VALUES('RELATORIO_SERVICO_CLIENTE')
INSERT @permissoesMasterVendedor VALUES('RELATORIO_MAIS_VENDIDOS')

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O PERFIL VENDEDOR MASTER

DECLARE @perfilMasterVendedor int  = (SELECT ID FROM PERFIL WHERE TIPO = 'MV')

INSERT INTO PERMISSAO_PERFIL (ID_PERFIL, ID_PERMISSAO)
SELECT 
    @perfilMasterVendedor, A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN PERMISSAO_PERFIL B ON A.ID = B.ID_PERMISSAO AND B.ID_PERFIL = @perfilMasterVendedor 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesMasterVendedor) 
    AND B.ID IS NULL 

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O GRUPO VENDEDOR MASTER 

DECLARE @grupoMasterVendedor int = (SELECT ID FROM GRUPO WHERE TIPO = 'MV' AND GESTAO_INTERNA = 'S')

INSERT INTO GRUPO_PERMISSAO (ID_GRUPO, ID_PERMISSAO)
SELECT
    @grupoMasterVendedor,
    A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN GRUPO_PERMISSAO B ON A.ID = B.ID_PERMISSAO AND B.ID_GRUPO = @grupoMasterVendedor 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesMasterVendedor) 
    AND B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES DO PERFIL E E GRUPO DE GESTÃO INTERNA PARA VENDEDOR
----------------------------------------------------------------------------------------------------------------------------------------------------------

DECLARE @permissoesVendedor TABLE (permissao varchar(200))
-- apoio
INSERT @permissoesVendedor VALUES('BANCO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CEST_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CFOP_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CNAE_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CSOSN_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CST_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CTISS_VISUALIZAR')
INSERT @permissoesVendedor VALUES('NCM_VISUALIZAR')
INSERT @permissoesVendedor VALUES('ORIGEM_MERCADORIA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('SITUACAO_TRIBUTARIA_VISUALIZAR')
--INSERT @permissoesVendedor VALUES('NATUREZA_OPERACAO_VISUALIZAR')
-- cadastro
INSERT @permissoesVendedor VALUES('EMPRESA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CONTA_BANCARIA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CLIENTE_VISUALIZAR')
INSERT @permissoesVendedor VALUES('PLANO_CONTA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CATEGORIA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('PRODUTO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('SERVICO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CENTRO_CUSTO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('UNIDADE_MEDIDA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('FORNECEDOR_VISUALIZAR')
INSERT @permissoesVendedor VALUES('TRANSPORTADORA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('MOTIVO_CANCELAMENTO_CONTA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CONTABILIDADE_VISUALIZAR')
-- transacao
INSERT @permissoesVendedor VALUES('VENDA_GERENCIAR')
INSERT @permissoesVendedor VALUES('VENDA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CONTA_RECEBER_GERENCIAR')
INSERT @permissoesVendedor VALUES('CONTA_RECEBER_VISUALIZAR')
INSERT @permissoesVendedor VALUES('ORDEM_SERVICO_GERENCIAR')
INSERT @permissoesVendedor VALUES('ORDEM_SERVICO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CONTRATO_GERENCIAR')
INSERT @permissoesVendedor VALUES('CONTRATO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('COMPRA_GERENCIAR')
INSERT @permissoesVendedor VALUES('COMPRA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('CONTA_PAGAR_GERENCIAR')
INSERT @permissoesVendedor VALUES('CONTA_PAGAR_VISUALIZAR')
INSERT @permissoesVendedor VALUES('NOTA_FISCAL_PRODUTO_GERENCIAR')
INSERT @permissoesVendedor VALUES('NOTA_FISCAL_PRODUTO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('NOTA_FISCAL_SERVICO_GERENCIAR')
INSERT @permissoesVendedor VALUES('NOTA_FISCAL_SERVICO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('AJUSTE_CONTRATO_GERENCIAR')
INSERT @permissoesVendedor VALUES('AJUSTE_CONTRATO_VISUALIZAR')
-- financeiro
INSERT @permissoesVendedor VALUES('EXTRATO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('BOLETO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('EMITIR_BOLETO')
INSERT @permissoesVendedor VALUES('REMESSA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('RETORNO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('FLUXO_CAIXA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('DRE_VISUALIZAR')
INSERT @permissoesVendedor VALUES('ANALISE_ORCAMENTARIA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('TRANSFERENCIA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('INTEGRACAO_BANCARIA_VISUALIZAR')
INSERT @permissoesVendedor VALUES('LANCAMENTO_VISUALIZAR')
-- seguranca
INSERT @permissoesVendedor VALUES('PERFIL_VISUALIZAR')
INSERT @permissoesVendedor VALUES('GRUPO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('USUARIO_GERENCIAR')
INSERT @permissoesVendedor VALUES('USUARIO_VISUALIZAR')
INSERT @permissoesVendedor VALUES('FUNCAO_AJUDA_VISUALIZAR')
-- pessoa
INSERT @permissoesVendedor VALUES('REGISTRO_PONTO_GERENCIAR')
INSERT @permissoesVendedor VALUES('PONTO_GERENCIAR')
-- relatorio
INSERT @permissoesVendedor VALUES('RELATORIO_ANALISE_PAGAMENTO')
INSERT @permissoesVendedor VALUES('RELATORIO_ANALISE_RECEBIMENTO')
INSERT @permissoesVendedor VALUES('RELATORIO_LANCAMENTO_CAIXA')
INSERT @permissoesVendedor VALUES('RELATORIO_GIRO_ESTOQUE')
INSERT @permissoesVendedor VALUES('RELATORIO_MOVIMENTO_ESTOQUE')
INSERT @permissoesVendedor VALUES('RELATORIO_VENDA_CLIENTE')
INSERT @permissoesVendedor VALUES('RELATORIO_VENDA_VENDEDOR')
INSERT @permissoesVendedor VALUES('RELATORIO_SERVICO_CLIENTE')
INSERT @permissoesVendedor VALUES('RELATORIO_MAIS_VENDIDOS')

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O PERFIL VENDEDOR

DECLARE @perfilVendedor int  = (SELECT ID FROM PERFIL WHERE TIPO = 'V')

INSERT INTO PERMISSAO_PERFIL (ID_PERFIL, ID_PERMISSAO)
SELECT 
    @perfilVendedor, A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN PERMISSAO_PERFIL B ON A.ID = B.ID_PERMISSAO AND B.ID_PERFIL = @perfilVendedor 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesVendedor) 
    AND B.ID IS NULL 

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O GRUPO VENDEDOR rollback

DECLARE @grupoVendedor int = (SELECT ID FROM GRUPO WHERE TIPO = 'V' AND GESTAO_INTERNA = 'S')

INSERT INTO GRUPO_PERMISSAO (ID_GRUPO, ID_PERMISSAO)
SELECT
    @grupoVendedor,
    A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN GRUPO_PERMISSAO B ON A.ID = B.ID_PERMISSAO AND B.ID_GRUPO = @grupoVendedor 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesVendedor) 
    AND B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES DO PERFIL E E GRUPO DE GESTÃO INTERNA PARA FUNCIONÁRIOS
----------------------------------------------------------------------------------------------------------------------------------------------------------

DECLARE @permissoesFuncionario TABLE (permissao varchar(200))
INSERT @permissoesFuncionario VALUES('REGISTRO_PONTO_GERENCIAR')
INSERT @permissoesFuncionario VALUES('PONTO_GERENCIAR')

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O PERFIL FUNCIONÁRIOS

DECLARE @perfilFuncionario int  = (SELECT ID FROM PERFIL WHERE TIPO = 'FR')

INSERT INTO PERMISSAO_PERFIL (ID_PERFIL, ID_PERMISSAO)
SELECT 
    @perfilFuncionario, A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN PERMISSAO_PERFIL B ON A.ID = B.ID_PERMISSAO AND B.ID_PERFIL = @perfilFuncionario 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesFuncionario) 
    AND B.ID IS NULL 

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O GRUPO FUNCIONARIO

DECLARE @grupoFuncionario int = (SELECT ID FROM GRUPO WHERE TIPO = 'FR' AND GESTAO_INTERNA = 'S')

INSERT INTO GRUPO_PERMISSAO (ID_GRUPO, ID_PERMISSAO)
SELECT
    @grupoFuncionario,
    A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN GRUPO_PERMISSAO B ON A.ID = B.ID_PERMISSAO AND B.ID_GRUPO = @grupoFuncionario 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesFuncionario) 
    AND B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES DO PERFIL E E GRUPO DE GESTÃO INTERNA PARA MASTER CONTABILIDADE
----------------------------------------------------------------------------------------------------------------------------------------------------------

DECLARE @permissoesMasterContabilidade TABLE (permissao varchar(200))
INSERT @permissoesMasterContabilidade VALUES('EXPORTAR_ARQUIVO')
INSERT @permissoesMasterContabilidade VALUES('CONTABILIDADE_GERENCIAR')
INSERT @permissoesMasterContabilidade VALUES('CONTABILIDADE_VISUALIZAR')
INSERT @permissoesMasterContabilidade VALUES('USUARIO_GERENCIAR')
INSERT @permissoesMasterContabilidade VALUES('INCLUIR_BALANCETE_VERIFICACAO')
INSERT @permissoesMasterContabilidade VALUES('FLUXO_CAIXA_VISUALIZAR')
INSERT @permissoesMasterContabilidade VALUES('DRE_VISUALIZAR')

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O PERFIL FUNCIONÁRIOS

DECLARE @perfilMasterContabilidade int  = (SELECT ID FROM PERFIL WHERE TIPO = 'MC')

INSERT INTO PERMISSAO_PERFIL (ID_PERFIL, ID_PERMISSAO)
SELECT 
    @perfilMasterContabilidade, A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN PERMISSAO_PERFIL B ON A.ID = B.ID_PERMISSAO AND B.ID_PERFIL = @perfilMasterContabilidade 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesMasterContabilidade) 
    AND B.ID IS NULL 

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O GRUPO FUNCIONARIO

DECLARE @grupoMasterContabilidade int = (SELECT ID FROM GRUPO WHERE TIPO = 'MC' AND GESTAO_INTERNA = 'S')

INSERT INTO GRUPO_PERMISSAO (ID_GRUPO, ID_PERMISSAO)
SELECT
    @grupoMasterContabilidade,
    A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN GRUPO_PERMISSAO B ON A.ID = B.ID_PERMISSAO AND B.ID_GRUPO = @grupoMasterContabilidade 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesMasterContabilidade) 
    AND B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERINDO PERMISSOES DO PERFIL E E GRUPO DE GESTÃO INTERNA PARA CONTABILIDADE
----------------------------------------------------------------------------------------------------------------------------------------------------------

DECLARE @permissoesContabilidade TABLE (permissao varchar(200))
INSERT @permissoesContabilidade VALUES('EXPORTAR_ARQUIVO')
INSERT @permissoesContabilidade VALUES('USUARIO_GERENCIAR')
INSERT @permissoesContabilidade VALUES('INCLUIR_BALANCETE_VERIFICACAO')
INSERT @permissoesContabilidade VALUES('FLUXO_CAIXA_VISUALIZAR')
INSERT @permissoesContabilidade VALUES('DRE_VISUALIZAR')

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O PERFIL FUNCIONÁRIOS

DECLARE @perfilContabilidade int  = (SELECT ID FROM PERFIL WHERE TIPO = 'C')

INSERT INTO PERMISSAO_PERFIL (ID_PERFIL, ID_PERMISSAO)
SELECT 
    @perfilContabilidade, A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN PERMISSAO_PERFIL B ON A.ID = B.ID_PERMISSAO AND B.ID_PERFIL = @perfilContabilidade 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesContabilidade) 
    AND B.ID IS NULL 

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- PERMISSAO PARA O GRUPO FUNCIONARIO

DECLARE @grupoContabilidade int = (SELECT ID FROM GRUPO WHERE TIPO = 'C' AND GESTAO_INTERNA = 'S')

INSERT INTO GRUPO_PERMISSAO (ID_GRUPO, ID_PERMISSAO)
SELECT
    @grupoContabilidade,
    A.ID
FROM 
    PERMISSAO A 
    LEFT JOIN GRUPO_PERMISSAO B ON A.ID = B.ID_PERMISSAO AND B.ID_GRUPO = @grupoContabilidade 
WHERE 
    A.DESCRICAO IN (SELECT PERMISSAO FROM @permissoesContabilidade) 
    AND B.ID IS NULL

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERIR GRUPO PADROES A EMPRESAS QUE AINDA FALTAM
----------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO GRUPO_EMPRESA(ID_EMPRESA, ID_GRUPO)
SELECT 
	e.ID, g.id 
FROM 
	EMPRESA e 
	LEFT JOIN GRUPO g ON g.GESTAO_INTERNA = 'S' 
	LEFT JOIN GRUPO_EMPRESA ge ON ge.ID_EMPRESA = e.id AND ge.ID_GRUPO = g.id 
WHERE 
	ge.id is null

----------------------------------------------------------------------------------------------------------------------------------------------------------
-- INSERIR GRUPO SUPORTE TODOS USUARIOS SUPORTE
----------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO USUARIO_GRUPO_EMPRESA (ID_USUARIO, ID_GRUPO_EMPRESA)
SELECT a.ID, b.ID FROM 
    USUARIO a 
    LEFT JOIN GRUPO_EMPRESA b ON 1 = 1 
    LEFT JOIN USUARIO_GRUPO_EMPRESA c ON a.id = c.ID_USUARIO AND b.id = c.ID_GRUPO_EMPRESA 
    JOIN GRUPO g ON b.ID_GRUPO = g.id AND g.TIPO = 'AD' AND g.GESTAO_INTERNA = 'S'
WHERE 
    c.id is null and a.ID_PERFIL = (SELECT ID FROM PERFIL WHERE TIPO = 'AD')

----------------------------------------------------------------------------------------------------------------------------------------------------------