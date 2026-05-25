# Ospedale — Sistema de Gestión Hospitalaria

## Integrantes
- **[NOMBRE COMPLETO DEL ESTUDIANTE]** — NRC: [NÚMERO DE NRC]

## Descripción
Sistema de gestión hospitalaria desarrollado en Java con Swing.  
Permite gestionar pacientes, médicos, citas y hospitalizaciones.

## Tecnologías
- Java 8+, Swing (GUI), FlatLaf, JSON, NetBeans / Apache Ant

## Cómo ejecutar

### Desde NetBeans
1. File → Open Project → seleccionar carpeta `OspedaleNewVersion-main`
2. Click derecho → Run (F6)

### Línea de comandos
```bash
# Compilar
javac -cp "lib/flatlaf-demo-3.6.jar;lib/json-20250107.jar" -sourcepath src -d build/classes src/core/models/*.java src/core/controllers/*.java src/core/views/*.java src/main/Main.java

# Ejecutar
java -cp "lib/flatlaf-demo-3.6.jar;lib/json-20250107.jar;build/classes" core.views.LoginView
```

## Credenciales de prueba

| Rol | Usuario | Contraseña |
|-----|---------|-----------|
| Admin | admin_root | Admin@1234 |
| Paciente | jgarcia90 | Pass@1234 |
| Doctor | dr_aguirre | Doc@1234 |

## Flujo principal
1. Login → Admin registra doctores, navega vistas
2. Paciente solicita cita/hospitalización
3. Doctor acepta, completa y prescribe medicamentos
4. Logout disponible en todas las vistas

## Arquitectura MVC
```
src/core/models/      → Modelos (User, Patient, Doctor, Appointment, DataStore...)
src/core/views/       → Vistas (LoginView, PatientView, DoctorView, AdminView)
src/core/controllers/ → Componentes UI (PanelRound)
```
