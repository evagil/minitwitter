// Configuración de la aplicación
export const CONFIG = {
    API: {
        TIMEOUT: 10000, // 10 segundos
        BASE_URL_DEV: '',
        BASE_URL_PROD: import.meta.env.VITE_API_URL || 'http://localhost:8080',
    },
    PAGINATION: {
        TIMELINE_LIMIT: 10,
        USER_TWEETS_LIMIT: 15,
    },
    VALIDATION: {
        USERNAME_MIN_LENGTH: 5,
        USERNAME_MAX_LENGTH: 25,
        TWEET_MIN_LENGTH: 1,
        TWEET_MAX_LENGTH: 280,
    },
    UI: {
        SUCCESS_MESSAGE_DURATION: 2000, // 2 segundos
        NAVIGATION_DELAY: 100, // 100ms
    },
    ROUTES: {
        HOME: '/',
        USUARIOS: '/usuarios',
        USUARIO: (id) => `/usuario/${id}`,
    },
    EVENTS: {
        MOSTRAR_CREAR_TWEET: 'mostrarCrearTweet',
        RESET_HOME: 'resetHome',
    },
};

