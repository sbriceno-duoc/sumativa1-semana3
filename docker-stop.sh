#!/bin/bash

# Script para detener el entorno Docker del proyecto

echo "=========================================="
echo "🛑 DETENIENDO ENTORNO DOCKER"
echo "=========================================="
echo ""

# Detener los servicios
docker-compose down || docker compose down

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Servicios detenidos correctamente"
    echo ""
    echo "📝 Nota: Los datos de MySQL se mantienen en el volumen 'mysql_data'"
    echo ""
    echo "Para eliminar también los volúmenes (BORRA TODOS LOS DATOS):"
    echo "   docker-compose down -v"
    echo ""
else
    echo ""
    echo "❌ Error al detener los servicios"
    exit 1
fi

