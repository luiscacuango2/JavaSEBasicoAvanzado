package com.anncode.amazonviewer.db;

/**
 * Contiene el esquema detallado de la base de datos amazonviewer.
 * Organiza las tablas y columnas mediante clases internas estáticas para mejorar la legibilidad.
 */
public class DataBase {

    /** Constantes para ID de Material */
    public static final class MaterialNames {
        public static final String MOVIE    = "Movie";
        public static final String SERIE    = "Serie";
        public static final String CHAPTER  = "Chapter";
        public static final String BOOK     = "Book";
        public static final String MAGAZINE = "Magazine";
    }

    /** Tabla de Películas */
    public static final class TMovie {
        public static final String NAME     = "movie";
        public static final String ID       = "id";
        public static final String TITLE    = "title";
        public static final String GENRE    = "genre";
        public static final String CREATOR  = "creator";
        public static final String DURATION = "duration";
        public static final String YEAR     = "year";
    }

    /** Tabla de Usuarios del sistema */
    public static final class TUser {
        public static final String NAME     = "user";
        public static final String ID       = "id";
        public static final String USERNAME = "name";
    }

    /** Tabla de Relación: Registro de contenido visto */
    public static final class TViewed {
        public static final String NAME           = "viewed";
        public static final String ID             = "id";
        public static final String ID_MATERIAL    = "id_material"; // ID de la película/serie/libro
        public static final String ID_ELEMENT     = "id_element";  // ID de la película/capítulo/libro
        public static final String ID_USER        = "id_user";
        public static final String DATE           = "date";
    }

    /** Tabla de Catálogo de Materiales (1. Película, 2. Serie, 3. Libro, etc.) */
    public static final class TMaterial {
        public static final String NAME     = "material";
        public static final String ID       = "id";
        public static final String NAME_COL = "name";
    }

    /** Tabla de Revistas */
    public static final class TMagazine {
        public static final String NAME         = "magazine";
        public static final String ID           = "id";
        public static final String TITLE        = "title";
        public static final String EDITORIAL    = "editorial";
        public static final String EDITION_DATE = "edition_date";
        public static final String AUTHORS      = "authors"; // Campo faltante
    }

    /** Tabla de Series */
    public static final class TSerie {
        public static final String NAME     = "serie";
        public static final String ID       = "id";
        public static final String TITLE    = "title";
        public static final String GENRE    = "genre";
        public static final String CREATOR  = "creator";
        public static final String DURATION = "duration"; // Duración promedio o total
        public static final String YEAR     = "year";
        public static final String SESSION_QUANTITY  = "session_quantity";
    }

    /** Tabla de Capítulos de Series */
    public static final class TChapter {
        public static final String NAME           = "chapter";
        public static final String ID             = "id";
        public static final String TITLE          = "title";
        public static final String DURATION       = "duration";
        public static final String YEAR           = "year";
        public static final String SESSION_NUMBER = "session_number";
        public static final String ID_SERIE       = "id_serie";
    }

    /** Tabla de Libros (Agregada para completar el modelo) */
    public static final class TBook {
        public static final String NAME         = "book";
        public static final String ID           = "id";
        public static final String TITLE        = "title";
        public static final String EDITORIAL    = "editorial";
        public static final String EDITION_DATE = "edition_date";
        public static final String ISBN         = "isbn";
        public static final String AUTHORS      = "authors";
    }

    /** Tabla de Páginas de Libros */
    public static final class TPage {
        public static final String NAME     = "page";
        public static final String ID       = "id";
        public static final String NUMBER   = "number";
        public static final String CONTENT  = "content";
        public static final String ID_BOOK  = "id_book"; // Relación con el libro
    }
}