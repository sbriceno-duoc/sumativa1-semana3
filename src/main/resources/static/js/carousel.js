/**
 * Carrusel de imágenes y videos para detalle de recetas
 * Este script maneja la navegación entre múltiples archivos multimedia
 */

(function() {
    'use strict';
    

    
    // Variables del carrusel
    let currentSlide = 0;
    let slides = null;
    let indicators = null;
    
    /**
     * Muestra un slide específico del carrusel
     */
    function showSlide(index) {
        if (!slides || slides.length === 0) {
            return;
        }
        
        // Ajustar índice (circular)
        if (index >= slides.length) {
            currentSlide = 0;
        } else if (index < 0) {
            currentSlide = slides.length - 1;
        } else {
            currentSlide = index;
        }
        

        
        // Pausar todos los videos
        document.querySelectorAll('.carousel-item video').forEach(function(video) {
            video.pause();
        });
        
        // Remover clase active de todos los slides
        slides.forEach(function(slide) {
            slide.classList.remove('active');
        });
        
        // Remover clase active de todos los indicadores
        if (indicators) {
            indicators.forEach(function(indicator) {
                indicator.classList.remove('active');
            });
        }
        
        // Activar slide actual
        if (slides[currentSlide]) {
            slides[currentSlide].classList.add('active');
        }
        
        // Activar indicador actual
        if (indicators && indicators[currentSlide]) {
            indicators[currentSlide].classList.add('active');
        }
    }
    
    /**
     * Mueve el carrusel en una dirección
     */
    function moveCarousel(direction) {
        
        showSlide(currentSlide + direction);
    }
    
    /**
     * Va directamente a un slide específico
     */
    function goToSlide(index) {
        
        showSlide(index);
    }
    
    /**
     * Maneja gestos de swipe en dispositivos táctiles
     */
    function handleSwipe(startX, endX) {
        const threshold = 50; // Umbral mínimo de movimiento
        
        if (endX < startX - threshold) {
            moveCarousel(1); // Swipe izquierda -> siguiente
        } else if (endX > startX + threshold) {
            moveCarousel(-1); // Swipe derecha -> anterior
        }
    }
    
    /**
     * Inicializa el carrusel
     */
    function initCarousel() {

        
        // Obtener elementos del DOM
        slides = document.querySelectorAll('.carousel-item');
        indicators = document.querySelectorAll('.carousel-indicators .indicator');
        

        
        if (!slides || slides.length === 0) {
            return;
        }
        
        // Botones de navegación
        const prevBtn = document.getElementById('carouselPrev');
        const nextBtn = document.getElementById('carouselNext');
        

        
        // Event listeners para botones

        if (prevBtn) {
            prevBtn.addEventListener('click', function(e) {
                e.preventDefault();
                moveCarousel(-1);
            });
        }

        if (nextBtn) {
            nextBtn.addEventListener('click', function(e) {
                e.preventDefault();
                moveCarousel(1);
            });
        }

        // Event listeners para indicadores
        if (indicators) {
            indicators.forEach(function(indicator, index) {
                indicator.addEventListener('click', function(e) {
                    e.preventDefault();
                    goToSlide(index);
                });
            });
        }
        
        // Soporte para teclado
        document.addEventListener('keydown', function(e) {
            if (e.key === 'ArrowLeft') {
                moveCarousel(-1);
            } else if (e.key === 'ArrowRight') {
                moveCarousel(1);
            }
        });
        
        // Soporte para gestos táctiles
        const carouselInner = document.querySelector('.carousel-inner');
        if (carouselInner) {
            let touchStartX = 0;
            let touchEndX = 0;
            
            carouselInner.addEventListener('touchstart', function(e) {
                touchStartX = e.changedTouches[0].screenX;
            }, { passive: true });
            
            carouselInner.addEventListener('touchend', function(e) {
                touchEndX = e.changedTouches[0].screenX;
                handleSwipe(touchStartX, touchEndX);
            }, { passive: true });
        }
        

    }
    
    // Inicializar cuando el DOM esté listo
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initCarousel);
    } else {
        initCarousel();
    }
    
})();
