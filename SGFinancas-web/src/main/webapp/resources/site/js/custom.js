$('#newsletterForm').submit(function (){
    $.ajax({
        type: 'POST',
        url: 'newsletter_temp.php',
        data: $(this).serialize(),
    }).done(function (msg){
        alert(msg);
    });
    return false;
});