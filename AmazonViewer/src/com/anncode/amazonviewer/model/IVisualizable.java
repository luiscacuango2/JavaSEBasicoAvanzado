package com.anncode.amazonviewer.model;

import java.util.Date;

/**
 * Esta interfaz define el contrato de comportamiento para los objetos 
 * que pueden ser visualizados dentro de Amazon Viewer.
 * <p>
 * Su implementación es obligatoria para aquellas clases que necesiten 
 * registrar métricas de tiempo de consumo (como películas, capítulos o libros).
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public interface IVisualizable {

    /**
     * Captura el momento exacto en que el usuario comienza a visualizar el contenido.
     * @param dateI Objeto {@code Date} que representa el punto de inicio en el tiempo.
     * @return El objeto {@code Date} capturado para su posterior procesamiento.
     */
    Date startToSee(Date dateI);

    /**
     * Registra el momento en que finaliza la visualización y permite calcular 
     * la duración total de la sesión.
     * <p>
     * Al comparar {@code dateI} con {@code dateF}, se obtiene el tiempo 
     * invertido por el usuario en el contenido.
     * </p>
     * @param dateI Objeto {@code Date} con la fecha y hora de inicio previamente capturada.
     * @param dateF Objeto {@code Date} con la fecha y hora actual de finalización.
     */
    void stopToSee(Date dateI, Date dateF);

}