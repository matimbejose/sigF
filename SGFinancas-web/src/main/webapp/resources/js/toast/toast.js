class VillefortToast {
    constructor(options = {}) {
        this.options = {
            closeButton: true,
            debug: false,
            positionClass: `toast-top-right`,
            onclick: null,
            showDuration: 500,
            hideDuration: 500,
            timeOut: 1E4,
            extendedTimeOut: 2E4,
            showEasing: `swing`,
            hideEasing: `linear`,
            showMethod: `fadeIn`,
            hideMethod: `fadeOut`
        };
        Object.keys(options).forEach(k => this.options[k] = options[k]);
        this.listaToasts = [];
    }

    updateConfig() {
        toastr.options = this.options;
    }

    showCustom(type, title, message, onclick = null) {
        this.updateConfig();
        if (onclick) {
            toastr.options.onclick = onclick;
        }
        toastr[type](message, title);
    }

    setDadosEmpresa(mostrar) {
        this.updateConfig();
        if (!mostrar) {
            return;
        }
        this.listaToasts.push({
            type: `warning`,
            message: `As informações da sua empresa precisam ser completadas.`,
            title: `Cadastro da sua empresa`
        });

    }

    setEmpresaExpirou(qtd) {
        this.updateConfig();
        if (qtd <= 0) {
            return;
        }
        this.listaToasts.push({
            type: `error`,
            message: `Existe(m) ${qtd} empresas que já expiraram o prazo de gratuidade!`,
            title: `Credenciamento`
        });

    }

    setContatosCliente(qtd) {
        this.updateConfig();
        if (qtd <= 0) {
            return;
        }
        this.listaToasts.push({
            type: `info`,
            message: `Existe(m) ${qtd} cliente(s) para entrar em contato hoje!`,
            title: `Contato cliente`
        });

    }

    setContaCorrente(mostrar) {
        this.updateConfig();
        if (!mostrar) {
            return;
        }
        this.listaToasts.push({
            type: `warning`,
            message: `Você ainda não cadastrou nenhuma conta bancária.`,
            title: `Cadastro de conta bancária`,
            onclick: () => location.href = `/restrito/contaBancaria/cadastroContaBancaria.xhtml`
        });

    }

    setNotaFiscal(mostrar) {
        this.updateConfig();
        if (!mostrar) {
            return;
        }
        this.listaToasts.push({
            type: `info`,
            message: `Não foi informado o certificado digital para emissão de NF-e.`,
            title: `Emissão de NF-e`
        });

    }

    setNotaFiscalParametro(mostrar) {
        this.updateConfig();
        if (!mostrar) {
            return;
        }
        this.listaToasts.push({
            type: `info`,
            message: `Não foi informado o número da última NF-e emitida e série de entrada/saída.`,
            title: `Emissão de NF-e`
        });

    }

    setImportacaoParametro(mostrar) {
        this.updateConfig();
        if (!mostrar) {
            return;
        }
        this.listaToasts.push({
            type: `info`,
            message: `Não foi informado a categoria de importação de produto ou serviço.`,
            title: `Importação de produtos e serviços`
        });

    }

    show() {
        let interval = setInterval(() => {
            const item = this.listaToasts.pop();
            if (item === undefined) {
                return clearInterval(interval);
            }
            if (item.onclick) {
                toastr.options.onclick = item.onclick;
            }
            toastr[item.type](item.message, item.title);
        }, 400);
    }
}