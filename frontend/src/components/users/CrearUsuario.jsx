import React, { useState } from 'react';
import { userService } from '@/services/userService';
import { CONFIG } from '@/constants/config';
import { MESSAGES } from '@/constants/messages';

const CrearUsuario = ({ onUsuarioCreado }) => {
    const [userName, setUserName] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!userName.trim()) {
            setError(MESSAGES.VALIDATION.USERNAME_VACIO);
            return;
        }

        const { USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH } = CONFIG.VALIDATION;
        if (userName.length < USERNAME_MIN_LENGTH || userName.length > USERNAME_MAX_LENGTH) {
            setError(MESSAGES.VALIDATION.USERNAME_LONGITUD);
            return;
        }

        setLoading(true);
        setError(null);

        try {
            await userService.crearUsuario(userName);
            setUserName('');
            if (onUsuarioCreado) {
                onUsuarioCreado();
            }
        } catch (err) {
            // El backend puede retornar el mensaje como string directo o como objeto con message
            const errorMessage = err.response?.data?.message 
                || (typeof err.response?.data === 'string' ? err.response.data : null)
                || err.message 
                || MESSAGES.ERROR.CREAR_USUARIO;
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-3xl mx-auto mb-8">
            <div className="card bg-base-100 shadow-2xl rounded-3xl border border-base-300">
                <div className="card-body p-8 space-y-6">
                    <div className="flex items-baseline justify-between">
                        <h2 className="text-3xl font-extrabold text-primary tracking-tight">
                            Crear Usuario
                        </h2>
                        <span className="badge badge-outline badge-primary text-xs uppercase">
                            Nuevo usuario
                        </span>
                    </div>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <label className="form-control w-full">
                            <div className="label">
                                <span className="label-text font-semibold text-sm">
                                    Nombre de usuario
                                </span>
                                <span className="label-text-alt text-xs text-gray-500">
                                    {CONFIG.VALIDATION.USERNAME_MIN_LENGTH}-{CONFIG.VALIDATION.USERNAME_MAX_LENGTH} caracteres
                                </span>
                            </div>
                            <input
                                type="text"
                                className="input input-bordered w-full input-lg"
                                placeholder="Escribí el nombre de usuario..."
                                value={userName}
                                onChange={(e) => setUserName(e.target.value)}
                                minLength={CONFIG.VALIDATION.USERNAME_MIN_LENGTH}
                                maxLength={CONFIG.VALIDATION.USERNAME_MAX_LENGTH}
                                disabled={loading}
                            />
                        </label>
                        {error && (
                            <div className="alert alert-error shadow-sm">
                                <span className="text-sm">{error}</span>
                            </div>
                        )}
                        <div className="flex justify-end">
                            <button
                                type="submit"
                                className="btn btn-primary btn-lg px-8"
                                disabled={loading || !userName.trim()}
                            >
                                {loading ? (
                                    <span className="loading loading-spinner loading-sm"></span>
                                ) : (
                                    'Crear Usuario'
                                )}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default CrearUsuario;

