# 🔐 SecureDocs

Sistema web de gestión segura de expedientes y documentos empresariales, desarrollado para **TechCorp S.A.**, que implementa mecanismos de seguridad basados en **JWT, RBAC y ABAC**.

El sistema permite gestionar documentos y usuarios de acuerdo con sus roles, permisos y atributos de seguridad, además de registrar las operaciones realizadas mediante un módulo de auditoría.

---

## 📌 Descripción del proyecto

**SecureDocs** es una aplicación web orientada a la gestión segura de documentos empresariales.

El sistema controla el acceso a los recursos mediante dos mecanismos principales:

* **RBAC (Role-Based Access Control):** controla las operaciones permitidas según el rol del usuario.
* **ABAC (Attribute-Based Access Control):** valida atributos del usuario, documento y entorno antes de permitir determinadas operaciones.

Además, el sistema utiliza **JWT (JSON Web Token)** para autenticar a los usuarios y mantener una sesión segura.

---

## 🎯 Objetivos

### Objetivo general

Desarrollar un sistema de gestión documental que permita controlar de forma segura el acceso a documentos y funcionalidades mediante autenticación, autorización basada en roles y políticas basadas en atributos.

### Objetivos específicos

* Implementar autenticación mediante JWT.
* Implementar autorización basada en roles mediante RBAC.
* Implementar reglas de acceso mediante ABAC.
* Gestionar usuarios y documentos.
* Permitir la aprobación de documentos según los permisos correspondientes.
* Registrar las operaciones realizadas en el sistema.
* Gestionar roles y permisos.
* Proporcionar una interfaz web para interactuar con el sistema.

---

# 🛠️ Tecnologías utilizadas

## Backend

* Java 21
* Spring Boot 3.5.16
* Spring Security
* Spring Data JPA
* Maven
* JWT
* MySQL

## Frontend

* React
* Vite
* JavaScript
* Axios
* Bootstrap
* Lucide React

## Base de datos

* MySQL
* Base de datos: `securedocs_db`

---

# ⚙️ Requisitos previos

Antes de ejecutar el proyecto se debe contar con:

* Java JDK 21
* Maven
* MySQL Server
* Node.js y npm
* Git
* IntelliJ IDEA, VS Code u otro IDE compatible

Para verificar las instalaciones:

```powershell
java -version
```

```powershell
mvn -version
```

```powershell
node -v
```

```powershell
npm -v
```

```powershell
git --version
```

---

# 🗄️ Configuración de la base de datos

## 1. Crear la base de datos

Ingresar a MySQL y ejecutar:

```sql
CREATE DATABASE securedocs_db;
```

Luego verificar:

```sql
SHOW DATABASES;
```

La base de datos utilizada por el proyecto es:

```text
securedocs_db
```

---

# 🔧 Configuración del Backend

El backend está desarrollado con Spring Boot y se ejecuta en el puerto:

```text
8081
```

Por lo tanto, la URL base del backend es:

```text
http://localhost:8081
```

La configuración de conexión a MySQL se encuentra en:

```text
backend/secure-docs/src/main/resources/application.properties
```

La configuración debe contener los datos correspondientes al servidor MySQL local.

Ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/securedocs_db
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

