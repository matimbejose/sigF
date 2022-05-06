$(function () {
    $('.telefone')
            .mask("(99) 9999-9999?9")
            .keydown(function () {
                var $elem = $(this);
                var tamanhoAnterior = this.value.replace(/\D/g, '').length;
                setTimeout(function () {
                    var novoTamanho = $elem.val().replace(/\D/g, '').length;
                    if (novoTamanho !== tamanhoAnterior) {
                        if (novoTamanho === 11) {
                            $elem.unmask();
                            $elem.mask("(99) 99999-9999");
                        } else if (tamanhoAnterior > novoTamanho) {
                            if (novoTamanho === 10) {
                                $elem.unmask();
                                $elem.mask("(99) 9999-9999?9");
                            }
                        }
                    }
                }, 1);
            });
});