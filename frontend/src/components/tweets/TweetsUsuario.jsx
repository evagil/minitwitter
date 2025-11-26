import React, { useEffect, useState } from 'react';
import { tweetService } from '@/services/tweetService';
import TweetCard from './TweetCard';
import { CONFIG } from '@/constants/config';
import { MESSAGES } from '@/constants/messages';

const TweetsUsuario = ({ userId, onVolver, currentUserId }) => {
    const [tweets, setTweets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [offset, setOffset] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const limit = CONFIG.PAGINATION.USER_TWEETS_LIMIT;

    useEffect(() => {
        cargarTweets(true);
    }, [userId]);

    const cargarTweets = async (reset = false) => {
        try {
            setLoading(true);
            const currentOffset = reset ? 0 : offset;
            const data = await tweetService.obtenerTweetsDeUsuario(userId, currentOffset, limit);
            
            if (reset) {
                setTweets(data);
                setOffset(limit);
            } else {
                setTweets(prev => [...prev, ...data]);
                setOffset(prev => prev + limit);
            }
            
            setHasMore(data.length === limit);
        } catch (err) {
            console.error('Error al cargar tweets:', err);
            setError(MESSAGES.ERROR.CARGAR_TWEETS_USUARIO);
        } finally {
            setLoading(false);
        }
    };

    const handleRetweetSuccess = () => {
        cargarTweets(true);
    };

    if (loading && tweets.length === 0) {
        return (
            <div className="container mx-auto p-4 max-w-2xl">
                <div className="flex justify-center items-center min-h-64">
                    <div className="loading loading-spinner loading-lg text-primary"></div>
                    <span className="ml-2">{MESSAGES.LOADING.CARGANDO_TWEETS}</span>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4 max-w-2xl">
            {onVolver && (
                <button
                    onClick={onVolver}
                    className="btn btn-ghost btn-sm mb-4"
                >
                    {MESSAGES.UI.VOLVER_TIMELINE}
                </button>
            )}

            {error && (
                <div className="alert alert-error mb-6">
                    {error}
                </div>
            )}

            <div className="space-y-4">
                {tweets.length === 0 ? (
                    <div className="text-center py-12">
                        <p className="text-gray-500 text-lg">
                            {MESSAGES.EMPTY.NO_TWEETS_USUARIO}
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
                        onClick={() => cargarTweets(false)}
                        className="btn btn-primary"
                        disabled={loading}
                    >
                        {loading ? (
                            <span className="loading loading-spinner loading-sm"></span>
                        ) : (
                            MESSAGES.UI.MOSTRAR_MAS
                        )}
                    </button>
                ) : tweets.length > 0 ? (
                    <button className="btn btn-ghost" disabled>
                        {MESSAGES.UI.NO_HAY_MAS}
                    </button>
                ) : null}
            </div>
        </div>
    );
};

export default TweetsUsuario;

