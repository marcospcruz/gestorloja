package br.com.marcospcruz.gestorloja.model;

public class EstoqueReportBeanModel extends ItemEstoque {

	private float valorTotal;

	public EstoqueReportBeanModel(ItemEstoque report) {
		setCodigoDeBarras(report.getCodigoDeBarras());
		setDataContagem(report.getDataContagem());
		setEstoqueDedutivel(report.isEstoqueDedutivel());
		setFabricante(report.getFabricante());
		setProduto(report.getProduto());
		setTipoProduto(report.getTipoProduto());
		setQuantidade(report.getQuantidade());
		setValorCusto(report.getValorCusto());
		setValorUnitario(report.getValorUnitario());
		setValorTotal(getQuantidade() * getValorUnitario());
	}

	public EstoqueReportBeanModel() {
		// TODO Auto-generated constructor stub
	}

	public void setValorTotal(float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public float getValorTotal() {
		return valorTotal;
	}

}
