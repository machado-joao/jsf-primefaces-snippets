package com.github.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.github.erp.model.Empresa;

public class EmpresaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public EmpresaRepository() {
	}

	public EmpresaRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Empresa porId(long id) {
		return entityManager.find(Empresa.class, id);
	}

	public List<Empresa> todas() {
		return entityManager.createQuery("FROM Empresa", Empresa.class).getResultList();
	}

	public List<Empresa> porNome(String nomeFantasia) {

		TypedQuery<Empresa> query = entityManager
				.createQuery("FROM Empresa WHERE UPPER(nomeFantasia) LIKE :nomeFantasia", Empresa.class);
		query.setParameter("nomeFantasia", "%" + nomeFantasia.toUpperCase() + "%");

		return query.getResultList();
	}

	public Empresa salvar(Empresa empresa) {
		return entityManager.merge(empresa);
	}

	public void remover(Empresa empresa) {
		empresa = porId(empresa.getId());
		entityManager.remove(empresa);
	}

}
