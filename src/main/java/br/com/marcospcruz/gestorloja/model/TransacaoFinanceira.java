package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

@Entity
public class TransacaoFinanceira implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6962908827365885221L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idTransacaoFinanceira;
	@ManyToOne
	@JoinColumn(name = "idCaixa")
	private Caixa caixa;
	@OneToOne
	@JoinColumn(name = "idMeioPagamento")
	private MeioPagamento meioPagamento;
	@ManyToOne
	@JoinColumn(name = "idOperacao")
	private Operacao operacao;
	private String descricaoTransacao;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataTransacao;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario usuario;

	public TransacaoFinanceira() {
		setDataTransacao(SingletonManager.getInstance().getData());
		setUsuario(SingletonManager.getInstance().getUsuarioLogado());
	}

	public int getIdTransacaoFinanceira() {
		return idTransacaoFinanceira;
	}

	public void setIdTransacaoFinanceira(int idTransacaoFinanceira) {
		this.idTransacaoFinanceira = idTransacaoFinanceira;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public MeioPagamento getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(MeioPagamento meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public String getDescricaoTransacao() {
		return descricaoTransacao;
	}

	public void setDescricaoTransacao(String descricaoTransacao) {
		this.descricaoTransacao = descricaoTransacao;
	}

	public Date getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(Date dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
