# Integración de API - PeluHome

Este documento describe la integración implementada entre la aplicación móvil PeluHome (Kotlin Multiplatform) y el API backend (Node.js).

## Arquitectura Implementada

La implementación sigue una arquitectura limpia con separación de capas:

### 📁 Estructura de Carpetas

```
composeApp/src/commonMain/kotlin/com/peluhome/project/
├── core/
│   └── Result.kt                          # Sealed class para manejo de respuestas
├── data/
│   ├── model/
│   │   ├── request.kt                     # Modelos de petición (SignInRequest, RegisterUserRequest)
│   │   └── response.kt                    # Modelos de respuesta (SignInResponse, RegisterUserResponse, ErrorResponse)
│   ├── repository/
│   │   └── AuthRepositoryImp.kt          # Implementación del repositorio de autenticación
│   ├── util/
│   │   └── Constants.kt                   # Constantes (URLs, endpoints, keys)
│   └── installAuthHeaderInterceptor.kt   # Interceptor para añadir token JWT
├── domain/
│   ├── model/
│   │   └── User.kt                        # Modelo de dominio del usuario
│   └── repository/
│       └── AuthRepository.kt              # Interface del repositorio
├── local/
│   └── StoreManager.kt                    # Gestión de almacenamiento local (DataStore)
├── di/
│   └── modules.kt                         # Módulos de inyección de dependencias (Koin)
├── presentation/
│   ├── sign_in/
│   │   ├── SignInViewModel.kt             # ViewModel para login
│   │   └── SignInScreen.kt                # Pantalla de login (actualizada)
│   ├── register_user/
│   │   ├── RegisterUserViewModel.kt       # ViewModel para registro
│   │   └── RegisterUserScreen.kt          # Pantalla de registro (actualizada)
│   └── components/
│       └── AlertComponent.kt              # Componente de diálogo de alertas
└── utils/
    └── createDataStorePref.kt             # Utilidad para crear DataStore
```

## Tecnologías Utilizadas

- **Ktor Client**: Cliente HTTP multiplataforma
- **Kotlinx Serialization**: Serialización/deserialización JSON
- **Koin**: Inyección de dependencias
- **DataStore**: Almacenamiento local persistente
- **Coroutines**: Programación asíncrona
- **ViewModel**: Gestión de estado de UI

## Endpoints Implementados

### 1. Registro de Usuario
- **Endpoint**: `POST /api/auth/register`
- **Request Body**:
```json
{
  "names": "string",
  "paternal_surname": "string",
  "maternal_surname": "string",
  "document_type": "string",
  "document_number": "string",
  "phone": "string",
  "email": "string",
  "password": "string"
}
```
- **Response**:
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "user": { ... },
    "token": "jwt_token"
  }
}
```

### 2. Inicio de Sesión
- **Endpoint**: `POST /api/auth/login`
- **Request Body**:
```json
{
  "document_number": "string",
  "password": "string"
}
```
- **Response**:
```json
{
  "success": true,
  "message": "Inicio de sesión exitoso",
  "data": {
    "user": { ... },
    "token": "jwt_token"
  }
}
```

## Configuración

### 1. URL del API

Actualiza la URL base del API en `Constants.kt`:

```kotlin
const val URL_BASE = "http://10.0.2.2:3000/api/"  // Android Emulator
// o
const val URL_BASE = "http://localhost:3000/api/"   // iOS Simulator
// o
const val URL_BASE = "https://tu-api.com/api/"      // Producción
```

**Nota importante para desarrollo local:**
- **Android Emulator**: Usa `10.0.2.2` en lugar de `localhost`
- **iOS Simulator**: Usa `localhost`
- **Dispositivo físico**: Usa la IP de tu computadora en la red local

### 2. Permisos de Internet (Android)

Ya configurado en `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 3. Configuración de Koin

Ya inicializado en:
- **Android**: `PeluHomeApplication.kt`
- **iOS**: `MainViewController.kt`

## Uso de los ViewModels

### SignInViewModel

```kotlin
@Composable
fun MiPantalla() {
    val viewModel: SignInViewModel = koinViewModel()
    val state = viewModel.state
    
    // Llamar al login
    viewModel.signIn(documentNumber, password)
    
    // Manejar estados
    LaunchedEffect(state.success, state.error) {
        when {
            state.success != null -> {
                // Navegar a home
                viewModel.clear()
            }
            state.error != null -> {
                // Mostrar error
                viewModel.clear()
            }
        }
    }
}
```

