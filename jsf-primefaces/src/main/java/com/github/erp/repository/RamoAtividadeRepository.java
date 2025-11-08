package com.github.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.github.erp.model.RamoAtividade;

public class RamoAtividadeRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public RamoAtividadeRepository() {
	}

	public RamoAtividadeRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public RamoAtividade porId(long id) {
		return entityManager.find(RamoAtividade.class, id);
	}

	public List<RamoAtividade> pesquisar(String descricao) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<RamoAtividade> cq = cb.createQuery(RamoAtividade.class);

		Root<RamoAtividade> root = cq.from(RamoAtividade.class);

		cq.select(root);
		cq.where(cb.like(root.get("descricao"), descricao + "%"));

		TypedQuery<RamoAtividade> query = entityManager.createQuery(cq);

		return query.getResultList();
	}

	public RamoAtividade salvar(RamoAtividade ramoAtividade) {
		return entityManager.merge(ramoAtividade);
	}

	public void remover(RamoAtividade ramoAtividade) {
		ramoAtividade = porId(ramoAtividade.getId());
		entityManager.remove(ramoAtividade);
	}

}
