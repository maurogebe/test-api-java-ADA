# API Template

## Descripción

Este es un proyecto de API creado como plantilla para demostrar las capacidades de Spring Boot con varias tecnologías y herramientas. La API está diseñada para ser extensible y adaptable a diferentes necesidades.

## Tecnologías Usadas

**Java:** Lenguaje principal para el desarrollo de la lógica de negocio. :coffee:

**Spring Boot:** Framework utilizado para crear servicios web RESTful. :herb:

**Spring Boot Starter Data JPA:** Para la integración con JPA para el manejo de datos. :file_cabinet:

**Spring Boot Starter Web:** Para el desarrollo de aplicaciones web. :globe_with_meridians:

**Spring Boot Starter Security:** Para implementar seguridad en la aplicación. :closed_lock_with_key:

**Spring Boot Starter OAuth2 Resource Server:** Para la implementación de autenticación y autorización con OAuth2.

**Base de Datos:** :card_file_box:

**MySQL:** Sistema de gestión de base de datos utilizado para almacenar la información de la aplicación. :file_cabinet:

**MySQL Connector:** Conector JDBC para MySQL.

**JWT (JSON Web Tokens):**

- `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: Bibliotecas para la generación y validación de tokens JWT.

**Correo Electrónico:** :email:

**Mailjet Client:** Para el envío de correos electrónicos a través de Mailjet.

**Generación de Documentos:**

- **iText7**: Para la generación de PDFs. :page_facing_up:
- **Flying Saucer y OpenPDF:** Herramientas para renderizar y generar documentos PDF. :page_facing_up:
- **Apache PDFBox:** Para la manipulación de documentos PDF.

**Mapeo de Objetos:**

- **MapStruct:** Para la conversión entre objetos Java.
- **Reconocimiento Óptico de Caracteres (OCR):** :mag:
   - **Tess4J:** Biblioteca para implementar OCR usando Tesseract con IA. :robot:

**Thymeleaf:** Motor de plantillas para generar vistas dinámicas en el servidor.

**Lombok:** Biblioteca para reducir el código boilerplate en Java. :wrench:

**Testing:**

- **JUnit:** Framework de pruebas unitarias. :microscope:
- **Spring Boot Starter Test y Spring Security Test:** Para pruebas en la aplicación. :test_tube:
- 
## Arquitectura Hexagonal

Este proyecto utiliza **Arquitectura Hexagonal** (también conocida como Arquitectura de Puertos y Adaptadores) para organizar el código y facilitar la extensión y mantenimiento. La arquitectura hexagonal ayuda a mantener el núcleo de la aplicación desacoplado de las interfaces externas (como bases de datos, servicios web, etc.).

En esta arquitectura, el núcleo de la aplicación está en el centro, y las interfaces externas se comunican con él a través de puertos y adaptadores. Los puertos definen las operaciones que el núcleo puede realizar, mientras que los adaptadores implementan las interfaces para interactuar con el núcleo.

Para más información sobre la Arquitectura Hexagonal, puedes consultar la [documentación oficial](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)) y [artículos adicionales](https://www.dddcommunity.org/ddd/hexagonal-architecture/).

## Instalación

Para clonar este repositorio y ejecutar la aplicación localmente, sigue estos pasos:

1. Clona el repositorio:
    ```bash
    git clone https://github.com/maurogebe/template-api-spring-boot.git
    ```

2. Navega al directorio del proyecto:
    ```bash
    cd template-api-spring-boot
    ```

3. Construye el proyecto usando Gradle:
    ```bash
    ./gradlew build
    ```

4. Ejecuta la aplicación:
    ```bash
    ./gradlew bootRun
    ```

## Contacto


<img src="https://i.imgur.com/sXKJZmn.jpg" alt="Avatar" style="border-radius: 50%; width: 100px; height: 120px;" />

- **Nombre:** Fabian Mauricio Guerra Bedoya
- **LinkedIn:** [Fabian Mauricio Guerra Bedoya](https://www.linkedin.com/in/fabian-mauricio-guerra-bedoya)
- **Correo Electrónico:** maurogebe.96@gmail.com

## Licencia

Este proyecto está licenciado bajo la [MIT License](LICENSE).