### RegisterUserViewModel

```kotlin
@Composable
fun MiPantalla() {
    val viewModel: RegisterUserViewModel = koinViewModel()
    val state = viewModel.state
    
    // Llamar al registro
    viewModel.registerUser(
        names = "Juan",
        paternalSurname = "Pérez",
        maternalSurname = "García",
        documentType = "DNI",
        documentNumber = "12345678",
        phone = "999888777",
        email = "juan@example.com",
        password = "password123"
    )
    
    // Manejar estados
    LaunchedEffect(state.success, state.error) {
        when {
            state.success != null -> {
                // Registro exitoso
                viewModel.clear()
            }
            state.error != null -> {
                // Mostrar error
                viewModel.clear()
            }
        }
    }
}
```

## Almacenamiento Local

El sistema utiliza DataStore para almacenar:
- **Token JWT**: Se almacena automáticamente después del login/registro
- **Datos del usuario**: Se persisten localmente

### Acceso al StoreManager

```kotlin
val storeManager: StoreManager = get() // Inyectado con Koin

// Guardar valor
storeManager.saveValue(key, value)

// Obtener valor
val value = storeManager.getValue<MiTipo>(key)

// Eliminar valor
storeManager.removeValue(key)
```

## Manejo de Errores

El sistema maneja diferentes tipos de errores:

1. **Errores de red**: "Compruebe su conexión a internet"
2. **Errores del servidor**: Mensaje del API
3. **Errores de validación**: Mensajes específicos del API

Todos los errores se manejan mediante la clase `Result`:

```kotlin
when (response) {
    is Result.Success -> {
        // Manejar éxito
        val user = response.data
    }
    is Result.Error -> {
        // Manejar error
        val message = response.message
    }
}
```

## Seguridad

- **Tokens JWT**: Se almacenan de forma segura en DataStore
- **Interceptor de autenticación**: Añade automáticamente el token a las peticiones
- **HTTPS**: Asegúrate de usar HTTPS en producción

## Testing del API

### Usando el emulador/simulador

1. **Inicia el API** en tu máquina local:
```bash
cd /Users/jota/Documents/Proyectos/Peluhome/PeluHomeApi
npm start
```

2. **Verifica la conexión**:
- Android Emulator: `http://10.0.2.2:3000/api/`
- iOS Simulator: `http://localhost:3000/api/`

3. **Prueba el registro/login** desde la app

### Depuración

Los ViewModels incluyen logs de debug:
```kotlin
println("DEBUG: Register button clicked")
println("DEBUG: Validation passed, calling API")
```

Revisa Logcat (Android) o Console (iOS) para ver los mensajes.

## Próximos Pasos

Para extender la funcionalidad:

1. **Agregar más endpoints**:
   - Crear nuevos repositorios en `domain/repository/`
   - Implementar en `data/repository/`
   - Añadir modelos en `data/model/` y `domain/model/`
   - Registrar en `di/modules.kt`

2. **Ejemplo - Servicio de Bookings**:

```kotlin
// domain/repository/BookingRepository.kt
interface BookingRepository {
    suspend fun getBookings(): Result<List<Booking>>
    suspend fun createBooking(booking: BookingRequest): Result<Booking>
}

// data/repository/BookingRepositoryImp.kt
class BookingRepositoryImp(
    val httpClient: HttpClient
) : BookingRepository {
    // Implementación
}

// di/modules.kt
val dataModule = module {
    // ... existing code
    factory<BookingRepository> { BookingRepositoryImp(get()) }
}
```

## Troubleshooting

### Error: "Unable to resolve host"
- Verifica que el API está corriendo
- Verifica la URL en `Constants.kt`
- En Android Emulator usa `10.0.2.2` en lugar de `localhost`

### Error: "No se pudo conectar a la base de datos"
- Verifica que tu API backend está conectado a la base de datos
- Revisa las credenciales en el archivo `.env` del API

### El token no se envía en las peticiones
- Verifica que `installAuthHeaderInterceptor` está configurado en `modules.kt`
- Verifica que el token se guardó correctamente después del login

## Contacto

Para más información sobre la implementación, consulta los archivos de código fuente o contacta al equipo de desarrollo.




