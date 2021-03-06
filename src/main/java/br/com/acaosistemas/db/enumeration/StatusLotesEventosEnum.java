package br.com.acaosistemas.db.enumeration;

import br.com.acaosistemas.frw.enumeration.BaseEnum;

/**
 * Enumeracao para representar os status que um lote de eventos
 * do eSocial pode assumir.
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Alterações:
 * <p>
 * 2018.03.12 - ABS - Adiconado JavaDOc.
 * 
 * @author Anderson Bestteti Santos
 *
 */
public enum StatusLotesEventosEnum implements BaseEnum<Integer> {
	/**
     * Indica que o lote esta pronto para ser enviado para o eSocial.
     * {@value 201}
     */
    A_ENVIAR(201, "A enviar"),
 
    /**
     * Indica que o evento da stage foi assinado com sucesso pelo web service
     * de assinatura de evento.
     * {@value 298}
     */
    ENVIADO_COM_SUCESSO(298, "Lote de eventos enviado para o eSocial com sucesso"),
    
    /**
     * Indica que houve um erro na assinatura do evento da stage pelo web service
     * de assinatura de evento.
     * {@value 299}
     */
    ERRO_ENVIO_IRRECUPERAVEL(299, "Erro no envio do lote de eventos para o eSocial - irrecuperavel"),
    
    /**
     * Indica que o lote de eventos pronto para ter seus eventos desassociados.
     * {@value 301}
     */
    DESASSOCIACAO_A_DESASSOCIAR(301, "O lote de eventos pronto para ter seus eventos desassociados."),
    
    /**
     * Indica que o lote esta pronto para ser consultado no eSocial.
     * {@value 501} 
     */
    A_CONSULTAR(501, "A consultar"),
    
    /**
     * Indica que a consulta do lote no eSocial foi realizada com sucesso.
     * {@value 598}
     */
    CONSULTADO_COM_SUCESSO(598, "Lote de eventos consultado com sucesso no eSocial"),
    
    /**
     * Indica que houve um erro na consultado do lote de eventos no eSocial
     * {@value 599}
     */
    ERRO_CONSULTA_IRRECUPERAVEL(599, "Erro na consulta do lote de eventos no eSocial - irrecuperavel");

    private Integer id;
    private String descricao;

    /**
     * Construtor.
     *
     * @param id Identificador da enumeracao.
     * @param descricao Descricao da enumeracao.
     */
    StatusLotesEventosEnum(final Integer id, final String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    /**
     * Recupera a enumeracao atraves do ID informado.
     *
     * @param id Identificador da enumeracao.
     * @return Enumeracao alcancada.
     */
    public static StatusLotesEventosEnum getById(final Integer id) {
    	StatusLotesEventosEnum statusLoteEnum = null;
        for (final StatusLotesEventosEnum someEnum : values()) {
            if (someEnum.getId().equals(id)) {
                statusLoteEnum =  someEnum;
            }
        }
        return statusLoteEnum;
    }
}