#!/bin/bash

# Script para resetear completamente el entorno Docker (incluye borrado de datos)

echo "=========================================="
echo "⚠️  RESETEO COMPLETO DEL ENTORNO DOCKER"
echo "=========================================="
echo ""
echo "⚠️  ADVERTENCIA: Esto eliminará TODOS los datos de la base de datos"
echo ""
read -p "¿Estás seguro de continuar? (yes/no): " confirm

if [ "$confirm" != "yes" ]; then
    echo "❌ Operación cancelada"
    exit 0
fi

echo ""
echo "🛑 Deteniendo servicios..."
docker-compose down -v || docker compose down -v

echo ""
echo "🗑️  Eliminando volúmenes..."
docker volume rm sumativa_1_semana_3_mysql_data 2>/dev/null || true

echo ""
echo "🚀 Iniciando servicios con base de datos limpia..."
docker-compose up -d || docker compose up -d

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ ENTORNO RESETEADO CORRECTAMENTE"
    echo "=========================================="
    echo ""
    echo "⏳ Esperando a que MySQL inicialice..."
    sleep 15
    echo ""
    echo "✅ Base de datos creada con datos iniciales"
    echo ""
    echo "Ahora puedes iniciar la aplicación:"
    echo "   mvn spring-boot:run -Dspring-boot.run.profiles=docker"
    echo ""
else
    echo ""
    echo "❌ Error al resetear el entorno"
    exit 1
fi

