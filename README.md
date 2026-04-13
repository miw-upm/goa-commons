## [Máster en Ingeniería Web por la Universidad Politécnica de Madrid (miw-upm)](http://miw.etsisi.upm.es)

## Back-end con Tecnologías de Código Abierto (BETCA).

> Librería de utilidades compartidas entre los microservicios del proyecto GOA

### Estado del código
[![CI goa-commons](https://github.com/miw-upm/goa-commons/actions/workflows/ci.yml/badge.svg)](https://github.com/miw-upm/goa-commons/actions/workflows/ci.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=miw-upm-github_goa-commons&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=miw-upm-github_goa-commons)

### Tecnologías necesarias

`Java` `Maven` `GitHub` `GitHub Actions` `GitHub Packages`

### Configuración necesaria

Añadir en `~/.m2/settings.xml`:

\```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>miw-upm</username>
            <password>token</password>
        </server>
    </servers>
</settings>
\```


### Contenido

| Paquete | Clase | Descripción |
|---------|-------|-------------|
| `device` | `DeviceInfo` | Información del dispositivo del cliente |
| `device` | `DeviceInfoResolver` | Parser del User-Agent y resolución de IP |
| `mail` | `Email` | Modelo de email (to, subject, body) |
| `uuid` | `UUIDBase64` | Codificación/decodificación de UUID en Base64 |

### :gear: Instalación del proyecto

1. Clonar el repositorio en tu equipo, **mediante consola**:

```sh
> cd <folder path>
> git clone https://github.com/miw-upm/goa-commons
```

2. Importar el proyecto mediante **IntelliJ IDEA**
    * **Open**, y seleccionar la carpeta del proyecto.

### :gear: Uso como dependencia

Añadir en el `pom.xml` del microservicio:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/miw-upm/goa-commons</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>es.upm.miw</groupId>
        <artifactId>goa-commons</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

Y en `~/.m2/settings.xml`:

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>TU_USUARIO</username>
            <password>TU_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```
