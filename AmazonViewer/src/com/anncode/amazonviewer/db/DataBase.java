package com.anncode.amazonviewer.db;

/**
 * Esta clase define el esquema centralizado de la base de datos AmazonViewer.
 * <p>
 * Sigue el patrón de diseño de <b>Constantes de Esquema</b>, eliminando el uso de
 * "Magic Strings" en los DAOs. Facilita el mantenimiento permitiendo cambios en
 * nombres de tablas o columnas desde un único punto.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2026-01-03
 */
public class DataBase {

    /**
     * Constructor por defecto de la clase DataBase.
     * <p>
     * Se utiliza para instanciar la estructura lógica del esquema de la base de datos.
     * Aunque no realiza operaciones de apertura de conexión directamente (responsabilidad
     * delegada a {@link IDBConnection}), su existencia permite acceder de forma organizada
     * a las constantes de tablas y columnas definidas en sus clases internas.
     * </p>
     */
    public DataBase() {
    }

    /** * Identificadores lógicos para los tipos de materiales.
     * Estos nombres deben coincidir con la columna 'name' de la tabla 'material'.
     */
    public static final class MaterialNames {
        /** Constante para el nombre del Material de Películas. */
        public static final String MOVIE    = "Movie";
        /** Constante para el nombre del Material de Series. */
        public static final String SERIE    = "Serie";
        /** Constante para el nombre del Material de Capítulos de Series. */
        public static final String CHAPTER  = "Chapter";
        /** Constante para el nombre del Material de Libros. */
        public static final String BOOK     = "Book";
        /** Constante para el nombre del Material de Revistas. */
        public static final String MAGAZINE = "Magazine";
    }

    /** Tabla de Películas */
    public static final class TMovie {
        /** Constante para el nombre de la tabla de Películas. */
        public static final String NAME     = "movie";
        /** Constante para el identificador único de la tabla de Películas. */
        public static final String ID       = "id";
        /** Constante para el título de la tabla de Películas. */
        public static final String TITLE    = "title";
        /** Constante para el género de la tabla de Películas. */
        public static final String GENRE    = "genre";
        /** Constante para el creador de la tabla de Películas. */
        public static final String CREATOR  = "creator";
        /** Constante para la duración de la tabla de Películas. */
        public static final String DURATION = "duration";
        /** Constante para el año de lanzamiento de la tabla de Películas. */
        public static final String YEAR     = "year";
    }

    /** Tabla de Usuarios del sistema.*/
    public static final class TUser {
        /** Nombres reales del Usuario. */
        public static final String NAME     = "user";
        /** Identificador único del Usuario. */
        public static final String ID       = "id";
        /** Nombre de Usuario. */
        public static final String USERNAME = "name";
    }

    /**
     * Tabla de Relación: Registro de contenido visto o leído.
     * Tabla de transacciones que registra el historial de visualización y lectura.
     * */
    public static final class TViewed {
        /** Constante para el nombre de la tabla de Vistas. */
        public static final String NAME           = "viewed";
        /** Constante para el identificador único de la tabla de Vistas. */
        public static final String ID             = "id";
        /** Llave foránea que apunta a TMaterial.ID */
        public static final String ID_MATERIAL    = "id_material"; // ID de la película/serie/libro
        /** Llave foránea que apunta a TElement.ID */
        public static final String ID_ELEMENT     = "id_element";  // ID de la película/capítulo/libro
        /** Llave foránea que apunta a TUser.ID */
        public static final String ID_USER        = "id_user";
        /** Constante para la fecha de visualización o lectura. */
        public static final String DATE           = "date";
    }

    /** Tabla de Catálogo de Materiales (1. Película, 2. Serie, 3. Libro, etc.) */
    public static final class TMaterial {
        /** Constante para el nombre de la tabla de Material. */
        public static final String NAME     = "material";
        /** Constante para el identificador único de la tabla de Material. */
        public static final String ID       = "id";
        /** Constante para el nombre de columna de la tabla de Material. */
        public static final String NAME_COL = "name";
    }

    /**
     * Tabla de Revistas.
     * Almacena datos de revistas y publicaciones periódicas.
     * */
    public static final class TMagazine {
        /** Nombre de la tabla de Revistas. */
        public static final String NAME         = "magazine";
        /** Identificador único de la revista. */
        public static final String ID           = "id";
        /** Título de la revista. */
        public static final String TITLE        = "title";
        /** Editorial de la revista. */
        public static final String EDITORIAL    = "editorial";
        /** Fecha de edición de la revista. */
        public static final String EDITION_DATE = "edition_date";
        /** Autores de la revista. */
        public static final String AUTHORS      = "authors"; // Campo faltante
    }

    /** Tabla de Series */
    public static final class TSerie {
        /** Nombre de la serie. */
        public static final String NAME     = "serie";
        /** Identificador único de la serie. */
        public static final String ID       = "id";
        /** Título de la serie. */
        public static final String TITLE    = "title";
        /** Género de la serie. */
        public static final String GENRE    = "genre";
        /** Creador de la serie. */
        public static final String CREATOR  = "creator";
        /** Duración promedio o total de la serie. */
        public static final String DURATION = "duration"; // Duración promedio o total
        /** Año de lanzamiento de la serie. */
        public static final String YEAR     = "year";
        /** Cantidad de sesiones de la serie. */
        public static final String SESSION_QUANTITY  = "session_quantity";
    }

    /** Tabla de Capítulos de Series */
    public static final class TChapter {
        /** Nombre de la tabla de Capítulos. */
        public static final String NAME           = "chapter";
        /** Identificador único del capítulo. */
        public static final String ID             = "id";
        /** Título del capítulo. */
        public static final String TITLE          = "title";
        /** Duración del capítulo. */
        public static final String DURATION       = "duration";
        /** Año de lanzamiento del capítulo. */
        public static final String YEAR           = "year";
        /** Número de sesión del capítulo. */
        public static final String SESSION_NUMBER = "session_number";
        /** Llave foránea que apunta a TSerie.ID */
        public static final String ID_SERIE       = "id_serie";
    }

    /** Tabla de Libros (Agregada para completar el modelo) */
    public static final class TBook {
        /** Nombre del libro. */
        public static final String NAME         = "book";
        /** Identificador único del libro. */
        public static final String ID           = "id";
        /** Título del libro. */
        public static final String TITLE        = "title";
        /** Editorial del libro. */
        public static final String EDITORIAL    = "editorial";
        /** Fecha de edición del libro. */
        public static final String EDITION_DATE = "edition_date";
        /** ISBN del libro. */
        public static final String ISBN         = "isbn";
        /** Autores del libro. */
        public static final String AUTHORS      = "authors";
    }

    /** Tabla de Páginas de Libros */
    public static final class TPage {
        /** Nombre del libro. */
        public static final String NAME     = "page";
        /** Identificador único del libro. */
        public static final String ID       = "id";
        /** Número de páginas del libro. */
        public static final String NUMBER   = "number";
        /** Contenido de la página del libro. */
        public static final String CONTENT  = "content";
        /** Llave foránea que apunta a TBook.ID */
        public static final String ID_BOOK  = "id_book"; // Relación con el libro
    }
}