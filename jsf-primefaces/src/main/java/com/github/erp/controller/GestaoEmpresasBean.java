package com.github.erp.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.github.erp.enums.TipoEmpresa;
import com.github.erp.model.Empresa;
import com.github.erp.model.RamoAtividade;
import com.github.erp.repository.EmpresaRepository;
import com.github.erp.repository.RamoAtividadeRepository;
import com.github.erp.service.EmpresaService;
import com.github.erp.util.FacesMessages;

@Named
@ViewScoped
public class GestaoEmpresasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesMessages messages;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private EmpresaService empresaService;

	@Inject
	private RamoAtividadeRepository ramoAtividadeRepository;

	private String termo;
	private List<Empresa> empresas;
	private Converter ramoAtividadeConverter;
	private Empresa empresa;

	public void pesquisar() {

		empresas = empresaRepository.porNome(termo);

		if (this.empresas.isEmpty()) {
			messages.info("Sua consulta não retornou registros");
		}

	}

	public void buscarEmpresas() {
		empresas = empresaRepository.todas();
	}

	public List<RamoAtividade> completarRamoAtividade(String termo) {

		List<RamoAtividade> ramoAtividades = ramoAtividadeRepository.pesquisar(termo);
		ramoAtividadeConverter = new RamoAtividadeConverter(ramoAtividades);

		return ramoAtividades;
	}

	public void prepararNovaEmpresa() {
		empresa = new Empresa();
	}

	public void prepararEdicao() {
		ramoAtividadeConverter = new RamoAtividadeConverter(Arrays.asList(empresa.getRamoAtividade()));
	}

	public void salvar() {

		empresaService.salvar(empresa);
		atualizarRegistros();

		messages.info("Empresa salva com sucesso!");
		RequestContext.getCurrentInstance().update(Arrays.asList("frm:empresasDataTable", "frm:messages"));
	}

	public void remover() {

		empresaService.remover(empresa);
		empresa = null;
		atualizarRegistros();

		messages.info("Empresa removida com sucesso!");
	}

	private boolean jaHouvePesquisa() {
		return termo != null && !termo.isEmpty();
	}

	private void atualizarRegistros() {
		if (jaHouvePesquisa()) {
			pesquisar();
		} else {
			buscarEmpresas();
		}
	}

	public String getTermo() {
		return termo;
	}

	public void setTermo(String termo) {
		this.termo = termo;
	}

	public TipoEmpresa[] getTiposEmpresa() {
		return TipoEmpresa.values();
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public Converter getRamoAtividadeConverter() {
		return ramoAtividadeConverter;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public boolean isEmpresaSelecionada() {
		return empresa != null && empresa.getId() != 0;
	}

}
