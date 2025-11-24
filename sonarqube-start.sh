#!/bin/bash

echo "=========================================="
echo "🔍 INICIANDO SONARQUBE"
echo "=========================================="
echo ""

if ! command -v docker &> /dev/null; then
    echo "❌ Error: Docker no está instalado"
    exit 1
fi

echo "🚀 Iniciando SonarQube con PostgreSQL..."
docker-compose -f docker-compose.sonarqube.yml up -d

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ SONARQUBE INICIADO CORRECTAMENTE"
    echo "=========================================="
    echo ""
    echo "⏳ Esperando a que SonarQube esté listo (puede tardar 2-3 minutos)..."
    echo ""
    
    for i in {1..60}; do
        if curl -s http://localhost:9000/api/system/status | grep -q "UP"; then
            echo ""
            echo "✅ SonarQube está listo!"
            break
        fi
        echo -n "."
        sleep 5
    done
    
    echo ""
    echo ""
    echo "🌐 Accede a SonarQube en: http://localhost:9000"
    echo ""
    echo "🔐 Credenciales por defecto:"
    echo "   Usuario: admin"
    echo "   Contraseña: admin"
    echo "   (Se te pedirá cambiar la contraseña en el primer login)"
    echo ""
    echo "📊 Para analizar el proyecto:"
    echo "   mvn clean verify sonar:sonar \\"
    echo "     -Dsonar.projectKey=recetas-seguras \\"
    echo "     -Dsonar.host.url=http://localhost:9000 \\"
    echo "     -Dsonar.login=<token>"
    echo ""
    echo "🛑 Para detener:"
    echo "   ./sonarqube-stop.sh"
    echo "   o"
    echo "   docker-compose -f docker-compose.sonarqube.yml down"
    echo ""
else
    echo "❌ Error al iniciar SonarQube"
    exit 1
fi
