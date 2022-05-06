package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NFSeRelatorioDTO;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface NFeMapper {

    @Mapping(target = "numero", source = "idNFS.numeroNotaFiscalFormatada")
    @Mapping(target = "status", expression = "java(cp.getDataCancelamento() == null ? \"Normal\" : \"Cancelada\")")
    @Mapping(target = "dataEmissao", source = "idNFS.dataEmissao")
    @Mapping(target = "tomador", source = "idNFS.nomeCliente")
    @Mapping(target = "convenio", source = "idConta.idPlanoConta.descricao")
    @Mapping(target = "valor", source = "idNFS.valorTotal")
    @Mapping(target = "impostos", source = "idNFS.idCliente")
    @Mapping(target = "PISS", ignore = true)
    @Mapping(target = "PIR", ignore = true)
    @Mapping(target = "PPIS", ignore = true)
    @Mapping(target = "PCOFINS", ignore = true)
    @Mapping(target = "PCSLL", ignore = true)
    @Mapping(target = "situacao", expression = "java(cp)")
    NFSeRelatorioDTO toDTO(ContaParcela cp);

    NFe toMine(TNFe obj);

    TNFe toHis(NFe obj);

    NFe.InfNFeSupl toMine(TNFe.InfNFeSupl obj);

    TNFe.InfNFeSupl toHis(NFe.InfNFeSupl obj);

    NFe.InfNFe toMine(TNFe.InfNFe obj);

    TNFe.InfNFe toHis(NFe.InfNFe obj);

    NFe.InfNFe.Transp toMine(TNFe.InfNFe.Transp obj);

    TNFe.InfNFe.Transp toHis(NFe.InfNFe.Transp obj);

    NFe.InfNFe.Transp.Vol toMine(TNFe.InfNFe.Transp.Vol obj);

    TNFe.InfNFe.Transp.Vol toHis(NFe.InfNFe.Transp.Vol obj);

    NFe.InfNFe.Transp.Vol.Lacres toMine(TNFe.InfNFe.Transp.Vol.Lacres obj);

    TNFe.InfNFe.Transp.Vol.Lacres toHis(NFe.InfNFe.Transp.Vol.Lacres obj);

    @Mapping(target = "cpfCnpj", ignore = true)
    NFe.InfNFe.Transp.Transporta toMine(TNFe.InfNFe.Transp.Transporta obj);

    TNFe.InfNFe.Transp.Transporta toHis(NFe.InfNFe.Transp.Transporta obj);

    NFe.InfNFe.Transp.RetTransp toMine(TNFe.InfNFe.Transp.RetTransp obj);

    TNFe.InfNFe.Transp.RetTransp toHis(NFe.InfNFe.Transp.RetTransp obj);

    NFe.InfNFe.Total toMine(TNFe.InfNFe.Total obj);

    TNFe.InfNFe.Total toHis(NFe.InfNFe.Total obj);

    NFe.InfNFe.Total.RetTrib toMine(TNFe.InfNFe.Total.RetTrib obj);

    TNFe.InfNFe.Total.RetTrib toHis(NFe.InfNFe.Total.RetTrib obj);

    NFe.InfNFe.Total.ISSQNtot toMine(TNFe.InfNFe.Total.ISSQNtot obj);

    TNFe.InfNFe.Total.ISSQNtot toHis(NFe.InfNFe.Total.ISSQNtot obj);

    NFe.InfNFe.Total.ICMSTot toMine(TNFe.InfNFe.Total.ICMSTot obj);

    @Mapping(target = "VTotTrib", ignore = true)
    TNFe.InfNFe.Total.ICMSTot toHis(NFe.InfNFe.Total.ICMSTot obj);

    NFe.InfNFe.Pag toMine(TNFe.InfNFe.Pag obj);

    TNFe.InfNFe.Pag toHis(NFe.InfNFe.Pag obj);

    NFe.InfNFe.Pag.DetPag toMine(TNFe.InfNFe.Pag.DetPag obj);

    TNFe.InfNFe.Pag.DetPag toHis(NFe.InfNFe.Pag.DetPag obj);

    NFe.InfNFe.Pag.DetPag.Card toMine(TNFe.InfNFe.Pag.DetPag.Card obj);

    TNFe.InfNFe.Pag.DetPag.Card toHis(NFe.InfNFe.Pag.DetPag.Card obj);

    NFe.InfNFe.InfAdic toMine(TNFe.InfNFe.InfAdic obj);

    TNFe.InfNFe.InfAdic toHis(NFe.InfNFe.InfAdic obj);

    NFe.InfNFe.InfAdic.ProcRef toMine(TNFe.InfNFe.InfAdic.ProcRef obj);

    TNFe.InfNFe.InfAdic.ProcRef toHis(NFe.InfNFe.InfAdic.ProcRef obj);

    NFe.InfNFe.InfAdic.ObsFisco toMine(TNFe.InfNFe.InfAdic.ObsFisco obj);

    TNFe.InfNFe.InfAdic.ObsFisco toHis(NFe.InfNFe.InfAdic.ObsFisco obj);

    NFe.InfNFe.InfAdic.ObsCont toMine(TNFe.InfNFe.InfAdic.ObsCont obj);

    TNFe.InfNFe.InfAdic.ObsCont toHis(NFe.InfNFe.InfAdic.ObsCont obj);

    NFe.InfNFe.Ide toMine(TNFe.InfNFe.Ide obj);

    TNFe.InfNFe.Ide toHis(NFe.InfNFe.Ide obj);

    NFe.InfNFe.Ide.NFref toMine(TNFe.InfNFe.Ide.NFref obj);

    TNFe.InfNFe.Ide.NFref toHis(NFe.InfNFe.Ide.NFref obj);

    NFe.InfNFe.Ide.NFref.RefNFP toMine(TNFe.InfNFe.Ide.NFref.RefNFP obj);

    TNFe.InfNFe.Ide.NFref.RefNFP toHis(NFe.InfNFe.Ide.NFref.RefNFP obj);

    NFe.InfNFe.Ide.NFref.RefNF toMine(TNFe.InfNFe.Ide.NFref.RefNF obj);

    TNFe.InfNFe.Ide.NFref.RefNF toHis(NFe.InfNFe.Ide.NFref.RefNF obj);

    NFe.InfNFe.Ide.NFref.RefECF toMine(TNFe.InfNFe.Ide.NFref.RefECF obj);

    TNFe.InfNFe.Ide.NFref.RefECF toHis(NFe.InfNFe.Ide.NFref.RefECF obj);

    NFe.InfNFe.Exporta toMine(TNFe.InfNFe.Exporta obj);

    TNFe.InfNFe.Exporta toHis(NFe.InfNFe.Exporta obj);

    @Mapping(target = "cpfCnpj", ignore = true)
    NFe.InfNFe.Emit toMine(TNFe.InfNFe.Emit obj);

    TNFe.InfNFe.Emit toHis(NFe.InfNFe.Emit obj);

    @Mapping(target = "idCst", ignore = true)
    @Mapping(target = "idCsosn", ignore = true)
    NFe.InfNFe.Det toMine(TNFe.InfNFe.Det obj);

    TNFe.InfNFe.Det toHis(NFe.InfNFe.Det obj);

    @Mapping(target = "idNcm", ignore = true)
    @Mapping(target = "idCfop", ignore = true)
    @Mapping(target = "idProduto", ignore = true)
    NFe.InfNFe.Det.Prod toMine(TNFe.InfNFe.Det.Prod obj);

    TNFe.InfNFe.Det.Prod toHis(NFe.InfNFe.Det.Prod obj);

    NFe.InfNFe.Det.Prod.VeicProd toMine(TNFe.InfNFe.Det.Prod.VeicProd obj);

    TNFe.InfNFe.Det.Prod.VeicProd toHis(NFe.InfNFe.Det.Prod.VeicProd obj);

    NFe.InfNFe.Det.Prod.Rastro toMine(TNFe.InfNFe.Det.Prod.Rastro obj);

    TNFe.InfNFe.Det.Prod.Rastro toHis(NFe.InfNFe.Det.Prod.Rastro obj);

    NFe.InfNFe.Det.Prod.Med toMine(TNFe.InfNFe.Det.Prod.Med obj);

    TNFe.InfNFe.Det.Prod.Med toHis(NFe.InfNFe.Det.Prod.Med obj);

    NFe.InfNFe.Det.Prod.DetExport toMine(TNFe.InfNFe.Det.Prod.DetExport obj);

    TNFe.InfNFe.Det.Prod.DetExport toHis(NFe.InfNFe.Det.Prod.DetExport obj);

    NFe.InfNFe.Det.Prod.DetExport.ExportInd toMine(TNFe.InfNFe.Det.Prod.DetExport.ExportInd obj);

    TNFe.InfNFe.Det.Prod.DetExport.ExportInd toHis(NFe.InfNFe.Det.Prod.DetExport.ExportInd obj);

    NFe.InfNFe.Det.Prod.DI toMine(TNFe.InfNFe.Det.Prod.DI obj);

    TNFe.InfNFe.Det.Prod.DI toHis(NFe.InfNFe.Det.Prod.DI obj);

    NFe.InfNFe.Det.Prod.DI.Adi toMine(TNFe.InfNFe.Det.Prod.DI.Adi obj);

    TNFe.InfNFe.Det.Prod.DI.Adi toHis(NFe.InfNFe.Det.Prod.DI.Adi obj);

    NFe.InfNFe.Det.Prod.Comb toMine(TNFe.InfNFe.Det.Prod.Comb obj);

    TNFe.InfNFe.Det.Prod.Comb toHis(NFe.InfNFe.Det.Prod.Comb obj);

    NFe.InfNFe.Det.Prod.Comb.Encerrante toMine(TNFe.InfNFe.Det.Prod.Comb.Encerrante obj);

    TNFe.InfNFe.Det.Prod.Comb.Encerrante toHis(NFe.InfNFe.Det.Prod.Comb.Encerrante obj);

    NFe.InfNFe.Det.Prod.Comb.CIDE toMine(TNFe.InfNFe.Det.Prod.Comb.CIDE obj);

    TNFe.InfNFe.Det.Prod.Comb.CIDE toHis(NFe.InfNFe.Det.Prod.Comb.CIDE obj);

    NFe.InfNFe.Det.Prod.Arma toMine(TNFe.InfNFe.Det.Prod.Arma obj);

    TNFe.InfNFe.Det.Prod.Arma toHis(NFe.InfNFe.Det.Prod.Arma obj);

    NFe.InfNFe.Det.ImpostoDevol toMine(TNFe.InfNFe.Det.ImpostoDevol obj);

    TNFe.InfNFe.Det.ImpostoDevol toHis(NFe.InfNFe.Det.ImpostoDevol obj);

    NFe.InfNFe.Det.ImpostoDevol.IPI toMine(TNFe.InfNFe.Det.ImpostoDevol.IPI obj);

    TNFe.InfNFe.Det.ImpostoDevol.IPI toHis(NFe.InfNFe.Det.ImpostoDevol.IPI obj);

    @Mapping(target = "ISSQN", ignore = true)
    @Mapping(target = "II", ignore = true)
    @Mapping(target = "PIS", ignore = true)
    @Mapping(target = "COFINS", ignore = true)
    @Mapping(target = "ICMS", ignore = true)
    // @Mapping(target = "IPI", ignore = true)
    @Mapping(target = "PISST", ignore = true)
    @Mapping(target = "COFINSST", ignore = true)
    @Mapping(target = "content", expression = "java(entityMapper.listToMap(obj.getContent()))")
    NFe.InfNFe.Det.Imposto toMine(TNFe.InfNFe.Det.Imposto obj);

    @Mapping(target = "content", expression = "java(entityMapper.mapToList(obj.getContent(), this))")
    TNFe.InfNFe.Det.Imposto toHis(NFe.InfNFe.Det.Imposto obj);

    NFe.InfNFe.Det.Imposto.PISST toMine(TNFe.InfNFe.Det.Imposto.PISST obj);

    TNFe.InfNFe.Det.Imposto.PISST toHis(NFe.InfNFe.Det.Imposto.PISST obj);

    @Mapping(target = "tipoPIS", ignore = true)
    NFe.InfNFe.Det.Imposto.PIS toMine(TNFe.InfNFe.Det.Imposto.PIS obj, NFe.InfNFe.Det det);

    TNFe.InfNFe.Det.Imposto.PIS toHis(NFe.InfNFe.Det.Imposto.PIS obj);

    @Mapping(target = "orig", ignore = true)
    NFe.InfNFe.Det.Imposto.PIS.PISQtde toMine(TNFe.InfNFe.Det.Imposto.PIS.PISQtde obj, NFe.InfNFe.Det.Imposto.PIS pis);

    TNFe.InfNFe.Det.Imposto.PIS.PISQtde toHis(NFe.InfNFe.Det.Imposto.PIS.PISQtde obj);

    @Mapping(target = "orig", ignore = true)
    NFe.InfNFe.Det.Imposto.PIS.PISOutr toMine(TNFe.InfNFe.Det.Imposto.PIS.PISOutr obj, NFe.InfNFe.Det.Imposto.PIS pis);

    TNFe.InfNFe.Det.Imposto.PIS.PISOutr toHis(NFe.InfNFe.Det.Imposto.PIS.PISOutr obj);

    @Mapping(target = "orig", ignore = true)
    NFe.InfNFe.Det.Imposto.PIS.PISNT toMine(TNFe.InfNFe.Det.Imposto.PIS.PISNT obj, NFe.InfNFe.Det.Imposto.PIS pis);

    TNFe.InfNFe.Det.Imposto.PIS.PISNT toHis(NFe.InfNFe.Det.Imposto.PIS.PISNT obj);

    @Mapping(target = "orig", ignore = true)
    NFe.InfNFe.Det.Imposto.PIS.PISAliq toMine(TNFe.InfNFe.Det.Imposto.PIS.PISAliq obj, NFe.InfNFe.Det.Imposto.PIS pis);

    TNFe.InfNFe.Det.Imposto.PIS.PISAliq toHis(NFe.InfNFe.Det.Imposto.PIS.PISAliq obj);

    @Mapping(target = "idCidade", ignore = true)
    NFe.InfNFe.Det.Imposto.ISSQN toMine(TNFe.InfNFe.Det.Imposto.ISSQN obj);

    TNFe.InfNFe.Det.Imposto.ISSQN toHis(NFe.InfNFe.Det.Imposto.ISSQN obj);

    NFe.InfNFe.Det.Imposto.II toMine(TNFe.InfNFe.Det.Imposto.II obj);

    TNFe.InfNFe.Det.Imposto.II toHis(NFe.InfNFe.Det.Imposto.II obj);

    NFe.InfNFe.Det.Imposto.ICMS toMine(TNFe.InfNFe.Det.Imposto.ICMS obj);

    TNFe.InfNFe.Det.Imposto.ICMS toHis(NFe.InfNFe.Det.Imposto.ICMS obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMSST toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMSST obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMSST toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMSST obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMSPart toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMSPart obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS90 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS90 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS70 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS70 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS60 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS60 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS51 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS51 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS40 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS40 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS40 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS40 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS30 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS30 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS20 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS20 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS20 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS20 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS10 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS10 obj);

    NFe.InfNFe.Det.Imposto.ICMS.ICMS00 toMine(TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 obj);

    TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 toHis(NFe.InfNFe.Det.Imposto.ICMS.ICMS00 obj);

    NFe.InfNFe.Det.Imposto.COFINSST toMine(TNFe.InfNFe.Det.Imposto.COFINSST obj);

    TNFe.InfNFe.Det.Imposto.COFINSST toHis(NFe.InfNFe.Det.Imposto.COFINSST obj);

    @Mapping(target = "tipoCOFINS", ignore = true)
    NFe.InfNFe.Det.Imposto.COFINS toMine(TNFe.InfNFe.Det.Imposto.COFINS obj, NFe.InfNFe.Det det);

    TNFe.InfNFe.Det.Imposto.COFINS toHis(NFe.InfNFe.Det.Imposto.COFINS obj);

    @Mapping(target = "orig", ignore = true)
    NFe.InfNFe.Det.Imposto.COFINS.COFINSQtde toMine(TNFe.InfNFe.Det.Imposto.COFINS.COFINSQtde obj, NFe.InfNFe.Det.Imposto.COFINS cofins);

    TNFe.InfNFe.Det.Imposto.COFINS.COFINSQtde toHis(NFe.InfNFe.Det.Imposto.COFINS.COFINSQtde obj);

    @Mapping(target = "orig", ignore = true)
    NFe.InfNFe.Det.Imposto.COFINS.COFINSOutr toMine(TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr obj, NFe.InfNFe.Det.Imposto.COFINS cofins);

    TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr toHis(NFe.InfNFe.Det.Imposto.COFINS.COFINSOutr obj);

    @Mapping(target = "orig", ignore = true)
    NFe.InfNFe.Det.Imposto.COFINS.COFINSNT toMine(TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT obj, NFe.InfNFe.Det.Imposto.COFINS cofins);

    TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT toHis(NFe.InfNFe.Det.Imposto.COFINS.COFINSNT obj);

    @Mapping(target = "orig", ignore = true)
    NFe.InfNFe.Det.Imposto.COFINS.COFINSAliq toMine(TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq obj, NFe.InfNFe.Det.Imposto.COFINS cofins);

    TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq toHis(NFe.InfNFe.Det.Imposto.COFINS.COFINSAliq obj);

    @Mapping(target = "cpfCnpj", ignore = true)
    NFe.InfNFe.Dest toMine(TNFe.InfNFe.Dest obj);

    TNFe.InfNFe.Dest toHis(NFe.InfNFe.Dest obj);

    NFe.InfNFe.Compra toMine(TNFe.InfNFe.Compra obj);

    TNFe.InfNFe.Compra toHis(NFe.InfNFe.Compra obj);

    NFe.InfNFe.Cobr toMine(TNFe.InfNFe.Cobr obj);

    TNFe.InfNFe.Cobr toHis(NFe.InfNFe.Cobr obj);

    NFe.InfNFe.Cobr.Fat toMine(TNFe.InfNFe.Cobr.Fat obj);

    TNFe.InfNFe.Cobr.Fat toHis(NFe.InfNFe.Cobr.Fat obj);

    NFe.InfNFe.Cobr.Dup toMine(TNFe.InfNFe.Cobr.Dup obj);

    TNFe.InfNFe.Cobr.Dup toHis(NFe.InfNFe.Cobr.Dup obj);

    NFe.InfNFe.Cana toMine(TNFe.InfNFe.Cana obj);

    TNFe.InfNFe.Cana toHis(NFe.InfNFe.Cana obj);

    NFe.InfNFe.Cana.ForDia toMine(TNFe.InfNFe.Cana.ForDia obj);

    TNFe.InfNFe.Cana.ForDia toHis(NFe.InfNFe.Cana.ForDia obj);

    NFe.InfNFe.Cana.Deduc toMine(TNFe.InfNFe.Cana.Deduc obj);

    TNFe.InfNFe.Cana.Deduc toHis(NFe.InfNFe.Cana.Deduc obj);

    NFe.InfNFe.Avulsa toMine(TNFe.InfNFe.Avulsa obj);

    TNFe.InfNFe.Avulsa toHis(NFe.InfNFe.Avulsa obj);

    NFe.InfNFe.AutXML toMine(TNFe.InfNFe.AutXML obj);

    TNFe.InfNFe.AutXML toHis(NFe.InfNFe.AutXML obj);

}
