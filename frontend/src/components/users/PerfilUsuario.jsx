import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { userService } from '@/services/userService';
import { tweetService } from '@/services/tweetService';
import TweetCard from '../tweets/TweetCard';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faArrowLeft } from '@fortawesome/free-solid-svg-icons';

const PerfilUsuario = ({ currentUserId }) => {
    const { id } = useParams();
    const [usuario, setUsuario] = useState(null);
    const [tweets, setTweets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [offset, setOffset] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const limit = 15;

    useEffect(() => {
        cargarDatos(true);
    }, [id]);

    const cargarDatos = async (reset = false) => {
        try {
            setLoading(true);
            const [userData, tweetsData] = await Promise.all([
                userService.buscarUsuario(id),
                tweetService.obtenerTweetsDeUsuario(id, reset ? 0 : offset, limit)
            ]);

            setUsuario(userData);
            
            if (reset) {
                setTweets(tweetsData);
                setOffset(limit);
            } else {
                setTweets(prev => [...prev, ...tweetsData]);
                setOffset(prev => prev + limit);
            }
            
            setHasMore(tweetsData.length === limit);
        } catch (err) {
            console.error('Error al cargar datos:', err);
            setError('Error al cargar el perfil del usuario');
        } finally {
            setLoading(false);
        }
    };

    const handleRetweetSuccess = () => {
        cargarDatos(true);
    };

    const handleLoadMore = () => {
        if (!loading && hasMore) {
            cargarDatos(false);
        }
    };

    if (loading && !usuario) {
        return (
            <div className="container mx-auto p-4">
                <div className="flex justify-center items-center min-h-64">
                    <div className="loading loading-spinner loading-lg text-primary"></div>
                    <span className="ml-2">Cargando perfil...</span>
                </div>
            </div>
        );
    }

    if (error && !usuario) {
        return (
            <div className="container mx-auto p-4">
                <div className="alert alert-error">
                    {error}
                </div>
                <Link to="/" className="btn btn-primary mt-4">
                    Volver al Timeline
                </Link>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <div className="max-w-2xl mx-auto">
                <Link to="/" className="btn btn-ghost btn-sm mb-4">
                    <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
                    Volver al Timeline
                </Link>

                {usuario && (
                    <div className="card bg-neutral shadow-xl rounded-2xl mb-6">
                        <div className="card-body p-6">
                            <div className="flex items-center gap-4">
                                <div className="avatar placeholder">
                                    <div className="bg-primary text-primary-content rounded-full w-20 h-20 flex items-center justify-center text-3xl">
                                        <FontAwesomeIcon icon={faUser} />
                                    </div>
                                </div>
                                <div>
                                    <h2 className="text-3xl font-bold text-primary">
                                        {usuario.userName}
                                    </h2>
                                    <p className="text-gray-500">ID: {usuario.id}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                <h3 className="text-2xl font-bold text-secondary mb-4">
                    Tweets ({tweets.length})
                </h3>

                <div className="space-y-4">
                    {tweets.length === 0 ? (
                        <div className="text-center py-12">
                            <p className="text-gray-500 text-lg">
                                Este usuario aún no ha publicado ningún tweet
                            </p>
                        </div>
                    ) : (
                        tweets.map((tweet) => (
                            <TweetCard
                                key={tweet.id}
                                tweet={tweet}
                                currentUserId={currentUserId}
                                onRetweetSuccess={handleRetweetSuccess}
                            />
                        ))
                    )}
                </div>

                <div className="text-center mt-6">
                    {hasMore ? (
                        <button
                            onClick={handleLoadMore}
                            className="btn btn-primary"
                            disabled={loading}
                        >
                            {loading ? (
                                <span className="loading loading-spinner loading-sm"></span>
                            ) : (
                                'Mostrar más'
                            )}
                        </button>
                    ) : tweets.length > 0 ? (
                        <button className="btn btn-ghost" disabled>
                            No hay más...
                        </button>
                    ) : null}
                </div>
            </div>
        </div>
    );
};

export default PerfilUsuario;

