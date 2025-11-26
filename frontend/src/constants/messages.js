// Mensajes de la aplicación
import { CONFIG } from '@/constants/config';

export const MESSAGES = {
    ERROR: {
        CARGAR_USUARIOS: 'Error al cargar los usuarios',
        CARGAR_TWEETS: 'Error al cargar el timeline',
        CARGAR_TWEETS_USUARIO: 'Error al cargar los tweets del usuario',
        CREAR_USUARIO: 'Error al crear el usuario',
        CREAR_TWEET: 'Error al crear el tweet',
        RETWEET: 'Error al hacer retweet',
        CONEXION: 'Error de conexión. Verifica que el backend esté corriendo',
        TIMEOUT: 'El servidor no respondió a tiempo',
        DESCONOCIDO: 'Ha ocurrido un error inesperado',
    },
    VALIDATION: {
        USERNAME_VACIO: 'El nombre de usuario no puede estar vacío',
        USERNAME_LONGITUD: `El nombre de usuario debe tener entre ${CONFIG.VALIDATION.USERNAME_MIN_LENGTH} y ${CONFIG.VALIDATION.USERNAME_MAX_LENGTH} caracteres`,
        TWEET_VACIO: 'El tweet no puede estar vacío',
        TWEET_LONGITUD: `El tweet no puede tener más de ${CONFIG.VALIDATION.TWEET_MAX_LENGTH} caracteres`,
        USUARIO_REQUERIDO: 'Por favor, selecciona un usuario',
    },
    SUCCESS: {
        TWEET_CREADO: '¡Tweet creado exitosamente!',
        USUARIO_CREADO: 'Usuario creado exitosamente',
    },
    LOADING: {
        CARGANDO: 'Cargando...',
        CARGANDO_TIMELINE: 'Cargando timeline...',
        CARGANDO_TWEETS: 'Cargando tweets...',
        CARGANDO_USUARIOS: 'Cargando usuarios...',
        CARGANDO_PERFIL: 'Cargando perfil...',
    },
    EMPTY: {
        NO_USUARIOS: 'No hay usuarios',
        NO_TWEETS: 'No hay tweets aún. ¡Sé el primero en twittear!',
        NO_TWEETS_USUARIO: 'Este usuario aún no ha publicado ningún tweet',
    },
    UI: {
        ANTERIOR: '← Anterior',
        SIGUIENTE: 'Siguiente →',
        MOSTRAR_MAS: 'Mostrar más',
        NO_HAY_MAS: 'No hay más...',
        VOLVER_TIMELINE: '← Volver al Timeline',
        CREAR_TWEET: 'Crear Tweet',
        HOME: 'Home',
        USUARIOS: 'Usuarios',
        SELECCIONAR_USUARIO: 'Selecciona un usuario',
        USUARIO_ID: 'Usuario (ID)',
        TEXTO_TWEET: 'Texto del Tweet',
        CREAR_TWEET_BUTTON: 'Crear Tweet',
        DEBE_SELECCIONAR_USUARIO: 'Por favor, selecciona un usuario primero',
        TWEET_SIN_ID: 'Error: Tweet sin ID',
    },
};

