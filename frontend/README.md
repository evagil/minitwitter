# MiniTwitter Frontend

Frontend de MiniTwitter construido con React + Vite, Tailwind CSS y DaisyUI.

## Características

- ✅ Timeline de tweets con paginación
- ✅ Crear nuevos tweets
- ✅ Hacer retweets con comentarios opcionales
- ✅ Ver perfil de usuarios
- ✅ Lista de usuarios
- ✅ Crear nuevos usuarios
- ✅ Diseño responsive y moderno
- ✅ Tema personalizado siguiendo el estilo del proyecto almacen-de-peliculas

## Tecnologías

- **React 19** - Biblioteca de UI
- **Vite** - Build tool y dev server
- **Tailwind CSS** - Framework de CSS utility-first
- **DaisyUI** - Componentes de UI para Tailwind
- **React Router** - Navegación
- **Axios** - Cliente HTTP
- **FontAwesome** - Iconos

## Instalación

1. Instalar dependencias:
```bash
npm install
```

2. Iniciar el servidor de desarrollo:
```bash
npm run dev
```

El frontend estará disponible en `http://localhost:3001`

## Configuración

El frontend está configurado para conectarse al backend en `http://localhost:8080`. 
Asegúrate de que el backend de MiniTwitter esté corriendo antes de usar el frontend.

## Estructura del Proyecto

```
frontend/
├── src/
│   ├── components/
│   │   ├── shared/        # Componentes compartidos (Header, UserSelector)
│   │   ├── tweets/        # Componentes de tweets (Timeline, TweetCard, CrearTweet)
│   │   └── users/          # Componentes de usuarios (ListaUsuarios, PerfilUsuario, CrearUsuario)
│   ├── hooks/             # Custom hooks (useCurrentUser)
│   ├── services/          # Servicios API (tweetService, userService, api)
│   ├── App.jsx            # Componente principal
│   ├── main.jsx           # Punto de entrada
│   └── index.css          # Estilos globales
├── package.json
├── vite.config.js
└── tailwind.config.js
```

## Uso

1. **Seleccionar Usuario**: Haz clic en el botón "Seleccionar Usuario" en la esquina inferior derecha para elegir un usuario activo.

2. **Crear Tweet**: Una vez seleccionado un usuario, puedes crear tweets desde el timeline.

3. **Hacer Retweet**: Haz clic en el botón "Retweet" de cualquier tweet para compartirlo.

4. **Ver Perfil**: Haz clic en "Ver Perfil" en la lista de usuarios o navega a `/usuario/:id`.

5. **Crear Usuario**: Ve a la página de usuarios y crea un nuevo usuario con un nombre entre 5 y 25 caracteres.

## Scripts Disponibles

- `npm run dev` - Inicia el servidor de desarrollo
- `npm run build` - Construye la aplicación para producción
- `npm run preview` - Previsualiza la build de producción
- `npm run lint` - Ejecuta el linter

