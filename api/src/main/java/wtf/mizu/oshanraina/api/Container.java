package wtf.mizu.oshanraina.api;

import org.jetbrains.annotations.NotNull;

/**
 * A compile-time-generated wrapper that implements certain traits that would
 * require expensive operations otherwise.
 * <p>
 * A <b>good and robust</b> example of using a {@link Container} is Mizu's
 * {@code Kawa} library, available on Maven Central.
 * <p>
 * Most of the libraries provided by our developers actually take advantage
 * of containers and implement powerful intermediates to avoid ASM/Reflection.
 *
 * @param <T> the wrapped object type.
 *
 * @author Shyrogan
 * @since 0.0.1
 */
public interface Container<T> {
    /**
     * The wrapped object getter.
     *
     * @return the wrapped object.
     */
    @NotNull T obj();
}
