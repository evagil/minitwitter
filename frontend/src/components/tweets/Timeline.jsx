import React, { useEffect, useState } from 'react';
import { tweetService } from '@/services/tweetService';
import TweetCard from './TweetCard';
import { CONFIG } from '@/constants/config';
import { MESSAGES } from '@/constants/messages';

const Timeline = ({ currentUserId }) => {
    const [tweets, setTweets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [hasNext, setHasNext] = useState(false);
    const [hasPrev, setHasPrev] = useState(false);
    const limit = CONFIG.PAGINATION.TIMELINE_LIMIT;

    const cargarTweets = async (page = 0) => {
        try {
            setLoading(true);
            const offset = page * limit;
            const data = await tweetService.obtenerTimeline(offset, limit);
            
            setTweets(data);
            setCurrentPage(page);
            setHasNext(data.length === limit);
            setHasPrev(page > 0);
        } catch (err) {
            console.error('Error al cargar tweets:', err);
            setError(MESSAGES.ERROR.CARGAR_TWEETS);
            setTweets([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        cargarTweets(0);
    }, []);

    const handleTweetCreated = () => {
        cargarTweets(0);
    };

    const handleRetweetSuccess = () => {
        cargarTweets(currentPage);
    };

    const handleNextPage = () => {
        if (hasNext && !loading) {
            cargarTweets(currentPage + 1);
        }
    };

    const handlePrevPage = () => {
        if (hasPrev && !loading) {
            cargarTweets(currentPage - 1);
        }
    };

    if (loading && tweets.length === 0) {
        return (
            <div className="container mx-auto p-4">
                <div className="flex justify-center items-center min-h-64">
                    <div className="loading loading-spinner loading-lg text-primary"></div>
                    <span className="ml-2">{MESSAGES.LOADING.CARGANDO_TIMELINE}</span>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <div className="max-w-2xl mx-auto">
                <div className="text-center mb-8">
                    <h1 className="text-4xl font-bold text-primary mb-3">
                        MiniTwitter
                    </h1>
                    <p className="text-muted-foreground text-lg">
                        Tu timeline de tweets
                    </p>
                </div>

                {error && (
                    <div className="alert alert-error mb-6">
                        {error}
                    </div>
                )}

                <div className="space-y-4">
                    {tweets.length === 0 ? (
                        <div className="text-center py-12">
                            <p className="text-gray-500 text-lg">
                                {MESSAGES.EMPTY.NO_TWEETS}
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

                {/* Paginación */}
                <div className="flex justify-center items-center gap-4 mt-6">
                    <button
                        onClick={handlePrevPage}
                        className={`btn btn-primary ${(!hasPrev || loading) ? 'btn-disabled opacity-50 cursor-not-allowed' : ''}`}
                        disabled={!hasPrev || loading}
                    >
                        {MESSAGES.UI.ANTERIOR}
                    </button>
                    <span className="text-sm text-gray-500">
                        Página {currentPage + 1}
                    </span>
                    <button
                        onClick={handleNextPage}
                        className={`btn btn-primary ${(!hasNext || loading) ? 'btn-disabled opacity-50 cursor-not-allowed' : ''}`}
                        disabled={!hasNext || loading}
                    >
                        {MESSAGES.UI.SIGUIENTE}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Timeline;

