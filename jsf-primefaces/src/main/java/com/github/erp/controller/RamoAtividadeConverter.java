package com.github.erp.controller;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.github.erp.model.RamoAtividade;

public class RamoAtividadeConverter implements Converter {

	private List<RamoAtividade> ramoAtividades;

	public RamoAtividadeConverter(List<RamoAtividade> ramoAtividades) {
		this.ramoAtividades = ramoAtividades;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (value == null) {
			return null;
		}

		long id = Long.valueOf(value);

		for (RamoAtividade ramoAtividade : ramoAtividades) {
			if (id == ramoAtividade.getId()) {
				return ramoAtividade;
			}
		}

		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value == null) {
			return null;
		}

		RamoAtividade ramoAtividade = (RamoAtividade) value;

		return Long.toString(ramoAtividade.getId());
	}

}
