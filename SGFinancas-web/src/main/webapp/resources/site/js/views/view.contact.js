$('#envio').submit(function (){
    $.ajax({
        type: 'POST',
        url: 'envia.php',
        data: $(this).serialize(),
    }).done(function (msg){
        if(msg == "passou"){
           $('#contactSuccess').removeClass('hidden');
        }else{
           $('#contactError').removeClass('hidden');
        }
    });
    return false;
}); 