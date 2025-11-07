#!/bin/bash

# Script para iniciar el entorno Docker del proyecto

echo "=========================================="
echo "🐳 INICIANDO ENTORNO DOCKER"
echo "=========================================="
echo ""

# Verificar que Docker esté instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Error: Docker no está instalado"
    echo "Por favor, instala Docker Desktop desde: https://www.docker.com/products/docker-desktop"
    exit 1
fi

# Verificar que Docker Compose esté disponible
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo "❌ Error: Docker Compose no está disponible"
    exit 1
fi

echo "✅ Docker está instalado y disponible"
echo ""

# Detener contenedores existentes si los hay
echo "🛑 Deteniendo contenedores existentes..."
docker-compose down 2>/dev/null || docker compose down 2>/dev/null
echo ""

# Iniciar los servicios
echo "🚀 Iniciando servicios con Docker Compose..."
docker-compose up -d || docker compose up -d

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
    echo "⏳ Esperando a que MySQL esté listo..."
    sleep 10
    echo ""
    echo "📝 Para ver los logs:"
    echo "   docker-compose logs -f mysql"
    echo ""
    echo "🛑 Para detener los servicios:"
    echo "   ./docker-stop.sh"
    echo "   o"
    echo "   docker-compose down"
    echo ""
    echo "=========================================="
    echo ""
    echo "✅ Ahora puedes iniciar la aplicación Spring Boot:"
    echo "   mvn spring-boot:run -Dspring-boot.run.profiles=docker"
    echo ""
    echo "🌐 La aplicación estará disponible en:"
    echo "   http://localhost:8082"
    echo ""
else
    echo ""
    echo "❌ Error al iniciar los servicios"
    echo "Verifica los logs con: docker-compose logs"
    exit 1
fi

