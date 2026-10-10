![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# 🧠 Semana 9 | Evaluación Final Transversal

# 🚚 Sistema de Gestión de Biblioteca Escolar

## 👤 Autor del proyecto

- **Nombre completo:** Fabrizio Fernandini
- **Ramo:** Desarrollo Orientado a Objetos II
- **Sección:** 005A
- **Carrera:** Analista Programador Computacional
- **Sede:** Campus Virtual

---

## 📝 Descripción del Sistema

BibliotecaEscolarEFT es una aplicación de escritorio desarrollada en Java con 
interfaz gráfica Java Swing (JFrame), orientada a la modernización y control 
eficiente de la circulación de libros en una biblioteca escolar.

El software implementa una arquitectura sólida basada en el patrón MVC 
(Modelo - Vista - Controlador), acceso a datos desacoplado mediante el 
patrón DAO (Data Access Object) con interfaces e implementaciones JDBC, 
y el patrón Singleton para la gestión centralizada e ininterrumpida de 
la conexión a la base de datos MySQL.   Además, integra un módulo de 
procesamiento concurrente mediante hilos de ejecución (Runnable y BlockingQueue) 
para gestionar las colas de solicitudes de préstamos en segundo plano, 
previniendo el congelamiento de la interfaz de usuario y garantizando el control 
estricto del stock.

---

## 🧱 Estructura de paquetes y clases

```plaintext
BibliotecaEscolarEFT/
├── 📁 .idea/
├── 📁 .mvn/
└── 📁 src/
    └── 📁 main/
        └── 📁 java/
            └── 📁 cl/
                └── 📁 bibliotecaescolar/
                    ├── 📁 app/
                    │   └── 📄 Main.java
                    ├── 📁 config/
                    │   └── 📄 ConexionDB.java
                    ├── 📁 controller/
                    │   ├── 📄 CategoriaController.java
                    │   ├── 📄 EstudianteController.java
                    │   ├── 📄 LibroController.java
                    │   ├── 📄 PrestamoController.java
                    │   └── 📄 UsuarioController.java
                    ├── 📁 dao/
                    │   ├── 📁 impl/
                    │   │   ├── 📄 CategoriaDAOImpl.java
                    │   │   ├── 📄 EstudianteDAOImpl.java
                    │   │   ├── 📄 LibroDAOImpl.java
                    │   │   ├── 📄 PrestamoDAOImpl.java
                    │   │   └── 📄 UsuarioDAOImpl.java
                    │   ├── 📄 CategoriaDAO.java
                    │   ├── 📄 EstudianteDAO.java
                    │   ├── 📄 LibroDAO.java
                    │   ├── 📄 PrestamoDAO.java
                    │   └── 📄 UsuarioDAO.java
                    ├── 📁 model/
                    │   ├── 📄 Bibliotecario.java
                    │   ├── 📄 Categoria.java
                    │   ├── 📄 CategoriaLibro.java
                    │   ├── 📄 Estudiante.java
                    │   ├── 📄 Libro.java
                    │   ├── 📄 Persona.java
                    │   ├── 📄 Prestamo.java
                    │   ├── 📄 ProcesadorPrestamos.java
                    │   ├── 📄 RolUsuario.java
                    │   └── 📄 Usuario.java
                    ├── 📁 util/
                    │   ├── 📄 UtilidadesGUI.java
                    │   ├── 📄 ValidadorCorreo.java
                    │   ├── 📄 ValidadorFormulario.java
                    │   ├── 📄 ValidadorRut.java
                    │   └── 📄 ValidarISBN.java
                    └── 📁 view/
                        ├── 📄 GestionEstudiantes.java
                        ├── 📄 GestionEstudiantes.form
                        ├── 📄 GestionLibros.java
                        ├── 📄 GestionLibros.form
                        ├── 📄 GestionPrestamos.java
                        ├── 📄 GestionPrestamos.form
                        ├── 📄 VentanaLogin.java
                        ├── 📄 VentanaLogin.form
                        ├── 📄 VentanaPrincipal.java
                        ├── 📄 VentanaPrincipal.form
                        ├── 📄 VentanaReportes.java
                        └── 📄 VentanaReportes.form
├── pom.xml
├── PRY2203_EFT_S9_Script_crea_tablas_biblioteca.sql
├── PRY2203_EFT_S9_Script_poblado_tablas_biblioteca.sql
└── README.md

```

---

## 🚀 Funcionalidades Principales del Sistema

El sistema administra la persistencia relacional en MySQL mediante DAOs e
interfaces Swing orientadas al rol del usuario autenticado:

### 🔑 Autenticación y Control de Accesos por Rol
El sistema restringe las funcionalidades disponibles mediante un control de acceso basado en el rol del usuario:
* **Rol Bibliotecario:** Acceso completo a la gestión del catálogo de libros, estudiantes, control de préstamos/devoluciones y visualización de reportes analíticos del sistema.
* **Rol Estudiante:** Vista adaptada y restringida exclusivamente para la solicitud de préstamos, registro de devoluciones propias y consulta de su historial individual.

### 📚 Gestión de Libros (CRUD)
Módulo completo para el mantenimiento del catálogo que incluye:
* Registro, edición, eliminación y listado de libros vinculados a categorías.
* **Validación técnica** rigurosa de formatos **ISBN-10** e **ISBN-13**.
* Actualización en tiempo real del stock disponible tras cada operación.

### 🎓 Gestión de Estudiantes (CRUD)
Módulo destinado al alta y mantención de los datos personales de los alumnos:
* Al registrar un nuevo estudiante, el sistema **sincroniza automáticamente** su perfil de acceso en la tabla de usuarios.
* Asignación automática del rol `ESTUDIANTE` y una clave por defecto para su primer ingreso.

### 🔄 Préstamos, Devoluciones y Concurrencia
Automatización del flujo principal del inventario con soporte multihilo:
* **Validación de Stock:** Permite solicitar libros verificando la disponibilidad física antes de confirmar el préstamo.
* **Cálculo de Atrasos:** Registro de devoluciones con cálculo automático de días de retraso respecto a la fecha límite establecida.
* **Gestión de Concurrencia:** El procesamiento de las solicitudes de préstamos se delega a un hilo secundario (`ProcesadorPrestamos`), evitando que la interfaz gráfica (UI) se bloquee o se congele durante la transacción.

### 📊 Módulo de Reportes
Generación de informes analíticos clave para la toma de decisiones:
* Libros más solicitados por los usuarios.
* Historial detallado de préstamos por estudiante.
* Listado de libros actualmente en préstamo (clasificados entre vigentes y atrasados).

### 🚪 Navegación y Sesión Segura
* Centralización del ciclo de vida de las ventanas del sistema.
* Confirmación previa obligatoria antes de cerrar sesión o salir de la aplicación de acuerdo al rol activo.


## 🛠️ Prerrequisitos

* Tener instalado **Java JDK 17, 21 o superior (Desarrollado y testeado en JDK 21)**.
* Un IDE compatible (se recomienda IntelliJ IDEA).
* **Gestor de proyectos:** Apache Maven (incluido por defecto en IntelliJ IDEA).
* Base de Datos: MySQL Server 8.0 o superior instalado y en ejecución.

### 🗄️ Configuración de la Base de Datos

Abrir su cliente de MySQL (MySQL Workbench, DBeaver o consola) y ejecutar el
script de creación de tablas ubicado en la raíz del proyecto (`PRY2203_EFT_S9_Script_crea_tablas_biblioteca.sql`):

```SQL
CREATE DATABASE IF NOT EXISTS biblioteca;
USE biblioteca;

CREATE TABLE usuarios (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(100),
                          rut VARCHAR(12) UNIQUE,
                          correo VARCHAR(100),
                          contraseña VARCHAR(100),
                          rol ENUM('bibliotecario', 'estudiante') NOT NULL
);

CREATE TABLE estudiantes (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             nombre VARCHAR(100),
                             rut VARCHAR(12) UNIQUE,
                             curso VARCHAR(20),
                             correo VARCHAR(100)
);

CREATE TABLE categorias (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            nombre VARCHAR(100)
);

CREATE TABLE libros (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        titulo VARCHAR(200),
                        autor VARCHAR(100),
                        isbn VARCHAR(20) UNIQUE,
                        editorial VARCHAR(100),
                        stock INT,
                        id_categoria INT,
                        FOREIGN KEY (id_categoria) REFERENCES categorias(id)
);

CREATE TABLE prestamos (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           id_estudiante INT,
                           id_libro INT,
                           fecha_prestamo DATE,
                           fecha_devolucion DATE,
                           devuelto BOOLEAN DEFAULT FALSE,
                           FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id),
                           FOREIGN KEY (id_libro) REFERENCES libros(id)
);

```
### Poblamiento

Inserta el conjunto de usuarios de prueba (bibliotecario y estudiantes), categorías, libros iniciales y préstamos. (`PRY2203_EFT_S9_Script_poblado_tablas_biblioteca.sql`):

```SQL
USE biblioteca;

-- Insertar usuarios
INSERT INTO usuarios (nombre, rut, correo, contraseña, rol) VALUES
('Antonia Pérez', '12345678-9', 'antonia@correo.cl', 'clave123', 'bibliotecario'),
('Carlos Ruiz', '98765432-1', 'carlos@correo.cl', 'clave123', 'estudiante'),
('María Torres', '11222333-4', 'maria@correo.cl', 'clave123', 'estudiante'),
('Ignacio Silva', '22334455-6', 'ignacio@correo.cl', 'clave123', 'estudiante'),
('Laura Méndez', '33445566-7', 'laura@correo.cl', 'clave123', 'estudiante'),
('Javier Soto', '44556677-8', 'javier@correo.cl', 'clave123', 'estudiante'),
('Fernanda Ríos', '55667788-9', 'fer@correo.cl', 'clave123', 'estudiante'),
('Pedro Lagos', '66778899-0', 'pedro@correo.cl', 'clave123', 'estudiante'),
('Valentina Jara', '77889900-1', 'valen@correo.cl', 'clave123', 'estudiante'),
('Tomás Vidal', '88990011-2', 'tomas@correo.cl', 'clave123', 'estudiante');

-- Insertar estudiantes
INSERT INTO estudiantes (nombre, rut, curso, correo) VALUES
('Carlos Ruiz', '98765432-1', '3ro Medio A', 'carlos@correo.cl'),
('María Torres', '11222333-4', '4to Medio B', 'maria@correo.cl'),
('Luis Gómez', '19283746-5', '2do Medio C', 'luis@correo.cl'),
('Ignacio Silva', '22334455-6', '1ro Medio A', 'ignacio@correo.cl'),
('Laura Méndez', '33445566-7', '3ro Medio B', 'laura@correo.cl'),
('Javier Soto', '44556677-8', '4to Medio A', 'javier@correo.cl'),
('Fernanda Ríos', '55667788-9', '2do Medio B', 'fer@correo.cl'),
('Pedro Lagos', '66778899-0', '1ro Medio C', 'pedro@correo.cl'),
('Valentina Jara', '77889900-1', '3ro Medio C', 'valen@correo.cl'),
('Tomás Vidal', '88990011-2', '4to Medio C', 'tomas@correo.cl');

-- Insertar categorías
INSERT INTO categorias (nombre) VALUES
('Ciencia Ficción'),
('Historia'),
('Filosofía'),
('Literatura'),
('Tecnología');

-- Insertar libros
INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) VALUES
('Dune', 'Frank Herbert', '9780441013593', 'Ace Books', 5, 1),
('Fundación', 'Isaac Asimov', '9788497594256', 'Debolsillo', 3, 1),
('Breve Historia del Tiempo', 'Stephen Hawking', '9780553176988', 'Debate', 2, 2),
('El mundo de Sofía', 'Jostein Gaarder', '9788478884452', 'Siruela', 4, 3),
('1984', 'George Orwell', '9780451524935', 'Penguin Books', 6, 4),
('Crónica de una muerte anunciada', 'Gabriel García Márquez', '9780307387340', 'Sudamericana', 3, 4),
('Introducción a la Inteligencia Artificial', 'Stuart Russell', '9780136042594', 'Pearson', 2, 5),
('Fahrenheit 451', 'Ray Bradbury', '9781451673319', 'Simon & Schuster', 5, 1),
('Sapiens', 'Yuval Noah Harari', '9788499924211', 'Debate', 4, 2),
('Más allá del bien y del mal', 'Friedrich Nietzsche', '9788420688114', 'Alianza', 3, 3);

-- Insertar préstamos
INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES
(1, 1, '2025-06-15', '2025-06-22', FALSE),
(2, 2, '2025-06-10', '2025-06-17', TRUE),
(3, 3, '2025-06-20', '2025-06-27', FALSE),
(4, 4, '2025-06-22', '2025-06-29', FALSE),
(5, 5, '2025-06-18', '2025-06-25', TRUE),
(6, 6, '2025-06-12', '2025-06-19', FALSE),
(7, 7, '2025-06-05', '2025-06-12', TRUE),
(8, 8, '2025-06-21', '2025-06-28', FALSE),
(9, 9, '2025-06-16', '2025-06-23', TRUE),
(10, 10, '2025-06-19', '2025-06-26', FALSE);
```


### ⚙️ Configuración de Credenciales de Conexión

Antes de ejecutar la aplicación, se deben ajustar los parámetros de acceso a la
base de datos local:

```java
// Archivo: cl.bibliotecaescolar.config.ConexionDB

private static final String URL = "jdbc:mysql://127.0.0.1:3306/biblioteca"; // Reemplazar con la ruta de la BBDD
private static final String USER = "root";           // Reemplazar por usuario de MySQL (ej: root)
private static final String PASSWORD = "Duoc123";   // Reemplazar por contraseña local
```

## 📦 Ejecución del Proyecto

### 🚀 Opción A: Ejecución mediante el JAR ejecutable (Recomendado)

> **Requisito previo obligatorio:** Antes de ejecutar el archivo `.jar`, asegúrese de que el servidor MySQL esté activo y la base de datos `biblioteca` se encuentre creada e importada utilizando los scripts SQL provistos en la raíz del proyecto.

Una vez configurado el entorno de base de datos, puede ejecutar el sistema de dos formas:

1. **Modo Directo (Doble clic en Windows):**
   - Ve a la carpeta donde ubicaste el archivo `BibliotecaEscolarEFT.jar` (en la raíz del proyecto).
   - Haz **doble clic** sobre el archivo `.jar` para iniciar la interfaz gráfica directamente.
   - **Nota:** Para el arranque mediante doble clic, es un requisito que el sistema operativo tenga asociada la extensión `.jar` con el ejecutable `javaw.exe` de Java JDK 21+. Si la aplicación se cierra de inmediato al hacer doble clic, ejecútela por consola para visualizar posibles alertas de conexión a MySQL. El archivo JAR fue creado con las credenciales por defecto `Usuario: root, URL: localhost, Password: Duoc123`.

2. **Modo Consola / Terminal (Recomendado para depuración):**
   - Abre la consola de comandos (`cmd` o `PowerShell`) en la ruta del archivo y ejecuta:
     ```bash
     java -jar BibliotecaEscolarEFT.jar
     ```

> **Nota:** Recuerda que para el arranque con doble clic es requisito que el sistema operativo tenga vinculada la extensión `.jar` con el ejecutable `javaw.exe` de Java JDK 21+.


### 🚀 Opción B: Ejecución desde el IDE (IntelliJ IDEA)

1. **Clonar/Abrir el proyecto:** Asegúrese de estar en la rama correspondiente a la Semana actual.

2. **Cargar la Base de Datos:** Ejecute los dos scripts SQL descritos en la sección anterior.

3. **Abrir el proyecto en el IDE** (IntelliJ IDEA de preferencia).

4. Cargar dependencias de Maven: Al abrir el proyecto, el IDE descargará
   automáticamente la dependencia del conector MySQL (mysql-connector-j)
   definida en el archivo pom.xml. Si no lo hace de forma automática,
   ejecute Reload All Maven Projects (en IntelliJ a la derecha aparece una M, pulsar botón reload).

5. Navegue hasta la clase cl.bibliotecaescolar.app.Main.

6. Ejecute el método main.

7. Credenciales de prueba por defecto (cargadas en la base de datos descrita en el punto anterior):


> **Nota de Seguridad (Opción A y B):** Al iniciar, la aplicación valida la conexión a la base de datos de manera inmediata. Si el servidor MySQL no está activo o las credenciales no coinciden, la aplicación mostrará una alerta de error crítico y finalizará para prevenir inconsistencias en la sesión.

## 🔑 Credenciales de Prueba

```txt
Bibliotecario:
Email: antonia@correo.cl
Contraseña: clave123

Estudiante:
Email: carlos@correo.cl
Contraseña: clave123
```

---

## Repositorio

```bash
https://github.com/Capa8cl/poo2-bibliotecaescolar-eft.git
```

---
© Duoc UC | Escuela de Informática y Telecomunicaciones | Evaluación Final Transversal (EFT)