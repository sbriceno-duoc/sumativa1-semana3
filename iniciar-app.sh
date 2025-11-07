#!/bin/bash

# Script para iniciar correctamente la aplicación Spring Boot con Docker

echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║     INICIANDO APLICACIÓN RECETAS SEGURAS CON DOCKER       ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Verificar Docker
echo "🐳 Verificando contenedor MySQL..."
if docker ps | grep "recetas_mysql" > /dev/null; then
    echo "   ✅ MySQL Docker está corriendo"
else
    echo "   ⚠️  MySQL Docker no está corriendo"
    echo "   Iniciando Docker..."
    ./docker-start.sh
fi

echo ""
echo "🔨 Compilando aplicación..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "   ✅ Compilación exitosa"
else
    echo "   ❌ Error en compilación"
    exit 1
fi

echo ""
echo "🚀 Iniciando aplicación con perfil Docker..."
echo ""
echo "════════════════════════════════════════════════════════════"
echo ""
echo "⚠️  IMPORTANTE: La aplicación correrá en el puerto 8082"
echo ""
echo "📝 Accede a:"
echo "   🌐 http://localhost:8082"
echo ""
echo "❌ NO usar: http://localhost:8080 (ese es Apache)"
echo ""
echo "🔐 Credenciales:"
echo "   admin / admin123"
echo "   usuario1 / usuario123"
echo ""
echo "🛑 Para detener: Presiona Ctrl+C"
echo ""
echo "════════════════════════════════════════════════════════════"
echo ""

# Iniciar con perfil Docker
mvn spring-boot:run -Dspring-boot.run.profiles=docker

