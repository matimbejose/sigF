<?php

// Passando os dados obtidos pelo formulário para as variáveis abaixo
$nomeremetente      = $_POST['nome'];
$emailremetente     = trim($_POST['email']);
$cidade             = $_POST['cidade'];
$estado             = $_POST['estado'];
$emaildestinatario  = 'comercial@sgfinancas.com'; // Digite seu e-mail aqui, lembrando que o e-mail deve estar em seu servidor web
$assunto            = "Requerimento Representante";

if(!isset($_POST['msg'])){
    $mensagem  = $_POST['msg'];
}else{
    $mensagem  = "";
}

 
 
/* Montando a mensagem a ser enviada no corpo do e-mail. */
$mensagemHTML = '<P>FORMULARIO PREENCHIDO NO SITE WWW.SGFINANCAS.COM</P>
<p><b>Nome:</b> '.$nomeremetente.'
<p><b>E-Mail:</b> '.$emailremetente.'
<p><b>Assunto:</b> '.$assunto.'
<p><b>Cidade: </b> '.$cidade.'
<p><b>Estado: </b> '.$estado.'
<p><b>Mensagem:</b> '.$mensagem.'</p>
<hr>';


// O remetente deve ser um e-mail do seu domínio conforme determina a RFC 822.
// O return-path deve ser ser o mesmo e-mail do remetente.
$headers = "MIME-Version: 1.1\n";
$headers .= "Content-type: text/html; charset=utf-8\n";
$headers .= "From: $emailremetente\n"; // remetente
$headers .= "Return-Path: $emaildestinatario \n"; // return-path
$envio = mail($emaildestinatario, $assunto, $mensagemHTML, $headers); 
 

if($envio){//grava mensagem de confirmação do envio de email
	$msg ='passou';
}else{
	$msg ='erro';
}

echo $msg;//envia a variavel para a pagina onde o ajax irar tratar a mensagem

?>
