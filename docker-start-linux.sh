#!/bin/bash

# Script para iniciar el entorno Docker en Linux con manejo de errores de DNS

echo "=========================================="
echo "🐳 INICIANDO ENTORNO DOCKER (Linux)"
echo "=========================================="
echo ""

# Verificar que Docker esté instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Error: Docker no está instalado"
    echo "Instala Docker siguiendo: https://docs.docker.com/engine/install/"
    exit 1
fi

echo "✅ Docker está instalado y disponible"

# Verificar conectividad a internet
echo "🔍 Verificando conectividad..."
if ! ping -c 1 8.8.8.8 &> /dev/null; then
    echo "⚠️  Advertencia: No hay conectividad a internet"
    echo "Por favor, verifica tu conexión de red"
    exit 1
fi

# Verificar resolución DNS
echo "🔍 Verificando DNS..."
if ! nslookup archive.apache.org &> /dev/null; then
    echo "⚠️  Advertencia: Problema con resolución DNS"
    echo ""
    echo "Solución rápida:"
    echo "1. Edita /etc/docker/daemon.json (puede requerir sudo)"
    echo "2. Agrega: {\"dns\": [\"8.8.8.8\", \"8.8.4.4\"]}"
    echo "3. Ejecuta: sudo systemctl restart docker"
    echo ""
    echo "¿Deseas continuar de todos modos? (s/n)"
    read -r respuesta
    if [[ ! "$respuesta" =~ ^[Ss]$ ]]; then
        exit 1
    fi
fi

echo "✅ Conectividad verificada"
echo ""

# Detectar comando de docker-compose
if command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE="docker-compose"
elif docker compose version &> /dev/null; then
    DOCKER_COMPOSE="docker compose"
else
    echo "❌ Error: Docker Compose no está disponible"
    echo "Instala Docker Compose siguiendo: https://docs.docker.com/compose/install/"
    exit 1
fi

echo "✅ Usando: $DOCKER_COMPOSE"
echo ""

# Detener contenedores existentes si los hay
echo "🛑 Deteniendo contenedores existentes..."
$DOCKER_COMPOSE down 2>/dev/null
echo ""

# Limpiar caché de build si existe un argumento --clean
if [[ "$1" == "--clean" ]]; then
    echo "🧹 Limpiando caché de Docker..."
    docker builder prune -f
    echo ""
fi

# Iniciar los servicios con mejor manejo de red
echo "🚀 Construyendo e iniciando servicios..."
echo "   (Esto puede tomar varios minutos la primera vez)"
echo ""

# Intentar diferentes estrategias de build
BUILD_SUCCESS=false

# Estrategia 1: Build con network=host y DNS personalizados
echo "Intentando build con network=host..."
if DOCKER_BUILDKIT=1 $DOCKER_COMPOSE build \
    --build-arg BUILDKIT_INLINE_CACHE=1 \
    --network=host 2>/dev/null; then
    echo "✅ Build completado con network=host"
    BUILD_SUCCESS=true
fi

# Estrategia 2: Build estándar
if [ "$BUILD_SUCCESS" = false ]; then
    echo "⚠️  Intentando build estándar..."
    if DOCKER_BUILDKIT=1 $DOCKER_COMPOSE build --build-arg BUILDKIT_INLINE_CACHE=1 2>/dev/null; then
        echo "✅ Build completado con método estándar"
        BUILD_SUCCESS=true
    fi
fi

# Estrategia 3: Build con docker directamente usando DNS
if [ "$BUILD_SUCCESS" = false ]; then
    echo "⚠️  Intentando con configuración DNS explícita..."
    if docker build --network=host \
        --dns 8.8.8.8 \
        --dns 8.8.4.4 \
        -t sumativa1-semana3-app \
        -f Dockerfile . 2>/dev/null; then
        echo "✅ Build completado con DNS explícito"
        BUILD_SUCCESS=true
    fi
fi

if [ "$BUILD_SUCCESS" = false ]; then
    echo ""
    echo "❌ Error al construir las imágenes"
    echo ""
    echo "Posibles soluciones:"
    echo "1. Verifica tu conexión a internet: ping 8.8.8.8"
    echo "2. Configura DNS en Docker daemon.json:"
    echo "   sudo nano /etc/docker/daemon.json"
    echo "   Agrega: {\"dns\": [\"8.8.8.8\", \"8.8.4.4\"]}"
    echo "3. Reinicia Docker: sudo systemctl restart docker"
    echo "4. Ver guía completa: cat TROUBLESHOOTING_LINUX.md"
    exit 1
fi

# Iniciar los contenedores
echo ""
echo "🚀 Iniciando contenedores..."
$DOCKER_COMPOSE up -d

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ SERVICIOS INICIADOS CORRECTAMENTE"
    echo "=========================================="
    echo ""
    echo "📦 Contenedores activos:"
    docker ps --filter "name=recetas_" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    echo ""
    echo "🗄️  MySQL disponible en:"
    echo "   - Host: localhost"
    echo "   - Puerto: 3306"
    echo "   - Base de datos: recetas_db"
    echo "   - Usuario: recetas_user"
    echo "   - Contraseña: recetas_pass"
    echo ""
    echo "⏳ Esperando a que los servicios estén listos..."
    sleep 15
    echo ""
    
    # Verificar estado de salud
    echo "🔍 Verificando estado de los servicios..."
    mysql_health=$(docker inspect recetas_mysql --format='{{.State.Health.Status}}' 2>/dev/null || echo "unknown")
    app_health=$(docker inspect recetas_app --format='{{.State.Health.Status}}' 2>/dev/null || echo "unknown")
    
    echo "   MySQL: $mysql_health"
    echo "   App: $app_health"
    echo ""
    
    if [[ "$app_health" == "healthy" || "$app_health" == "starting" ]]; then
        echo "✅ La aplicación está iniciando correctamente"
        echo ""
        echo "🌐 Accede a la aplicación en:"
        echo "   http://localhost:8082"
        echo ""
        echo "👥 Credenciales:"
        echo "   admin / admin123"
        echo "   usuario1 / usuario123"
        echo ""
    else
        echo "⚠️  La aplicación puede estar iniciando todavía"
        echo "   Verifica los logs con: docker logs recetas_app"
    fi
    
    echo "📝 Comandos útiles:"
    echo "   Ver logs: $DOCKER_COMPOSE logs -f"
    echo "   Ver logs de app: docker logs -f recetas_app"
    echo "   Ver logs de MySQL: docker logs -f recetas_mysql"
    echo "   Detener servicios: $DOCKER_COMPOSE down"
    echo ""
    echo "=========================================="
else
    echo ""
    echo "❌ Error al iniciar los servicios"
    echo "Verifica los logs con: $DOCKER_COMPOSE logs"
    echo "O consulta: TROUBLESHOOTING_LINUX.md"
    exit 1
fi
