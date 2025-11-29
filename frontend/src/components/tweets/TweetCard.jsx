import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRetweet, faUser } from '@fortawesome/free-solid-svg-icons';
import { tweetService } from '@/services/tweetService';
import { userService } from '@/services/userService';
import { MESSAGES } from '@/constants/messages';

const TweetCard = ({ tweet, onRetweetSuccess, currentUserId }) => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleRetweet = async () => {
        if (!currentUserId) {
            alert(MESSAGES.UI.DEBE_SELECCIONAR_USUARIO);
            return;
        }

        if (!tweet.id) {
            alert(MESSAGES.UI.TWEET_SIN_ID);
            return;
        }

        setLoading(true);
        setError(null);
        setSuccess(null);

        try {
            await tweetService.hacerRetweet(currentUserId, tweet.id);
            setSuccess(MESSAGES.SUCCESS.RETWEET_EXITOSO);
            if (onRetweetSuccess) {
                onRetweetSuccess();
            }
            // Limpiar el mensaje de éxito después de 3 segundos
            setTimeout(() => {
                setSuccess(null);
            }, 3000);
        } catch (err) {
            const backendData = err.response?.data;
            const backendMessage = typeof backendData === 'string'
                ? backendData
                : backendData?.message;

            setError(
                backendMessage ||
                err.message ||
                MESSAGES.ERROR.RETWEET
            );
        } finally {
            setLoading(false);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleString('es-AR', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const esRetweet = tweet.tweetOriginalId !== null && tweet.tweetOriginalId !== undefined;

    return (
        <>
            <div className="card bg-neutral shadow-xl rounded-2xl transition-transform duration-300 hover:shadow-2xl">
                <div className="card-body p-4">
                    {esRetweet ? (
                        <>
                            {/* Header del retweet */}
                            <div className="flex items-start gap-3 mb-3">
                        <div className="avatar placeholder">
                            <div className="bg-primary text-primary-content rounded-full w-12 h-12 flex items-center justify-center">
                                <FontAwesomeIcon icon={faUser} />
                            </div>
                        </div>
                        <div className="flex-1">
                            <div className="flex items-center gap-2 mb-1">
                                <h3 className="font-bold text-accent">
                                    {tweet.autorUserName || 'Usuario desconocido'}
                                </h3>
                                    <span className="badge badge-primary badge-sm">
                                        <FontAwesomeIcon icon={faRetweet} className="mr-1" />
                                            Retuiteó
                                    </span>
                                    </div>
                                    <p className="text-sm text-gray-500 mb-2">
                                        Retuiteado el {formatDate(tweet.createdAt)}
                                    </p>
                                </div>
                            </div>

                            {/* Tweet original */}
                            <div className="bg-base-100 rounded-lg p-3 border-l-4 border-primary ml-15">
                                <div className="flex items-center gap-2 mb-2">
                                    <div className="avatar placeholder">
                                        <div className="bg-secondary text-secondary-content rounded-full w-8 h-8 flex items-center justify-center text-xs">
                                            <FontAwesomeIcon icon={faUser} />
                                        </div>
                                    </div>
                                    <div>
                                        <p className="text-sm font-semibold text-accent">
                                            {tweet.tweetOriginalAutor || 'Usuario desconocido'}
                                        </p>
                                    </div>
                                </div>
                                <p className="text-base-content text-base">{tweet.texto}</p>
                                {tweet.tweetOriginalCreatedAt && (
                                    <p className="text-xs text-gray-500 mt-2">
                                        Publicado el {formatDate(tweet.tweetOriginalCreatedAt)}
                                    </p>
                                )}
                            </div>
                        </>
                    ) : (
                        <>
                            {/* Header del tweet normal */}
                            <div className="flex items-start gap-3">
                                <div className="avatar placeholder">
                                    <div className="bg-primary text-primary-content rounded-full w-12 h-12 flex items-center justify-center">
                                        <FontAwesomeIcon icon={faUser} />
                                    </div>
                                </div>
                                <div className="flex-1">
                                    <div className="flex items-center gap-2 mb-1">
                                        <h3 className="font-bold text-accent">
                                            {tweet.autorUserName || 'Usuario desconocido'}
                                        </h3>
                            </div>
                            <p className="text-sm text-gray-500 mb-2">
                                {formatDate(tweet.createdAt)}
                            </p>
                        </div>
                    </div>

                    {/* Contenido del tweet */}
                    <div className="ml-15">
                                <p className="text-base-content text-lg mb-3">{tweet.texto}</p>
                            </div>
                        </>
                        )}

                    {/* Acciones */}
                    {currentUserId && (
                        <div className="flex flex-col gap-2 mt-2">
                            {error && (
                                <div className="alert alert-error alert-sm">
                                    <span className="text-xs">{error}</span>
                                </div>
                            )}
                            {success && (
                                <div className="alert alert-success alert-sm">
                                    <span className="text-xs">{success}</span>
                                </div>
                            )}
                            <div className="flex justify-end">
                                <button
                                    onClick={handleRetweet}
                                    className="btn btn-outline btn-primary btn-sm"
                                    disabled={loading}
                                >
                                    {loading ? (
                                        <span className="loading loading-spinner loading-sm"></span>
                                    ) : (
                                        <>
                                            <FontAwesomeIcon icon={faRetweet} />
                                            Retweet
                                        </>
                                    )}
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </>
    );
};

export default TweetCard;

