$('#requerimento').submit(function (){
    $.ajax({
        type: 'POST',
        url: 'requerimento.php',
        data: $(this).serialize(),
    }).done(function (result){
        if(result == "passou"){
             $('#requerimentoSuccess').removeClass('hidden');
        }else{
             $('#requerimentoError').removeClass('hidden');
        }
    });
    return false;
})


