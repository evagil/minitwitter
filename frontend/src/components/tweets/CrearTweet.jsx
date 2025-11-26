import React, { useState, useEffect } from 'react';
import { tweetService } from '@/services/tweetService';
import { userService } from '@/services/userService';
import { CONFIG } from '@/constants/config';
import { MESSAGES } from '@/constants/messages';

const CrearTweet = ({ onTweetCreated }) => {
    const [userId, setUserId] = useState('');
    const [texto, setTexto] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const [usuarios, setUsuarios] = useState([]);

    useEffect(() => {
        cargarUsuarios();
    }, []);

    const cargarUsuarios = async () => {
        try {
            const data = await userService.listarUsuarios();
            setUsuarios(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error('Error al cargar usuarios:', err);
            setUsuarios([]);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!userId) {
            setError(MESSAGES.VALIDATION.USUARIO_REQUERIDO);
            return;
        }

        if (!texto.trim()) {
            setError(MESSAGES.VALIDATION.TWEET_VACIO);
            return;
        }

        if (texto.length > CONFIG.VALIDATION.TWEET_MAX_LENGTH) {
            setError(MESSAGES.VALIDATION.TWEET_LONGITUD);
            return;
        }

        setLoading(true);
        setError(null);
        setSuccess(false);

        try {
            await tweetService.crearTweet(parseInt(userId), texto);
            setTexto('');
            setSuccess(true);
            setTimeout(() => {
                setSuccess(false);
                if (onTweetCreated) {
                    onTweetCreated();
                }
            }, CONFIG.UI.SUCCESS_MESSAGE_DURATION);
        } catch (err) {
            setError(err.response?.data?.message || err.message || MESSAGES.ERROR.CREAR_TWEET);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="card bg-neutral shadow-xl rounded-2xl mb-6">
            <div className="card-body p-6">
                <h2 className="text-2xl font-bold text-primary mb-4">Crear Tweet</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="label">
                            <span className="label-text">{MESSAGES.UI.USUARIO_ID}</span>
                        </label>
                        <select
                            className="select select-bordered w-full"
                            value={userId}
                            onChange={(e) => setUserId(e.target.value)}
                            disabled={loading}
                            required
                        >
                            <option value="">{MESSAGES.UI.SELECCIONAR_USUARIO}</option>
                            {usuarios.map((usuario) => (
                                <option key={usuario.id} value={usuario.id}>
                                    {usuario.userName} (ID: {usuario.id})
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="mb-4">
                        <label className="label">
                            <span className="label-text">{MESSAGES.UI.TEXTO_TWEET}</span>
                        </label>
                        <textarea
                            className="textarea textarea-bordered w-full"
                            placeholder="¿Qué está pasando?"
                            value={texto}
                            onChange={(e) => setTexto(e.target.value)}
                            maxLength={CONFIG.VALIDATION.TWEET_MAX_LENGTH}
                            rows={4}
                            disabled={loading}
                            required
                        />
                        <div className="label">
                            <span className="label-text-alt text-gray-500">
                                {texto.length}/{CONFIG.VALIDATION.TWEET_MAX_LENGTH}
                            </span>
                        </div>
                    </div>
                    {error && (
                        <div className="alert alert-error mb-4">
                            <span className="text-sm">{error}</span>
                        </div>
                    )}
                    {success && (
                        <div className="alert alert-success mb-4">
                            <span className="text-sm">{MESSAGES.SUCCESS.TWEET_CREADO}</span>
                        </div>
                    )}
                    <div className="flex justify-end">
                        <button
                            type="submit"
                            className="btn btn-primary"
                            disabled={loading || !userId || !texto.trim()}
                        >
                            {loading ? (
                                <span className="loading loading-spinner loading-sm"></span>
                            ) : (
                                MESSAGES.UI.CREAR_TWEET_BUTTON
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CrearTweet;

