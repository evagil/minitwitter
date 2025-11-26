import React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHome, faUsers } from "@fortawesome/free-solid-svg-icons";
import { CONFIG } from '@/constants/config';
import { MESSAGES } from '@/constants/messages';

export default function Header() {
    const location = useLocation();
    const navigate = useNavigate();

    const handleHome = () => {
        if (location.pathname !== CONFIG.ROUTES.HOME) {
            navigate(CONFIG.ROUTES.HOME);
        }
        window.dispatchEvent(new CustomEvent(CONFIG.EVENTS.RESET_HOME));
    };

    const handleCrearTweet = () => {
        if (location.pathname === CONFIG.ROUTES.HOME) {
            window.dispatchEvent(new CustomEvent(CONFIG.EVENTS.MOSTRAR_CREAR_TWEET));
        } else {
            navigate(CONFIG.ROUTES.HOME);
            setTimeout(() => {
                window.dispatchEvent(new CustomEvent(CONFIG.EVENTS.MOSTRAR_CREAR_TWEET));
            }, CONFIG.UI.NAVIGATION_DELAY);
        }
    };

    return (
        <header className="sticky top-0 z-10 bg-base-100 shadow-sm">
            <div className="flex items-center justify-between p-4">
                {/* Logo y título */}
                <div className="flex items-center gap-4">
                    <Link to="/" className="flex items-center gap-2">
                        <h1 className="text-2xl font-bold text-primary">MiniTwitter</h1>
                    </Link>
                </div>

                {/* Navegación */}
                <nav className="flex items-center gap-4">
                    <button
                        onClick={handleHome}
                        className="btn btn-ghost btn-sm flex items-center gap-2"
                    >
                        <FontAwesomeIcon icon={faHome} />
                        {MESSAGES.UI.HOME}
                    </button>
                    <Link
                        to={CONFIG.ROUTES.USUARIOS}
                        className="btn btn-ghost btn-sm flex items-center gap-2"
                    >
                        <FontAwesomeIcon icon={faUsers} />
                        {MESSAGES.UI.USUARIOS}
                    </Link>
                    <button
                        onClick={handleCrearTweet}
                        className="btn btn-ghost btn-sm flex items-center gap-2"
                    >
                        {MESSAGES.UI.CREAR_TWEET}
                    </button>
                </nav>
            </div>
        </header>
    );
}

