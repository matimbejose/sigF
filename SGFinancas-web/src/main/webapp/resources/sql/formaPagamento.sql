
SELECT 
	'INSERT INTO FORMA_PAGAMENTO(DESCRICAO, TENAT_ID) VALUES (''Em espécie'','+ cast(TENAT_ID as varchar)+')'
FROM 
	EMPRESA 

SELECT 
	'INSERT INTO FORMA_PAGAMENTO(DESCRICAO, TENAT_ID) VALUES (''Cartão de crédito'','+ cast(TENAT_ID as varchar)+')'
FROM 
	EMPRESA 

SELECT 
	'INSERT INTO FORMA_PAGAMENTO(DESCRICAO, TENAT_ID) VALUES (''Cartão de débito'','+ cast(TENAT_ID as varchar)+')'
FROM 
	EMPRESA 

SELECT 
	'INSERT INTO FORMA_PAGAMENTO(DESCRICAO, TENAT_ID) VALUES (''Transferência bancária'','+ cast(TENAT_ID as varchar)+')'
FROM 
	EMPRESA 


SELECT 
	'INSERT INTO FORMA_PAGAMENTO(DESCRICAO, TENAT_ID) VALUES (''Passe fácil'','+ cast(TENAT_ID as varchar)+')'
FROM 
	EMPRESA 

SELECT 
	'INSERT INTO FORMA_PAGAMENTO(DESCRICAO, TENAT_ID) VALUES (''Visa vale'','+ cast(TENAT_ID as varchar)+')'
FROM 
	EMPRESA 
