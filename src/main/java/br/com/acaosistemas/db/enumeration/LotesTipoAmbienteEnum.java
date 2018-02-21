package br.com.acaosistemas.db.enumeration;

import br.com.acaosistemas.frw.enumeration.BaseEnum;

public enum LotesTipoAmbienteEnum implements BaseEnum<Integer> {
    /**
     * Enumeracao para o ambiente de producao.
     * {@value 1}
     */
    PRODUCAO(1, "Producao"),
    /**
     * Enumeracao para o ambiente de producao restrita.
     * {@value 2}
     */
    PRODUCAO_RESTRITA(2, "Producao restrita");

    private Integer id;
    private String descricao;	
	
    /**
     * Construtor padrao para a Enum.
     * @param id com o codigo.
     * @param descricao com descritivo da Enum.
     */
	LotesTipoAmbienteEnum(final Integer id, final String descricao) {
        setId(id);
        setDescricao(descricao);
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(final String descricao) {
        this.descricao = descricao;
    }

    /**
     * Recupera a enumeracao com base no identificador.
     *
     * @param id
     *            Valor para o identificador.
     * @return A enumeracao alcancada.
     */
    public static LotesTipoAmbienteEnum getById(final Integer id) {
    	LotesTipoAmbienteEnum result = null;
        for (final LotesTipoAmbienteEnum someEnum : values()) {
            if (someEnum.getId().equals(id)) {
                result = someEnum;
            }
        }
        return result;
    }	
}
