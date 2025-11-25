// JS para mostrar/ocultar el modal de eliminación de receta
// Compatible con CSP (sin funciones inline onclick)

document.addEventListener('DOMContentLoaded', function() {
    console.log('[receta-modal.js] cargado correctamente');

    var modal = document.getElementById('modalEliminar');
    var btnEliminar = document.getElementById('btnEliminarReceta');
    var btnCancelar = document.getElementById('btnCancelarEliminar');

    if (!modal) {
        console.warn('[receta-modal.js] No se encontró el modalEliminar en el DOM');
        return;
    }

    // Event listener para mostrar el modal al hacer clic en "Eliminar Receta"
    if (btnEliminar) {
        btnEliminar.addEventListener('click', function(e) {
            e.preventDefault();
            modal.classList.add('active');
            console.log('[receta-modal.js] Modal de confirmación mostrado');
        });
    }

    // Event listener para ocultar el modal al hacer clic en "Cancelar"
    if (btnCancelar) {
        btnCancelar.addEventListener('click', function(e) {
            e.preventDefault();
            modal.classList.remove('active');
            console.log('[receta-modal.js] Modal de confirmación ocultado');
        });
    }

    // Cerrar modal al hacer clic fuera del contenido
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            modal.classList.remove('active');
            console.log('[receta-modal.js] Modal cerrado por clic fuera');
        }
    });

    // Cerrar modal con tecla ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && modal.classList.contains('active')) {
            modal.classList.remove('active');
            console.log('[receta-modal.js] Modal cerrado con tecla ESC');
        }
    });
});