server.port=8081
```

> **Nota:** reemplazar `TU_PASSWORD` por la contraseña configurada en MySQL.

---

# ▶️ Levantamiento del Backend

Ingresar a la carpeta del backend:

```powershell
cd C:\Solucion_en_Nube\sem06\SecureDocs\backend\secure-docs
```

Ejecutar el proyecto mediante Maven:

```powershell
mvn spring-boot:run
```

También puede ejecutarse desde IntelliJ IDEA mediante la clase principal:

```text
SecureDocsApplication
```

Si el inicio es correcto, el backend quedará disponible en:

```text
http://localhost:8081
```

Durante el inicio, el sistema carga los datos iniciales configurados por el proyecto.

---

# 🌐 Levantamiento del Frontend

Abrir una nueva terminal y dirigirse a:

```powershell
cd C:\Solucion_en_Nube\sem06\SecureDocs\frontend
```

Instalar las dependencias:

```powershell
npm install
```

Ejecutar el servidor de desarrollo:

```powershell
npm run dev
```

El frontend estará disponible normalmente en:

```text
http://localhost:5173
```

---

# 🚀 Ejecución completa

Para ejecutar SecureDocs se deben iniciar ambos servicios.

### Terminal 1 — Backend

```powershell
cd C:\Solucion_en_Nube\sem06\SecureDocs\backend\secure-docs
mvn spring-boot:run
```

### Terminal 2 — Frontend

```powershell
cd C:\Solucion_en_Nube\sem06\SecureDocs\frontend
npm run dev
```

Luego ingresar desde el navegador a:

```text
http://localhost:5173
```

---

# 🔐 Autenticación

SecureDocs utiliza **JWT (JSON Web Token)** para la autenticación.

El usuario inicia sesión mediante:

```http
POST /api/auth/login
```

Después de autenticarse correctamente, el backend genera un token JWT.

El frontend almacena el token y lo utiliza para realizar las solicitudes protegidas.

Las solicitudes autenticadas utilizan:

```http
Authorization: Bearer <TOKEN>
```

---

# 👥 Roles del sistema

El sistema contempla los siguientes roles:

| Rol           | Descripción                                                         |
| ------------- | ------------------------------------------------------------------- |
| ADMINISTRADOR | Gestión general del sistema, usuarios, roles, permisos y documentos |
| GERENTE       | Gestión y aprobación de documentos según sus permisos               |
| SUPERVISOR    | Gestión de documentos según sus permisos                            |
| EMPLEADO      | Operaciones básicas sobre documentos según sus permisos             |
| AUDITOR       | Consulta de información y auditoría                                 |
| INVITADO      | Acceso limitado al sistema                                          |

---

# 🛡️ RBAC

**RBAC (Role-Based Access Control)** determina qué acciones puede realizar un usuario según su rol.

Los permisos utilizados por SecureDocs incluyen:

```text
CREAR_DOCUMENTO
CONSULTAR_DOCUMENTO
MODIFICAR_DOCUMENTO
ELIMINAR_DOCUMENTO
APROBAR_DOCUMENTO
VER_AUDITORIA
GESTIONAR_USUARIOS
ASIGNAR_ROLES
```

La autorización se realiza en el backend.

El frontend únicamente adapta la interfaz según los permisos disponibles, pero la validación de seguridad se mantiene en el servidor.

---

# 🔎 ABAC

**ABAC (Attribute-Based Access Control)** utiliza atributos para determinar si una operación puede realizarse.

Entre los atributos considerados por el sistema se encuentran:

### Usuario

```text
id
nombre
correo
rol
departamento
nivelSeguridad
pais
tipoContrato
estado
```

### Documento

```text
id
titulo
descripcion
propietario
departamento
nivelConfidencialidad
estado
pais
fechaCreacion
```

### Entorno

```text
hora
fecha
direccion_ip
ubicacion
dispositivo
```

Estas condiciones permiten aplicar políticas adicionales además de los permisos asociados al rol.

Por ejemplo, una operación sobre un documento puede depender de la relación entre:

```text
Nivel de seguridad del usuario
        ↓
Nivel de confidencialidad del documento
```

o:

```text
Usuario
   ↓
Departamento
   ↓
Documento
```

De esta manera, tener un permiso RBAC no significa automáticamente que todas las operaciones sobre todos los documentos estén permitidas.

---

# 📄 Gestión de documentos

El sistema permite realizar operaciones sobre documentos mediante:

* Crear documentos.
* Consultar documentos.
* Consultar el detalle de un documento.
* Modificar documentos.
* Eliminar documentos.
* Aprobar documentos.

Los documentos contienen información como:

```text
Título
Descripción
Propietario
Departamento
Nivel de confidencialidad
Estado
País
Fecha de creación
```

---

# 👤 Gestión de usuarios

Los usuarios pueden ser gestionados desde el módulo correspondiente.

La información incluye atributos como:

```text
Nombre
Correo
Rol
Departamento
Nivel de seguridad
País
Tipo de contrato
Estado
```

El acceso a la gestión de usuarios depende de los permisos asignados al usuario autenticado.

---

# 📊 Auditoría

SecureDocs incorpora un módulo de auditoría para registrar las operaciones realizadas en el sistema.

La auditoría permite realizar seguimiento de acciones como:

* Inicio de sesión.
* Creación de documentos.
* Modificación de documentos.
* Eliminación de documentos.
* Aprobación de documentos.
* Operaciones relacionadas con usuarios y permisos.

Endpoint:

```http
GET /api/auditorias
```

El objetivo es mantener trazabilidad de las operaciones realizadas dentro del sistema.

---

# 🔑 Roles y permisos

El sistema cuenta con un módulo de administración de RBAC.

Endpoints principales:

```http
GET /api/rbac/roles
```

```http
GET /api/rbac/permisos
```

Para asignar un permiso a un rol:

```http
POST /api/rbac/roles/{rolId}/permisos/{permisoId}
```

Para retirar un permiso:

```http
DELETE /api/rbac/roles/{rolId}/permisos/{permisoId}
```

---

# 🔗 Endpoints principales

## Autenticación

```http
POST /api/auth/login
```

## Usuarios

```http
GET /api/usuarios
```

```http
POST /api/usuarios
```

## Documentos

```http
GET /api/documentos
```

```http
GET /api/documentos/{id}
```

```http
POST /api/documentos
```

```http
PUT /api/documentos/{id}
```

```http
PUT /api/documentos/{id}/aprobar
```

```http
DELETE /api/documentos/{id}
```

## Auditoría

```http
GET /api/auditorias
```

## RBAC

```http
GET /api/rbac/roles
```

```http
GET /api/rbac/permisos
```

```http
POST /api/rbac/roles/{rolId}/permisos/{permisoId}
```

```http
DELETE /api/rbac/roles/{rolId}/permisos/{permisoId}
```

---

# 🧪 Pruebas del sistema

Las pruebas pueden realizarse mediante:

* Postman.
* Interfaz web de SecureDocs.
* Pruebas de autenticación.
* Pruebas de autorización RBAC.
* Pruebas de políticas ABAC.
* Pruebas CRUD de documentos.
* Pruebas de gestión de usuarios.
* Pruebas de auditoría.

### Ejemplo de flujo de autenticación

```text
1. Usuario ingresa correo y contraseña.
              ↓
