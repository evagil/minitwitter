# Descripción general

- Este es un proyecto escrito en **Java 17** utilizando el paradigma orientado a objetos.
- Se construye una versión simplificada de Twitter con arquitectura monolítica de backend.
- El backend y el frontend son aplicaciones separadas que se comunican vía servicios web.
- El modelo de dominio es donde se implementan todas las reglas de negocio.

## Estructura de Carpetas

- Es un proyecto Maven estándar.
- `src/main/java` para los fuentes.
- `unrn.model` contiene las clases del modelo de dominio.
- `src/test/java` para los tests unitarios e integración.
- `src/main/resources` para configuración (ej. persistencia).
- `src/test/resources` para datos de prueba (`test-data.sql`).
- `frontend/` contendrá la aplicación React para el front-end.

---

# Modelo de Dominio

Requerimientos a modelar:

1. Cada **Usuario** conoce todos los **Tweets** que hizo.
2. Un **Tweet** puede ser un **ReTweet** de otro, y debe conocer a su tweet de origen.
3. Un **ReTweet** no tiene texto adicional.
4. Los tweets de un usuario se deben eliminar al eliminar el usuario.
5. No se pueden agregar dos usuarios con el mismo `userName`.
6. `userName` debe tener entre 5 y 25 caracteres.
7. Los tweets deben tener entre 1 y 280 caracteres.
8. Un usuario no puede hacer re-tweet de un tweet creado por él mismo.

## Reglas de implementación

- Nunca generes **getters ni setters**: evitar objetos anémicos.
- Los objetos se inicializan siempre por **constructor** con validaciones incluidas.
- Cada validación del constructor debe estar en un método privado `assert{Condición}`.
- Excepciones: usar siempre `RuntimeException` con mensajes definidos como **constantes estáticas** de visibilidad de paquete.
- Aplicar el principio **Tell, Don’t Ask** siempre que sea posible.

---

# Testing Automatizado con JUnit 5

### 1. Nombre claro y descriptivo

- Nombra los métodos de test con el patrón:  
  **`cuestionATestear_resultadoEsperado`**
- Usa `@DisplayName("Cuestión a testear resultado esperado")`.

### 2. Sin mocks

- Los tests unitarios deben correr en memoria ejecutando código real.
- No usar mocks, stubs o fakes (solo en servicios externos si correspondiera).

### 3. Estructura de test

```java
@Test
@DisplayName("Nombre del test")
void testNombreDelMetodo() {
    // Setup: preparar el escenario
    // Ejercitación: ejecutar la acción
    // Verificación: comprobar resultado esperado
}
```

### 4. Un solo caso por test

- Cada test evalúa un único caso.
- Si hay varios casos, se crean tests separados.

### 5. Asserts claros y descriptivos

Ejemplo:

```java
assertEquals(expected, actual, "El tweet creado no coincide con lo esperado");
```

### 6. Casos límite a probar

- `null`
- listas vacías o textos vacíos
- longitudes inválidas de `userName` o `tweet`
- re-tweets no permitidos

### 7. Verificación de excepciones

```java
var ex = assertThrows(RuntimeException.class, () -> {
    // Código que debería fallar
});
assertEquals(User.ERROR_USERNAME_DUPLICADO, ex.getMessage());
```

### 8. Tests de integración

- Usar **H2 en memoria** como base de datos.
- Cargar datos iniciales con `test-data.sql`.
- Ejecutar `truncate` en `@BeforeEach` para limpiar datos.
- Incluir tests de persistencia y capa web.

---

# Frontend

El frontend se implementará con **React 19.1.1** y **Vite 7.2.4**.

### Configuración

El archivo `package.json` define el proyecto React:

- **react**: ^19.1.1
- **react-dom**: ^19.1.1
- **vite**: ^7.2.4
- **@vitejs/plugin-react**: ^5.0.0
- Incluye ESLint 9.33.0 para control de calidad de código.

### Scripts disponibles

- `npm run dev` → inicia el servidor de desarrollo con Vite.
- `npm run build` → compila la aplicación para producción.
- `npm run preview` → sirve la build para revisión.
- `npm run lint` → corre el linter en todo el proyecto.

### Estructura esperada

- `frontend/src` → fuentes del frontend React.
- `frontend/src/components` → componentes reutilizables.
- `frontend/src/pages` → páginas principales (Home, Crear Tweet, Usuario).

---

# Cobertura

- Se requiere al menos **90% de cobertura de código** entre tests unitarios e integración.
