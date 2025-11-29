import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useState } from 'react';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faHome, faUser, faUsers, faRetweet, faTrash, faArrowLeft, faTimes } from '@fortawesome/free-solid-svg-icons';
import { CurrentUserProvider } from './hooks/useCurrentUser';
import ErrorBoundary from './components/shared/ErrorBoundary';
import Header from './components/shared/layout/Header';
import HomePage from './components/pages/HomePage';
import ListaUsuarios from './components/users/ListaUsuarios';
import PerfilUsuario from './components/users/PerfilUsuario';
import CrearUsuario from './components/users/CrearUsuario';
import { useCurrentUser } from './hooks/useCurrentUser';
import UserSelector from './components/shared/UserSelector';
import './App.css';

library.add(faHome, faUser, faUsers, faRetweet, faTrash, faArrowLeft, faTimes);

function AppContent() {
    const { currentUserId, setCurrentUserId } = useCurrentUser();
    const [usuariosVersion, setUsuariosVersion] = useState(0);

    return (
        <Router>
            <Header />
            <div className="bg-base-100 text-base-content min-h-screen">
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route 
                        path="/usuarios" 
                        element={
                            <div>
                                <CrearUsuario 
                                    onUsuarioCreado={() => {
                                        setUsuariosVersion((v) => v + 1);
                                    }} 
                                />
                                <ListaUsuarios 
                                    key={usuariosVersion}
                                />
                            </div>
                        } 
                    />
                    <Route 
                        path="/usuario/:id" 
                        element={<PerfilUsuario currentUserId={currentUserId} />} 
                    />
                </Routes>
            </div>
            <UserSelector />
        </Router>
    );
}

function App() {
    return (
        <ErrorBoundary>
            <CurrentUserProvider>
                <AppContent />
            </CurrentUserProvider>
        </ErrorBoundary>
    );
}

export default App;

