import React from 'react';
import { useCurrentUser } from '@/hooks/useCurrentUser';
import { userService } from '@/services/userService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faTimes } from '@fortawesome/free-solid-svg-icons';

const UserSelector = () => {
    const { currentUserId, setCurrentUserId } = useCurrentUser();
    const [showSelector, setShowSelector] = React.useState(false);
    const [usuarios, setUsuarios] = React.useState([]);
    const [loading, setLoading] = React.useState(false);

    React.useEffect(() => {
        if (showSelector) {
            cargarUsuarios();
        }
    }, [showSelector]);

    const cargarUsuarios = async () => {
        try {
            setLoading(true);
            const data = await userService.listarUsuarios();
            setUsuarios(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error('Error al cargar usuarios:', err);
            setUsuarios([]);
        } finally {
            setLoading(false);
        }
    };

    const handleSelectUser = (userId) => {
        setCurrentUserId(userId);
        setShowSelector(false);
    };

    const currentUser = usuarios.find(u => u.id === currentUserId);

    return (
        <div className="fixed bottom-4 right-4 z-50">
            {currentUserId && currentUser ? (
                <div className="card bg-primary text-primary-content shadow-2xl">
                    <div className="card-body p-4">
                        <div className="flex items-center gap-3">
                            <div className="avatar placeholder">
                                <div className="bg-primary-content text-primary rounded-full w-10 h-10 flex items-center justify-center">
                                    <FontAwesomeIcon icon={faUser} />
                                </div>
                            </div>
                            <div>
                                <p className="font-bold">{currentUser.userName}</p>
                                <p className="text-xs opacity-80">Usuario activo</p>
                            </div>
                            <button
                                onClick={() => setCurrentUserId(null)}
                                className="btn btn-ghost btn-sm btn-circle"
                            >
                                <FontAwesomeIcon icon={faTimes} />
                            </button>
                        </div>
                    </div>
                </div>
            ) : (
                <button
                    onClick={() => setShowSelector(true)}
                    className="btn btn-primary btn-lg shadow-2xl"
                >
                    <FontAwesomeIcon icon={faUser} className="mr-2" />
                    Seleccionar Usuario
                </button>
            )}

            {showSelector && (
                <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-50">
                    <div className="bg-base-100 rounded-2xl p-6 w-full max-w-md shadow-lg max-h-[80vh] overflow-y-auto">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-semibold text-primary">
                                Seleccionar Usuario
                            </h3>
                            <button
                                onClick={() => setShowSelector(false)}
                                className="btn btn-ghost btn-sm btn-circle"
                            >
                                <FontAwesomeIcon icon={faTimes} />
                            </button>
                        </div>
                        {loading ? (
                            <div className="flex justify-center py-8">
                                <div className="loading loading-spinner loading-lg text-primary"></div>
                            </div>
                        ) : (
                            <div className="space-y-2">
                                {usuarios.length === 0 ? (
                                    <p className="text-center text-gray-500 py-4">
                                        No hay usuarios disponibles
                                    </p>
                                ) : (
                                    usuarios.map((usuario) => (
                                        <button
                                            key={usuario.id}
                                            onClick={() => handleSelectUser(usuario.id)}
                                            className={`btn btn-block justify-start ${
                                                currentUserId === usuario.id
                                                    ? 'btn-primary'
                                                    : 'btn-outline'
                                            }`}
                                        >
                                            <FontAwesomeIcon icon={faUser} className="mr-2" />
                                            {usuario.userName}
                                        </button>
                                    ))
                                )}
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserSelector;

