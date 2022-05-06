/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.nfs.sp.sp;

import java.util.Date;

/**
 *
 * @author desenvolvimento
 */
public class NFSeSPSP {

    public static class ChaveNFe {

        public String InscricaoPrestador;

        public String NumeroNFe;

        public String CodigoVerificacao;

    }

    public static class CPFCNPJPrestador {

        public String CNPJ;

    }

    public static class EnderecoPrestador {

        public String TipoLogradouro;

        public String Logradouro;

        public String NumeroEndereco;

        public String Bairro;

        public String Cidade;

        public String UF;

        public String CEP;

    }

    public static class CPFCNPJTomador {

        public String CPF;

    }

    public static class EnderecoTomador {

        public String TipoLogradouro;

        public String Logradouro;

        public String NumeroEndereco;

        public String ComplementoEndereco;

        public String Bairro;

        public String Cidade;

        public String UF;

        public String CEP;

    }

    public static class NFe {

        public ChaveNFe ChaveNFe;

        public Date DataEmissaoNFe;

        public CPFCNPJPrestador CPFCNPJPrestador;

        public String RazaoSocialPrestador;

        public EnderecoPrestador EnderecoPrestador;

        public String EmailPrestador;

        public String StatusNFe;

        public String TributacaoNFe;

        public int OpcaoSimples;

        public int NumeroGuia;

        public Date DataQuitacaoGuia;

        public double ValorServicos;

        public int CodigoServico;

        public double AliquotaServicos;

        public double ValorISS;

        public int ValorCredito;

        public boolean ISSRetido;

        public CPFCNPJTomador CPFCNPJTomador;

        public String RazaoSocialTomador;

        public EnderecoTomador EnderecoTomador;

        public String EmailTomador;

        public String Discriminacao;

        public Object FonteCargaTributaria;

        public Object xmlns;

        public String text;

    }

}
