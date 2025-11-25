# Optimización de SonarQube - Evitar Descargar Dependencias

## Problema

Cada vez que ejecutas `./sonar-scan.sh`, Maven descarga las mismas dependencias, lo que hace que el análisis sea muy lento (5-10 minutos).

## Solución Implementada

### ✅ Cambios Aplicados

1. **docker-compose.yml** - Agregado volumen persistente para cache de Maven:
   ```yaml
   volumes:
     - maven_cache:/.m2
   ```

2. **sonar-scan.sh** - Modificado para usar el volumen persistente:
   ```bash
   -Dmaven.repo.local=/.m2/repository
   ```

### 📋 Pasos para Aplicar

**IMPORTANTE**: Debes recrear el contenedor para que aplique el nuevo volumen.

```bash
# 1. Detener los contenedores actuales
docker-compose down

# 2. Iniciar con el nuevo volumen
docker-compose up -d

# 3. Esperar a que la aplicación esté lista
docker logs -f recetas_app
# (Ctrl+C cuando veas "Tomcat started")

# 4. Ejecutar análisis (primera vez será lento, las siguientes serán rápidas)
./sonar-scan.sh
```

### ⚡ Resultados Esperados

- **Primera ejecución**: 5-10 minutos (descarga dependencias)
- **Ejecuciones siguientes**: 1-2 minutos (usa cache)

---

## Alternativa: Ejecutar SonarQube desde tu Host (MÁS RÁPIDO)

Si quieres evitar completamente usar Docker para el análisis, puedes ejecutar Maven directamente desde tu máquina:

### Opción A: Usando Maven Local (Recomendado)

```bash
# Ejecuta desde tu terminal (NO desde Docker)
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=sumativa2 \
  -Dsonar.projectName='Recetas Seguras' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_07544b918e1e702ae9e26cdac1984b9f411c4806
```

**Ventajas**:
- Usa tu cache de Maven local (`~/.m2`)
- No descarga nada
- Súper rápido (30-60 segundos)

**Requisitos**:
- SonarQube debe estar corriendo: `./sonarqube-start.sh`
- Maven debe estar instalado en tu máquina

### Opción B: Script Rápido

Crea un archivo `sonar-scan-local.sh`:

```bash
#!/bin/bash

echo "=========================================="
echo "🔍 ANÁLISIS SONARQUBE (LOCAL - RÁPIDO)"
echo "=========================================="
echo ""

# Verificar que SonarQube esté corriendo
if ! docker ps | grep -q "sonarqube"; then
    echo "❌ Error: SonarQube no está corriendo"
    echo "   Ejecuta: ./sonarqube-start.sh"
    exit 1
fi

echo "✅ SonarQube activo"
echo "🚀 Ejecutando análisis desde host..."
echo ""

# Ejecutar desde tu máquina (usa cache local de Maven)
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=sumativa2 \
  -Dsonar.projectName='Recetas Seguras' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_07544b918e1e702ae9e26cdac1984b9f411c4806

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ ANÁLISIS COMPLETADO"
    echo "=========================================="
    echo ""
    echo "📊 Ver resultados en: http://localhost:9000/dashboard?id=sumativa2"
    echo ""
else
    echo ""
    echo "❌ Error en el análisis"
    echo ""
fi
```

Luego:
```bash
chmod +x sonar-scan-local.sh
./sonar-scan-local.sh
```

---

## Comparación de Tiempos

| Método | Primera Vez | Siguientes Veces |
|--------|-------------|------------------|
| **Docker sin cache** (antes) | 10 min | 10 min ⚠️ |
| **Docker con cache** (nueva) | 10 min | 1-2 min ✅ |
| **Maven local** (recomendado) | 1 min | 30 seg 🚀 |

---

## Verificar que el Cache Funciona

Después de recrear el contenedor, verifica que el volumen esté montado:

```bash
# Ver volúmenes del contenedor
docker inspect recetas_app | grep -A 10 "Mounts"

# Deberías ver algo como:
# "Destination": "/.m2",
# "Source": "/var/lib/docker/volumes/sumativa1-semana3_maven_cache/_data"
```

## Problema de Permisos en /.m2 (SOLUCIONADO)

**Error encontrado**: `Could not create local repository at /.m2/repository`

**Causa**: El volumen `maven_cache` montado en `/.m2` era propiedad de `root`, pero el contenedor ejecuta como usuario `spring`.

**Solución inmediata**:
```bash
# Cambiar permisos del directorio
docker exec -u root recetas_app sh -c "chown -R spring:spring /.m2"

# Ejecutar análisis
./sonar-scan.sh
```

**Solución permanente**: Se actualizó el Dockerfile (líneas 76-77) para crear `/.m2` con los permisos correctos al construir la imagen:

```dockerfile
RUN mkdir -p /home/spring/.m2 /.m2 /app/uploads && \
    chown -R spring:spring /app /home/spring/.m2 /.m2 /app/uploads
```

La próxima vez que reconstruyas la imagen (`docker-compose up -d --build`), no necesitarás cambiar permisos manualmente.

---

## Limpieza (Si Necesitas Espacio)

Si en el futuro quieres limpiar el cache de Maven:

```bash
# Detener contenedores
docker-compose down

# Eliminar volumen de Maven cache
docker volume rm sumativa1-semana3_maven_cache

# Reiniciar
docker-compose up -d
```

---

**Fecha**: 24 de Noviembre, 2025
**Mejora**: Cache de dependencias Maven para análisis SonarQube
**Tiempo ahorrado**: ~8 minutos por análisis
