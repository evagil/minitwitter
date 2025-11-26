import api from './api.js';

export const userService = {
    // Crear un nuevo usuario
    crearUsuario: async (userName) => {
        const response = await api.post('/usuarios', null, {
            params: { userName }
        });
        return response.data;
    },

    // Listar todos los usuarios
    listarUsuarios: async () => {
        const response = await api.get('/usuarios');
        return Array.isArray(response.data) ? response.data : [];
    },

    // Buscar usuario por ID
    buscarUsuario: async (id) => {
        const response = await api.get(`/usuarios/${id}`);
        return response.data;
    },

    // Eliminar usuario
    eliminarUsuario: async (id) => {
        const response = await api.delete(`/usuarios/${id}`);
        return response.data;
    }
};

