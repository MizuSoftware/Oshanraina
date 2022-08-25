package wtf.mizu.oshanraina;

/**
 * A {@link Container} is wrapper generated at compile-time that implements
 * certain traits that would require expensive operations otherwise.
 * <p>
 * A <b>good and robust example</b> of {@link Container} usage is Mizu's
 * {@code Kawa} library.
 * <p>
 * Most of the libraries provided by our developers actually take advantage
 * of containers and implement powerful intermediate to avoid ASM/Reflection.
 *
 * @param <T> the object wrapped type
 */
public interface Container<T> {

    /**
     * Returns the object wrapped by this {@link Container}.
     *
     * @return the wrapped object
     */
    T obj();

}
