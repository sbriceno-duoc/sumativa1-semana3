#!/bin/bash

echo "=========================================="
echo "🛑 DETENIENDO SONARQUBE"
echo "=========================================="
echo ""

docker-compose -f docker-compose.sonarqube.yml down

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ SonarQube detenido correctamente"
    echo ""
    echo "💾 Los datos se mantienen en volúmenes Docker"
    echo ""
    echo "🗑️  Para eliminar TODOS los datos (cuidado):"
    echo "   docker-compose -f docker-compose.sonarqube.yml down -v"
    echo ""
else
    echo "❌ Error al detener SonarQube"
    exit 1
fi
