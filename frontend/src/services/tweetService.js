import api from './api.js';

export const tweetService = {
    // Crear un nuevo tweet
    crearTweet: async (userId, texto) => {
        const response = await api.post('/tweets/crear', null, {
            params: { userId, texto }
        });
        return response.data;
    },

    // Hacer retweet
    hacerRetweet: async (userId, tweetId) => {
        const response = await api.post('/tweets/retweet', {
            userId,
            tweetId
        });
        return response.data;
    },

    // Obtener timeline con paginación
    obtenerTimeline: async (offset = 0, limit = 10) => {
        const response = await api.get('/tweets/timeline', {
            params: { offset, limit }
        });
        return Array.isArray(response.data) ? response.data : [];
    },

    // Obtener tweets de un usuario
    obtenerTweetsDeUsuario: async (userId, offset = 0, limit = 15) => {
        const response = await api.get(`/tweets/de-usuario/${userId}`, {
            params: { offset, limit }
        });
        return Array.isArray(response.data) ? response.data : [];
    },

    // Listar retweets
    listarRetweets: async (offset = 0, limit = 10) => {
        const response = await api.get('/tweets/retweets', {
            params: { offset, limit }
        });
        return Array.isArray(response.data) ? response.data : [];
    }
};

