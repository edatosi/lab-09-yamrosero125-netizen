package listas;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lista simplemente enlazada genérica.
 *
 * INSTRUCCIONES PARA EL ESTUDIANTE
 * --------------------------------
 * 1. Cada método marcado con "TODO" lanza UnsupportedOperationException.
 *    Reemplace esa línea por su implementación.
 * 2. NO modifique las pruebas (src/test).
 * 3. Resuelva en orden (Etapa 1, 2, 3...): cada etapa usa las anteriores.
 * 4. Solo se permite usar nodos propios (clase Nodo). No use
 *    java.util.LinkedList ni ArrayList dentro de esta clase.
 */
public class ListaEnlazada<T> {

    /** Primer nodo de la lista (null cuando la lista está vacía). */
    private Nodo<T> cabeza;

    /** Cantidad de elementos. Debe mantenerse actualizada en cada operación. */
    private int cantidad;

    public ListaEnlazada() {
        this.cabeza = null;
        this.cantidad = 0;
    }

    // =====================================================================
    // Métodos auxiliares (privados)
    // =====================================================================

    /**
     * Verifica que el índice corresponda a un elemento existente.
     *
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    private void validarIndice(int indice) {
        if (indice < 0 || indice >= this.cantidad) {
            throw new IndexOutOfBoundsException(
                    "Índice: " + indice + ", tamaño: " + this.cantidad);
        }
    }

    /**
     * Lanza una excepción si la lista no tiene elementos.
     *
     * @throws NoSuchElementException si la lista está vacía
     */
    private void validarNoVacia() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }
    }

    /**
     * Recorre la lista desde la cabeza y retorna el nodo de la posición
     * indicada. Se asume que el índice ya fue validado.
     */
    private Nodo<T> obtenerNodo(int indice) {
        Nodo<T> puntero = this.cabeza;
        for (int posicion = 0; posicion < indice; posicion++) {
            puntero = puntero.getSiguiente();
        }
        return puntero;
    }

    // =====================================================================
    // ETAPA 1. Estado de la lista
    // =====================================================================

    /** @return true si la lista no tiene elementos. */
    public boolean estaVacia() {
        return this.cantidad == 0;
    }

    /** @return número de elementos de la lista. */
    public int tamanio() {
        return this.cantidad;
    }

    // =====================================================================
    // ETAPA 2. Agregar en los extremos
    // =====================================================================

    /** Inserta el dato como nuevo primer elemento. */
    public void agregarAlInicio(T dato) {
        // Crear el nodo
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        // Enlazar el nuevo nodo a la cabeza actual
        nuevoNodo.setSiguiente(this.cabeza);
        // Actualizar la cabeza
        this.cabeza = nuevoNodo;
        // Aumentar cantidad
        this.cantidad++;
    }

    /** Inserta el dato como nuevo último elemento. */
    public void agregarAlFinal(T dato) {
        if (estaVacia()) {
            // Si está vacía, agregar al final es lo mismo que agregar al inicio
            agregarAlInicio(dato);
        } else {
            // Recorrer hasta el último nodo (el que no tiene siguiente)
            Nodo<T> puntero = this.cabeza;
            while (puntero.getSiguiente() != null) {
                puntero = puntero.getSiguiente();
            }
            // Crear el nuevo nodo y enlazarlo después del último
            Nodo<T> nuevoNodo = new Nodo<>(dato);
            puntero.setSiguiente(nuevoNodo);
            // Aumentar cantidad
            this.cantidad++;
        }
    }

    // =====================================================================
    // ETAPA 3. Consultar elementos
    // =====================================================================

    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerPrimero() {
        validarNoVacia();
        // El primer elemento siempre está en la cabeza
        return this.cabeza.getDato();
    }

    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerUltimo() {
        validarNoVacia();
        // El último nodo está en la posición cantidad - 1
        return obtenerNodo(this.cantidad - 1).getDato();
    }

    /**
     * @param indice posición (0 = primer elemento)
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T obtener(int indice) {
        validarIndice(indice);
        return obtenerNodo(indice).getDato();
    }

    // =====================================================================
    // ETAPA 4. Búsqueda
    // =====================================================================

    /** @return true si algún elemento es igual (equals) al dato. Admite null. */
    public boolean contiene(T dato) {
        // Si indiceDe encuentra una posición válida, el dato está en la lista
        return indiceDe(dato) != -1;
    }

    /** @return posición de la primera aparición del dato, o -1 si no está. */
    public int indiceDe(T dato) {
        Nodo<T> puntero = this.cabeza;
        int posicion = 0;
        while (puntero != null) {
            // Objects.equals compara con equals y también acepta valores null
            if (Objects.equals(puntero.getDato(), dato)) {
                return posicion;
            }
            puntero = puntero.getSiguiente();
            posicion++;
        }
        // Se recorrió toda la lista sin encontrar el dato
        return -1;
    }

    // =====================================================================
    // ETAPA 5. Insertar y reemplazar en cualquier posición
    // =====================================================================

    /**
     * Inserta el dato en la posición indicada, desplazando los siguientes.
     * Se permite indice == tamanio() (equivale a agregar al final).
     *
     * @throws IndexOutOfBoundsException si indice < 0 o indice > tamanio()
     */
    public void insertar(int indice, T dato) {
        // Aquí sí se permite indice == cantidad, por eso no se usa validarIndice
        if (indice < 0 || indice > this.cantidad) {
            throw new IndexOutOfBoundsException(
                    "Índice: " + indice + ", tamaño: " + this.cantidad);
        }
        if (indice == 0) {
            agregarAlInicio(dato);
        } else {
            // Detenerse en el nodo ANTERIOR a la posición donde va el nuevo
            Nodo<T> anterior = obtenerNodo(indice - 1);
            Nodo<T> nuevoNodo = new Nodo<>(dato);
            // El nuevo nodo apunta al que antes ocupaba esa posición
            nuevoNodo.setSiguiente(anterior.getSiguiente());
            // El anterior ahora apunta al nuevo nodo
            anterior.setSiguiente(nuevoNodo);
            this.cantidad++;
        }
    }

    /**
     * Reemplaza el dato de la posición indicada.
     *
     * @return el dato que había antes
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T reemplazar(int indice, T dato) {
        validarIndice(indice);
        Nodo<T> nodo = obtenerNodo(indice);
        // Guardar el valor viejo antes de sobrescribirlo
        T datoAnterior = nodo.getDato();
        nodo.setDato(dato);
        return datoAnterior;
    }

    // =====================================================================
    // ETAPA 6. Eliminar
    // =====================================================================

    /**
     * Elimina y retorna el primer elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarPrimero() {
        validarNoVacia();
        T datoEliminado = this.cabeza.getDato();
        // La nueva cabeza es el segundo nodo (o null si solo había uno)
        this.cabeza = this.cabeza.getSiguiente();
        this.cantidad--;
        return datoEliminado;
    }

    /**
     * Elimina y retorna el último elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarUltimo() {
        validarNoVacia();
        // Con un solo elemento, eliminar el último es eliminar el primero
        if (this.cantidad == 1) {
            return eliminarPrimero();
        }
        // Detenerse en el PENÚLTIMO nodo
        Nodo<T> penultimo = obtenerNodo(this.cantidad - 2);
        T datoEliminado = penultimo.getSiguiente().getDato();
        // El penúltimo pasa a ser el último
        penultimo.setSiguiente(null);
        this.cantidad--;
        return datoEliminado;
    }

    /**
     * Elimina y retorna el elemento de la posición indicada.
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T eliminar(int indice) {
        validarIndice(indice);
        if (indice == 0) {
            return eliminarPrimero();
        }
        // Detenerse en el nodo anterior al que se va a eliminar
        Nodo<T> anterior = obtenerNodo(indice - 1);
        Nodo<T> nodoEliminado = anterior.getSiguiente();
        // "Saltar" el nodo eliminado enlazando el anterior con el siguiente
        anterior.setSiguiente(nodoEliminado.getSiguiente());
        this.cantidad--;
        return nodoEliminado.getDato();
    }

    /**
     * Elimina la primera aparición del dato.
     * @return true si se eliminó algo, false si el dato no estaba
     */
    public boolean eliminarValor(T dato) {
        int posicion = indiceDe(dato);
        if (posicion == -1) {
            return false;
        }
        eliminar(posicion);
        return true;
    }

    /** Deja la lista vacía. */
    public void vaciar() {
        // Al perder la referencia a la cabeza, los nodos quedan sin uso
        this.cabeza = null;
        this.cantidad = 0;
    }

    // =====================================================================
    // ETAPA 7. Representación en texto
    // =====================================================================

    /**
     * Formato: "[1, 2, 3]". Lista vacía: "[]".
     * Un elemento null se imprime como "null".
     */
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("[");
        Nodo<T> puntero = this.cabeza;
        while (puntero != null) {
            // append convierte null en el texto "null"
            texto.append(puntero.getDato());
            // Solo se agrega la coma si hay un elemento después
            if (puntero.getSiguiente() != null) {
                texto.append(", ");
            }
            puntero = puntero.getSiguiente();
        }
        texto.append("]");
        return texto.toString();
    }

    // =====================================================================
    // ETAPA 8. Retos
    // =====================================================================

    /**
     * Invierte la lista EN SITIO: no cree nodos nuevos, solo cambie
     * las referencias 'siguiente' de los nodos existentes.
     */
    public void invertir() {
        Nodo<T> anterior = null;
        Nodo<T> actual = this.cabeza;
        while (actual != null) {
            // Guardar el siguiente antes de cambiar el enlace
            Nodo<T> siguiente = actual.getSiguiente();
            // Hacer que el nodo actual apunte hacia atrás
            actual.setSiguiente(anterior);
            // Avanzar las dos referencias un paso
            anterior = actual;
            actual = siguiente;
        }
        // Al terminar, 'anterior' es el antiguo último nodo: la nueva cabeza
        this.cabeza = anterior;
    }

    /**
     * Retorna el elemento del medio. Si el tamaño es par, el segundo de los
     * dos centrales (ej.: [1,2,3,4] -> 3). Pista: dos punteros, uno avanza
     * de a 1 y otro de a 2.
     *
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerMedio() {
        validarNoVacia();
        Nodo<T> lento = this.cabeza;
        Nodo<T> rapido = this.cabeza;
        // Cuando el rápido llega al final, el lento está en la mitad
        while (rapido != null && rapido.getSiguiente() != null) {
            lento = lento.getSiguiente();
            rapido = rapido.getSiguiente().getSiguiente();
        }
        return lento.getDato();
    }

    /**
     * Elimina los elementos repetidos dejando solo la primera aparición
     * de cada uno, conservando el orden. Ej.: [1,2,1,3,2] -> [1,2,3]
     */
    public void eliminarDuplicados() {
        Nodo<T> actual = this.cabeza;
        while (actual != null) {
            // Buscar en el resto de la lista los nodos iguales al actual
            Nodo<T> anterior = actual;
            while (anterior.getSiguiente() != null) {
                if (Objects.equals(anterior.getSiguiente().getDato(), actual.getDato())) {
                    // Saltar el nodo repetido
                    anterior.setSiguiente(anterior.getSiguiente().getSiguiente());
                    this.cantidad--;
                } else {
                    anterior = anterior.getSiguiente();
                }
            }
            actual = actual.getSiguiente();
        }
    }
}