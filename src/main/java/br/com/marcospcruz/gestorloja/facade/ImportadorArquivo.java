package br.com.marcospcruz.gestorloja.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class ImportadorArquivo {

	private File file;
	protected FileInputStream inputStream;
	private StringBuilder mensagemRetorno;

	public void setFile(File file) {
		this.file = file;

	}

	protected void setMensagemRetorno(String mensagemRetorno) {
		if (this.mensagemRetorno == null)
			this.mensagemRetorno = new StringBuilder(mensagemRetorno);
		else
			this.mensagemRetorno.append(mensagemRetorno);

	}

	public String getMensagemRetorno() {
		return mensagemRetorno.toString();
	}

	public void carregaDados() throws Exception {
		abreArquivo();
		leDados();
		salvaDados();

	}

	abstract void salvaDados();

	protected abstract void leDados() throws IOException, Exception;

	protected void abreArquivo() throws IOException {
		inputStream = new FileInputStream(file);
	}

}
