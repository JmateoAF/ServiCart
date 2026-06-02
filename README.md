# Sistema de Pago de Servicios Básicos con Carrito de Compras

**Universidad de Cuenca**

**Materia:** Programación III

**Proyecto:** Software de pago de servicios básicos

**Lenguaje:** Java

**Instructor:** Ing. René Estrella

**Fecha de Entrega:** 3 de julio de 2026

**Repositorio GitHub:** https://github.com/JmateoAF/ServiCart

---

## Integrantes
- Jostin Aucancela
- Maritza Quishpi

---

## Descripción del Proyecto
Este es un proyecto de **programación orientada a objetos** bajo una **arquitectura multicapas** 
(Presentación, Lógica y Datos) que simula una plataforma de pagos. Los usuarios pueden acumular sus 
planillas de servicios básicos (Luz, Agua, Internet, etc.) en un **carrito de compras** para procesar el 
pago conjunto.

El sistema cuenta con dos mecanismos de persistencia en la capa de datos para cumplir con los requerimientos 
de diseño:

**Archivos Binarios:** Persistencia mediante serialización nativa de objetos Java.

**Base de Datos Relacional:** Persistencia local optimizada utilizando **SQLite**.

---

## Requisitos del Entorno (IntelliJ IDEA Ultimate)
Para que el proyecto funcione de manera idéntica en **Linux** y **Windows**, se recomienda utilizar los siguientes componentes en IntelliJ:

### Versión de Java
- **JDK 26** (Oracle OpenJDK o equivalente).

### Plugins Recomendados para el Equipo
Para trabajar cómodos con la base de datos y la interfaz sin salir de la IDE, vayan a *Settings -> Plugins -> Marketplace* e instalen:

- **Database Navigator** (O usar la pestaña *Database* nativa de IntelliJ Ultimate) - Para ver las tablas de SQLite directamente en la IDE.

- **JavaFX Runtime** - Para asegurar el soporte correcto de las vistas FXML.

---

## Posibles nombres para la aplicación
**ServiCart** - Mezcla "Servicios" y "Carrito". Es directo y comercial

**UniPay** - Corto, fácil de recordar y hace alusión a "Unificar" todos los pagos en un solo carrito.
