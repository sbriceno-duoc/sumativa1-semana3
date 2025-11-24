/**
 * Funcionalidad de compartir recetas en redes sociales
 * Compatible con CSP (Content Security Policy)
 */

(function() {
    'use strict';

    /**
     * Obtiene la URL actual de la receta
     */
    function getCurrentUrl() {
        return encodeURIComponent(window.location.href);
    }

    /**
     * Obtiene el título de la página
     */
    function getPageTitle() {
        return encodeURIComponent(document.title);
    }

    /**
     * Obtiene la descripción de la receta
     */
    function getRecipeDescription() {
        const descriptionElement = document.querySelector('.receta-descripcion p');
        if (descriptionElement) {
            return encodeURIComponent(descriptionElement.textContent.substring(0, 200) + '...');
        }
        return getPageTitle();
    }

    /**
     * Obtiene la URL de la imagen principal de la receta
     */
    function getRecipeImage() {
        const imageElement = document.querySelector('.carousel-media.receta-imagen, .receta-imagen-principal img');
        if (imageElement) {
            const imgSrc = imageElement.src;
            // Convertir URL relativa a absoluta
            const absoluteUrl = new URL(imgSrc, window.location.origin).href;
            return encodeURIComponent(absoluteUrl);
        }
        return '';
    }

    /**
     * Comparte en Facebook
     */
    function shareOnFacebook() {
        const url = getCurrentUrl();
        const shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${url}`;
        openShareWindow(shareUrl, 'Facebook');
    }

    /**
     * Comparte en Twitter (X)
     */
    function shareOnTwitter() {
        const url = getCurrentUrl();
        const text = getPageTitle();
        const shareUrl = `https://twitter.com/intent/tweet?url=${url}&text=${text}`;
        openShareWindow(shareUrl, 'Twitter');
    }

    /**
     * Comparte en WhatsApp
     */
    function shareOnWhatsApp() {
        const url = getCurrentUrl();
        const text = getPageTitle();
        const shareUrl = `https://wa.me/?text=${text}%20${url}`;
        openShareWindow(shareUrl, 'WhatsApp');
    }

    /**
     * Comparte en Pinterest
     */
    function shareOnPinterest() {
        const url = getCurrentUrl();
        const description = getRecipeDescription();
        const media = getRecipeImage();
        const shareUrl = `https://pinterest.com/pin/create/button/?url=${url}&description=${description}&media=${media}`;
        openShareWindow(shareUrl, 'Pinterest');
    }

    /**
     * Abre una ventana emergente para compartir
     */
    function openShareWindow(url, title) {
        const width = 600;
        const height = 400;
        const left = (window.innerWidth - width) / 2;
        const top = (window.innerHeight - height) / 2;

        const features = `width=${width},height=${height},left=${left},top=${top},toolbar=0,menubar=0,location=0,status=0,scrollbars=1,resizable=1`;

        window.open(url, `Compartir en ${title}`, features);
    }

    /**
     * Función nativa de compartir (Web Share API)
     * Funciona en móviles y navegadores modernos
     */
    function nativeShare() {
        if (navigator.share) {
            const title = document.title;
            const url = window.location.href;
            const text = document.querySelector('.receta-descripcion p')?.textContent.substring(0, 200) || title;

            navigator.share({
                title: title,
                text: text,
                url: url
            })
            .then(() => console.log('Compartido exitosamente'))
            .catch((error) => {
                console.log('Error al compartir:', error);
                // Si falla, mostrar opciones manuales
                showShareOptions();
            });
        } else {
            // Si no soporta Web Share API, mostrar opciones manuales
            showShareOptions();
        }
    }

    /**
     * Muestra las opciones de compartir si Web Share API no está disponible
     */
    function showShareOptions() {
        const shareButtons = document.querySelector('.share-buttons');
        if (shareButtons) {
            shareButtons.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    }

    /**
     * Copia el enlace al portapapeles
     */
    function copyLink() {
        const url = window.location.href;

        if (navigator.clipboard && navigator.clipboard.writeText) {
            navigator.clipboard.writeText(url)
                .then(() => {
                    showNotification('Enlace copiado al portapapeles');
                })
                .catch((err) => {
                    console.error('Error al copiar:', err);
                    fallbackCopyToClipboard(url);
                });
        } else {
            fallbackCopyToClipboard(url);
        }
    }

    /**
     * Método alternativo para copiar al portapapeles
     */
    function fallbackCopyToClipboard(text) {
        const textArea = document.createElement('textarea');
        textArea.value = text;
        textArea.style.position = 'fixed';
        textArea.style.left = '-999999px';
        document.body.appendChild(textArea);
        textArea.select();

        try {
            document.execCommand('copy');
            showNotification('Enlace copiado al portapapeles');
        } catch (err) {
            console.error('Error al copiar:', err);
            showNotification('No se pudo copiar el enlace', 'error');
        }

        document.body.removeChild(textArea);
    }

    /**
     * Muestra una notificación temporal
     */
    function showNotification(message, type = 'success') {
        // Remover notificaciones anteriores
        const oldNotification = document.querySelector('.share-notification');
        if (oldNotification) {
            oldNotification.remove();
        }

        const notification = document.createElement('div');
        notification.className = `share-notification ${type}`;
        notification.textContent = message;

        document.body.appendChild(notification);

        // Mostrar con animación
        setTimeout(() => {
            notification.classList.add('show');
        }, 10);

        // Ocultar después de 3 segundos
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => {
                notification.remove();
            }, 300);
        }, 3000);
    }

    /**
     * Inicializa los event listeners
     */
    function init() {
        // Botón de Facebook
        const facebookBtn = document.querySelector('.share-btn.facebook');
        if (facebookBtn) {
            facebookBtn.addEventListener('click', shareOnFacebook);
        }

        // Botón de Twitter
        const twitterBtn = document.querySelector('.share-btn.twitter');
        if (twitterBtn) {
            twitterBtn.addEventListener('click', shareOnTwitter);
        }

        // Botón de WhatsApp
        const whatsappBtn = document.querySelector('.share-btn.whatsapp');
        if (whatsappBtn) {
            whatsappBtn.addEventListener('click', shareOnWhatsApp);
        }

        // Botón de Pinterest
        const pinterestBtn = document.querySelector('.share-btn.pinterest');
        if (pinterestBtn) {
            pinterestBtn.addEventListener('click', shareOnPinterest);
        }

        // Botón de copiar enlace (si existe)
        const copyLinkBtn = document.querySelector('.share-btn.copy-link');
        if (copyLinkBtn) {
            copyLinkBtn.addEventListener('click', copyLink);
        }

        // Botón de compartir nativo (si existe)
        const nativeShareBtn = document.querySelector('.share-btn.native-share');
        if (nativeShareBtn) {
            // Mostrar solo si el navegador soporta Web Share API
            if (navigator.share) {
                nativeShareBtn.style.display = 'inline-block';
            }
            nativeShareBtn.addEventListener('click', nativeShare);
        }
    }

    // Inicializar cuando el DOM esté listo
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    // Exponer funciones globalmente para uso en HTML si es necesario
    window.RecetasShare = {
        facebook: shareOnFacebook,
        twitter: shareOnTwitter,
        whatsapp: shareOnWhatsApp,
        pinterest: shareOnPinterest,
        copyLink: copyLink,
        nativeShare: nativeShare
    };

})();
