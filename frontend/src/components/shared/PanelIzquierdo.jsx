import React, { useEffect, useState } from 'react';
import { userService } from '@/services/userService';
import { MESSAGES } from '@/constants/messages';

const PanelIzquierdo = ({ onUserSelect, selectedUserId }) => {
    const [usuarios, setUsuarios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        cargarUsuarios();
    }, []);

    const cargarUsuarios = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await userService.listarUsuarios();
            setUsuarios(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error('Error al cargar usuarios:', err);
            setError(MESSAGES.ERROR.CARGAR_USUARIOS);
            setUsuarios([]);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="p-4">
                <div className="flex justify-center items-center min-h-32">
                    <div className="loading loading-spinner loading-sm text-primary"></div>
                    <span className="ml-2 text-sm">{MESSAGES.LOADING.CARGANDO}</span>
                </div>
            </div>
        );
    }

    return (
        <div className="h-full bg-base-200 p-4">
            <div className="flex items-center justify-between mb-4">
                <h2 className="text-xl font-bold text-primary">Usuarios</h2>
            </div>
            {error && (
                <div className="alert alert-error alert-sm mb-4">
                    <span className="text-xs">{error}</span>
                </div>
            )}
            <div className="space-y-2">
                {usuarios.length === 0 ? (
                    <p className="text-sm text-gray-500">{MESSAGES.EMPTY.NO_USUARIOS}</p>
                ) : (
                    usuarios.map((usuario) => (
                        <button
                            key={usuario.id}
                            onClick={() => onUserSelect && onUserSelect(usuario.id)}
                            className={`btn btn-sm w-full justify-start ${
                                selectedUserId === usuario.id
                                    ? 'btn-primary'
                                    : 'btn-ghost'
                            }`}
                        >
                            {usuario.userName}
                        </button>
                    ))
                )}
            </div>
        </div>
    );
};

export default PanelIzquierdo;

