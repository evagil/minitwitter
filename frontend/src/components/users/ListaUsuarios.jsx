import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { userService } from '@/services/userService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faTrash } from '@fortawesome/free-solid-svg-icons';

const ListaUsuarios = ({ onUserSelect, selectedUserId }) => {
    const [usuarios, setUsuarios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        cargarUsuarios();
    }, []);

    const cargarUsuarios = async () => {
        try {
            setLoading(true);
            const data = await userService.listarUsuarios();
            setUsuarios(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error('Error al cargar usuarios:', err);
            setError('Error al cargar los usuarios');
            setUsuarios([]);
        } finally {
            setLoading(false);
        }
    };

    const handleEliminar = async (id) => {
        if (!window.confirm('¿Estás seguro de que quieres eliminar este usuario?')) {
            return;
        }

        try {
            await userService.eliminarUsuario(id);
            cargarUsuarios();
        } catch (err) {
            console.error('Error al eliminar usuario:', err);
            alert('Error al eliminar el usuario');
        }
    };

    if (loading) {
        return (
            <div className="container mx-auto p-4">
                <div className="flex justify-center items-center min-h-64">
                    <div className="loading loading-spinner loading-lg text-primary"></div>
                    <span className="ml-2">Cargando usuarios...</span>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <div className="max-w-4xl mx-auto">
                <div className="text-center mb-8">
                    <h1 className="text-4xl font-bold text-primary mb-3">
                        Usuarios
                    </h1>
                    <p className="text-muted-foreground text-lg">
                        Selecciona un usuario para comenzar
                    </p>
                </div>

                {error && (
                    <div className="alert alert-error mb-6">
                        {error}
                    </div>
                )}

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {usuarios.length === 0 ? (
                        <div className="col-span-full text-center py-12">
                            <p className="text-gray-500 text-lg">
                                No hay usuarios registrados
                            </p>
                        </div>
                    ) : (
                        usuarios.map((usuario) => (
                            <div
                                key={usuario.id}
                                className={`card bg-base-100 shadow-xl rounded-3xl border border-base-300 transition-transform duration-300 hover:-translate-y-1 hover:shadow-2xl ${
                                    selectedUserId === usuario.id ? 'ring-2 ring-primary ring-offset-2' : ''
                                }`}
                            >
                                <div className="card-body p-5 space-y-4">
                                    <div className="flex items-center gap-3">
                                        <div className="avatar placeholder">
                                            <div className="bg-primary text-primary-content rounded-full w-12 h-12 flex items-center justify-center">
                                                <FontAwesomeIcon icon={faUser} />
                                            </div>
                                        </div>
                                        <div className="flex-1 min-w-0">
                                            <h3 className="font-bold text-accent truncate">
                                                {usuario.userName}
                                            </h3>
                                            <p className="text-sm text-gray-500">
                                                ID: {usuario.id}
                                            </p>
                                        </div>
                                    </div>
                                    <div className="flex gap-2 pt-2">
                                        <button
                                            onClick={() => onUserSelect && onUserSelect(usuario.id)}
                                            className={`btn btn-sm flex-1 font-semibold ${
                                                selectedUserId === usuario.id
                                                    ? 'btn-primary'
                                                    : 'btn-outline btn-primary'
                                            }`}
                                        >
                                            {selectedUserId === usuario.id ? 'Seleccionado' : 'Seleccionar'}
                                        </button>
                                        <Link
                                            to={`/usuario/${usuario.id}`}
                                            className="btn btn-secondary btn-sm"
                                        >
                                            Ver Perfil
                                        </Link>
                                        <button
                                            onClick={() => handleEliminar(usuario.id)}
                                            className="btn btn-ghost btn-sm text-error"
                                        >
                                            <FontAwesomeIcon icon={faTrash} />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default ListaUsuarios;

