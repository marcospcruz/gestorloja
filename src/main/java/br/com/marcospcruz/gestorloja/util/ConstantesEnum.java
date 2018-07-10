package br.com.marcospcruz.gestorloja.util;

public enum ConstantesEnum {
	//@formatter:off
	SIMBOLO_MONETARIO_BR(0), 
	LBL_BTN_RELATORIO(1), 
	PRODUTO_LABEL(2), 
	LBL_BTN_TIPO_PRODUTO(3), 
	LBL_BTN_NOVO_ITEM_ESTOQUE(4), 
	LBL_BTN_LIMPAR(5), 
	CONSULTA_ESTOQUE(6), 
	DESCRICAO_ITEM_LABEL(7), 
	RELATORIO_TITLE_BORDER(8), CODIGO_LABEL(9), 
	CATEGORIA_LABEL(10), 
	TIPO_ITEM_LABEL(11), 
	QUANTIDADE_LABEL(12), 
	VALOR_UNITARIO_LABEL(13), 
	VALOR_TOTAL_LABEL(14), 
	DESCRICAO_LABEL(15), 
	SUB_DESCRICAO_LABEL(16), 
	CADASTRO_TIPO_PRODUTO_TITLE(17), 
	TIPO_PRODUTO_LABEL(18), 
	TIPOS_PRODUTOS_LABEL(19), 
	CONFIRMACAO_REGISTRO_ATUALIZADO(20), 
	CONFIRMANDO_ATUALIZACAO_MSG_TITLE(21), 
	SUB_TIPO_DE_LABEL(22), 
	SELECIONE_PRIMEIRA_MSG(23), 
	CONFIRMACAO_EXCLUSAO_TITLE(24), 
	CONFIRMACAO_EXCLUSAO(25), 
	UNIDADE_MEDIDA_LABEL(26), 
	SELECAO_TIPO_PRODUTO_INVALIDA_EXCEPTION_MESSAGE(27), 
	SELECAO_SUB_TIPO_PRODUTO_INVALIDA_EXCEPTION_MESSAGE(28), 
	PRODUTO_JA_EXISTENTE_MESSAGE_EXCEPTION(29), 
	PRODUTO_NECESSARIO_SELECIONAR_ALERTA(30), 
	SELECAO_INVALIDA(31), 
	CONFIRMACAO_SALVAMENTO(32), 
	CONFIRMACAO_SALVAMENTO_TITLE(33), 
	TIPO_PRODUTO_NAO_ENCONTRADO(34), 
	TIPO_ITEM_INVALIDO(35), 
	REMOCAO_INVALIDA_EXCEPTION(36), 
	TIPO_PRODUTO_SELECAO_MESSAGE_EXCEPTION(37), 
	TIPO_PRODUTO_JA_CADASTRADO(38), 
	TIPO_PRODUTO_POPULADO(39), 
	TIPO_PRODUTO_POPULADO_SUPERTIPO(40), 
	ERROR_MESSAGE_LOGO_MARCA(41), 
	ITEM_DO_ESTOQUE_EXCEPTION_SALVAMENTO(42), 
	EXCLUSAO_PRODUTO_COM_ITENS_ESTOQUE(43), 
	NO_DATA_FOUND(44), 
	FABRICANTE(45), 
	FABRICANTE_NAO_ENCONTRADO(46), 
	ITEM_DO_ESTOQUE_NAO_ENCONTRADO(47), 
	CODIGO_DE_BARRAS(48), 
	VALOR_CUSTO_LABEL(49), CADASTRO_PRODUTO_TITLE(50);
//@formatter:on
	private int index;

	ConstantesEnum(int index) {
		this.index = index;
	}

	private static final Object VALUES[] = { "R$", // 0
			"Relat�rio de Estoque (PDF)", // 1
			"Produto", // 2
			"Categorias de Produto", // 3
			"Novo Item Estoque", // 4
			"Limpar", // 5
			"Consulta Estoque", // 6
			"Descri��o Item", // 7
			"Relat�rios", // 8
			"C�digo", // 9
			"Categoria", // 10
			"Sub Categoria", // 11
			"Quantidade", // 12
			"Pre�o Final (R$)", // 13
			"Valor Total", // 14
			"Descri��o", // 15
			"Sub-Descri��o", // 16
			"Cadastro de Tipo de Produto", // 17
			"Categoria de Produto", // 18
			"Categorias de Produtos", // 19
			"Registro atualizado com Sucesso.", // 20
			"Confirmando Atualiza��o", // 21
			"Sub-Tipo de:", // 22
			"Selecione uma Op��o", // 23
			"Confirmar Exclus�o", // 24
			"Deseja realmente excluir este item?", // 25
			"Un. Medida", // 26
			"Sele��o de Tipo de Produto inv�lida.", // 27
			"Sele��o de Sub-Tipo de Produto inv�lida", // 28
			"Produto j� existente no Estoque!", // 29
			"� necess�rio selecionar um Produto na Lista.", // 30
			"Sele��o inv�lida!", // 31
			"Deseja salvar este item?", // 32
			"Confirmar salvamento!", // 33
			"Tipo de Produto n�o encontrado.", // 34
			"Tipo de Produto Inv�lido", // 35
			"Necess�rio efetuar a busca do Registro antes de Excluir.", // 36
			"Selecione um Tipo de Produto v�lido!", // 37
			"Tipo Produto j� Cadastrado com esta Descri��o!", // 38
			"Este Tipo de Produto cont�m Produtos associados a ele.", // 39
			"Este Tipo de Produto cont�m Sub-Tipos de Produtos associados a ele.", // 40
			"Logo Marca n�o encontrada.", // 41
			"� necess�rio selecionar um �tem de Estoque antes.", // 42
			"Exclus�o n�o permitida! H� �tens deste Produto no estoque.", // 43
			"N�o h� Dados no Estoque.", // 44
			"Fabricante", // 45
			"Fabricante / Marca n�o encontrado", // 46
			"�tem n�o encontrado no Estoque", // 47
			"C�digo Produto", // 48
			"Pre�o de Custo (R$)", // 49
			"Cadastro de Produto"//50
			};

	public Object getValue() {
		return VALUES[index];
	}
}
