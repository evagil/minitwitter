import React, { useState, useEffect } from 'react';
import PanelIzquierdo from '../shared/PanelIzquierdo';
import Timeline from '../tweets/Timeline';
import CrearTweet from '../tweets/CrearTweet';
import TweetsUsuario from '../tweets/TweetsUsuario';
import { CONFIG } from '@/constants/config';
import { MESSAGES } from '@/constants/messages';
import { useCurrentUser } from '@/hooks/useCurrentUser';

const HomePage = () => {
    const [mostrarCrearTweet, setMostrarCrearTweet] = useState(false);
    const [usuarioSeleccionado, setUsuarioSeleccionado] = useState(null);
    const { currentUserId } = useCurrentUser();

    useEffect(() => {
        const handleMostrarCrearTweet = () => {
            setMostrarCrearTweet(true);
            setUsuarioSeleccionado(null);
        };

        window.addEventListener(CONFIG.EVENTS.MOSTRAR_CREAR_TWEET, handleMostrarCrearTweet);
        return () => {
            window.removeEventListener(CONFIG.EVENTS.MOSTRAR_CREAR_TWEET, handleMostrarCrearTweet);
        };
    }, []);

    useEffect(() => {
        const handleResetHome = () => {
            setMostrarCrearTweet(false);
            setUsuarioSeleccionado(null);
        };

        window.addEventListener(CONFIG.EVENTS.RESET_HOME, handleResetHome);
        return () => {
            window.removeEventListener(CONFIG.EVENTS.RESET_HOME, handleResetHome);
        };
    }, []);

    const handleUserSelect = (userId) => {
        setUsuarioSeleccionado(userId);
        setMostrarCrearTweet(false);
    };

    const handleVolverHome = () => {
        setMostrarCrearTweet(false);
        setUsuarioSeleccionado(null);
    };

    return (
        <div className="flex h-[calc(100vh-64px)]">
            {/* Panel Izquierdo */}
            <div className="w-64 flex-shrink-0 border-r border-base-300">
                <PanelIzquierdo 
                    onUserSelect={handleUserSelect}
                    selectedUserId={usuarioSeleccionado}
                />
            </div>

            {/* Panel Principal */}
            <div className="flex-1 overflow-y-auto">
                {mostrarCrearTweet ? (
                    <div className="container mx-auto p-4 max-w-2xl">
                        <button
                            onClick={handleVolverHome}
                            className="btn btn-ghost btn-sm mb-4"
                        >
                            {MESSAGES.UI.VOLVER_TIMELINE}
                        </button>
                        <CrearTweet onTweetCreated={handleVolverHome} />
                    </div>
                ) : usuarioSeleccionado ? (
                    <TweetsUsuario 
                        userId={usuarioSeleccionado}
                        currentUserId={currentUserId}
                        onVolver={handleVolverHome}
                    />
                ) : (
                    <Timeline currentUserId={currentUserId} />
                )}
            </div>
        </div>
    );
};

export default HomePage;

