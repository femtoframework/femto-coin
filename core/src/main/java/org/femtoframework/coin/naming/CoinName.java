package org.femtoframework.coin.naming;

import org.femtoframework.coin.CoinConstants;
import org.femtoframework.util.StringUtil;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Coin Name
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoinName implements Name, Externalizable {

    public static final CoinName EMPTY = new CoinName("");

    private int off = 0;

    private int size;

    protected List<String> components;

    private List<Character> separators;


    private transient String name;

    public CoinName() {
    }

    /**
     * Name
     *
     * @param name Name
     */
    public CoinName(String name) {
        this(name, CoinConstants.CHAR_DELIM);
    }

    public CoinName(String name, char delim) {
        if (StringUtil.isInvalid(name)) {
            this.components = Collections.emptyList();
            this.separators = Collections.emptyList();
            this.off = 0;
            this.size = 0;
        }
        else {
            this.components = new ArrayList<>();
            this.separators = new ArrayList<>();

            //First
            this.separators.add(' ');
            this.off = 0;

            int l = name.length();
            char ch;
            int s = 0;
            for (int i = 0; i < l; i++) {
                ch = name.charAt(i);
                if (ch == CoinConstants.CHAR_COLON) {
                    //COLON
                    components.add(name.substring(s, i));
                    separators.add(CoinConstants.CHAR_COLON);
                    s = i + 1;
                }
                else if (ch == delim) {
                    //SEP
                    components.add(name.substring(s, i));
                    separators.add(delim);
                    s = i + 1;
                }
            }
            if (s < l) {
                components.add(name.substring(s));
            }
            this.name = name;
            this.size = components.size();
        }
    }

    protected CoinName(List<String> components, List<Character> separators, int off, int size) {
        this.components = components;
        this.separators = separators;
        this.off = off;
        this.size = size;
    }

    /**
     * Create Coin Name by namespace and paths
     *
     * @param paths Paths
     */
    public CoinName(String... paths) {
        this(paths, 0);
    }

    /**
     * Create Coin Name by namespace and paths
     *
     * @param paths Paths
     */
    public CoinName(String[] paths, int start) {
        this.size = paths.length - start;
        this.components = new ArrayList<>(size);
        for(int i = start; i < paths.length; i ++) {
            this.components.add(paths[i]);
        }
        this.separators = new ArrayList<>(size);
        for(int i = 0; i < size; i ++) {
            separators.add('/');
        }
        this.off = 0;
    }

    protected CoinName(List<String> components, List<Character> separators) {
        this(components, separators, 0, components.size());
    }

    /**
     * Returns the number of components in this name.
     *
     * @return the number of components in this name
     */
    public int size() {
        return size;
    }

    /**
     * Determines whether this name is empty.
     * An empty name is one with zero components.
     *
     * @return true if this name is empty, false otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Generates a new copy of this name.
     * Subsequent changes to the components of this name will not
     * affect the new copy, and vice versa.
     *
     * @return a copy of this name
     * @see Object#clone()
     */
    @Override
    public Object clone() {
        return new CoinName(components, separators, off, size);
    }

    /**
     * Removes a component from this name.
     * The component of this name at the specified position is removed.
     * Components with indexes greater than this position
     * are shifted down (toward index 0) by one.
     *
     * @param posn the index of the component to remove.
     *             Must be in the range [0,size()).
     * @return the component removed (a String)
     * @throws ArrayIndexOutOfBoundsException if posn is outside the specified range
     * @throws javax.naming.InvalidNameException
     *                                        if deleting the component
     *                                        would violate the syntax rules of the name
     */
    public Object remove(int posn) throws InvalidNameException {
        String n = get(posn);
        if (posn == 0) {
            off++;
            size--;
        }
        else {
            List<String> comp = new ArrayList<String>(size);
            comp.addAll(components.subList(off, off + size));
            List<Character> sep = new ArrayList<Character>(size + 1);
            sep.addAll(separators.subList(off, off + size + 1));
            comp.remove(posn);
            sep.remove(posn + 1);
            this.components = comp;
            this.separators = sep;
        }
        name = null;
        return n;
    }

    /**
     * Compares this name with another name for order.
     * Returns a negative integer, zero, or a positive integer as this
     * name is less than, equal to, or greater than the given name.
     * <p/>
     * <p> As with <tt>Object.equals()</tt>, the notion of ordering for names
     * depends on the class that implements this interface.
     * For example, the ordering may be
     * based on lexicographical ordering of the name components.
     * Specific attributes of the name, such as how it treats case,
     * may affect the ordering.  In general, two names of different
     * classes may not be compared.
     *
     * @param obj the non-null object to compare against.
     * @return a negative integer, zero, or a positive integer as this name
     *         is less than, equal to, or greater than the given name
     * @throws ClassCastException if obj is not a <tt>Name</tt> of a
     *                            type that may be compared with this name
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Object obj) {
        return compareTo((Name)obj);
    }

    /**
     * Compares name to this NameImpl to determine ordering.
     * Takes into account syntactic properties such as
     * elimination of blanks, case-ignore, etc, if relevant.
     * <p/>
     * Note: using syntax of this NameImpl and ignoring
     * that of comparison target.
     */
    public int compareTo(Name name) {
        if (this == name) {
            return 0;
        }

        int len1 = size();
        int len2 = name.size();
        int n = Math.min(len1, len2);

        int index1 = 0, index2 = 0;

        while (n-- != 0) {
            String comp1 = get(index1++);
            String comp2 = name.get(index2++);

            int local = comp1.compareTo(comp2);
            if (local != 0) {
                return local;
            }
        }

        return len1 - len2;
    }

    /**
     * Retrieves a component of this name.
     *
     * @param posn the 0-based index of the component to retrieve.
     *             Must be in the range [0,size()).
     * @return the component at index posn
     * @throws ArrayIndexOutOfBoundsException if posn is outside the specified range
     */
    public String get(int posn) {
        if (posn >= size || posn < 0) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound:" + posn + " size:" + size);
        }
        int pos = off + posn;
        return (components.get(pos));
    }

    /**
     * Retrieves the components of this name as an enumeration
     * of strings.  The effect on the enumeration of updates to
     * this name is undefined.  If the name has zero components,
     * an empty (non-null) enumeration is returned.
     *
     * @return an enumeration of the components of this name, each a string
     */
    public Enumeration<String> getAll() {
        if (off == 0 && size == components.size()) {
            return Collections.enumeration(components);
        }
        else {
            return Collections.enumeration(components.subList(off, off + size));
        }
    }

    /**
     * Creates a name whose components consist of a prefix of the
     * components of this name.  Subsequent changes to
     * this name will not affect the name that is returned and vice versa.
     *
     * @param posn the 0-based index of the component at which to stop.
     *             Must be in the range [0,size()].
     * @return a name consisting of the components at indexes in
     *         the range [0,posn).
     * @throws ArrayIndexOutOfBoundsException if posn is outside the specified range
     */
    public Name getPrefix(int posn) {
        if (posn > size || posn < 0) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound:" + posn + " size:" + size);
        }
        return new CoinName(components, separators, off, posn);
    }

    /**
     * Creates a name whose components consist of a suffix of the
     * components in this name.  Subsequent changes to
     * this name do not affect the name that is returned and vice versa.
     *
     * @param posn the 0-based index of the component at which to start.
     *             Must be in the range [0,size()].
     * @return a name consisting of the components at indexes in
     *         the range [posn,size()).  If posn is equal to
     *         size(), an empty name is returned.
     * @throws ArrayIndexOutOfBoundsException if posn is outside the specified range
     */
    public Name getSuffix(int posn) {
        if (posn > size || posn < 0) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound:" + posn + " size:" + size);
        }
        int pos = off + posn;
        int newSize = size - posn;
        return new CoinName(components, separators, pos, newSize);
    }

    /**
     * Determines whether this name ends with a specified suffix.
     * A name <tt>n</tt> is a suffix if it is equal to
     * <tt>getSuffix(size()-n.size())</tt>.
     *
     * @param n the name to check
     * @return true if <tt>n</tt> is a suffix of this name, false otherwise
     */
    public boolean endsWith(Name n) {
        return n instanceof CoinName && endsWith(n.size(), (CoinName)n);
    }

    public boolean endsWith(int posn, CoinName suffix) {
        // posn is number of elements in suffix
        // startIndex is the starting position in this name
        // at which to start the comparison. It is calculated by
        // subtracting 'posn' from size()
        int startIndex = size() - posn;
        if (startIndex < 0 || startIndex > size()) {
            return false;
        }
        int from = off + startIndex;
        int to = off + size;
        int sFrom = suffix.off;
        List comp = suffix.components;
        for (int i = from, j = sFrom; i < to; i++, j++) {
            String my = components.get(i);
            String his = (String)comp.get(j);
            if (!(my.equals(his))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether this name starts with a specified prefix.
     * A name <tt>n</tt> is a prefix if it is equal to
     * <tt>getPrefix(n.size())</tt>.
     *
     * @param n the name to check
     * @return true if <tt>n</tt> is a prefix of this name, false otherwise
     */
    public boolean startsWith(Name n) {
        return n instanceof CoinName && (startsWith(n.size(), (CoinName)n));
    }

    public boolean startsWith(int posn, CoinName prefix) {
        if (posn < 0 || posn > size()) {
            return false;
        }
        int from = off;
        int to = off + posn;
        int pFrom = prefix.off;
        List comp = prefix.components;
        for (int i = from, j = pFrom; i < to; i++, j++) {
            String my = components.get(i);
            String his = (String)comp.get(j);
            if (!(my.equals(his))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a single component at a specified position within this name.
     * Components of this name at or after the index of the new component
     * are shifted up by one (away from index 0) to accommodate the new
     * component.
     *
     * @param comp the component to add
     * @param posn the index at which to add the new component.
     *             Must be in the range [0,size()].
     * @return the updated name (not a new one)
     * @throws ArrayIndexOutOfBoundsException if posn is outside the specified range
     * @throws javax.naming.InvalidNameException
     *                                        if adding <tt>comp</tt> would violate
     *                                        the syntax rules of this name
     */
    public Name add(int posn, String comp) {
        throw new UnsupportedOperationException("Unsupported");
    }

    /**
     * Adds a single component to the end of this name.
     *
     * @param comp the component to add
     * @return the updated name (not a new one)
     * @throws javax.naming.InvalidNameException
     *          if adding <tt>comp</tt> would violate
     *          the syntax rules of this name
     */
    public Name add(String comp) {
        if (components == Collections.EMPTY_LIST) {
            components = new ArrayList<>();
            separators = new ArrayList<>();
        }
        components.add(comp);
        separators.add(CoinConstants.CHAR_DELIM);
        size++;
        name = null;
        return this;
    }

    /**
     * Adds the components of a name -- in order -- at a specified position
     * within this name.
     * Components of this name at or after the index of the first new
     * component are shifted up (away from 0) to accommodate the new
     * components.
     *
     * @param n    the components to add
     * @param posn the index in this name at which to add the new
     *             components.  Must be in the range [0,size()].
     * @return the updated name (not a new one)
     * @throws ArrayIndexOutOfBoundsException if posn is outside the specified range
     * @throws javax.naming.InvalidNameException
     *                                        if <tt>n</tt> is not a valid name,
     *                                        or if the addition of the components would violate the syntax
     *                                        rules of this name
     */
    public Name addAll(int posn, Name n) {
        throw new UnsupportedOperationException("Unsupported");
    }

    /**
     * Adds the components of a name -- in order -- to the end of this name.
     *
     * @param suffix the components to add
     * @return the updated name (not a new one)
     * @throws javax.naming.InvalidNameException
     *          if <tt>suffix</tt> is not a valid name,
     *          or if the addition of the components would violate the syntax
     *          rules of this name
     */
    public Name addAll(Name suffix) {
        if (components == Collections.EMPTY_LIST) {
            components = new ArrayList<>();
            separators = new ArrayList<>();
        }
        else if (components.size() > size) {
            ArrayList<String> components = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
                components.add(this.components.get(i));
            }
            ArrayList<Character> separators = new ArrayList<Character>();
            for (int i = 0; i < size; i++) {
                separators.add(this.separators.get(i));
            }
            this.components = components;
            this.separators = separators;
        }
        int s = suffix.size();
        for (int i = 0; i < s; i++) {
            components.add(suffix.get(i));
            separators.add(CoinConstants.CHAR_DELIM);
        }
        size += s;
        name = null;
        return this;
    }

    public int hashCode() {
        int hash = 0;
        int to = off + size;
        for (int i = 0; i < to; i++) {
            String comp = components.get(i);
            hash += comp.hashCode();
        }
        return hash;
    }

    public String toString() {
        if (name != null) {
            return name;
        }
        if (size == 0) {
            return "";
        }
        StringBuilder answer = new StringBuilder();
        String comp;
        boolean compsAllEmpty = true;

        int end = off + size;
        for (int i = off; i < end; i++) {
            comp = components.get(i);
            if (i != off) {
                answer.append((separators.get(i)).charValue());
            }
            if (comp.length() > 0) {
                compsAllEmpty = false;
            }
            answer = answer.append(comp);
        }
        if (compsAllEmpty && (size >= 1)) {
            answer = answer.append(CoinConstants.CHAR_DELIM);
        }
        name = (answer.toString());
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof CoinName) {
            CoinName target = (CoinName)obj;
            if (target.size() == this.size()) {
                int from = off;
                int to = off + size;
                int tFrom = target.off;
                List comp = target.components;
                for (int i = from, j = tFrom; i < to; i++, j++) {
                    String my = components.get(i);
                    String his = (String)comp.get(j);
                    if (!(my.equals(his))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeExternal(ObjectOutput oos) throws IOException {
        oos.writeInt(off);
        oos.writeInt(size);
        oos.writeObject(components);
        oos.writeObject(separators);
    }

    @Override
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        off = ois.readInt();
        size = ois.readInt();
        components = (List<String>)ois.readObject();
        separators = (List<Character>)ois.readObject();
    }
}
