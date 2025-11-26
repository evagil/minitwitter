import { createContext, useContext, useState } from 'react';

const CurrentUserContext = createContext();

export const CurrentUserProvider = ({ children }) => {
    const [currentUserId, setCurrentUserId] = useState(null);

    return (
        <CurrentUserContext.Provider value={{ currentUserId, setCurrentUserId }}>
            {children}
        </CurrentUserContext.Provider>
    );
};

export const useCurrentUser = () => {
    const context = useContext(CurrentUserContext);
    if (!context) {
        throw new Error('useCurrentUser debe usarse dentro de CurrentUserProvider');
    }
    return context;
};