2. Backend valida las credenciales.
              ↓
3. Backend genera JWT.
              ↓
4. Frontend almacena el token.
              ↓
5. El token se envía en las solicitudes protegidas.
              ↓
6. Spring Security valida el token.
              ↓
7. Se verifica el rol y los permisos.
              ↓
8. Se aplican las reglas ABAC.
              ↓
9. Se permite o rechaza la operación.
```

---

# 🚦 Códigos HTTP utilizados

| Código | Significado                                   |
| ------ | --------------------------------------------- |
| 200    | Solicitud procesada correctamente             |
| 201    | Recurso creado correctamente                  |
| 400    | Solicitud incorrecta                          |
| 401    | No autenticado o token inválido               |
| 403    | Autenticado, pero sin autorización suficiente |
| 404    | Recurso no encontrado                         |
| 500    | Error interno del servidor                    |

Una diferencia importante:

### 401 Unauthorized

El usuario no está correctamente autenticado.

Por ejemplo:

```text
Token ausente
Token inválido
Token expirado
```

### 403 Forbidden

El usuario está autenticado, pero no tiene autorización para realizar la operación.

Por ejemplo:

```text
Usuario autenticado
        ↓
Rol válido
        ↓
Permiso insuficiente
        ↓
403 Forbidden
```

---

# 🎨 Interfaz web

El frontend cuenta con los siguientes módulos principales:

* Login.
* Dashboard.
* Documentos.
* Usuarios.
* Auditoría.
* Roles y permisos.

La interfaz adapta las opciones disponibles según el usuario autenticado y sus permisos.

---

# 🔄 Flujo general del sistema

```text
              ┌──────────────┐
              │    LOGIN     │
              └──────┬───────┘
                     │
                     ▼
              ┌──────────────┐
              │     JWT      │
              └──────┬───────┘
                     │
                     ▼
              ┌──────────────┐
              │    RBAC      │
              │ Rol/Permisos │
              └──────┬───────┘
                     │
                     ▼
              ┌──────────────┐
              │     ABAC     │
              │  Atributos   │
              └──────┬───────┘
                     │
              ┌──────┴───────┐
              │              │
              ▼              ▼
          PERMITIDO       DENEGADO
              │              │
              ▼              ▼
          OPERACIÓN       RESPUESTA
              │            403/401
              ▼
          AUDITORÍA
```

---

# 📋 Flujo recomendado para demostración

Para realizar una demostración del sistema se recomienda seguir el siguiente flujo:

1. Iniciar sesión como administrador.
2. Mostrar el Dashboard.
3. Consultar los documentos.
4. Crear un documento.
5. Modificar el documento.
6. Aprobar el documento.
7. Cerrar sesión.
8. Iniciar sesión con otro rol.
9. Mostrar las opciones disponibles según sus permisos.
10. Intentar realizar una operación restringida.
11. Mostrar el bloqueo producido por las reglas de seguridad.
12. Ingresar al módulo de auditoría.
13. Mostrar las operaciones registradas.
14. Ingresar al módulo RBAC.
15. Mostrar los roles y permisos existentes.

Este flujo permite demostrar de manera integrada:

```text
JWT + RBAC + ABAC + CRUD + Auditoría
```

---

# 🧹 Validación del proyecto

Antes de realizar una entrega se recomienda comprobar:

### Backend

```powershell
mvn clean
```

```powershell
mvn test
```

### Frontend

```powershell
npm run lint
```

```powershell
npm run build
```

Si las validaciones terminan correctamente, se puede ejecutar nuevamente el sistema y realizar las pruebas funcionales.

---

# 🔒 Consideraciones de seguridad

No se deben almacenar contraseñas reales ni secretos dentro del repositorio.

El archivo:

```text
application.properties
```

debe configurarse con los datos correspondientes al entorno local.

En un entorno de producción se recomienda utilizar variables de entorno para credenciales, claves secretas y otros datos sensibles.

---

# 👩‍💻 Autora

* Adriana Chinchayhuara

---

# 📌 Estado del proyecto

**Estado:** En desarrollo / implementación académica.

El sistema cuenta con:

* [x] Autenticación JWT
* [x] Spring Security
* [x] RBAC
* [x] ABAC
* [x] Gestión de documentos
* [x] Gestión de usuarios
* [x] Auditoría
* [x] Gestión de roles y permisos
* [x] Frontend React
* [x] Integración frontend/backend
* [x] Interfaz web
* [x] Base de datos MySQL
