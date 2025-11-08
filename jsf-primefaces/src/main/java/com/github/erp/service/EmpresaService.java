package com.github.erp.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.github.erp.model.Empresa;
import com.github.erp.repository.EmpresaRepository;
import com.github.erp.util.Transacional;

public class EmpresaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaRepository empresaRepository;

	@Transacional
	public void salvar(Empresa empresa) {
		empresaRepository.salvar(empresa);
	}

	@Transacional
	public void remover(Empresa empresa) {
		empresaRepository.remover(empresa);
	}

}
