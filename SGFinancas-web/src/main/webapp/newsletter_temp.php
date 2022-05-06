<?php

// Passando os dados obtidos pelo formulário para as variáveis abaixo

$emailremetente     = trim($_POST['newsletterEmail']);
$emaildestinatario  = 'comercial@sgfinancas.com'; // Digite seu e-mail aqui, lembrando que o e-mail deve estar em seu servidor web
$assunto            = "newsletter";

 
 
/* Montando a mensagem a ser enviada no corpo do e-mail. */
$mensagemHTML = '<P>FORMULARIO PREENCHIDO NO SITE WWW.SGFINANCAS.COM</P>
<p><b>Nome:</b> '.$nomeremetente.'
<p><b>E-Mail:</b> '.$emailremetente.'
<p><b>Assunto:</b> '.$assunto.'
<p><b>Mensagem para assinatura:</b> '.$emaildestinatario.'</p>
<hr>';


// O remetente deve ser um e-mail do seu domínio conforme determina a RFC 822.
// O return-path deve ser ser o mesmo e-mail do remetente.
$headers = "MIME-Version: 1.1\n";
$headers .= "Content-type: text/html; charset=utf-8\n";
$headers .= "From: $emailremetente\n"; // remetente
$headers .= "Return-Path: $emaildestinatario \n"; // return-path
$envio = mail($emaildestinatario, $assunto, $mensagemHTML, $headers); 
 

if($envio){//grava mensagem de confirmação do envio de email
	$msg ='Assinatura feita com sucesso';
}else{
	$msg ='Erro na assinatura do email';
}

echo $msg;//envia a variavel para a pagina onde o ajax irar tratar a mensagem

?>
